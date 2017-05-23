import java.util.*;

class test {
    public static void main (String Args[]) {

        Scanner input = new Scanner(System.in);

        String Navn;
        int Timelonn;
        double[] Arbeidstimer = new double[4];

        for (int uke=0; uke<4; uke++) {
            int ukenr = uke+1;
            System.out.print("Arbeidstimer i uke " + ukenr + ": ");
            Arbeidstimer[uke] = input.nextDouble();
        }

        for (int uke=0; uke<4; uke++) {
            int ukenr = uke+1;
            System.out.println("I uke " + ukenr + " hadde arbeidstaker " + Arbeidstimer[uke] + " Arbeidstimer");
        }


    }
}