package chapter1.section1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise38_BinarySearchXBruteForce {

	public static void main(String[] args) {
		
		List<Integer> arrayList = new ArrayList<>();
		int key = 760788;
		//int key = 760788333;
		
		int value = 0;
		
		List<String> lines = null;

		try {
			lines = Files.readAllLines(Paths.get(args[0]));
		} catch (IOException e) {
			StdOut.println(e.getMessage());
		}
		
		for (String line : lines) {
			value = Integer.parseInt(line.trim());
			
			arrayList.add(value);
		}
		
		int[] arr = arrayList.stream().mapToInt(i->i).toArray();
		
		//BRUTEFORCE
		long startTime = System.nanoTime();
		
		StdOut.println("Bruteforce: " + bruteForceSearch(key, arr));
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		
		StdOut.println("Duration: " + duration + " nanoseconds.");
		
		//BINARY SEARCH
		Arrays.sort(arr);
		
		startTime = System.nanoTime();
		
		StdOut.println("BinarySearch: " + binarySearch(key, arr, 0, arr.length-1));
		
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
