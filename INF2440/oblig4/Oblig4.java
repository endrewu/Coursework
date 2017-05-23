import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;

class Oblig4 {
    int intsToSort;
    int randomSeed = 123;

    //global variables
    double[] seqTime;
    double[] parTime;

    int[] a;
    int[] b;
    int max;
    int bit1, bit2;
    int numBit;
    int n;
    int[][] allCount;
    int[] sumCount;
    int cores;
    Random r;
    CyclicBarrier barrier, wait, done;
    ReentrantLock lock;

    volatile boolean stop = false;

    Oblig4() {
        /*
        * INITIALIZATIONS
        */
        cores = Runtime.getRuntime().availableProcessors();
        barrier = new CyclicBarrier(cores);  //used to synchronize threads at runtime
        wait = new CyclicBarrier(cores+1);
        done = new CyclicBarrier(cores+1);   //main waiting on threads to finish
        lock = new ReentrantLock();
        allCount = new int[cores][];

        Thread[] threads = new Thread[cores];
        for (int i = 0; i < cores; i++) {
            (threads[i] = new Thread(new Para(i))).start();
        }

        seqTime = new double[3];
        parTime = new double[3];

        int sorted = -1;

        System.out.println("   ints   |   seqTime   |   parTime   | Speedup");
        System.out.println("------------------------------------------------");

        for (int run = 100000000; run >= 1000; run /= 10) {
            intsToSort = run;
            for (int med = 0; med < 3; med++) {

                /*
                * SEQUENTIAL START
                */

                //initialize a new unsorted array
                a = populate(intsToSort);
                
                long starttime = System.nanoTime();
                
                //sequential method
                radix2(a);

                double timeTaken = (System.nanoTime() - starttime) / 1000000.0;
                seqTime[med] = timeTaken;

                //check that array is sorted correctly, if not prints index of first bad number
                sorted = sortCheck(a);            
                if (sorted != -1)
                    System.out.println("Array not sorted correctly at index "+sorted);

               /*
                * END SEQUENTIAL
                */

               /*
                *PARALLEL START
                */

                //initialize a new unsorted array
                a = populate(intsToSort);

                //set temporary global max-value
                max = a[0];
                
                starttime = System.nanoTime();
                
                //starts threads
                try {
                    wait.await();
                    done.await();
                } catch (Exception e) {
                    System.out.println("trouble in oblig4");
                }

                timeTaken = (System.nanoTime() - starttime) / 1000000.0;
                parTime[med] = timeTaken;

                //check that array is sorted correctly, if not prints index of first bad number
                sortCheck(a);
                sorted = sortCheck(a);    
                if (sorted != -1)
                    System.out.println("Array not sorted correctly in parallel at index "+sorted);

               /*
                * END PARALLEL
                */
           }
           Arrays.sort(seqTime);
           Arrays.sort(parTime);

           System.out.printf("%9d | %8.3f ms | %8.3f ms | %4.2f\n", run, seqTime[2],parTime [2], seqTime[2]/parTime[2]);
        }

        stop = true;

        //restart threads so they can terminate
        try{
            wait.await();
        } catch (Exception e) {
            System.out.println("Error in Oblig4");
        }

        //tries to join threads
        for (int i = 0; i < cores; i++){
            if (threads[i] != null)
                try {
                    threads[i].join();
                } catch (Exception e) {
                    System.out.println("Couldn't join threads");
                }
        }

    }

    public static void main(String[] args) {
        new Oblig4();
    }

    //Recieves an int, creates and returnes an int[] of that length
    int[] populate(int len) {
        r = new Random(randomSeed);
        int[] a = new int[len];
        int t = r.nextInt();

        for (int i = 0; i < a.length; i++) {
            while(t < 0)
                t = r.nextInt();
            a[i] = t;

            t = r.nextInt();
        }
        
        return a;
    }

    //check that the array is sorted correctly
    int sortCheck(int[] a) {
        int t = a[0], neste;
        for (int i = 1; i < a.length; i++) {
            neste = a[i];
            if (t > neste) return i;
            else t = neste;
        }
        return -1;
    }

    /*
     * HERE ARE SEQUENTIALS METHODS
     */

    // ver2. sorterer også tall mellom 2**32 og 2**31
    void radix2(int [] a) {
        // 2 digit radixSort: a[]
        int max = a[0], numBit = 2, n =a.length;
        // a) finn max verdi i a[]
        for (int i = 1 ; i < n ; i++)
            if (a[i] > max) max = a[i];

        while (max >= (1L<<numBit) )numBit++; // antall siffer i max

        // bestem antall bit i siffer1 og siffer2
        int bit1 = numBit/2,
        bit2 = numBit-bit1;
        int[] b = new int [n];
        radixSort( a,b, bit1, 0);    // første siffer fra a[] til b[]
        radixSort( b,a, bit2, bit1);// andre siffer, tilbake fra b[] til a[]
    } // end


    /** Sort a[] on one digit ; number of bits = maskLen, shiftet up ‘shift’ bits */
    void radixSort ( int [] a, int [] b, int maskLen, int shift){
        int  acumVal = 0, j, n = a.length;
        int mask = (1<<maskLen) -1;
        int [] count = new int [mask+1];

        // b) count=the frequency of each radix value in a
        for (int i = 0; i < n; i++) {
            count[(a[i]>> shift) & mask]++;
        }

        //System.out.println("seq count");
        for (int i = 0; i < count.length; i++) {
            //System.out.println(count[i]);
        }

        // c) Add up in 'count' - accumulated values
        for (int i = 0; i <= mask; i++) {
            j = count[i];
            count[i] = acumVal;
            acumVal += j;
        }
        
        // d) move numbers in sorted order a to b
        for (int i = 0; i < n; i++) {
            b[count[(a[i]>>shift) & mask]++] = a[i];
        }
    }// end radixSort


    /*
     * HERE IS PARALLEL CLASS WITH IT'S METHODS
     */
    //updates global max value, protected by a lock
    void setMax(int a) {
        lock.lock();

        if (a > max)
            max = a;

        lock.unlock();
    }

    //simple method to call the a cyclic barrier
    void threadBarrier() {
        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("failure to engage barrier in threadbarrier");
        }
    }

    class Para implements Runnable {
        int index;
        int left;
        int right;
        int mask;

        Para(int index) {
            this.index = index;
        }

        //finds local max, then tries to update global max
        void findMax() {
            int localMax = 0;
            for (int i = left; i <= right; i++) {
                if (a[i] > localMax)
                    localMax = a[i];
            }

            if(localMax > max)
                setMax(localMax); 
        }

        void paraRadix2() {
            if (index == 0)
                numBit = 2; n = a.length;

            //a) finn max verdi i a[] max value
            findMax();

            threadBarrier();

            // bestem antall bit i siffer1 og siffer2
            if(index == 0) {
                while (max >= (1L<<numBit) )numBit++; // digits in max

                bit1 = numBit/2;
                bit2 = numBit-bit1;

                b = new int [n];
            }

            threadBarrier();
            paraRadixSort(a, b, bit1, 0);
            threadBarrier();                 //første siffer fra a[] til b[]
            paraRadixSort(b, a, bit2, bit1); //andre siffer, tilbake fra b[] til a[]
            threadBarrier();
        }

        //sort a[] on one digit ; number of bits = maskLen, shifted up 'shift' bits
        void paraRadixSort(int[] a, int[] b, int maskLen, int shift) {
            int acumVal = 0, j, n = a.length;

            mask = (1<<maskLen) -1;
            if (index == 0)
                sumCount = new int [mask+1];

            int [] count = new int [mask+1];

            // b) count=the frequency of each radix value in local area of a
            for (int i = left; i <= right; i++) {
                count[(a[i]>> shift) & mask]++;
            }

            allCount[index] = count;

            threadBarrier();

            int leftCount = count.length / cores * index;
            int rightCount = (count.length / cores * (index+1)) - 1;

            //sums all local counts to sumCount in parallel
            for (int i = leftCount; i <= rightCount; i++) {
                int s = 0;
                for (int c = 0; c < cores; c++) {
                    s = s + allCount[c][i];
                }

                sumCount[i] = s;
            }

            threadBarrier();

            //new localCount for c)
            int[] localCount = new int[count.length];

            // c) Add up in 'count' - accumulated values
            for (int i = 0; i <= mask; i++) {
                for (int c = 0; c < cores; c++) {
                    j=allCount[c][i];

                    if (c == index) {
                        localCount[i] = acumVal;
                    }

                    acumVal += j;
                }
            }

            threadBarrier();

            // d) move numbers in sorted order a to b
            for (int i = left; i <= right; i++) {
                b[localCount[(a[i]>>shift) & mask]++] = a[i];                
            }

            threadBarrier();
        }

        public void run() {
            while (! stop) {
                try {
                    wait.await();
                } catch (Exception e) {System.out.println("trouble in run");}

                //decide threads local part of a[]
                left = a.length / cores * index;
                right = (a.length / cores * (index+1)) - 1;
                
                if (! stop) {
                    paraRadix2();

                    try {
                        done.await();
                    }catch (Exception e) {System.out.println("trouble in run");}
                }
            }
        }
    }
}