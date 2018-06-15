package GeneticProgramming;

import java.util.ArrayList;

public class Problem {
	
	private ArrayList<Double> features = new ArrayList<Double>();
	private int classification;
	
	public Problem(ArrayList<Double> features, int classif){
		
		this.setFeatures(features);
		this.setClassification(classif);
	}
	
	
	public ArrayList<Double> getFeatures() {
		return features;
	}
	public void setFeatures(ArrayList<Double> features) {
		this.features = features;
	}
	public int getClassification() {
		return classification;
	}
	public void setClassification(int classification) {
		this.classification = classification;
	}
}
