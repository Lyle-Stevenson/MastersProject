package GeneticProgramming;

import java.util.ArrayList;


public class Node {
	Node left, right; // References to this nodes children
	private Node parentNode = null; // Reference to nodes parent.
	private int level; // What level of depth this node is at within the tree.
	private double terminalValue; //Hold the terminal value if its a terminal node
	private String functionValue;
	private Object value;
	private ArrayList<Node> childNodes = new ArrayList<Node>();	
	public Node() // Constructor
	{
	}

	// Getters & setters
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

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

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
	
	public ArrayList<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(ArrayList<Node> childNodes) {
		this.childNodes = childNodes;
	}
	
	//Returns all the nodes below this node.
	public ArrayList<Node> getAllSubNodes(){
		ArrayList<Node> subNodes = new ArrayList<Node>(); //Arraylist to store all the nodes below this one.
		for(Node childNode : getChildNodes()){ //For each child node of given node.
			subNodes.add(childNode); //Add child node to sub nodes array
			if(childNode instanceof FunctionNode){ //Checks if child is a function node
			subNodes.addAll(childNode.getAllSubNodes()); //Recursion to get all the nodes of the child nodes
			}
		}
		return subNodes;//Returns all subnodes found.
	}
	
	//Returns all nodes below this one including the given node.
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
	
	//Copies a node and all its sub nodes. Used to provide a completely seperate instance copy of a tree.
	public Node copy(Node parent) {
		Node copiedNode; //Creates a new node
		if(this instanceof TerminalNode){//Checks if node is a terminal or function and applies that to new node.
			copiedNode = new TerminalNode(this.getTerminalValue());
			copiedNode.setFunctionValue(this.getFunctionValue()); //Function values for terminal nodes hold the feature label i.e "f2" or "f58"
		}else{
			copiedNode = new FunctionNode(this.getFunctionValue());
		}
		
		copiedNode.setParentNode(parent);// On first call of this method parent will always be null all recursive calls will refer to a copied node as the parent.
		
		if(this.getLeft() != null){//If the node being copied has children then copy those children also.
		copiedNode.setLeft(this.getLeft().copy(copiedNode)); //Copies the originals children and sets the copied children.	
		}
		if(this.getRight() != null){
			copiedNode.setRight(this.getRight().copy(copiedNode));	
			}
		
		if(this.parentNode != null){ //Resets the level counter on all nodes to make sure they are correct.
			copiedNode.setLevel(this.parentNode.getLevel() + 1);
		}
		
		//adds the copied nodes children to the array list tracking them.
		copiedNode.childNodes.add(copiedNode.getLeft());
		copiedNode.childNodes.add(copiedNode.getRight());
		
		return copiedNode; 
	}
	
	//Resets all the levels on a node and its children to make sure they are correct.
	public void resetLevels(Node node){
		if(node.getParentNode() == null){
			node.setLevel(1);
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
	
	//Returns the current maxdept of a node.
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
