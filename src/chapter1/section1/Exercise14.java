package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise14 {

	public static void main(String[] args) {
		StdOut.print(lg(15));
		StdOut.println("\nExpected: 3");
	}

	private static int lg(int n) {
		
		int logInt = 0;
		
		while (n > 0) {
			logInt++;
			
			n /= 2;
		}
		
		return logInt - 1;
	}
	
}
