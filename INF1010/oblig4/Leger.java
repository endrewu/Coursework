import java.util.Iterator;
import java.util.NoSuchElementException;

interface Avtale {//oppretter interfaces for legemidlene
    int avtalenummer();
}

interface Comparable<T> {
    int compareTo(T o);
}

interface Lik {
    boolean samme(String s);
}

class Leger implements Avtale, Comparable<Leger>, Lik {//oppretter legeklassen
    private String navn;
    private int spesialist;//inneholder aktuelle verdier legen trenger
    private int avtalenummer;
    EldsteForstReseptListe beholder = new EldsteForstReseptListe();

    Leger (String navn, int spesialist, int avtalenr) {//konstruktør
        this.navn = navn;
        this.spesialist = spesialist;
        this.avtalenummer = avtalenr;
    }

    public int compareTo(Leger l) {	//aktuelle metoder	
        return navn.compareTo(l.navn);		
    }
    public boolean samme(String s) {
        if (navn.equals(s)) return true;
        return false;
    }
    public int avtalenummer() {
        return avtalenummer;
    }

    public String hentNavn() {
        return navn;
    }

    public void skriv(){
        System.out.println("Navn: " + navn + " Spesialist: " + spesialist + " Avtalenummr: " + avtalenummer);
    }
}

interface AbstraktSortertEnkelListe<T extends Comparable & Lik> {//interface for sortert enkel liste
    void settInn(T t);
    T finnObjekt(String s);
    Iterator iterator();
}

class SortertEnkelListe<T extends Comparable & Lik> implements AbstraktSortertEnkelListe<T>, Iterable<T> {//listen vi har leger i
    protected Node forste,siste;
    Node naverende;
    T midlertidig;

    class Node{//bruker noder for å binde listen sammen
        Node (T t) { 
            objektet = t;
            midlertidig = t;
        }
	Node neste;
	T objektet;
    }
    public void settInn(T t) {//metode for å sette inn nytt generisk objekt
        Node n = new Node(t);
        if(forste == null){//unntaksmetode for tom container
            forste = n;
            siste = n;
            n.neste = null;
        }else{//generell metode for å legge inn nytt objekt sortert
            Node denne = forste;
            Node forrige = forste;
            
            while(denne != null){
                if(t.compareTo(denne.objektet) < 0){
                    if(denne == forste){
                        forste = n;

                    }else{
                        forrige.neste = n;
                    }
                    n.neste = denne;
                    return;
                }
                forrige = denne;
                denne = denne.neste;
            }
            forrige.neste = n;
            siste = n;
        }           
    }   
  
    public T finnObjekt(String s) { //finner objekt via string
        for(T t : this){
            if(t.samme(s)){
                return t;
            }
        }return null;
    }
   
    public Iterator iterator() {//oppretter iterator
        SortertIterator it = new SortertIterator();
        return it;
    }
    
    class SortertIterator implements Iterator {//iterator klassen som 
        Node naverende;//itererer gjennom listen
        Node neste = null;

        public SortertIterator(){
            naverende = forste;
            neste = forste;
        }
        public  boolean hasNext() {//metoder vi bruker for å iterere gjennom lista
            return neste != null;
        }
        public T next() {
            if( hasNext()){
                T retur = neste.objektet;
                neste = neste.neste;
                return retur;
            }else{
                throw new NoSuchElementException();
            }
        }
        
        public  void remove() {//sto på blogen at vi ikke trengte å bruke denne metoden
        
        }
    }
}
