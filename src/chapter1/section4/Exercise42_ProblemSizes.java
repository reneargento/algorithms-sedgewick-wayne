package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

/**
 * Created by Rene Argento on 27/11/16.
 */
public class Exercise42_ProblemSizes {

    private double timeTrial(int n, int sumMethod) {
        int max = 1000000;
        int[] numbers = new int[n];

        for(int i = 0; i < n; i++) {
            numbers[i] = StdRandom.uniform(-max, max);
        }

        Stopwatch timer = new Stopwatch();
        switch (sumMethod) {
            case 0: twoSum(numbers); break;
            case 1: twoSumFast(numbers); break;
            case 2: threeSum(numbers); break;
            case 3: threeSumFast(numbers); break;
        }

        return timer.elapsedTime();
    }

    private int twoSum(int[] numbers) {
        int count = 0;

        for(int i = 0; i < numbers.length; i++) {
            for(int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == 0) {
                    count++;
                }
            }
        }

        return count;
    }

    private int twoSumFast(int[] numbers) {
        Arrays.sort(numbers);
        int count = 0;

        for(int i = 0; i < numbers.length; i++) {
            if (binarySearch(numbers, -numbers[i], 0, numbers.length - 1) > i) {
                count++;
            }
        }

        return count;
    }

    private int threeSum(int[] numbers) {
        int count = 0;

        for(int i = 0; i < numbers.length; i++) {
            for(int j = i + 1; j < numbers.length; j++) {
                for(int k = j + 1; k < numbers.length; k++) {
                    if (numbers[i] + numbers[j] + numbers[k] == 0) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private int threeSumFast(int[] numbers) {
        Arrays.sort(numbers);
        int count = 0;

        for(int i = 0; i < numbers.length; i++) {
            for(int j = i + 1; j < numbers.length; j++) {
                if (binarySearch(numbers, -numbers[i] - numbers[j], 0, numbers.length - 1) > j) {
                    count++;
                }
            }
        }

        return count;
    }

    private int binarySearch(int[] numbers, int target, int low, int high) {
        if (low > high) {
            return -1;
        }

        int middle = low + (high - low) / 2;

        if (numbers[middle] > target) {
            return binarySearch(numbers, target, low, middle - 1);
        } else if (numbers[middle] < target) {
            return binarySearch(numbers, target, middle + 1, high);
        } else {
            return middle;
        }
    }

    public static void main(String[] args) {
        //TwoSum
        StdOut.println("TwoSum");
        runExperiments(0);

        //TwoSumFast
        StdOut.println("TwoSumFast");
        runExperiments(1);

        //ThreeSum
        StdOut.println("ThreeSum");
        runExperiments(2);

        //ThreeSumFast
        StdOut.println("ThreeSumFast");
        runExperiments(3);
    }

    private static void runExperiments(int sumMethod) {
        Exercise42_ProblemSizes problemSizes = new Exercise42_ProblemSizes();

        //2^7 = 128
        double previousTime = problemSizes.timeTrial(128, sumMethod);

        StdOut.printf("%6s %7s %5s\n", "N", "Time", "Ratio");

        // 2^13 = 8192 --For ThreeSum and ThreeSumFast to converge
        // 2^16 = 65536 --For TwoSum to converge
        // 2^22 = 4194304 --For TwoSumFast to converge
        for(int n = 256; n <= 8192; n += n) {
            double time = problemSizes.timeTrial(n, sumMethod);
            StdOut.printf("%6d %7.1f ", n, time);
            StdOut.printf("%5.1f\n", time / previousTime);
            previousTime = time;
        }
    }

}
