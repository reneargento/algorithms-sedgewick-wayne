package chapter1.section1;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise29_EqualKeys {

	public static void main(String[] args) {
		int[] array = {1, 2, 4, 4, 5, 6, 6, 7, 7, 7, 8};

		Arrays.sort(array);

		StdOut.println("Rank: " + rank(3, array) + " Expected: 2");
		StdOut.println("Rank: " + rank(5, array) + " Expected: 4");
		StdOut.println();

		StdOut.println("Count: " + count(5, array) + " Expected: 1");
		StdOut.println("Count: " + count(7, array) + " Expected: 3");
		StdOut.println("Count: " + count(20, array) + " Expected: 0");

		StdOut.println("Verification: " + verify(7, array) + " Expected: true");
	}

	// Find the number of elements that are smaller than the key
	public static int rank (int key, int[] array) {
		return rank(key, array, 0, array.length - 1);
	}

	private static int rank(int key, int[] array, int low, int high) {
		if (low <= high) {
			int middle = low + (high - low) / 2;

			if (key < array[middle]) {
				return rank(key, array, low, middle - 1);
			} else if (key > array[middle]) {
				int rightIndex = rank(key, array, middle + 1, high);
				if (rightIndex == -1) {
					return middle + 1;
				} else {
					return rightIndex;
				}
			} else {
				int leftIndex = rank(key, array, low, middle - 1);
				if (leftIndex == -1) {
					return middle;
				} else {
					return leftIndex;
				}
			}
		}
		return -1;
	}

	public static int count(int key, int[] array) {
		int firstOccurrence = getIndex(key, array, 0, array.length - 1, true);

		if (firstOccurrence == -1) {
			return 0;
		}
		int lastOccurrence = getIndex(key, array, 0, array.length - 1, false);
		return lastOccurrence - firstOccurrence + 1;
	}

	private static int getIndex(int key, int[] array, int low, int high, boolean firstOccurrence) {
		if (low <= high) {
			int middle = low + (high - low) / 2;

			if (key < array[middle]) {
				return getIndex(key, array, low, middle - 1, firstOccurrence);
			} else if (key > array[middle]) {
				return getIndex(key, array, middle + 1, high, firstOccurrence);
			} else {
				int index;
				if (firstOccurrence) {
					index = getIndex(key, array, low, middle - 1, true);
				} else {
					index = getIndex(key, array, middle + 1, high, false);
				}
				if (index == -1) {
					return middle;
				} else {
					return index;
				}
			}
		}
		return -1;
	}
	
	private static boolean verify(int key, int[] array) {
		boolean verification = false;
				
		int indexFromRank = rank(key, array);
		int count = count(key, array);
		
		for (int i = indexFromRank; i < indexFromRank + count; i++) {
			if (array[i] != key) {
				verification = false;
				break;
			} else {
				verification = true;
			}
		}
		
		return verification;
	}

}
