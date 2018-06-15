package GeneticProgramming;

public class Node {
	Node left, right; // References to this nodes children
	private Node parentNode = null; // Reference to nodes parent.
	private int level; // What level of depth this node is at within the tree.
	private double terminalValue;
	private String functionValue;
	private Object value;
	public Node() // Constructor
	{
	}

	// Getters & setters for left & right nodes.
	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public void setLeft(Node ln) {
		left = ln;
	}

	public void setRight(Node rn) {
		right = rn;
	}

	// Getter & setter for the parent node.
	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	// Getter & setter for the level.
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getTerminalValue() {
		return terminalValue;
	}

	public void setTerminalValue(double terminalValue) {
		this.terminalValue = terminalValue;
	}

	public String getFunctionValue() {
		return functionValue;
	}

	public void setFunctionValue(String functionValue) {
		this.functionValue = functionValue;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
