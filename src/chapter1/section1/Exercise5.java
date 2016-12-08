package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

public class Exercise5 {

	public static void main(String[] args) {
		isBetween0And1(1.12, 1.33);
		isBetween0And1(0.2, 1);
	}
	
	private static void isBetween0And1(double x, double y) {
		if (x >= 0 && x <= 1 && y >= 0 && y <= 1 ) {
			StdOut.println("true");
		} else {
			StdOut.println("false");
		}
	}
	
}
