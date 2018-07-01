package chapter1.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento
 */
public class Exercise32_Histogram {

	// Parameters example: 4 2.5 9.3 2 2.5 4.3 5.5 6.6 6.7 6.8 8.3 10
	public static void main(String[] args) {

		int n = Integer.parseInt(args[0]);
		double l = Double.parseDouble(args[1]);
		double r = Double.parseDouble(args[2]);

		List<Double> numbers = new ArrayList<>();
		int numberIndex = 3;
		try {
			while (args[numberIndex] != null) {
				numbers.add(Double.parseDouble(args[numberIndex])); // {2, 2.5, 4.3, 5.5, 6.6, 6.7, 6.8, 8.3, 10};
				numberIndex++;
			}
		} catch (ArrayIndexOutOfBoundsException exception) {
			// We read all the input
		}

		double[] numbersArray = new double[numbers.size()];
		for(int i = 0; i < numbers.size(); i++) {
			numbersArray[i] = numbers.get(i);
		}

		histogram(n, l, r, numbersArray);
	}

	private static void histogram(int numberOfIntervals, double left, double right, double[] numbers) {

		int[] numbersInInterval = new int[numberOfIntervals];
		double intervalSize = (right - left) / numberOfIntervals;

		computeHistogramValues(numberOfIntervals, left, right, numbers, numbersInInterval);

		int maxCount = StdStats.max(numbersInInterval);

		double minX = left - 1;
		double maxX = right + 1;
		double minY = -2;
		double maxY = maxCount + 2;

		StdDraw.setCanvasSize(1024, 512);
		StdDraw.setXscale(minX, maxX);
		StdDraw.setYscale(minY, maxY);

		double middleX = minX + (maxX - minX) / 2;
		double middleY = minY + (maxY - minY) / 2;

		// Labels
		StdDraw.text(middleX, maxY - 0.5, "Numbers in intervals");
		StdDraw.text(minX + 0.25, middleY, "Numbers", 90);
		StdDraw.text(middleX, -1.2, "Intervals");

		// X labels
		for(int x = 0; x < numberOfIntervals; x++) {
			double minValue = left + (intervalSize * x);
			double maxValue = minValue + intervalSize - 0.01;
			String intervalDescription = String.format("[%.2f - %.2f]", minValue, maxValue);
			StdDraw.text(left + (x + 0.5) * intervalSize, -0.25, intervalDescription);
		}

		// Y labels
		for(int y = 0; y < maxY; y++) {
			StdDraw.text(minX + 0.7, y, String.valueOf(y));
		}

		for (int i = 0; i < numberOfIntervals; i++) {

			double x = left + (i + 0.5) * intervalSize;
			double y = numbersInInterval[i] / 2.0;
			double halfWidth = intervalSize / 3.0;
			double halfHeight = numbersInInterval[i] / 2.0;

			StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
		}

	}

	private static void computeHistogramValues(int n, double l, double r, double[] numbers, int[] numbersInInterval) {
		double interval = r - l;
		double intervalOfNumbers = interval / n;

		int indexOfInterval;

		for (int i = 0; i < numbers.length; i++) {
			indexOfInterval = 0;

			for (double j = l; j <= r && indexOfInterval < n; j+= intervalOfNumbers) {
				if (numbers[i] >= j && numbers[i] <= j + intervalOfNumbers) {
					numbersInInterval[indexOfInterval]++;
				}

				indexOfInterval++;
			}
		}
	}

}