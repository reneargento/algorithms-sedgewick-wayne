package chapter1.section1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Exercise21 {

	/**
     	* Reads in a sequence of strings from the whitelist file, specified as
     	* a command-line argument; reads in strings from standard input;
     	* prints to standard output a table with a column of the names, 
	* the integers, and the result of dividing the first by the second,
	* accurate to three decimal places.
     	*/
	
	/**
	 * Exercise21.txt
	 * Rene 2 1 
	 * Bacon 4 3 
	 * Abcdef 6 2
	 */
	public static void main(String[] args) {

		StdOut.printf("%8s %7s %7s %7s", "Names", "Number1", "Number2", "Result\n");

		In in = new In("Exercise21.txt");
		String[] allLines = in.readAllStrings();

		formattedPrint(allLines);

	}

	private static void formattedPrint(String[] allLines) {

		for (int i = 0; i < allLines.length; i++) {
			StdOut.printf("%8s", allLines[i]);
			if ((i + 1) % 3 == 0) {
				double value1 = Double.parseDouble(allLines[i - 1]);
				double value2 = Double.parseDouble(allLines[i]);
				double result = value1 / value2;
				StdOut.printf("%7.3f \n", result);
			}
		}

	}

}
