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

		// Read the integers from a file
		In in = new In(args[0]);
		int[] array = in.readAllInts();

		// Sort the array
		Arrays.sort(array);

		// BRUTEFORCE
		long startTime = System.nanoTime();

		StdOut.println("Bruteforce: " + bruteForceSearch(key, array));

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);

		StdOut.println("Duration: " + duration + " nanoseconds.");

		// BINARY SEARCH
		startTime = System.nanoTime();

		StdOut.println("BinarySearch: " + binarySearch(key, array, 0, array.length - 1));

		endTime = System.nanoTime();
		duration = (endTime - startTime);

		StdOut.println("Duration: " + duration + " nanoseconds.");
	}

	private static int bruteForceSearch(int key, int[] array) {
		if (array == null) {
			throw new IllegalArgumentException();
		}

		int result = -1;

		for (int i = 0; i < array.length; i++) {
			if (key == array[i]) {
				result = i;
			}
		}

		return result;
	}

	private static int binarySearch(int key, int[] array, int low, int high) {
		if (array == null) {
			throw new IllegalArgumentException();
		}

		if (low <= high) {
			int middle = low + (high - low) / 2;

			if (key < array[middle]) {
				return binarySearch(key, array, low, middle - 1);
			} else if (key > array[middle]) {
				return binarySearch(key, array, middle + 1, high);
			} else {
				return middle;
			}
		} else {
			return -1;
		}
	}

}