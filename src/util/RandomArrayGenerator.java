package util;

import edu.princeton.cs.algs4.StdRandom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rene on 20/02/17.
 */
public class RandomArrayGenerator {

    public static Map<Integer, Comparable[]> generateAllArrays(int numberOfExperiments, int initialArraySize) {

        Map<Integer, Comparable[]> allArrays = new HashMap<>();

        int arraySize = initialArraySize;

        for(int i=0; i < numberOfExperiments; i++) {
            Comparable[] array = generateArray(arraySize);
            allArrays.put(i, array);

            arraySize *= 2;
        }

        return allArrays;
    }

    public static Comparable[] generateArray(int length) {
        Comparable[] array = new Comparable[length];

        for(int i=0; i < length; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    public static Comparable[] generateDistinctValuesShuffledArray(int length) {
        Comparable[] array = new Comparable[length];

        for(int i=0; i < length; i++) {
            array[i] = i;
        }

        StdRandom.shuffle(array);

        return array;
    }

    public static Comparable[] generateRandomArrayWith2Values(int length) {
        Comparable[] array = new Comparable[length];

        for(int i=0; i < length; i++) {
            array[i] = StdRandom.uniform(2);
        }

        return array;
    }

    public static Comparable[] generateRandomArrayWith3Values(int length) {
        Comparable[] array = new Comparable[length];

        for(int i=0; i < length; i++) {
            array[i] = StdRandom.uniform(3);
        }

        return array;
    }

}
