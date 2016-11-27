package Chapter1.Section1;

import edu.princeton.cs.algs4.StdOut;

public class Exercise21 {

	public static void main(String[] args) {
		
		String lines = "Rene 2 1 " +
				"Bacon 4 3 " +
				"Abcdef 6 2 ";
		
		formattedPrint(lines);
	}
	
	private static void formattedPrint(String lines) {
		
		StdOut.printf("%8s %7s %7s %7s","Names","Number1","Number2","Result\n");
		
		String[] splittedValues = lines.split(" ");
		
		for (int i=0; i < splittedValues.length; i++) {
			if (i % 3 != 0) {
				StdOut.printf("%8s",splittedValues[i]);
			} else{
				StdOut.printf("%7s",splittedValues[i]);
			}
			
			if ((i+1) % 3 == 0) {
				double value1 = Double.parseDouble(splittedValues[i-1]);
				double value2 = Double.parseDouble(splittedValues[i]);
				double result = value1 / value2;
				StdOut.printf("%7.3f \n",result);
			}
		}
		
	}
	
}
