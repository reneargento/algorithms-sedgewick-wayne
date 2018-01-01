package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise26_SortingThreeNumbers {

	public static void main(String[] args) {
		int a = 99;
		int b = 10;
		int c = 5;
		int t = 0;
		
		if (a > b) { t = a; a = b; b = t; }
		if (a > c) { t = a; a = c; c = t; }
		if (b > c) { t = b; b = c; c = t; }
		
		StdOut.println("a: " + a);
		StdOut.println("b: " + b);
		StdOut.println("c: " + c);
	}

}
