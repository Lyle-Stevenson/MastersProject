package GeneticProgramming;
import java.util.ArrayList;

public class Parameters {
	public final static int maxDepth = 4;//6
	public final static int maximumRecombinationDepth = 6;//17	
	public final static int popSize = 200;//1000
	public final static int tnmtSize = 5;//1000
	public static double crossoverProbability = 0.8;
	public static int numberOfGenerations = 1000;//50
	public static String testingData = "testingN-Balanced-Normalsied.csv";
	public static String trainingData = "trainingN-Balanced-Normalised.csv";
}
