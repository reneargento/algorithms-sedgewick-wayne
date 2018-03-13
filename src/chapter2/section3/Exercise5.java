package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 04/03/17.
 */
public class Exercise5 {

    public static void main(String[] args) {

        int arrayLength = Integer.parseInt(args[0]);
        Comparable array[] = ArrayGenerator.generateRandomArrayWith2Values(arrayLength);

        StringJoiner originalArray = new StringJoiner(" ");
        for (Comparable element : array) {
            originalArray.add(String.valueOf(element));
        }
        StdOut.println("Original array: " + originalArray);

        sort3WayPartitioning(array);

        StringJoiner sortedArray = new StringJoiner(" ");
        for (Comparable element : array) {
            sortedArray.add(String.valueOf(element));
        }
        StdOut.println("Sorted array: " + sortedArray);
    }

    @SuppressWarnings("unchecked")
    private static void sort3WayPartitioning(Comparable[] array) {

        int lt = 0;
        int gt = array.length - 1;
        int i = lt + 1;

        Comparable pivot = array[0];

        while(i <= gt) {
            int comparison = array[i].compareTo(pivot);

            if (comparison < 0) {
                exchange(array, lt, i);
                lt++;
                i++;
            } else if (comparison > 0) {
                exchange(array, i, gt);
                gt--;
            } else {
                i++;
            }
        }
    }

    private static void exchange(Comparable[] array, int position1, int position2) {
        Comparable temp = array[position1];
        array[position1] = array[position2];
        array[position2] = temp;
    }

}
