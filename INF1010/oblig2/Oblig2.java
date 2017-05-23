/*Grunnet litt tidsmangel er det noe som gjenstår før dette programmet utfører 
 *alle operasjoner som blir etterspurt i oppgaveteksten. Av denne grunn er det
 *også sparsommelig kommentert. Helt spesifikt opplever jeg at jeg ikke får
 *kalt på mittBibliotek.hentAntall() fordi javac forteller meg at det er en
 *ikke-statisk metode kalt fra en statisk kontekst.
 *Jeg har heller ikke fått fullført funksjonen til denne generiske klassen.
 *Foreløpig i metoden vilNoenHaBoka ligger derfor bare en enkel utskrift av
 *navnet til vedkommende som evt. vil ha boka hardkodet.
 */


class Oblig2 {
    public static void main (String[] args) {
        Test t = new Test();
        t.prove();
   }
} //end class Oblig2

class Person {
    private String navn;
    private Person bestevenn;
    private String bokKat;
    Person (String n, String k) {
        navn = n;
        bokKat = k;
    }

    public String hentNavn() {
        return navn;
    } //end hentNavn()

    public void blirVennMed(Person p) {
        bestevenn = p;
    } //end blirVennMed()
    
    public Person hentBestevenn() {
        return bestevenn;
    } //end hentBestevenn()

    public String minBesteVennHeter() {
        if (bestevenn != null) {
        return bestevenn.hentNavn();
        } else {
            return "ingen";
        }
    } //end minBesteVennHeter()

    public void skrivUtMeg() {
        System.out.println(navn + " er venn med "+ minBesteVennHeter());
        System.out.println(navn + " liker " + bokKat + "bøker og har " + mittBibliotek.hentAntall() + " av dem.");
    } //end skrivUtMeg()

    public void skrivUtAlt () {
        skrivUtMeg();
        if (hentBestevenn() != null) {
            hentBestevenn().skrivUtAlt();
        }
    } //end skrivUtAlt()

    private boolean mittInteressefelt(Bok b) {
        if (bokKat.equals(b.kategori())) {
            return true;
        } else {
            return false;
        }
    } //end mittInteressefelt()

    public Bok vilJegHaBoka(Bok b) {
        if (mittInteressefelt(b) == true) {
            return null;
        } else {
            return b;
        }
    }
} //end class Person

class mittBibliotek<Bok> {
    private Bok[]alle = (Bok[]) new Object[100];
    private int antall = 0;
    
    public void settInn(Bok det) {
        alle[antall] = det;
        antall++;
    } //end settInn()

    public Bok taUt() {
        antall--;
        return alle[antall];
    } //end taUt()
    
    public int hentAntall() {
        return antall;
    } //end hentAntall() 
} //end class mittBibliotek

class Bok {
    private String kat;
    Bok (String k) {
        kat = k;
    }
    
    public String kategori() {
        return kat;
    } //end kategori()
} //end class Bok

class Test {
        Person Jeg = new Person("Jeg", "poesi");
        Person Lisa = new Person("Lisa", "krim");
        Person Ramzi = new Person("Ramzi", "mat");
        Person Emil = new Person("Emil", "sports");
    public void prove() {
        Jeg.blirVennMed(Lisa);
        Lisa.blirVennMed(Ramzi);
        Ramzi.blirVennMed(Emil);
        Jeg.skrivUtAlt();
        
        vilNoenHaBoka(new Bok("Java"));
        vilNoenHaBoka(new Bok("krim"));
        vilNoenHaBoka(new Bok("historie"));
        vilNoenHaBoka(new Bok("sports"));
        vilNoenHaBoka(new Bok("poesi"));
        vilNoenHaBoka(new Bok("sports"));
        vilNoenHaBoka(new Bok("poesi"));
        vilNoenHaBoka(new Bok("baby"));
        vilNoenHaBoka(new Bok("poesi"));
    } //end prove()

    public void vilNoenHaBoka(Bok b) {
        Bok bok = Emil.vilJegHaBoka(b);
        if (bok == null) {
            System.out.println("Emil");
        } else {
            bok = Ramzi.vilJegHaBoka(b);
            if (bok == null) {
                System.out.println("Ramzi");
            } else { 
                bok = Lisa.vilJegHaBoka(b);
                if (bok == null) {
                    System.out.println("Lisa");
                } else {
                    bok = Jeg.vilJegHaBoka(b);
                    if (bok == null) {
                        System.out.println("Ego");
                    }
                }
            }
        }
    } //end vilNoenHaBoka()
} //end class Test