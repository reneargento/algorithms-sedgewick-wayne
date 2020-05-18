package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to alexrad89 (https://github.com/alexrad89), to shrutigupta23 (https://github.com/shrutigupta23),
// to thiendao1407 (https://github.com/thiendao1407) and to a-set (https://github.com/a-set) for suggesting improvements
// to this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/34
// Based on https://proofwiki.org/wiki/Integers_Coprime_to_Zero
public class Exercise30_ArrayExercise {

	public static void main(String[] args) {
		int n = 5;
        boolean[][] array = createCoprimeArray(n);
        printArray(array);
	}

	public static boolean[][] createCoprimeArray(int n) {
        boolean[][] array = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                array[i][j] = isCoprime(i, j);
            }
        }
        return array;
    }

    public static void printArray(boolean[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                StdOut.printf("%6b", array[i][j]);
            }
            StdOut.println();
        }
    }

	private static boolean isCoprime(int i, int j) {
		if (i == j && i != 1) {
			return false;
		}

		if (i == 0 || j == 0) {
		    return handleZero(i, j);
        }
		return gcd(i, j) == 1;
	}

	private static boolean handleZero(int i, int j) {
        return (i == 0 && j == 1)
                || (i == 0 && j == -1)
                || (j == 0 && i == 1)
                || (j == 0 && i == -1);
    }

	private static int gcd(int number1, int number2) {
		if (number2 == 0) {
			return number1;
		} else {
			return gcd(number2, number1 % number2);
		}
	}

}