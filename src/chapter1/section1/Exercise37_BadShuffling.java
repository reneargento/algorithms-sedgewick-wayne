package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento
 */
// Thanks to ccumulatio (https://github.com/ccumulatio) for mentioning that the arrays should be reinitialized at each
// loop iteration: https://github.com/reneargento/algorithms-sedgewick-wayne/issues/33
// Thanks to thiendao1407 (https://github.com/thiendao1407) for fixing a bug on the code to display the position counts
// and in the shuffle method: https://github.com/reneargento/algorithms-sedgewick-wayne/issues/73
public class Exercise37_BadShuffling {

	// Parameters example: 200000 5
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);

		int[][] positions = new int[m][m];

		double[] array = new double[m];

		for (int i = 0; i < n; i++) {
			initializeArray(array);
			badShuffle(array);

			for (int j = 0; j < array.length; j++) {
				positions[(int) array[j]][j]++;
			}
		}

		printTable(positions);
	}

	private static void initializeArray(double[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
	}

	public static void badShuffle(double[] array) {
		int n = array.length;

		for (int i = 0; i < n; i++) {
			int randomIndex = StdRandom.uniform(n);


			double temp = array[i];
			array[i] = array[randomIndex];
			array[randomIndex] = temp;
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