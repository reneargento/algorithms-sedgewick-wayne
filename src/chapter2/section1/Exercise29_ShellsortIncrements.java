package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

/**
 * Created by Rene Argento on 03/02/17.
 */
public class Exercise29_ShellsortIncrements {

    private static int[] threeNPlus1IncrementSequence;
    private static int[] sedgewicksIncrementSequence;

    private enum IncrementType {
        ThreeNPlusOne, Sedgewicks;
    }

    public static void main(String[] args) {
        int initialArrayLength = 32768;
        int numberOfExperiments = 10;

        int maxSize = initialArrayLength;

        for(int i = 0; i < numberOfExperiments - 1; i++) {
            maxSize *= 2;
        }

        generate3NPlus1IncrementSequence(maxSize);
        generateSedgewicksIncrementSequence(maxSize);

        timeRandomInput(initialArrayLength, numberOfExperiments);
    }

    private static void timeRandomInput(int initialLength, int numberOfExperiments) {
        int length = initialLength;

        for(int experiment = 0; experiment < numberOfExperiments; experiment++) {

            Comparable[] array = new Comparable[length];

            for(int i = 0; i < length; i++) {
                array[i] = StdRandom.uniform();
            }

            Comparable[] arrayCopy = Arrays.copyOf(array, array.length);

            int[] incrementSequence3NPlus1 = getIncrementSequence(IncrementType.ThreeNPlusOne, array.length);
            int[] incrementSequenceSedgewick = getIncrementSequence(IncrementType.Sedgewicks, arrayCopy.length);

            double time1 = time(array, incrementSequence3NPlus1);
            double time2 = time(arrayCopy, incrementSequenceSedgewick);

            StdOut.printf("For an array of size %d: \n", length);
            StdOut.printf("3N + 1 sequence: %.1f \n", time1);
            StdOut.printf("Sedgewicks's sequence: %.1f \n", time2);
            StdOut.println();

            length *= 2;
        }
    }

    public static double time(Comparable[] array, int[] incrementSequence) {
        Stopwatch timer = new Stopwatch();

        shellsort(array, incrementSequence);

        return timer.elapsedTime();
    }

    private static void generate3NPlus1IncrementSequence(int maxSize) {
        int maxIncrement = 1;
        int numberOfIncrements = 1;

        while((maxIncrement * 3) + 1 < maxSize) {
            maxIncrement = maxIncrement * 3;
            maxIncrement++;

            numberOfIncrements++;
        }

        threeNPlus1IncrementSequence = new int[numberOfIncrements];

        int index = 0;
        while(maxIncrement > 0) {
            threeNPlus1IncrementSequence[index] = maxIncrement;

            maxIncrement--;
            maxIncrement = maxIncrement / 3;
            index++;
        }
    }

    private static void generateSedgewicksIncrementSequence(int maxSize) {
        //Get number of increments required
        int numberOfIncrements = 0;

        int incrementIndex = 0;
        int currentIncrementSet1 = (int) (9 * Math.pow(4, incrementIndex) - 9 * Math.pow(2, incrementIndex) + 1);
        //Start with incrementIndex + 2 as power to avoid negative values
        int currentIncrementSet2 = (int) (Math.pow(4, incrementIndex + 2) - 3 * Math.pow(2, incrementIndex + 2) + 1);

        while(currentIncrementSet1 < maxSize || currentIncrementSet2 < maxSize) {

            if (currentIncrementSet1 < maxSize) {
                numberOfIncrements++;
            }
            if (currentIncrementSet2 < maxSize) {
                numberOfIncrements++;
            }

            incrementIndex++;
            currentIncrementSet1 = (int) (9 * Math.pow(4, incrementIndex) - 9 * Math.pow(2, incrementIndex) + 1);
            currentIncrementSet2 = (int) (Math.pow(4, incrementIndex + 2) - 3 * Math.pow(2, incrementIndex + 2) + 1);
        }

        if (numberOfIncrements == 0) {
            sedgewicksIncrementSequence = new int[]{};
        }

        //Generate array of increments
        sedgewicksIncrementSequence = new int[numberOfIncrements];
        incrementIndex = 0;

        currentIncrementSet1 = (int) (9 * Math.pow(4, incrementIndex) - 9 * Math.pow(2, incrementIndex) + 1);
        currentIncrementSet2 = (int) (Math.pow(4, incrementIndex + 2) - 3 * Math.pow(2, incrementIndex + 2) + 1);

        for(int i = numberOfIncrements - 1; i >= 0; i--) {

            sedgewicksIncrementSequence[i] = currentIncrementSet1;

            if (i - 1 < 0) {
                break;
            }

            sedgewicksIncrementSequence[i - 1] = currentIncrementSet2;

            i--;
            incrementIndex++;

            //Update increments
            currentIncrementSet1 = (int) (9 * Math.pow(4, incrementIndex) - 9 * Math.pow(2, incrementIndex) + 1);
            currentIncrementSet2 = (int) (Math.pow(4, incrementIndex + 2) - 3 * Math.pow(2, incrementIndex + 2) + 1);
        }
    }

    private static int[] getIncrementSequence(IncrementType incrementType, int arraySize) {

        int[] incrementSequence = null;
        if (incrementType == IncrementType.ThreeNPlusOne) {
            incrementSequence = threeNPlus1IncrementSequence;
        } else if (incrementType == IncrementType.Sedgewicks) {
            incrementSequence = sedgewicksIncrementSequence;
        }

        if (incrementSequence == null) {
            return new int[]{};
        }

        int low = 0;
        int high = incrementSequence.length - 1;
        int middle = 0;

        while(low <= high) {
            middle = low + (high - low) / 2;

            if (incrementSequence[middle] > arraySize) {
                low = middle + 1;
            } else if (incrementSequence[middle] < arraySize) {
                high = middle - 1;
            } else if (incrementSequence[middle] == arraySize) {
                break;
            }
        }

        //Check if we are still within the array bounds
        if (incrementSequence[middle] >= arraySize) {
            middle++;
        }

        int[] incrementSequenceToBeUsed = new int[incrementSequence.length - middle];
        int index = 0;
        for(int i = middle; i < incrementSequence.length; i++) {
            incrementSequenceToBeUsed[index] = incrementSequence[i];
            index++;
        }

        return incrementSequenceToBeUsed;
    }

    @SuppressWarnings("unchecked")
    private static void shellsort(Comparable[] array, int[] incrementSequence) {

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

}