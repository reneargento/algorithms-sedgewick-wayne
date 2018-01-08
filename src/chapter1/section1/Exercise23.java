package chapter1.section1;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise23 {
	
	public static void main(String[] args) {
		
		int[] arr = {1, 2, 3, 4, 5, 6, 7};
		int[] numbers = {1, 4 , 5, 9, 10};
		
		Arrays.sort(arr);
				
		binarySearch(arr, numbers, '+');
		
		StdOut.println();
		binarySearch(arr, numbers, '-');

		StdOut.println("\n\nExpected:");
		StdOut.println("Numbers NOT in whitelist:\n" +
                "9, 10\n" +
                "Numbers IN whitelist:\n" +
                "1, 4, 5");
	}
	
	private static void binarySearch(int[] arr, int[] numbers, char operation) {
		
		if (operation == '+'){
			StdOut.println("Numbers NOT in whitelist:");
		} else {
			StdOut.println("Numbers IN whitelist:");
		}
		
		int numbersCount = 0;
		
		for (int i = 0; i < numbers.length; i++) {
			
			int index = rank(numbers[i], arr, 0, arr.length-1);

			if (operation == '+' && index == -1) {
				if (numbersCount != 0) {
					StdOut.print(", ");
				}
				
				StdOut.print(numbers[i]);
				
				numbersCount++;
			} else if (operation == '-' && index != -1) {
				if (numbersCount != 0) {
					StdOut.print(", ");
				}
				
				StdOut.print(numbers[i]);
				
				numbersCount++;
			}
		}
		
	}
	
	private static int rank(int key, int[] arr, int lo, int hi){
		
		if (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			
			if (key < arr[mid]) {
				return rank(key, arr, lo, mid-1);
			} else if (key > arr[mid]) {
				return rank(key, arr, mid+1, hi);
			} else {
				return mid;
			}
		} else {
			return -1;
		}
		
	}
	
}
