package no.uio.ifi.alboc.syntax;

/*
 * module Syntax
 */

import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.chargenerator.CharGenerator;
import no.uio.ifi.alboc.code.Code;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import no.uio.ifi.alboc.scanner.Scanner;
import no.uio.ifi.alboc.scanner.Token;
import static no.uio.ifi.alboc.scanner.Token.*;
import no.uio.ifi.alboc.types.*;

/*
 * Creates a syntax tree by parsing an AlboC program; 
 * prints the parse tree (if requested);
 * checks it;
 * generates executable code. 
 */
public class Syntax {
    static DeclList library;
    static Program program;

    public static void init() {
    //-- Must be changed in part 1+2:
        library = new GlobalDeclList();
		library.addDecl(new FuncLibrary("getchar", Types.intType, null));
		library.addDecl(new FuncLibrary("getint", Types.intType, null));
		library.addDecl(new FuncLibrary("putchar", Types.intType, Types.intType));
		library.addDecl(new FuncLibrary("putint", Types.intType, Types.intType));
		library.addDecl(new FuncLibrary("exit", Types.intType, Types.intType));
    }

    public static void finish() {
    //-- Must be changed in part 1+2:
    }

    public static void checkProgram() {
        program.check(library);
    }

    public static void genCode() {
        program.genCode(null);
    }

    public static void parseProgram() {
        program = Program.parse();
    }

    public static void printProgram() {
        program.printTree();
    }
}


/*
 * Super class for all syntactic units.
 * (This class is not mentioned in the syntax diagrams.)
 */
abstract class SyntaxUnit {
    int lineNum;

    SyntaxUnit() {
        lineNum = Scanner.curLine;
    }

    abstract void check(DeclList curDecls);
    abstract void genCode(FuncDecl curFunc);
    abstract void printTree();

    void error(String message) {
        Error.error(lineNum, message);
    }
}

class FuncLibrary extends FuncDecl {
	FuncLibrary(String name, Type type, Type paramType) {
		super(name);
		type = type; 
		lineNum = -99;
		
		funcParams = new ParamDeclList();
		
		if(paramType != null) {
			ParamDecl pd = new ParamDecl("library");
			pd.type = paramType;
			funcParams.addDecl(pd);
		}
	}
}
/*
 * A <Program>
 */
class Program extends SyntaxUnit {
    DeclList progDecls;
    
    @Override void check(DeclList curDecls) {
        progDecls.check(curDecls);

        if (! AlboC.noLink) {
        // Check that 'main' has been declared properly:
        //-- Must be changed in part 2:
			Declaration d = progDecls.findDecl("main", this);
			if(d.type != Types.intType) Error.error("'main' should be an int function!");
			if(d.numParams() > 0)  Error.error("Function 'main' should have no parameters!");
        }
    }

    @Override void genCode(FuncDecl curFunc) {
        progDecls.genCode(null);
    }

    static Program parse() {
        Log.enterParser("<program>");

        Program p = new Program();
        p.progDecls = GlobalDeclList.parse();
        if (Scanner.curToken != eofToken)
            Error.expected("A declaration");

        Log.leaveParser("</program>");
        return p;
    }

    @Override void printTree() {
        progDecls.printTree();
    }
}


/*
 * A declaration list.
 * (This class is not mentioned in the syntax diagrams.)
 */

abstract class DeclList extends SyntaxUnit {
    Declaration firstDecl = null;
    DeclList outerScope;

    DeclList () {
    //-- Must be changed in part 1:
    }

    @Override void check(DeclList curDecls) {
        outerScope = curDecls;

        Declaration dx = firstDecl;
        while (dx != null) {
            dx.check(this);  dx = dx.nextDecl;
        }
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        Declaration d = firstDecl;
        while (d != null) {
            d.printTree();  d = d.nextDecl;
        }    
        //Slutt endring
    }

    void addDecl(Declaration d) {
    //-- Must be changed in part 1:
        //Start endring
        if (firstDecl == null) {
            System.out.println("Adding to empty list: " + d.name);
            firstDecl = d;
        } else {
            Declaration tempDecl = firstDecl;
            while(tempDecl.nextDecl != null) {
                System.out.println("added: "+d.name+" in scope: "+tempDecl.name);
                if(tempDecl.name.equals(d.name)) error("This name is already in this scope: " + d.name);
                tempDecl = tempDecl.nextDecl;
            }
            tempDecl.nextDecl = d;
        }
        //Slutt endring
    }

    int dataSize() {
        Declaration dx = firstDecl;
        int res = 0;

        while (dx != null) {
            res += dx.declSize();  dx = dx.nextDecl;
        }
        return res;
    }

    Declaration findDecl(String name, SyntaxUnit use) {
    //-- Must be changed in part 2:
        Declaration d = firstDecl;
        while(d != null) {
            if(d.name.equals(name)) {
                Log.noteBinding(d.name, d.lineNum, use.lineNum);
                return d; 
            }
            d = d.nextDecl;
        }
        if(outerScope == null) Error.error("Declaration: " + name + " is unknown");
        return outerScope.findDecl(name,use);
    }
	
	int numDecls() {
		Declaration d = firstDecl;
		int num = 0;
		while(d != null) {
			num++;
			d = d.nextDecl;
		}
		return num;
	}
}


/*
 * A list of global declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class GlobalDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        Declaration tempDecl = firstDecl;
        while (tempDecl != null) {
            tempDecl.genCode(curFunc);
            tempDecl = tempDecl.nextDecl;
        }
    }

    static GlobalDeclList parse() {
        GlobalDeclList gdl = new GlobalDeclList();

        while (Scanner.curToken == intToken) {
            DeclType ts = DeclType.parse();
            gdl.addDecl(Declaration.parse(ts));
        }
        return gdl;
    }
}


/*
 * A list of local declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class LocalDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2: 
        int increment = 0;
        Declaration lvd = firstDecl;
        
        while(lvd != null) {
            lvd.assemblerName = (-lvd.declSize()-increment)+"(%ebp)";
            increment += lvd.declSize();
            lvd.genCode(curFunc);
            lvd = lvd.nextDecl;
        }
    }

    static LocalDeclList parse() {
    //-- Must be changed in part 1:
        //Start endring
        LocalDeclList ldl = new LocalDeclList();
        
        while (Scanner.curToken == intToken) {
            DeclType ts = DeclType.parse();
            ldl.addDecl(Declaration.parse(ts));
        }
        //Slutt endring
        return ldl;
    }
}


/*
 * A list of parameter declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class ParamDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        int increment = 8;
        
        Declaration d = firstDecl;
        while(d != null) {
            d.assemblerName = increment+"(%ebp)";
            increment += d.declSize();
            d.genCode(curFunc);
            d = d.nextDecl;
        }  
    }

    static ParamDeclList parse() {
    //-- Must be changed in part 1:
        //Start endring
        ParamDeclList pdl = new ParamDeclList();
        
        while (Scanner.curToken == intToken) {
            DeclType ts = DeclType.parse();
            pdl.addDecl(ParamDecl.parse(ts));
            if(Scanner.curToken == commaToken) Scanner.skip(commaToken);
        }
        //slutt endring
        return pdl;
    }

    @Override void printTree() {
        Declaration px = firstDecl;
        while (px != null) {
            px.printTree();  px = px.nextDecl;
            if (px != null) Log.wTree(", ");
        }
    }
}


/*
 * A <type>
 */
class DeclType extends SyntaxUnit {
    int numStars = 0;
    Type type;

    @Override void check(DeclList curDecls) {
        type = Types.intType;
        for (int i = 1;  i <= numStars;  ++i)
            type = new PointerType(type);
    }

    @Override void genCode(FuncDecl curFunc) {}

    static DeclType parse() {
        Log.enterParser("<type>");
        DeclType dt = new DeclType();

        Scanner.skip(intToken);
        while (Scanner.curToken == starToken) {
            ++dt.numStars;
            Scanner.readNext();
        }

        Log.leaveParser("</type>");
        return dt;
    }

    @Override void printTree() {
        Log.wTree("int ");
        for (int i = 1;  i <= numStars;  ++i) Log.wTree("*");
    }
}


/*
 * Any kind of declaration.
 */
abstract class Declaration extends SyntaxUnit {
    String name, assemblerName;
    DeclType typeSpec;
    Type type;
    boolean visible = false;
    Declaration nextDecl = null;

    Declaration(String n) {
        name = n;
    }

    abstract int declSize();

    int numParams() {return -1;}
    
    static Declaration parse(DeclType dt) {
        Declaration d = null;
        if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) {
            d = FuncDecl.parse(dt);
        } else if (Scanner.curToken == nameToken) {
            d = GlobalVarDecl.parse(dt);
        } else {
            Error.expected("A declaration name");
        }
        d.typeSpec = dt;
        return d;
    }


    /**
     * checkWhetherVariable: Utility method to check whether this Declaration is
     * really a variable. The compiler must check that a name is used properly;
     * for instance, using a variable name a in "a()" is illegal.
     * This is handled in the following way:
     * <ul>
     * <li> When a name a is found in a setting which implies that should be a
     *      variable, the parser will first search for a's declaration d.
     * <li> The parser will call d.checkWhetherVariable(this).
     * <li> Every sub-class of Declaration will implement a checkWhetherVariable.
     *      If the declaration is indeed a variable, checkWhetherVariable will do
     *      nothing, but if it is not, the method will give an error message.
     * </ul>
     * Examples
     * <dl>
     *  <dt>GlobalVarDecl.checkWhetherVariable(...)</dt>
     *  <dd>will do nothing, as everything is all right.</dd>
     *  <dt>FuncDecl.checkWhetherVariable(...)</dt>
     *  <dd>will give an error message.</dd>
     * </dl>
     */
    abstract void checkWhetherVariable(SyntaxUnit use);

    /**
     * checkWhetherFunction: Utility method to check whether this Declaration
     * is really a function.
     * 
     * @param nParamsUsed Number of parameters used in the actual call.
     *                    (The method will give an error message if the
     *                    function was used with too many or too few parameters.)
     * @param use From where is the check performed?
     * @see   checkWhetherVariable
     */
    abstract void checkWhetherFunction(int nParamsUsed, SyntaxUnit use);
}


/*
 * A <var decl>
 */
abstract class VarDecl extends Declaration {
    boolean isArray = false;
    int numElems = 1;

    VarDecl(String n) {
        super(n);
    }

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
        typeSpec.check(curDecls);
        
        if(isArray) {
            type = new ArrayType(typeSpec.type, numElems);
        } else {
            type = typeSpec.type;
        }
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        if(isArray) {
            typeSpec.printTree();
            Log.wTree(name + '[' + numElems + ']');
        } else {
            typeSpec.printTree();
            Log.wTree(name);
        }
        Log.wTree(";");
        Log.wTreeLn();
        //slutt endring
    }

    @Override int declSize() {
        return type.size();
    }

    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
        use.error(name + " is a variable and no function!");
    }

    @Override void checkWhetherVariable(SyntaxUnit use) {
    // OK
    }
}


/*
 * A global <var decl>.
 */
class GlobalVarDecl extends VarDecl {
    GlobalVarDecl(String n) {
        super(n);
        assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        // vi maa ha med type, navn, om array, da og arrayverdi
        int n;
        if(isArray) { 
            n = numElems; 
        } else {
            n = 1;
        }
        int nBytes = 4;
        Code.genVar(name, true, n, nBytes, type+" "+name); 
    }

    static GlobalVarDecl parse(DeclType dt) {
        //Start endringer
        Log.enterParser("<var decl>");
        GlobalVarDecl gvd = null;
        if(Scanner.curToken == nameToken) {
            gvd = new GlobalVarDecl(Scanner.curName);
            gvd.typeSpec = dt;
            Scanner.skip(nameToken);
            if(Scanner.curToken == leftBracketToken) {
                gvd.isArray = true;
                Scanner.skip(leftBracketToken);
                gvd.numElems = Scanner.curNum; //Antar at dette er hva vi bruker for aa avgjore posisjon i array
                Scanner.skip(numberToken);
                Scanner.skip(rightBracketToken);
            }
        }
        Scanner.skip(semicolonToken);
        Log.leaveParser("</var decl>");
        //slutt endringer
    //-- Must be changed in part 1:
        return gvd;
    }
}


/*
 * A local variable declaration
 */
class LocalVarDecl extends VarDecl {
    LocalVarDecl(String n) {
        super(n);
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:

    }

    static LocalVarDecl parse(DeclType dt) {
        Log.enterParser("<var decl>");
        LocalVarDecl lvd = null;
        if (Scanner.curToken == nameToken) {
            lvd = new LocalVarDecl(Scanner.curName);
            lvd.typeSpec = dt;
            Scanner.skip(nameToken);
            if(Scanner.curToken == leftBracketToken) {
                Scanner.skip(leftBracketToken);
                lvd.isArray = true;
                Number n = Number.parse();
                lvd.numElems = n.numVal;
                Scanner.skip(rightBracketToken);
            } 
            Scanner.skip(semicolonToken);
        } else {
            Error.expected("A nameToken");
        }

        Log.leaveParser("</var decl>");
    //-- Must be changed in part 1:
        return lvd;
    }
}


/*
 * A <param decl>
 */
class ParamDecl extends VarDecl {
    ParamDecl(String n) {
        super(n);
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
    }

    static ParamDecl parse(DeclType dt) {
        //-- Must be changed in part 1:
        Log.enterParser("<param decl>");
        ParamDecl pd = null;
        
        if(Scanner.curToken==nameToken) {
            pd = new ParamDecl(Scanner.curName);
            Scanner.skip(nameToken);
            pd.typeSpec = dt;
        } else {
            Error.expected("A name");
        }
        Log.leaveParser("</param decl>");
        return pd;
    }

    @Override void printTree() {
        typeSpec.printTree();  Log.wTree(name);
    }
}


/*
 * A <func decl>
 */
class FuncDecl extends Declaration {
    ParamDeclList funcParams;
    String exitLabel;
    StatmList stmt;
    LocalDeclList localDeclList;
    
    FuncDecl(String n) {
    // Used for user functions:
        super(n);
        assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
    //-- Must be changed in part 1:
    }

    @Override int declSize() {
        return 0;
    }

	int numParams() {
		if(funcParams != null)  return funcParams.numDecls();
		return 0;
	}

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
        typeSpec.check(curDecls);
        type = typeSpec.type;
        funcParams.check(curDecls);
         
        localDeclList.check(funcParams);
        stmt.check(localDeclList);
    }

    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
    //-- Must be changed in part 2:
        int n = funcParams.numDecls();
		if(n != nParamsUsed) {
			Error.error("Calls to "+name+" should have " + n + " parameters, not " + nParamsUsed);
		}
    }

    @Override void checkWhetherVariable(SyntaxUnit use) {
    //-- Must be changed in part 2:
        use.error(name + " is a function and no variable!");
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        Code.genInstr("",".globl",assemblerName,"");
        int size = localDeclList.dataSize();
        Code.genInstr(assemblerName, "enter", "$"+size+",$0", "Start function "+name); 
        
        //Mye av dette skal vel i FunctionCall, ikke FuncDecl?
        if(funcParams != null)      funcParams.genCode(this);
        if(localDeclList != null)   localDeclList.genCode(this); 
        if(stmt != null)            stmt.genCode(this);
        
        Code.genInstr(".exit$"+name, "", "", "");
        Code.genInstr("", "leave", "", "");
        Code.genInstr("", "ret", "", "End function "+name);
    }

    static FuncDecl parse(DeclType ts) {
    //-- Must be changed in part 1:
        Log.enterParser("<func decl>");
        FuncDecl fd = null;
        VarDecl vd = null;

        if(Scanner.curToken == nameToken && Scanner.nextToken == leftParToken) {
            fd = new FuncDecl(Scanner.curName);
            Scanner.skip(nameToken);
            Scanner.skip(leftParToken);
            if(Scanner.nextToken != rightParToken) {
                fd.funcParams = ParamDeclList.parse();
            }
            
            Scanner.skip(rightParToken);
            Log.enterParser("<func body>");
            Scanner.skip(leftCurlToken);

            fd.localDeclList = LocalDeclList.parse();
            fd.stmt = StatmList.parse();

            Scanner.skip(rightCurlToken);
            Log.leaveParser("</func body>");
            //Must be changed in part 1
        }
        Log.leaveParser("</func decl>");
        return fd;
    }

    @Override void printTree() {
        //-- Must be changed in part 1:
        //Start endring
        typeSpec.printTree();
        Log.wTree(name + "(");
        if(Scanner.curToken != rightParToken){
            funcParams.printTree();
        }
        Log.wTreeLn(") {");
        Log.indentTree();
        localDeclList.printTree();
        stmt.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
    }
}


/*
 * A <statm list>.
 */
class StatmList extends SyntaxUnit {
    //-- Must be changed in part 1:
    Statement firstStatm;

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
        Statement s = firstStatm;
        while(s != null) {
            s.check(curDecls);
            s = s.nextStatm;
        }
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        Statement tempStatm = firstStatm;
        while (tempStatm != null) {
            tempStatm.genCode(curFunc);
            tempStatm = tempStatm.nextStatm;
        }
    }

    static StatmList parse() {
        Log.enterParser("<statm list>");
        StatmList sl = new StatmList();
        Statement lastStatm = sl.firstStatm = Statement.parse();
        while (Scanner.curToken != rightCurlToken) {
        //-- Must be changed in part 1:
            lastStatm.nextStatm = Statement.parse();
            lastStatm = lastStatm.nextStatm;
        }
       
        Log.leaveParser("</statm list>");
        return sl;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        Statement s = firstStatm;
        while (s != null) {
            s.printTree();  s = s.nextStatm;
        }    
        //Slutt endring
    }
}


/*
 * A <statement>.
 */
abstract class Statement extends SyntaxUnit {
    Statement nextStatm = null;

    static Statement parse() {
        Log.enterParser("<statement>");
     
        Statement s = null;
        if (Scanner.curToken==nameToken && 
            Scanner.nextToken==leftParToken) {
        //-- Must be changed in part 1:
            s = CallStatm.parse();
        } else if (Scanner.curToken==nameToken || Scanner.curToken==starToken) {
        //-- Must be changed in part 1:
            s = AssignStatm.parse();
        } else if (Scanner.curToken == forToken) {
        //-- Must be changed in part 1:
            s = ForStatm.parse();
        } else if (Scanner.curToken == ifToken) {
            s = IfStatm.parse();
        } else if (Scanner.curToken == returnToken) {
        //-- Must be changed in part 1:
            s = ReturnStatm.parse();
        } else if (Scanner.curToken == whileToken) {
            s = WhileStatm.parse();
        } else if (Scanner.curToken == semicolonToken) {
            s = EmptyStatm.parse();
        } else {
            Error.expected("A statement");
        }

        Log.leaveParser("</statement>");
        return s;
    }
}




/*
 * An <empty statm>.
 */
class EmptyStatm extends Statement {
    //-- Must be changed in part 1+2:

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
    }

    static EmptyStatm parse() {
    //-- Must be changed in part 1:
        Log.enterParser("<empty statm>");
        EmptyStatm es = new EmptyStatm();
        Scanner.skip(semicolonToken);
        Log.leaveParser("</empty statm>");
        return es;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        Log.wTreeLn(";");
        //Slutt endring
    }
}


class CallStatm extends Statement {
    FunctionCall fc;

    @Override void check(DeclList curDecls) {
        fc.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
        fc.genCode(curFunc);
    }

    static CallStatm parse() {
    //-- Must be changed in part 1:
        Log.enterParser("<call-statm>");
        CallStatm cs = new CallStatm();

        cs.fc = FunctionCall.parse();
        Scanner.skip(semicolonToken);

        Log.leaveParser("</call-statm>");
        return cs;
    }

    @Override void printTree() {
        //Start endring
        fc.printTree();
        Log.wTreeLn(";");
        //slutt endring
    
    }
}


/*
 * A <for-statm>.
 */
class ForStatm extends Statement {
    StatmList body;
    Assignment init;
    Expression expr;
    Assignment incr;

    @Override void check(DeclList curDecls) {
        init.check(curDecls);
        expr.check(curDecls);
        incr.check(curDecls);
        body.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
        String testLabel = Code.getLocalLabel(), 
                endLabel  = Code.getLocalLabel();
            Code.genInstr("", "", "", "Start for-statement");
            init.genCode(curFunc);
        
            Code.genInstr(testLabel, "", "", "");
            expr.genCode(curFunc);
            Code.genInstr("", "cmpl", "$0,%eax", "");
            Code.genInstr("", "je", endLabel, "");
            body.genCode(curFunc);
            
            incr.genCode(curFunc);
            
            Code.genInstr("", "jmp", testLabel, "");
            Code.genInstr(endLabel, "", "", "End for-statement");
    }

    static ForStatm parse() {
        Log.enterParser("<for-statm>");
        ForStatm fs = new ForStatm();

        Scanner.skip(forToken);
        Scanner.skip(leftParToken);
        Log.enterParser("<for-control>");
        fs.init = Assignment.parse();
        Scanner.skip(semicolonToken);
        fs.expr = Expression.parse();
        Scanner.skip(semicolonToken);
        fs.incr = Assignment.parse();

        Log.leaveParser("</for-control>");
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        fs.body = StatmList.parse();
        Scanner.skip(rightCurlToken);

        Log.leaveParser("</for-statm>");
        return fs;
    }

    @Override void printTree() {
        //Start endring
        Log.wTree("for (");
        init.printTree();
        Log.wTree("; ");
        expr.printTree();
        Log.wTree("; ");
        incr.printTree();
        Log.wTreeLn(") {");
        Log.indentTree();
        body.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
        //Slutt endring
    }
}

class AssignStatm extends Statement {
    Assignment assign;

    @Override void check(DeclList curDecls) {
        assign.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
        assign.genCode(curFunc);
    }

    static AssignStatm parse() {
        Log.enterParser("<assign-statm>");
        AssignStatm as = new AssignStatm();

        as.assign = Assignment.parse();
        Scanner.skip(semicolonToken);

        Log.leaveParser("</assign-statm>");
        return as;
    }

    @Override void printTree() {
        assign.printTree();
        Log.wTreeLn(";");
    }
}


class Assignment extends Statement {
    LhsVariable lv;
    Expression expr;

    @Override void check(DeclList curDecls) {
        lv.check(curDecls);
        expr.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
        lv.genCode(curFunc);
        Code.genInstr("", "pushl", "%eax","");
        expr.genCode(curFunc);
        Code.genInstr("", "popl", "%edx", "");
        Code.genInstr("", "movl", "%eax,(%edx)", "  =");
    }

    static Assignment parse() {
        Log.enterParser("<assignment>");
        Assignment assign = new Assignment();

        assign.lv = LhsVariable.parse();
        Scanner.skip(assignToken);
        assign.expr = Expression.parse();

        Log.leaveParser("</assignment>");
        return assign;
    }

    @Override void printTree() {
        lv.printTree();
        Log.wTree(" = ");
        expr.printTree();
    }
}


/*
 * An <if-statm>.
 */
class IfStatm extends Statement {
    //-- Must be changed in part 1+2:
    Expression test;
    StatmList body;
    StatmList elsePart;

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
        test.check(curDecls);
        body.check(curDecls);
        if(elsePart != null) elsePart.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        String testLabel = Code.getLocalLabel(), 
                endLabel  = Code.getLocalLabel();
        Code.genInstr("", "", "", "Start if-statement");
        test.genCode(curFunc);
        
        if(elsePart != null) {
            Code.genInstr("", "cmpl", "$0,%eax", "");
            Code.genInstr("", "je", testLabel, ""); 
            body.genCode(curFunc);
            Code.genInstr("", "jmp", endLabel, "");
            Code.genInstr(testLabel, "", "", "  else-part");
            elsePart.genCode(curFunc);
            Code.genInstr(endLabel, "", "", "End if-statement");
        } else {
            Code.genInstr("", "cmpl", "$0,%eax", "");
            Code.genInstr("", "je", endLabel, "");
            body.genCode(curFunc);
            Code.genInstr(endLabel, "", "", "End if-statement");
        }
    }

    static IfStatm parse() {
    //-- Must be changed in part 1:
        Log.enterParser("<if-statm>");
        IfStatm is = new IfStatm();
        Scanner.skip(ifToken);
        Scanner.skip(leftParToken);
        is.test = Expression.parse();
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        is.body = StatmList.parse();
        Scanner.skip(rightCurlToken);

        if (Scanner.curToken == elseToken) {
            Log.enterParser("<else-part>");
            Scanner.skip(elseToken);
            Scanner.skip(leftCurlToken);
            is.elsePart = StatmList.parse();
            Log.leaveParser("</else-part>");
            Scanner.skip(rightCurlToken);
        }
        
        Log.leaveParser("</if-statm>");
        return is;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        Log.wTree("if (");
        test.printTree();
        Log.wTreeLn(") {");
        Log.indentTree();
        body.printTree();
        Log.outdentTree();
    
        if (elsePart != null) {
            Log.wTreeLn("} else {");
            Log.indentTree();
            elsePart.printTree();
            Log.outdentTree();
            
        }
        Log.wTreeLn("}");
        //Slutt endring
    }
}


/*
 * A <return-statm>.
 */
class ReturnStatm extends Statement {
    Expression expr;

    @Override void check(DeclList curDecls) {
        expr.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
        expr.genCode(curFunc);
        Code.genInstr("", "jmp", ".exit$"+curFunc.name, "Return-statement");
    }

    static ReturnStatm parse() {
    Log.enterParser("<return-statm>");
    Scanner.skip(returnToken);
    ReturnStatm rs = new ReturnStatm();
    rs.expr = Expression.parse();
    Scanner.skip(semicolonToken);

    Log.leaveParser("</return-statm>");
    return rs;
    }

    @Override void printTree() {
        Log.wTree("return ");
        expr.printTree();
        Log.wTreeLn(";");

    }
}


/*
 * A <while-statm>.
 */
class WhileStatm extends Statement {
    Expression test;
    StatmList body;

    @Override void check(DeclList curDecls) {
        test.check(curDecls);
        body.check(curDecls);

        Log.noteTypeCheck("while (t) ...", test.type, "t", lineNum);
        if (test.type instanceof ValueType) {
        // OK
        } else {
            error("While-test must be a value.");
        }
    }

    @Override void genCode(FuncDecl curFunc) {
        String testLabel = Code.getLocalLabel(), 
            endLabel  = Code.getLocalLabel();

        Code.genInstr(testLabel, "", "", "Start while-statement");
        test.genCode(curFunc);
        Code.genInstr("", "cmpl", "$0,%eax", "");
        Code.genInstr("", "je", endLabel, "");
        body.genCode(curFunc);
        Code.genInstr("", "jmp", testLabel, "");
        Code.genInstr(endLabel, "", "", "End while-statement");
    }

    static WhileStatm parse() {
        Log.enterParser("<while-statm>");

        WhileStatm ws = new WhileStatm();
        Scanner.skip(whileToken);
        Scanner.skip(leftParToken);
        ws.test = Expression.parse();
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        ws.body = StatmList.parse();
        Scanner.skip(rightCurlToken);

        Log.leaveParser("</while-statm>");
        return ws;
    }

    @Override void printTree() {
        Log.wTree("while (");  test.printTree();  Log.wTreeLn(") {");
        Log.indentTree();  body.printTree();  Log.outdentTree();
        Log.wTreeLn("}");
    }
}


/*
 * An <Lhs-variable>
 */

class LhsVariable extends SyntaxUnit {
    int numStars = 0;
    Variable var;
    Type type;

    @Override void check(DeclList curDecls) {
        var.check(curDecls);
        type = var.type;
        for (int i = 1;  i <= numStars;  ++i) {
            Type e = type.getElemType();
            if (e == null) 
                error("Type error in left-hand side variable!");
            type = e;
        }
    }

    @Override void genCode(FuncDecl curFunc) {
        var.genAddressCode(curFunc);
        for (int i = 1;  i <= numStars;  ++i)
            Code.genInstr("", "movl", "(%eax),%eax", "  *");
    }

    static LhsVariable parse() {
    Log.enterParser("<lhs-variable>");

    LhsVariable lhs = new LhsVariable();
    while (Scanner.curToken == starToken) {
            ++lhs.numStars;  Scanner.skip(starToken);
        }
        Scanner.check(nameToken);
        lhs.var = Variable.parse();

        Log.leaveParser("</lhs-variable>");
        return lhs;
    }

    @Override void printTree() {
    for (int i = 1;  i <= numStars;  ++i) Log.wTree("*");
        var.printTree();
    }
}


/*
 * An <expression list>.
 */

class ExprList extends SyntaxUnit {
    Expression firstExpr = null;

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
        Expression e = firstExpr;
        while(e != null) {
            e.check(curDecls);
            e = e.nextExpr;
        }
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        Expression e = firstExpr;
        while(e != null) {
            e.genCode(curFunc);
            e = e.nextExpr;
        }
    }

    static ExprList parse() {
        Expression lastExpr = null;

        Log.enterParser("<expr list>");
        ExprList el = new ExprList();

        //Empty expression list
        if (Scanner.curToken == rightParToken) {
            Log.leaveParser("</expr list>");
            return el;
        }


        el.firstExpr = lastExpr = Expression.parse();
        
        while (Scanner.curToken == commaToken) {
            Scanner.skip(commaToken);
            lastExpr.nextExpr = Expression.parse();
            lastExpr = lastExpr.nextExpr;
        }

        Log.leaveParser("</expr list>");
        //-- Must be changed in part 1:
        return el;
    }

    int size() {
        int size = 0;
        Expression tempExpr = firstExpr;
        while (tempExpr != null) {
            size++;
            tempExpr = tempExpr.nextExpr;
        }
        return size;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        Expression e = firstExpr;
        while(e != null) {
            e.printTree(); e = e.nextExpr;
            if(e != null) Log.wTree(", ");
        }
        //Slutt endring
    }
    //-- Must be changed in part 1:
}


/*
 * An <expression>
 */
class Expression extends SyntaxUnit {
    Expression nextExpr = null;
    Term firstTerm, secondTerm = null;
    Operator relOpr = null;
    Type type = null;
    
    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
        firstTerm.check(curDecls);
        if (relOpr != null) {
            relOpr.check(curDecls);
            secondTerm.check(curDecls);
        }
        type = Types.intType;
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        firstTerm.genCode(curFunc);
        if(relOpr != null) {
            Code.genInstr("", "pushl", "%eax", "");
            secondTerm.genCode(curFunc);
            relOpr.genCode(curFunc);
        }
    }

    static Expression parse() {
        Log.enterParser("<expression>");

        Expression e = new Expression();
        e.firstTerm = Term.parse();
        if (Token.isRelOperator(Scanner.curToken)) {
            e.relOpr = RelOpr.parse();
            e.secondTerm = Term.parse();
        }
        
        Log.leaveParser("</expression>");
        return e;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        firstTerm.printTree();
        if(relOpr != null) {
            relOpr.printTree();
            secondTerm.printTree();
        }
        //Slutt endring
    }
    
    //Unoedvendig? firstterm og secondterm skal ikke innverteres paa stacken
    void putOnStack(FuncDecl curFunc, int i) {
        i++;
        if(nextExpr != null) nextExpr.putOnStack(curFunc, i);
        genCode(curFunc);
        Code.genInstr("", "pushl", "%eax", "Push parameter #"+i);
    }
}


/*
 * A <term>
 */
class Term extends SyntaxUnit {
    //-- Must be changed in part 1+2:
    Factor first, lastFactor;
    TermOpr opr, lastOpr;
    //Term next;

    @Override void check(DeclList curDecls) {
  //-- Must be changed in part 2:
        Factor tempFactor = first;
        while (tempFactor != null) {
            tempFactor.check(curDecls);
            tempFactor = tempFactor.nextFactor;
        }
    }

    @Override void genCode(FuncDecl curFunc) {
  //-- Must be changed in part 2:
        first.genCode(curFunc);
        TermOpr tempOpr = opr;
        Factor tempFactor = first.nextFactor;
        while (tempOpr != null) {
            Code.genInstr("", "pushl", "%eax", "");
            tempFactor.genCode(curFunc);
            tempOpr.genCode(curFunc);

            tempOpr = tempOpr.nextTermOpr;
            tempFactor = tempFactor.nextFactor;
        }
    }

    static Term parse() {
        //-- Must be changed in part 1:

        Log.enterParser("<term>");
        Term t = new Term();
        t.first = t.lastFactor = Factor.parse();

        while (Token.isTermOperator(Scanner.curToken)) {
            if (t.opr == null) {
                t.opr = t.lastOpr = TermOpr.parse();
            } else {
                t.lastOpr.nextTermOpr = TermOpr.parse();
                t.lastOpr = t.lastOpr.nextTermOpr;
            }
            t.lastFactor.nextFactor = Factor.parse();
            t.lastFactor = t.lastFactor.nextFactor;
        }
        
        Log.leaveParser("</term>");
        return t;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        //Start endring
        first.printTree();
        TermOpr tempOpr = opr;
        Factor tempFact = first.nextFactor;
        while(tempOpr != null) {
            tempOpr.printTree();
            tempFact.printTree();
            tempOpr = tempOpr.nextTermOpr;
            tempFact = tempFact.nextFactor;
        }
        //Slutt endring
    }
}

/*
 * A <termopr>
 */
class TermOpr extends Operator {
    TermOpr nextTermOpr;

    @Override void check(DeclList curDecls) {

    }

    @Override void genCode(FuncDecl curFunc) {
        Code.genInstr("", "movl", "%eax,%ecx", "");
        Code.genInstr("", "popl", "%eax", "");
        switch (oprToken) {
            case addToken:
            Code.genInstr("", "addl", "%ecx,%eax", "Compute +");
            break;
            case subtractToken:
            Code.genInstr("", "subl", "%ecx,%eax", "Compute -");
            break;
        }
    }

    static TermOpr parse() {
        Log.enterParser("<term opr>");
        TermOpr to = new TermOpr();

        if (Scanner.curToken == addToken || Scanner.curToken == subtractToken) {
            to.oprToken = Scanner.curToken;
            Scanner.skip(addToken, subtractToken); 
        }

        Log.leaveParser("</term opr>");
        return to;
    }

    @Override void printTree() {
        if(oprToken == addToken) {
            Log.wTree(" + ");
        } else if(oprToken == subtractToken) {
            Log.wTree(" - ");
        }

    }
}

/*
 * A <factor>
 */
class Factor extends SyntaxUnit {
    Factor nextFactor;
    Primary first, lastPrimary;
    FactorOpr opr, lastOpr;

    @Override void check(DeclList curDecls) {
        Primary tempPrimary = first;
        while (tempPrimary != null) {
            tempPrimary.check(curDecls);
            tempPrimary = tempPrimary.nextPrimary;
        }
    }

    @Override void genCode(FuncDecl curFunc) {
        first.genCode(curFunc);
        FactorOpr tempOpr = opr;
        Primary tempPrimary = first.nextPrimary;
        while (tempOpr != null) {
            Code.genInstr("", "pushl", "%eax", "");
            tempPrimary.genCode(curFunc);
            tempOpr.genCode(curFunc);

            tempPrimary = tempPrimary.nextPrimary;
            tempOpr = tempOpr.nextOpr;
        }
    }
    
    static Factor parse() {
        Log.enterParser("<factor>");
        Factor f = new Factor();
        f.first = f.lastPrimary = Primary.parse();
        
        while(Token.isFactorOperator(Scanner.curToken)) {
            if(f.opr == null) {
                f.opr = f.lastOpr = FactorOpr.parse();
            } else {
                f.lastOpr.nextOpr = FactorOpr.parse();
                f.lastOpr = f.lastOpr.nextOpr;
            }
            f.lastPrimary.nextPrimary = Primary.parse();
            f.lastPrimary = f.lastPrimary.nextPrimary;
        }

        Log.leaveParser("</factor>");
        return f;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        first.printTree();
        FactorOpr tempOpr = opr;
        Primary tempPrimary = first.nextPrimary;
        while(tempOpr != null) {
            tempOpr.printTree();
            tempPrimary.printTree();
            tempOpr = tempOpr.nextOpr;
            tempPrimary = tempPrimary.nextPrimary;
        }
        //Slutt endring
    }
}

class FactorOpr extends Operator {
    FactorOpr nextOpr;

    @Override void check(DeclList curDecls) {

    }

    @Override void genCode(FuncDecl curFunc) {
        Code.genInstr("", "movl", "%eax,%ecx", "");
        Code.genInstr("", "popl", "%eax", "");
        switch(oprToken) {
            case divideToken:
            Code.genInstr("", "cdq", "", "");
            Code.genInstr("", "idivl", "%ecx", "Compute /");
            break;
            case starToken:
            Code.genInstr("", "imull", "%ecx,%eax", "Compute *");
            break;
        }
    }

    static FactorOpr parse() {
        Log.enterParser("<factor opr>");
        FactorOpr fo = new FactorOpr();

        if (Scanner.curToken == starToken || Scanner.curToken == divideToken) {
            fo.oprToken = Scanner.curToken;
            Scanner.skip(starToken, divideToken); 
        }

        Log.leaveParser("</factor opr>");
        return fo;
    }

    @Override void printTree() {
        if(oprToken == starToken) {
            Log.wTree(" * ");
        } else if(oprToken == divideToken) {
            Log.wTree(" / ");
        }

    }
}



class Primary extends SyntaxUnit {
    Operand op;
    Primary nextPrimary;
    PrefixOpr prefixOpr = null;

    @Override void check(DeclList curDecls) {
        if(prefixOpr != null) prefixOpr.check(curDecls);
        op.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
        op.genCode(curFunc);
        if (prefixOpr != null) {
            prefixOpr.genCode(curFunc);
        }
    }

    static Primary parse() {
        Log.enterParser("<primary>");
        Primary p = new Primary();

        if (Token.isPrefixOperator(Scanner.curToken)) {
            p.prefixOpr = PrefixOpr.parse();
        }
        
        p.op = Operand.parse();

        Log.leaveParser("</primary>");
        return p;
  }

    @Override void printTree() {
        //Start endring
        if(prefixOpr != null) {
            //Log.wTree(prefixOpr);
            prefixOpr.printTree();
        }
        op.printTree();
        //Slutt endring    
    }
}


/*
 * A <prefix opr>
 */
class PrefixOpr extends Operator {
    @Override void check(DeclList curDecls) {

    }

    @Override void genCode(FuncDecl curFunc) {
        switch(oprToken) {
            case subtractToken:
            Code.genInstr("", "negl", "%eax", "Compute prefix -");
            break;
            case starToken:
            Code.genInstr("", "movl", "(%eax),%eax", "Compute prefix *");
            break;
        }
    }

    static PrefixOpr parse() {
        Log.enterParser("<prefix opr>");
        PrefixOpr po = new PrefixOpr();

        po.oprToken = Scanner.curToken;
        Scanner.skip(starToken, subtractToken); 

        Log.leaveParser("</prefix opr>");
        return po;
    }

    @Override void printTree() {
        if(oprToken == starToken) {
            Log.wTree(" *");
        } else if(oprToken == subtractToken) {
            Log.wTree(" -");
        }

    }
}


/*
 * An <operator>
 */
abstract class Operator extends SyntaxUnit {
    Operator nextOpr = null;
    Token oprToken;

    @Override void check(DeclList curDecls) {}  // Never needed.
}


/*
 * A <rel opr> (==, !=, <, <=, > or >=).
 */

class RelOpr extends Operator {
    @Override void genCode(FuncDecl curFunc) {
        Code.genInstr("", "popl", "%ecx", "");
        Code.genInstr("", "cmpl", "%eax,%ecx", "");
        Code.genInstr("", "movl", "$0,%eax", "");
        switch (oprToken) {
        case equalToken:        
            Code.genInstr("", "sete", "%al", "Test ==");  break;
        case notEqualToken:
            Code.genInstr("", "setne", "%al", "Test !=");  break;
        case lessToken:
            Code.genInstr("", "setl", "%al", "Test <");  break;
        case lessEqualToken:
            Code.genInstr("", "setle", "%al", "Test <=");  break;
        case greaterToken:
            Code.genInstr("", "setg", "%al", "Test >");  break;
        case greaterEqualToken:
            Code.genInstr("", "setge", "%al", "Test >=");  break;
        }
    }

    static RelOpr parse() {
        Log.enterParser("<rel opr>");

        RelOpr ro = new RelOpr();
        ro.oprToken = Scanner.curToken;
        Scanner.readNext();

        Log.leaveParser("</rel opr>");
        return ro;
    }

    @Override void printTree() {
        String op = "?";
        switch (oprToken) {
        case equalToken:        op = "==";  break;
        case notEqualToken:     op = "!=";  break;
        case lessToken:         op = "<";   break;
        case lessEqualToken:    op = "<=";  break;
        case greaterToken:      op = ">";   break;
        case greaterEqualToken: op = ">=";  break;
        }
        Log.wTree(" " + op + " ");
    }
}


/*
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {
    Operand nextOperand = null;
    Type type;

    static Operand parse() {
        Log.enterParser("<operand>");
        Operand o = null;
        if (Scanner.curToken == numberToken) {
            o = Number.parse();
        } else if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) {
            o = FunctionCall.parse();
        } else if (Scanner.curToken == nameToken) {
            o = Variable.parse();
        } else if (Scanner.curToken == ampToken) {
            o = Address.parse();
        } else if (Scanner.curToken == leftParToken) {
            o = InnerExpr.parse();  
        } else {
            Error.expected("An operand");
        }

        Log.leaveParser("</operand>");
        return o;
    }
}


/*
 * A <function call>.
 */
class FunctionCall extends Operand {
    ExprList expressions;   // lagt til 30.10 - Christian
    String name;            // Lagt til 26.11 - Christian
    FuncDecl declRef;
    //-- Must be changed in part 1+2:

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
        Declaration d = curDecls.findDecl(name,this); 
        d.checkWhetherFunction(expressions.size(), this);
        declRef = (FuncDecl)d;
        type = declRef.type;

        expressions.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
        if(expressions.firstExpr != null) { 
            expressions.firstExpr.putOnStack(curFunc, 0);
        }
        Code.genInstr("", "call", name, "Call "+name);
        if(expressions.firstExpr != null)   
        Code.genInstr("", "addl", "$"+expressions.size()*4+",%esp","Remove parameters");
    }

    static FunctionCall parse() {
    //-- Must be changed in part 1:
        Log.enterParser("<function call>");
        FunctionCall fc = new FunctionCall();
        fc.name = Scanner.curName;
        Scanner.skip(nameToken);
        Scanner.skip(leftParToken); // Skip the function name and the initial leftParToken
        fc.expressions = ExprList.parse();
        Scanner.skip(rightParToken);
        Log.leaveParser("</function call>");
        return fc;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        Log.wTree(name+"(");
        expressions.printTree();
        Log.wTree(")");
    }
    //-- Must be changed in part 1+2:
}


/*
 * A <number>.
 */
class Number extends Operand {
    int numVal;

    @Override void check(DeclList curDecls) {
        //-- Must be changed in part 2:
    }
    
    @Override void genCode(FuncDecl curFunc) {
        Code.genInstr("", "movl", "$"+numVal+",%eax", ""+numVal); 
    }

    static Number parse() {
    //-- Must be changed in part 1:
        Log.enterParser("<number>");
        Number n = new Number();
       
        n.numVal = Scanner.curNum;
        Scanner.skip(numberToken);
       
        Log.leaveParser("</number>");
        return n;
    }

    @Override void printTree() {
        Log.wTree("" + numVal);
    }
}


/*
 * A <variable>.
 */

class Variable extends Operand {
    String varName;
    VarDecl declRef = null;
    Expression index = null;

    @Override void check(DeclList curDecls) {
        Declaration d = curDecls.findDecl(varName,this);
        d.checkWhetherVariable(this); 
        declRef = (VarDecl)d;

        if (index == null) {
            type = d.type;
        } else {
            index.check(curDecls);
            Log.noteTypeCheck("a[e]", d.type, "a", index.type, "e", lineNum);

            if (index.type == Types.intType) {
                // OK
            } else {
                error("Only integers may be used as index.");
            }
            if (d.type.mayBeIndexed()) {
                // OK
            } else {
                error("Only arrays and pointers may be indexed.");
            }
            type = d.type.getElemType();
        }
    }

    @Override void genCode(FuncDecl curFunc) {
        //-- Must be changed in part 2:
        if(index == null) {
            if(declRef.type instanceof ArrayType) {
                Code.genInstr("", "leal", declRef.assemblerName+",%eax", varName+"");
            } else {
                Code.genInstr("", "movl", declRef.assemblerName+",%eax", varName+"");
            }
        } else { 
            index.genCode(curFunc);
            if(declRef.type instanceof ArrayType) {
                Code.genInstr("", "leal", declRef.assemblerName+",%edx", varName+"[...]");
            } else {
                Code.genInstr("", "movl", declRef.assemblerName+",%edx", varName+"[...]");
            }
            Code.genInstr("", "movl", "(%edx,%eax,4),%eax", "");
        }
    }

    void genAddressCode(FuncDecl curFunc) {
    // Generate code to load the _address_ of the variable
    // rather than its value.

        if (index == null) {
            Code.genInstr("", "leal", declRef.assemblerName+",%eax", varName);
        } else {
            index.genCode(curFunc);
            if (declRef.type instanceof ArrayType) {
                Code.genInstr("", "leal", declRef.assemblerName+",%edx", 
                              varName+"[...]");
            } else {
                Code.genInstr("", "movl", declRef.assemblerName+",%edx", 
                              varName+"[...]");
            }
            Code.genInstr("", "leal", "(%edx,%eax,4),%eax", "");
        }
    }

    static Variable parse() {
        //-- Must be changed in part 1:
        // Har endret pa
        Log.enterParser("<variable>");
        Variable v = new Variable();
        if(Scanner.curToken == nameToken) {
            v.varName = Scanner.curName;
            Scanner.skip(nameToken);
        } 
        if(Scanner.curToken == leftBracketToken) {
            Scanner.skip(leftBracketToken);
            v.index = Expression.parse();
            Scanner.skip(rightBracketToken);
        }
        Log.leaveParser("</variable>");
        return v;
    }

    @Override void printTree() {
        //-- Must be changed in part 1:
        //Start endring
        Log.wTree(varName);
        if(index != null) {
            Log.wTree("[");
            index.printTree();
            Log.wTree("]");
        }
        //Slutt endring
    }
}


/*
 * An <address>.
 */
class Address extends Operand {
    Variable var;

    @Override void check(DeclList curDecls) {
        var.check(curDecls);
        type = new PointerType(var.type);
    }

    @Override void genCode(FuncDecl curFunc) {
        var.genAddressCode(curFunc);
    }

    static Address parse() {
        Log.enterParser("<address>");

        Address a = new Address();
        Scanner.skip(ampToken);
        a.var = Variable.parse();

        Log.leaveParser("</address>");
        return a;
    }

    @Override void printTree() {
        Log.wTree("&");  var.printTree();
    }
}


/*
 * An <inner expr>.
 */
class InnerExpr extends Operand {
    Expression expr;

    @Override void check(DeclList curDecls) {
        expr.check(curDecls);
        type = expr.type;
    }

    @Override void genCode(FuncDecl curFunc) {
        expr.genCode(curFunc);
    }

    static InnerExpr parse() {
        Log.enterParser("<inner expr>");

        InnerExpr ie = new InnerExpr();
        Scanner.skip(leftParToken);
        ie.expr = Expression.parse();
        Scanner.skip(rightParToken);
        Log.leaveParser("</inner expr>");
        return ie;
    }

    @Override void printTree() {
        Log.wTree("(");  expr.printTree();  Log.wTree(")");
    }
}
