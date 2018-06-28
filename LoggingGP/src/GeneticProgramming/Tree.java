package GeneticProgramming;

import java.awt.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import GeneticProgramming.Parameters;

public class Tree {

	private static final String[] FUNCTIONS = {"+","-","/","*"}; // List of
																		// possible
																		// function
																		// nodes
	private static final String[] TERMINALS = { "f1","f2","f3","f4","f5","f6","f7","f8"};
		/*,"f9","f10",
												"f11","f12","f13","f14","f15","f16","f17","f18","f19","f20",
												"f21","f22","f23","f24","f25","f26","f27","f28","f29","f30",
												"f31","f32","f33","f34","f35","f36","f37","f38","f39","f40",
												"f41","f42","f43","f44","f45","f46","f47","f48","f49","f50",
												"f51","f52","f53","f54","f55","f56","f57","f58"}; // Possible terminal node values*/
	private double fitness = 0;
	private double accuracy = 0;
	private static Random random = new Random();
	private static Parameters params = new Parameters();
	
	private double c1Mean;
	private double c1SD;
	private double c0Mean;
	private double c0SD;

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
		this.setHead(generateHead()); // Trees head is created on initialisation
		this.getHead().setParentNode(null); // Head node has no parent
		this.getHead().setLevel(1); // Sets level to 0
		this.setHead(grow(this.getHead(), params.maxDepth)); // Call to generate the
														// rest of tree.
	}
	
	public Tree(int depth) { // Constructor
		this.setHead(generateHead()); // Trees head is created on initialisation
		this.getHead().setParentNode(null); // Head node has no parent
		this.getHead().setLevel(1); // Sets level to 0
		this.setHead(grow(this.getHead(), depth)); // Call to generate the
														// rest of tree.
	}
	
	public Tree(Node head){
		this.setHead(head);
	}

	public Node generateHead() { // Generates the head node of the tree
		String function = FUNCTIONS[random.nextInt(FUNCTIONS.length)]; // Selects a random fucntion for node.
		return new FunctionNode(function); // Returns head node
	}

	public Node grow(Node parent, int depth) { // Grows the tree from given
												// node.
		int level = parent.getLevel() + 1; // Level tracker to assign to newly
											// created children.
		for (int i = 0; i < 2; i++) { // Grow method is ran through twice as each node can have a left and right child
			Node child = null; // Initialise the child
			if (level < depth && random.nextBoolean()) { //50% chance the new child is a terminal or fucntion node.
				String function = FUNCTIONS[random.nextInt(FUNCTIONS.length)]; //Random function is selected
				child = new FunctionNode(function); //Child node is set
				child.setLevel(parent.getLevel() + 1); //Childs level is set
				child.setParentNode(parent); //Parent is set.
				if (i == 0) { //If first iteration then its left child
					parent.setLeft(child);
					parent.getChildNodes().add(child);
				} else { //If second iteration then its right child
					parent.setRight(child);
					parent.getChildNodes().add(child);
				}
				child = grow(child, depth); //Grow tree from this child
			} else { //If child is selected to be a terminal node
				String terminal = TERMINALS[random.nextInt(TERMINALS.length)]; //Random terminal is selected and stored as "funtion" while it is text and not int value
				child = new TerminalNode(terminal); //Terminal value added to node
				child.setLevel(parent.getLevel() + 1); //Childs level is set
				child.setParentNode(parent);//Childs parent is set
				if (i == 0) { //If first iteration then its left child
					parent.setLeft(child);
					parent.getChildNodes().add(child);
				} else { //If second iteration then its right child
					parent.setRight(child);
					parent.getChildNodes().add(child);
				}
			}
		}
		return parent; //Returns head node once tree is complete.
	}

	public void train(ArrayList<Problem> training){
		
		ArrayList<Double> c1 = new ArrayList<Double>();
		ArrayList<Double> c0 = new ArrayList<Double>();
		
		for(Problem p:training){
			subInFeats(p.getFeatures());
			double evaluation;	
			evaluation= evaluate(this.getHead());
			if(evaluation == Double.POSITIVE_INFINITY || evaluation == Double.NEGATIVE_INFINITY) {
				evaluation = 0;
			}
			if(p.getClassification() == 0){
				c0.add(evaluation);
			}else{
				c1.add(evaluation);
			}
		}
		setC1Mean(getMean(c1));
		setC0Mean(getMean(c0));
		
		setC1SD(getStdDev(getC1Mean(),c1));
		setC0SD(getStdDev(getC0Mean(),c0));		
		
		setFitness(calcFitness());
		
		
	}
	
	public void test(ArrayList<Problem> testing){

		int correct = 0;
		int incorrect = 0;
		
		for(Problem p:testing){
			int classification;
			subInFeats(p.getFeatures());
			double evaluation = evaluate(this.getHead());
			double c0Prob = getClass(evaluation, this.c0Mean, getC0SD());
			double c1Prob = getClass(evaluation, this.c1Mean, getC1SD());
			//System.out.println("Eval : " + evaluation);
			//System.out.println("C0: " + "Prob: " + c0Prob + " Mean: "  + this.c0Mean);
			//System.out.println("C1: " + "Prob: " + c1Prob + " Mean: "  + this.c1Mean);
			
			if(c0Prob > c1Prob){
				classification = 0;
			}else{
				classification = 1;
			}
			
			if(classification == p.getClassification()){
				correct++;
			}else{
				incorrect++;
			}
			System.out.println("predicted " + classification + "  actual " + p.getClassification());
		}
		double total = correct+incorrect;
		double accuracy = (correct / total) * 100;
		setAccuracy(accuracy);
	}
	
	public double getClass(double x, double mean, double SD){
		double value = 0;
		double left = 1/(SD*(Math.sqrt(2 * Math.PI)));
		double rightTop = Math.pow((x - mean),2) * -1;
		double rightBottom = 2*Math.pow(SD, 2);
		double right = rightTop / rightBottom;

		value = left * Math.exp(right);
		
		return value;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public double calcFitness(){
		double fitness = 0;
		
		double top = Math.abs(this.getC0Mean() - this.getC1Mean()) + 1;
		double bottom = Math.sqrt(this.getC0SD() + this.getC1SD()) + 1;
		fitness = top / bottom;
		
		return fitness;
	}
	 double getMean(ArrayList<Double> data) {
	        double sum = 0.0;
	        for(double a : data)
	            sum += a;
	        return sum/data.size();
	    }

	    double getVariance(double mean, ArrayList<Double> data) {
	        double temp = 0;
	        for(double a :data)
	            temp += (a-mean)*(a-mean);
	        return temp/(data.size()-1);
	    }

	    double getStdDev(double mean , ArrayList<Double> data ) {
	        return Math.sqrt(getVariance(mean,data));
	    }
	
	
	    
	public void printTree() {
		Queue<Node> q = new LinkedList<Node>();
		q.add(this.getHead());
		while (!q.isEmpty()) {
			Node n = q.poll();
			if(n instanceof FunctionNode){
			System.out.println(n.getFunctionValue() + " : Level - " + n.getLevel());}
			else{
				System.out.println(n.getFunctionValue() + " : Level - " + n.getLevel());}	
			if (n.left != null) {
				q.add(n.left);// enqueue the left child
			}
			if (n.right != null) {
				q.add(n.right);// enque the right child
			}
		}
	}
	
	public void subInFeats(ArrayList<Double> features) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(this.getHead());
		while (!q.isEmpty()) {
			Node n = q.poll();
			switch(n.getFunctionValue()){
			case "f1":
				n.setTerminalValue(features.get(0));
				break;
			case "f2":
				n.setTerminalValue(features.get(1));
				break;
			case "f3":
				n.setTerminalValue(features.get(2));
				break;
			case "f4":
				n.setTerminalValue(features.get(3));
				break;
			case "f5":
				n.setTerminalValue(features.get(4));
				break;
			case "f6":
				n.setTerminalValue(features.get(5));
				break;
			case "f7":
				n.setTerminalValue(features.get(6));
				break;
			case "f8":
				n.setTerminalValue(features.get(7));
				break;
			case "f9":
				n.setTerminalValue(features.get(8));
				break;
			case "f10":
				n.setTerminalValue(features.get(9));
				break;
			case "f11":
				n.setTerminalValue(features.get(10));
				break;
			case "f12":
				n.setTerminalValue(features.get(11));
				break;
			case "f13":
				n.setTerminalValue(features.get(12));
				break;
			case "f14":
				n.setTerminalValue(features.get(13));
				break;
			case "f15":
				n.setTerminalValue(features.get(14));
				break;
			case "f16":
				n.setTerminalValue(features.get(15));
				break;
			case "f17":
				n.setTerminalValue(features.get(16));
				break;
			case "f18":
				n.setTerminalValue(features.get(17));
				break;
			case "f19":
				n.setTerminalValue(features.get(18));
				break;
			case "f20":
				n.setTerminalValue(features.get(19));
				break;
			case "f21":
				n.setTerminalValue(features.get(20));
				break;
			case "f22":
				n.setTerminalValue(features.get(21));
				break;
			case "f23":
				n.setTerminalValue(features.get(22));
				break;
			case "f24":
				n.setTerminalValue(features.get(23));
				break;
			case "f25":
				n.setTerminalValue(features.get(24));
				break;
			case "f26":
				n.setTerminalValue(features.get(25));
				break;
			case "f27":
				n.setTerminalValue(features.get(26));
				break;
			case "f28":
				n.setTerminalValue(features.get(27));
				break;
			case "f29":
				n.setTerminalValue(features.get(28));
				break;
			case "f30":
				n.setTerminalValue(features.get(29));
				break;
			case "f31":
				n.setTerminalValue(features.get(30));
				break;
			case "f32":
				n.setTerminalValue(features.get(31));
				break;
			case "f33":
				n.setTerminalValue(features.get(32));
				break;
			case "f34":
				n.setTerminalValue(features.get(33));
				break;
			case "f35":
				n.setTerminalValue(features.get(34));
				break;
			case "f36":
				n.setTerminalValue(features.get(35));
				break;
			case "f37":
				n.setTerminalValue(features.get(36));
				break;
			case "f38":
				n.setTerminalValue(features.get(37));
				break;
			case "f39":
				n.setTerminalValue(features.get(38));
				break;
			case "f40":
				n.setTerminalValue(features.get(39));
				break;
			case "f41":
				n.setTerminalValue(features.get(40));
				break;
			case "f42":
				n.setTerminalValue(features.get(41));
				break;
			case "f43":
				n.setTerminalValue(features.get(42));
				break;
			case "f44":
				n.setTerminalValue(features.get(43));
				break;
			case "f45":
				n.setTerminalValue(features.get(44));
				break;
			case "f46":
				n.setTerminalValue(features.get(45));
				break;
			case "f47":
				n.setTerminalValue(features.get(46));
				break;
			case "f48":
				n.setTerminalValue(features.get(47));
				break;
			case "f49":
				n.setTerminalValue(features.get(48));
				break;
			case "f50":
				n.setTerminalValue(features.get(49));
				break;
			case "f51":
				n.setTerminalValue(features.get(50));
				break;
			case "f52":
				n.setTerminalValue(features.get(51));
				break;
			case "f53":
				n.setTerminalValue(features.get(52));
				break;
			case "f54":
				n.setTerminalValue(features.get(53));
				break;
			case "f55":
				n.setTerminalValue(features.get(54));
				break;
			case "f56":
				n.setTerminalValue(features.get(55));
				break;
			case "f57":
				n.setTerminalValue(features.get(56));
				break;
			case "f58":
				n.setTerminalValue(features.get(57));
				break;
			}
			if (n.left != null) {
				q.add(n.left);// enqueue the left child
			}
			if (n.right != null) {
				q.add(n.right);// enque the right child
			}
		}
	}
	
	
	public double evaluate(Node node){
		if(node !=null){
			if(node instanceof TerminalNode){
				return node.getTerminalValue();
			}else{
				double left = evaluate(node.left);
				double right = evaluate(node.right);
				return calculate(left,node.getFunctionValue(), right);
			}
		}
	return -1;
	}

	private double calculate(double left, String functionValue, double right) {
		
	double sum = 0;
	
	switch (functionValue){
	case "+":
		sum = left + right;
		return sum;
	case "*":
		sum = left * right;
		return sum;
	case "-":
		sum = left - right;
		return sum;
	case "/":
		if(right == 0){
			right = 1;
		}
		sum = left / right;
	return sum;
	}
	
	return -1;
}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public double getC0Mean() {
		return c0Mean;
	}

	public void setC0Mean(double c0Mean) {
		this.c0Mean = c0Mean;
	}

	public double getC1SD() {
		return c1SD;
	}

	public void setC1SD(double c1sd) {
		c1SD = c1sd;
	}

	public double getC0SD() {
		return c0SD;
	}

	public void setC0SD(double c0sd) {
		c0SD = c0sd;
	}

	public double getC1Mean() {
		return c1Mean;
	}

	public void setC1Mean(double c1Mean) {
		this.c1Mean = c1Mean;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public Tree copy(Node head) {
		Tree copy = new Tree(head);
		
		return copy;
		
		
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
}
