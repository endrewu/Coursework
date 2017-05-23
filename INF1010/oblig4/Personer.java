import java.util.Iterator;
import java.util.NoSuchElementException;

class Personer {//Person klassen med aktuelle variabler
    private String navn;
    private int nummer;
    private char kjonn;
    YngsteForstReseptListe beholder = new YngsteForstReseptListe();//beholder for resepter

    Personer(int nr, String navn, char kjonn) {//konstruktør
        this.navn = navn;
        nummer = nr;
        this.kjonn = kjonn;
    }

    public String hentNavn() {//returnmetoder vi bruker andre steder i programmet
        return navn;
    }
    public char hentKjonn(){
        return kjonn;
    }
    public void skriv(){//skriver ut.
        System.out.println("Navn: " + navn + " personnummer: " + " Kjønn " + kjonn);
    }
}

class Resepter {//reseptklassen 
    int nummer, reit;
    private int eier;
    private Leger utskriver;
    private char farge;
    private Legemiddel legemiddel;
    Personer brukesIkke;
    Resepter(int nr, char farge, int persNummer, Leger lege, Legemiddel legemiddel, int reit, Personer personPeker) {//konstruktør med parametere vi trenger for å lage et objekt av resepter
        nummer = nr;
        this.farge = farge;
        eier = persNummer;
	this.legemiddel = legemiddel;	
        this.reit = reit;
        utskriver = lege;
        brukesIkke = personPeker;
    }

    public char hentFarge() {//metoder vi bruker for å returnere aktuell info
        return farge;
    }

    public int hentReit() {
        return reit;
    }

    public Legemiddel hentLegemiddel() {
        return legemiddel;
    }

    public void hentUt(){//prosserer uttak av resept
        if(reit == 0){
            System.out.println("Ugyldig resept; Ingen reit igjen.");
        }else{
            System.out.println("Resept: ");
            System.out.println("Nr: "+ nummer + " Reit: " + reit + " Navn på pasient: " + brukesIkke.hentNavn() + " Legenavn: " + utskriver.hentNavn());
            reit--;
            legemiddel.skriv();
        }
    }
}

interface AbstraktTabell<T>{//interface
    
    boolean settInn(T t, int i);
    T finnObjekt(int i);
    Iterator iterator();  
}

class Tabell<T> implements AbstraktTabell<T>, Iterable<T> {//generisk tabellklasse vi bruker for å lagre personer og legemidler i
    int j;

    private T[] tabell;
    public Tabell(int i) {//konstruktør
        tabell = (T []) new Object[i];
    }

    public T finnObjekt(int i) {//returnerer objekt utifra i
        if (tabell[i] != null){
           
            return tabell[i];
        }
        return null;
    }

    public boolean settInn(T t, int i) {//setter inn på plas ut ifra i
        if (tabell[i] == null) {
            tabell[i] = t;
            return true;
        }
        return false;
    }
	
    public Iterator iterator() {//oppretter iterator og returnerer
        Iterator it = new TabellIterator();
        return it;
    }
    
    class TabellIterator implements Iterator {//iterator
	int runde;
	public TabellIterator(){//konstruktør
	    int i = tabell.length;
	    runde = 0; 
	}
	public  boolean hasNext() {//aktuelle metoder
	    if(tabell[runde] != null){
		return true;
	    }else{
		return false;
	    }
			
	}
	public T next() {
	    if(hasNext() == true){
		runde++;
		return tabell[runde-1];
	    }else{
		return null;
	    }
	}        
	public  void remove() {//trengs ikke i følge bloggen
			
	}
    }
}

class EnkelReseptListe implements Iterable<Resepter> {//liste for resepter
    protected Node forste,siste;
    
    class Node{//bruker noder for å binde sammen listen
        Node (Resepter r) { objektet = r; }
	Node neste;
	Resepter objektet;
    }

    public Iterator iterator() {//returnerer iterator
        LenkeListeIterator it = new LenkeListeIterator();
        return it;
    }
    
    class LenkeListeIterator implements Iterator {//iterator
        Node naverende;
        Node neste = null;

        public LenkeListeIterator(){//konstruktør
            naverende = forste;
            neste = forste;
        }

        public boolean hasNext() {//aktuelle metoder
            return neste != null;
        }

        public Resepter next() {
            if ( hasNext() ) {
                Resepter ut = neste.objektet; 
                neste = neste.neste;
                return ut;
            } else {
                throw new NoSuchElementException();
            }
        }
        
        public  void remove() {}
    }
    
    public Resepter finnResept(int i){//returner objekt/null utifra i
        Iterator it = new LenkeListeIterator();
        Resepter temp = (Resepter)it.next();
        for ( Resepter r: this) {
            if (r.nummer == i) return r;
        }
        return null;
    }
}

class EldsteForstReseptListe extends EnkelReseptListe{//reseptbeholder for leger
    public void settInn(Resepter r){//metode som setter inn nye sist
        Node n = new Node(r);
        if(siste == null){
            forste = n;
            siste = n;
            n.neste = null;
        }else{
            siste.neste = n;		
            siste = n;
        }
    }
}

class YngsteForstReseptListe extends EnkelReseptListe{//reseptbeholder for personer
    public void settInn(Resepter r){//metode som setter inn nye forst
        Node n = new Node(r);
        if(forste == null){
            forste = n;
            siste = n;
            n.neste = null;
        }else{
            n.neste = forste;
            forste = n;
        }           
    }
}
