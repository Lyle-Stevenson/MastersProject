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
	
	public GP (){
		
		try {
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		populate();
	}
		
	private void getData() throws IOException {
		testData = readData("test.csv");
		trainingData = readData("training.csv");
	}

	public void populate(){

			Tree individual = new Tree();
			population.add(individual);
			individual.train(trainingData);
			System.out.print(individual.test(testData) + "% Accuracy");
		
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
