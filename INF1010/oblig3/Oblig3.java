/*
 * Pekeren til neste person i listen er ikke beskyttet som private. Dette
 * hadde vært ønskelig, men for enkelhetens skyld har jeg ikke tatt meg
 * bryet med å endre ListeAvPersoner for å støtte at denne blir gjort private.
 *
 * 
 */

class Person {
    Person neste;
    //Variable fra oblig 1 og 2, ikke endret for denne obligen
    private String navn;
    private Person[] kjenner;
    private Person[] likerikke;
    private Person forelsketi;
    private Person sammenmed;
    private String samlerpa;
    private Gave[] minegaver;
    
    //Konstruktor
    Person(String n) {
        navn=n;
        kjenner=new Person[100];
        likerikke=new Person[10];
    }
    
    //Sjekker om personen ikke liker en annen person
    public boolean likerIkke(Person p) {
        for (int i = 0; i<likerikke.length; i++) {
            if (likerikke[i] == p) return true;
        }
        return false;
    } 
    
    //Setter sammenmed til personen p, og setter personen p.sammenmed
    //til objektet this
    public void blirSammenMed(Person p) {
        if (this == p) return;
        sammenmed = p;
        p.sammenmed = this;
    }
    
    //Setter forelsketi til personen p
    public void blirForelsketI(Person p) {
        if (this == p) return;
        if (!likerIkke(p)) forelsketi=p;
    }
    
    //Finner en ledig plass i kjenner-arrayen (om mulig) og skriver
    //inn en ny person der.
    //Gir tilbakemelding om det ikke er noen ledige plasser
    public void blirKjentMed(Person p) {
        if (this == p) return;
        for (int i=0; i < kjenner.length; i++) {
            if (kjenner[i]==null) {
                kjenner[i]=p;
                return;
            }
        }
        System.out.println("Kjenner-arrayen hadde ingen ledige plasser");
    }
    
    //mottar to variable, en String og en int
    public void samlerAv(String smlp, int ant) {
        //Stringen bestemmer hva personen samler på
        samlerpa = smlp;
        //int bestemmer hvor mange gaver vedkommende kan ta i mot
        minegaver = new Gave[ant];
    }

    //Sjekker om "jeg" vil ha gaven selv
    public Gave vilJegHaGaven(Gave g) {
        //Sjekker at "jeg" samler på denne kategorien
        if (g.kategori().equals(samlerpa)) {
            //Finner en ledig plass å legge den
            for (int i=0; i<minegaver.length;i++) {
                if (minegaver[i] == null) {
                    minegaver[i] = g;
                    //Om "jeg" vil ha gaven peker g til null
                    g=null;
                }
            }
        }
        //gaven blir returnert, den kan enten inneholde et objekt
        //eller peke til null
        return g;
    }
    
    //Sender en gave til kjæresten og spør om den vil ha gaven
    public Gave vilPartnerHaGaven(Gave g) {
        if (sammenmed != null) {
            g=sammenmed.vilJegHaGaven(g);
        }
        return g;
    }

    //Sender en gave til den man er forelsket i og spør om den vil ha gaven
    public Gave vilForelskelseHaGaven(Gave g) {
        if (forelsketi != null) {
            g=forelsketi.vilJegHaGaven(g);
        }
        return g;
    }

    //Sender en gave til vennegjengen og sjekker en etter en om noen vil
    //ha gaven
    public Gave vilVennerHaGaven(Gave g) {
        //Lager en lokal array av alle venner
        Person venner[] = hentVenner();
        for (int i=0; i<venner.length;i++) {
            if (g != null) {
                //Sjekker om de vil ha gaven
                g=venner[i].vilJegHaGaven(g);
            }
        }
        return g;
    }
    
    //Tar i mot en gave
    public Gave taGaven(Gave g) {
        //Sender gaven til "jeg"-objektet
        g=vilJegHaGaven(g);
        //Om "jeg" ikke vil ha den, sender den til kjæreste
        if (g!=null) g=vilPartnerHaGaven(g);
        //Eller forelskelse
        if (g!=null) g=vilForelskelseHaGaven(g);
        //Eller venneflokken
        if (g!=null) g=vilVennerHaGaven(g);
        //Om noen ville ha gaven peker g til null før den blir returnert
        //Om ingen ville ha gaven returneres gaven her til det
        //opprinnelige kallet
        return g;
    }

    //Sjekker om personen er venn med personen p
    //Skal nå fungere selv om p ikke ligger på første plass i arrayen
    public boolean erVennMed(Person p) {
        //Sjekker at de kjenner hverandre
        if (erKjentMed(p)) {
            //Sjekker likerikke-arrayen, om p står oppført returneres false
            for (int i=0; i<likerikke.length;i++) {
                if (!likerIkke(p)) {
                    return true;
                }
            }
        }
        //Om de ikke kjenner hverandre returneres false
        return false;
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

    //Skriver ut alle gaver en person har mottatt
    //en gave per linje
    public void skrivUtGaver() {
        for (int i=0; i<minegaver.length; i++) {
            if (minegaver[i] != null) {
                System.out.println(minegaver[i].gaveId());
            }
        }
    }

    //Skriver ut alt
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
        if (sammenmed != null) 
            System.out.println(navn + " er sammen med " + sammenmed.hentNavn());
        if (minegaver[0] != null) {
            System.out.println(navn +" har mottatt disse gavene:");
            skrivUtGaver();
        }
    }

    /*
     *  Her folger metoder fra oblig 1 hvor det ikke er gjort endringer 
     *  for oblig 2. Disse er ikke rettet i henhold til tilbakemelding
     *  på oblig 1 da jeg ikke benytter metodene i denne løsningen
     */

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
    
    
    //Finner ledig plass i likerikke-arrayen og setter person p inn i den plassen
    public void blirUvennMed(Person p) {
        for (int i=0; i < likerikke.length; i++) {
            if (likerikke[i]==null) {
                likerikke[i]=p;
                return;
            }
        }
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
    
    //Returnerer "bestevennen", eller null om vedkommende 
    //ikke har noen bestevenn
    public Person hentBestevenn() {
        if (kjenner[0] != null) {
            return kjenner[0];
        }
        return null;
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
}

class Test {
    GaveLager gavelager = new GaveLager();
    Personer p = new Personer();
    ListeAvPersoner l = new ListeAvPersoner();

    Test() {
        int retgaver = 0;
        Person temp = p.hentPerson();
        while (temp != null) {
            l.settInnSist(temp);
            temp = p.hentPerson();
        }
        String[] personliste = p.hentPersonnavn();
        Gave g = gavelager.hentGave();
        while (g != null){
            for (int i = 0; i<personliste.length; i++) {
                if (g != null) {
                    temp = l.finnPerson(personliste[i]);
                    g = temp.taGaven(g);
                }
            }
            if (g != null) retgaver++;
            g = gavelager.hentGave();
        }
        l.skrivAlle();
    }
}

class Oblig3 {
    public static void main (String[] args) {
        Test T = new Test();
    }
}
