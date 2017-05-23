package no.uio.ifi.alboc.chargenerator;

/*
 * module CharGenerator
 */

import java.io.*;
import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;

/*
 * Module for reading single characters.
 */

public class CharGenerator {
    public static char curC, nextC;
    
    private static LineNumberReader sourceFile = null;
    private static String sourceLine;
    private static int sourcePos;
    
    public static void init() {
        try {
            sourceFile = new LineNumberReader(new FileReader(AlboC.sourceName));
        } catch (FileNotFoundException e) {
            Error.error("Cannot read " + AlboC.sourceName + "!");
        }
        sourceLine = "";  sourcePos = 0;  curC = nextC = ' ';

        try {
            sourceFile.mark(100);
            Log.noteSourceLine(curLineNum()+1, sourceFile.readLine());
            sourceFile.reset();
        } catch (IOException e) {}

        readNext();  readNext();
    }
    
    public static void finish() {
        if (sourceFile != null) {
            try {
                sourceFile.close();
            } catch (IOException e) {
                Error.error("Could not close source file!");
            }
        }
    }
    
    public static boolean isMoreToRead() {
    	if ((curC >= 32 && curC <= 126) || curC == 10 || curC == 9) {
            return true;
        }
        return false;
    }
    
    public static int curLineNum() {
        return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }
    
    public static int getSourcePos() {
    	return sourcePos;
    }
    
    public static void readNextLine() {
        try {
            sourceFile.mark(100);
            String tmp = sourceFile.readLine();
            if (tmp != "")
                Log.noteSourceLine(curLineNum(), tmp); //Hvis linja er tom for characters
            sourceFile.reset();
            sourcePos = 0;
        } catch (IOException e) {}
    }
    
    public static void skipToNextLine() {
        try {
            sourceFile.readLine();
            readNextLine();
            readNext();
        } catch (IOException e) {}
    }
    
    public static void incrementSourcePos() {
    	sourcePos++;
    }
    
    public static void readNext() {
        curC = nextC;
        if (! isMoreToRead()) return;

        try {
            nextC = (char)sourceFile.read();
            sourcePos++;
        } catch (IOException e) {}
        if (nextC == '\n')
            readNextLine();

        if(nextC == '#' && sourcePos == 1)
            skipToNextLine();
        	
    }
}
