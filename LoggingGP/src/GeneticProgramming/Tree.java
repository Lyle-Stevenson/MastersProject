package GeneticProgramming;
import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import GeneticProgramming.Parameters;


public class Tree {

	 private static final String[] OPERATORS = {"+", "-", "/", "*"};
	 private static final int MAX_OPERAND = 10;

	 private static Random random = new Random();
	 private static Parameters params = new Parameters();
	 
	 private Node head = null;

//	    public static Tree full(int depth) {
//	        if (depth > 1) {
//	            String operator = OPERATORS[random.nextInt(OPERATORS.length)];
//	            return new Tree(operator, full(depth - 1), full(depth - 1));
//	        } else {
//	            return new Tree(random.nextInt(MAX_OPERAND) + 1);
//	        }
//	    }
	 
	 	public Tree(){
	 		this.head = generateHead();
	 		this.head.setParentNode(null);
	 		this.head.setLevel(0);
	 		this.head = grow(this.head,params.maxDepth);
	 	}

	 	public Node generateHead(){
	 		String operator = OPERATORS[random.nextInt(OPERATORS.length)];
            return new Node(operator);
	 	}
	 	
	 	
	    public Node grow(Node parent, int depth) {
	    	int level = parent.getLevel() +1;
	    	for(int i = 0; i < 2; i ++){
	    		Node child = null;
		        if (level < depth && random.nextBoolean()) {
		            String operator = OPERATORS[random.nextInt(OPERATORS.length)];
		            child = new Node(operator);
		            child.setLevel(parent.getLevel() +1);
			        child.setParentNode(parent);
			        if(i == 0){
			        	parent.setLeft(child);
			        }else{
			        	parent.setRight(child);
			        }
			        child = grow(child, depth);
		        } else {
		            child = new Node(random.nextInt(MAX_OPERAND) + 1);
		            child.setLevel(parent.getLevel() +1);
			        child.setParentNode(parent);
			        if(i == 0){
			        	parent.setLeft(child);
			        }else{
			        	parent.setRight(child);
			        }
		        }	        
	    	}
	    	return parent;
	    }
	    
	    public void printTree(){
	    	 Queue<Node> q = new LinkedList<Node>();
	    	 q.add(this.head);//You don't need to write the root here, it will be written in the loop
	    	 while (!q.isEmpty())
	    	 {
	    	    Node n = q.poll();
	    	    System.out.println(n.getValue() + " : Level - " + n.getLevel()); //Only write the value when you dequeue it
	    	    if (n.left !=null)
	    	    {
	    	        q.add(n.left);//enqueue the left child
	    	    }
	    	    if (n.right !=null)
	    	    {
	    	       q.add(n.right);//enque the right child
	    	    }
	    	 }
	    }
}
