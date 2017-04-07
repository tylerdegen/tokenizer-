import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class ptt {
    public static void main(String[] args) {
        System.out.println("test");
        Scanner in;
        //ideally export this and make it public static
        try {
            in = new Scanner(Paths.get(args[0]));
        } catch (IOException e) {
            System.err.println("Error opening file: " + args[0]);
            return;
        }
        Tokenizer t = new Tokenizer(in);
        
        constructPT(t);
    }


    //parse program
    public static ParseTree constructPT(Tokenizer t) {
        //start with creating the program
        ParseTree p = new ParseTree("program");
        if (t.getToken() != TokenKind.PROGRAM) {
            System.out.println("Input does not begin with program.");
        }
        t.skipToken();
        //then begin statement
        p.append(parseDeclSeq(t));
        if (t.getToken() != TokenKind.BEGIN) {
            System.out.println("Should have begin");
        }
        t.skipToken();
        p.append(parseStmtSeq(t));
        if (t.getToken() != TokenKind.END) {
            System.out.print("should have end");
        }
        t.skipToken();
        if (t.getToken() != TokenKind.EOF) {
            System.out.print("File should end with end!");
        }

        return p;

    }

    public static ParseTree parseDeclSeq(Tokenizer t) {
        ParseTree p = new ParseTree("declseq");
        if (t.getToken() != TokenKind.INT) {
            System.out.println("Should have an int here");
        }
        p.append(parseDecl(t));
        while (t.getToken() != TokenKind.BEGIN) {
            p.append(parseDeclSeq(t));
        }
        return p;
    }

    private static ParseTree parseDecl(Tokenizer t) {
        ParseTree p = new ParseTree("decl");
        if (t.getToken() != TokenKind.INT) {
            System.out.println("parseDecl should have int");
        }
        t.skipToken();
        p.append(parseIdList(t));
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("should finish with a semicolon");
        }
        t.skipToken();
        return p;
    }

    private static ParseTree parseIdList(Tokenizer t) {
        ParseTree p = new ParseTree("idlist");
        if (t.getToken() != TokenKind.IDENTIFIER) {
            System.out.println("Wanted an ID");
        }
        ParseTree p2 = new ParseTree("id");
        //todo -- find out how to obtain value
        p2.setVal("ABC");
        p.append(p2);
        t.skipToken();
        if (t.getToken() == TokenKind.COMMA) {
            t.skipToken();
            p.append(parseIdList(t));
        }
        return p;
    }

    private static ParseTree parseStmtSeq(Tokenizer t) {
        ParseTree p = new ParseTree("stmtseq");
        TokenKind tk = t.getToken();
        switch (tk) {
            case IDENTIFIER:
                p.append(parseAssign(t));
                break;
            case IF:
                p.append(parseIf(t));
                break;
                /*
            case DO:
                p.append(parseWhile(t));
                break;
                */
            case READ:
                p.append(parseIn(t));
                break;
            case WRITE:
                p.append(parseOut(t));
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
                p.append(parseStmtSeq(t));
                break;
            default:
                System.out.println("no more");
        }

        //no case system
        return p;
    }


    //how handle input and output? ah read and write tokens, come back to these
    public static ParseTree parseOut(Tokenizer t) {
        ParseTree p = new ParseTree("out");
        if (t.getToken() != TokenKind.WRITE) {
            System.out.println("should be write");
        }
        t.skipToken();
        p.append(parseIdList(t));
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("was expecting a semicolon");
        }
        t.skipToken();
        return p;
    }
    public static ParseTree parseIn(Tokenizer t) {
        ParseTree p = new ParseTree("in");
        if (t.getToken() != TokenKind.READ) {
            System.out.println("expected read");
        }
        t.skipToken();
        p.append(parseIdList(t));
        if (t.getToken() != TokenKind.SEMICOLON) {
            System.out.println("expecting semicolon!");
        }
        t.skipToken();
        return p;
    }

    //Putting in stub classes, filling in later
    public static ParseTree parseWhile(Tokenizer t) {
        ParseTree p = new ParseTree("loop");
        if (t.getToken() != TokenKind.WHILE) {
            System.out.println("expecting while");
        }
        t.skipToken();
        p.append(parseCond(t));
        if (t.getToken() != TokenKind.LOOP){
        	System.out.print("expecting loop");
        }
        t.skipToken();
        p.append(parseStmtSeq(t));
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

    public static ParseTree parseIf(Tokenizer t) {
        ParseTree p = new ParseTree("if");
        if (t.getToken() != TokenKind.IF) {
            System.out.println("expecting if token");
        }
        t.skipToken();
        p.append(parseCond(t));
        if (t.getToken() != TokenKind.THEN){
        	System.out.println("expecting then");
        }
        t.skipToken();
        p.append(parseStmtSeq(t));
        if (t.getToken() == TokenKind.ELSE){
        	t.skipToken();
        	p.append(parseStmtSeq(t));
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
    
    public static ParseTree parseCond(Tokenizer t) {
        ParseTree p = new ParseTree("cond");
        if (t.getToken() == TokenKind.EXCLAMATION){
        	ParseTree n = new ParseTree("not");
        	p.append(n);
        	t.skipToken();
        	p.append(parseCond(t));
        }
        else if (t.getToken() == TokenKind.LEFT_PAR){
        	t.skipToken();
        	p.append(parseCond(t));
        	if (t.getToken() == TokenKind.AND_OPERATOR){
        		ParseTree a = new ParseTree("and");
        		p.append(a);
        	}
        	else if (t.getToken() == TokenKind.OR_OPERATOR){
        		ParseTree o = new ParseTree("or");
        		p.append(o);
        	} else{
        		System.out.println("expecting and or or");
        	}
        	t.skipToken();
        	p.append(parseCond(t));
        	if (t.getToken() != TokenKind.RIGHT_PAR){
        		System.out.println("expecting right paren");
        	}
        	t.skipToken();
        }
        else{
        	p.append(parseCmpr(t));
        }
        return p;
    }

    public static ParseTree parseCmpr(Tokenizer t) {
        ParseTree p = new ParseTree("cmpr");
        //check against all comparators?
        if (t.getToken() != TokenKind.LEFT_BRACKET) {
            System.out.println("expecting left bracket");
        }
        t.skipToken();
        p.append(parseExpr(t));
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
                p.append(boolp);
                break;
            default:
                System.out.println("Expecting a comparison operator!");
        }
        t.skipToken();
        p.append(parseExpr(t));
        if (t.getToken() != TokenKind.RIGHT_BRACKET){
        	System.out.println("expecting right bracket!");
        }
        t.skipToken();
        
        return p;
    }

    public static ParseTree parseAssign(Tokenizer t) {
        ParseTree p = new ParseTree("assign");
        if (t.getToken() != TokenKind.ASSIGNMENT_OPERATOR) {
            System.out.println("error");
        }
        return p;
    }

    public static ParseTree parseExpr(Tokenizer t) {
        ParseTree p = new ParseTree("expr");
        //check against plus, minus 
        if (t.getToken() != TokenKind.WHILE) {
            System.out.println("error");
        }
        return p;
    }

    public static ParseTree parseTrm(Tokenizer t) {
        ParseTree p = new ParseTree("trm");
        //for strictly multiplication
        if (t.getToken() != TokenKind.WHILE) {
            System.out.println("error");
        }
        return p;
    }

    //parens
    public static ParseTree parseOp(Tokenizer t) {
        ParseTree p = new ParseTree("op");
        if (t.getToken() != TokenKind.WHILE) {
            System.out.println("error");
        }
        return p;
    }
}
