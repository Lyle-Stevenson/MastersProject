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
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Crossover test
		// Tree a1 = new Tree();
		// a1.train(trainingData);
		// Tree a2 = new Tree(a1.getHead().copy(null));
		// Tree b1 = new Tree();
		// b1.train(trainingData);
		// Tree b2 = new Tree(b1.getHead().copy(null));
		//
		// System.out.println(" ");
		// System.out.println("Parent1: ");
		// a2.printTree();
		// System.out.println(" ");
		// System.out.println("Parent2: ");
		// b2.printTree();
		// System.out.println(" ");
		// System.out.println("Child: ");
		// Tree child = crossover(a2,b2);
		// child.printTree();

		populate();
		System.out.println(Parameters.maxDepth + " " + Parameters.maximumRecombinationDepth);
		for (Tree t : this.population) {
			t.train(this.trainingData);
			if (t.getFitness() > this.best.getFitness()) {
				this.best = new Tree(t.getHead().copy(null));
				this.best.setFitness(t.getFitness());
				this.start = new Tree(t.getHead().copy(null));
				this.start.setFitness(t.getFitness());
			}
		}
		for (int gen = 0; gen < Parameters.numberOfGenerations; gen++) {
			ArrayList<Tree> newPopulation = new ArrayList<Tree>();
			newPopulation.add(this.best);
			for (int i = 0; i < Parameters.popSize-1; i++) {
				Tree child;
				if (random.nextDouble() < Parameters.crossoverProbability) {
					Tree parent1 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));
					Tree parent2 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));

					while (parent1 == parent2) {
						parent2 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));
					}
					child = crossover(parent1, parent2);
				}	else {
					Tree parent1 = new Tree(selection(Parameters.tnmtSize).getHead().copy(null));
				child = mutation(parent1);
			}
				newPopulation.add(child);
			}

			this.population = newPopulation;
			for (Tree t : this.population) {
				t.train(this.trainingData);
				//System.out.println(t.getFitness() + " " + this.best.getFitness());
				if (t.getFitness() > this.best.getFitness()) {
					this.best = new Tree(t.getHead().copy(null));
					this.best.setFitness(t.getFitness());
				}
			}
			Results.add(this.best.getFitness());
			
		}
		this.best.test(testData);
		System.out.println(this.best.getHead().getMaxDepth());
		//this.best.test2(trainingData);
		/*System.out.println("Test: "+this.best.getAccuracy()+"%");
		System.out.println("Training: "+this.best.getAccuracy2()+"%");
		System.out.println("c0: "+this.best.getAccuracyC0()+"%");
		System.out.println("c1: "+this.best.getAccuracyC1()+"%");
		System.out.println("here: " + this.best.printTreeText());*/
		try {
			outputBest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Tree mutation(Tree parent1) {
		Tree child;

		Node point1;

		if (parent1.getHead() instanceof TerminalNode) {
			point1 = parent1.getHead();
		} else {
			point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}
		
		while(point1.getLevel() >= Parameters.maximumRecombinationDepth){
			point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}

		child = parent1;

		Tree mutationTree = new Tree(Parameters.maximumRecombinationDepth - (point1.getLevel()));

		if (point1.getParentNode() == null) {
			child = mutationTree;
		} else if (point1.getParentNode().getLeft() == point1) {
			point1.getParentNode().setLeft(mutationTree.getHead());
			point1.getParentNode().getChildNodes().remove(point1);
			point1.getParentNode().getChildNodes().add(mutationTree.getHead());
			mutationTree.getHead().setParentNode(point1.getParentNode());
			mutationTree.getHead().resetLevels(mutationTree.getHead());
		} else {
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

		if (parent1.getHead() instanceof TerminalNode) {
			point1 = parent1.getHead();
		} else {
			point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}

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

	public Tree selection(int tnSize) {
		ArrayList<Tree> tournament = new ArrayList<Tree>();
		Tree chosenParent = null;
		Random generator = new Random();
		double bestFitness = 0;

		for (int i = 0; i < tnSize; i++) {
			tournament.add(this.population.get(generator.nextInt(this.population.size() - 1)));
		}

		chosenParent = tournament.get(0);

		for (Tree t : tournament) {
			if (t.getFitness() > bestFitness) {
				bestFitness = t.getFitness();
				chosenParent = t;
			}
		}
		return chosenParent;
	}

	private void getData() throws IOException {
		testData = readData(Parameters.testingData);
		trainingData = readData(Parameters.trainingData);
	}

	public void populate() {

		for (int i = 0; i < Parameters.popSize; i++) {
			Tree individual = new Tree();
			population.add(individual);
		}
	}
	
	public void outputBest() throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileWriter("NezerBalanced-58feats-Bests.csv",true));
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
	    //sb.append('\n');

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