/* Jeg har ikke rukket å bli helt ferdig med denne obligen.
 * Innlesning og metode finnAntallUversDager skal være fungerende, men jeg har ikke hatt tid
 * til å lete etter eventuelle feil før innleveringsfristen. 
 * Jeg avsluttet programmeringen midt inne i metoden sammenlignVindVedKyststasjonerMotResten,
 * der jeg sluttet er det lagt inn en kommentar som forklarer hvordan jeg tenkte å skrive
 * metoden (linje 267).
 */

import java.util.*;
import java.io.*;

/**
 * Omsluttende klasse for problemet, tar opp parametere
 * fra kommandolinja og starter kommandoløkka. Feilmelding
 * hvis ikke minst to parametere
 *********************************************************/
class Meterologi {
    /**
     * Sjekker parametere, starter opp ordreløkka etter at
     * filene er lest via konstruktøren til 'MetInst'
     *****************************************************/
    public static void main (String[] args) {
        if(args.length >= 2) {
            System.out.println("Starter ...");
            MetInst m = new MetInst(args[0], args[1]);
            m.ordreløkke();
        } else {
            System.out.println("Bruk: >java Meterologi <fil med Stasjonsdata> <fil med Observasjonsdata>");
        }
    }
} //end class Meterologi

class MetInst{
    static HashMap<String, Stasjon> stasjonHash = new HashMap<String, Stasjon>();
    HashMap<String, Region> regionHash = new HashMap<String, Region>();
    MetInst(String stasjonsdata, String klimadata) {
        lesStasjonsdata(stasjonsdata);
        lesKlimadata(klimadata);
    } //end konstruktør
    
    void ordreløkke() {
        Scanner in = new Scanner(System.in);
        String valg = "0";
        while (!(valg.equals("9"))) {
            System.out.println("\nMenyvalg:");
            System.out.println("1. Finn antall uværsdager");
            System.out.println("2. Sammenlign vind ved kyststasjoner mot resten");
            System.out.println("3. Sammenlign Østlandet mot Nord-Norge");
            System.out.println("4. Finn kaldeste værstasjons for en måned");
            System.out.println("5. Skriv ut varmeste region for hver måned");
            System.out.println("6. Skriv ut en oversikt over alle stasjoner med stasjonsnummer");
            System.out.println("9. Avslutt");
            System.out.println();
            System.out.print("Velg et alternativ: ");
            valg = in.nextLine();
            if (valg.equals("1")) {
                valgEn();
            } else if (valg.equals("2")) {
                sammenlignVindVedKyststasjonerMotResten();
            } else if (valg.equals("3")) {
                //sammenlignOstlandetMotNordNorge (int maaned)
            } else if (valg.equals("4")) {
                //finnKaldesteVerstasjonMaaned (int maaned)
            } else if (valg.equals("5")) {
                skrivUtVarmesteRegionForHverMaaned();
            } else if (valg.equals("6")) {
                skrivUtStasjoner();
            } else if (valg.equals("9")) {
                System.out.println("Avslutter ...");
            } else {
                System.out.println("Ugyldig valg, prøv igjen");
            }
        }
    } //end void ordreløkke

    void valgEn() {
        Scanner in = new Scanner(System.in);
        System.out.print("Vennligst angi stasjonsnummer som skal sjekkes (skriv Hjelp for å få en oversikt over alle stasjoner med stasjonsnummer): ");
        int stasjonsNummer = 0;
        int maaned = 0;
        String temp = in.nextLine();
        if (temp.equalsIgnoreCase("hjelp")) {
            skrivUtStasjoner();
            System.out.print("Vennligst angi stasjonsnummer som skal sjekkes (skriv Hjelp for å få en oversikt over alle stasjoner med stasjonsnummer): ");
            temp = in.nextLine();
        }
        if (!(temp.equalsIgnoreCase("hjelp"))) {
            try {
                stasjonsNummer = Integer.parseInt(temp);
            } catch (Exception e) {
                System.out.println("Angitt verdi ser ikke ut til å være en tallverdi");
            }
        }
        System.out.print("Vennligst angi hvilken måned som skal sjekkes (1-10): ");
        temp = in.nextLine();
        try {
            maaned = Integer.parseInt(temp);
        } catch (Exception e) {
            System.out.println("Gitt verdi ser ikke ut til å være en tallverdi");
        }
        finnAntallUversDager (stasjonsNummer, maaned);
    } //end void valgEn
    
    void lesStasjonsdata(String fil) {
        try{
            Scanner lesFil = new Scanner (new FileReader(fil));
            if (lesFil.hasNextLine()) {
                lesFil.nextLine();
            }
            while (lesFil.hasNext()) {
                int stnr = lesFil.nextInt();
                String navn = lesFil.next();
                int hoh = lesFil.nextInt();
                String kommune = lesFil.next();
                String fylke = lesFil.next();
                String region = lesFil.next();
                Stasjon stasjon = new Stasjon (stnr, navn, hoh, kommune, fylke, region);
                stasjonHash.put(stnr+"", stasjon);
                if (!regionHash.containsKey(region)) {
                    Region r = new Region (region);
                    regionHash.put(region, r);
                    r.stasjoniRegion.put(stnr+"", stasjon);
                } else {
                    Region r = regionHash.get(region);
                    r.stasjoniRegion.put(stnr+"", stasjon);
                }
            }
            lesFil.close();
        } catch (Exception e) {
            System.out.println("Det har oppstått en feil");
        }
    } //end void lesStasjonsdata
    
    void lesKlimadata(String fil) {
        try {
            Scanner lesTopp = new Scanner (new FileReader(fil));
            if (lesTopp.hasNextLine()) {
                for (int i = 0; i < 9; i++) {
                    lesTopp.nextLine();
                }
            }
            int stnr = 0;
            int mnd = 0;
            int dag = 0;
            double temperatur = -999;
            double nedbor24 = -999;
            double nedbor12 = -999;
            double vindret = -999;
            double vindhast = -999;
            boolean forsteLesning = true;
            while (lesTopp.hasNext()) {
                int stnrTopp = lesTopp.nextInt();
                int mndTopp = lesTopp.nextInt();
                int dagTopp = lesTopp.nextInt();
                int timeTopp = lesTopp.nextInt();
                double temperaturTopp = lesTopp.nextDouble();
                double nedbor24Topp = lesTopp.nextDouble();
                double nedbor12Topp = lesTopp.nextDouble();
                double vindretTopp = lesTopp.nextDouble();
                double vindhastTopp = lesTopp.nextDouble();
                if (!(stnrTopp == stnr && mndTopp == mnd && dagTopp == dag)) {
                    if (forsteLesning == false && stasjonHash.get(stnr+"").mndHash.containsKey(mnd+"")) {
                        stasjonHash.get(stnr+"").mndHash.get(mnd+"").lagDag(stnr, mnd, dag, temperatur, nedbor24, nedbor12, vindret, vindhast);
                    } else if (forsteLesning == false) {
                        Maaned maaned = new Maaned (stnr, mnd, dag, temperatur, nedbor24, nedbor12, vindret, vindhast);
                        stasjonHash.get(stnr+"").mndHash.put(mnd+"", maaned);
                    }
                    stnr = stnrTopp;
                    mnd = mndTopp;
                    dag = dagTopp;
                    temperatur = temperaturTopp;
                    nedbor24 = nedbor24Topp;
                    nedbor12 = nedbor12Topp;
                    vindret = vindretTopp;
                    vindhast = vindhastTopp;
                    forsteLesning = false;
                } else {
                    temperatur = temperaturSjekk(temperatur, temperaturTopp);
                    nedbor24 = nedbor24Sjekk(nedbor24, nedbor24Topp);
                    nedbor12 = nedbor12Sjekk(nedbor12, nedbor12Topp);
                    vindret = vindretSjekk(vindret, vindretTopp);
                    vindhast = vindhastSjekk(vindhast, vindhastTopp);
                }
                if (!lesTopp.hasNext()) {
                    Maaned maaned = new Maaned (stnr, mnd, dag, temperatur, nedbor24, nedbor12, vindret, vindhast);
                    stasjonHash.get(stnr+"").mndHash.put(mnd+"", maaned);
                }
            } //end while
            
            lesTopp.close();
        } catch (Exception e) {
            System.out.println("Det har oppstått en feil");
        }
    } //end void lesKlimadata

    static double temperaturSjekk(double temperatur, double temperaturTopp) {    
        if (temperatur == -999 && temperaturTopp == -999) {
            return temperaturTopp;
        } else if (temperatur != -999 && temperaturTopp == -999) {
            return temperatur;
        } else if (temperatur == -999 && temperaturTopp != -999) {
            return temperaturTopp;
        } else {
            return (temperatur + temperaturTopp)/2;
        }
    } //end double temperaturSjekk
    
    static double nedbor24Sjekk(double nedbor24, double nedbor24Topp) {
        if (nedbor24Topp != -999) {
            return nedbor24Topp;
        } else {
            return nedbor24;
        }
    } //end double nedbor24Sjekk
    
    static double nedbor12Sjekk (double nedbor12, double nedbor12Topp) {
        if (nedbor12 == -999 && nedbor12Topp == -999) {
            return nedbor12;
        }
        if (nedbor12 == -999 && nedbor12Topp != -999) {
            return nedbor12Topp;
        } else if(nedbor12 != -999 && nedbor12Topp == -999) {
            return nedbor12;
        } else {
            return (nedbor12 + nedbor12Topp);
        }
    } //end double nedbor12Sjekk
    
    static double vindretSjekk(double vindret, double vindretTopp) {
        if (vindret == -999 && vindretTopp != -999) {
            return vindretTopp;
        } else {
            return vindret;
        }
    } //end double vindretSjekk
    
    static double vindhastSjekk(double vindhast, double vindhastTopp) {
        if (vindhast == -999 && vindhastTopp != -999) {
            return vindhastTopp;
        } else {
            return vindhast;
        }
    } //end double vindhastSjekk
    
    void finnAntallUversDager (int stasjonsNummer, int maaned) {
        int antallDager = 0;
        for (String key : stasjonHash.get(stasjonsNummer+"").mndHash.get(maaned+"").dagHash.keySet()) {
            if (stasjonHash.get(stasjonsNummer+"").mndHash.get(maaned+"").dagHash.get(key).nedbor != -999 && stasjonHash.get(stasjonsNummer+"").mndHash.get(maaned+"").dagHash.get(key).vindhast != -999) {
                if (stasjonHash.get(stasjonsNummer+"").mndHash.get(maaned+"").dagHash.get(key).nedbor + stasjonHash.get(stasjonsNummer+"").mndHash.get(maaned+"").dagHash.get(key).vindhast > 10.7) {
                    antallDager++;
                }
            } else if (stasjonHash.get(stasjonsNummer+"").mndHash.get(maaned+"").dagHash.get(key).nedbor > 10.7 || stasjonHash.get(stasjonsNummer+"").mndHash.get(maaned+"").dagHash.get(key).vindhast > 10.7) {
                antallDager++;
            }
        }
        System.out.println("Det har i måned " + maaned + " blitt målt " + antallDager +" uværsdager ved målestasjon " + stasjonHash.get(stasjonsNummer+"").navn);
    } //end void finnAntallUversDager
    
    void sammenlignVindVedKyststasjonerMotResten() {
        double vindKyst = -999;
        double vindInnland = -999;
        for (String key : stasjonHash.keySet()) {
            if (stasjonHash.get(key).hoh < 60) {
                for (String keym : stasjonHash.get(key).mndHash.keySet()) {
                    for (String keyd : stasjonHash.get(key).mndHash.get(keym).dagHash.keySet()) {
                        if (stasjonHash.get(key).mndHash.get(keym).dagHash.get(keyd).vindhast != -999) {
                            //Denne løkken blar seg gjennom alle dager som er registrert, og er tenkt å regne ut gjennomsnittsmengder ved hjelp av en metode lik temperaturSjekk(double temperatur, double temperaturTopp). En liknende løkke for å regne ut gjennomsnittet innlands.
                        }
                    }
                }
            } /*end if kyststasjon*/
        }
    } //end void sammenlignVindVedKyststasjonerMotResten

    void sammenlignOstlandetMotNordNorge (int maaned) {
        
    } //end void sammenlignOstlandetMotNordNorge

    void finnKaldesteVerstasjonMaaned (int maaned) {
        
    } //end void finnKaldesteVerstasjonMaaned

    void skrivUtVarmesteRegionForHverMaaned() {
        
    } //end skrivUtVarmesteRegionForHverMaaned

    void skrivUtStasjoner() {
        for (String key : stasjonHash.keySet()) {
            String navn = stasjonHash.get(key).navn;
            System.out.println(navn+" stasjon har stasjonsnummer "+key);
        }
    } //end void skrivUtStasjoner
} //end class MetInst

class Region {
    String regionNavn;
    HashMap<String, Stasjon> stasjoniRegion = new HashMap<String, Stasjon>();
    Region (String navn) {
        regionNavn = navn;
    }
} //end class Region

class Stasjon {
    HashMap<String, Maaned> mndHash = new HashMap<String, Maaned>();
    int stnr;
    String navn;
    int hoh;
    String kommune;
    String fylke;
    String region;
    Stasjon(int stnr, String navn, int hoh, String kommune, String fylke, String region) {
        this.stnr = stnr;
        this.navn = navn;
        this.hoh = hoh;
        this.kommune = kommune;
        this.fylke = fylke;
        this.region = region;
    } //end konstruktør
} //end class Stasjon

class Maaned {
    HashMap<String, Dag> dagHash = new HashMap<String, Dag>();
    int stnr = 0;
    int mnd = 0;
    double temperatur = -999;
    double nedbor24 = 0;
    double nedbor12 = 0;
    
    Maaned (int stnr, int mnd, int dag, double temperatur, double nedbor24, double nedbor12, double vindret, double vindhast) {
        this.stnr = stnr;
        this.mnd = mnd;
        this.temperatur = MetInst.temperaturSjekk(this.temperatur, temperatur);
        this.nedbor24 += MetInst.nedbor24Sjekk(this.nedbor24, nedbor24);
        this.nedbor12 += MetInst.nedbor12Sjekk(this.nedbor12, nedbor12);
        Dag d = new Dag(stnr, mnd, dag, temperatur, nedbor24, nedbor12, vindret, vindhast);
        dagHash.put(dag+"", d);
    } //end konstruktør

    void lagDag (int stnr, int mnd, int dag, double temperatur, double nedbor24, double nedbor12, double vindret, double vindhast) {
        this.stnr = stnr;
        this.mnd = mnd;
        this.temperatur = MetInst.temperaturSjekk(this.temperatur, temperatur);
        this.nedbor24 += MetInst.nedbor24Sjekk(this.nedbor24, nedbor24);
        this.nedbor12 += MetInst.nedbor12Sjekk(this.nedbor12, nedbor12);
        Dag d = new Dag(stnr, mnd, dag, temperatur, nedbor24, nedbor12, vindret, vindhast);
        dagHash.put(dag+"", d);
    } //void lagDag

} //end class Maaned

class Dag {
    int stnr;
    int mnd;
    int dag;
    int time;
    double temperatur;
    double nedbor;
    double vindret;
    double vindhast;
    Dag (int stnr, int mnd, int dag, double temperatur, double nedbor24, double nedbor12, double vindret, double vindhast) {
        this.stnr = stnr;
        this.mnd = mnd;
        this.dag = dag;
        this.temperatur = temperatur;
        if (nedbor24 != -999) {
            this.nedbor = nedbor24;
        } else if (nedbor12 != -999) {
            this.nedbor = nedbor12;
        } else {
            nedbor = -999;
        }
        this.vindret = vindret;
        this.vindhast = vindhast;
    }
} //end class Dag
