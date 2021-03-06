package no.uio.ifi.alboc.log;

/*
 * module Log
 */

import java.io.*;
import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.scanner.Scanner;
import static no.uio.ifi.alboc.scanner.Token.*;
import no.uio.ifi.alboc.types.*;

/*
 * Produce logging information.
 */
public class Log {
    public static boolean doLogBinding = false, doLogTypeCheck = false,
        doLogParser = false, doLogScanner = false, doLogTree = false;
    
    private static String logName, curTreeLine = "";
    private static int nLogLines = 0, parseLevel = 0, treeLevel = 0;
    
    public static void init() {
    	logName = AlboC.sourceBaseName + ".log";
    }
    
    public static void finish() {
	//-- Must be changed in part 0:
    }

    private static void writeLogLine(String data) {
        try {
            PrintWriter log = (nLogLines==0 ? new PrintWriter(logName) :
                               new PrintWriter(new FileOutputStream(logName,true)));
            log.println(data);  ++nLogLines;
            log.close();
        } catch (FileNotFoundException e) {
            nLogLines = 0;  // To avoid infinite recursion
            // Error.error -> Log.noteError -> Log.writeLogLine -> ...
            Error.error("Cannot open log file " + logName + "!");
        }
    }

    /*
     * Make a note in the log file that an error has occured.
     * (If the log file is not in use, request is ignored.)
     *
     * @param message  The error message
     */
    public static void noteError(String message) {
    	if (nLogLines > 0) 
            writeLogLine(message);
    }

    public static void enterParser(String symbol) {
    	if (! doLogParser) return;
    	//-- Must be changed in part 1:
    	for(int i = 0; i < parseLevel; i++) {
			symbol = "  " + symbol;
		}
    	parseLevel++;
    	writeLogLine("Parser: " + symbol);
    }

    public static void leaveParser(String symbol) {
    	if (! doLogParser) return;
    	//-- Must be changed in part 1:
    	if(parseLevel > 0) {
    		parseLevel--;
            for(int i = 0; i < parseLevel; i++) {
                symbol = "  " + symbol;
            }
    	} else {
    		Error.error("Wrong in parseLevel. Tried to leave a parser when not enough parsers have" + 
    				" been started. (parseLevel < 0).");
    	}
    	
    	writeLogLine("Parser: " + symbol);
    }

    /**
     * Make a note in the log file that another source line has been read.
     * This note is only made if the user has requested it.
     *
     * @param lineNum  The line number
     * @param line     The actual line
     */
    public static void noteSourceLine(int lineNum, String line) {
    	if (! doLogParser && ! doLogScanner) return;
    	if(line == null) {		//2014-10-02 La til denne if-testen, C
            writeLogLine(lineNum+": ");
    	} else {
            String logLine = lineNum + ": " + line;
            writeLogLine(logLine);
        }
    	//-- Must be changed in part 0:
    }
  
    /**
     * Make a note in the log file that another token has been read 
     * by the Scanner module into Scanner.nextToken.
     * This note will only be made if the user has requested it.
     */
    public static void noteToken() {
    	if (! doLogScanner) return;
        if (Scanner.curToken == null) return;
        String logToken;
        if (Scanner.curToken == numberToken) {
            logToken = "Scanner : " + Scanner.curToken +" "+Scanner.curNum;
        } else if (Scanner.curToken == nameToken) {
            logToken = "Scanner : " + Scanner.curToken +" "+Scanner.curName;
        } else {
            logToken = "Scanner : " + Scanner.curToken;
        }
        writeLogLine(logToken);
	//-- Must be changed in part 0:
    }

    public static void noteBinding(String name, int lineNum, int useLineNum) {
    	if (! doLogBinding) return;
	//-- Must be changed in part 2:
        String logToken;
		if(lineNum == -99) {
			logToken =  logToken = "Binding:  Line " + useLineNum + ": " + name + " refers to declaration in library";
		} else {
			logToken = "Binding:  Line " + useLineNum + ": " + name + " refers to declaration in line " + lineNum;
		}
        writeLogLine(logToken);
    }

    public static void noteTypeCheck(String what, Type t, String s, int lineNum) {
        if (! doLogTypeCheck) return;
      
        writeLogLine("Checking types: " +
                     (lineNum>0 ? "Line "+lineNum+": " : "") + what +
                     ",\n                where Type(" + s + ") is " + t);
    }

    public static void noteTypeCheck(String what, Type t1, String s1, 
                                     Type t2, String s2, int lineNum) {
        if (! doLogTypeCheck) return;
      
        writeLogLine("Checking types: " +
                     (lineNum>0 ? "Line "+lineNum+": " : "") + what +
                     ",\n                where Type(" + s1 + ") is " + t1 +
                     " and Type(" + s2 + ") is " + t2);
    }

    public static void wTree(String s) {
        if (curTreeLine.length() == 0) {
            for (int i = 1;  i <= treeLevel;  ++i) curTreeLine += "  ";
        }
        curTreeLine += s;
    }

    public static void wTreeLn() {
        writeLogLine("Tree:     " + curTreeLine);
        curTreeLine = "";
    }

    public static void wTreeLn(String s) {
        wTree(s);  wTreeLn();
    }

    public static void indentTree() {
    	//-- Must be changed in part 1:
    	treeLevel++;
    }

    public static void outdentTree() {
    	//-- Must be changed in part 1:
    	treeLevel--;
    }
}
