package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise7 {

	public static void main(String[] args) {
		funcA();
		funcB();
		funcC();
	}
	
	private static void funcA() {
		double t = 9.0;
		
		while (Math.abs(t - 9.0/t) > .001)
			t = (9.0/t + t) / 2.0;
		
		StdOut.printf("%.5f\n", t);
	}
	
	private static void funcB() {
		int sum = 0;
		
		for (int i = 1; i < 1000; i++) {
			for (int j = 0; j < i; j++) {
				sum++;
			}
		}
		
		StdOut.println(sum);
	}
	
	private static void funcC() {
		int sum = 0;
		
		for (int i = 1; i < 1000; i *= 2) {
			for (int j = 0; j < 1000; j++) {
				sum++;
			}
		}
		
		StdOut.println(sum);
	}
	
}
