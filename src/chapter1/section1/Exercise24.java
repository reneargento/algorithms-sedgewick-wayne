package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise24 {

	// Parameters example: 90 20
	public static void main(String[] args) {
		int gcd = gcd(105, 24);
				
		StdOut.println("GDC 1: " + gcd);
		
		StdOut.println();

		int argument1 = Integer.parseInt(args[0]);
		int argument2 = Integer.parseInt(args[1]);

		int gc2 = gcd(argument1, argument2);
		StdOut.println("GDC 2: " + gc2);

		StdOut.println();
		
		int gcd3 = gcd(1111111, 1234567);
		
		StdOut.println("GDC 3: " + gcd3);
	}
	
	private static int gcd(int p, int q) {
		
		StdOut.println("p: " + p + " - q: " + q);
		
		if (q == 0) {
			return p;
		} else {
			return gcd(q, p % q);
		}
	}

}
