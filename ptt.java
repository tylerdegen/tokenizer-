import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ptt {

	public static Tokenizer t;

    public static void main(String[] args) {
        System.out.println("test");
        Scanner in;
        //ideally export this and make it public static
        try {
            in = new Scanner(Paths.get(args[0]));
            t = new Tokenizer(in);
        } catch (IOException e) {
            System.err.println("Error opening file: " + args[0]);
            return;
        }
        
        constructPT();
    }


    //parse program
    public static ParseTree constructPT() {
        //start with creating the program
        ParseTree p = new ParseTree("program");
        if (t.getToken() != TokenKind.PROGRAM) {
            System.out.println("Input does not begin with program.");
        }
        t.skipToken();
        //then begin statement
        p.addChild(generateDeclSeq());
        if (t.getToken() != TokenKind.BEGIN) {
            System.out.println("Should have begin");
        }
        t.skipToken();
        p.addChild(generateStmtSeq());
        if (t.getToken() != TokenKind.END) {
            System.out.print("should have end");
        }
        t.skipToken();
        if (t.getToken() != TokenKind.EOF) {
            System.out.print("File should end with end!");
        }

        return p;

    }

    public static ParseTree generateDeclSeq() {
        ParseTree p = new ParseTree("declseq");
        if (t.getToken() != TokenKind.INT) {
            System.out.println("Should have an int here");
        }
        p.addChild(generateDecl());
        while (t.getToken() != TokenKind.BEGIN) {
            p.addChild(generateDeclSeq());
        }
        return p;
    }

    private static ParseTree generateDecl() {
        ParseTree p = new ParseTree("decl");
        if (t.getToken() != TokenKind.INT) {
            System.out.println("generateDecl should have int");
        }
        t.skipToken();
        p.addChild(generateIdList());
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("should finish with a semicolon");
        }
        t.skipToken();
        return p;
    }

    private static ParseTree generateIdList() {
        ParseTree p = new ParseTree("idlist");
        if (t.getToken() != TokenKind.IDENTIFIER) {
            System.out.println("Wanted an ID");
        }
        ParseTree p2 = new ParseTree("id");
        //todo -- find out how to obtain value
        p2.setVal("ABC");
        p.addChild(p2);
        t.skipToken();
        if (t.getToken() == TokenKind.COMMA) {
            t.skipToken();
            p.addChild(generateIdList());
        }
        return p;
    }

    private static ParseTree generateStmtSeq() {
        ParseTree p = new ParseTree("stmtseq");
        TokenKind tk = t.getToken();
        switch (tk) {
            case IDENTIFIER:
                p.addChild(generateAssign());
                break;
            case IF:
                p.addChild(generateIf());
                break;
            case READ:
                p.addChild(generateIn());
                break;
            case WRITE:
                p.addChild(generateOut());
                break;
            default:
                System.out.println("DIdn't find case for generateStmtSeq");
                break;
        }
        switch (tk) {
            case IDENTIFIER:
            case READ:
            case WRITE:
                p.addChild(generateStmtSeq());
                break;
            default:
                System.out.println("no more");
        }
        return p;
    }

    public static ParseTree generateOut() {
        ParseTree p = new ParseTree("out");
        if (t.getToken() != TokenKind.WRITE) {
            System.out.println("should be write");
        }
        t.skipToken();
        p.addChild(generateIdList());
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("was wanting a semicolon");
        }
        t.skipToken();
        return p;
    }
    public static ParseTree generateIn() {
        ParseTree p = new ParseTree("in");
        if (t.getToken() != TokenKind.READ) {
            System.out.println("wanted read");
        }
        t.skipToken();
        p.addChild(generateIdList());
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("wanting semicolon!");
        }
        t.skipToken();
        return p;
    }

    //Putting in stub classes, filling in later
    public static ParseTree generateWhile() {
        ParseTree p = new ParseTree("loop");
        if (t.getToken() != TokenKind.WHILE) {
            System.out.println("wanting while");
        }
        t.skipToken();
        p.addChild(generateCond());
        if (t.getToken() != TokenKind.LOOP){
        	System.out.print("wanting loop");
        }
        t.skipToken();
        p.addChild(generateStmtSeq());
        if (t.getToken() != TokenKind.END){
        	System.out.println("end wanted");
        }
        t.skipToken();
        if (t.getToken() != TokenKind.SEMICOLON){
        	System.out.println("Expected semicolon");
        }
        t.skipToken();
        return p;
    }

    public static ParseTree generateIf() {
        ParseTree p = new ParseTree("if");
        if (t.getToken() != TokenKind.IF) {
            System.out.println("wanting if token");
        }
        t.skipToken();
        p.addChild(generateCond());
        if (t.getToken() != TokenKind.THEN){
        	System.out.println("wanting then");
        }
        t.skipToken();
        p.addChild(generateStmtSeq());
        if (t.getToken() == TokenKind.ELSE){
        	t.skipToken();
        	p.addChild(generateStmtSeq());
        }
        if (t.getToken() != TokenKind.END){
        	System.out.println("Expecting end");
        }
        t.skipToken();
        if (t.getToken() != TokenKind.SEMICOLON){
        	System.out.println("Expecting semicolon");
        }
        t.skipToken();
        return p;
    }

    //Don't have an explicit condition, this is an amalgamation of not and or 
    
    public static ParseTree generateCond() {
        ParseTree p = new ParseTree("cond");
        if (t.getToken() == TokenKind.EXCLAMATION){
        	ParseTree n = new ParseTree("not");
        	p.addChild(n);
        	t.skipToken();
        	p.addChild(generateCond());
        }
        else if (t.getToken() == TokenKind.LEFT_PAR){
        	t.skipToken();
        	p.addChild(generateCond());
        	if (t.getToken() == TokenKind.AND_OPERATOR){
        		ParseTree a = new ParseTree("and");
        		p.addChild(a);
        	}
        	else if (t.getToken() == TokenKind.OR_OPERATOR){
        		ParseTree o = new ParseTree("or");
        		p.addChild(o);
        	} else{
        		System.out.println("wanting and or or");
        	}
        	t.skipToken();
        	p.addChild(generateCond());
        	if (t.getToken() != TokenKind.RIGHT_PAR){
        		System.out.println("wanting right paren");
        	}
        	t.skipToken();
        }
        else{
        	p.addChild(generateCmpr());
        }
        return p;
    }

    public static ParseTree generateCmpr() {
        ParseTree p = new ParseTree("cmpr");
        //check against all comparators?
        if (t.getToken() != TokenKind.LEFT_BRACKET) {
            System.out.println("wanting left bracket");
        }
        t.skipToken();
        p.addChild(generateExpr());
        TokenKind tk = t.getToken();
        switch (tk) {
            case LESS:
            case GREATER:
            case LESS_EQUAL:
            case GREATER_EQUAL:
            case EQUALITY_TEST:
            case NOT_EQUAL:
            	ParseTree boolp = new ParseTree("bool");
            	//how give value to boolP?
                p.addChild(boolp);
                break;
            default:
                System.out.println("Expecting a comparison operator!");
        }
        t.skipToken();
        p.addChild(generateExpr());
        if (t.getToken() != TokenKind.RIGHT_BRACKET){
        	System.out.println("wanting right bracket!");
        }
        t.skipToken();
        
        return p;
    }

    public static ParseTree generateAssign() {
        ParseTree p = new ParseTree("assign");
        if (t.getToken() != TokenKind.IDENTIFIER) {
            System.out.println("identifier wanted");
        }
        ParseTree id = new ParseTree("id");
        //how give value to id?
        p.addChild(id);
        t.skipToken();
        if (t.getToken() != TokenKind.ASSIGNMENT_OPERATOR){
        	System.out.println("assignment operator wanted");
        }
        t.skipToken();
        p.addChild(generateExpr());
        if (t.getToken() != TokenKind.SEMICOLON){
        	System.out.println("semicolon wanted");
        }
        t.skipToken();
        return p;
    }

    public static ParseTree generateExpr() {
        ParseTree p = new ParseTree("expr");
        //check against plus, minus 
        p.addChild(generateTrm());
        if (t.getToken() == TokenKind.PLUS){
        	ParseTree plus = new ParseTree("plus");
        	p.addChild(plus);
        }else if (t.getToken() == TokenKind.MINUS){
        	ParseTree minus = new ParseTree("minus");
        	p.addChild(minus);
        }
        else{
        	System.out.println("Expecting plus or minus");
        }
        t.skipToken();
        p.addChild(generateExpr());	
        return p;
    }

    public static ParseTree generateTrm() {
        ParseTree p = new ParseTree("trm");
        //for strictly multiplication
        p.addChild(generateOp());
        if (t.getToken() != TokenKind.MULT) {
            System.out.println("Expecting multiplication!");
        } else{
        	p.addChild(new ParseTree("times"));
        	t.skipToken();
        	p.addChild(generateTrm());
        }
        return p;
    }

    //parens
    public static ParseTree generateOp() {
        ParseTree p = new ParseTree("op");
        TokenKind tk = t.getToken();
        
        switch (tk) {
            case INTEGER_CONSTANT:
            	ParseTree i = new ParseTree("integer_constant");
                p.addChild(i);
                t.skipToken();
                break;
            case IDENTIFIER:
                ParseTree id = new ParseTree("identifier");
        	p.addChild(id);
        	t.skipToken();
                break;
            case MINUS:
            	ParseTree min = new ParseTree("min");
            	t.skipToken();
            	p.addChild(min);
            	p.addChild(generateOp());
            	break;
            case LEFT_PAR:
            	t.skipToken();
            	p.addChild(generateExpr());
            	if (t.getToken() != TokenKind.RIGHT_PAR){
            		System.out.println("Right parenthesis wanted");
            	}
            	t.skipToken();
            	break;
            default:
            	System.out.println("Expecting a factor");
            	break;
        }
        
        return p;
    }
}
