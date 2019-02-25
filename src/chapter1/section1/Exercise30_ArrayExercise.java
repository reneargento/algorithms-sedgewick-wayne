package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to alexrad89 (https://github.com/alexrad89) for suggesting an improvement on this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/34
public class Exercise30_ArrayExercise {

	public static void main(String[] args) {
		
		int n = 5;
		
		boolean[][] array = new boolean[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				array[i][j] = isCoprime(i, j);
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				StdOut.print(array[i][j] + " ");
			}
			StdOut.println();
		}
	}
	
	private static boolean isCoprime(int i, int j) {
		if (i == j) {
			return false;
		}

		boolean isPrime = true;
		
		int smallestValue = (i < j)? i : j;
		
		for (int k = 2; k <= smallestValue; k++) {
			
			if (i % k == 0 && j % k == 0) {
				isPrime = false;
				break;
			}
			
		}
		return isPrime;
	}

}
