import java.util.*;

class Oblig1B {
    public static void main (String Args[]) {
        
        Scanner input = new Scanner(System.in);
        
        String Navn;
        int Timelonn;
        double[] TimerJobbet = new double[4];
        double TimerOvertid;
        int uke;
        double[] Ukelonn = new double[4];
        double[] Totallonn = new double[4];
        
        System.out.print ("Vennligst legg inn arbeidstagers navn: ");
        Navn = input.nextLine();
        System.out.print ("Vennligst legg inn arbeidstagers timelonn: ");
        Timelonn = input.nextInt();
        for (uke=0; uke<4; uke++) {
            int ukenr = uke+1;
            System.out.print ("Vennligst legg inn arbeidstagers timeantall for uke " + ukenr +": ");
            TimerJobbet[uke] = input.nextDouble();
            Ukelonn[uke] = Timelonn * TimerJobbet[uke];
        }
        System.out.println ("");
        System.out.println ("L�nnsslipp for arbeidstager " + Navn);
        System.out.println ("Timel�nn:                     " + Timelonn + " kr. i timen");
        for (uke=0; uke<4; uke++) {
            int ukenr = uke+1;
            System.out.println ("\nTimer jobbet i uke " + ukenr + " er:      " + TimerJobbet[uke] + " timer");    
            if (TimerJobbet[uke] <= 40) {
                System.out.println ("L�nn for uken er              " + Ukelonn[uke] + " kr.");
                double UtbetaltOvertid = 0;
                System.out.println ("Overtidsl�nn for uken er      " + UtbetaltOvertid + " kr.");
                Totallonn[uke] = Ukelonn[uke] + UtbetaltOvertid;
                System.out.println ("Total l�nn for uken er        " + Totallonn[uke] + " kr.");
            }
            else {
                TimerOvertid = TimerJobbet[uke] - 40;
                double LonnOvertid = Timelonn * 1.5;
                double UtbetaltOvertid = TimerOvertid * LonnOvertid;
                Ukelonn[uke] = 40 * Timelonn;
                System.out.println ("L�nn for uken er              " + Ukelonn[uke] + " kr.");
                System.out.println ("Overtidsl�nn for uken er      " + UtbetaltOvertid + " kr.");
                Totallonn[uke] = Ukelonn[uke] + UtbetaltOvertid;
                System.out.println ("Total l�nn for uken er        " + Totallonn[uke] + " kr.");
            }
        }
        double Manedslonn = 0;
        for (uke = 0; uke < Totallonn.length; uke++) {
            Manedslonn += Totallonn[uke];
        }
        System.out.println("\nTotal l�nn for hele perioden er " + Manedslonn + " kr.");
    }
}