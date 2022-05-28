package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rene Argento on 04/02/17.
 */
// Thanks to suyj-git (https://github.com/suyj-git) for suggesting the use of non-integer values for t.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/249
public class Exercise30_GeometricIncrements {

    private static class Result implements Comparable<Result> {
        double tValue;
        double time;
        int[] incrementSequence;

        public Result(double tValue, double time, int[] incrementSequence) {
            this.tValue = tValue;
            this.time = time;
            this.incrementSequence = incrementSequence;
        }

        @Override
        public int compareTo(Result other) {
            return Double.compare(time, other.time);
        }
    }

    private static final int ARRAY_LENGTH = 1000000;

    public static void main(String[] args) {
        Comparable[] array = ArrayGenerator.generateRandomArray(ARRAY_LENGTH);
        List<Result> results = new ArrayList<>();
        timeRandomInput(array, results);
        printBestTValues(results);
    }

    private static void timeRandomInput(Comparable[] array, List<Result> results) {
        for (double tValue = 2; tValue <= 16; tValue += 0.1) {
            Comparable[] arrayCopy = new Comparable[ARRAY_LENGTH];
            System.arraycopy(array, 0, arrayCopy, 0, ARRAY_LENGTH);

            int[] incrementSequence = generateIncrementSequence(tValue);
            double time = time(arrayCopy, incrementSequence);
            results.add(new Result(tValue, time, incrementSequence));
        }
    }

    public static double time(Comparable[] array, int[] incrementSequence) {
        Stopwatch timer = new Stopwatch();
        shellsort(array, incrementSequence);
        return timer.elapsedTime();
    }

    private static int[] generateIncrementSequence(double tValue) {
        double maxIncrement = 1;
        int numberOfIncrements = 1;
        double value = tValue;

        while (value < ARRAY_LENGTH) {
            maxIncrement = value;
            value *= tValue;
            numberOfIncrements++;
        }

        int[] incrementSequence = new int[numberOfIncrements];
        for (int i = 0; i < numberOfIncrements; i++) {
            incrementSequence[i] = (int) Math.max(maxIncrement, 1);
            maxIncrement = maxIncrement / tValue;
        }
        return incrementSequence;
    }

    @SuppressWarnings("unchecked")
    private static void shellsort(Comparable[] array, int[] incrementSequence) {
        for (int increment : incrementSequence) {
            // h-sort the array
            for (int j = increment; j < array.length; j++) {
                int currentIndex = j;

                while (currentIndex >= increment && array[currentIndex].compareTo(array[currentIndex - increment]) < 0) {
                    Comparable temp = array[currentIndex];
                    array[currentIndex] = array[currentIndex - increment];
                    array[currentIndex - increment] = temp;

                    currentIndex = currentIndex - increment;
                }
            }
        }
    }

    private static void printBestTValues(List<Result> results) {
        Collections.sort(results);

        for (int i = 0; i < 3; i++) {
            Result result = results.get(i);
            StdOut.printf("Best %d tValue: %.2f \n", i + 1, result.tValue);
            StdOut.printf("Best %d sequence:\n", i + 1);

            for (int j = result.incrementSequence.length - 1; j >= 0; j--) {
                StdOut.print(result.incrementSequence[j]);
                if (j != 0) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
            StdOut.println();
        }
    }
}
