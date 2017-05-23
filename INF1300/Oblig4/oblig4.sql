/*Oppgave 1*/
/*Jeg har forsøkt å laste dette inn ved hjelp av \i for å se om det funket. Jeg fikk en feilmelding indikert ved starten av create table student, men fant ikke helt ut av hva den reagerte på*/

/*Endrewu 16.oktober*/

create table student (
bnavn varchar(8) primary key,
navn varchar(80) not null,
uid int unique not null,
spkode varchar(4)
);

create table emnestud (
bnavn varchar(8) not null,
emnekode varchar(7) not null,
grnr varchar(8),
sem varchar(6) not null,
primary key (bnavn, emnekode, sem)
);

create table emne (
emnekode varchar(7) primary key,
enavn varchar(80),
fsem varchar(6)
);

/*Oppgave 2*/
/*2.1*/
SELECT DISTINCT navn FROM student WHERE navn LIKE '__ %';

/*2.2*/SELECT s.bnavn, emnekode FROM Student S, EmneStud E WHERE S.navn LIKE '__ %' and S.bnavn = E.bnavn ORDER BY bnavn asc;

/*2.3*/SELECT DISTINCT enavn FROM Student S, Emnestud ES, Emne E WHERE spkode is null and S.bnavn = ES,bnavn and ES.emnekode = E.emnekode;

/*2.4*/SELECT DISTINCT navn FROM student S, emnestud E WHERE emnekode like 'inf1300' and S.bnavn = E.bnavn;

/*2.5*/SELECT bnavn FROM emnestud WHERE emnekode LIKE 'inf2220' and sem=’2013-2’ INTERSECT SELECT bnavn FROM emnestud WHERE emnekode LIKE 'inf1300' and sem=’2013-2’;

/*2.6*/SELECT DISTINCT e1.bnavn FROM emnestud e1 JOIN emnestud e2 on e1.bnavn=e2.bnavn and e1.grnr=e2.grnr WHERE e1.emnekode='inf2220' and e2.emnekode='inf1300';

/*2.7*/SELECT DISTINCT navn, s.bnavn FROM student s, emnestud e WHERE emnekode='inf2220' and sem='2013-2' and s.bnavn=e.bnavn INTERSECT SELECT DISTINCT navn, s.bnavn FROM student s, emnestud e WHERE emnekode='inf1300' and sem='2013-2' and s.bnavn=e.bnavn;
