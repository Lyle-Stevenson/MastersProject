package GeneticProgramming;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Node {
	Node left, right; // References to this nodes children
	private Node parentNode = null; // Reference to nodes parent.
	private int level; // What level of depth this node is at within the tree.
	private double terminalValue;
	private String functionValue;
	private Object value;
	private ArrayList<Node> childNodes = new ArrayList<Node>();	
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
	
	public ArrayList<Node> getAllSubNodes(){
		ArrayList<Node> subNodes = new ArrayList<Node>();
		for(Node childNode : getChildNodes()){
			subNodes.add(childNode);
			if(childNode instanceof FunctionNode){
			subNodes.addAll(childNode.getAllSubNodes());
			}
		}
		return subNodes;
	}
	
	public ArrayList<Node> getAllNodes(){
		ArrayList<Node> allNodes = new ArrayList<Node>();
		allNodes.add(this);
		for(Node childNode : getChildNodes()){
			allNodes.add(childNode);
			if(childNode instanceof FunctionNode){
			allNodes.addAll(childNode.getAllSubNodes());
			}
		}
		return allNodes;
	}

	public ArrayList<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(ArrayList<Node> childNodes) {
		this.childNodes = childNodes;
	}
	
	public Node copy(Node parent) {
		Node copiedNode;
		if(this instanceof TerminalNode){
			copiedNode = new TerminalNode(this.getTerminalValue());
			copiedNode.setFunctionValue(this.getFunctionValue());
		}else{
			copiedNode = new FunctionNode(this.getFunctionValue());
		}
		
		copiedNode.setParentNode(parent);
		
		if(this.getLeft() != null){
		copiedNode.setLeft(this.getLeft().copy(copiedNode));	
		}
		if(this.getRight() != null){
			copiedNode.setRight(this.getRight().copy(copiedNode));	
			}
		
		if(this.parentNode != null){
			copiedNode.setLevel(this.parentNode.getLevel() + 1);
		}
		copiedNode.childNodes.add(copiedNode.getLeft());
		copiedNode.childNodes.add(copiedNode.getRight());
		return copiedNode; 
	}
	
	public void resetLevels(Node node){
		if(node.getParentNode() == null){
			node.setLevel(0);
		}else{
		int parentlevel = node.getParentNode().getLevel();
		node.setLevel(parentlevel +1);
		}
		if(node.getLeft() != null){
			resetLevels(node.getLeft());
		}
		if(node.getRight() != null){
			resetLevels(node.getRight());
		}
	}
	
	public int getMaxDepth(){		
		int maxDepth = 0;
		for(Node node : getAllSubNodes()){
			if(node != null){
				if(node.level > maxDepth){
					maxDepth = node.level;
				}
			}
		}
		return maxDepth;
	}


}
