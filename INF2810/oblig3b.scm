(load "evaluator.scm")

;;Nok en gang har jeg jobbet med vegan, bjornife og eirikfl på obligen.

(set! the-global-environment (setup-environment))

;;Oppgave 1
;; a

;;(foo 2 square) -> 0
;; defineres ut i fra den første definisjonen gitt, av foo. I denne definisjonen blir da cond = 2 og else = square.
;; Fordi (= cond 2) er sann blir printout 0

;;(foo 4 square) -> 16
;; Samme som ovenfor. Fordi (= cond 2) blir falsk kalles else ved (else (else cond)) -> (else (square 4)) = 16

;;(cond ((= cond 2) 0)(else (else 4))) -> 2
;; ..(cond ((= 3 2) 0) (else (/ 4 2)))
;; Fordi cond er definert til 3 vil altså else slå til. Denne kaller prosedyren else, definert som (/ x 2) med parameteren 4. Altså 2.

;; Poenget er altså at evaluatoren er i stand til å kjenne igjen syntaksen i special cases, men også kan kjøre prosedyrer som bruker de samme
;; navnene uten problemer.

;; Oppgave 2
;; a
(define primitive-procedures
  (list (list 'car car)
        (list 'cdr cdr)
        (list 'cons cons)
        (list 'null? null?)
        (list 'not not)
        (list '+ +)
        (list '- -)
        (list '* *)
        (list '/ /)
        (list '= =)
        (list 'eq? eq?)
        (list 'equal? equal?)
        (list 'display 
              (lambda (x) (display x) 'ok))
        (list 'newline 
              (lambda () (newline) 'ok))
        ;;      HER STARTER EGNE ENDRINGER
        (list '1+
              (lambda (x) (+ x 1)))
        (list '1-
              (lambda (x) (- x 1)))
        ;;      HER ENDER EGNE ENDRINGER
        ))

;; b
(define (install-primitive! name exp)
  (set! primitive-procedures (append primitive-procedures (list (list name exp))))
  (define-variable! name (list 'primitive exp) the-global-environment))

;; Oppgave 3
;;Endringer som gjelder for hele 3:
(define (special-form? exp)
  (cond ((quoted? exp) #t)
        ((assignment? exp) #t)
        ((definition? exp) #t)
        ((if? exp) #t)
        ((lambda? exp) #t)
        ((begin? exp) #t)
        ((cond? exp) #t)
        ((and? exp) #t)
        ((or? exp) #t)
        ((let? exp) #t)
        ((while? exp) #t)
        (else #f)))

(define (eval-special-form exp env)
  (cond ((quoted? exp) (text-of-quotation exp))
        ((assignment? exp) (eval-assignment exp env))
        ((definition? exp) (eval-definition exp env))
        ((if? exp) (eval-if exp env))
        ((and? exp) (eval-and exp env))
        ((or? exp) (eval-or exp env))
        ((let? exp) (eval-let exp env))
        ((lambda? exp)
         (make-procedure (lambda-parameters exp)
                         (lambda-body exp)
                         env))
        ((begin? exp) 
         (eval-sequence (begin-actions exp) env))
        ((cond? exp) (mc-eval (cond->if exp) env))))


;; a
(define (and? exp) (tagged-list? exp 'and))

(define (and-checks exp) (cdr exp))

(define (or? exp) (tagged-list? exp 'or))

(define (or-checks exp) (cdr exp))

(define (eval-and exp env)
  (cond ((null? (and-checks exp))
         (car exp))
        ((true? (car (and-checks exp)))
         (eval-and (and-checks exp) env))
        ((false? (car (and-checks exp)))
         (car (and-checks exp)))))

(define (eval-or exp env)
  (cond ((null? (or-checks exp))
         (car exp))
        ((true? (car (or-checks exp)))
         (car (or-checks exp)))
        ((false? (car (or-checks exp)))
         (eval-or (or-checks exp) env))))

;; b
(define then 'then)
(define elsif 'elsif)
(define else 'else)

(define (eval-if exp env)
  (mc-eval (if-loop (cdr exp) env) env))

(define (if-loop exp env)
  (cond ((true? (mc-eval (car exp) env)) (caddr exp)) 
        ((equal? (cadddr exp) 'elsif) (mc-eval (if-loop (cddddr exp) env) env))
        ((equal? (cadddr exp) 'else) (car (cddddr exp)))))

;; c
(define (let? exp) (tagged-list? exp 'let))  

(define (let-exp exp)
  (define (helper exp)
    (if (null? exp)
        '()
        (cons (cadar exp) (helper (cdr exp)))))
  (helper (cadr exp)))

(define (let-var exp)
  (define (helper exp)
    (if (null? exp)
        '()
        (cons (caar exp) (helper (cdr exp)))))
  (helper (cadr exp)))

(define (let-body exp)
  (cddr exp))

(define (eval-let exp env)
  (mc-apply (mc-eval (make-lambda (let-var exp) (let-body exp)) env) (let-exp exp)))

;; d
(define (let-body exp env)
  (cdr (member 'in exp)))

(define (let-list-of-vars-exp exp env)
  (car exp))

(define (let-vars exp env)
  (define (let-vars-loop exp env)
    (if (null? exp)
        '()
        (cons (car exp) 
              (cond ((equal? (cadddr exp) 'in) 
                     '())
                    ((equal? (cadddr exp) 'and)
                     (let-vars-loop (cddddr exp) env))))))
        (let-vars-loop exp env))

(define (let-exp exp env)
  (define (let-exp-loop exp env)
    (if (null? exp)
        '()
        (cons (car exp) 
              (cond ((equal? (cadr exp) 'in) 
                     '())
                    ((equal? (cadr exp) 'and)
                     (let-exp-loop (cddddr exp) env))))))
  (let-exp-loop (cddr exp) env))

(define (eval-let exp env) 
  (mc-eval (append (list (make-lambda (let-vars exp env) (let-body exp env))) (let-exp exp env)) env))

;; e
;; Denne har jeg ikke fått gjort
















