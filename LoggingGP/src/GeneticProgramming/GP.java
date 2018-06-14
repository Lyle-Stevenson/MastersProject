package GeneticProgramming;

import java.util.ArrayList;
import java.util.Random;

public class GP {
	
	ArrayList<Tree> population = new ArrayList<Tree>();
	Parameters params = new Parameters();
	private static Random random = new Random();
	
	public GP (){
		
		
		
		populate();
	}
		
	public void populate(){
		
		ArrayList<Float> feats = new ArrayList<Float>();
		
			for(int i = 0; i < 59; i++){	
				float j = random.nextFloat();
				feats.add(j);
			}
		
			Tree individual = new Tree();
			population.add(individual);
			individual.subInFeats(feats);
			individual.printTree();
			System.out.println("Value: " + individual.evaluate(individual.getHead()));
		
	}
}
