package chapter1.section1;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise38_BinarySearchVersusBruteForce {

	public static void main(String[] args) {

		int key = 760788;

		// read the integers from a file
		In in = new In(args[0]);
		int[] arr = in.readAllInts();

		// sort the array
		Arrays.sort(arr);

		// BRUTEFORCE
		long startTime = System.nanoTime();

		StdOut.println("Bruteforce: " + bruteForceSearch(key, arr));

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);

		StdOut.println("Duration: " + duration + " nanoseconds.");

		// BINARY SEARCH
		startTime = System.nanoTime();

		StdOut.println("BinarySearch: " + binarySearch(key, arr, 0, arr.length - 1));

		endTime = System.nanoTime();
		duration = (endTime - startTime);

		StdOut.println("Duration: " + duration + " nanoseconds.");
	}

	private static int bruteForceSearch(int key, int[] arr) {

		if (arr == null) {
			throw new IllegalArgumentException();
		}

		int result = -1;

		for (int i = 0; i < arr.length; i++) {
			if (key == arr[i]) {
				result = i;
			}
		}

		return result;
	}

	private static int binarySearch(int key, int[] arr, int lo, int hi) {

		if (arr == null) {
			throw new IllegalArgumentException();
		}

		if (lo <= hi) {
			int mid = lo + (hi - lo) / 2;

			if (key < arr[mid]) {
				return binarySearch(key, arr, lo, mid - 1);
			} else if (key > arr[mid]) {
				return binarySearch(key, arr, mid + 1, hi);
			} else {
				return mid;
			}
		} else {
			return -1;
		}
	}

}
