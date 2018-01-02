package util;

import edu.princeton.cs.algs4.StdRandom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rene Argento on 20/02/17.
 */
public class ArrayGenerator {

    public static Map<Integer, Comparable[]> generateAllArrays(int numberOfExperiments, int initialArraySize, int multiplier) {

        Map<Integer, Comparable[]> allArrays = new HashMap<>();

        int arraySize = initialArraySize;

        for(int i = 0; i < numberOfExperiments; i++) {
            Comparable[] array = generateRandomArray(arraySize);
            allArrays.put(i, array);

            arraySize *= multiplier;
        }

        return allArrays;
    }

    public static Comparable[] generateRandomArray(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    public static int[] generateRandomIntegerArray(int length, int lowerBoundInclusive, int upperBoundExclusive) {
        int[] array = new int[length];

        for(int i = 0; i < length; i++) {
            array[i] = StdRandom.uniform(lowerBoundInclusive, upperBoundExclusive);
        }

        return array;
    }

    public static int[] generateRandomIntegerArray(int length, int upperBoundExclusive) {
        int[] array = new int[length];

        for(int i = 0; i < length; i++) {
            array[i] = StdRandom.uniform(upperBoundExclusive);
        }

        return array;
    }

    public static String[] generateRandomStringArray(int length, int minStringLength, int maxStringLength) {
        String[] array = new String[length];

        for(int i = 0; i < length; i++) {
            int stringSize = StdRandom.uniform(minStringLength, maxStringLength + 1);
            array[i] = new String();

            for(int j = 0; j < stringSize; j++) {
                char randomChar = (char) StdRandom.uniform(65, 122 + 1);
                array[i] = array[i].concat(String.valueOf(randomChar));
            }
        }

        return array;
    }

    public static char[] generateRandomUniqueUppercaseChars(int numberOfChars) {
        char[] chars = new char[numberOfChars];
        int charsIndex = 0;

        Set<Character> generatedChars = new HashSet<>();

        while (charsIndex < chars.length) {
            int randomUppercaseCharIntValue = StdRandom.uniform(65, 91);

            char generatedChar = ((char) randomUppercaseCharIntValue);

            if(!generatedChars.contains(generatedChar)) {
                chars[charsIndex++] = generatedChar;
                generatedChars.add(generatedChar);
            }
        }

        return chars;
    }

    public static Comparable[] generateOrderedArray(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = i;
        }

        return array;
    }

    public static Comparable[] generateReverseOrderedArray(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = length - 1 - i;
        }

        return array;
    }

    public static Comparable[] generateArrayWithAllKeysEqual(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = 0;
        }

        return array;
    }

    public static Comparable[] generateDistinctValuesShuffledArray(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = i;
        }

        StdRandom.shuffle(array);

        return array;
    }

    public static Comparable[] generateRandomArrayWith2Values(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = StdRandom.uniform(2);
        }

        return array;
    }

    public static Comparable[] generateRandomArrayWith3Values(int length) {
        Comparable[] array = new Comparable[length];

        for(int i = 0; i < length; i++) {
            array[i] = StdRandom.uniform(3);
        }

        return array;
    }

}
