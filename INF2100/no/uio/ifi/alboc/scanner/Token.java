package no.uio.ifi.alboc.scanner;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token { 
    addToken, ampToken, assignToken, 
    commaToken, 
    divideToken,
    elseToken, eofToken, equalToken, 
    forToken, 
    greaterEqualToken, greaterToken, 
    ifToken, intToken, 
    leftBracketToken, leftCurlToken, leftParToken, lessEqualToken, lessToken, 
    nameToken, notEqualToken, numberToken, 
    returnToken, rightBracketToken, rightCurlToken, rightParToken, 
    semicolonToken, starToken, subtractToken, 
    whileToken;

    public static boolean isFactorOperator(Token t) {
    	if(t == divideToken|| t == starToken) return true;
        return false;
    }

    public static boolean isTermOperator(Token t) {
    	if(t == subtractToken || t == addToken) return true;
        return false;
    }

    public static boolean isPrefixOperator(Token t) {
    	if(t == subtractToken || t == starToken) return true;
    	return false;
    }

    public static boolean isRelOperator(Token t) {
    	if(t == equalToken || t == greaterEqualToken || t == greaterToken||
    	   t == lessEqualToken || t == lessToken || t == notEqualToken) return true;
        return false;
    }

    public static boolean isOperand(Token t) {
        if(t == ampToken || t == assignToken || t == commaToken || t == elseToken ||
           t == equalToken || t == ifToken || t == intToken || t == leftBracketToken ||
           t == leftCurlToken || t == leftParToken || t == nameToken || t == numberToken ||
           t == returnToken || t == rightBracketToken || t == rightCurlToken || t == rightParToken ||
           t == semicolonToken || t == whileToken || t == forToken || t == eofToken) return true;
        return false;
    }
}
