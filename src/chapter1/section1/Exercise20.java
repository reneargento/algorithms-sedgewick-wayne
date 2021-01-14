package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to kyicy (https://github.com/kyicy) for showing a better way to compute the value of ln(N!).
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/188
public class Exercise20 {
	
	public static void main(String[] args) {
		StdOut.println("ln(5!) = " + lnFactorial(5));
		StdOut.println("Expected: 4.787491742782046");
	}

	private static double lnFactorial(int n) {
		if (n == 1) {
			return 0;
		}
		return Math.log(n) + lnFactorial(n - 1);
	}
}
