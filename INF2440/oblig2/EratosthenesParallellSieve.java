import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
* Implements the bitArray of length 'maxNum' [0..maxNum/16 ]
*   1 - true (is prime number)
*   0 - false
*  can be used up to 2 G Bits (integer range)
*/
public class EratosthenesParallellSieve {
    byte [] bitArr ;           // bitArr[0] represents the 8 integers:  1,3,5,...,15, and so on
    int  maxNum;               // all primes in this bit-array is <= maxNum
    int cores;                 // number of cores on this computer
    int printCntr = 0;

    CyclicBarrier cb, aftermath;
    ReentrantLock lock;

    ArrayList<Long> factors = new ArrayList<Long>();

    final  int [] bitMask = {1,2,4,8,16,32,64,128};  // kanskje trenger du denne
    final  int [] bitMask2 ={255-1,255-2,255-4,255-8,255-16,255-32,255-64, 255-128}; // kanskje trenger du denne


    EratosthenesParallellSieve (int maxNum, int cores) {
        this.maxNum = maxNum;
        this.cores = cores;
        cb = new CyclicBarrier(cores+1);
        aftermath = new CyclicBarrier(2);
        lock = new ReentrantLock();
        
        bitArr = new byte [(maxNum/16)+1];
        setAllPrime();

        long begin = System.nanoTime();

        generatePrimesByEratosthenes();

        double end = (System.nanoTime()-begin) / 1000000.0;
        
        System.out.println("Generated all primes <= " +maxNum+ " in " +end+ "ms");
        System.out.println("  using Eratosthenes' sieve in parallell");

        factorize(100, (long)maxNum*(long)maxNum);
    } // end konstruktor EratosthenesParallellSieve

    void setAllPrime() {
        for (int i = 0; i < bitArr.length; i++) {
            bitArr[i] = (byte)255;
        }
    }

    void crossOut(int i) {
        // set as not prime- cross out (set to 0)  bit represening 'int i'
        if (i % 2 != 0)
            bitArr[i / 16] &= bitMask2[(i % 16)>>1];
    } //
 
    void crossOutMult(int i, int start, int end) {
        int n = i*i;
        while (n <= end) {
            if (n >= start)
                crossOut(n);
            n = n+(2*i);
        }
    }

    boolean isPrime (int i) {
        if (i == 2) return true;
        if (i % 2 == 0) return false;
        if ( (bitArr[i / 16] & bitMask[(i % 16) >>> 1] ) > 0) return true;
        return false;
    }

    void factorize (int mul, long num) {
        int perCore = maxNum/cores;

        Thread[] threads = new Thread[cores];

        long begin = System.nanoTime();

        for (long l = num-mul; l < num; l++) {
            for (int i = 0; i < cores; i++) {

                (threads[i] = new Thread(new ParallellFact(i, l, (perCore*i)+1, perCore*(i+1)))).start();
            }

            try {
                cb.await();
            } catch (Exception e) {}

            for (int t = 0; t < threads.length; t++) {
                try {
                    threads[t].join();
                } catch (Exception e) {}
            }
        }

        double end = (System.nanoTime()-begin) / 1000000.0;
        System.out.println("100 factorizations calculated in " +end+ "ms");
    } // end factorize

    void addFactors(ArrayList<Long> fakt) {
        lock.lock();
        
        factors.addAll(fakt);

        lock.unlock();
    }

    class ParallellFact implements Runnable {
        int index;
        long num, n;
        int start;      //first prime to check for factor
        int end;        //last prime to check for factor

        long i;          //running prime
        ArrayList<Long> fakt;

        ParallellFact(int index, long num, int start, int end) {
            this.index = index;
            this.num = n = num;
            this.start = start;
            this.end = end;

            fakt = new ArrayList<Long>();

            if (isPrime(start)) {
                i = start;
            } else {
                i = nextPrime((int)start);
            }
        }

        public void run() {
            while (i < end && i < n) {
                if (n % i == 0) {
                    fakt.add(i);
                    n/=i;
                } else {
                    i = nextPrime((int)i);
                }
            }

            addFactors(fakt);

            try {
                cb.await();
            } catch (Exception e) {}

            if (index == 0) {
                cleanUp(factors, num);
            }
        }
    }

    void print(long l) {
        if (printCntr < 5 || printCntr > 94) {
            System.out.printf("%,d = ", l);
            while(!factors.isEmpty()) {
                System.out.print(factors.remove(0));
                if (factors.size() > 0)
                    System.out.print("*");
            }
            System.out.println("");
        } else {
            factors.clear();
        }

        printCntr++;
    }

    void cleanUp(ArrayList<Long> a, long num) {
        long l = 1;
        for (int i = 0; i < a.size(); i++) {
            l = l*(long)a.get(i);
        }
        if (l == num) {
            print(num);
        } else {
            a.add(num/l);
            print(num);
        }
    }

    int nextPrime(int i) {
        // returns next prime number after number 'i'
        i++;
        while (i <= maxNum) {
            if (isPrime(i)){
                return i;
            }
            i++;
        }
        return Integer.MAX_VALUE;
    } // end nextTrue


    void printAllPrimes(){
        for ( int i = 2; i <= maxNum; i++)
            if (isPrime(i)) System.out.println(" "+i);
    }

    void generatePrimesByEratosthenes() {
        // crosses out all odd, non-prime numbers in bitArr[]
        crossOut(1);      // 1 is not a prime
        int i = nextPrime(2);
        int end = (int)Math.sqrt(maxNum);

        while (i <= Math.sqrt(end)) {
            crossOutMult(i, 0, end);
            i = nextPrime(i);
        }

        int area = maxNum/cores;

        for (int j = 0; j < cores; j++) {
            new Thread(new ParallellCross(area*j, area*(j+1))).start();     //Should be cleaned up a bit, the first thread will now always cross out for primes down to 2 doing double work
        }
        
        try {
            cb.await();
        } catch (Exception e) {}
    } // end generatePrimesByEratosthenes

    class ParallellCross implements Runnable{
        int start;          //beginning of area this thread should cross out
        int end;            //end of area this thread should cross out

        ParallellCross(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void run() {
            int i = nextPrime(1); //every thread crosses out using primes < sqrt(maxNum)

            while (i <= Math.sqrt(maxNum)) {
                crossOutMult(i, start, end);
                i = nextPrime(i);
            }
            try {
                cb.await();
            } catch (Exception e) {}
        }
    }
} // end class Bool