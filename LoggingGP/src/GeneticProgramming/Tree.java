package GeneticProgramming;

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
	private static final String[] TERMINALS = { "f1","f2","f3","f4","f5","f6","f7","f8","f9","f10",
												"f11","f12","f13","f14","f15","f16","f17","f18","f19","f20",
												"f21","f22","f23","f24","f25","f26","f27","f28","f29","f30",
												"f31","f32","f33","f34","f35","f36","f37","f38","f39","f40",
												"f41","f42","f43","f44","f45","f46","f47","f48","f49","f50",
												"f51","f52","f53","f54","f55","f56","f57","f58"};
	private double fitness = 0;
	private double accuracy = 0;
	private double accuracy2 = 0;
	private double accuracyC1 = 0;
	private double accuracyC0 = 0;
	private static Random random = new Random();
	
	private double c1Mean;
	private double c1SD;
	private double c0Mean;
	private double c0SD;

	private Node head = null; // Reference to head of the tree

	//Defaults constructor is to create a tree using grow method
	public Tree() { // Constructor
		this.setHead(generateHead()); // Trees head is created on initialisation
		this.getHead().setParentNode(null); // Head node has no parent
		this.getHead().setLevel(1); // Sets level to 0
		this.setHead(grow(this.getHead(), Parameters.maxDepth)); // Call to generate the
														// rest of tree.
	}
	
	//This constructor creates a tree using full method
	public Tree(String full) { // Constructor
		this.setHead(generateHead()); // Trees head is created on initialisation
		this.getHead().setParentNode(null); // Head node has no parent
		this.getHead().setLevel(1); // Sets level to 0
		this.setHead(full(this.getHead(), Parameters.maxDepth)); // Call to generate the
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
		for (int i = 0; i < 2; i++) { //  method is ran through twice as each node can have a left and right child
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
	
	
	public Node full(Node parent, int depth) { // Grows the tree from given
		// node.
		int level = parent.getLevel() + 1; // Level tracker to assign to newly
		// created children.
		for (int i = 0; i < 2; i++) { 
			Node child = null; // Initialise the child
			if (level < depth) { 
				String function = FUNCTIONS[random.nextInt(FUNCTIONS.length)]; 
				child = new FunctionNode(function); // Child node is set
				child.setLevel(parent.getLevel() + 1); // Childs level is set
				child.setParentNode(parent); // Parent is set.
				if (i == 0) { // If first iteration then its left child
					parent.setLeft(child);
					parent.getChildNodes().add(child);
				} else { // If second iteration then its right child
					parent.setRight(child);
					parent.getChildNodes().add(child);
				}
				child = grow(child, depth); // Grow tree from this child
			} else { // If child is selected to be a terminal node
				String terminal = TERMINALS[random.nextInt(TERMINALS.length)]; 
				child = new TerminalNode(terminal); // Terminal value added to
													// node
				child.setLevel(parent.getLevel() + 1); // Childs level is set
				child.setParentNode(parent);// Childs parent is set
				if (i == 0) { // If first iteration then its left child
					parent.setLeft(child);
					parent.getChildNodes().add(child);
				} else { // If second iteration then its right child
					parent.setRight(child);
					parent.getChildNodes().add(child);
				}
			}
		}
		return parent; // Returns head node once tree is complete.
	}

	//Trains the tree on the training set.
	public void train(ArrayList<Problem> training){
		
		//Array for class evaluations.
		ArrayList<Double> c1 = new ArrayList<Double>();
		ArrayList<Double> c0 = new ArrayList<Double>();
		
		//Evaluates each problem on a tree and adds its value to whichever class it belongs too.
		for(Problem p:training){
			this.subInFeats(p.getFeatures());
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
		this.c0Mean = 0;
		this.c1Mean = 0;
		this.c0SD = 0;
		this.c1SD = 0;
		
		//Calculates the means and SD for the evaluations values found for each class.
		this.setC1Mean(calculateMean(c1));
		this.setC0Mean(calculateMean(c0));
		
		this.setC1SD(calculateStdDev(c1));
		this.setC0SD(calculateStdDev(c0));		
		
		//Calculates the fitness of the tree
		this.setFitness(this.calcFitness());
	}
	
	//Tests the tree against the test set.
	public void test(ArrayList<Problem> testing){

		int correct = 0;
		int incorrect = 0;
		int c1Total = 0;
		int c0Total = 0;
		int c1Correct = 0;
		int c0Correct = 0;
		
		for(Problem p:testing){
			int classification;
			this.subInFeats(p.getFeatures());
			double evaluation = evaluate(this.getHead());
			//Returns the class probability for each class
			double c0Prob = getClass(evaluation, this.c0Mean, this.getC0SD());
			double c1Prob = getClass(evaluation, this.c1Mean, this.getC1SD());
		
			//Whichever class has the highest probability is assigned to that problem.
			if(c0Prob > c1Prob){
				classification = 0;
			}else{
				classification = 1;
			}
			
			//Tacks the total number of predictions in each class.
			if(p.getClassification() == 0){
				c0Total ++;
			}else{
				c1Total ++;
			}
			
			//Checks the predicted class against the actual class to see if it was correct.
			if(classification == 0 && p.getClassification() == 0){
				correct++;
				c0Correct ++;
			}else if(classification == 1 && p.getClassification() == 1){ 
				correct++;
				c1Correct ++;
			}
			else{
				incorrect++;
			}
			
		}
		
		//Calcualtes the overall accuracy and the accuracy on each individual class.
		double total = correct+incorrect;
		double accuracy = (correct / total) * 100;
		double c1Acc = c1Correct;
		c1Acc = c1Acc / c1Total *100;
		double c0Acc = c0Correct;
		c0Acc = c0Acc / c0Total * 100;
		setAccuracy(accuracy);
		setAccuracyC1(c1Acc);
		setAccuracyC0(c0Acc);
	}
	
	//Predicts the class of a problem using the Guassian distribution method.
	public double getClass(double x, double mean, double SD){
		double value = 0;
		double left = 1/(SD*(Math.sqrt(2 * Math.PI)));
		double rightTop = Math.pow((x - mean),2) * -1;
		double rightBottom = 2*Math.pow(SD, 2);
		double right = rightTop / rightBottom;

		value = left * Math.exp(right);
		
		return value;
	}
	
	//Calculates fitness using the Fisher criterion
	public double calcFitness(){
		double fitness = 0;
		
		double top = Math.abs(this.getC0Mean() - this.getC1Mean()) + 1;
		double bottom = Math.sqrt(this.getC0SD() + this.getC1SD()) + 1;
		fitness = top / bottom;
		
		return fitness;
	}
	
	double calculateMean(ArrayList<Double> test) {
        double sum = 0.0;
        for(double a : test)
            sum += a;
        return sum/test.size();
    }

    double calculateVariance(ArrayList<Double> test) {
        double mean = calculateMean(test);
        double temp = 0;
        for(double a :test)
            temp += (a-mean)*(a-mean);
        return temp/(test.size()-1);
    }
    
    double calculateStdDev(ArrayList<Double> test) {
        return Math.sqrt(calculateVariance(test));
    }
	
	//Prints the tree using the terminal numeric values an shows what level each terminal is at.
	public void printTree() {
		Queue<Node> q = new LinkedList<Node>();
		q.add(this.getHead());
		while (!q.isEmpty()) {
			Node n = q.poll();
			if(n instanceof FunctionNode){
			System.out.println(n.getFunctionValue() + " : Level - " + n.getLevel());}
			else{
				System.out.println(n.getTerminalValue() + " (" + n.getFunctionValue() + " )" +  " : Level - " + n.getLevel());}	
			if (n.left != null) {
				q.add(n.left);// enqueue the left child
			}
			if (n.right != null) {
				q.add(n.right);// enque the right child
			}
		}
	}
	
	//Prints the tree in text form using the terminal lables not numeric values.
	public String printTreeText() {
		Queue<Node> q = new LinkedList<Node>();
		q.add(this.getHead());
		String tree = "";
		while (!q.isEmpty()) {
			Node n = q.poll();
			tree = tree.concat(n.getFunctionValue());
			tree = tree.concat(",");
			if (n.left != null) {
				q.add(n.left);// enqueue the left child
			}
			if (n.right != null) {
				q.add(n.right);// enque the right child
			}
		}
		return tree;
	}
	
	//Each terminal is listed as a label i.e "f1" "f32" these labels are substituted for their real values in this method. 
	public void subInFeats(ArrayList<Double> features) {
		Queue<Node> q = new LinkedList<Node>(); // A linked list to queue all of the nodes for substitution.
		q.add(this.getHead());// Adds the head of tree as starting node.
		while (!q.isEmpty()) { 
			Node n = q.poll();
			//Switch statement to find which terminal value should be subsititued in.
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
			//Adds child nodes to the queue.
			if (n.left != null) {
				q.add(n.left);// enqueue the left child
			}
			if (n.right != null) {
				q.add(n.right);// enque the right child
			}
		}
	}
	
	//Returns the evaluation of the tree using recursion.
	public double evaluate(Node node){
		if(node !=null){
			if(node instanceof TerminalNode){ //If a node is a terminal it returns its value.
				return node.getTerminalValue();
			}else{
				double left = evaluate(node.left); //Returns the evaluation of the left node. If its a function the evaluation method is called again.
				double right = evaluate(node.right); //Returns the evaluation of the right node. If its a function the evaluation method is called again.
				return calculate(left,node.getFunctionValue(), right); // Calculates the value a function node will give.
			}
		}
	return -1;
	}

	//Provides the calculation of a function node and its children
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
	case "SIN":
		sum = left * Math.sin(right);
		return sum;
	case "COS":
		sum = left * Math.cos(right);
		return sum;
	case "TAN":
		sum = left * Math.tan(right);
		return sum;
	case "LOG":
		sum = left * Math.log(right);
		return sum;
	case "EXP":
		sum = left * Math.exp(right);
		return sum;
	case "/":
		if(right == 0){ //Protects from division by 0
			right = 1;
		}
		sum = left / right;
	return sum;
	}
	
	return -1;
}

	//Getters and setters.
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

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getAccuracy2() {
		return accuracy2;
	}

	public void setAccuracy2(double accuracy2) {
		this.accuracy2 = accuracy2;
	}

	public double getAccuracyC1() {
		return accuracyC1;
	}

	public void setAccuracyC1(double accuracyC1) {
		this.accuracyC1 = accuracyC1;
	}

	public double getAccuracyC0() {
		return accuracyC0;
	}

	public void setAccuracyC0(double accuracyC0) {
		this.accuracyC0 = accuracyC0;
	}
	
}
