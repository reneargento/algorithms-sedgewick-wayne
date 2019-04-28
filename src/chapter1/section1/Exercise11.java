package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise11 {
	
	public static void main(String[] args) {
		boolean[][] array = {{true, false, true},
							 {false, true, false}};
		printArray(array);
	}
	
	private static void printArray(boolean[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				
				if (array[i][j]) {
					StdOut.print("*");
				} else {
					StdOut.print(" ");
				}
				
			}
			StdOut.println();
		}
		
	}
	
}
