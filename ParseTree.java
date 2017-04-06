import java.util.ArrayList;
import java.util.List;

public class ParseTree {
	public String type;
	public List<ParseTree> children;
	public String value;
	int indentSize = 4;
	
	//initialize the tree
	public ParseTree(String type){
		this.type = type;
		this.children = new ArrayList<ParseTree>();
		this.value = "";
	}
	
	//set the value if it's an identifier or an int
	public void setVal(String val){
		this.value = val;
	}
	
	//adjust indentsize if needed
	public void setIndent(int indent){
		this.indentSize = indent;
	}
	
	//add children
	public void append(ParseTree p){
		//this or no?
		this.children.add(p);
	}
	
	//consolidate these strings
	public String programStr(){
		String s = "";
		s += ("program \n");
		s += children.get(0).declSeqStr(indentSize);
		s += ("begin \n");
		s += children.get(1).stmtSeqStr(indentSize);
		s += ("end \n");
		return s;
	}
	
	public String stmtSeqStr(int indent){
		String s = "";
		if (children.get(0).type == "assign"){
			//s += children.get(0).assignStr(indent);
			s+="assign";
		}
		else if (children.get(0).type == "if"){
			//s += children.get(0).assignStr(indent);
			s+= "if";
		}
		else if (children.get(0).type == "loop"){
			//s += children.get(0).assignStr(indent);
			s+= "loop";
		}
		else if (children.get(0).type == "in"){
			//s += children.get(0).assignStr(indent);
			s+= "in";
		}
		else if (children.get(0).type == "out"){
			//s += children.get(0).assignStr(indent);
			s+= "out";
		}
		else if (children.get(0).type == "case"){
			//s += children.get(0).assignStr(indent);
			s+= "case";
		}
		else {
			s+= "error in stmtSeqStr type";
		}
		
		if(children.size() > 1){
			s += children.get(1).stmtSeqStr(indent);
		}
	}
	
	public static void main(String[] args){
		System.out.println("Test");
	}
}

//parse program
public static ParseTree constructPT(Tokenizer t){
	//start with creating the program
	ParseTree p = new ParseTree("program");
	if (t.currentToken() != TokenKind.PROGRAM){
		System.out.println("Input does not begin with program.");
	}
	t.skipToken();
	//then begin statement
	p.append(parseDeclSeq(s));
	if (s.currentToken() != TokenKind.BEGIN){
		System.out.println("Should have begin");
	}
	t.skipToken();
	p.append(parseStmtSeq(s));
	if (s.currentToken != TokenKind.END){
		System.out.print("should have end");
	}
	t.skipToken();
	if (t.currentToken() != TokenKind.EOF){
		System.out.print("File should end with end!");
	}
	
	return p;
	
}

public static ParseTree parseDeclSeq(Tokenizer t){
	ParseTree p = new ParseTree("declseq");
	if (t.currentToken() != TokenKind.INT){
		System.out.println("Should have an int here");
	}
	p.append(parseDecl(s));
	while(t.currentToken() != TokenKind.BEGIN){
		p.append(parseDeclSeq(t));
	}
	return p;
}

private static ParseTree parseDecl(Tokenizer t){
	ParseTree p = new ParseTree("decl");
	if (s.currentToken() != TokenKind.INT){
		System.out.println("parseDecl should have int");
	}
	t.skipToken();
	p.append(parseIdList(s));
	if (s.currentToken() != TokenKind.SEMICOLON){
		System.out.println("should finish with a semicolon");
	}
	t.skipToken();
	return p;
}

private static ParseTree parseIdList(Tokenizer t){
	ParseTree p = new ParseTree("idlist");
	if (t.currentToken() != TokenKind.IDENTIFIER){
		System.out.println("Wanted an ID");
	}
	ParseTree p2 = newParseTree("id");
	//todo -- find out how to obtain value
	p2.setVal("ABC");
	p.append(p2);
	t.nextToken();
	if (t.currentToken() == TokenKind.COMMA){
		t.nextToken();
		p.append(parseIdList(t));
	}
	return p;
}

private static ParseTree parseStmtSeq(Tokenizer t){
	ParseTree p = new ParseTree("stmtseq");
	switch (t.currentToken()){
		case TokenKind.IDENTIFIER: p.append(parseAssign(t));
			break;
		case TokenKind.IF: p.append(parseIf(t));
			break;
		case TokenKind.DO: p.append(parseWhile(t));
			break;
		case TokenKind.INPUT: p.append(parseInput(t));
			break;
		case TokenKind.OUTPUT: p.append(parseOutput(t));
			break;
		case TokenKind.CASE: p.append(parseCase(t));
			break;
		default: System.out.println("DIdn't find case for parseStmtSeq");
			break;
			
	}
	//CHECK THIS
	TokenKind tk = t.currentToken();
	switch (t.currentToken()){
		case TokenKind.IDENTIFIER: case TokenKind.DO: case TokenKind.INPUT: case TokenKind.OUTPUT: case TokenKind.CASE: p.append(parseStmtSeq(s)); break;
		default: System.out.println("no more");
	}
	
	//no case system
}


//how handle input and output? ah read and write tokens, come back to these
public static ParseTree parseOut(Tokenize t){
	ParseTree p = new ParseTree("out");
	if (t.currentToken() != TokenKind.WRITE){
		System.out.println("should be write");
	}
	t.nextToken();
	p.append(parseIdList(t));
	if (t.currentToken() != TokenKind.SEMICOLON){
		System.out.println("was expecting a semicolon");
	}
	t.skipToken();
	return p;
}
public static ParseTree parseIn(Tokenizer t){
	ParseTree p = new ParseTree("in");
	if (t.currentToken() != TokenKind.READ){
		System.out.println("expected read");
	}
	t.skipToken();
	p.append(parseIdList(t));
	if (t.currentToken() != TokenKind.SEMICOLON){
		System.out.println("expecting semicolon!");
	}
	t.skipToken();
	return p;
}

//Putting in stub classes, filling in later
public static ParseTree parseWhile(Tokenizer t){
	ParseTree p = new ParseTree("loop");
	if (s.currentToken() != TokenKind.WHILE){
		System.out.println("error");
	}
	return p;
}

public static ParseTree parseIf(Tokenizer t){
	ParseTree p = new ParseTree("if");
	if (s.currentToken() != TokenKind.IF){
		System.out.println("error");
	}
	return p;
}

//what is cond token
public static ParseTree parseCond(Tokenizer t){
	ParseTree p = new ParseTree("cond");
	if (s.currentToken() != TokenKind.COND){
		System.out.println("error");
	}
	return p;
}

public static ParseTree parseCmpr(Tokenizer t){
	ParseTree p = new ParseTree("cmpr");
	//check against all comparators?
	if (s.currentToken() != TokenKind.WHILE){
		System.out.println("error");
	}
	return p;
}

public static ParseTree parseAssign(Tokenizer t){
	ParseTree p = new ParseTree("assign");
	if (s.currentToken() != TokenKind.ASSIGNMENT_OPERATOR){
		System.out.println("error");
	}
	return p;
}

public static ParseTree parseExpr(Tokenizer t){
	ParseTree p = new ParseTree("expr");
	//check against plus, minus 
	if (s.currentToken() != TokenKind.WHILE){
		System.out.println("error");
	}
	return p;
}

public static ParseTree parseTrm(Tokenizer t){
	ParseTree p = new ParseTree("trm");
	//for strictly multiplication
	if (s.currentToken() != TokenKind.WHILE){
		System.out.println("error");
	}
	return p;
}

//parens
public static ParseTree parseOp(Tokenizer t){
	ParseTree p = new ParseTree("op");
	if (s.currentToken() != TokenKind.WHILE){
		System.out.println("error");
	}
	return p;
}





