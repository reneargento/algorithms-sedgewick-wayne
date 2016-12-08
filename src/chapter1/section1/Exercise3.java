package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

public class Exercise3 {

	public static void main(String[] args) {
		isEqual(1, 2, 3);
		isEqual(2, 2, 2);
	}
	
	private static void isEqual(int num1, int num2, int num3) {
		if (num1 == num2 && num2 == num3) {
			StdOut.println("Equal");
		} else {
			StdOut.println("Not equal");
		}
	}

}
