(load "huffman.scm")

;;Oppgave 1
;; a
(define (p-cons x y)
  (lambda (proc) (proc x y)))

(define (p-car proc)
  (proc (lambda (x y) x)))

(define (p-cdr proc)
  (proc (lambda (x y) y)))

;; b
(define foo 30)

((lambda (x y)
   (+ foo x y))
 foo 20)

((lambda (foo)
   ((lambda (x y)
      (+ foo x y))
    foo 20))
 10)

;; c
;; Resultatet blir (6 -4 21 1/2)
;; Det første som skjer er at listene a1, a2 og a3 blir bundet til henholdsvis x, y og z.
;; Deretter brukes disse verdiene til å utføre regneoperasjoner, som er grunnen til at rekkefølgen må stokkes om i den siste parantesen, grunnet schemes prefiksnotasjon.
;; Regneoperasjonene blir altså (+ 1 5), (- 2 6), (* 3 7) og (/ 4 8) i den rekkefølgen.
;; Uten map kan denne kalles ved f.eks. følgende prosedyre
((lambda (x y z) (y x z))
 1 + 3)
;; som vil gi resultatet 4 (fordi (+ 1 3) = 4)


;;Oppgave 2
;; a
(define (member? item items)
  (cond ((null? items) #f)
        ((eq? item (car items)) #t)
        ( else (member? item (cdr items)))))

;; b
;; Vi trenger å beholde en peker til rotnoden av treet så man kan resette dekodingen til roten for hver bit.
;; Derfor trenger vi den indre definisjonen som gir oss en annen peker som vi kan bruke til traversere treet.
;; Dersom vi skriver om prosedyren for å fjerne den indre definisjonen vil vi måtte kalle på tree i stedet for
;; current-branch. Dette medfører at next-branch alltid vil returnere en gren som har dybde 1, fordi pekeren
;; til tree aldri endres.

;; c
(define (decode bits tree)
  (define (decode-1 bits current-branch message)
    (if (null? bits)
        message
        (let ((next-branch
               (choose-branch (car bits) current-branch)))
          (if (leaf? next-branch)
              (decode-1 (cdr bits) tree (append message (list (symbol-leaf next-branch))))
              (decode-1 (cdr bits) next-branch message)))))
  (decode-1 bits tree '()))

;; d
(decode sample-code sample-tree)
;;> (ninjas fight ninjas by night)

;; e
(define (encode message tree)
  (define (encode-1 message current-branch bits)
    (if (null? message)
        bits
        (cond ((and (member? (car message) (symbols (left-branch current-branch))) (leaf? (left-branch current-branch)))
               (encode-1 (cdr message) tree (append bits (list 0))))
              ((and (member? (car message) (symbols (right-branch current-branch))) (leaf? (right-branch current-branch)))
               (encode-1 (cdr message) tree (append bits (list 1))))
              ((member? (car message) (symbols (left-branch current-branch)))
               (encode-1 message (left-branch current-branch) (append bits (list 0))))
              ((member? (car message) (symbols (right-branch current-branch)))
               (encode-1 message (right-branch current-branch) (append bits (list 1))))))) 
  (encode-1 message tree '()))

;; f
(define (grow-huffman-tree cells)
  (define (merge cells)
    (if (= (length cells) 2)
        (make-code-tree (car cells) (cadr cells))
        (merge (adjoin-set (make-code-tree (car cells) (cadr cells)) (cddr cells)))))
  (merge (make-leaf-set cells)))

(define freqs '((a 2) (b 5) (c 1) (d 3) (e 1) (f 3)))
(define codebook (grow-huffman-tree freqs))
(decode (encode '(a b c) codebook) codebook)

;; g
(define pairs '((ninjas 57) (samurais 20) (fight 45) (night 12) (hide 3) (in 2) (ambush 2) (defeat 1) (the 5) (sword 4) (by 12) (assassin 1) (river 2) (forest 1) (wait 1) (poison 1)))
(define codebook2 (grow-huffman-tree pairs))

(define words '(ninjas fight ninjas fight ninjas ninjas fight samurais samurais fight samurais fight ninjas ninjas fight by night))

(length (encode words codebook2))
;;40 bits

(/ (length (encode words codebook2))
   (length words))
;; Denne oppgir gjennomsnittlig lengde som 2 6/17

;; For å uttrykke alfabetet som en fixed length code må man kunne uttrykke 17 ulike ord. Dette krever 5 bit per ord.
;; 5 bit * 17 ord blir da 85 bit for å uttrykke hele meldingen

;; h
(define (huffman-leaves tree)
  (cond ((null? tree)
         '())
        ((leaf? tree)
         (list (list (symbol-leaf tree) (weight-leaf tree))))
        (else
          (append (huffman-leaves (left-branch tree))
                 (huffman-leaves (right-branch tree))))))

;; i






