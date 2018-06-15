package GeneticProgramming;

public class TerminalNode extends Node {
	
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
