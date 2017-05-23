class ForelesnEks {

    ForelesnEks() {
	ListeAvPersoner mineVenner  =
	    new ListeAvPersoner();

	Person ane = new Person("Ane");
	mineVenner.settInnFørst(ane);

	Person jonas = new Person("Jonas");
	mineVenner.settInnFørst(jonas);

	Person imran = new Person("Imran");
	mineVenner.settInnSist(imran);

	Person siri = new Person("Siri");
	mineVenner.settInnEtter(ane, siri);

	Person jan = new Person("Jan");
	mineVenner.settInnEtter(ane, jan);
        
	ane = mineVenner.finnPerson("Ane");
	mineVenner.settInnEtter(ane, new Person("Mari"));
	mineVenner.skrivAlle();

        Person endre = new Person("Endre");

    }
}



class Eks2014 {
    public static void main(String[] klargs) {
        new ForelesnEks();
    }
}





class Person {
    String navn;
    Person neste;

    Person(String n) {
	navn = n;
    }

    public void skriv(){
        System.out.println(navn);
    }
}











class ListeAvPersoner {
    private Person personliste, sistePerson;
    private int antall;
    
    ListeAvPersoner(){
        Person lh = new Person("LISTEHODE!!");
	personliste = lh;
	sistePerson = lh;
	antall = 0;
    }

    /* 
       Invariante tilstandspåstander
       (skal gjelde før og etter alle metodekall):
       personliste peker på listehodet
       første person i lista er personliste.neste
       Hvis lista er tom er antall lik 0 og 
       personliste og sistePerson peker på
       listehodet og personliste.neste er null.
    */

    public boolean eksisterer(Person person) {
        Person p = personliste.neste;
        for (int i = antall; i>0; i--) {
            if (p.navn.equals(person.navn)) return true;
            else p = p.neste;
        }
        return false;
    }
       
    
    public void settInnFørst(Person nypers){
        if (eksisterer(nypers)) {
            System.out.println(nypers.navn +" eksisterer fra før og ble ikke lagt til");
            return;
        }
        nypers.neste = personliste.neste;
        personliste.neste = nypers;
        antall++;
        if (sistePerson == personliste) sistePerson = nypers;
    }
    
    public void settInnSist(Person inn){
        if (eksisterer(inn)) {
            System.out.println(inn.navn +" eksisterer fra før og ble ikke lagt til");
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
            System.out.println(inn.navn +" eksisterer fra før og ble ikke lagt til");
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
	    if (p.navn.equals(s)) return p;
	    else p = p.neste;
        }
	return null;
    }

    
    public void skrivAlle() {
        Person p = personliste.neste;
        for (int i = antall; i>0; i--) {
            p.skriv();
            p = p.neste;
        }
    }
}



