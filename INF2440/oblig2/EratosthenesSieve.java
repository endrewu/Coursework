import java.util.*;

/**
* Implements the bitArray of length 'maxNum' [0..maxNum/16 ]
*   1 - true (is prime number)
*   0 - false
*  can be used up to 2 G Bits (integer range)
*/
public class EratosthenesSieve {
    byte [] bitArr ;           // bitArr[0] represents the 8 integers:  1,3,5,...,15, and so on
    int  maxNum;               // all primes in this bit-array is <= maxNum
    int printCntr = 0;
    final  int [] bitMask = {1,2,4,8,16,32,64,128};  // kanskje trenger du denne
    final  int [] bitMask2 ={255-1,255-2,255-4,255-8,255-16,255-32,255-64, 255-128}; // kanskje trenger du denne


    EratosthenesSieve (int maxNum) {
        long begin = System.nanoTime();
        
        this.maxNum = maxNum;
        bitArr = new byte [(maxNum/16)+1];
        setAllPrime();
        generatePrimesByEratosthenes();

        double end = (System.nanoTime()-begin) / 1000000.0;
        
        System.out.println("Generated all primes <= " +maxNum+ " in " +end+ "ms");
        System.out.println("  using Eratosthenes' sieve");

        factMult(100, (long)maxNum*(long)maxNum);
    } // end konstruktor EratosthenesSieve

    void setAllPrime() {
        for (int i = 0; i < bitArr.length; i++) {
            bitArr[i] = (byte)255;
        }
    }

    void crossOut(int i) {
        // set as not prime- cross out (set to 0)  bit represening 'int i'
        if (i % 2 != 0)
            bitArr[i / 16] &= bitMask2[(i % 16)>>1];
    } 
 
    void crossOutMult(int i) {
        int n = i*i;
        while (n <= maxNum) {
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

    ArrayList<Long> factorize (long num) {
        ArrayList <Long> fakt = new ArrayList <Long>();

        long i = nextPrime(1);
        long n = num;

        while (i < maxNum && i < n) {
            if(n % i == 0) {
                fakt.add(i);
                n = n/i;
            } else {
                i = nextPrime((int)i);
            }
        }
        if (n != 1) fakt.add(n);

        return fakt;
    } // end factorize

    void factMult(int mul, long num) {
        long begin = System.nanoTime();

        for (long l = num-mul; l < num; l++) {
            ArrayList a = factorize(l);
            print(l, a);
        }

        double end = (System.nanoTime()-begin) / 1000000.0;
        System.out.println("100 factorizations calculated in " +end+ "ms");
    }

    void print(long l, ArrayList a) {
        if(printCntr < 5 || printCntr > 94) {
            System.out.printf("%,d = ", l);
            while(!a.isEmpty()) {
                System.out.print(a.remove(0));
                if (a.size() > 0)
                    System.out.print("*");
            }
            System.out.println("");
        } else {
            a.clear();
        }

        printCntr++;
    }

    boolean checkNumbers(ArrayList a, long num) {
        long l = 1;
        for (int i = 0; i < a.size(); i++) {
            l = l*(long)a.get(i);
        }
        if (l == num) return true;
        return false;
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
        // krysser av alle  oddetall i 'bitArr[]' som ikke er primtall (setter de =0)
        crossOut(1);      // 1 er ikke et primtall
        int i = nextPrime(2);
        while (i <= Math.sqrt(maxNum)) {
            crossOutMult(i);
            i = nextPrime(i);
        }
        //printAllPrimes();
    } // end generatePrimesByEratosthenes
} // end class Bool