import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

class Oblig3 {
    //for drawing envelope of 100 points
	static int DEBUG_SIZE = 100;
    boolean debug = false;

    int cores;
	int n; //number of points

	int[] x;
	int[] y;

    int minX, maxX;
    int MAX_X, MAX_Y;
    int LIMIT;

    boolean printToScreen = true;
    boolean printToFile = false;
    File file;
    FileWriter fw;

    IntList envelope;

    NPunkter np;

    Oblig3(String[] args) {
        IntList printThis = new IntList();
        IntList fullScope;

        int max = 10000000;
        int min = 100;

        if (args.length > 0)
            handleParams(args);

        cores = Runtime.getRuntime().availableProcessors();
        double lnCore = Math.log(cores);
        double ln2 = Math.log(2);
        LIMIT = (int)(lnCore/ln2)-1;

        //if debug is flagged the program only checks 100 points and later prints them
        if (debug == true) {
            max = 100;
            min = 100;
        }

        //for every size 100 <= n <= 10 000 000 sequential and parallell tasks will run 9 times
        for (int size = max; size >= min; size /= 10) {
            println("Running solutions for "+size+" points");

            np = new NPunkter(size);
            n = size;
            x = new int[size];
            y = new int[size];

            np.fyllArrayer(x, y);

            envelope = new IntList();
            fullScope = new IntList();

            //This is one of the big operations in this task, but since I'm doing it once
            //with no parallell tasks I'm excluding it from the time-taking
            fixMinMax(fullScope);

            print("Sequential: ");
            double[] times = new double[9];
            for (int i = 0; i < 9; i++) {
                envelope.clear();
                long startTime = System.nanoTime();
                sequential(fullScope);
                double endTime = (System.nanoTime() - startTime) / 1000000.0;

                times[i] = endTime;
            }
            Arrays.sort(times);
            println(times[5]+"ms");


            print("Parallell: ");
            for (int i = 0; i < 9; i++) {
                envelope.clear();
                long startTime = System.nanoTime();
                parallell(fullScope);
                double endTime = (System.nanoTime() - startTime) / 1000000.0;
            
                times[i] = endTime;
            }
            Arrays.sort(times);
            println(times[5]+"ms");

            //makes a copy of the envelope for 1000 points that can later be printed
            if (size == 1000) {
                printThis.clear();
                printThis = new IntList(envelope.size());
                for (int i = 0; i < envelope.size(); i++) {
                    int tmp = envelope.get(i);
                    printThis.add(tmp);
                }
            }

        }

        //checks if debug is flagged, if so it draws the 100 points and envelope
        if (debug == true) {
            TegnUt tu = new TegnUt (this, envelope);
            for (int i = 1; i > 0; i++);
        } else {
            print1000envelope(printThis);
        }
    }

    //returns the relative distance of a point point to the line drawn from start to end
    int calculateD(int start, int end, int point) {
        int a, b, c;
        a = y[start] - y[end];
        b = x[end] - x[start];
        c = y[end]*x[start] - y[start]*x[end];

        int t = a*x[point] + b*y[point] + c;
        return t;
    }

    //if the points are on a line, this method will find the closest one to the start of the line and save the points to the envelope in correct order
    void fixLine(int start, int end, IntList scope, IntList superEnvelope) {
        if (scope.size() == 0) {
            return;
        }

        IntList newScope = new IntList();
        int firstPoint = end;
        int pythagoras = pythagoras(start, end);

        for (int i = 0; i < scope.size(); i++) {
            int tmp = scope.get(i);

            int p = pythagoras(start, tmp);

            if (p < pythagoras) {
                if (firstPoint != end) newScope.add(firstPoint);
                pythagoras = p;
                firstPoint = tmp;
            } else {
                newScope.add(tmp);
            }
        }

        superEnvelope.add(firstPoint);
        if (firstPoint == end) return;

        fixLine(firstPoint, end, newScope, superEnvelope); 
    }

    void fixMinMax(IntList scope) {
        minX = 0; maxX = 0;
        MAX_X = 0; MAX_Y = 0;

        //find minX and maxX for initial step
        for (int i = 1; i < x.length; i++) {
            if (x[i] < x[minX]) {
                scope.add(minX);
                minX = i;
            } else if (x[i] > x[maxX]) {
                scope.add(maxX);
                maxX = i;
                MAX_X = x[i];
            } else {
                scope.add(i);
            }
            if (y[i] > MAX_Y) MAX_Y = y[i];

        }
    }

    //checks all points in a scope for distance to the line. Sorts all the points on the right side 
    //of the line into a new scope and returns the points with the biggest negative value
    int fixScope(int start, int end, IntList scope, IntList newScope) {
        int min = 0;
        int newPoint = -1;

        for (int i = 0; i < scope.size(); i++) {
            int tmp = scope.get(i);

            int d = calculateD(start, end, tmp);

            if (d <= 0) {
                if (d < min) {
                    min = d;
                    if (newPoint != -1)
                        newScope.add(newPoint);
                    newPoint = tmp;
                } else {
                    newScope.add(tmp);
                }
            }
        }

        return newPoint;
    }

    //Parallell method to find the envelope
    void parallell(IntList fullScope) {
        IntList leftScope = new IntList();
        IntList rightScope = new IntList();
        int min = 0, max = 0, leftPoint = -1, rightPoint = -1;
        Thread left, right;

        for (int i = 0; i < x.length; i++) {
            int d = calculateD(minX, maxX, i);

            if (d < 0) {
                if (d < min) {
                    min = d;
                    if (rightPoint != -1)
                        rightScope.add(rightPoint);
                    rightPoint = i;
                } else {
                    rightScope.add(i);
                }
            }
            if (d > 0) {
                if (d >= max) {
                    max = d;
                    if (leftPoint != -1)
                        leftScope.add(leftPoint);
                    leftPoint = i;
                } else {
                    leftScope.add(i);
                }
            }
        }

        IntList leftEnvelope = new IntList(leftScope.size());
        IntList rightEnvelope = new IntList(rightScope.size());

        right = new Thread(new Para(minX, maxX, rightScope, rightPoint, rightEnvelope, 0));
        right.start();
        left = new Thread(new Para(maxX, minX, leftScope, leftPoint, leftEnvelope, 0));
        left.start();

        try {
            right.join();
            left.join();
        } catch (Exception e) {
            println("Error joining threads in method parallell");
        }

        envelope.add(minX);
        envelope.add(rightEnvelope);
        envelope.add(maxX);
        envelope.add(leftEnvelope);
    }

    //custom method to replace System.out.print
    void print(String s) {
        if(printToScreen == true) {
            System.out.print(s);
        }

        if(printToFile == true) {
            try {
                fw = new FileWriter(file, true);
                fw.append(s);
                fw.close();
            } catch (Exception IOException) {
                System.out.println("Unable to create FileWriter for "+file);
            }
        }
    }

    //method to recreate 1000 points so they can be printed with coordinates
    void print1000envelope(IntList e) {
        x = new int[1000];
        y = new int[1000];

        np = new NPunkter(1000);

        np.fyllArrayer(x, y);

        println("Printing "+e.size() +" points, envelope of "+ 1000 +" points, found in parallell");
        for (int i = 0; i < e.size(); i++) {
            int temp = e.get(i);
            println(e.get(i) +" ( "+x[temp]+","+y[temp]+" )");
        }
    }

    //custom method to replace System.out.println
    void println(String s) {
        if(printToScreen == true) {
            System.out.println(s);
        }

        if(printToFile == true) {
            try {
                fw = new FileWriter(file, true);
                fw.append(s+"\n");
                fw.close();
            } catch (Exception IOException) {
                System.out.println("Unable to create FileWriter for "+file);
            }
        }
    }

    //calculates relative length between two objects on a line
    int pythagoras(int start, int point) {
        int a = y[point] - y[start];
        int b = x[point] - x[start];

        int c = a*a + b*b;
        return c;
    }

    //recursive method
    void seqRec(int start, int end, IntList scope, int point, IntList superEnvelope) {
        IntList leftScope = new IntList();
        IntList rightScope = new IntList();

        int rightPoint = fixScope(start, point, scope, rightScope);
        if (rightPoint != -1) {
            seqRec(start, point, rightScope, rightPoint, superEnvelope);
        } else if (rightScope.size() > 0) {
            fixLine(start, point, rightScope, superEnvelope);
        }

        superEnvelope.add(point);

        int leftPoint = fixScope(point, end, scope, leftScope);
        if (leftPoint != -1) {
            seqRec(point, end, leftScope, leftPoint, superEnvelope);
        } else if (leftScope.size() > 0) {
            fixLine(point, end, leftScope, superEnvelope);
        }
    }

    void sequential(IntList fullScope) {
        IntList leftScope = new IntList();
        IntList rightScope = new IntList();
        int min = 0, max = 0, leftPoint = -1, rightPoint = -1;

        for (int i = 0; i < x.length; i++) {
            int d = calculateD(minX, maxX, i);

            if (d < 0) {
                if (d < min) {
                    min = d;
                    if (rightPoint != -1)
                        rightScope.add(rightPoint);
                    rightPoint = i;
                } else {
                    rightScope.add(i);
                }
            }
            if (d > 0) {
                if (d >= max) {
                    max = d;
                    if (leftPoint != -1)
                        leftScope.add(leftPoint);
                    leftPoint = i;
                } else {
                    leftScope.add(i);
                }
            }
        }

        envelope.add(minX);
        seqRec (minX, maxX, rightScope, rightPoint, envelope);
        envelope.add(maxX);
        seqRec (maxX, minX, leftScope, leftPoint, envelope);
    }

    //method to handle flags given as parameters for the program
    void handleParams(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().equals("-debug")) {
                debug = true;
            }

            if (args[i].toLowerCase().equals("-f")) {
                printToFile = true;
                try {
                    if (args[i+1].charAt(0) != '-') {
                        file = new File(args[i+1]);
                    } else {
                        System.out.println("Unable to create file. -f must be trailed by a valid filepath");
                        printToFile = false;
                    }
                } catch (Exception e) {
                    System.out.println("Unable to create file. -f must be trailed by a valid filepath");
                }
            }

            if (args[i].toLowerCase().equals("-p")) {
                printToScreen = false;
            }
        }

    }

    public static void main (String[] args) {
        new Oblig3(args);
    }

    class Para implements Runnable {
        int start, end, point, depth;
        IntList scope, superEnvelope, leftEnvelope, rightEnvelope, leftScope, rightScope;
        Thread left, right;
        
        Para(int start, int end, IntList scope, int point, IntList superEnvelope, int depth) {
            this.start = start;
            this.end = end;
            this.point = point;
            this.scope = scope;
            this.superEnvelope = superEnvelope;

            leftEnvelope = new IntList();
            rightEnvelope = new IntList();
            leftScope = new IntList();
            rightScope = new IntList();
        }

        public void run() {
            if (depth < LIMIT) {
                int rightPoint = fixScope(start, point, scope, rightScope);
                if (rightPoint != -1) {
                    right = new Thread(new Para(start, point, rightScope, rightPoint, rightEnvelope, depth+1));
                    right.start();
                } else if (rightScope.size() > 0) {
                    fixLine(start, point, rightScope, rightEnvelope);
                }

                int leftPoint = fixScope(point, end, scope, leftScope);
                if (leftPoint != -1) {
                    left = new Thread(new Para(point, end, leftScope, leftPoint, leftEnvelope, depth+1));
                    left.start();
                } else if (leftScope.size() > 0) {
                    fixLine(point, end, leftScope, leftEnvelope);
                }

                try {
                    if (right != null)
                        right.join();
                    if (left != null)
                        left.join();
                } catch (Exception e) {
                    println("Failed to join threads in Para");
                }
            } else {
                int rightPoint = fixScope(start, point, scope, rightScope);
                if (rightPoint != -1) {
                    seqRec(start, point, rightScope, rightPoint, rightEnvelope);
                } else if (rightScope.size() > 0) {
                    fixLine(start, point, rightScope, rightEnvelope);
                }

                int leftPoint = fixScope(point, end, scope, leftScope);
                if (leftPoint != -1) {
                    seqRec(point, end, leftScope, leftPoint, leftEnvelope);
                } else if (leftScope.size() > 0) {
                    fixLine(point, end, leftScope, leftEnvelope);
                }
            }

            superEnvelope.add(rightEnvelope);
            superEnvelope.add(point);
            superEnvelope.add(leftEnvelope);
        }
    }
}