/*
  I etterp�klokskapens �nd har jeg innsett en del ting som nok kunne v�rt gjort smartere
  i dette programmet. Jeg kunne nok ha benyttet meg av flere hjelpemetoder, f.eks. en for
  � nullstille beboer og saldo ved utflytting/utkasting. Det finnes ogs� muligens bedre
  m�ter � ta seg av utskrift fra de ulike metodene.

  Det er ogs� mange sm�ting som kunne v�rt utbedret. For eksempel kunne jeg brukt try
  catch ved flere input fra bruker, spesielt ved oppdatering av husleie hvor jeg, for
  enkelhets skyld ber om nextInt, som vil f� programmet til � kr�sje dersom den f�r 
  en annen input.
  Det hadde ogs� v�rt en smal sak � be om leieprisene f�r oppretting av hybeldata.txt
  men jeg fant det un�dvendig i denne sammenheng.
*/

import java.util.*;
import java.io.*;

class Oblig3 {
    public static void main (String[] args) {
        Utsyn u = new Utsyn();
        u.innlesning();
        u.meny();
    } //end main
} //end Oblig3

class Utsyn {
    Hybel[][] h = new Hybel[3][6];
    int maned;
    int ar;
    int totalfortjeneste;
    int totaltAntallManeder;
    int manedsleie;
    int manedsleieTopp;
    
    //Leser inn hybeldata.txt og registrerer dataene lokalt i programmet.
    //Dersom det ikke eksisterer en fil kalt hybeldata.txt blir bruker bedt
    //om m�ned og �r for start av systemet, og filen opprettes.
    void innlesning() {
        if (new File("hybeldata.txt").exists()) {
            try{
                //Leser filen, splitter hver linje p� separatortegnet "; "
                //og lagrer dataene i programmet
                BufferedReader fil = new BufferedReader(new FileReader("hybeldata.txt"));
                String[] temp = fil.readLine().split("; ");
                maned = Integer.parseInt(temp[0]);
                ar = Integer.parseInt(temp[1]);
                totalfortjeneste = Integer.parseInt(temp[2]);
                totaltAntallManeder = Integer.parseInt(temp[3]);
                manedsleie = Integer.parseInt(temp[4]);
                manedsleieTopp = Integer.parseInt(temp[5]);
                //Dytter dataene til hybelobjektene
                for(int i = 0; i < 3; i++) {
                    for(int j = 0; j < 6; j++) {
                        String[] temphybel = fil.readLine().split("; ");
                        int etasje = Integer.parseInt(temphybel[0]);
                        char bokstav = temphybel[1].charAt(0);
                        int saldo = Integer.parseInt(temphybel[2]);
                        String student = new String();
                        if (temphybel[3].equals("TOM HYBEL")) {
                            student = null;
                        } else {
                            student = temphybel[3];
                        }
                        if (i==2) {
                            h[i][j] = new Hybel(etasje, bokstav, saldo, student, manedsleieTopp);
                        } else{
                            h[i][j] = new Hybel(etasje, bokstav, saldo, student, manedsleie);
                        }
                    }
                } //end for
                fil.close();
            } catch (Exception e) {
                System.out.println("Det har oppst�tt en feil");
            } //end try catch
        } else {
            opprettHybeldata();
        } //end if else
    } //end innlesning
    
    //Skriver ut menyen og registrerer valg, sender videre til andre metoder
    void meny() {
        Scanner input = new Scanner(System.in);
        String valg;
        //Skriver ut menyen og registrerer valg
        do {
            System.out.println("Velg et menyalternativ:");
            System.out.println("1. Skriv oversikt");
            System.out.println("2. Registrer ny leietager");
            System.out.println("3. Registrer betaling fra leietager");
            System.out.println("4. Registrer frivillig utflytting");
            System.out.println("5. M�nedskj�ring av husleie");
            System.out.println("6. Kast ut leietagere");
            System.out.println("7. �k husleien");
            System.out.println("8. Avslutt");
            System.out.println();
            System.out.print("Tast inn ditt valg: ");
            valg = input.nextLine();
            //Starter metoden som er valgt i menyen
            if (valg.equals("1")){
                skrivOversikt();
            } else if (valg.equals("2")) {
                registrerLeietager();
            } else if (valg.equals("3")) {
                registrerBetaling();
            } else if (valg.equals("4")) {
                registrerFlytting();
            } else if (valg.equals("5")) {
                manedskjoring();
            } else if (valg.equals("6")) {
                registrerUtkasting();
            } else if (valg.equals("7")) {
                okHusleie();
            } else if (valg.equals("8")) {
                avslutt();
                return;
            } else {
                System.out.println("Ikke-definert valg, vennligst pr�v igjen...\n");
            }
        } while (! (valg.equals("8")));  //end do while
    } //end meny
    
    //Skriver oversikten over hybelhuset, formatert til tabell, s�
    //utskriften ikke er avhengig av lengden p� beboers navn
    void skrivOversikt() {
        System.out.println("Skriver ut oversikt...");
        System.out.println("Hybel  Leietager\t\tSaldo");
        System.out.println("------ ------------------------ ------");
        for(int i = 0; i<h.length; i++) {
            for(int j = 0; j<h[i].length; j++) {
                if(h[i][j].student != null) {
                    System.out.printf("%1d%-6c%-25s%-6d", h[i][j].etasje, h[i][j].bokstav, h[i][j].student, h[i][j].saldo);
                    System.out.println();
                } /*end if*/ else {
                    System.out.printf("%1d%-6c%-25s%-6d", h[i][j].etasje, h[i][j].bokstav, "TOM LEILIGHET", h[i][j].saldo);
                    System.out.println();
                } //end else
            }
        } //end for
        System.out.println();
        System.out.println(maned+"/"+ar+", og driftstid: "+totaltAntallManeder+" m�neder i drift.");
        System.out.println("Totalfortjeneste: "+totalfortjeneste);
        System.out.println();
    } //end skrivOversikt()
    
    //Registrerer leietager
    void registrerLeietager() {
        Scanner input = new Scanner(System.in);
        System.out.println("*** Registrer leietager ***");
        int test=-1; //For � sjekke at det faktisk er ledige hybler
        System.out.println("F�lgende hybler er ledige:");
        for (int i=0; i<h.length; i++) {
            for (int j=0; j<h[i].length; j++) {
                if (h[i][j].student == null) {
                    System.out.print(h[i][j].etasje+""+h[i][j].bokstav+"\t");
                    test++;
                } //end if
            }
            System.out.println();
        } //end for
        System.out.println();

        //Returnerer til meny, dersom alle hyblene er opptatt
        if (test==-1) {
            System.out.println("Det er dessverre ingen ledige hybler");
            return;
        } //end if
        
        //Registrerer valgt hybel med beboer og trekker leie
        System.out.print("Vennligst angi hvilken hybel du �nsker � leie ut: ");
        String temp = input.nextLine().toUpperCase();
        int[] rom = finnHybel(temp);
        int e = rom[0], b = rom[1];
        if (e==-1 || b==-1) {
            return;
        } //end if
        if (h[e][b].student!=null) {
            System.out.println("Hybelen er ikke tom, innflytting avbrutt");
            return;
        } //end if
        System.out.println("Vennligst angi studentens navn");
        h[e][b].student = input.nextLine();
        h[e][b].saldo = 15000;
        h[e][b].saldo = h[e][b].saldo - h[e][b].leie;
        totalfortjeneste += h[e][b].leie;
    } //end registrerLeietager()
    
    //Registrerer betaling for valgt hybel
    void registrerBetaling() {
        System.out.println("*** Registrer betaling ***");
        Scanner input = new Scanner(System.in);
        System.out.print("Vennligst oppgi hvilken hybel det skal registreres betaling for: ");
        String temp = input.nextLine().toUpperCase();
        int[] rom = finnHybel(temp);
        int e = rom[0], b = rom[1];
        if (e==-1 || b==-1) {
            return;
        } //end if
        //Godtar ikke betaling for tom hybel
        if (h[e][b].student==null) {
            System.out.println("Hybelen er tom, betaling kan ikke registreres\n");
            return;
        }
        System.out.print("Hvor mye penger skal registreres p� saldo? ");
        h[e][b].saldo+=input.nextInt();
    } //end registrerBetaling()

    //Registrerer frivillig utflytting
    void registrerFlytting() {
        System.out.println("*** Registrer utflytting ***");
        Scanner input = new Scanner(System.in);
        System.out.print("Vennligst oppgi navnet p� studenten som �nsker � flytte: ");
        String navn = input.nextLine();
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[i].length; j++) {
                if (navn.equals(h[i][j].student)) {
                    //I tilfelle studenten har utest�ende leie blir operasjonen
                    //avbrutt. 
                    if (h[i][j].saldo < 0) {
                        System.out.println("Beboer har ikke betalt husleie, operasjonen avbrytes");
                        return;
                    } else {
                        h[i][j].student = null;
                        h[i][j].saldo-=h[i][j].saldo;
                    }
                } //end if
            }
        } //end for
    } //end registrerFlytting()

    //Starter m�nedskj�ring
    void manedskjoring() {
        Scanner input = new Scanner(System.in);
        //Bekrefter m�nedskj�ring
        System.out.print("Vennligst bekreft at du �nsker � foret� en m�nedskj�ring (j/n): ");
        String svar = input.nextLine();
        if (!(svar.equals("j"))) {
            System.out.println("M�nedskj�ring avbrytes...\n");
            return;
        } //end if
        System.out.println("*** M�nedskj�ring ***");
        int manedsfortjeneste = 0;
        //betaling av leie
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[i].length; j++) {
                if (h[i][j].student != null) {
                    if (h[i][j].saldo < h[i][j].leie) {
                        manedsfortjeneste += h[i][j].saldo;
                        h[i][j].saldo -= h[i][j].leie;
                    } else {
                        manedsfortjeneste += h[i][j].leie;
                        h[i][j].saldo -= h[i][j].leie;
                    }
                } //end if
            }
        } //end leiebetaling
        
        //Oppdatering av m�ned og �r
        maned++;
        totaltAntallManeder++;
        if (maned == 13) {
            ar++;
            maned = 1;
        }
        //Utregning av m�nedsutgifter
        int hybelUtgifter = 1200;
        int fellesUtgifter = 1700;
        int sumUtgifter = (hybelUtgifter*3*6+fellesUtgifter*3);
        manedsfortjeneste = manedsfortjeneste - sumUtgifter;
        totalfortjeneste += manedsfortjeneste;
        //Bekreftelse p� utf�rt m�nedskj�ring
        System.out.println("M�nedskj�ring er utf�rt for "+maned+"/"+ar+". Driftstid er "+totaltAntallManeder+" m�neder.");
        System.out.println("Husleiesatsene er for bunnetasjene "+manedsleie+"kr. og for toppetasjen "+manedsleieTopp+"kr.");
        System.out.println("M�nedsinntekter etter faste utgifter er " + manedsfortjeneste + " og total fortjeneste er n� lik "+totalfortjeneste+"kr.");
        System.out.println("Gjennomsnittlig m�nedsfortjeneste er " + totalfortjeneste/totaltAntallManeder+"kr.\n");
    } //end manedskjoring

    //Registrerer utkasting
    void registrerUtkasting() {
        int gebyr = 3000;
        for (int i = 0; i < h.length; i++) {
            for(int j = 0; j < h[i].length; j++) {
                if (h[i][j].saldo < -h[i][j].leie) {
                    //Tilkall H. Hole
                    tilkallHole(h[i][j].etasje, h[i][j].bokstav, h[i][j].saldo);
                    //Bekreftelse p� tilkalling av H. Hole
                    System.out.println("H. Hole er tilkalt og vil kaste ut beboeren fra hybel " + h[i][j].etasje +""+ h[i][j].bokstav+".");
                    System.out.println("Utest�ende krav p� " + (-h[i][j].saldo) + "kr. og halvparten av gebyr p� "+ gebyr + "kr. er innkrevd (ikke sp�r hvordan), og satt inn p� konto.");
                    //Oppdatering av fortjeneste og utkasting av student
                    totalfortjeneste += (-h[i][j].saldo) + (gebyr / 2);
                    h[i][j].student = null;
                    h[i][j].saldo = 0;
                    System.out.println("Totalfortjeneste er n� lik: "+ totalfortjeneste +"kr.\n");
                } //end if
            }
        } //end for
    } //end registrerUtkasting()

    //�ker husleia
    void okHusleie() {
        System.out.println("*** �k husleien ***");
        Scanner input = new Scanner(System.in);
        //Skriv ut gamle husleieprisen
        System.out.println("N�v�rende husleie for f�rste og andre etasje er " + manedsleie + "kr.");
        System.out.println("N�v�rende husleie for toppetasjen er " + manedsleieTopp + "kr.");
        //Angi ny husleie
        System.out.print("Vennligst angi ny husleie for f�rste og andre etasje: ");
        manedsleie = input.nextInt();
        System.out.print("Vennligst angi ny husleie for toppetasjen: ");
        manedsleieTopp = input.nextInt();
        //Skriv ut nye husleiepriser
        System.out.println("Ny husleie for f�rste og andre etasje er " + manedsleie + "kr.");
        System.out.println("Ny husleie for toppetasjen er " + manedsleieTopp + "kr.");
    } //end okHuseleie()

    //Oppretter en "tom" hybeldata.txt dersom denne ikke eksisterer fra f�r
    void opprettHybeldata() {
        Scanner input = new Scanner(System.in);
        try {
            System.out.print("Vennligst oppgi �r for start av systemet (����): ");
            int ar = input.nextInt();
            System.out.print("Vennligst oppgi m�ned for start av systemet (MM): ");
            int maned = input.nextInt();
            FileWriter fw = new FileWriter("hybeldata.txt");
            fw.write(maned+"; "+ar+"; 0; 0; 6000; 7000\n");
            fw.write("1; A; 0; TOM HYBEL\n");
            fw.write("1; B; 0; TOM HYBEL\n");
            fw.write("1; C; 0; TOM HYBEL\n");
            fw.write("1; D; 0; TOM HYBEL\n");
            fw.write("1; E; 0; TOM HYBEL\n");
            fw.write("1; F; 0; TOM HYBEL\n");
            fw.write("2; A; 0; TOM HYBEL\n");
            fw.write("2; B; 0; TOM HYBEL\n");
            fw.write("2; C; 0; TOM HYBEL\n");
            fw.write("2; D; 0; TOM HYBEL\n");
            fw.write("2; E; 0; TOM HYBEL\n");
            fw.write("2; F; 0; TOM HYBEL\n");
            fw.write("3; A; 0; TOM HYBEL\n");
            fw.write("3; B; 0; TOM HYBEL\n");
            fw.write("3; C; 0; TOM HYBEL\n");
            fw.write("3; D; 0; TOM HYBEL\n");
            fw.write("3; E; 0; TOM HYBEL\n");
            fw.write("3; F; 0; TOM HYBEL\n");
            fw.close();
        } catch (Exception e) {
            System.out.println("Kunne ikke opprette hybeldata.txt");
        } //end try catch
        innlesning();
    } //end opprettHybeldata()

    //Tar i mot en hybeladresse (f.eks. 1A) og konverterer den til adressen
    //i arrayen som programmet forst�r. Returnerer som en int-array
    int[] finnHybel(String temp) {
        int[] retur = {-1, -1};
        int e = Character.getNumericValue(temp.charAt(0)-1);
        int b = (int) (temp.charAt(1)-'A');
        if (e>2 || b>5) {
            System.out.println("Etasjen eller hybelen eksisterer ikke");
        } else {
            retur[0] = e;
            retur[1] = b;
        }
        return retur;
    } //end finnHybel()

    //Starter tilkalling av H. Hole
    void tilkallHole(int etasje, int rom, int krav) {
        try {
            FileWriter fw = new FileWriter ("torpedo.txt", true);
            char bokstav = (char) rom;
            //Skriver til terminal
            System.out.println((-krav)+ "kr. i leie er utest�ende for hybel "+etasje+""+bokstav + ", H. Hole tilkalles...");
            //Skriver til fil
            fw.write("Hybel: " + etasje + "" + bokstav + "; Krav: " + (-krav)+"\n");
            fw.close();
        } catch (Exception e) {
            System.out.println("Det har oppst�tt en feil");
        } //end try catch
    } //end tilkallHole

    //Avslutningsmetoden
    void avslutt() {
        System.out.println("Avslutter...");
        //Oppdaterer hybeldata.txt med data fra programmet
        try{
            FileWriter fw = new FileWriter ("hybeldata.txt");
            fw.write(maned+"; "+ar+"; "+totalfortjeneste+"; "+totaltAntallManeder+"; "+manedsleie+"; "+manedsleieTopp+"\n");
            for(int i = 0; i < h.length; i++) {
                for(int j = 0; j < h[i].length; j++) {
                    //Dersom en hybels student st�r oppf�rt som en nullpeker
                    //skrives dette som "TOM HYBEL" i txt-filen.
                    if(h[i][j].student == null) {
                        fw.write(h[i][j].etasje+"; "+h[i][j].bokstav+"; "+h[i][j].saldo+"; "+ "TOM HYBEL" +"\n");
                    } else{
                        fw.write(h[i][j].etasje+"; "+h[i][j].bokstav+"; "+h[i][j].saldo+"; "+h[i][j].student+"\n");
                    } //end if else
                }
            } //end for
            fw.close();
            //Dersom det skulle oppst� en feil blir prosessen avbrutt og
            //bruker sendes tilbake til menyen, s� man kan pr�ve p� nytt.
        } catch (Exception e){
            System.out.println("Lagring feilet, pr�v p� nytt");
            meny();
        } //end try catch
        
    } //end avslutt
    
} //end Utsyn

class Hybel {
    int etasje;
    char bokstav;
    int saldo;
    String student;
    int leie;
    //Registrerer hyblene med data hentet fra hybeldata.txt
    Hybel(int etasje, char bokstav, int saldo, String student, int leie) {
        this.etasje=etasje;
        this.bokstav=bokstav;
        this.saldo=saldo;
        this.student=student;
        this.leie=leie;
    }
} //End Hybel