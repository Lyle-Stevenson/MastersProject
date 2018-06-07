package GeneticProgramming;

public class Node {
	   Object value;
	   Node left, right;
	   private Node parentNode = null;
	   private int level;

	   public Node(Object value)
	   {
	      this.value=value;
	   }   

	   public Node(Object value, Node left, Node right) 
	   {
	      this.value = value;
	      this.left = left;
	      this.right = right;
	   } 

	   // Getter & setter for the value.
	   public Object getValue(){
	      return value;}
	   public void setValue(Object value){
	      this.value = value;}

	   // Getters & setters for left & right nodes.
	   public Node getLeft(){ return left;}
	   public Node getRight(){ return right;}
	   public void setLeft(Node ln){left = ln;}
	   public void setRight(Node rn){right = rn;}

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
	   
}
