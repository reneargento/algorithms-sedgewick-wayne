package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise19 {

	public static void main(String[] args) {
//		for (int n = 0; n < 90; n++) {
//			StdOut.println(n + " - " + F(n));
//		}
		
		for (int n = 0; n < 90; n++) {
			int[] arr;
			
			if (n == 0 || n == 1) {
				arr = new int[2];
			} else {
				arr = new int[n+1];
			}
			
			arr[0] = 0;
			arr[1] = 1;
			
			StdOut.println(n + " - " + enhancedF(n, arr));
		}
	}
	
	private static int F(int n) {
		if (n == 0) return 0;
		if (n == 1) return 1;
		return F(n-1) + F(n-2);
	}
	
	private static int enhancedF(int n, int[] arr) {
		if (n == 0) return arr[0];
		if (n == 1) return arr[1];
		
		for (int i = 2; i <= n; i++) {
			arr[i] = arr[i-2] + arr[i-1];
		}
		
		return arr[n];
	}
}
