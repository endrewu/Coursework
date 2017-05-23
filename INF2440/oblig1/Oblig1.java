import java.util.*;
import java.util.concurrent.*;

public class Oblig1 {

    public static void main(String[] args) {
        System.out.print("Starting...    ");
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("using " +cores+ " cores...");


        System.out.println("Array size         Sequential      Arrays.sort     Parallell");
        
        //for-loops run the sorting alghorithms 9 times for 100,000,000 numbers, then for 10,000,000 and so on
        for (int sortSize = 100000000; sortSize >= 1000; sortSize /= 10) {
            System.out.printf("%-17d", sortSize);
            for (int i = 0; i < 9; i++) {
                new Sequential(sortSize);
                new Parallell(sortSize, cores);
            }
        }
    }
}

//Abstract class with the methods used to sort arrays and generate new arrays with the same random seed
abstract class Helper {
    //insertSort as given in the assignment
    void insertSort (int[] a, int v, int h) {
        int i , t;
        for (int k = v ; k < h ; k++) {
            // invariant: a[v..k] er nå sortert stigende (største først)
            t = a[k+1];
            i = k;
            while (i >= v && a[i] < t) {
                a[i+1] = a[i];
                i--;
            }
            a[i+1] = t;
        } // end for k
    } // end insertSort

    //simple method to move an int to the right place in top50-sorting
    void moveIn(int[] a, int start) {
        int i = start+49;
        while (i > start && a[i-1] < a[i]) {
            int temp = a[i-1];
            a[i-1] = a[i];
            a[i] = temp;
            i--;
        }
    }

    //method to create pseudo-random arrays of the given size, using the same seed as the sequential sort
    int[] newArray(int sortSize) {
        Random r = new Random(98765);
        int[] a = new int[sortSize];

        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt();
        }

        return a;
    }
}

//class for sequential sorting
class Sequential extends Helper {
    static int run = 0;
    static double[] results = new double[9];
    static double[] arrSortResults = new double[9]; //time to sort using Arrays.sort()

    int sortSize;

    Sequential(int sortSize) {
        this.sortSize = sortSize;

        //Sorting using insertSort
        int[] a = newArray(sortSize);

        long begin = System.nanoTime();

        //sorting
        insertSort(a, 0, 49);
        for (int i = 50; i < a.length; i++) {
            if (a[i] > a[49]) {
                int temp = a[i];
                a[i] = a[49];
                a[49] = temp;
                moveIn(a, 0);
            }
        }

        results[run] = (System.nanoTime()-begin) / 1000000.0;


        //Sorting using Arrays.sort()
        int[] b = newArray(sortSize);

        begin = System.nanoTime();

        Arrays.sort(b);

        arrSortResults[run] = (System.nanoTime()-begin) / 1000000.0;

        compareArrays(a, b); //check that both arrays sorted the first 50 results the same

        //Update run-count, if 9th run sorts results and prints median time.
        if (run++ == 8) {
            Arrays.sort(results);
            Arrays.sort(arrSortResults);
            System.out.printf("%10fms",results[5]);
            System.out.printf("%15fms", arrSortResults[5]);
            run = 0;
        }

    }

    //checks that the two sorted arrays have the same first 50 results or terminates
    void compareArrays(int a[], int b[]) {
        int j = b.length-1;

        for (int i = 0; i < 50; i++) {
            if (a[i] != b[j]){
                System.out.println("ERROR, mismatch between Arrays.sort and insertSort. \nTerminating ...");
                System.exit(1);
            }
            j--;
        }
    }
}


class Parallell extends Helper {
    static double[] results = new double[9];
    static int run;

    CyclicBarrier b;
    CyclicBarrier c;

    int sortSize;
    int cores;

    Thread[] t;

    Parallell(int sortSize, int cores) {
        this.sortSize = sortSize;
        this.cores = cores;

        t = new Thread[cores];

        int[] a = newArray(sortSize);

        b = new CyclicBarrier(cores+1);
        c = new CyclicBarrier(2);

        long begin = System.nanoTime();

        for (int i = 0; i < t.length; i++) {
            int start = a.length/cores*i;     //start and end of the subarrays. Doesn't take account for odd length as it will always be a power of 10
            int end = a.length/cores*(i+1);   

            t[i] = new Thread(new Para(i, a, start, end));
            t[i].start();
        }

        try {
            b.await();
        } catch (Exception e) {}
        try {
            c.await();
        }catch (Exception e) {}

        results[run] = (System.nanoTime()-begin) / 1000000.0;

        //Update run-count, if 9th run sorts results and prints median time.
        if (run++ == 8) {
            Arrays.sort(results);
            System.out.printf("%12fms\n", results[5]);
            run = 0;
        }

    }

    //Inner class for threads
    class Para implements Runnable {
        int index;
        int[] a;
        int start;
        int end;

        Para(int i, int[] a, int start, int end) {
            index = i;
            this.a = a;
            this.start = start;
            this.end = end;
        }

        public void run() {
            //sorting
            insertSort(a, start, start+49);         //InsertSort of every thread's "owned" area
            for (int i = start+50; i < end; i++) {  //Sort the rest of the area similarly to the sequential solution
                if (a[i] > a[start+49]) {
                    int tmp = a[i];
                    a[i] = a[start+49];
                    a[start+49] = tmp;
                    moveIn(a, start);
                }
            }

            try{
                b.await();
            } catch (Exception e) {}

            //Thread 0 handles the clean-up
            if (index == 0) {
                int[] returnArray = new int[50];
                int[] cursor = new int[cores];

                //Similar to the sorting of the tail from the sequential part
                //this checks the top 50 picks from thread 1+ and compares
                //them to the top 50 from thread 0, then picks the top 50
                for (int i = 1; i < cores; i++) {
                    for (int j = 49; j >= 0; j--) {
                        if (a[end*i+j] > a[49]) {
                            int tmp = a[49];
                            a[49] = a[end*i+j];
                            a[end*i+j] = tmp;
                            moveIn(a, 0);
                        }
                    }
                }

                try {
                    c.await();
                } catch (Exception e) {}
            }
        }
    }
}