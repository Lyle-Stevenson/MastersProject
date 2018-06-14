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
	
	public GP (){
		
		
		
		populate();
	}
		
	public void populate(){

			Tree individual = new Tree();
			population.add(individual);
			individual.printTree();
			try {
				individual.subInFeats(readData("test.csv"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			individual.printTree();
			System.out.println("Value: " + individual.evaluate(individual.getHead()));
		
	}
	
	public ArrayList<Float> readData(String filePath) throws IOException {
	    ArrayList<Float> result = new ArrayList<Float>();
	    Scanner scan = new Scanner(new File(filePath),"UTF-8");
	    while (scan.hasNextLine()) {
	        String line = scan.nextLine();
	        String[] lineArray = line.split(",");
	        for(int i = 0; i < 58; i ++){
	        	result.add(Float.parseFloat(lineArray[i]));
	        }
	        
	        }
	        return result;
	    }
}
