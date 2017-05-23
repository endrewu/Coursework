import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
//importerer aktuelle ting
class Hjelp {
    //Oppretter beholdere for personer, resepter, leger og legemidler
    Tabell<Personer> personBeholder = new Tabell<Personer>(30);
    SortertEnkelListe<Leger> legeliste = new SortertEnkelListe<Leger>();
    Tabell<Legemiddel> legemiddelBeholder = new Tabell<Legemiddel>(50);

    //Tellere for å holde oversikt på antall legemidler, personer og resepter
    int antallLM = 0;
    int antallPers = 0;
    int antallRes = 0;

    //Innlesning fra fil
    void innlesning() {
        File fil = null;
        //Sjekker om fil eksisterer
        if(new File("data.txt").exists()) {
            fil = new File("data.txt");
            try {
                //Oppretter scanner
                Scanner inn = new Scanner(fil);
                int i = -1; //Holder styr på hva vi leser
                while (inn.hasNext()) {
                    String input = inn.nextLine();
                    
                    //Om linjen ikke inneholder data for objekter, leser hva slags objekter som følger
                    if (input.startsWith("#")) {
                        if (input.contains("Personer")) i=1; 
                        if (input.contains("Legemidler")) i=2;
                        if (input.contains("Leger")) i=3;
                        if (input.contains("Resepter")) i=4;

                        //Dersom linjen ikke er tom leser dataene
                    } else if(!(input.isEmpty())){
                        if( i == 1){ //Personer

                            //Splitter linjen i brukbare variable
                            String[] splitArray = input.split(",");
                            int nr = Integer.parseInt(splitArray[0].trim());
                            String navn = splitArray[1].trim();
                            char kjonn = splitArray[2].trim().charAt(0);
                        
                            //Oppretter og øker antall
                            opprettPerson(nr, navn, kjonn);
                            antallPers++;

                        }else if( i == 2){ //Legemidler

                            //Splitter linjen i brukbare variable
                            String[] splitArray = input.split(",");
                            int nr = Integer.parseInt(splitArray[0].trim());
                            int pris = Integer.parseInt(splitArray[4].trim());
                            int mengde = Integer.parseInt(splitArray[5].trim());
                            char type = splitArray[3].trim().charAt(0);
                            String form = splitArray[2].trim();
                            String navn = splitArray[1].trim();
                            int styrke;
                        
                            //Tilrettelegger for skillet mellom A, B og C-klasse legemidler
                            if(splitArray.length == 7){
                                styrke = Integer.parseInt(splitArray[6].trim());
                            }else{
                                styrke = 0;
                            }

                            //Oppretter og øker antall
                            opprettLegemiddel(nr,navn,form,type,pris,mengde,styrke);
                            antallLM++;
                   
                        }else if( i == 3){ //Leger

                            //Splitter linjen i brukbare variable
                            String[] splitArray = input.split(",");
                            String navn = splitArray[0].trim();
                            int spesialist = Integer.parseInt(splitArray[1].trim());
                            int avtaleNr = Integer.parseInt(splitArray[2].trim());
                         
                            //Oppretter
                            opprettLege(navn,spesialist,avtaleNr);
                        
                        }else if( i == 4){ //Resepter

                            //Splitter linjen i brukbare variable
                            String[] splitArray = input.split(",");
                            int nr = Integer.parseInt(splitArray[0].trim());
                            char farge = splitArray[1].trim().charAt(0);
                            int persNr = Integer.parseInt(splitArray[2].trim());
                            String legeNavn = splitArray[3].trim();
                            int legemiddelNr = Integer.parseInt(splitArray[4].trim());
                            int reit = Integer.parseInt(splitArray[5].trim()); 

                            //Oppretter og øker antall
                            opprettResept(nr,farge,persNr,legeNavn,legemiddelNr,reit);
                            antallRes++;
                        }
                    }
                }
            } catch (FileNotFoundException e){
                System.out.println("Filen kunne ikke leses");
            }
        }
    }
    
    void opprettPerson(int nr, String navn, char kjonn) {

        Personer temp = new Personer(nr, navn, kjonn); //Oppretter objekt
        personBeholder.settInn(temp,nr); //Setter inn
    }

    void opprettResept(int nr,char farge,int persNr,String legeNavn,int legemiddelNr,int reit) {

        Personer retPerson = personBeholder.finnObjekt(persNr); //Finn personpeker
        Leger retLege = legeliste.finnObjekt(legeNavn); //Finn legepeker
        Legemiddel retLM = legemiddelBeholder.finnObjekt(legemiddelNr); //Finn legemiddelpeker

        Resepter temp = new Resepter(nr,farge,persNr,retLege,retLM,reit,retPerson); //Oppretter objekt
        retLege.beholder.settInn(temp); //Setter inn i legens beholder
        retPerson.beholder.settInn(temp); //Setter inn i personens beholder
    }

    void opprettLege(String navn, int spesialist, int avtaleNr){
        Leger temp = new Leger(navn,spesialist,avtaleNr); //Oppretter
        legeliste.settInn(temp); //Setter inn
    }
    //metode for aa opprette legemiddel. I parameteret sender vi med infoen som trengs for aa opprette et objekt av legemiddel. 
    void opprettLegemiddel(int nr, String navn, String form, char type, int pris, int mengde, int styrke){
        if(type == 'a'){		//Her sjekker vi hva slags type legemiddel som skal opprettes	
            if(form.equals("pille")){
                
                Legemiddel temp = new LegemiddelAPille(nr,navn,form,type,pris,mengde,styrke);
                legemiddelBeholder.settInn(temp,nr);
            }else if(form.equals("liniment")){
                Legemiddel temp = new LegemiddelALiniment(nr,navn,form,type,pris,mengde,styrke);
                legemiddelBeholder.settInn(temp,nr);

            }else if(form.equals("injeksjon")){
                Legemiddel temp = new LegemiddelAInjeksjon(nr,navn,form,type,pris,mengde,styrke);
                legemiddelBeholder.settInn(temp,nr);

            }
		   
        }else if(type == 'b'){
			
            if(form.equals("pille")){
                Legemiddel temp = new LegemiddelBPille(nr,navn,form,type,pris,mengde,styrke);
                legemiddelBeholder.settInn(temp,nr);

            }else if(form.equals("liniment")){
                Legemiddel temp = new LegemiddelBLiniment(nr,navn,form,type,pris,mengde,styrke);
                legemiddelBeholder.settInn(temp,nr);

            }else if(form.equals("injeksjon")){
                Legemiddel temp = new LegemiddelBInjeksjon(nr,navn,form,type,pris,mengde,styrke);
                legemiddelBeholder.settInn(temp,nr);

            }
        }else if(type == 'c'){
            if(form.equals("pille")){
                Legemiddel temp = new LegemiddelPille(nr,navn,form,type,pris,mengde);
                legemiddelBeholder.settInn(temp,nr);

            }else if(form.equals("liniment")){
                Legemiddel temp = new LegemiddelLiniment(nr,navn,form,type,pris,mengde);
                legemiddelBeholder.settInn(temp,nr);

            }else if(form.equals("injeksjon")){
                Legemiddel temp = new LegemiddelInjeksjon(nr,navn,form,type,pris,mengde);
                legemiddelBeholder.settInn(temp,nr);
            }
        }
    }

    void meny() {//metoden vi bruker for ordestyrt meny. 
	Scanner inn = new Scanner(System.in);//to skannere, en for innlesning av menyvalg
	Scanner menyvalg = new Scanner(System.in);//og en for bruk av oppretting av objekter
	String valg;//Stringen vi bruker for å finne hvilken order brukeren har valgt
	do {
	    System.out.println("\nVelg hva du vil gjore");
	    System.out.println("1: Opprette og legge inne et nytt legemiddel");
	    System.out.println("2: Opprette og legge inn en ny lege");
	    System.out.println("3: Opprette og legge inn en ny person");
	    System.out.println("4: Opprette og legge inn en ny resept");
	    System.out.println("5: Hente et legemiddel");
            System.out.println("6: Skriv ut all info");
            System.out.println("7: Skriv ut en persons gyldige blå resepter");
            System.out.println("8: Skriv ut alle avtaleleges resepter på narkotiske stoffer");
            System.out.println("9: Skriv ut alle personers vanedannende resepter");
	    System.out.println("q: Avslutt systemet");
		
	    valg = menyvalg.nextLine();
	    if(valg.equals("1")){ //Legemiddel
                System.out.println("Du har valgt aa legge inn et nytt legemiddel");
                
                //Navn
                System.out.print("Tast inn navn paa legemiddelet og trykk enter: ");
                String navn = inn.nextLine();
                
                //Form
                System.out.print("Tast inn form paa legemiddelet og trykk enter: ");
                String form = inn.nextLine();
                
                //Type
                System.out.print("Tast inn type og trykk enter: ");
                String type = inn.nextLine().trim();
                char cType = type.charAt(0);
                
                //Pris
                System.out.print("Tast inn pris og trykk enter: ");
                int pris = inn.nextInt();
                
                //Mengde
                System.out.print("Tast inn mengde og trykk enter: ");
                int mengde = inn.nextInt();
                
                //Styrke
                if(cType == 'a' || cType == 'b'){
                    System.out.print("Tast inn styrke og trykk enter: ");
                    int styrke = inn.nextInt();

                    //Opprett objektet
                    opprettLegemiddel(antallLM,navn,form,cType,pris,mengde,styrke);
                }else if(cType == 'c'){
                    opprettLegemiddel(antallLM,navn,form,cType,pris,mengde,0);
                }	
		
		
	    }else if(valg.equals("2")){ //Lege
                System.out.println("Du har valgt aa legge inn en ny lege");

                //Navn
                System.out.print("Skriv inn navn og trykk enter: ");
                String navn = inn.nextLine();
                
                //Spesialist eller ikke
                System.out.print("Skriv inn 1 om lege er spesialist, ellers 0: ");
                int spesialist = inn.nextInt();

                //Avtalenummer
                System.out.print("Skriv inn avtalenummer om lege har, ellers 0: ");
                int avtaleNr = inn.nextInt();
			
                opprettLege(navn,spesialist,avtaleNr);
			
	    }else if(valg.equals("3")){ //person

                boolean test = false;
                System.out.println("Du har valgt aa legge inn en ny person");
                
                //navn
                System.out.print("Skriv inn navn og trykk enter: ");
                String navn = inn.nextLine();

                //kjonn
                char kjonn = 'a';
                while (test == false) {
                    System.out.print("Skriv inn personens kjønn (m/k): ");
                    String temp = inn.nextLine().toLowerCase().trim();
                    kjonn = temp.charAt(0);
                    if ( kjonn == 'm' || kjonn == 'k') test = true;
                    else System.out.println("Ikke godkjent som kjønn, prøv på nytt");
                }

                //oppretter objektet
                opprettPerson(antallPers, navn, kjonn);
                antallPers++;

	    }else if(valg.equals("4")){ //resept

                boolean test = false;
                
                System.out.println("Du har valgt aa legge inn en ny resept");
                
                //Farge på resepten
                char farge = 'a';
                while (test == false) {
                    System.out.print("Skriv inn reseptens farge (h/b): ");
                    String temp = inn.nextLine().toLowerCase().trim();
                    farge = temp.charAt(0);
                    if ( farge == 'h' || farge == 'b') test = true;
                    else System.out.println("Ikke godkjent farge, prøv på nytt");
                }

                //Personnummer for eier
                test = false;
                int persNummer = -1;
                while ( test == false ) {
                    System.out.print("Skriv inn personnummeret til pasienten: ");
                    String pers = inn.nextLine();
                    persNummer = Integer.parseInt(pers);
                    if (personBeholder.finnObjekt(persNummer) != null) test = true;
                    if (personBeholder.finnObjekt(persNummer) == null) System.out.println("Personen eksisterer ikke, prøv på nytt");
                }

                //Navn på utskrivende lege
                System.out.print("Skriv inn navnet på den utskrivende lege: ");
                test = false;
                String legeNavn = null;
                while ( test == false ) {
                    System.out.print("Skriv inn navnet på den utskrivende lege: ");
                    legeNavn = inn.nextLine().trim();
                    for (Leger lege : legeliste) {
                        if (lege.samme(legeNavn)) test = true;
                    }
                    if (test == false) {
                        System.out.println(legeNavn + " er ikke en registrert lege.");
                        System.out.println("Sjekk skrivemåten eller registrer " + legeNavn + " som ny lege før resepten legges inn");
                    }
                }
                
                //Legemiddelnummer
                int legemiddelNummer = -1;
                test = false;
                while (test == false) {
                    System.out.print("Skriv inn legemiddelets nummer og trykk enter: ");
                    String temp = inn.nextLine();
                    legemiddelNummer = Integer.parseInt(temp);
                    if (legemiddelBeholder.finnObjekt(legemiddelNummer) != null) test = true;
                    if (legemiddelBeholder.finnObjekt(legemiddelNummer) == null) System.out.println("Legemiddelet eksisterer ikke, prøv på nytt");
                }
                
                //Reit
                System.out.print("Skriv inn reseptens reit og trykk enter: ");
                String Sreit = inn.nextLine();
                int reit = Integer.parseInt(Sreit);

                //Opprett objektet
                opprettResept(antallRes, farge, persNummer, legeNavn, legemiddelNummer, reit);
                antallRes++;
                
	    }else if(valg.equals("5")){ //Hente legemiddel
                boolean test = false;

                //Personnummer for eier
                test = false;
                int persNummer = -1;
                Personer eier = null;
                while ( test == false ) {
                    System.out.print("Skriv inn personnummeret til pasienten: ");
                    String pers = inn.nextLine();
                    persNummer = Integer.parseInt(pers);
                    if (personBeholder.finnObjekt(persNummer) != null) {
                        eier = personBeholder.finnObjekt(persNummer);
                        test = true;
                    }
                    if (personBeholder.finnObjekt(persNummer) == null) System.out.println("Personen eksisterer ikke, prøv på nytt");
                }

                //Reseptnummer
                test = false;
                int reseptNummer = -1;
                Resepter resept = null;
                while ( test == false ) {
                    System.out.print("Skriv inn reseptens løpenummer: ");
                    String temp = inn.nextLine();
                    reseptNummer = Integer.parseInt(temp);
                    if (eier.beholder.finnResept(reseptNummer) != null) {
                        resept = eier.beholder.finnResept(reseptNummer);
                       
                        test = true;
                        if (eier.beholder.finnResept(reseptNummer) == null) System.out.println("Resepten eksisterer ikke, prøv på nytt");
                    }
                }
                resept.hentUt();
            }else if(valg.equals("6")){ //All info
                System.out.println("----------Legemidler: ---------");
                for(Legemiddel lm : legemiddelBeholder){
                    lm.skriv();
                }
                System.out.println("----------Leger: ---------");
                for(Leger l : legeliste){
                    l.skriv();
                }
                System.out.println("----------Personer: ---------");
                for(Personer p : personBeholder){
                    p.skriv();
                }
            }else if(valg.equals("7")){//En persons blå resepter
                boolean test = false;
                
                //Personnummer
                test = false;
                int persNummer = -1;
                Personer eier = null;
                while ( test == false ) {
                    System.out.println("Vennligst skriv inn personnummeret til personen du vil se på: ");
                    System.out.print("Personnummer: ");
                    String pers = inn.nextLine();
                    persNummer = Integer.parseInt(pers);
                    if (personBeholder.finnObjekt(persNummer) != null) {
                        eier = personBeholder.finnObjekt(persNummer);
                        test = true;
                    }
                    if (personBeholder.finnObjekt(persNummer) == null) System.out.println("Personen eksisterer ikke, prøv på nytt");
                }
                int antallBR = 0;
                int antallDoser = 0;
                //metode for å se om legemiddel er av injeksjon og så legger inn antall reit i variabel
                for (Resepter r: eier.beholder) {
                    if (r.hentFarge() == 'b') {
                        if (r.hentReit() > 0) {
                            antallBR ++;
                            if (r.hentLegemiddel() instanceof Injeksjon) antallDoser += r.hentReit();
                        }
                    }
                }
                System.out.println(eier.hentNavn() + " har " + antallBR + " gyldige blå resepter og til sammen er det " + antallDoser + " antall injeksjonsdoser igjen");

            }else if(valg.equals("8")){//sjekker om lege har skrevet ut narkotiske resepter
                //A-legemidler
                int narklm;
                for (Leger l : legeliste) {
                    narklm = 0;
                    if (l.avtalenummer() > 0) {
                        System.out.print(l.hentNavn());
                        for ( Resepter r: l.beholder) {
                            if (r.hentLegemiddel() instanceof LegemiddelA) narklm++;
                        }
                    System.out.println(" har skrevet ut " + narklm + " resepter på narkotiske legemidler.");
                    }
                }
            }else if(valg.equals("9")){//sjekker om lege har skrevet vanedannene legemidler og til hvilket kjonn
                //B-legemidler
                int vanedlm;
                int totalVaneMenn = 0;
                int totalVaneKvinner = 0;
                for (Personer p: personBeholder) {
                    vanedlm = 0;
                    System.out.print(p.hentNavn());
                    for (Resepter r: p.beholder) {
                        if (r.hentLegemiddel() instanceof LegemiddelB && r.hentReit() > 0){
                            if(p.hentKjonn() == 'm'){
                                totalVaneMenn++;
                            }else if(p.hentKjonn() == 'k'){
                                totalVaneKvinner++;
                            }
                            vanedlm++;
                            
                            
                        }
                    }System.out.println(" har " + vanedlm + " gyldige resepter for vanedannende legemidler");
                }
                System.out.println("Totalt er det skrevet ut " + (totalVaneKvinner+totalVaneMenn) + " vanedannende stoffer. Av disse er " + totalVaneKvinner + " skrevet ut til kvinner og " + totalVaneMenn + " til menn.");
            }else if(valg.equals("q")){ //Avslutt
                System.out.println("Avslutter systemet");
            }else {//Feilmelding hvis bruker taster inn feil
                System.out.print("Du må skrive inn et tall: ");
            }
        } while (! (valg.equals("q")) );//Når bruker skriver inn q avsluttes systemet
    }
}

class Oblig4 {//mainklassen som sparker i gang systemet
    public static void main (String[] args) {
        Hjelp h = new Hjelp();
        h.innlesning();
        h.meny();
    }
}
