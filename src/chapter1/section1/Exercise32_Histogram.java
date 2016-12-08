package chapter1.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdStats;

public class Exercise32_Histogram {

	public static void main(String[] args) {
		
		int n = 4;
		double l = 2.5;
		double r = 9.3;
		double[] numbers = {2, 2.5, 4.3, 5.5, 6.6, 6.7, 6.8, 8.3, 10};
		
		histogram(n, l, r, numbers);
	}

	private static void histogram(int n, double l, double r, double[] numbers) {
		
		int[] numbersInInterval = new int[n];
		
		verifyIntervals(n, l, r, numbers, numbersInInterval);
		
		int maxCount = StdStats.max(numbersInInterval);
		
		StdDraw.setCanvasSize(1024, 512);
        StdDraw.setXscale(l, r);
        StdDraw.setYscale(0, maxCount + 1);
        
        double intervalOfNumbers = (r - l) / n;
        
        for (int i = 0; i < n; i++) {
        	
            double x = l + (i + 0.5) * intervalOfNumbers,
                   y = numbersInInterval[i] / 2.0,
                   rw = intervalOfNumbers / 2.0,
                   rh = numbersInInterval[i] / 2.0;
            
            StdDraw.filledRectangle(x, y, rw, rh);
        }
        
	}
	
	private static void verifyIntervals(int n, double l, double r, double[] numbers, int[] numbersInInterval) {
		
		double interval = r - l;
		double intervalOfNumbers = interval / n;

		int indexOfInterval = 0;
		
		for (int i=0; i< numbers.length; i++) {
			indexOfInterval = 0;
			
			for (double j=l; j<=r && indexOfInterval < n; j+= intervalOfNumbers) {
				if (numbers[i] >= j && numbers[i] <= j + intervalOfNumbers) {
					numbersInInterval[indexOfInterval]++;
				}
				
				indexOfInterval++;
			}
		}
	}
	
}
