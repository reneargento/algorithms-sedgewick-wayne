package chapter1.section2;

import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.Interval2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento
 */
public class Exercise3 {
	
	private static Map<Interval2D, Interval1D[]> intervalMap = new HashMap<>();

	// Parameters example: 6 3.5 7.2
	public static void main(String[] args) {

		int n = Integer.parseInt(args[0]);
		
		double min = Double.parseDouble(args[1]);
		double max = Double.parseDouble(args[2]);
		
		if (min > max) {
			double temp = min;
			min = max;
			max = temp;
		}
		
		Interval2D[] intervals = new Interval2D[n];
		
		drawAndCreateIntervals(intervals, min, max);
		
		int[] results = countIntervalIntersectionsAndContains(intervals);
				
		StdOut.println("Pairs of intervals that intersect: " + results[0]);
		StdOut.println("Intervals contained in one another: " + results[1]);
	}
	
	private static void drawAndCreateIntervals(Interval2D[] intervals, double min, double max) {
		
		StdDraw.setCanvasSize(1024, 512);
		StdDraw.setPenRadius(.015);
        StdDraw.setXscale(min, max);
        StdDraw.setYscale(min, max);
		
		for (int i = 0; i < intervals.length; i++) {

			Interval1D width = generateInterval1D(min, max);
			Interval1D height = generateInterval1D(min, max);
			
			Interval2D interval = new Interval2D(width, height);
			interval.draw();
			
			intervals[i] = interval;
			
			intervalMap.put(interval, new Interval1D[] {width, height});
		}
	}
	
	private static Interval1D generateInterval1D(double min, double max) {
		
		double firstValue = StdRandom.uniform(min, max);
		double secondValue = StdRandom.uniform(min, max);
		
		if (firstValue > secondValue) {
			double temp = firstValue;
			firstValue = secondValue;
			secondValue = temp;
		}
		
		Interval1D interval = new Interval1D(firstValue, secondValue);
		return interval;
	}
	
	private static int[] countIntervalIntersectionsAndContains(Interval2D[] intervals) {
		
		int[] results = new int[2]; //Intersections and contains
		
		int intersections = 0;
		int contains = 0;
		
		for (int i = 0; i < intervals.length - 1; i++) {
			for (int j = i + 1; j < intervals.length; j++) {
				
				if (intervals[i].intersects(intervals[j]) ) {
					intersections++;
				}
				
				if (isContained(intervals[i], intervals[j])) {
					contains++;
				}
			}
		}
		
		results[0] = intersections;
		results[1] = contains;
		
		return results;
	}
	
	private static boolean isContained(Interval2D interval1, Interval2D interval2) {
		boolean isContained = false;
		
		Interval1D[] intervalsFromInterval1 = intervalMap.get(interval1);
		Interval1D[] intervalsFromInterval2 = intervalMap.get(interval2);
		
		//Is interval1 contained in interval2 OR interval2 contained in interval1 ?
		if (  (intervalsFromInterval1[0].min() > intervalsFromInterval2[0].min() 
				&& intervalsFromInterval1[0].max() < intervalsFromInterval2[0].max()
				&& intervalsFromInterval1[1].min() > intervalsFromInterval2[1].min() 
				&& intervalsFromInterval1[1].max() < intervalsFromInterval2[1].max() )
				||
				(intervalsFromInterval2[0].min() > intervalsFromInterval1[0].min() 
						&& intervalsFromInterval2[0].max() < intervalsFromInterval1[0].max()
						&& intervalsFromInterval2[1].min() > intervalsFromInterval1[1].min() 
						&& intervalsFromInterval2[1].max() < intervalsFromInterval1[1].max() )
				) {
			isContained = true;
		}
		
		return isContained;
	}
	
}
