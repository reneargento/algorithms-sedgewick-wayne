package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise30_ArrayExercise {

	public static void main(String[] args) {
		
		int n = 5;
		
		boolean[][] array = new boolean[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				array[i][j] = isPrime(i, j);
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				StdOut.print(array[i][j] + " ");
			}
			StdOut.println();
		}
	}
	
	private static boolean isPrime(int i, int j) {
		
		boolean isPrime = true;
		
		int biggestValue = (i >= j)? i : j;
		
		for (int k = 2; k <= biggestValue; k++) {
			
			if (i % k == 0 && j % k == 0){
				isPrime = false;
				break;
			}
			
		}
		return isPrime;
	}

}
