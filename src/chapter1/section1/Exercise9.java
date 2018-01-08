package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise9 {
	
	public static void main(String[] args) {
		StdOut.println(intToBinary(32));
		StdOut.println("Expected: 100000");
	}
	
	private static String intToBinary(int n) {
		String result = "";
		
		while (n > 0) {
			result = n % 2 + result;
			
			n /= 2;
		}
		
		return result;
	}
	
}
