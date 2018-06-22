package GeneticProgramming;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GP {
	
	ArrayList<Tree> population = new ArrayList<Tree>();
	Parameters params = new Parameters();
	private static Random random = new Random();
	ArrayList<Problem> testData;
	ArrayList<Problem> trainingData;
	double bestFitness = 0;
	Tree best;
	
	public GP (){
		System.out.println("1");
		try {
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Crossover test
//		Tree a1 = new Tree();
//		a1.train(trainingData);
//		Tree a2 = new Tree(a1.getHead().copy(null));
//		Tree b1 = new Tree();
//		b1.train(trainingData);
//		Tree b2 = new Tree(b1.getHead().copy(null));
//		
//		System.out.println(" ");
//		System.out.println("Parent1: ");
//		a2.printTree();
//		System.out.println(" ");
//		System.out.println("Parent2: ");
//		b2.printTree();
//		System.out.println(" ");
//		System.out.println("Child: ");
//		Tree child = crossover(a2,b2);
//		child.printTree();
		
			
		populate();
		System.out.println("2");
		for(Tree t:population){
			t.train(trainingData);
			if(t.getFitness() > bestFitness){
				bestFitness = t.getFitness();
				best = t;
			}
		}
		System.out.println("3");
		for(int gen = 0; gen < Parameters.numberOfGenerations; gen++){
			System.out.println("gen");
		ArrayList<Tree> newPopulation = new ArrayList<Tree>();
		for(int i = 0; i < Parameters.popSize; i++){
			System.out.println("pop " + i);
			 //if ( random.nextDouble() < Parameters.crossoverProbability) {
			System.out.println("");
			System.out.println("Parent1: ");
			Tree parent1 = new Tree(selection(2).getHead().copy(null));
			parent1.printTree();
			System.out.println(" ");
			System.out.println("Parent2: ");
			Tree parent2 = new Tree(selection(2).getHead().copy(null));
			
			while(parent1 == parent2){
				parent2 = new Tree(selection(2).getHead().copy(null));
			}
			
			parent2.printTree();
			Tree child = crossover(parent1,parent2);
//			while(child.getHead().getMaxDepth() > Parameters.maximumRecombinationDepth){
//				child = crossover(parent1,parent2);
//			}
			System.out.println(" ");
			System.out.println("Child: ");
			child.printTree();
			      //}
//			      else {
//			    	parent1 = selection(2).copy(selection(2).getHead());
//			        child = mutation(parent1);
//			      }	
			        newPopulation.add(child);
		}
		
		population = newPopulation;
		System.out.println("3");
		int count = 0;
		for(Tree t:population){
			count++;
			System.out.println("here "+count);
			t.printTree();
			t.train(trainingData);
			if(t.getFitness() > bestFitness){
				bestFitness = t.getFitness();
				best = t;
			}
		}
		System.out.println("4");
		System.out.println(gen);
	}
		System.out.println("best: " + best.getFitness());
		best.printTree();
		best.test(testData);
		System.out.println("The best fitness found was: " + best.getAccuracy() + "%" );
	}
	
	private Tree mutation(Tree parent1) {
		Tree child;
		
		Node point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		System.out.println("Point: " + point1.getValue());
		child = parent1;
		int point1Idx = child.getHead().getAllNodes().indexOf(point1);
		Node point = child.getHead().getAllNodes().get(point1Idx);
		Node parent = point.getParentNode();
		Tree mutationTree = new Tree();
		
		mutationTree.printTree();
		
		if(parent.getLeft() == point){
			parent.setLeft(mutationTree.getHead());
		}else{
			parent.setRight(mutationTree.getHead());
		}
		
		return child;
	}

	private Tree crossover(Tree parent1, Tree parent2) {
		Tree child;
		Node point1;
		Node point2;
		if(parent1.getHead() instanceof TerminalNode){
			point1 = parent1.getHead();
		}else{
		point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		}
		if(parent2.getHead() instanceof TerminalNode){
			point2 = parent2.getHead();
		}else{
		point2 = parent2.getHead().getAllNodes().get(random.nextInt(parent2.getHead().getAllNodes().size()));
		}
		
		if (point1.getParentNode() == null) {
			point2.setParentNode(null);
			point2.resetLevels(point2);
			child = new Tree(point2);
			
		}else{
			
			child = parent1;
			
			
			if(point1.getParentNode().getLeft() == point1){
				point1.getParentNode().setLeft(point2);
				point1.getParentNode().getChildNodes().remove(point1);
				point1.getParentNode().getChildNodes().add(point2);
				point2.setParentNode(point1.getParentNode());
				point2.resetLevels(point2);
			}else{
				point1.getParentNode().setRight(point2);
				point1.getParentNode().getChildNodes().remove(point1);
				point1.getParentNode().getChildNodes().add(point2);
				point2.setParentNode(point1.getParentNode());
				point2.resetLevels(point2);
			}
		}	
		return child;
	}

	public Tree selection(int tnSize)
	{	
			ArrayList<Tree> tournament = new ArrayList<Tree>();
			Tree chosenParent = null;
			Random generator = new Random();
			double bestFitness = 0;
			
			for(int i = 0; i < tnSize; i++)
			{	
				tournament.add(this.population.get(generator.nextInt(this.population.size()-1)));
			}
			
			chosenParent = tournament.get(0);
			
			for(Tree t : tournament)
			{
				if(t.getFitness() > bestFitness)
				{
					bestFitness = t.getFitness();
					chosenParent = t;
				}
			}
			return chosenParent;
	}
		
	private void getData() throws IOException {
		testData = readData("test.csv");
		trainingData = readData("training.csv");
	}

	public void populate(){
			
			for(int i = 0; i < Parameters.popSize; i++){
			Tree individual = new Tree();
			population.add(individual);
			System.out.println(" ");
			System.out.println("new: ");
			individual.printTree();
			}
	}
	
	public ArrayList<Problem> readData(String filePath) throws IOException {
	    ArrayList<Problem> result = new ArrayList<Problem>();
	    Scanner scan = new Scanner(new File(filePath),"UTF-8");
	    while (scan.hasNextLine()) {
	    	ArrayList<Double> feats = new ArrayList<Double>();
	        String line = scan.nextLine();
	        String[] lineArray = line.split(",");
	        for(int i = 0; i < 58; i ++){
	        	feats.add(Double.parseDouble(lineArray[i]));
	        }
	        Problem prob = new Problem(feats, Integer.parseInt(lineArray[58]));
	        result.add(prob);
	        }
	        return result;
	    }
}
