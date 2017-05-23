/*Oppgave 1*/
/*a*/select distinct es.emnekode, enavn from emnestud es, emne e where es.emnekode=e.emnekode order by emnekode;

/*b*/select distinct emnekode, count(distinct grnr) as antall_grupper from emnestud group by emnekode order by emnekode;

/*c*/select distinct grlremne, count(distinct grlrgrnr) from student group by grlremne;

/*d*/select distinct emnekode, grnr, count(distinct bnavn) as antall_studenter from emnestud where sem='2013-2' and emnekode!='inf1100' and grnr !='null' group by emnekode, grnr order by emnekode asc, antall_studenter desc;

/*e*/select distinct emnekode, grnr, count(distinct bnavn) as antall_studenter from emnestud where sem='2013-2' and emnekode!='inf1100' and grnr !='null' group by emnekode, grnr having count(bnavn) < 34 order by emnekode asc, antall_studenter desc;

/*f*/select distinct emnekode, count(distinct grnr) as antall_grupper, count(distinct bnavn)/count(distinct grnr) as gjsn_studenter from emnestud group by emnekode order by emnekode;

/*g*/select min(antall) from (select distinct emnekode, grnr, count(distinct bnavn) antall from emnestud group by emnekode, grnr) telling;

/*h*/select distinct emnekode, grnr, min(antall) from (select distinct emnekode grnr, count(distinct bnavn) antall from emnestud group by emnekode, grnr) telling group by emnekode, grnr order by min desc;
