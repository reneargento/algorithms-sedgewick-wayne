package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 29/01/17.
 */
public class Exercise12 {

    private static class ComparesByIncrement {

        int increment;
        double comparesByArraySize;

        public ComparesByIncrement(int increment, double comparesByArraySize) {
            this.increment = increment;
            this.comparesByArraySize = comparesByArraySize;
        }
    }

    public static void main(String[] args) {

        int maxArraySize = 10000000;

        for(int i = 100; i <= maxArraySize; i *= 10) {
            double[] array = new double[i];

            for(int j = 0; j < array.length; j++) {
                double value = StdRandom.uniform();
                array[j] = value;
            }

            List<ComparesByIncrement> comparesByIncrements = shellsort(array);
            printStatistics(comparesByIncrements);
        }
    }

    private static List<ComparesByIncrement> shellsort(double[] array) {

        List<ComparesByIncrement> comparesByIncrements = new ArrayList<>();
        double numberOfCompares;

        int incrementSequence = 1;

        while(incrementSequence * 3 + 1 < array.length) {
            incrementSequence *= 3;
            incrementSequence++;
        }

        while (incrementSequence > 0) {

            numberOfCompares = 0;

            for(int i = incrementSequence; i < array.length; i++) {

                numberOfCompares++;

                for(int j = i; j >= incrementSequence && array[j] < array[j - incrementSequence]; j -= incrementSequence) {
                    double temp = array[j];
                    array[j] = array[j - incrementSequence];
                    array[j - incrementSequence] = temp;

                    numberOfCompares++;
                }
            }

            double comparesByArraySize = numberOfCompares / (double) array.length;
            ComparesByIncrement comparesByIncrement = new ComparesByIncrement(incrementSequence, comparesByArraySize);
            comparesByIncrements.add(comparesByIncrement);

            incrementSequence /= 3;
        }

        return comparesByIncrements;
    }

    private static void printStatistics(List<ComparesByIncrement> comparesByIncrements) {
        StdOut.printf("%13s %22s\n", "Increment |", "Compares by Array Size");

        for(ComparesByIncrement comparesByIncrement : comparesByIncrements) {
            StdOut.printf("%11d %16.2f", comparesByIncrement.increment, comparesByIncrement.comparesByArraySize);
            StdOut.println();
        }
    }

}
