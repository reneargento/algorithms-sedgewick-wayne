package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise20 {
	
	public static void main(String[] args) {
		StdOut.println(Math.log(factorial(5)));
		StdOut.println("Expected: 4.787491742782046");
	}
	
	private static int factorial(int n) {
		if (n == 1) {
			return 1;
		} else {
			return n * factorial(n-1);
		}
	}
	
}
