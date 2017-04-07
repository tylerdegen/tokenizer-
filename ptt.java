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
        p.addChild(parseDeclSeq());
        if (t.getToken() != TokenKind.BEGIN) {
            System.out.println("Should have begin");
        }
        t.skipToken();
        p.addChild(parseStmtSeq());
        if (t.getToken() != TokenKind.END) {
            System.out.print("should have end");
        }
        t.skipToken();
        if (t.getToken() != TokenKind.EOF) {
            System.out.print("File should end with end!");
        }

        return p;

    }

    public static ParseTree parseDeclSeq() {
        ParseTree p = new ParseTree("declseq");
        if (t.getToken() != TokenKind.INT) {
            System.out.println("Should have an int here");
        }
        p.addChild(parseDecl());
        while (t.getToken() != TokenKind.BEGIN) {
            p.addChild(parseDeclSeq());
        }
        return p;
    }

    private static ParseTree parseDecl() {
        ParseTree p = new ParseTree("decl");
        if (t.getToken() != TokenKind.INT) {
            System.out.println("parseDecl should have int");
        }
        t.skipToken();
        p.addChild(parseIdList());
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("should finish with a semicolon");
        }
        t.skipToken();
        return p;
    }

    private static ParseTree parseIdList() {
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
            p.addChild(parseIdList());
        }
        return p;
    }

    private static ParseTree parseStmtSeq() {
        ParseTree p = new ParseTree("stmtseq");
        TokenKind tk = t.getToken();
        switch (tk) {
            case IDENTIFIER:
                p.addChild(parseAssign());
                break;
            case IF:
                p.addChild(parseIf());
                break;
                /*
            case DO:
                p.addChild(parseWhile());
                break;
                */
            case READ:
                p.addChild(parseIn());
                break;
            case WRITE:
                p.addChild(parseOut());
                break;
            default:
                System.out.println("DIdn't find case for parseStmtSeq");
                break;
        }
        //CHECK THIS, should it skip a token here
        switch (tk) {
            case IDENTIFIER:
            //case TokenKind.DO:
            case READ:
            case WRITE:
                p.addChild(parseStmtSeq());
                break;
            default:
                System.out.println("no more");
        }

        //no case system
        return p;
    }


    //how handle input and output? ah read and write tokens, come back to these
    public static ParseTree parseOut() {
        ParseTree p = new ParseTree("out");
        if (t.getToken() != TokenKind.WRITE) {
            System.out.println("should be write");
        }
        t.skipToken();
        p.addChild(parseIdList());
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("was expecting a semicolon");
        }
        t.skipToken();
        return p;
    }
    public static ParseTree parseIn() {
        ParseTree p = new ParseTree("in");
        if (t.getToken() != TokenKind.READ) {
            System.out.println("expected read");
        }
        t.skipToken();
        p.addChild(parseIdList());
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("expecting semicolon!");
        }
        t.skipToken();
        return p;
    }

    //Putting in stub classes, filling in later
    public static ParseTree parseWhile() {
        ParseTree p = new ParseTree("loop");
        if (t.getToken() != TokenKind.WHILE) {
            System.out.println("expecting while");
        }
        t.skipToken();
        p.addChild(parseCond());
        if (t.getToken() != TokenKind.LOOP){
        	System.out.print("expecting loop");
        }
        t.skipToken();
        p.addChild(parseStmtSeq());
        if (t.getToken() != TokenKind.END){
        	System.out.println("end expected");
        }
        t.skipToken();
        if (t.getToken() != TokenKind.SEMICOLON){
        	System.out.println("Expected semicolon");
        }
        t.skipToken();
        return p;
    }

    public static ParseTree parseIf() {
        ParseTree p = new ParseTree("if");
        if (t.getToken() != TokenKind.IF) {
            System.out.println("expecting if token");
        }
        t.skipToken();
        p.addChild(parseCond());
        if (t.getToken() != TokenKind.THEN){
        	System.out.println("expecting then");
        }
        t.skipToken();
        p.addChild(parseStmtSeq());
        if (t.getToken() == TokenKind.ELSE){
        	t.skipToken();
        	p.addChild(parseStmtSeq());
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
    
    public static ParseTree parseCond() {
        ParseTree p = new ParseTree("cond");
        if (t.getToken() == TokenKind.EXCLAMATION){
        	ParseTree n = new ParseTree("not");
        	p.addChild(n);
        	t.skipToken();
        	p.addChild(parseCond());
        }
        else if (t.getToken() == TokenKind.LEFT_PAR){
        	t.skipToken();
        	p.addChild(parseCond());
        	if (t.getToken() == TokenKind.AND_OPERATOR){
        		ParseTree a = new ParseTree("and");
        		p.addChild(a);
        	}
        	else if (t.getToken() == TokenKind.OR_OPERATOR){
        		ParseTree o = new ParseTree("or");
        		p.addChild(o);
        	} else{
        		System.out.println("expecting and or or");
        	}
        	t.skipToken();
        	p.addChild(parseCond());
        	if (t.getToken() != TokenKind.RIGHT_PAR){
        		System.out.println("expecting right paren");
        	}
        	t.skipToken();
        }
        else{
        	p.addChild(parseCmpr());
        }
        return p;
    }

    public static ParseTree parseCmpr() {
        ParseTree p = new ParseTree("cmpr");
        //check against all comparators?
        if (t.getToken() != TokenKind.LEFT_BRACKET) {
            System.out.println("expecting left bracket");
        }
        t.skipToken();
        p.addChild(parseExpr());
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
        p.addChild(parseExpr());
        if (t.getToken() != TokenKind.RIGHT_BRACKET){
        	System.out.println("expecting right bracket!");
        }
        t.skipToken();
        
        return p;
    }

    public static ParseTree parseAssign() {
        ParseTree p = new ParseTree("assign");
        if (t.getToken() != TokenKind.IDENTIFIER) {
            System.out.println("identifier expected");
        }
        ParseTree id = new ParseTree("id");
        //how give value to id?
        p.addChild(id);
        t.skipToken();
        if (t.getToken() != TokenKind.ASSIGNMENT_OPERATOR){
        	System.out.println("assignment operator expected");
        }
        t.skipToken();
        p.addChild(parseExpr());
        if (t.getToken() != TokenKind.SEMICOLON){
        	System.out.println("semicolon expected");
        }
        t.skipToken();
        return p;
    }

    public static ParseTree parseExpr() {
        ParseTree p = new ParseTree("expr");
        //check against plus, minus 
        p.addChild(parseTrm());
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
        p.addChild(parseExpr());	
        return p;
    }

    public static ParseTree parseTrm() {
        ParseTree p = new ParseTree("trm");
        //for strictly multiplication
        p.addChild(parseOp());
        if (t.getToken() != TokenKind.MULT) {
            System.out.println("Expecting multiplication!");
        } else{
        	p.addChild(new ParseTree("times"));
        	t.skipToken();
        	p.addChild(parseTrm());
        }
        return p;
    }

    //parens
    public static ParseTree parseOp() {
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
            	p.addChild(parseOp());
            	break;
            case LEFT_PAR:
            	t.skipToken();
            	p.addChild(parseExpr());
            	if (t.getToken() != TokenKind.RIGHT_PAR){
            		System.out.println("Right parenthesis expected");
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
