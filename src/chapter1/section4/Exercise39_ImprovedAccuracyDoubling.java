package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 26/11/16.
 */
public class Exercise39_ImprovedAccuracyDoubling {

    private static double timeTrial(int n) {
        //Time ThreeSum.count() for n random 6-digit ints.
        int max = 1000000;
        int[] values = new int[n];

        for(int i = 0; i < n; i++) {
            values[i] = StdRandom.uniform(-max, max);
        }

        Stopwatch timer = new Stopwatch();
        int count = threeSumCount(values);
        return timer.elapsedTime();
    }

    private static int threeSumCount(int[] values) {
        //Count triples that sum to 0
        int n = values.length;
        int count = 0;

        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                for(int k = j + 1; k < n; k++) {
                    if (values[i] + values[j] + values[k] == 0) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static double getMean(double[] values) {
        double sum = 0;

        for(int i = 0; i < values.length; i++) {
            sum += values[i];
        }

        return sum / values.length;
    }

    private static double getStandardDeviation(double[] values, double mean) {
        double sumOfSquareDifferences = 0;

        for(int i = 0; i < values.length; i++) {
            sumOfSquareDifferences += Math.pow(values[i] - mean, 2);
        }

        double variance = sumOfSquareDifferences / values.length;
        return Math.sqrt(variance);
    }

    public static void main(String[] args) {
        int numberOfCalls = Integer.parseInt(args[0]);
        Map<Integer, double[]> timeTrials = new HashMap<>();

        StdOut.printf("%6s %7s %5s\n", "Items", "Mean", "StdDeviation");

        for(int n = 125; n <= 1000; n += n) {

            double[] times = new double[numberOfCalls];
            timeTrials.put(n, times);

            for(int i = 0; i < numberOfCalls; i++) {
                double time = timeTrial(n);
                times[i] = time;
            }
        }

        Integer[] timeTrialKeys = new Integer[timeTrials.keySet().size()];
        timeTrials.keySet().toArray(timeTrialKeys);

        Arrays.sort(timeTrialKeys);

        for(int trial : timeTrialKeys) {

            double[] times = timeTrials.get(trial);
            double mean = getMean(times);
            double standardDeviation = getStandardDeviation(times, mean);

            StdOut.printf("%6d %7.2f %5.5f\n", trial, mean, standardDeviation);
        }

    }

}
