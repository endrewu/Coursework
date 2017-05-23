import java.util.*;
import java.util.concurrent.*;

public class Oblig2 {

    public static void main(String[] args) {
        System.out.print("Starting...    ");
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("using " +cores+ " cores...");

        for (int i = 2000000000; i >= 20000000; i /= 10) {
            new Sequential(i);
            System.out.println("");
            new Parallell(i, cores);
            System.out.println("");
        }
    }
}

class Sequential {
    static double[] time = new double[100];
    static int run = 0;

    int size;

    Sequential(int size) {
        new EratosthenesSieve(size);
    }
}

class Parallell {
    Parallell(int size, int cores) {
        new EratosthenesParallellSieve(size, cores);
    }
}