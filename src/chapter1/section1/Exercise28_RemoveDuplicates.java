package chapter1.section1;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise28_RemoveDuplicates {

	public static void main(String[] args) {
		
		int[] whitelist = {1, 2, 3, 4, 5, 6, 6, 7, 7, 8};
		int[] keys = {1, 4 , 5, 9, 10};
		
		Arrays.sort(whitelist);
		
		int[] compactWhitelist = removeDuplicates(whitelist);
				
		binarySearch(compactWhitelist, keys);
		StdOut.println("\nExpected (numbers not found): 9, 10");
	}

	private static int[] removeDuplicates(int[] whitelist) {
		int[] newWhitelist = new int[whitelist.length];
		
		newWhitelist[0] = whitelist[0];
		int count = 0;
		
		for (int i = 1; i < whitelist.length; i++) {
			if (whitelist[i] != newWhitelist[count]) {
				count++;
				
				newWhitelist[count] = whitelist[i];
			}
		}
		count++;
		
		int[] compactNewWhitelist = new int[count];
		System.arraycopy(newWhitelist, 0, compactNewWhitelist, 0, count);
		
		return compactNewWhitelist;
	}
	
	private static void binarySearch(int[] whitelist, int[] keys) {
		
		int numbersCount = 0;
		
		for (int i = 0; i < keys.length; i++) {
			
			int index = rank(keys[i], whitelist, 0, whitelist.length-1);

			if (index == -1) {
				if (numbersCount != 0) {
					StdOut.print(", ");
				}
				
				StdOut.print(keys[i]);
				
				numbersCount++;
			}
		}
		
	}
	
	private static int rank(int key, int[] arr, int lo, int hi) {
		
		if (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			
			if (key < arr[mid]) {
				return rank(key, arr, lo, mid - 1);
			} else if (key > arr[mid]) {
				return rank(key, arr, mid + 1, hi);
			} else {
				return mid;
			}
		} else {
			return -1;
		}
		
	}
	
}
