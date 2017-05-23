// 2014.02.06: Gitt nytt navn til metoden settInnIStarten...

public class ListeAvPersoner {
    private Person personliste, sistePerson;
    private int antall;
    
    ListeAvPersoner(){
	// skal etablere datastrukturen for tom liste:
        Person lh = new Person("LISTEHODE!!");
	personliste = lh;
	sistePerson = lh;
	antall = 0;
    }

    /* 
       Invariante tilstandspÃ¥stander (skal gjelde fÃ¸r og etter alle metodekall):

       - personliste peker pÃ¥ listehodet
       - fÃ¸rste person i lista er fÃ¸rste person etter
         listehodet, dvs. personliste.neste
       - sisteperson peker pÃ¥ siste person i lista, dvs.
       - sisteperson.neste er alltid null
       - ingen andre personobjekter har neste som er null

       - NÃ¥r lista er tom skal (tilstanden etableres av konstruktÃ¸r):
            - antall innholde tallet 0
            - personliste peke pÃ¥ listehodet
	    - sistePerson peke pÃ¥ listehodet
	    - personliste.neste vÃ¦re null
    */
       
     public boolean eksisterer(Person person) {
        Person p = personliste.neste;
        for (int i = antall; i>0; i--) {
            if (p.hentNavn().equals(person.hentNavn())) return true;
            else p = p.neste;
        }
        return false;
    }   

    public void settInnIStarten(Person nypers){
        if (eksisterer(nypers)) {
            System.out.println(nypers.hentNavn() +" eksisterer fra før og ble ikke lagt til");
            return;
        }
        nypers.neste = personliste.neste;
        personliste.neste = nypers;
        antall++;
        if (sistePerson == personliste) sistePerson = nypers;
    }
    
    public void settInnSist(Person inn){
        if (eksisterer(inn)) {
            System.out.println(inn.hentNavn() +" eksisterer fra før og ble ikke lagt til");
            return;
        }
	sistePerson.neste = inn;
	sistePerson = inn;
	antall++;
    }
    
    public void settInnEtter(Person denne, Person inn) {
        if (!eksisterer(denne)) return;
        if (denne == inn) return;
        if (eksisterer(inn)) {
            System.out.println(inn.hentNavn() +" eksisterer fra før og ble ikke lagt til");
            return;
        }
        inn.neste = denne.neste;
	denne.neste = inn;
	antall++;
        if (sistePerson == denne) sistePerson = inn;
    }
    
    public Person finnPerson(String s) {
	Person p = personliste.neste;
        for (int i = antall; i>0; i--) {
	    if (p.hentNavn().equals(s)) return p;
	    else p = p.neste;
        }
	return null;
    }

    
    public void skrivAlle() { 
        Person p = personliste.neste;
	System.out.println("------");
        for (int i = antall; i>0; i--) {
     	    System.out.print(antall - i +1 + ": ");
            p.skrivUtAltOmMeg();
            p = p.neste;
        }
	System.out.println("=======");
    }
}


