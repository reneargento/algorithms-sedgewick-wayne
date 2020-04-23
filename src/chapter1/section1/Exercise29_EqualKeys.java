package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to YRFT (https://github.com/YRFT) for suggesting a simpler solution to this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/134
public class Exercise29_EqualKeys {

	public static void main(String[] args) {
		int[] array = {1, 2, 4, 4, 5, 6, 6, 7, 7, 7, 8};

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
		return lessThanKey(key, array, 0, array.length - 1);
	}

	private static int lessThanKey(int key, int[] array, int low, int high) {
        if (low <= high) {
            int middle = low + (high - low) / 2;

            if (key > array[middle]) {
                return lessThanKey(key, array, middle + 1, high);
            } else {
                return lessThanKey(key, array, low, middle - 1);
            }
        }
        return low;
    }

    private static int greaterThanKey(int key, int[] array, int low, int high) {
        if (low <= high) {
            int middle = low + (high - low) / 2;

            if (key < array[middle]) {
                return greaterThanKey(key, array, low, middle - 1);
            } else {
                return greaterThanKey(key, array, middle + 1, high);
            }
        }
        return array.length - high - 1;
    }

	public static int count(int key, int[] array) {
	    int lessThanKey = lessThanKey(key, array, 0, array.length - 1);
	    int greaterThanKey = greaterThanKey(key, array, 0, array.length - 1);
		return array.length - lessThanKey - greaterThanKey;
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
