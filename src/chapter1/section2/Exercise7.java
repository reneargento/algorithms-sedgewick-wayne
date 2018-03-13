package chapter1.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise7 {

	public static void main(String[] args) {
		
		String s = "test";
		
		StdOut.println(mystery(s));
		StdOut.println("Expected: tset");
	}
	
	public static String mystery(String s) {
		int n = s.length();
		
		if (n <= 1) return s;
		String a = s.substring(0, n/2);
		String b = s.substring(n/2, n);
		return mystery(b) + mystery(a);
	}
	
}
