package no.uio.ifi.alboc.scanner;

/*
 * module Scanner
 */

import no.uio.ifi.alboc.chargenerator.CharGenerator;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * Module for forming characters into tokens.
 */
public class Scanner {
    public static Token curToken, nextToken;
    public static String curName, nextName;
    public static int curNum, nextNum;
    public static int curLine, nextLine;
    private static int errorInLine;
    
    public static void init() {
        curToken = null; nextToken = null;
        curName = ""; nextName = "";
        curNum = nextNum = 0;
        curLine = nextLine = 0;
        readNext(); readNext();
    }

    public static void finish() {
        //-- Must be changed in part 0:
    }

    public static void readNext() {
        curToken = nextToken;  curName = nextName;  curNum = nextNum;
        curLine = nextLine;
        nextToken = null;
      
        while (nextToken == null) {
            nextLine = CharGenerator.curLineNum();

            if (! CharGenerator.isMoreToRead()) {
                nextToken = eofToken;
            } else if (CharGenerator.curC == '+') {
                nextToken = addToken;
            } else if (CharGenerator.curC == '&') { 
                nextToken = ampToken;
            } else if (CharGenerator.curC == '=') { 
                if (CharGenerator.nextC == '=') { 
                    nextToken = equalToken;
                    CharGenerator.readNext();
                } else {
                    nextToken = assignToken; 
                }
            } else if (CharGenerator.curC == ',') { 
                nextToken = commaToken;
            } else if (CharGenerator.curC == '/') { 
                if(CharGenerator.nextC == '*') {
                    errorInLine = CharGenerator.curLineNum()+1;
                    while(! (CharGenerator.curC == '*' && CharGenerator.nextC == '/')) { //Handles commented sections /* */
                        if(!CharGenerator.isMoreToRead())
                            Error.error("Comment starting on line " + errorInLine + " never ends!");
                    	CharGenerator.readNext();
                    }
                    CharGenerator.readNext();
                    errorInLine = -1;
                } else {
                    nextToken = divideToken;
                }
            } else if (CharGenerator.curC == '>') {
                if (CharGenerator.nextC == '=') {
                    nextToken = greaterEqualToken;
                    CharGenerator.readNext();   
                } else {
                    nextToken = greaterToken;
                }
            } else if (CharGenerator.curC == '[') {
                nextToken = leftBracketToken;
            } else if (CharGenerator.curC == '{') {
                nextToken = leftCurlToken;
            } else if (CharGenerator.curC == '(') {
                nextToken = leftParToken;
            } else if (CharGenerator.curC == '<') {
                if (CharGenerator.nextC == '=') {
                    nextToken = lessEqualToken;
                    CharGenerator.readNext();
                } else {
                    nextToken = lessToken;
                }
            } else if (CharGenerator.curC == '!') {
                if(CharGenerator.nextC == '=')
                    nextToken = notEqualToken;
                CharGenerator.readNext();
            } else if (isNum09(CharGenerator.curC)) {
                String tmp = "";
                while(isNum09(CharGenerator.curC)) {
                    tmp = tmp+CharGenerator.curC;
                    if (isNum09(CharGenerator.nextC)) {
                        CharGenerator.readNext();
                    } else {
                        break;
                    }
                }
                nextToken = numberToken;
                nextNum = Integer.valueOf(tmp);
            } else if (CharGenerator.curC == ']') {
                nextToken = rightBracketToken;
            } else if (CharGenerator.curC == '}') {
                nextToken = rightCurlToken;
            } else if (CharGenerator.curC == ')') {
                nextToken = rightParToken;
            } else if (CharGenerator.curC == ';') {
                nextToken = semicolonToken;
            } else if (CharGenerator.curC == '*') {
            	if(CharGenerator.nextC == '/') {
                    Error.error("Error at line: " + (nextLine+1) + " Illegal start of expression: */   Comment was never started");
            	} else {
                    nextToken = starToken;
            	}
            } else if (CharGenerator.curC == '-') {
                nextToken = subtractToken;
            } else if (CharGenerator.curC == '.') {
            } else if (isLetterAZ(CharGenerator.curC)) {
                String tmp = "";
                while (isValidName(CharGenerator.curC)) {
                    tmp = tmp+CharGenerator.curC;
                    if (isValidName(CharGenerator.nextC)) {
                        CharGenerator.readNext();
                    } else {
                        break;
                    }
                }

                if (tmp.equals("if")) {
                    nextToken = ifToken;
                } else if (tmp.equals("else")) {
                    nextToken = elseToken;
                } else if (tmp.equals("while")) {
                    nextToken = whileToken;
                } else if (tmp.equals("int")) {
                    nextToken = intToken;
                } else if (tmp.equals("for")) {
                    nextToken = forToken;
                } else if (tmp.equals("return")) {
                    nextToken = returnToken;
                } else {
                    nextName = tmp;
                    nextToken = nameToken;
                }
            } else if ((int)CharGenerator.curC <= 32) { //sorts out all blank-spaces
                //nodo
            } else if (CharGenerator.curC == '\'') {
                if(CharGenerator.nextC == '\n') {
                    Error.error("Error at line: " + nextLine + ". Illegal character constant!");
                } else {
                    CharGenerator.readNext();
                    nextToken = numberToken;
                    nextNum = (int)CharGenerator.curC;
                }
                CharGenerator.readNext();
            } else {
                Error.error(nextLine,
                    "Illegal symbol: '" + CharGenerator.curC + "'!");
            }
            CharGenerator.readNext();
        }
        check(curToken);
        Log.noteToken();
    }

    private static boolean isValidName(char c) {
        if (isLetterAZ(c) || isNum09(c) || c == '_')
            return true;
        return false;
    }

    private static boolean isLetterAZ(char c) {
        if ((int)c > 64 && (int)c < 91) return true;
        if ((int)c > 96 && (int)c < 123) return true;
        
        return false;
    }

    private static boolean isNum09(char c) {
        if (c >= '0' && c <= '9') return true;
        return false;
    }

    public static void check(Token t) {
        if (curToken != t)
            Error.expected("A " + t);
    }
    
    public static void check(Token t1, Token t2) {
        if (curToken != t1 && curToken != t2)
            Error.expected("A " + t1 + " or a " + t2);
    }
    
    public static void skip(Token t) {
        check(t);  readNext();
    }
    
    public static void skip(Token t1, Token t2) {
        check(t1,t2);  readNext();
    }
}
