(load "prekode3a.scm")
;; Også denne obligen er løst gjennom arbeid sammen med vegan, bjornife og eirikfl.
;; Planen var opprinnelig å gjøre om til to pargrupper, men vi tenkte ikke på at gruppen deres hang igjen fra oblig 2a

;; Oppgave 1
;; a og b
(define (mem valg proc)
  (let ((table (make-table)))
    (cond ((eq? valg 'unmemoize)
           (proc 'um))
          ((eq? valg 'memoize)
           (lambda args
             (cond ((null? args)
                    (proc))
                   ((eq? (car args) 'um)
                    proc)
                   (else (let ((pc-result (lookup args table)))
                           (or pc-result
                               (let ((result (apply proc args)))
                                 (insert! args result table)
                                 result))))))))))

(set! fib (mem 'memoize fib))
(fib 3)
(fib 3)
(fib 2)
(fib 4)
(set! fib (mem 'unmemoize fib))
(fib 3)

(set! test-proc (mem 'memoize test-proc))
(test-proc)
(test-proc 0)
(test-proc 40 41 42 43 44)
(test-proc 40 41 42 43 44)
(test-proc 42 43 44)

;; c
;; Problemet med mem-fib er at den ikke memoiserer alle kall på prosedyren, men kun det endelige resultatet.
;; Altså er resultatet for (mem-fib 3) memoisert, men (mem-fib 2) blir ikke memoisert som en del av kjøringen
;; av (mem-fib 3)

;; d
(define (name-help . args)
  (let ((table (make-table))
        (variables '()))
    (define (init-name . args)
      (cond ((pair? args)
             (cond ((eq? (car args) 'default)
                    (insert! (cadr args) (caddr args) table)
                    (cons (cadr args) variables)
                    (apply init-name (cdddr args)))
                   (else (insert! (car args) 'empty table)
                         (cons (car args) variables)
                         (apply init-name (cdr args)))))))
    (apply init-name args)
    (lambda vars
      (let ((outtable table))
        (define (set-values vars2)
          (cond ((pair? vars2)
                 (cond ((assoc (car vars2) (cdr table))
                        (set-cdr! (assoc (car vars2) (cdr table)) (cadr vars2))
                        (cond ((< 2 (length vars)) (set-values (cddr vars2)))))
                       (else (display "ERROR: ARGUMENT NOT IN TABLE"))))))
        (set-values vars)
        outtable))))

(define (greet . args)
  (let ((table (apply (name-help 'default 'time "day" 'default 'title "sir" 'firstword) args)))
    (let ((out (cons "good" (cons (lookup 'time table) (cons (lookup 'title table) '())))))
      (display out))))



(define test-table (make-table))
(insert! 'tiss "urin" test-table)
(insert! 'bæsj "avføring" test-table)
(insert! 'fis "promp" test-table)

;; Oppgave 2
;; a
(define (list-to-stream items)
  (if (null? items)
      the-empty-stream
      (cons-stream (car items)
                   (list-to-stream (cdr items)))))

(define (stream-to-list stream . arg)
  (define (iter-stream-to-list stream target sum)
    (if (= target sum)
        '()
        (cons (stream-car stream)
              (iter-stream-to-list (stream-cdr stream) target (+ 1 sum)))))
  
  (if (null? arg)
      (if (stream-null? stream)
          '()
          (cons (stream-car stream)
                (stream-to-list (stream-cdr stream))))
      (iter-stream-to-list stream (stream-car arg) 0)))

(list-to-stream '(1 2 3 4 5))
(stream-to-list (stream-interval 10 20))
(show-stream nats 15)
(stream-to-list nats 10)

;; b
(define (stream-map proc . argstreams)
  (if (memq the-empty-stream argstreams)
      the-empty-stream
      (cons-stream
       (apply proc (map stream-car argstreams))
       (apply stream-map
              (cons proc (map stream-cdr argstreams))))))

(define a (stream-interval 0 10))
(define b (stream-interval 10 15))

(show-stream (stream-map + a b))

;; c
;; Å skrive om prosedyren for å støtte strømmer er ikke fullt så lett.
;; Å sammenligne car av en strøm med cdr av samme strømmen er en håpløs operasjon.
;; I beste fall vil det gjøre bruken av strømmer unødvendig, da vi uansett må iterere
;; gjennom hele strømmen for å få et entydig resultat på om car eksisterer senere i strømmen.
;; I verste fall opererer vi her på en uendelig strøm, og prosessen vil aldri terminere den
;; første sammenligningen.

;; d
(define (remove-duplicates stream)
  (if (stream-null? stream)
      the-empty-stream
      (cons-stream (stream-car stream)
                   (stream-filter (lambda (x) 
                                    (not (eq? x (stream-car stream))))
                                  (remove-duplicates (stream-cdr stream))))))

(define stream-test 
  (cons-stream 1 (cons-stream 2 (cons-stream 1 (cons-stream 2 (cons-stream 4 '()))))))
(show-stream (remove-duplicates stream-test))

;; e

;; Svar på REPLet gir (// markerer linjeskift) 0 // 1 // 2 // 3 // 4 // 5 // 5 // 6 // 7 // 7
;; Første kallet på stream-ref viser (display) 1-5 og gir så returverdien, 5. Dette blir også memoisert
;; så ved neste kall vises kun 6 og 7 før 7 returneres, som så memoiseres. 

;; f
(define (mul-streams s1 s2)
  (stream-map * s1 s2))

(define factorials (cons-stream 1 (mul-streams nats factorials)))
(stream-ref factorials 5)













