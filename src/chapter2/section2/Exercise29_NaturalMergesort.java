package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.Random;

/**
 * Created by Rene Argento on 21/02/17.
 */
// Thanks to ckwastra (https://github.com/ckwastra) for fixing the count of the number of passes:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/262
public class Exercise29_NaturalMergesort {

    public static void main(String[] args) {
        // Using smaller array lengths to identify the pattern
        int[] arrayLengths = { 1000, 10000, 100000, 1000000 };

        for (int arrayLength : arrayLengths) {
            doExperiment(arrayLength);
        }
    }

    private static void doExperiment(int arrayLength) {
        int numberOfExperiments = 100;
        long totalNumberOfPasses = 0;

        for (int i = 0; i < numberOfExperiments; i++) {
            Long[] array = generateLongArray(arrayLength);
            totalNumberOfPasses += countNumberOfPasses(array);
        }
        double averageNumberOfPasses = totalNumberOfPasses / 100.0;
        StdOut.printf("Number of passes needed for an array of %d random Long keys: %.2f \n", arrayLength,
                averageNumberOfPasses);
    }

    private static Long[] generateLongArray(int length) {
        Long[] array = new Long[length];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            array[i] = random.nextLong();
        }
        return array;
    }

    private static int countNumberOfPasses(Long[] array) {
        int subArrays = 1;
        int lastSubArrayIndex = findSortedSubArray(array, 0);

        while (lastSubArrayIndex != array.length - 1) {
            subArrays++;
            lastSubArrayIndex = findSortedSubArray(array, lastSubArrayIndex + 1);
        }

        if (subArrays == 1) {
            return 1;
        }
        return (int) (Math.log(subArrays) / Math.log(2));
    }

    private static int findSortedSubArray(Long[] array, int startIndex) {
        for (int i = startIndex; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return i;
            }
        }
        return array.length - 1;
    }
}
