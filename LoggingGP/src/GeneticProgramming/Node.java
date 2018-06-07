package GeneticProgramming;

public class Node {
	Object value; // Value of function or termial node, i.e "+" or "1"
	Node left, right; // References to this nodes children
	private Node parentNode = null; // Reference to nodes parent.
	private int level; // What level of depth this node is at within the tree.

	public Node(Object value) // Constructor
	{
		this.value = value;
	}

	public Node(Object value, Node left, Node right) {
		this.value = value;
		this.left = left;
		this.right = right;
	}

	// Getter & setter for the value.
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
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

}
