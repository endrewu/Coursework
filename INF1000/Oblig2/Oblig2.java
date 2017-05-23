/*//////////////////

Jeg registrerer at min løsning for den valgfrie oppgaven er et enormt felt av mange if og else if tester. Til tross for at det ikke var noe krav om kommentering i denne oppgaven har jeg valgt å kommentere disse feltene litt for å forhåpentligvis bedre lesbarheten.
Det er stort sett veldig gjentagende operasjoner som manuelt sjekker hvert eneste nabofelt, med tester for å unngå array out of bounds der feltet ligger langs første eller siste rad/kolonne.

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
            System.out.println("Du har følgende valgmuligheter:");
            System.out.println("1. Kjøp et felt");
            System.out.println("2. Annuler kjøp av et felt");            
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
                System.out.println("Ugyldig valg, vennligst prøv igjen\n");
            }
        }
    }


    void kjopFelt(){
        int i = 0;
        int j = 0;
        System.out.println("** Kjøp et felt **");
        try {
            System.out.println("Oppgi navn på feltet som ønskes kjøpt (for eksempel 1-2),");
            System.out.print("eller tast inn avbryt for å avbryte operasjonen: ");
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
            System.out.println("Det har oppstått en feil, vennligst prøv igjen\n");
            kjopFelt();
        }
    }

    
    void annulerSalg() {
        int i = 0;
        int j = 0;
        System.out.println("*** Annuler salg ***");
        try{
            System.out.println("Vennligst gi navnet på feltet som ønskes solgt, eller");
            System.out.print("tast inn avbryt for å avbryte operasjonen: ");
            String temp = in.nextLine();
            if (temp.equals("avbryt")) {
                System.out.println("Avbryter...");
                System.out.println();
            } else {
                String[] temp2 = temp.split("-");
                i = Integer.parseInt(temp2[0]);
                j = Integer.parseInt(temp2[1]);
                
                System.out.print("Vennligst gi navnet på oljeselskapet som ønsker å annulere salget: ");
                String selskap = in.nextLine();
                
                if (selskap.equals(felt[i][j])) {
                    felt[i][j] = null;
                    System.out.println("\nSalg av felt "+i+"-"+j+" til "+selskap+" er annulert\n");
                } else {
                    System.out.println("\nFeltet er ikke eid av "+selskap+", operasjonen avbrytes\n");
                }
            }
        } catch (Exception e) {
            System.out.println("\nDet har oppstått en feil, vennligst prøv igjen\n");
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
            System.out.println("\nVennligst gi navnet på feltet som skal registreres,");
            System.out.print("eller tast avbryt for å avbryte operasjonen: ");
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
                    System.out.print("løpet av siste år: ");
                    int nyefat = in.nextInt();
                    fat[i][j] += nyefat;
                    System.out.println("Felt "+i+"-"+j+" er registrert med "+nyefat+" nye fat, og en total på "+fat[i][j]+" antall fat\n");
                    in.nextLine();
                } else {
                    System.out.println("Feltet er ikke i drift, vennligst prøv igjen");
                }
            }
        } catch (Exception e) {
            System.out.println("Det har oppstått en feil, vennligst prøv igjen");
            registrerFat();
        }
    }


    void visSelskap() {
        int i = 0;
        int j = 0;
        System.out.println("*** Vis oversikt over selskap ***");
        int antallfat = 0;
        System.out.println("Gi navnet på oljeselskapet du ønsker oversikt over,");
        System.out.print("eller tast inn avbryt for å avbryte operasjonen: ");
        String selskap = in.nextLine();
        if(selskap.equals("avbryt")) {
            System.out.println("Avbryter...");
            System.out.println();
        } else {
            System.out.println("Selskapet "+selskap+" eier følgende felt: ");
            for (i=0; i<felt.length; i++) {
                for(j=0; j<felt[i].length; j++) {
                    if (selskap.equals(felt[i][j])) {
                        antallfat += fat[i][j]; 
                        System.out.print(i+"-"+j+" ");
                    }
                }
            }
            System.out.println();
            System.out.println("Og har så langt utvunnet "+antallfat+" fat med olje\n");
        }
    }

    void sjekkFelt(){
        int i = 0;
        int j = 0;
        System.out.println("** Sjekk et felt **");
        try {
            System.out.println("Oppgi navn på feltet som ønskes sjekket, eller tast");
            System.out.print("inn avbryt for å avbryte operasjonen: ");
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
                if (i != 0 && i != 9) { //sjekker naboraden for alle felt som ikke ligger i første eller siste rad
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
                } else if (i == 0) { //sjekker naboraden for alle felt som ligger langs første rad
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
                if (j != 0 && j != 19) { //sjekker nabokolonnen for alle felt som ikke ligger langs første eller siste kolonne
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
                } else if (j == 0) { //sjekker nabokolonnen for alle felt som ligger langs første kolonne
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
            System.out.println("Det har oppstått en feil, vennligst prøv igjen\n");
            sjekkFelt();
        }
    }
}