/*//////////////////

Jeg registrerer at min l�sning for den valgfrie oppgaven er et enormt felt av mange if og else if tester. Til tross for at det ikke var noe krav om kommentering i denne oppgaven har jeg valgt � kommentere disse feltene litt for � forh�pentligvis bedre lesbarheten.
Det er stort sett veldig gjentagende operasjoner som manuelt sjekker hvert eneste nabofelt, med tester for � unng� array out of bounds der feltet ligger langs f�rste eller siste rad/kolonne.

 *//////////////////

import java.util.Scanner;

class Oblig2 {
    public static void main (String[] args){
        System.out.println("*** VELKOMMEN TIL RURITANIAS OLJEFELTOVERSIKT ***\n");
        Instruksjoner i = new Instruksjoner();
        i.meny();
    }
}


class Instruksjoner {
    String[][] felt = new String[10][20];
    int[][] fat = new int[10][20];
    Scanner in = new Scanner(System.in);
    
    
    void meny(){
        String valg = "0";
        while (! (valg.equals("9"))) {
            System.out.println("Du har f�lgende valgmuligheter:");
            System.out.println("1. Kj�p et felt");
            System.out.println("2. Annuler kj�p av et felt");            
            System.out.println("3. Lag oversiktskart");
            System.out.println("4. Oppdater oljeutvinning");
            System.out.println("5. Vis et oljeselskap");
            System.out.println("6. Sjekk et felts nabofelt");
            System.out.println("9. Avslutt");
            System.out.println();            
            valg = in.nextLine();            
            if (valg.equals("1")) {
                kjopFelt();
            } else if (valg.equals("2")) {
                annulerSalg();
            } else if (valg.equals("3")) {                
                skrivKart();
            } else if (valg.equals("4")) {               
                registrerFat();
            } else if (valg.equals("5")) {
                visSelskap();
            } else if (valg.equals("6")) {
                sjekkFelt();
            } else if (valg.equals("9")) {
                System.out.println("Avslutter ...");
            } else {
                System.out.println("Ugyldig valg, vennligst pr�v igjen\n");
            }
        }
    }


    void kjopFelt(){
        int i = 0;
        int j = 0;
        System.out.println("** Kj�p et felt **");
        try {
            System.out.println("Oppgi navn p� feltet som �nskes kj�pt (for eksempel 1-2),");
            System.out.print("eller tast inn avbryt for � avbryte operasjonen: ");
            String temp = in.nextLine();
            if (temp.equals("avbryt")) {
                System.out.println("Avbryter...");
                System.out.println();
            } else {
                String[] temp2 = temp.split("-");
                i = Integer.parseInt(temp2[0]);
                j = Integer.parseInt(temp2[1]);
                if (felt[i][j] != null){
                    System.out.println("Oljefeltet er ikke til lenger salgs");
                    System.out.println();
                } else {
                    System.out.print("Oppgi oljeselskapets navn: ");
                    felt[i][j] = in.nextLine();
                    System.out.println("\nOljefeltet " + i + "-" + j +" selges til " + felt[i][j]);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Det har oppst�tt en feil, vennligst pr�v igjen\n");
            kjopFelt();
        }
    }

    
    void annulerSalg() {
        int i = 0;
        int j = 0;
        System.out.println("*** Annuler salg ***");
        try{
            System.out.println("Vennligst gi navnet p� feltet som �nskes solgt, eller");
            System.out.print("tast inn avbryt for � avbryte operasjonen: ");
            String temp = in.nextLine();
            if (temp.equals("avbryt")) {
                System.out.println("Avbryter...");
                System.out.println();
            } else {
                String[] temp2 = temp.split("-");
                i = Integer.parseInt(temp2[0]);
                j = Integer.parseInt(temp2[1]);
                
                System.out.print("Vennligst gi navnet p� oljeselskapet som �nsker � annulere salget: ");
                String selskap = in.nextLine();
                
                if (selskap.equals(felt[i][j])) {
                    felt[i][j] = null;
                    System.out.println("\nSalg av felt "+i+"-"+j+" til "+selskap+" er annulert\n");
                } else {
                    System.out.println("\nFeltet er ikke eid av "+selskap+", operasjonen avbrytes\n");
                }
            }
        } catch (Exception e) {
            System.out.println("\nDet har oppst�tt en feil, vennligst pr�v igjen\n");
            annulerSalg();
        }
    }
    

    void skrivKart(){
        int i = 0;
        int j = 0;
        System.out.println("*** Kart over oljefeltene ***");
        System.out.print("   0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19\n");
        for (i = 0; i<felt.length; i++){
            System.out.print(i+"  ");
            for (j = 0; j<felt[i].length; j++ ){
                if (felt[i][j] != null) {
                    System.out.print("X  ");
                } else {
                    System.out.print(".  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    void registrerFat(){
        int i = 0;
        int j = 0;
        System.out.println("*** Registrer fat for et felt ***");
        try {
            System.out.println("\nVennligst gi navnet p� feltet som skal registreres,");
            System.out.print("eller tast avbryt for � avbryte operasjonen: ");
            String temp = in.nextLine();
            if (temp.equals("avbryt")) {
                System.out.println("Avbryter...");
                System.out.println();
            } else {
                String[] temp2 = temp.split("-");
                i = Integer.parseInt(temp2[0]);
                j = Integer.parseInt(temp2[1]);
                if(felt[i][j] != null){
                    System.out.println("Vennligst oppgi hvor mange fat som er utvunnet i felt "+i+"-"+j+" i");
                    System.out.print("l�pet av siste �r: ");
                    int nyefat = in.nextInt();
                    fat[i][j] += nyefat;
                    System.out.println("Felt "+i+"-"+j+" er registrert med "+nyefat+" nye fat, og en total p� "+fat[i][j]+" antall fat\n");
                    in.nextLine();
                } else {
                    System.out.println("Feltet er ikke i drift, vennligst pr�v igjen");
                }
            }
        } catch (Exception e) {
            System.out.println("Det har oppst�tt en feil, vennligst pr�v igjen");
            registrerFat();
        }
    }


    void visSelskap() {
        int i = 0;
        int j = 0;
        System.out.println("*** Vis oversikt over selskap ***");
        int antallfat = 0;
        System.out.println("Gi navnet p� oljeselskapet du �nsker oversikt over,");
        System.out.print("eller tast inn avbryt for � avbryte operasjonen: ");
        String selskap = in.nextLine();
        if(selskap.equals("avbryt")) {
            System.out.println("Avbryter...");
            System.out.println();
        } else {
            System.out.println("Selskapet "+selskap+" eier f�lgende felt: ");
            for (i=0; i<felt.length; i++) {
                for(j=0; j<felt[i].length; j++) {
                    if (selskap.equals(felt[i][j])) {
                        antallfat += fat[i][j]; 
                        System.out.print(i+"-"+j+" ");
                    }
                }
            }
            System.out.println();
            System.out.println("Og har s� langt utvunnet "+antallfat+" fat med olje\n");
        }
    }

    void sjekkFelt(){
        int i = 0;
        int j = 0;
        System.out.println("** Sjekk et felt **");
        try {
            System.out.println("Oppgi navn p� feltet som �nskes sjekket, eller tast");
            System.out.print("inn avbryt for � avbryte operasjonen: ");
            String temp = in.nextLine();
            if (temp.equals("avbryt")) {
                System.out.println("Avbryter...");
                System.out.println();
            } else {
                String[] temp2 = temp.split("-");
                i = Integer.parseInt(temp2[0]);
                j = Integer.parseInt(temp2[1]);

                System.out.println("Nabofeltene for felt "+i+"-"+j+" er: ");
                //Sjekker for naborader
                if (i != 0 && i != 9) { //sjekker naboraden for alle felt som ikke ligger i f�rste eller siste rad
                    if (felt[i-1][j]==null) { //sjekker om det gitte nabofeltet er ledig
                        System.out.println((i-1)+"-"+j+" er ledig registert med "+fat[i-1][j]+" utvunnede fat.");
                    } else {
                        System.out.println((i-1)+"-"+j+" eid av "+felt[i-1][j]+" registrert med "+fat[i-1][j]+" utvunnede fat.");
                    }
                    if (felt[i+1][j]==null) {
                        System.out.println((i+1)+"-"+j+" er ledig registert med "+fat[i+1][j]+" utvunnede fat.");
                    } else {
                        System.out.println((i+1)+"-"+j+" eid av "+felt[i+1][j]+" registrert med "+fat[i+1][j]+" utvunnede fat.");
                    }
                } else if (i == 0) { //sjekker naboraden for alle felt som ligger langs f�rste rad
                    if (felt[i+1][j]==null) {
                        System.out.println((i+1)+"-"+j+" er ledig registert med "+fat[i+1][j]+" utvunnede fat.");
                    } else {
                        System.out.println((i+1)+"-"+j+" eid av "+felt[i+1][j]+" registrert med "+fat[i+1][j]+" utvunnede fat.");
                    }
                } else if (i == 9) { //sjekker naboraden for alle felt som ligger langs siste rad
                    if (felt[i-1][j]==null) {
                        System.out.println((i-1)+"-"+j+" er ledig registert med "+fat[i-1][j]+" utvunnede fat.");
                    } else {
                        System.out.println((i-1)+"-"+j+" eid av "+felt[i-1][j]+" registrert med "+fat[i-1][j]+" utvunnede fat.");
                    }
                }
                //Sjekker for nabokolonner
                if (j != 0 && j != 19) { //sjekker nabokolonnen for alle felt som ikke ligger langs f�rste eller siste kolonne
                    if (felt[i][j-1] == null) {
                        System.out.println(i+"-"+(j-1)+" er ledig registert med "+fat[i][j-1]+" utvunnede fat.");
                    } else {
                        System.out.println(i+"-"+(j-1)+" eid av "+felt[i][j-1]+" registrert med "+fat[i][j-1]+" utvunnede fat.");
                    }
                    if (felt[i][j+1] == null) {
                        System.out.println(i+"-"+(j+1)+" er ledig registert med "+fat[i][j+1]+" utvunnede fat.");
                    } else {
                        System.out.println(i+"-"+(j+1)+" eid av "+felt[i][j+1]+" registrert med "+fat[i][j+1]+" utvunnede fat.");
                    }
                } else if (j == 0) { //sjekker nabokolonnen for alle felt som ligger langs f�rste kolonne
                    if (felt[i][j+1] == null) {
                        System.out.println(i+"-"+(j+1)+" er ledig registert med "+fat[i][j+1]+" utvunnede fat.");
                    } else {
                        System.out.println(i+"-"+(j+1)+" eid av "+felt[i][j+1]+" registrert med "+fat[i][j+1]+" utvunnede fat.");
                    }
                } else if (j == 19) { //sjekker nabokolonnen for alle felt som ligger langs siste kolonne
                    if (felt[i][j-1] == null) {
                        System.out.println(i+"-"+(j-1)+" er ledig registert med "+fat[i][j-1]+" utvunnede fat.");
                    } else {
                        System.out.println(i+"-"+(j-1)+" eid av "+felt[i][j-1]+" registrert med "+fat[i][j-1]+" utvunnede fat.");
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Det har oppst�tt en feil, vennligst pr�v igjen\n");
            sjekkFelt();
        }
    }
}