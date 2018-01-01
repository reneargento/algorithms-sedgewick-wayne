package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento
 */
public class Exercise35_DiceSimulation {

	public static void main(String[] args) {
		double[] distExact = diceSimulation();
		double[] distExperiment = diceExperiment();
		
		StdOut.println("Exact:");
		for (int i = 2; i < distExact.length; i++) {
			StdOut.printf("%5.3f ", distExact[i]);
		}
		
		StdOut.println();
		
		StdOut.println("Experiment:");
		for (int i = 2; i < distExperiment.length; i++) {
			StdOut.printf("%5.3f ", distExperiment[i]);
		}
	}
	
	private static double[] diceSimulation() {
		
		int SIDES = 6;
		double[] dist = new double[2*SIDES+1];
		for (int i = 1; i <= SIDES; i++) {
			for (int j = 1; j <= SIDES; j++) {
				dist[i+j] += 1.0;
			}
		}
		
		for (int k = 2; k <= 2 * SIDES; k++) {
			dist[k] /= 36.0;
		}
		
		return dist;
	}
	
	private static double[] diceExperiment() {
		int SIDES = 6;
		double[] distExperiment = new double[2 * SIDES + 1];
		
		int n = 6000000;
		
		int diceOne = 0;
		int diceTwo = 0;
		int sum = 0;
		
		for (int i = 0; i < n; i++) {
			diceOne = StdRandom.uniform(1, 7);
			diceTwo = StdRandom.uniform(1, 7);
			
			sum = diceOne + diceTwo;
			
			distExperiment[sum]++;
		}
		
		for (int k = 2; k <= 2 * SIDES; k++) {
			distExperiment[k] /= n;
		}
		
		return distExperiment;
	}

}
