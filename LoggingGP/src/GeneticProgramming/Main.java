package GeneticProgramming;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		ArrayList<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i < 30; i++){
		GP system = new GP();
		results.add(system.outputArray());
		}
		try {
			outputFitness(results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void outputFitness(ArrayList<ArrayList<Double>> results) throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileWriter("N-EFS-1000gens.csv",true));
	    
	    
	    for(int i = 0; i < 1000; i++){
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
