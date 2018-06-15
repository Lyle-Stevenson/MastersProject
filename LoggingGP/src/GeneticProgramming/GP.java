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
		
		try {
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Tree t = new Tree();
		Tree r = new Tree();
		
		t.train(trainingData);
		t.test(testData);
		r.train(trainingData);
		r.test(testData);
		
		System.out.println("Tree1:");
		t.printTree();
//		System.out.println("Tree2:");
//		r.printTree();
		
		Tree parent1 = t.copy(t.getHead().copy());
		Tree parent2 = r.copy(r.getHead());
		
//		Node point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
//		
//		int point = parent1.getHead().getAllNodes().indexOf(point1);
//		
//		Node parent = parent1.getHead().getAllNodes().get(point).getParentNode();
//		
//		parent.setLeft(new Node());
		
		parent1.getHead().getAllNodes().get(2).getParentNode().setRight(new Node());
		
		System.out.println("Child:");
		parent1.printTree();
		System.out.println("Tree1:");
		t.printTree();
		
//		System.out.println("Child:");
//		Tree child = crossover(parent1,parent2);
		
		//child.printTree();
		
//		for(int gen = 0; gen < Parameters.numberOfGenerations; gen++){
//		populate();
//		for(Tree t:population){
//			t.train(trainingData);
//			t.test(testData);
//			if(t.getFitness() > bestFitness){
//				bestFitness = t.getFitness();
//				best = t;
//			}
//		}
//		
//		Tree parent1;
//		Tree parent2;
//		Tree child;
//		ArrayList<Tree> newPopulation = new ArrayList<Tree>();
//		for(int i = 0; i < Parameters.popSize; i++){	
//			// if ( random.nextDouble() < Parameters.crossoverProbability) {
//			        parent1 = selection(2).copy(selection(2).getHead());
//			        parent1.printTree();
//			        parent2 = selection(2).copy(selection(2).getHead());
//			        parent2.printTree();
//			        child = crossover(parent1,parent2);
//			        child.printTree();
//			    //  }
//			   //   else {
//			    //	parent1 = selection(2).copy(selection(2).getHead());
//			   //    child = mutation(parent1);
//			    //  }	
//			        System.out.println("Add");
//			 newPopulation.add(child);
//		}
//		
//		population = newPopulation;
//		System.out.println(gen);
//	}
//	
//		System.out.println("The best fitness found was: " + best.getFitness() + "%" );
	}
	
	private Tree mutation(Tree parent1) {
		Tree child;
		
		Node point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		child = parent1;
		Node parentNode = point1.getParentNode();								
		int point1Idx = parentNode.getChildNodes().indexOf(point1);
		parentNode.getChildNodes().set(point1Idx, child.grow(parentNode, Parameters.maxDepth) );
		
		return child;
	}

	private Tree crossover(Tree parent1, Tree parent2) {
		Tree child;
		
		Node point1 = parent1.getHead().getAllNodes().get(random.nextInt(parent1.getHead().getAllNodes().size()));
		Node point2 = parent2.getHead().getAllNodes().get(random.nextInt(parent2.getHead().getAllNodes().size()));
		
		if (point1.getParentNode() == null) {
			child = parent2.copy(point2);		
		}else{
			child = parent1;
			int point1Idx = child.getHead().getAllNodes().indexOf(point1);
			Node point = child.getHead().getAllNodes().get(point1Idx);
			Node parent = point.getParentNode();
			if(parent.getLeft() == point){
				parent.setLeft(point2);
			}else{
				parent.setRight(point2);
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
