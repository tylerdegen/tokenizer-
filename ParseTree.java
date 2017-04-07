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
		this.children.add(p);
	}

	public static void main(String[] args){
		System.out.println("Test");
	}
}
