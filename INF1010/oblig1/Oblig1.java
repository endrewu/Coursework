class Person {
    private String navn;
    private Person[] kjenner;
    private Person[] likerikke;
    private Person forelsketi;
    private Person sammenmed;

    //Konstruktøren
    Person(String n, int lengde) {
        navn=n;
        kjenner=new Person[lengde];
        likerikke=new Person[lengde];
    }

    //Returnerer navn
    public String hentNavn() {
        return navn;
    }

    //Sjekker om vedkommende kjenner personen p
    public boolean erKjentMed(Person p) {
        for (int i = 0; i<kjenner.length; i++) {
            if (kjenner[i] == p) {
                return true;
            }
        }
        return false;
    }
    
    //Finner en ledig plass i kjenner-arrayen (om mulig) og skriver inn en ny person der.
    //Gir tilbakemelding om det ikke er noen ledige plasser
    public void blirKjentMed(Person p) {
        for (int i=0; i < kjenner.length; i++) {
            if (kjenner[i]==null) {
                kjenner[i]=p;
                return;
            }
        }
        System.out.println("Kjenner-arrayen hadde ingen ledige plasser");
    }
    
    //Setter forelsketi til personen p
    public void blirForelsketI(Person p) {
        forelsketi=p;
    }
    
    //Finner ledig plass i likerikke-arrayen og setter person p inn i den plassen
    public void blirUvennMed(Person p) {
        for (int i=0; i < likerikke.length; i++) {
            if (likerikke[i]==null) {
                likerikke[i]=p;
                return;
            }
        }
    }
    
    //Sjekker om personen er venn med personen p
    public boolean erVennMed(Person p) {
        //Sjekker at de kjenner hverandre
        if (erKjentMed(p)) {
            //Sjekker likerikke-arrayen, om p står oppført returneres false
            for (int i=0; i<likerikke.length;i++) {
                if (likerikke[i] == p) {
                    return false;
                } else {
                    //Om de kjenner hverandre og ikke er uvenner er de per definisjon venner
                    return true;
                }
            }
        }
        //Om de ikke kjenner hverandre returneres false
        return false;
    }
    
    //Oppretter vennskap(i.e. kjennskap) eller sletter uvennskap
    public void blirVennMed(Person p) {
        //Sjekker om de kjenner hverandre
        if (erKjentMed(p)) {
            //Sjekker om de ikke liker hverandre og sletter i så fall uvennskapet
            for (int i = 0; i<likerikke.length; i++) {
                if (likerikke[i]==p) {
                    likerikke[i]=null;
                    return;
                }
            }
            //Om de ikke kjenner hverandre blir de kjent med hverandre
        } else {
            blirKjentMed(p);
        }
    }

    //Skriver ut venner
    public void skrivUtVenner() {
        //For hver person man kjenner...
        for (Person p: kjenner) {
            //Sjekkes det om de er venner og disse skrives ut
            if (erVennMed(p)) {
                System.out.println(hentNavn()+" er venn med "+p.hentNavn());
            }
        }
    }
    
    //Returnerer "bestevennen", eller null om vedkommende ikke har noen bestevenn
    public Person hentBestevenn() {
        if (kjenner[0] != null) {
            return kjenner[0];
        }
        return null;
    }
    
    //Returnerer en array med personens venner
    public Person[] hentVenner() {
        //Henter antall venner fra annen metode
        int i = antVenner();
        int j = 0;
        //Lager en array for venner som er stor nok for alle personens venner
        Person venner[] = new Person[i];
        //Går gjennom personens kjenninger ...
        for (Person p: kjenner) {
            //Sjekker om de er venner og setter de så inn i første ledige plass i arrayen
            if (erVennMed(p)) {
                venner[j]=p;
                j++;
            }
        }
        //Returnerer arrayen med vennene
        return venner;
    }
    
    //Returnerer antall venner
    public int antVenner() {
        int i=0;
        //For hver person en kjenner...
        for(Person p: kjenner) {
            //Sjekkes det om de er venner og antallet venner økes med en
            if (erVennMed(p)) {
                i++;
            }
        }
        //Returnerer antall venner når alle kjenninger er sjekket
        return i;
    }
    
    //Fra oppgaveteksten, skriver ut kjenninger
    public void skrivUtKjenninger() {
        for (Person p: kjenner) {
            if (p != null) {
                System.out.print(p.hentNavn() + " ");
            }
        }
        System.out.println("");
    }
    
    //Fra oppgaveteksten, skriver ut uvenner
    public void skrivUtLikerIkke() {
        for (Person p: likerikke) {
            if (p != null) {
                System.out.print(p.hentNavn() + " ");
            }
        }
        System.out.println("");
    }
    
    //Fra oppgaveteksten, skriver ut alt
    public void skrivUtAltOmMeg() {
        System.out.print(navn + " kjenner ");
        skrivUtKjenninger();
        if (forelsketi != null) {
            System.out.println(navn + " er forelsket i " + forelsketi.hentNavn());
        }
        if (likerikke[0]!=null) {
            System.out.print(navn + " liker ikke ");
            skrivUtLikerIkke();
        }
    }
}


class Test {
    void test() {
        //Oppretter de fire personene
        Person jeg = new Person("Endre", 3);
        Person Ask = new Person("Ask", 3);
        Person Dana = new Person("Dana", 3);
        Person Tom = new Person("Tom", 3);
        
        //Oppretter datastrukturen
        jeg.blirKjentMed(Ask);
        jeg.blirKjentMed(Dana);
        jeg.blirKjentMed(Tom);
        
        Ask.blirKjentMed(jeg);
        Ask.blirKjentMed(Tom);
        Ask.blirKjentMed(Dana);
        Ask.blirForelsketI(jeg);
        Ask.blirUvennMed(Dana);
        Ask.blirUvennMed(Tom);
        
        Dana.blirKjentMed(Ask);
        Dana.blirKjentMed(Tom);
        Dana.blirKjentMed(jeg);
        Dana.blirForelsketI(Tom);
        Dana.blirUvennMed(jeg);
        
        Tom.blirKjentMed(jeg);
        Tom.blirKjentMed(Ask);
        Tom.blirKjentMed(Dana);
        Tom.blirForelsketI(Dana);
        Tom.blirUvennMed(Ask);
        Tom.blirUvennMed(jeg);

        //Skriver ut testene
        jeg.skrivUtAltOmMeg();
        Ask.skrivUtAltOmMeg();
        Dana.skrivUtAltOmMeg();
        Tom.skrivUtAltOmMeg();

    }
}

class Oblig1 {
    public static void main (String [] args) {
        //Oppretter klassen Test
        Test T = new Test();
        //initialiserer metoden test
        T.test();
    }
}
