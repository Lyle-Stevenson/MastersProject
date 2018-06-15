package GeneticProgramming;

import java.lang.reflect.Constructor;
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
			subNodes.addAll(childNode.getAllSubNodes());
		}
		return subNodes;
	}
	
	public ArrayList<Node> getAllNodes(){
		ArrayList<Node> allNodes = new ArrayList<Node>();
		allNodes.add(this);
		for(Node childNode : getChildNodes()){
			allNodes.add(childNode);
			allNodes.addAll(childNode.getAllSubNodes());
		}
		return allNodes;
	}

	public ArrayList<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(ArrayList<Node> childNodes) {
		this.childNodes = childNodes;
	}
	
	public Node copy() {
		Node copiedNode;
		if(this instanceof TerminalNode){
			copiedNode = new TerminalNode(this.getTerminalValue());
			copiedNode.setFunctionValue(this.getFunctionValue());
		}else{
			copiedNode = new FunctionNode(this.getFunctionValue());
			if(this.getLeft() != null){
				copiedNode.setLeft(this.getLeft().copy());
				copiedNode.getLeft().setParentNode(copiedNode);
				}
			if(this.getRight() != null){
					copiedNode.setRight(this.getRight().copy());
					copiedNode.getRight().setParentNode(copiedNode);
					}
		}
		
		if(this.parentNode != null){
			copiedNode.setLevel(this.parentNode.getLevel() + 1);
		}
		for(Node node : this.childNodes){
			copiedNode.getChildNodes().add(node.copy());
		}
		return copiedNode; 
	}
	

}
