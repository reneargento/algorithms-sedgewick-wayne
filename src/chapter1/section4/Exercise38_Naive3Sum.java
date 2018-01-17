package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 26/11/16.
 */
public class Exercise38_Naive3Sum {

    private static double timeTrial(int n, boolean useEvenNaiverImplementation) {
        //Time ThreeSum.count() for n random 6-digit ints.
        int max = 1000000;
        int[] values = new int[n];

        for(int i = 0; i < n; i++) {
            values[i] = StdRandom.uniform(-max, max);
        }

        Stopwatch timer = new Stopwatch();

        if (useEvenNaiverImplementation) {
            evenNaiverThreeSumCount(values);
        } else {
            threeSumCount(values);
        }

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

    private static int evenNaiverThreeSumCount(int[] values) {
        //Count triples that sum to 0
        int n = values.length;
        int count = 0;

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                for(int k = 0; k < n; k++) {
                    if (i < j && j < k) {
                        if (values[i] + values[j] + values[k] == 0) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        Map<Integer, Double> timesOfNaiveThreeSum = new HashMap<>();
        Map<Integer, Double> timesOfEvenNaiverThreeSum = new HashMap<>();

        int maxTrials = 4000;

        StdOut.println("Number of items and ratio");

        //Compute running times of naive 3-Sum
        for(int n = 250; n <= maxTrials; n += n) {
            double time = timeTrial(n, false);
            timesOfNaiveThreeSum.put(n, time);
        }

        //Compute running times of even naiver 3-Sum
        for(int n = 250; n <= maxTrials; n += n) {
            double time = timeTrial(n, true);
            timesOfEvenNaiverThreeSum.put(n, time);
        }

        for(int n = 250; n <= maxTrials; n += n) {
            double timeOfNaiveThreeSum = timesOfNaiveThreeSum.get(n);
            double timeOfEvenNaiverThreeSum = timesOfEvenNaiverThreeSum.get(n);

            double ratio = timeOfEvenNaiverThreeSum / timeOfNaiveThreeSum;

            StdOut.printf("%7d %5.1f\n", n, ratio);
        }
    }

}
