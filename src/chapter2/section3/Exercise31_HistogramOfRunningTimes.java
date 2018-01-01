package chapter2.section3;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rene Argento on 11/03/17.
 */
public class Exercise31_HistogramOfRunningTimes {

    private static final int NUMBER_OF_ARRAYS = 4;

    public static void main(String[] args) {
        int numberOfExperiments = Integer.parseInt(args[0]); // 20

        // This value is irrelevant since the exercise description tells us to use 10^3, 10^4, 10^5 and 10^6
        int initialArraySize = Integer.parseInt(args[1]);
        int realArraySize = 1000;

        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();

        for(int i=0; i < NUMBER_OF_ARRAYS; i++) {
            Comparable[] array = ArrayGenerator.generateRandomArray(realArraySize);
            allInputArrays.put(i, array);

            realArraySize *= 10;
        }

        doExperiment(numberOfExperiments, allInputArrays);
    }

    private static void doExperiment(int numberOfExperiments, Map<Integer, Comparable[]> allInputArrays) {

        List<Double> runningTimes = new ArrayList<>();

        for(int i=0; i < NUMBER_OF_ARRAYS; i++) {

            for(int j=0; j < numberOfExperiments; j++) {
                Comparable[] originalArray = allInputArrays.get(i);
                Comparable[] array = new Comparable[originalArray.length];
                System.arraycopy(originalArray, 0, array, 0, originalArray.length);

                Stopwatch timer = new Stopwatch();
                QuickSort.quickSort(array);
                double runningTime = timer.elapsedTime();

                runningTimes.add(runningTime);
            }
        }

        histogram(runningTimes);
    }

    private static void histogram(List<Double> runningTimes) {

        double[] runningTimesArray = new double[runningTimes.size()];
        double maxCount = 0;

        for(int i=0; i < runningTimes.size(); i++) {
            runningTimesArray[i] = runningTimes.get(i);

            if(runningTimesArray[i] > maxCount) {
                maxCount = runningTimesArray[i];
            }
        }

        StdDraw.setCanvasSize(1024, 512);
        StdDraw.setXscale(0, runningTimesArray.length);
        StdDraw.setYscale(-0.2, maxCount + 0.5);

        //Labels
        StdDraw.text(runningTimesArray.length / 2, maxCount + 0.4, "Running Times");
        StdDraw.text(5, maxCount, String.valueOf(maxCount) + " seconds");
        StdDraw.text(0.8, -0.1, String.valueOf(0));
        StdDraw.text(runningTimesArray.length - 1.5, -0.1, String.valueOf(runningTimesArray.length));

        int horizontalSpace = runningTimes.size() / NUMBER_OF_ARRAYS;
        int leftOffset = (horizontalSpace / 2) * -1;

        for(int i=0; i < NUMBER_OF_ARRAYS; i++) {
            StdDraw.text(leftOffset + (i + 1) * horizontalSpace, maxCount + 0.2, "10^" + (i+3));
        }

        StdStats.plotBars(runningTimesArray);
    }

}
