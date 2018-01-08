package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento
 */
public class Exercise37_BadShuffling {
	
public static void main(String[] args) {
		
		int n = 200000;
		int m = 5;
		
		int[][] positions = new int[m][m];
		
		double[] arr = new double[m];
		arr[0] = 0;
		arr[1] = 1;
		arr[2] = 2;
		arr[3] = 3;
		arr[4] = 4;
		
		for (int i = 0; i < n; i++) {
			badShuffle(arr);
			
			for (int j = 0; j< arr.length; j++) {
				positions[j][(int)arr[j]]++;
			}
		}
		
		StdOut.println("Order after shuffles:");
		
		for (int i = 0; i < arr.length; i++) {
			StdOut.print(arr[i] + " ");
		}
		
		StdOut.println();
		
		printTable(positions);
		//Entries are close to N/M
	}
	
	public static void badShuffle(double[] a) {
		int n = a.length;
		
		for (int i = 0; i < n; i++) {
			int r = 0 + StdRandom.uniform(n - i);
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
