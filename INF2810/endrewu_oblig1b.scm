;; Oblig 1b

;; Oppgave 1
;; a
;;  ____ ____
;; |    |    |
;; | 47 | 11 |
;; |____|____|

;; b
;;  ____ ____
;; |    |    |
;; | 47 | nil|
;; |____|____|

;; c
;;  ____ ____    ____ ____
;; |    |    |  |    |    |
;; | 47 |  ---->| 11 | nil|
;; |____|____|  |____|____|

;; d
;;  ____ ____    ____ ____
;; |    |    |  |    |    |
;; | 47 |  ---->|  | | nil|
;; |____|____|  |__|_|____|
;;                 |
;;               __V_ ____    ____ ____
;;              |    |    |  |    |    |
;;              | 11 |  ---->| 12 | nil|
;;              |____|____|  |____|____|

;; e
;;  _____ _____
;; |     |     |
;; |1 2 3|1 2 3|
;; |_____|_____|


;; f
(car (cdr (cdr '(1 2 3 4))))

;; g
(car (car (cdr '((1 2) (3 4)))))

;; h
(car (car (cdr (cdr '((1) (2) (3) (4))))))

;; i
(cons (cons 1 '())
      (cons (cons 2 '())
            (cons (cons 3 '()) 
                  (cons (cons 4 '()) '()))))

(list (list 1)
      (list 2)
      (list 3)
      (list 4))


;; Oppgave 2
;; a
(define (length2 items)
  (define (counter items x)
    (if (null? items)
        x
        (counter (cdr items) (+ 1 x))))
  (counter items 0))

;; b
(define (rev-list items)
  (define (rev in out)
    (if (null? in)
        out
        (rev (cdr in)
             (cons (car in) out))))
  (rev items '()))

;; Har brukes halerekursjon. Har ærlig talt valgt dette fordi jeg ikke fikk det til med vanlig rekursjon, og endte derfor opp med å bruke eksempelet fra forelesning. 

;; c
(define (ditch numbers n)
  (cond ((null? numbers) '())
        ((= (car numbers) n)
         (ditch (cdr numbers) n))
        (else (cons (car numbers)
                    (ditch (cdr numbers) n)))))
;; Denne løsningen bruker helt "vanlig" rekursjon. Ressursbehovet vil vokse lineært O(n) med lengden n på listen som gis ettersom hvert nummer i listen vil føre til et kall på prosessen.

;; d
(define (nth index items)
  (define (iter index items cntr)
    (if (= index cntr)
        (car items)
        (iter index (cdr items) (+ cntr 1))))
  (iter index items 0))

;; e
(define (where n numbers)
  (define (iter n numbers out)
    (cond ((null? numbers) #f)
          ((= (car numbers) n) out)
          (else (iter n (cdr numbers) (+ out 1)))))
  (iter n numbers 0))

;; f
(define (map2 proc a b)
  (cond ((null? a) '())
        ((null? b) '())
        (else (cons (proc (car a) (car b))
                    (map2 proc (cdr a) (cdr b))))))

;; g
(map2 (lambda (x y)
        (/ (+ x y) 2))
      '(1 2 3 4)
      '(3 4 5))

(map2 (lambda (x y)
        (and (even? x) (even? y)))
      '(1 2 3 4)
      '(3 4 5))

;; h
(define (both? pred)
  (lambda (a b)
    (cond ((not (pred a)) #f)
          ((not (pred b)) #f)
          (else #t))))

;; i
(define (self x)
  (lambda (a)
    (x a a)))