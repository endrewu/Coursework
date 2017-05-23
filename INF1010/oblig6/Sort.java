import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

class Monitor {
    private ArrayList<String[]> dellosninger = new ArrayList<String[]>();
    private Sort sort;
    private int antOrd;

    Monitor(Sort sort, int antTrader, int antallOrd) {
        this.sort = sort;
        int antFlettinger = antTrader-1;
        this.antOrd = antallOrd;

        //Oppretter den mengden flettere jeg trenger
        for (int i = 0; i < antFlettinger; i++) {
            Fletter fletter = new Fletter(this);
            fletter.start();
        }
    }

    //Sender tilbake en ArrayList med to String[] i seg
    synchronized ArrayList hentOrd() {
        //Dersom det ikke er to arrayer som skal flettes sammen bes fletteren om å vente
        while (dellosninger.size() < 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Avbrutt");
            }
        } //Dersom det er minst to delløsninger i arraylisten
        
        String[] del1;
        String[] del2;
        
        del1 = dellosninger.remove(0);
        del2 = dellosninger.remove(0);
        
        //Oppretter Arraylisten som skal sendes tilbake
        ArrayList<String[]> skalFlettes = new ArrayList<String[]>(2);
        skalFlettes.add(del1);
        skalFlettes.add(del2);
        return skalFlettes;
    }

    //Setter inn sorterte Stringer i arraylisten
    synchronized void settInn(String[] ord) {
        //Setter en String[] inn i samlingen av delløsninger
        dellosninger.add(ord);
        
        //Dersom det kun er en String[] igjen i listen og denne er like lang som alle ordene sendes det til utskrivning
        if (dellosninger.size() == 1 && dellosninger.get(0).length == antOrd) {
            String[] losning = dellosninger.get(0);
            sort.skrivUt(losning);
        }
            
        //Varsler fletteren om at det er kommet flere arrayer som kanskje kan flettes
        notify();
    }
}

class Fletter extends Thread {
    Monitor monitor;

    Fletter(Monitor monitor) {
        this.monitor = monitor;
    }
    
    public void run() {
        ArrayList<String[]> skalFlettes = monitor.hentOrd();
        
        String[] del1 = skalFlettes.get(0);
        String[] del2 = skalFlettes.get(1);
        
        String[] sammensatt = new String[del1.length+del2.length];
        for (int i = 0; i < sammensatt.length; i++) {
            int delMedLavest = -1;
            int indexLavest = -1;
                
            //Finner den laveste verdien i del 1, altså den som står først
            for (int j = 0; j < del1.length; j++) {
                if (del1[j] != null) { //Ruter i del 1 som ikke er tom
                    if (indexLavest == -1) { //Er sann første gangen løkken kjøres
                        indexLavest = j;
                        delMedLavest = 1;
                    }
                }
            }
            //Sjekker om første verdi i del 2 er lavere enn første i del 1
            for (int j = 0; j < del2.length; j++) {
                if (del2[j] != null) { //Ruter i del 2 som ikke er tom
                    if (indexLavest == -1) { //Er sann kun om del 1 er tom
                        indexLavest = j;
                        delMedLavest = 2;
                    }
                    if (delMedLavest == 1) { //Dersom del 1 ikke er tom må del 2 sjekkes opp mot den
                        if (del2[j].compareTo(del1[indexLavest]) < 0) { //Sjekker om en streng fra del 2 er lavere enn den fra del 1
                            indexLavest = j;
                            delMedLavest = 2;
                        }
                    }
                }
            } //Slutt indre for-løkker
            
            //Når den laveste verdien er funnet settes den inn i den flettede arrayen og slettes fra den gamle
            if (delMedLavest == 1) {
                sammensatt[i] = del1[indexLavest];
                del1[indexLavest] = null;
            }
            if (delMedLavest == 2) {
                sammensatt[i] = del2[indexLavest];
                del2[indexLavest] = null;
            }
            indexLavest = -1;
            delMedLavest = -1;
        }
        //Når det hele er flettet sendes den til monitoren
        monitor.settInn(sammensatt);
    }
}

//Klassen Trad sorterer de første arrayene
class Trad extends Thread {
    String[] ord;
    Monitor monitor;

    //Konstruktør, tar i mot en array med ord som skal sorteres og monitoren
    Trad (String[] ord, Monitor monitor) {
        this.ord = ord;
        this.monitor = monitor;
    }

    public void run() {
        if (ord.length < 2) {
            return;
        } else {
            String[] retur = new String[ord.length];
            int laveste;
            String temp;

            for (int i = 0; i < ord.length; i++) {
                laveste = i;
                
                for (int j = 0; j < ord.length; j++) {
                    //Prøver å finne det ordet som skal alfabetiseres først
                    try{
                        if (ord[j].compareTo(ord[laveste]) > 0) {
                            laveste = j;
                        }
                        if (laveste != i) {
                            temp = ord[i];
                            ord[i] = ord[laveste];
                            ord[laveste] = temp;
                        }
                        //Dersom det ikke er lagt inn nok ord til å fylle arrayen kastes en nullpointerexception
                        //og programmet avsluttes
                    } catch (NullPointerException e) {
                        System.out.println("Det er oppgitt for få navn i listen i henhold til hva som står i første linje");
                        System.out.println("Systemet termineres");
                        System.exit(1);
                    }
                }
            }
            //Ferdig sorterte arrayer sendes til monitor
            monitor.settInn(ord);
        }
    }
}

class Sort {
    private int antTrader;
    private String innFil;
    private String utFil;
    private String[] ord;
    private int antOrd;
    private int perTrad;
    private int utestaende = 0;
    long startTid;

    //Starter programmet ved å opprette en ny instans av klassen Sort
    public static void main (String[] para) {
        if (para.length == 3) {
            Sort sort = new Sort(para[0], para[1], para[2]);
        } else {
            System.out.println("Du ma angi tre parametre for a starte programmet.");
            System.out.println("Angi et heltall, en fil for innlesning og en fil for utskrift.");
        }
    }

    //Konstruktør
    Sort (String antTraderS, String innFil, String utFil) {
        startTid = System.currentTimeMillis();

        antTrader = Integer.parseInt(antTraderS);
        this.innFil = innFil;
        this.utFil = utFil;

        //Leser inn kildefilen
        try {
            File fil = new File(innFil);
            Scanner sc = new Scanner(fil);
                
            antOrd = Integer.parseInt(sc.nextLine());
            ord = new String[antOrd];
            perTrad = antOrd / antTrader;
            utestaende = antOrd %antTrader;
            int i = 0;
            
            while (sc.hasNext()) {
                //Dersom det er flere navn i listen enn antatt kastes en feil og programmet avsluttes
                try {
                    ord[i] = sc.nextLine();
                    i++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Det er flere navn i listen enn hva som er oppgitt i første linje");
                    System.out.println("Systemet termineres");
                    System.exit(1);
                }
            }
            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("Feil");
        }
        
        //Oppretter monitoren, fletteren og antTrader antall trader
        int forskyvning = 0;
        Monitor monitor = new Monitor(this, antTrader, antOrd);
        //Fletter fletter = new Fletter(monitor, antTrader);
        //fletter.start();

        for (int i = 0; i < antTrader; i++) {
            Trad trad;
            
            if (i>= (antTrader - utestaende)) {
                String[] temp = Arrays.copyOfRange(ord, i*perTrad+forskyvning, ((i+1)*perTrad)+forskyvning+1);
                forskyvning++;
                trad = new Trad(temp, monitor);
            } else {
                String[] temp = Arrays.copyOfRange(ord, i*perTrad, ((i+1)*perTrad));
                trad = new Trad(temp, monitor);
            }
            trad.start();
        }
    } // Slutt konstruktør

    public void skrivUt(String[] losning) {
        try {
            FileWriter fw = new FileWriter(utFil);
            for (int i = 0; i < losning.length; i++) {
                fw.append(losning[i] + "\n");
            }
            fw.close();
        } catch (IOException e) {}
        long sluttTid = System.currentTimeMillis();
        System.out.println("Totalt brukte programmet " + (sluttTid - startTid) + " millisekunder på å sortere filen " + innFil + " med " + antTrader + " tråder");
    }
}
