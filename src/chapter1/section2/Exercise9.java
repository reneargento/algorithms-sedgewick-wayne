package chapter1.section2;

import java.util.Arrays;

import edu.princeton.cs.algs4.Counter;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise9 {

	public static void main(String[] args) {
		
		int[] whitelist = {2, 10, 3, 6, 5, 4, 7, 1, 9, 8};
		
		int[] keys = {10, 12, 5};
		
		Counter counter = new Counter("Operations");
		
		Arrays.sort(whitelist);
		
		for (int i = 0; i < keys.length; i++) {
			
			if (rank(keys[i], whitelist, counter) == -1) {
				StdOut.println(keys[i]);
			}
		}
		
		StdOut.println(counter);
	}
	
	private static int rank(int key, int[] a, Counter counter) {
		int lo = 0;
		int hi = a.length - 1;
		
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			counter.increment();
			
			if (key < a[mid]) {
				hi = mid - 1;
			} else if (key > a[mid]) {
				lo = mid + 1;
			} else {
				return mid;
			}
		}
		return -1;
	}
	
}
