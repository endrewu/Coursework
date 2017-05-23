class ParallellRadix {
    int cores = Runtime.getRuntime().availableProcessors();

    static void radix2(int[] a) {
        int max = a[0], numBit = 2, n =a.length;
    }

    static void radixSort(int[] a, int[] b, int maskLen, int shift) {

    }
}

    // ver2. sorterer også tall mellom 2**32 og 2**31
    static void radix2(int [] a) {
        // 2 digit radixSort: a[]
        int max = a[0], numBit = 2, n =a.length;
        // a) finn max verdi i a[]
        //TODO parallelliser
        for (int i = 1 ; i < n ; i++)
            if (a[i] > max) max = a[i];
        //ikke paralllelliser
        while (max >= (1L<<numBit) )numBit++; // antall siffer i max

        // bestem antall bit i siffer1 og siffer2
        int bit1 = numBit/2,
        bit2 = numBit-bit1;
        int[] b = new int [n];
        radixSort( a,b, bit1, 0);    // første siffer fra a[] til b[]
        radixSort( b,a, bit2, bit1);// andre siffer, tilbake fra b[] til a[]

        for (int i = 0; i < a.length; i ++) {
            System.out.println(a[i]);
        }
    } // end


    /** Sort a[] on one digit ; number of bits = maskLen, shiftet up ‘shift’ bits */
    static void radixSort ( int [] a, int [] b, int maskLen, int shift){
        int  acumVal = 0, j, n = a.length;
        int mask = (1<<maskLen) -1;
        int [] count = new int [mask+1];

        // b) count=the frequency of each radix value in a
        for (int i = 0; i < n; i++) {
            count[(a[i]>> shift) & mask]++;
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