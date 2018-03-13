package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.Constants;

import java.util.*;

/**
 * Created by Rene Argento on 07/02/17.
 */
public class Exercise38_VariousTypesOfItems {

    private enum KeyType {
        String, Double, Integer;
    }

    public static void main(String[] args) {

        int arrayLength = Integer.parseInt(args[0]);
        int experiments = Integer.parseInt(args[1]);

        runAllExperiments(arrayLength, experiments);
    }

    private static void runAllExperiments(int initialArrayLength, int experiments) {

        int arrayLength = initialArrayLength;

        StdOut.printf("%15s %17s %8s\n", "Sort Type", "Array Length", "Time");
        StdOut.println("String key, one double value");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Map[] stringKeyDoubleValueArray = generateStringKeyDoubleValueArray(arrayLength);
            runExperiments(stringKeyDoubleValueArray, KeyType.String);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("Double key, ten String values");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Map[] doubleKeyStringValuesArray = generateDoubleKeyStringValuesArray(arrayLength);
            runExperiments(doubleKeyStringValuesArray, KeyType.Double);
            arrayLength *= 2;
        }

        StdOut.println();
        StdOut.println();

        arrayLength = initialArrayLength;

        StdOut.println("Int key, int array value");

        for(int experiment = 0; experiment < experiments; experiment++) {

            Map[] intKeyIntArrayValuesArray = generateIntKeyIntArrayValuesArray(arrayLength);
            runExperiments(intKeyIntArrayValuesArray, KeyType.Integer);
            arrayLength *= 2;
        }
    }

    private static void runExperiments(Map[] array, KeyType keyType) {
        Map[] arrayCopy1 = Arrays.copyOf(array, array.length);
        Map[] arrayCopy2 = Arrays.copyOf(array, array.length);

        double timeSelection = time(array, SortTypes.SELECTION, keyType);
        printExperiment(SortTypes.SELECTION, array.length, timeSelection);

        double timeInsertion = time(arrayCopy1, SortTypes.INSERTION, keyType);
        printExperiment(SortTypes.INSERTION, arrayCopy1.length, timeInsertion);

        double timeShell = time(arrayCopy2, SortTypes.SHELL, keyType);
        printExperiment(SortTypes.SHELL, arrayCopy2.length, timeShell);
    }

    private static Map[] generateStringKeyDoubleValueArray(int arrayLength) {

        Map[] array = new HashMap[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            Map<String, Double> keyValues = new HashMap<>();

            String randomKey = generate10CharRandomString();
            double randomValue = StdRandom.uniform();

            keyValues.put(randomKey, randomValue);

            array[i] = keyValues;
        }

        return array;
    }

    private static Map[] generateDoubleKeyStringValuesArray(int arrayLength) {

        Map[] array = new HashMap[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            Map<Double, String[]> keyValues = new HashMap<>();

            double randomKey = StdRandom.uniform();
            String[] randomValue = new String[10];

            for(int j = 0; j < randomValue.length; j++) {
                randomValue[j] = generate10CharRandomString();
            }

            keyValues.put(randomKey, randomValue);

            array[i] = keyValues;
        }

        return array;
    }

    private static Map[] generateIntKeyIntArrayValuesArray(int arrayLength) {

        Map[] array = new HashMap[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            Map<Integer, Integer[]> keyValues = new HashMap<>();

            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            Integer[] randomValue = new Integer[20];

            for(int j = 0; j < randomValue.length; j++) {
                randomValue[j] = StdRandom.uniform(Integer.MAX_VALUE);
            }

            keyValues.put(randomKey, randomValue);

            array[i] = keyValues;
        }

        return array;
    }

    private static String generate10CharRandomString() {
        char[] chars = new char[10];

        for(int i = 0; i < chars.length; i++) {

            int randomCharIntValue = StdRandom.uniform(Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX,
                    Constants.ASC_II_LOWERCASE_LETTERS_FINAL_INDEX + 1);

            //Not valid characters
            while(randomCharIntValue >= 91 && randomCharIntValue <= 96) {
                randomCharIntValue = StdRandom.uniform(Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX,
                        Constants.ASC_II_LOWERCASE_LETTERS_FINAL_INDEX + 1);
            }

            char randomCharValue = (char) randomCharIntValue;
            chars[i] = randomCharValue;
        }

        return new String(chars);
    }

    public static double time(Map[] array, SortTypes sortType, KeyType keyType) {
        Stopwatch timer = new Stopwatch();

        switch (sortType) {
            case SELECTION: selectionSort(array, keyType);
                break;
            case INSERTION: insertionSort(array, keyType);
                break;
            case SHELL: shellSort(array, keyType);
                break;
        }

        return timer.elapsedTime();
    }

    private static void selectionSort(Map[] array, KeyType keyType) {
        for(int i = 0; i < array.length; i++) {
            int minIndex = i;

            for(int j = i + 1; j < array.length; j++) {
                if (isLower(array, j, minIndex, keyType)) {
                    minIndex = j;
                }
            }

            swapElements(array, i, minIndex);
        }
    }

    private static void insertionSort(Map[] array, KeyType keyType) {

        for(int i = 0; i < array.length; i++) {
            for(int j = i; j > 0 && isLower(array, j - 1, j, keyType); j--) {
                swapElements(array, j, j - 1);
            }
        }
    }

    private static void shellSort(Map[] array, KeyType keyType) {
        int incrementSequence = 1;

        while(incrementSequence * 3 + 1 < array.length) {
            incrementSequence *= 3;
            incrementSequence++;
        }

        while (incrementSequence > 0) {

            for(int i = incrementSequence; i < array.length; i++) {
                for(int j = i; j >= incrementSequence && isLower(array, j, j - incrementSequence, keyType); j -= incrementSequence) {
                    swapElements(array, j, j - incrementSequence);
                }
            }

            incrementSequence /= 3;
        }
    }

    private static void swapElements(Map[] array, int index1, int index2) {
        Map temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    private static boolean isLower(Map[] array, int index1, int index2, KeyType keyType) {

        switch (keyType) {
            case String:
                //Get keys
                String stringKeyObject1 = null;

                for(Object key : array[index1].keySet()) {
                    stringKeyObject1 = (String) key;
                }

                String stringKeyObject2 = null;

                for(Object key : array[index2].keySet()) {
                    stringKeyObject2 = (String) key;
                }

                if (stringKeyObject1 == null || stringKeyObject2 == null) {
                    return false;
                }

                //Compare both Strings
                for(int c = 0; c < stringKeyObject2.toCharArray().length; c++) {
                    if (stringKeyObject1.length() == c) {
                        return true;
                    }

                    if (stringKeyObject1.charAt(c) < stringKeyObject2.charAt(c)) {
                        return true;
                    } else if (stringKeyObject1.charAt(c) > stringKeyObject2.charAt(c)) {
                        return false;
                    }
                }
                break;
            case Double:
                Double doubleKeyObject1 = null;

                for(Object key : array[index1].keySet()) {
                    doubleKeyObject1 = (double) key;
                }

                Double doubleKeyObject2 = null;

                for(Object key : array[index2].keySet()) {
                    doubleKeyObject2 = (double) key;
                }

                if (doubleKeyObject1 == null || doubleKeyObject2 == null) {
                    return false;
                }

                if (doubleKeyObject1 < doubleKeyObject2) {
                    return true;
                }
                break;
            case Integer:
                Integer integerKeyObject1 = null;

                for(Object key : array[index1].keySet()) {
                    integerKeyObject1 = (int) key;
                }

                Integer integerKeyObject2 = null;

                for(Object key : array[index2].keySet()) {
                    integerKeyObject2 = (int) key;
                }

                if (integerKeyObject1 == null || integerKeyObject2 == null) {
                    return false;
                }

                if (integerKeyObject1 < integerKeyObject2) {
                    return true;
                }
                break;
        }

        return false;
    }

    private static void printExperiment(SortTypes sortType, int arrayLength, double time) {
        StdOut.printf("%15s %17d %8.2f\n", sortType, arrayLength, time);
    }

}
