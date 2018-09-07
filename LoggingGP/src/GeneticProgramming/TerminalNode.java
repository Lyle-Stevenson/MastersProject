package GeneticProgramming;

public class TerminalNode extends Node {
	
	//Terminals hold both terminal value which is the numerical value for the feature it holds and also a function value which holds the lable for the feature it is i.e "f2" "f43"
	public TerminalNode(double value) {
		this.setTerminalValue(value);
		this.setValue(value);
	}
	
	public TerminalNode(String value) {
		this.setFunctionValue(value);
		this.setValue(value);
	}
	
	public TerminalNode(double value, Node left, Node right) {
		this.setTerminalValue(value);
		this.setValue(value);
		this.left = left;
		this.right = right;
	}

}
