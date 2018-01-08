package chapter1.section1;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise29_EqualKeys {
	
public static void main(String[] args) {
		
		int[] whitelist = {1, 2, 4, 4, 5, 6, 6, 7, 7, 7, 8};
		int[] keys = {1, 4, 5, 9, 10};
		
		Arrays.sort(whitelist);
		
		StdOut.println(rank(3, whitelist));
		StdOut.println(rank(5, whitelist));
		
		StdOut.println();
		
		StdOut.println(count(5, whitelist));
		StdOut.println(count(7, whitelist));
		StdOut.println(count(20, whitelist));
		
		StdOut.println("Verification: " + verify(7, whitelist) + " Expected: true");
		//binarySearch(whitelist, keys);
	}
	
	private static void binarySearch(int[] whitelist, int[] keys) {
		
		int numbersCount = 0;
		
		for (int i = 0; i < keys.length; i++) {
			
			int index = rank(keys[i], whitelist, 0, whitelist.length-1, false);

			if (index == -1) {
				if (numbersCount != 0) {
					StdOut.print(", ");
				}
				
				StdOut.print(keys[i]);
				
				numbersCount++;
			}
		}
	}
	
	private static int rank(int key, int[] arr, int lo, int hi, boolean count){
		boolean found = false;
		int mid = -1;
		
		if (lo <= hi) {
			mid = lo + (hi - lo) / 2;
			
			if (key < arr[mid]) {
				return rank(key, arr, lo, mid-1, count);
			} else if (key > arr[mid]){
				return rank(key, arr, mid+1, hi, count);
			} else {
				found = true;
			}
		} 
		
		if (found) {
			//Go to the first occurrence of key in the array
			while (--mid >= 0 && arr[mid] == key);
			
			//Return the position of the first occurrence of the element
			return mid + 1;
		} else {
			if (count) {
				//If we are counting, we need to know that the element does not exist
				return -1;
			} else {
				//Return the position that the element would be in (which is equal to the number of elements smaller than the key)
				return lo;
			}
		}
		
	}
	
	private static int rank (int key, int[] arr) {
		//Find the number of elements that are smaller than the key
		
		int index = rank(key, arr, 0, arr.length-1, false);
		return index;
	}
	
	private static int count(int key, int[] arr) {
		
		int index = rank(key, arr, 0, arr.length-1, true);
		
		int count = 0;
		
		if (index != -1) {
			for (int i = index; i < arr.length && arr[i] == key; i++) {
				count++;
			}
		}
		
		return count;
	}
	
	private static boolean verify(int key, int[] arr) {
		
		boolean verification = false;
				
		int indexFromRank = rank(key, arr);
		int count = count(key, arr);
		
		for (int i = indexFromRank; i < indexFromRank + count - 1; i++) {
			if (arr[i] != key) {
				verification = false;
				break;
			} else {
				verification = true;
			}
		}
		
		return verification;
	}

}
