package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento
 */
public class Exercise36_EmpiricalShuffleCheck {

	// Parameters example: 200000 5
	public static void main(String[] args) {

		int n = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);
		
		int[][] positions = new int[m][m];
		
		double[] arr = new double[m];
		arr[0] = 0;
		arr[1] = 1;
		arr[2] = 2;
		arr[3] = 3;
		arr[4] = 4;
		
		for (int i=0; i < n; i++) {
			shuffle(arr);
			
			for (int j=0; j< arr.length; j++) {
				positions[j][(int)arr[j]]++;
			}
		}
		
		printTable(positions);
		//Entries are close to N/M
	}
	
	public static void shuffle(double[] a) {
		int n = a.length;
		
		for (int  i= 0; i < n; i++) {
			int r = i + StdRandom.uniform(n - i);
			double temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
	}
	
	private static void printTable(int[][] positions) {
		
		StdOut.println("TABLE");
		
		for (int i = 0; i < positions.length; i++) {
			StdOut.printf("%3d  ", i);
			
			for (int j = 0; j < positions[0].length; j++) {
				StdOut.printf("%4d ", positions[i][j]);
			}
			StdOut.println();
		}
	}

}
