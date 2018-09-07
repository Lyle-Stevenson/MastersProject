package GeneticProgramming;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		ArrayList<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>(); //Holds the best fitness found for each run
		for(int i = 0; i < Parameters.numberOfRuns; i++){
			GP system = new GP(); //Creates a new instance of the gp system.
			System.out.println(i);//Prints to keep track of what run the system is on
			results.add(system.outputArray());//Adds the best fitness to results array.
		}
		try {
			outputFitness(results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//Outputs the best fitness found for each run of the program.
	public static void outputFitness(ArrayList<ArrayList<Double>> results) throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileWriter("A-NewFitness-balanced-58feats-Ntest.csv",true));
	    
	    for(int i = 0; i < Parameters.popSize; i++){
	    	StringBuilder sb = new StringBuilder();
	    	for(int k = 0; k < 30; k++){
	    	    sb.append(results.get(k).get(i));
	    	    sb.append(",");
	    	}
	    	sb.append("\n");
	    	pw.write(sb.toString());
	    	
	    }
	    pw.close();
	}

}
