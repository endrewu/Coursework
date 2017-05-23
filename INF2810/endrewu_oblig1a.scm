;; 1a
(* (+ 2 2) 5)
;; Evalueres til 20

;; 1b
;;(* (+ 2 2) (5))
;; application: not a procedure; expected a procedure that can be applied to arguments given
;; Når gitt en parantes forventer scheme at det skal følge en prosedyre som kan. Selvevaluerende uttrykk kan ikke stå alene i en parantes

;; 1c
;;(* (2 + 2) 5)
;; Samme feil som over, igjen forventes en prosedyre, og fordi scheme benytter prefiks notasjon er ikke 2+2 en gyldig prosedyre.

;; 1d 
(define bar (/ 4 2))
bar
;; Evalueres til 2, bar defineres til verdien 2 og blir så skrevet ut

;; 1e 
(- bar 2)
;; Evalueres til 0

;; 1f
(/ (* bar 3 4 1) bar)
;; Evalueres til 12. Først evalueres den innerste parantesen til 24 som deles på 2.


;; 2a
(or (= 1 2)
    "piff!"
    "paff!"
    (zero? (1 - 1)))
;; Evalueres til "piff!"
;; Som nevnt er or en special form, når kalt vil den avslutte evalueringen så snart den finner et gyldig uttrykk. 1 og 2 er ikke like, men
;; strenger "piff!" som er selvevaluerende regnes som sann (i likhet med alt som ikke er spesifikt false).

(and (= 1 2)
     "piff!"
     "paff!"
     (zero? (1 - 1)))
;; Evalueres til #f. På samme måte som or slutter evalueringen så snart den finner et sant tilfelle vil and slutte å evaluere så snart den
;; finner et tilfelle som gir #f, altså (= 1 2)

(if (positive? 42)
    "poff!"
    (i-am-undefined))
;; Evalueres til "poff!". Nok et spesialtilfelle hvor if sjekker om uttrykket evalueres til #t eller ikke. Om den evalueres til #t returneres
;; den første linjen, i dette tilfellet "poff!". Om uttrykket evalueres til #f returneres det siste uttrykket, (i-am-undefined). Fordi if
;; evalueres til #t vil den aldri kalle på den udefinerte prosedyren.

;; 2b
(define (sign-if x)
  (if (< x 0)
      -1
      (if (> x 0)
          1
          0)))

(define (sign-cond x)
  (cond ((< x 0) -1)
        ((> x 0) 1)
        (else 0)))

;; 2c
(define (sign x)
  (or (and(< x 0) -1)
      (and(> x 0) 1)
      (and(= x 0) 0)))

;; 3a
(define (add1 x)
  (+ x 1))

(define (sub1 x)
  (- x 1))

;; 3b
(define (plus x y)
  (if (zero? y)
      x
      (plus (add1 x) (sub1 y))))

;; 3c
;; Jeg tror prosessen i 3b er iterativ. Dette fordi den kaller seg selv med nye tall, og når y når 0 returnerer resultatet uten at den 
;; må regne sammen alle stegene

(define (plus-rek x y)
  (if (= 1 y)
      (add1 x)
      (plus (add1 x) (sub1 y))))

;; 3d
(define (power-close-to b n)
(define (power-iter e)
  (if (> (expt b e) n)
      e
      (power-iter (+ 1 e))))
  (power-iter 1))
;; En måte man kan forenkle hjelpe-prosedyren på er å fjerne unødvendige deklarasjoner av konstanter. Ettersom b og n allerede er definert
;; i den ytre prosedyren, og disse er konstante gjennom prosedyren i prosedyren trenger ikke hvert kall på hjelpeprosedyren å inkludere disse.
;; Den har allerede tilgang på dem, og trenger kunn å holde orden på e