package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rene Argento on 21/02/17.
 */
public class Exercise29_NaturalMergesort {

    public static void main(String[] args) {

        List<Long[]> arrays = new ArrayList<>(4);
        Long[] array1 = generateLongArray(1000);
        Long[] array2 = generateLongArray(2000);
        Long[] array3 = generateLongArray(3000);

      //  Long[] array2 = generateLongArray(1000000); Very slow - using the other experiments to estimate
       // Long[] array3 = generateLongArray(1000000000); Not enough heap memory - using the other experiments to estimate

        arrays.add(array1);
        arrays.add(array2);
        arrays.add(array3);

        doExperiment(arrays);
    }

    private static Long[] generateLongArray(int length) {
        Long[] array = new Long[length];
        Random random = new Random();

        for(int i = 0; i < length; i++) {
            array[i] = random.nextLong();
        }

        return array;
    }

    private static void doExperiment(List<Long[]> arrays) {

        int numberOfExperiments = arrays.size();

        for(int i = 0; i < numberOfExperiments; i++) {
            Long[] currentArray = arrays.get(i);

            long numberOfPasses = naturalMergesort(currentArray);

            StdOut.printf("Number of passes needed for an array of %d random Long keys: %d \n", currentArray.length,
                    numberOfPasses);
        }
    }

    private static int naturalMergesort(Long[] array) {

        int numberOfPasses = 1;

        if (array == null || array.length == 1) {
            return numberOfPasses;
        }

        Comparable[] aux = new Comparable[array.length];

        int low = 0;
        int middle = 0;
        int high = 0;

        boolean secondSubArray = false;

        for(int i = 1; i < array.length; i++) {

            if (array[i].compareTo(array[i - 1]) < 0) {
                if (!secondSubArray) {
                    middle = i - 1;

                    secondSubArray = true;

                    numberOfPasses++;
                } else {
                    high = i - 1;

                    BottomUpMergeSort.merge(array, aux, low, middle, high);

                    middle = high;

                    numberOfPasses++;
                }
            }
        }

        if (high != array.length - 1) {
            BottomUpMergeSort.merge(array, aux, low, middle, array.length - 1);
            numberOfPasses++;
        }

        return numberOfPasses;
    }

}
