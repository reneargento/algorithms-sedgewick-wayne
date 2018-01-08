package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise3 {

	// Parameters example: 4 7 4
	public static void main(String[] args) {
		int integer1 = Integer.parseInt(args[0]);
		int integer2 = Integer.parseInt(args[1]);
		int integer3 = Integer.parseInt(args[2]);

		isEqual(integer1, integer2, integer3);
	}
	
	private static void isEqual(int num1, int num2, int num3) {
		if (num1 == num2 && num2 == num3) {
			StdOut.println("Equal");
		} else {
			StdOut.println("Not equal");
		}
	}

}
