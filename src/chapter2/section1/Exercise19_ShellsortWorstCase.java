package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 28/07/20.
 */
// As the book mentions:
// "Indeed, constructing an array for which shellsort runs slowly for a particular increment sequence is usually a
// challenging exercise."
// The best method to find the worst-case input for shellsort for an array with the numbers from 1 to 100 seems to be
// through experimentation.
// Note: this experiment takes a while to finish running and is not deterministic because:
// 100! array permutations > 10^8 experiment runs

// Thanks to w1374720640 (https://github.com/w1374720640) for mentioning an approach to solve this exercise and to
// YRFT (https://github.com/YRFT) for reporting an issue with the previous solution.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/170
public class Exercise19_ShellsortWorstCase {

    @SuppressWarnings("unchecked")
    private static class ShellSortWithCompareCount {
        private int comparesCount;

        private void shellSort(Comparable[] array) {
            comparesCount = 0;
            int incrementSequence = 1;

            while(incrementSequence * 3 + 1 < array.length) {
                incrementSequence *= 3;
                incrementSequence++;
            }

            while (incrementSequence > 0) {

                for(int i = incrementSequence; i < array.length; i++) {
                    for(int j = i; j >= incrementSequence; j -= incrementSequence) {
                        comparesCount++;
                        if (array[j].compareTo(array[j - incrementSequence]) < 0) {
                            Comparable temp = array[j];
                            array[j] = array[j - incrementSequence];
                            array[j - incrementSequence] = temp;
                        } else {
                            break;
                        }
                    }
                }

                incrementSequence /= 3;
            }
        }

        public int getComparesCount() {
            return comparesCount;
        }
    }

    public static void main(String[] args) {
        Integer[] worstCaseArray = findWorstCaseInput();
        printArray(worstCaseArray);
    }

    private static Integer[] findWorstCaseInput() {
        Integer[] worstCaseArray = null;
        int highestNumberOfCompares = 0;
        ShellSortWithCompareCount shellSort = new ShellSortWithCompareCount();

        for (int i = 0; i < 100000000; i++) {
            Integer[] array = generateRandomArray();
            Integer[] arrayCopy = new Integer[array.length];
            System.arraycopy(array, 0, arrayCopy, 0, array.length);
            shellSort.shellSort(array);

            if (shellSort.getComparesCount() > highestNumberOfCompares) {
                worstCaseArray = arrayCopy;
                highestNumberOfCompares = shellSort.getComparesCount();
            }
        }

        StdOut.println("Highest number of compares: " + highestNumberOfCompares);
        return worstCaseArray;
    }

    private static Integer[] generateRandomArray() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
        StdRandom.shuffle(array);
        return array;
    }

    private static void printArray(Integer[] array) {
        StringJoiner arrayDescription = new StringJoiner(" ");
        for (int element : array) {
            arrayDescription.add(String.valueOf(element));
        }
        StdOut.println(arrayDescription);
    }

}
