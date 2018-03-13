package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 04/02/17.
 */
public class Exercise30_GeometricIncrements {

    private static double[] minimumTimes = new double[3];
    private static int[] bestTValues = new int[3];
    private static List<Integer[]> bestIncrementSequences = new ArrayList<>();

    public static void main(String[] args) {
        int arrayLength = 1000000;
        int numberOfExperiments = 10;

        for(int i = 0; i < minimumTimes.length; i++) {
            minimumTimes[i] = Double.MAX_VALUE;
            bestIncrementSequences.add(new Integer[]{});
        }

        timeRandomInput(arrayLength, numberOfExperiments);

        showBestTValueAndIncrementSequence();
    }

    private static void timeRandomInput(int arrayLength, int numberOfExperiments) {

        int tValue = 2;

        for(int experiment = 0; experiment < numberOfExperiments; experiment++) {

            Comparable[] array = new Comparable[arrayLength];

            for(int i = 0; i < arrayLength; i++) {
                array[i] = StdRandom.uniform();
            }

            Integer[] incrementSequence = generateIncrementSequence(tValue, arrayLength);

            double time = time(array, incrementSequence);

            updateMinimumTimes(time, tValue, incrementSequence);

            tValue++;
        }
    }

    private static void updateMinimumTimes(double currentTime, int tValue, Integer[] incrementSequence) {

        int timeToReplace = -1;

        for(int i = 0; i < minimumTimes.length; i++) {
            if (currentTime < minimumTimes[i]) {
                timeToReplace = i;
                break;
            }
        }

        if (timeToReplace == -1) {
            return;
        }

        minimumTimes[timeToReplace] = currentTime;
        bestTValues[timeToReplace] = tValue;
        bestIncrementSequences.set(timeToReplace, incrementSequence);
    }

    public static double time(Comparable[] array, Integer[] incrementSequence) {
        Stopwatch timer = new Stopwatch();

        shellsort(array, incrementSequence);

        return timer.elapsedTime();
    }

    private static Integer[] generateIncrementSequence(int tValue, int arrayLength) {
        int maxIncrement = 1;
        int numberOfIncrements = 1;
        int value = tValue;

        while(value < arrayLength) {
            maxIncrement = value;
            value *= tValue;

            numberOfIncrements++;
        }

        Integer[] incrementSequence = new Integer[numberOfIncrements];

        int index = 0;
        while(maxIncrement > 0) {
            incrementSequence[index] = maxIncrement;

            maxIncrement = maxIncrement / tValue;
            index++;
        }

        return incrementSequence;
    }

    @SuppressWarnings("unchecked")
    private static void shellsort(Comparable[] array, Integer[] incrementSequence) {

        for(int increment : incrementSequence) {

            //h-sort the array
            for(int j = increment; j < array.length; j++) {
                int currentIndex = j;

                while(currentIndex >= increment && array[currentIndex].compareTo(array[currentIndex - increment]) < 0) {
                    Comparable temp = array[currentIndex];
                    array[currentIndex] = array[currentIndex - increment];
                    array[currentIndex - increment] = temp;

                    currentIndex = currentIndex - increment;
                }
            }
        }
    }

    private static void showBestTValueAndIncrementSequence() {

        for(int i = 0; i < bestTValues.length; i++) {
            StdOut.printf("Best %d tValue: %d \n", i + 1, bestTValues[i]);
            StdOut.printf("Best %d sequence:\n", i + 1);

            Integer[] incrementSequence = bestIncrementSequences.get(i);
            for(int j = 0; j < incrementSequence.length; j++) {
                StdOut.print(incrementSequence[j] + " ");
            }

            StdOut.println();
            StdOut.println();
        }

    }

}
