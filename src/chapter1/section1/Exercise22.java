package chapter1.section1;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise22 {

	public static void main(String[] args) {
		int arr[] = {1, 2, 3, 4, 5, 6, 7};
		int key = 2;
		
		Arrays.sort(arr);
		
		int index = rank(key, arr, 0, arr.length-1, 0);
		
		StdOut.println();
		StdOut.println("Index: " + index);

		StdOut.println("\nExpected:");
		StdOut.println("lo: 0 - hi: 6\n" +
				" lo: 0 - hi: 2\n" +
				"Index: 1");
	}
	
	private static int rank(int key, int[] arr, int lo, int hi, int depth) {
		
		if (depth != 0) {
			StdOut.println();
		}
		
		for (int i = 0; i < depth; i++) {
			StdOut.print(" ");
		}
		
		StdOut.print("lo: " + lo + " - hi: " + hi);
		
		if (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			
			if (key < arr[mid]) {
				return rank(key, arr, lo, mid-1, ++depth);
			} else if (key > arr[mid]) {
				return rank(key, arr, mid+1, hi, ++depth);
			} else {
				return mid;
			}
		} else {
			return -1;
		}
		
	}

}
