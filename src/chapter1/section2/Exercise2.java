package chapter1.section2;

import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento
 */
public class Exercise2 {

	// Parameter example: 3
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);

		Interval1D[] intervals = new Interval1D[n];
		
		getIntervals(intervals);
		
		StdOut.println("Pairs that intersect:");
		printIntervalIntersections(intervals);
	}
	
	private static void getIntervals(Interval1D[] intervals) {
		
		for (int i = 0; i < intervals.length; i++) {
			
			double firstValue = StdRandom.uniform();
			double secondValue = StdRandom.uniform();
			
			if (firstValue > secondValue) {
				double temp = firstValue;
				firstValue = secondValue;
				secondValue = temp;
			}
			
			intervals[i] = new Interval1D(firstValue, secondValue);
		}
	}

	private static void printIntervalIntersections(Interval1D[] intervals) {
		
		for (int i = 0; i < intervals.length - 1; i++) {
			for (int j = i + 1; j < intervals.length; j++) {
				
				if (intervals[i].intersects(intervals[j]) ) {
					StdOut.printf("Interval 1 - Min: %.3f  Max: %.3f \n", intervals[i].min(), intervals[i].max());
					StdOut.printf("Interval 2 - Min: %.3f  Max: %.3f \n", intervals[j].min(), intervals[j].max());
					StdOut.println();
				}
			}
		}
	}

}
