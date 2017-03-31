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
	public setVal(String val){
		this.value = val;
	}
	
	//adjust indentsize if needed
	public setIndent(int indent){
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
		s += children.get(0).declSeqStr(indentSize));
		s += ("begin \n");
		s += children.get(1).stmtSeqStr(indentSize));
		s += ("end \n");
		return s;
	}
	
	public stmtSeqStr(int indent){
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
}

//parse program
public static ParseTree constructPT(Tokenizer t){
	
}