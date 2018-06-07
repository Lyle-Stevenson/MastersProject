package GeneticProgramming;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import GeneticProgramming.Parameters;

public class Tree {

	private static final String[] OPERATORS = { "+", "-", "/", "*" }; // List of
																		// possible
																		// function
																		// nodes
	private static final int MAX_OPERAND = 10; // Possible terminal node values

	private static Random random = new Random();
	private static Parameters params = new Parameters();

	private Node head = null; // Reference to head of the tree

	// public static Tree full(int depth) {
	// if (depth > 1) {
	// String operator = OPERATORS[random.nextInt(OPERATORS.length)];
	// return new Tree(operator, full(depth - 1), full(depth - 1));
	// } else {
	// return new Tree(random.nextInt(MAX_OPERAND) + 1);
	// }
	// }

	public Tree() { // Constructor
		this.head = generateHead(); // Trees head is created on initialisation
		this.head.setParentNode(null); // Head node has no parent
		this.head.setLevel(0); // Sets level to 0
		this.head = grow(this.head, params.maxDepth); // Call to generate the
														// rest of tree.
	}

	public Node generateHead() { // Generates the head node of the tree
		String operator = OPERATORS[random.nextInt(OPERATORS.length)]; // Selects a random fucntion for node.
		return new Node(operator); // Returns head node
	}

	public Node grow(Node parent, int depth) { // Grows the tree from given
												// node.
		int level = parent.getLevel() + 1; // Level tracker to assign to newly
											// created children.
		for (int i = 0; i < 2; i++) { // Grow method is ran through twice as each node can have a left and right child
			Node child = null; // Initialise the child
			if (level < depth && random.nextBoolean()) { //50% chance the new child is a terminal or fucntion node.
				String operator = OPERATORS[random.nextInt(OPERATORS.length)]; //Random function is selected
				child = new Node(operator); //Child node is set
				child.setLevel(parent.getLevel() + 1); //Childs level is set
				child.setParentNode(parent); //Parent is set.
				if (i == 0) { //If first iteration then its left child
					parent.setLeft(child);
				} else { //If second iteration then its right child
					parent.setRight(child);
				}
				child = grow(child, depth); //Grow tree from this child
			} else { //If child is selected to be a terminal node
				child = new Node(random.nextInt(MAX_OPERAND) + 1); //Terminal value added to node
				child.setLevel(parent.getLevel() + 1); //Childs level is set
				child.setParentNode(parent);//Childs parent is set
				if (i == 0) { //If first iteration then its left child
					parent.setLeft(child);
				} else { //If second iteration then its right child
					parent.setRight(child);
				}
			}
		}
		return parent; //Returns head node once tree is complete.
	}

	public void printTree() {
		Queue<Node> q = new LinkedList<Node>();
		q.add(this.head);
		while (!q.isEmpty()) {
			Node n = q.poll();
			System.out.println(n.getValue() + " : Level - " + n.getLevel()); 
			if (n.left != null) {
				q.add(n.left);// enqueue the left child
			}
			if (n.right != null) {
				q.add(n.right);// enque the right child
			}
		}
	}
}
