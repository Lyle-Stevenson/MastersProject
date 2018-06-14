package GeneticProgramming;

public class FunctionNode extends Node {

	public FunctionNode(String value) {
		this.setFunctionValue(value);
		this.setValue(value);
	}
	
	public FunctionNode(String value, Node left, Node right) {
		this.setFunctionValue(value);
		this.setValue(value);
		this.left = left;
		this.right = right;
	}
	
}
