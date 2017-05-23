// Et par ting ønsker jeg å påpeke for den som skal rette denne obligen:
// i det store og hele synes jeg den løser oppgaven godt, men det er et
// par svakheter jeg ikke har fått tatt skikkelig fatt i. Den viktigste
// er kravet om at klassene Boks, Kolonne og Rad skal gjenbruke mest
// mulig av koden fra superklassen. Dette har jeg ikke gjort da jeg skrev
// de delene fordi jeg hadde en del problemer og tenkte at fungerende 
// kode er bedre enn effektiv kode, i alle fall der og da.
//
// Utenom dette er det hovedsakelig kosmetiske ting jeg ikke har fått
// gjort, mest nevneverdig er kanskje presentasjonen av løsningene i vindu
// som ville blitt mer lesevennlig med markerte bokser. En siste ting jeg
// også vil påpeke er at en del av koden ligger åpent i hele programmet.
// Hadde jeg hatt bedre tid ville jeg på sikt satset på å få alt dette
// private eller protected, men har jobbet mer med å få til en løsning som
// gjør jobben.

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 
class Innleser {
    Brett brett;
    int ramme;
    Innleser(File fil) {
        try {
            //Oppretter scanner, leser ut antall rader og kolonner i hver boks
            //ganger disse sammen for å få lengden på rammen
            Scanner sc = new Scanner(fil);
            int boksRader = Integer.parseInt(sc.nextLine());
            int boksKolonner = Integer.parseInt(sc.nextLine());
            ramme = boksRader*boksKolonner;
            
            //Oppretter brettet, og arrayer for kolonner og rader
            brett = new Brett(ramme);
            Kolonne[] kolonne = new Kolonne[ramme];
            Rad[] rad = new Rad[ramme];
            
            //Oppretter 2d-array for bokser og deretter alle bokser
            Boks[][] boks = new Boks[boksKolonner][boksRader];
            for (int i = 0; i < boksKolonner; i++) {
                for (int j = 0; j < boksRader; j++) {
                    boks[i][j] = new Boks(boksRader, boksKolonner);
                }
            }

            //Oppretter alle enkeltkolonner og -rader
            for (int i = 0; i < ramme; i++) {
                rad[i] = new Rad(ramme);
                kolonne[i] = new Kolonne(ramme);
            }
            
            //int j peker til raden i sudokubrettet
            int j = 0;
            //temp tar vare på forrige rute i brettet for en simpel lenkeliste
            Rute temp = null;
            //Starter innlesing av sudokubrettet
            while (sc.hasNext()) {
                //Rute temp;
                String linje = sc.nextLine();
                //int i peker til kolonnen i sudokubrettet
                for (int i = 0; i < ramme; i++) {
                    //Regner ut hvilken boks ruten skal plasseres i
                    double boksY = (double)Math.ceil(j/boksRader);
                    double boksX = (double)Math.ceil(i/boksKolonner);
                    
                    //Om karakteren er et punktum opprettes tom rute
                    //Ellers opprettes en rute med verdien fra innlesing
                    //Alle ruter som opprettes inneholder en peker til sin
                    //boks, kolonne og rute
                    if (linje.charAt(i) == '.') {
                        brett.brett[j][i] = new TomRute(brett, ramme, boks[(int)boksY][(int)boksX], kolonne[i], rad[j]);
                    } else {
                        brett.brett[j][i] = new FullRute(brett, ramme, boks[(int)boksY][(int)boksX], kolonne[j], rad[i], Character.getNumericValue(linje.charAt(i)));
                    }
                    
                    //Setter ruten inn i sin respektive boks, kolonne og rad
                    boks[(int)boksY][(int)boksX].settInn(boksRader, boksKolonner, brett.brett[j][i]);
                    rad[j].beholder[i] = brett.brett[j][i];
                    kolonne[i].beholder[j] = brett.brett[j][i];
                    
                    //Så lenge løkken allere har kjørt en gang settes forrige
                    //rutes neste-peker til denne ruten 
                    if (temp != null)
                        temp.neste = brett.brett[j][i];
                    //Temp blir satt til å peke på denne ruten
                    temp = brett.brett[j][i];
                }
                j++;
            }
            //Dersom det skjer en feil
        } catch (FileNotFoundException e) {
            System.out.println("Fil ikke funnet");
        }
    }

    //Returnerer brettet som er innlest
    public Brett hentBrett() {
        return brett;
    }
}

//Superclassen Rute.
abstract class Rute {
    //fyllUtRestenAvBrettet() finner løsningene av brettet
    public void fyllUtRestenAvBrettet(SudokuBeholder lager) {
        //Spesialtilfelle. If-testen er ansvarlig for å lagre en ferdig løsning.
        //Slår ut når neste == null, altså man har kommet til slutten av brettet
        if (neste == null) {
            //Om ruten ikke har et fast tall enda må dette finnes
            if (this instanceof TomRute) {
                for (int i = 1; i <= ramme; i++) {
                    //Finner en gyldig verdi, setter den inn og lagrer brettet
                    if (boks.gyldigVerdi(i) && rad.gyldigVerdi(i) && kolonne.gyldigVerdi(i)) {
                        verdi = i;
                        lager.settInn(brett);
                    }
                }
                //Nullstiller verdien på vei bakover i brettet
                //Sørger for at ruter som er feil fyllt ikke hindrer programmet
                verdi = 0;
            //Om ruten har en satt verdi lagres bare løsningen
            } else {
                lager.settInn(brett);
            }
        //Hovedregel. 
        } else {
            //Sjekker om ruten skal finne en verdi
            if (this instanceof TomRute) { //Sjekker om ruten skal finne verdi
                for (int i = 1; i <= ramme; i++) {
                    //Finner en gyldig verdi, setter den inn og kaller neste rute
                    if (boks.gyldigVerdi(i) && rad.gyldigVerdi(i) && kolonne.gyldigVerdi(i)) {
                        verdi = i;
                        neste.fyllUtRestenAvBrettet(lager);
                    }
                }
                //Nullstiller verdien på vei bakover i brettet
                //Sørger for at ruter som er feil fyllt ikke hindrer programmet
                verdi = 0;
            } else { //Dersom ruten allerede har en verdi kalles bare neste
                neste.fyllUtRestenAvBrettet(lager);
            }
        }
    }

    //Pekerne som er i bruk i en rute
    protected Brett brett;
    protected int ramme;
    protected int verdi;
    protected Rute neste;
    protected Kolonne kolonne;
    protected Rad rad;
    protected Boks boks;
}

//Sub-klassen for tomme ruter, altså de som er uvisse
class TomRute extends Rute {
    TomRute(Brett brett, int ramme, Boks boks, Kolonne kolonne, Rad rad) {
        this.brett = brett;
        this.ramme = ramme;
        this.boks = boks;
        this.kolonne = kolonne;
        this.rad = rad;
    }
}

//Sub-klassen for fulle ruter, altså de hvor verdien allerede er kjent
class FullRute extends Rute {
    //Generell konstruktør
    FullRute(Brett brett, int ramme, Boks boks, Kolonne kolonne, Rad rad, int verdi) {
        this.brett = brett;
        this.ramme = ramme;
        this.verdi = verdi;
        this.boks = boks;
        this.kolonne = kolonne;
        this.rad = rad;
    }

    //Forenklet konstruktør som blir bruk når en løsning kopieres over til sudokubeholderen
    FullRute(int verdi) {
        this.verdi = verdi;
    }
}

//Super-klassen for kolonner, rader, bokser.
//Sub-klassene bruker ikke så mye kode fra super-klassen som oppgaven krever
//Dette er fordi jeg hadde store vanskeligheter med lagringen av data tidlig
//i arbeidet og prioriterte å få til en kode som funket fremfor en som var mest
//mulig effektiv
class Beholder {
    protected int ramme;
    protected int boksRader;
    protected int boksKolonner;
}

//Sub-klasse for kolonner
class Kolonne extends Beholder {
    Rute[] beholder;
    Kolonne(int i) {
        ramme = i;
        beholder = new Rute[i];
    }

    //Sjekker om en gitt verdi er gyldig
    public boolean gyldigVerdi(int verdi) {
        boolean retur = true;
        for (int i = 0; i < ramme; i++) {
            if (beholder[i] != null)
                if (beholder[i].verdi == verdi) {
                    retur = false;
                }
        }
        return retur;
    }
}

//Sub-klasse for rader
class Rad extends Beholder {
    Rute[] beholder;
    Rad(int i) {
        ramme = i;
        beholder = new Rute[i];
    }

    //Sjekker om en gitt verdi er gyldig
    public boolean gyldigVerdi(int verdi) {
        boolean retur = true;
        for (int i = 0; i < ramme; i++) {
            if (beholder[i] != null) 
                if (beholder[i].verdi == verdi) {
                    retur = false;
                }
        }
        return retur;
    }
}

//Sub-klasse for bokser
class Boks extends Beholder {
    Rute[][] beholder;
    Boks(int boksRader, int boksKolonner) {
        this.boksRader = boksRader;
        this.boksKolonner = boksKolonner;
        beholder = new Rute[boksRader][boksKolonner];
    }

    //Setter inn en verdi i boksen
    public void settInn(int boksRader, int boksKolonner, Rute rute) {
        //Strengt tatt så finner denne koden kun første ledige plass i arrayen
        //men ettersom innlesing og lagring foregår horisontalt, rad for rad funker dette
        for (int i = 0; i < boksRader; i++) {
            for (int j = 0; j < boksKolonner; j++) {
                if (beholder[i][j] == null) {
                    beholder[i][j] = rute;
                    return;
                }
            }
        }
    }

    //Sjekker etter gyldig verdi
    public boolean gyldigVerdi(int verdi) {
        boolean retur = true;
        for (int i = 0; i < boksRader; i++) {
            for (int j = 0; j < boksKolonner; j++) {
                if (beholder[i][j] != null)
                    if (beholder[i][j].verdi == verdi) {
                        retur = false;
                    }
            }
        }
        return retur;
    }
}

//Klassen brett
class Brett {
    Rute[][] brett;
    int ramme;

    Brett (int ramme) {
        brett = new Rute[ramme][ramme];
        this.ramme = ramme;
    }

    //Skriver ut et brett. Er typisk brukt for å skrive ut et brett lagret i sudokubeholderen
    public void skrivUt() {
        for (int i = 0; i < ramme; i++) {
            for (int j = 0; j < ramme; j++) {
                //Dersom oppgaven er mindre enn 10*10 dreier det seg kun om tall
                if (brett[i][j].verdi < 10) {
                    System.out.print(brett[i][j].verdi);
                    //Dersom oppgaven er større konverteres tall til bokstav (litt hacky) og lagres som bokstav
                } else {
                    int a = brett[i][j].verdi+55;
                    char b = (char)a;
                    System.out.print(b);
                }
            }
            System.out.print("//");
        }
        System.out.println("");
    }

    //Returnerer en enkel tekststreng som inneholder den fullstendige løsningen på oppgaven
    //Brukes til å mate GUIet med løsningene
    public String hentVerdier() {
        String losning = "";
        for (int i = 0; i < ramme; i++) {
            for (int j = 0; j < ramme; j++) {
                if (brett[i][j].verdi < 10) {
                    losning = losning+Integer.toString(brett[i][j].verdi);
                } else {
                    int a = brett[i][j].verdi+55;
                    char b = (char)a;
                    losning = losning+b;
                }
            }
        }
        return losning;
    }
}

//Klassen som tar vare på inntil 750 løsninger av et gitt brett, enkel FIFO lenkeliste
class SudokuBeholder {
    private int ramme;
    private int antLosninger = 0;
    protected Node forste, siste;
    Node naverende;

    //Noder for å lenke objektene sammen
    class Node {
        Node (Brett brett) {
            objekt = brett;
        }
        Node neste;
        Brett objekt;
    }
    
    //Lager en kopi av det ferdigløste brettet og setter det inn sist i listen
    public void settInn(Brett temp) {
        antLosninger++;
        if (antLosninger < 751) {
            ramme = temp.ramme;
            Brett brett = new Brett(ramme);
            for (int i = 0; i < ramme; i++) {
                for (int j = 0; j < ramme; j++) {
                    //Kopierer det løste brettet, rute for rute
                    brett.brett[i][j] = new FullRute(temp.brett[i][j].verdi);
                }
            }
            Node n = new Node(brett);
            
            //Unntaksmetode for første brett
            if (forste == null) {
                forste = n;
                siste = n;
                n.neste = null;
            } else { //Generell metode for å sette inn brett
                siste.neste = n;
                siste = n;
            }
        }
    }

    //Tar ut et brett fra lenkelisten og returnerer det. 
    public Brett taUt() {
        Node retur;
        retur = forste;
        forste = forste.neste;
        retur.neste = null;
        return retur.objekt;
    }
    
    public int hentAntallLosninger(){
        return antLosninger;
    }

    //Sjekker om det er flere løsninger lagret i listen. Returnerer boolean
    public boolean flereLosninger() {
        if (forste != null)
            return true;
        return false;
    }

    //Rekursiv utskrift av alle løsninger. Nummerert til terminal
    public void skrivUtAlle() {
        int losning = 1;
        Node temp = forste;
        while (forste != null) {
            System.out.print(losning + ": ");
            Brett brett = taUt();
            brett.skrivUt();
            losning++;
        }

        //Skriver ut totalt antall løsninger, selv om disse overstiger de 750 som er lagret
        System.out.println("");
        System.out.println("Totalt ble det funnet " + antLosninger + " løsninger på sudokubrettet.");
    }

    //Skriver resultatet til fil dersom det er angitt to filer som startparametre
    public void skrivTilFil(String fil) {
        int losning = 1;
        try {
            FileWriter fw = new FileWriter(fil);
            while (forste != null) {
                String linje;
                Brett brett = taUt();
                fw.write(losning +": ");
                for (int i = 0; i < ramme; i++) {
                    for (int j = 0; j < ramme; j++) {
                        int t = brett.brett[i][j].verdi;
                        if (t < 10) {
                            linje = Integer.toString(t);
                            fw.write(linje);
                        } else {
                            int a = brett.brett[i][j].verdi+55;
                            char b = (char)a;
                            fw.write(b);
                        }
                    }
                    fw.write("//");
                }
                losning++;
                fw.write("\n");
            }
            fw.write("\n");
            
            fw.write("Totalt ble det funnet " + antLosninger + " løsninger på sudokubrettet.");
            fw.close();
        } catch (IOException e) {
        
        }
    }
}

//GUI
class Gui extends JFrame {
    private JPanel meny;
    private JPanel panel;
    private JPanel alleKort;
    private JButton neste;
    private JButton forrige;
    private JLabel antL;
    private int losninger;
    private Brett brett;
    private int ramme;
    private String losning = null;
    
    Gui() {
        super("Løsninger av Sudoku");
        setSize(500, 500);

        //Henter oppgavefilen fra JFileChooser og sender den til løsning
        //Ferdige løsninger lagres
        File fil = velgOppgave();
        Innleser i = new Innleser(fil);
        brett = i.hentBrett();
        ramme = brett.ramme;
        SudokuBeholder sb = new SudokuBeholder();
        brett.brett[0][0].fyllUtRestenAvBrettet(sb);

        //Oppretter en CardLayout som tar vare på alle løsninger
        //Tillater brukeren å bla gjennom alle løsningene (maks 750)
        alleKort = new JPanel(new CardLayout());
        
        //Så lenge det finnes løsninger i beholderen blir disse hentet ut
        //og lagret som kort som legges inn i cardlayout
        while (sb.flereLosninger()) {
            JPanel kort = new JPanel();
            kort.setLayout(new GridLayout(0,ramme));
            losning = sb.taUt().hentVerdier();
            for (int a = 0; a < losning.length(); a++) {
                kort.add(new JButton(Character.toString(losning.charAt(a))));
            }
            alleKort.add(kort);
        }
        
        //Oppretter det sin skal inn under løsningen. Denne gir litt info (kun totalt 
        //antall løsninger) og gir brukeren knapper for å bla i løsningene
        antL = new JLabel("Totalt antall løsninger er "+sb.hentAntallLosninger());
        neste = new JButton(">>");
        forrige = new JButton("<<");
        //Kodene for å bla gjennom løsningene med menyknappene
        neste.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //Bytter til neste kort
                    CardLayout cl = (CardLayout)(alleKort.getLayout());
                    cl.next(alleKort);
                }
            });
        forrige.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //Bytter til forrige kort
                    CardLayout cl = (CardLayout)(alleKort.getLayout());
                    cl.previous(alleKort);
                }
            });
        
        //Oppretter meny-panelet og all dens innmat
        meny = new JPanel();
        meny.setLayout(new GridLayout(0,2));
        meny.add(antL);
        meny.add(new JLabel(""));
        meny.add(forrige);
        meny.add(neste);

        //Setter opp løsningspanelet og menyen i rammen
        add(alleKort, BorderLayout.CENTER);
        add(meny, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //Oppretter JFileChooser for å velge hvilken oppgave som er gitt
    //Denne returneres som en fil
    public File velgOppgave() {
        File fil = null;
        JFileChooser fc = new JFileChooser();
        int returVerdi = fc.showOpenDialog(panel);
        if (returVerdi == JFileChooser.APPROVE_OPTION) {
            fil = fc.getSelectedFile();
        }
        return fil;
    }
}

class Oblig5 {
    public static void main (String[] args) {
        Brett brett = null;
        int ramme = 0;
        if(args.length == 1) {
            File fil = new File(args[0]);
            //Start kjøring av programmet. Løser sudokuoppgaven og printer til terminal
            Innleser i = new Innleser(fil);
            brett = i.hentBrett();
            ramme = brett.ramme;
            SudokuBeholder sb = new SudokuBeholder();
            brett.brett[0][0].fyllUtRestenAvBrettet(sb);
            sb.skrivUtAlle();
            //Start kjøring av programmet. Løser sudokuoppgaven og lagrer til fil
        } else if(args.length == 2) {
            File fil = new File(args[0]);
            Innleser i = new Innleser(fil);
            brett = i.hentBrett();
            SudokuBeholder sb = new SudokuBeholder();
            brett.brett[0][0].fyllUtRestenAvBrettet(sb);
            sb.skrivTilFil(args[1]);
            //Start kjøring av programmet. Lar brukeren velge oppgave fra JFileChooser,
            //løser sudokuen og viser alle løsninger i et nytt vindu
        } else {
            SwingUtilities.invokeLater
                ( new Runnable() {
                        public void run() {
                            Gui g = new Gui();
                        }
                    }
                    );
        }
    }
}

