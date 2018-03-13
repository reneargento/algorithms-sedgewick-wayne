package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise13 {
	
	public static void main(String[] args) {
		int[][] mat = {
				{1, 2, 3},
				{4, 5, 6}
		};
		
		transpose(mat);
		StdOut.println("\nExpected:");
		StdOut.println("1 4 \n" +
                "2 5 \n" +
                "3 6 ");
	}
	
	private static void transpose(int[][] mat) {
		
		int[][] newMat = new int[mat[0].length][mat.length];
		
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				newMat[j][i] = mat[i][j];
			}
		}
		
		print(newMat);
	}
	
	private static void print(int[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				StdOut.print(mat[i][j] + " ");
			}
			StdOut.println();
		}
	
	}
	
}
