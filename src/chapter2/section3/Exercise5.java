package chapter2.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 04/03/17.
 */
public class Exercise5 {

    public static void main(String[] args) {

        int arrayLength = Integer.parseInt(args[0]);
        Comparable array[] = generateRandomArrayWith2Values(arrayLength);

        sort3WayPartitioning(array);

        for (int i=0; i < arrayLength; i++) {
            StdOut.print(array[i] + " ");
        }

        StdOut.println();
    }

    private static Comparable[] generateRandomArrayWith2Values(int length) {
        Comparable[] array = new Comparable[length];

        for(int i=0; i < length; i++) {
            array[i] = StdRandom.uniform(2);
        }

        return array;
    }

    @SuppressWarnings("unchecked")
    private static void sort3WayPartitioning(Comparable[] array) {

        int lt = 0;
        int gt = array.length - 1;
        int i = lt + 1;

        Comparable pivot = array[0];

        while(i <= gt) {
            int comparison = array[i].compareTo(pivot);

            if(comparison < 0) {
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
