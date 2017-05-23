;; Uten å være på gruppe med dem så har jeg i denne obligen jobbet en del sammen med bjornife, eirikfl, vegan som jobber i gruppe på devilry

;; Oppgave 1
;; a
(define (make-counter)
  (let ((count 0))
    (lambda ()
      (set! count (+ count 1))
      count)))

;; tester
(define count 42)
(define count 42)
(define c1 (make-counter))
(define c2 (make-counter))
(display "c1 -> ")(c1) ;; -> 1
(display "c1 -> ")(c1) ;; -> 2
(display "c1 -> ")(c1) ;; -> 3
(display "count -> ")count ;; -> 42
(display "c2 -> ")(c2) ;; -> 1

;; b
;; se vedlagt jpg

;; Oppgave 2
;; a
(define (make-stack items)
  (lambda (proc . args)
    (cond ((eq? proc 'pop!)
           (if (not (null? items))
               (set! items (cdr items))))
          ((eq? proc 'push!)
           (if (pair? (car args))
               (set! args (car args)))
           (set! items (append (reverse args) items)))
          ((eq? proc 'stack)
           items))))

;;tester
(define s1 (make-stack (list 'foo 'bar)))
(define s2 (make-stack '()))
(s1 'pop!)
(display "(s1 'stack) -> ")(s1 'stack) ;; -> (bar)
(s2 'pop!)
(s2 'push! 1 2 3 4)
(display "(s2 'stack) -> ")(s2 'stack) ;; -> (4 3 2 1)
(s1 'push! 'bah)
(s1 'push! 'zap 'zip 'baz)
(display "(s1 'stack) -> ")(s1 'stack) ;; -> (baz zip zap bah bar)

;; b
(define (pop! arg)
  (arg 'pop!))

(define (push! arg . args)
  (arg 'push! args))

(define (stack arg)
  (arg 'stack))

(pop! s1)
(display "(stack s1) -> ")(stack s1)
(push! s1 'foo 'faa)
(display "(stack s1) -> ")(stack s1)

;; Oppgave 3
;; a
;; se vedlagt jpg
(define bar (list 'a 'b 'c 'd 'e))
(set-cdr! (cdddr bar) (cdr bar))
;; Forklaringen på verdiene er at vi lager en syklisk liste. Etter indeks 3 (altså 'd) peker listen tilbake på indeks 1 (altså 'b) og danner en syklisk liste over denne sublisten

;; b
;; se vedlagt jpg
(define bah (list 'bring 'a 'towel))
(set-car! bah (cdr bah))
(set-car! (car bah) 42)
;; Ved kallet på set-car! er evaluerer (cdr bah) til '(a towel). set-car! omdefinerer (car bah) til denne verdien, så i stedet for at car evaluerer til et enkelt objekt (altså bring) evaluerer det nå til sublisten gitt av cdr.
;; Da får vi den nøstede listen '((a towel) a towel)

;; c
(define (cycle? lst)
  (define (cycle-check hare tortoise)
    (cond ((null? hare) #f)
          ((null? (cdr hare)) #f)
          ((eq? tortoise hare) #t)
          (else (cycle-check (cddr hare) (cdr tortoise)))))
  (cond ((null? lst) #f)
        ((null? (cdr lst)) #f)
        (else (cycle-check (cddr lst) (cdr lst)))))



;; d
;; Definisjonen av en liste er en kjede av cons-celler hvor det siste elementet er den tomme lista '().
;; Når man da har en syklisk liste vil den aldri avsluttes med et element '() og er derfor ikke en liste.

;; e
(define (make-ring items)
  ;;connects the ends of the list to make a ring
  (define (make-circular ring)
    (if (null? (cdr ring))
        (set-cdr! ring items)
        (make-circular (cdr ring))))
  (make-circular items)
  ;;finds a list of the single items that make up the cyclic list
  (define (find-items ring)
    (define (help-find-items ring it result)
      (if (equal? ring it)
          (reverse (append result (cons (car it) '())))
          (help-find-items ring (cdr it) (cons (car it) result))))
    (help-find-items ring (cdr ring) '()))
  ;;commands
  (lambda (proc)
    (cond ((eq? proc 'top)
           (car items))
          ;;left-rotate
          ((eq? proc 'left-rotate!)
           (set! items (cdr items))
           (car items))
          ;;right-rotate
          ((eq? proc 'right-rotate!)
           (let ((lst (find-items items)))
             (set! lst (cons (car (reverse lst)) (reverse (cdr (reverse lst))))) 
             (set! items ((make-ring lst) 'out))
             (car items)))
          ;;returns ring
          ((eq? proc 'out)
           items)
          ;;insert
          ((eq? proc 'insert!)
           (lambda (symbol)
             (let ((lst (find-items items)))
               (set! lst (cons symbol lst))
               (set! items ((make-ring lst) 'out)))
             (car items)))
          ;;delete
          ((eq? proc 'delete!)
           (let ((lst (find-items items)))
             (set! lst (cdr lst))
             (set! items ((make-ring lst) 'out)))
           (car items)))))

(define (top ring)
  (ring 'top))

(define (left-rotate! ring)
  (ring 'left-rotate!))

(define (right-rotate! ring)
  (ring 'right-rotate!))

(define (insert! ring symbol)
  ((ring 'insert!) symbol))

(define (delete! ring)
  (ring 'delete!))

(define r1 (make-ring '(1 2 3 4)))
(define r2 (make-ring '(a b c d)))
(display "(top r1) -> ")(top r1) ;; -> 1
(display "(top r2) -> ")(top r2) ;; -> a
(display "(right-rotate! r1) -> ")(right-rotate! r1)
(display "(left-rotate! r1) -> ")(left-rotate! r1)
(display "(left-rotate! r1) -> ")(left-rotate! r1)
(display "(delete! r1) -> ")(delete! r1)
(display "(left-rotate! r1) -> ")(left-rotate! r1)
(display "(left-rotate! r1) -> ")(left-rotate! r1)
(display "(left-rotate! r1) -> ")(left-rotate! r1)
(display "(insert! r1 'x) -> ")(insert! r2 'x)
(display "(right-rotate! r2) -> ")(right-rotate! r2)
(display "(right-rotate! r2) -> ")(left-rotate! r2)
(display "(right-rotate! r2) -> ")(left-rotate! r2)
(display "(top r1) -> ")(top r1)

;; f
;;left-rotate har en effektiv konstant kompleksitet, da den kun gjør en operasjon.
;;right-rotate har en lineær kompleksitet O(n) for n antall elementer i listen.
;;denne kompleksiteten stammer fra implementeringen av find-items som dekomponerer ringen
;;til en enkelt liste som så endres og så brukes til å lage en ny ring.









