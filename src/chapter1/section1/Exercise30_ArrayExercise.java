package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to alexrad89 (https://github.com/alexrad89), to shrutigupta23 (https://github.com/shrutigupta23)
// and thiendao1407 (https://github.com/thiendao1407) for suggesting improvements to this exercise.
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
		if (i == j && i != 1) {
			return false;
		}
		return i == 0 || j == 0 || gcd(i, j) == 1;
	}

	private static int gcd(int number1, int number2) {
		if (number2 == 0) {
			return number1;
		} else {
			return gcd(number2, number1 % number2);
		}
	}

}