package chapter2.section3;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;
import util.StatsUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 11/03/17.
 */
public class Exercise31_HistogramOfRunningTimes {

    private static final int NUMBER_OF_ARRAYS = 4;
    private static final int NUMBER_OF_BUCKETS = 10;

    // Parameter example: 20 0
    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]);

        // This value is irrelevant since the exercise description tells us to use 10^3, 10^4, 10^5 and 10^6
        int initialArraySize = Integer.parseInt(args[1]);
        int realArraySize = 1000;

        for(int i = 0; i < NUMBER_OF_ARRAYS; i++) {
            Comparable[] array = ArrayGenerator.generateRandomArray(realArraySize);
            doExperiment(numberOfExperiments, array, i);

            realArraySize *= 10;
        }
    }

    private static void doExperiment(int numberOfExperiments, Comparable[] originalArray, int experimentId) {

        List<Double> runningTimesList = new ArrayList<>();
        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = 0;

        for(int i = 0; i < numberOfExperiments; i++) {
            Comparable[] array = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, array, 0, originalArray.length);

            Stopwatch timer = new Stopwatch();
            QuickSort.quickSort(array);
            double runningTime = timer.elapsedTime();

            runningTimesList.add(runningTime);

            if (runningTime < minValue) {
                minValue = runningTime;
            }

            if (runningTime > maxValue) {
                maxValue = runningTime;
            }
        }

        double rangeOfValues = maxValue - minValue;
        double rangeOfRunningTimesPerBucket = rangeOfValues / NUMBER_OF_BUCKETS;

        double[] runningTimes = getHistogramValues(runningTimesList, minValue, rangeOfRunningTimesPerBucket);
        drawHistogram(runningTimes, minValue, rangeOfRunningTimesPerBucket, experimentId + 3);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // No need to take any action if sleep is interrupted
        }
    }

    private static double[] getHistogramValues(List<Double> runningTimesList, double minValue,
                                               double rangeOfRunningTimesPerBucket) {
        double[] runningTimes = new double[NUMBER_OF_BUCKETS];

        for (double runningTime : runningTimesList) {
            int bucketId = getBucketId(runningTime, minValue, rangeOfRunningTimesPerBucket);
            runningTimes[bucketId]++;
        }

        return runningTimes;
    }

    private static int getBucketId(double value, double minValue, double rangeOfRunningTimesPerBucket) {
        double valueMinusOffset = value - minValue;
        int bucketId = (int) Math.floor(valueMinusOffset / rangeOfRunningTimesPerBucket);
        return Math.min(bucketId, NUMBER_OF_BUCKETS - 1);
    }

    private static void drawHistogram(double[] runningTimesHistogram, double minValue,
                                      double rangeOfRunningTimesPerBucket, int arraySizePowerOf10) {
        StdDraw.setCanvasSize(1024, 512);

        double maxCount = 0;

        for(int i = 0; i < runningTimesHistogram.length; i++) {
            if (runningTimesHistogram[i] > maxCount) {
                maxCount = runningTimesHistogram[i];
            }
        }

        double minX = -1.5;
        double maxX = runningTimesHistogram.length;
        double middleX = minX + (maxX - minX) / 2;

        double minY = -2.2;
        double maxY = maxCount + 3;
        double middleY = minY + (maxY - minY) / 2;

        StdDraw.setXscale(minX, maxX);
        StdDraw.setYscale(minY, maxY);

        // Labels
        String fontName = "Verdana";
        Font titlesFont = new Font(fontName, Font.PLAIN, 14);
        StdDraw.setFont(titlesFont);

        StdDraw.text(middleX, maxCount + 2, "Frequency vs Running Times");
        StdDraw.text(-1, middleY, "Frequency", 90);
        StdDraw.text(middleX, -1.3, "Running Times");

        StdDraw.text(middleX, maxCount + 1, "N = 10^" + arraySizePowerOf10);

        Font graphLabelsFont = new Font(fontName, Font.PLAIN, 10);
        StdDraw.setFont(graphLabelsFont);

        // Y labels
        for (int y = 0; y <= maxCount; y++) {
            StdDraw.text(-0.5, y, String.valueOf(y));
        }

        // X labels
        String[] runningTimeDescriptions = getBucketDescriptions(minValue, rangeOfRunningTimesPerBucket);
        int lineBreakIndex = 9;

        double secondXLineYPosition;

        if (maxCount <= 5) {
            secondXLineYPosition = -0.5;
        } else {
            secondXLineYPosition = -0.7;
        }

        for (int x = 0; x < runningTimeDescriptions.length; x++) {
            StdDraw.text(x, -0.3, runningTimeDescriptions[x].substring(0, lineBreakIndex));
            StdDraw.text(x, secondXLineYPosition, runningTimeDescriptions[x].substring(lineBreakIndex));
        }

        StatsUtil.plotBars(runningTimesHistogram, 0.25);
    }

    private static String[] getBucketDescriptions(double minValue, double rangeOfRunningTimesPerBucket) {
        String[] bucketDescriptions = new String[NUMBER_OF_BUCKETS];

        for (int i = 0; i < bucketDescriptions.length; i++) {
            double minSize = minValue + (rangeOfRunningTimesPerBucket * i);
            double maxSize = minSize + rangeOfRunningTimesPerBucket - 0.00001;

            bucketDescriptions[i] = String.format("[%.5f -%.5f] ", minSize, maxSize);
        }

        return bucketDescriptions;
    }

}