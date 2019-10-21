package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise33_Matrix_Operations {

	public static void main(String[] args) {
		// Vector dot product
		double[] x = {2.0, 3.0, 4.0};
		double[] y = {3.0, 2.0, 5.5};

		StdOut.println("Dot: " + dot(x,y));

		// Matrix-matrix product
		double[][] a = {
				{1, 2}
		};
		double[][] b = {
				{2, 3, 4},
				{2, 3, 4}
		};
		double[][] c = mult(a, b);

		StdOut.println("\nMatrix multiplication:");
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				StdOut.print(c[i][j] + " ");
			}
			StdOut.println();
		}

		// Transpose
		double[][] d = {
				{1, 2, 3},
				{4, 5, 6}
		};

		double[][] e = transpose(d);

		StdOut.println("\nTranspose:");
		for (int i = 0; i < e.length; i++) {
			for (int j = 0; j < e[0].length; j++) {
				StdOut.print(e[i][j] + " ");
			}
			StdOut.println();
		}

		// Matrix-vector product
		double[][] f = {
				{1, 2, 3},
				{4, 5, 6}
		};

		double[] g = {1, 2, 3};

		double[] h = mult(f, g);

		StdOut.println("\nMatrix-vector product:");
		for (int i = 0; i < h.length; i++) {
			StdOut.print(h[i] + " ");
		}

		StdOut.println();
		// Vector-matrix product
		double[] i = {1, 2};

		double[][] j = {
				{1, 2, 3},
				{4, 5, 6}
		};

		double[] k = mult(i, j);

		StdOut.println("\nVector-matrix product:");
		for (int l = 0; l < k.length; l++) {
			StdOut.print(k[l] + " ");
		}
	}

	// Vector dot product
	private static double dot(double[] x, double[] y) {

		if (x == null || y == null || x.length != y.length) {
			throw new IllegalArgumentException();
		}

		double result = 0;

		for (int i = 0; i < x.length; i++) {
			result += x[i] * y[i];
		}

		return result;
	}

	// Matrix-matrix product
	private static double[][] mult(double[][] a, double[][] b) {

		// To multiply matrixes the number of columns in the first matrix must be the same
		// as the number of rows on the second matrix
		if (a == null || b == null || a.length == 0 || b.length == 0 || a[0].length != b.length) {
			throw new IllegalArgumentException();
		}

		// Result will always have the same number of rows as the first matrix and
		// the same number of columns as the second matrix
		double[][] c = new double[a.length][b[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				for (int k = 0; k < a[0].length; k++) {
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}

		return c;
	}

	// Transpose
	private static double[][] transpose(double[][] a) {

		if (a == null) {
			throw new IllegalArgumentException();
		}

		if (a.length == 0) {
			return a;
		}

		double[][] b = new double[a[0].length][a.length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				b[j][i] = a[i][j];
			}
		}

		return b;
	}

	// Matrix-vector product
	private static double[] mult(double[][] a, double[] x) {

		// Number of columns in matrix must be the same as the number of elements in vector
		if (a == null || x == null || a[0].length != x.length) {
			throw new IllegalArgumentException();
		}

		double[] b = new double[a.length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < x.length; j++) {
				b[i] += a[i][j] * x[j];
			}
		}

		return b;
	}

	// Vector-matrix product
	private static double[] mult(double[] y, double[][] a) {

		// Number of rows in matrix must be the same as the number of elements in vector
		if (a == null || y == null || a.length != y.length) {
			throw new IllegalArgumentException();
		}

		double[] b = new double[a[0].length];

		for (int i = 0; i < a[0].length; i++) {
			for (int j = 0; j < y.length; j++) {
				b[i] += a[j][i] * y[j];
			}
		}

		return b;
	}

}