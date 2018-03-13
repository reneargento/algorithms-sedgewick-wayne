package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise15 {

	public static void main(String[] args) {
		int[] a = {1, 2, 3, 4};
		
		int[] newArrA = histogram(a, 5);
		
		int[] b = {1, 2, 3, 9};
		
		int[] newArrB = histogram(b, 7);
		
		for (int i = 0; i < newArrA.length; i++) {
			StdOut.print(newArrA[i] + " ");
		}
		StdOut.println("\nExpected: 0 1 1 1 1");

		StdOut.println();
		
		for (int i = 0; i < newArrB.length; i++) {
			StdOut.print(newArrB[i] + " ");
		}
        StdOut.println("\nExpected: 0 1 1 1 0 0 0");
	}

	private static int[] histogram(int[] a, int m) {
		int[] newArr = new int[m];
		
		for (int i = 0; i < a.length; i++) {
			if (a[i] < m) {
				newArr[a[i]]++;
			}
		}
		
		return newArr;
	}
	
}
