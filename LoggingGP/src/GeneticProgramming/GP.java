package GeneticProgramming;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GP {

	ArrayList<Tree> population = new ArrayList<Tree>();
	ArrayList<Double> Results = new ArrayList<Double>();
	Parameters params = new Parameters();
	private static Random random = new Random();
	ArrayList<Problem> testData;
	ArrayList<Problem> trainingData;
	Tree best = new Tree();
	Tree start = new Tree();

	public GP() {
		try {
			getData(); // Reads in the train and test set from file.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		populate();// Populates the initial populations using ramped half and half.
		for (Tree t : this.population) {
			t.train(this.trainingData); //Trains each tree on the training set.
			if (t.getFitness() > this.best.getFitness()) { // Keeps tack of the best tree in the population.
				this.best = new Tree(t.getHead().copy(null));
				this.best.setFitness(t.getFitness());
				this.start = new Tree(t.getHead().copy(null));
				this.start.setFitness(t.getFitness());
			}
		}
		//Evoluation in each generation
		for (int gen = 0; gen < Parameters.numberOfGenerations; gen++) {
			ArrayList<Tree> newPopulation = new ArrayList<Tree>(); //Creates an array of the new populations being created.
			newPopulation.add(this.best);// Elitism to add the best from previous pop to new pop
			for (int i = 0; i < Parameters.popSize-1; i++) {
				Tree child;
				if (random.nextDouble() < Parameters.crossoverProbability) { // Checks whether crossover or mutation operator will be used.
					//Tournament to select two parents and create copies of them for use.
					Tree parent1 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));
					Tree parent2 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));
					while (parent1 == parent2) { // If same parent is selected twice the select a new parent.
						parent2 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));
					}
					child = crossover(parent1, parent2); //Cross over two parents to create one child.
				}	else { //If mutation is selected select one parent to mutate.
					Tree parent1 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));
				child = mutation(parent1);
			}
				child.train(trainingData); //Train the resulting child on the trainin set
				newPopulation.add(child); //Adds child to new population.
		}

			this.population = newPopulation;//Overwrites old population with new one
			for (Tree t : this.population) { //Checks for the best child in the population.
				if (t.getFitness() > this.best.getFitness()) {
					this.best = new Tree(t.getHead().copy(null));
					this.best.setFitness(t.getFitness());
					this.best.setC0Mean(t.getC0Mean());
					this.best.setC1Mean(t.getC1Mean());
					this.best.setC0SD(t.getC0SD());
					this.best.setC1SD(t.getC1SD());
				}
			}
			Results.add(this.best.getFitness()); //Adds the best from current generation to a results arry for output.
		}
		this.best.test(testData); // Tests the best tree found on testing data.
		try {
			outputBest();//Outputs details of best tree found
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Mutation
	private Tree mutation(Tree parent1) {
		Tree child;
		Node point1;
		
		if (parent1.getHead() instanceof TerminalNode) {//If the tree is a single terminl node then it is selected as the mutation point
			point1 = parent1.getHead();
		} else {//Otherwise a random point in the tree is selected from all the nodes in the tree.
			point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}
		
		//If the selected muation node is at the level of the max recombination set then a new point is found.
		while(point1.getLevel() >= Parameters.maximumRecombinationDepth){
			point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}

		child = parent1;
		Tree mutationTree = new Tree(Parameters.maximumRecombinationDepth - (point1.getLevel())); //Mutation tree is formed from the point

		//If the point chose was the head node of the tree then new tree is the mutation tree.
		if (point1.getParentNode() == null) {
			child = mutationTree;
		} else if (point1.getParentNode().getLeft() == point1) { //If the point selected is the left node of its parent then replace left node with mutation tree. 
			point1.getParentNode().setLeft(mutationTree.getHead());
			point1.getParentNode().getChildNodes().remove(point1);
			point1.getParentNode().getChildNodes().add(mutationTree.getHead());
			mutationTree.getHead().setParentNode(point1.getParentNode());
			mutationTree.getHead().resetLevels(mutationTree.getHead());
		} else {//If the point selected is the right node of its parent then replace left node with mutation tree. 
			point1.getParentNode().setRight(mutationTree.getHead());
			point1.getParentNode().getChildNodes().remove(point1);
			point1.getParentNode().getChildNodes().add(mutationTree.getHead());
			mutationTree.getHead().setParentNode(point1.getParentNode());
			mutationTree.getHead().resetLevels(mutationTree.getHead());
		}
		return child;
	}

	private Tree crossover(Tree parent1, Tree parent2) {
		Tree child;
		Node point1;
		Node point2;

		if (parent1.getHead() instanceof TerminalNode) {//If the tree is a single terminal node then it is selected as the crossover point point
			point1 = parent1.getHead();
		} else {//Otherwise a random point in the tree is selected from all the nodes in the tree.
			point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}

		//If the selected crossover node node is at the level of the max recombination set then a new point is found.
		while(point1.getLevel() >= Parameters.maximumRecombinationDepth){
			point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}
		
		if (parent2.getHead() instanceof TerminalNode) {
			point2 = parent2.getHead();
		} else {
			point2 = parent2.getHead().getAllNodes().get(random.nextInt(parent2.getHead().getAllNodes().size()));
		}

		while (point1.getLevel() + point2.getMaxDepth() > Parameters.maximumRecombinationDepth) {
			point2 = parent2.getHead().getAllNodes().get(random.nextInt(parent2.getHead().getAllNodes().size()));
		}

		if (point1.getParentNode() == null) {
			point2.setParentNode(null);
			point2.resetLevels(point2);
			child = new Tree(point2);

		} else {

			child = parent1;

			if (point1.getParentNode().getLeft() == point1) {
				point1.getParentNode().setLeft(point2);
				point1.getParentNode().getChildNodes().remove(point1);
				point1.getParentNode().getChildNodes().add(point2);
				point2.setParentNode(point1.getParentNode());
				point2.resetLevels(point2);
			} else {
				point1.getParentNode().setRight(point2);
				point1.getParentNode().getChildNodes().remove(point1);
				point1.getParentNode().getChildNodes().add(point2);
				point2.setParentNode(point1.getParentNode());
				point2.resetLevels(point2);
			}
		}
		return child;
	}

	//Torunament selection
	public Tree selection(int tnSize) {
		ArrayList<Tree> tournament = new ArrayList<Tree>(); //Array list to hold trees in the tournament.
		Tree chosenParent = null;
		Random generator = new Random();
		double bestFitness = 0;

		//Selects the tournmant size amount of individuals from the population.
		for (int i = 0; i < tnSize; i++) {
			tournament.add(this.population.get(generator.nextInt(this.population.size() - 1)));
		}

		chosenParent = tournament.get(0); //First in the tournament is automatically selected as parent.
		bestFitness = chosenParent.getFitness();
		
		//Rest of tournament individuals are compared to see who is fitter.
		for (Tree t : tournament) {
			if (t.getFitness() > bestFitness) {
				bestFitness = t.getFitness();
				chosenParent = t;
			}
		}
		return chosenParent; 
	}

	//Fetches the training and testing dta from the files.
	private void getData() throws IOException {
		testData = readData(Parameters.testingData);
		trainingData = readData(Parameters.trainingData);
	}

	//Populates using ramped half and half
	public void populate() {

		//First half are generated using grow method
		for (int i = 0; i < Parameters.popSize/2; i++) {
			Tree individual = new Tree();
			population.add(individual);
		}
		
		//Second half are generated using full method
		for (int i = Parameters.popSize/2; i < Parameters.popSize; i++) {
			Tree individual = new Tree("Full");
			population.add(individual);
		}
	}
	
	// Outputs the best individuals data to a file.
	public void outputBest() throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileWriter("Bests.csv",true));
	    StringBuilder sb = new StringBuilder();
	    sb.append('\n');
	    sb.append(this.best.getFitness());
	    sb.append(',');
	    sb.append(this.best.getC0Mean());
	    sb.append(',');
	    sb.append(this.best.getC1Mean());
	    sb.append(',');
	    sb.append(this.best.getC0SD());
	    sb.append(',');
	    sb.append(this.best.getC1SD());
	    sb.append(',');
	    sb.append(this.best.getAccuracyC0());
	    sb.append(',');
	    sb.append(this.best.getAccuracyC1());
	    sb.append(',');
	    sb.append(this.best.getAccuracy());

	    pw.write(sb.toString());
	    pw.close();
	}	
	
	public ArrayList<Double> outputArray()
	{
		return this.Results;
	}

	public ArrayList<Problem> readData(String filePath) throws IOException {
		ArrayList<Problem> result = new ArrayList<Problem>();
		Scanner scan = new Scanner(new File(filePath), "UTF-8");
		while (scan.hasNextLine()) {
			ArrayList<Double> feats = new ArrayList<Double>();
			String line = scan.nextLine();
			String[] lineArray = line.split(",");
			for (int i = 0; i < 58; i++) {
				feats.add(Double.parseDouble(lineArray[i]));
			}
			Problem prob = new Problem(feats, Integer.parseInt(lineArray[58]));
			result.add(prob);
		}
		return result;
	}
}