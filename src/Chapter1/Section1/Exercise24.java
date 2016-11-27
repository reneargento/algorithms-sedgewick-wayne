package Chapter1.Section1;

import edu.princeton.cs.algs4.StdOut;

public class Exercise24 {

	public static void main(String[] args) {
		int gcd = gcd(105, 24);
				
		StdOut.print("GDC: " + gcd);
		
		StdOut.println();
		StdOut.println();
		
		int gcd2 = gcd(1111111, 1234567);
		
		StdOut.print("GDC: " + gcd2);
	}
	
	private static int gcd(int p, int q) {
		
		StdOut.println("p: " + p + " - q: " + q);
		
		if (q == 0) {
			return p;
		} else {
			return gcd(q, p%q);
		}
	}

}
