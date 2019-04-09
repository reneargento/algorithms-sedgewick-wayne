package chapter2.section2;

import util.ArrayGenerator;

/**
 * Created by Rene Argento on 14/02/17.
 */
// Running time: O(n * IS)
// Array size = n
// Number of maximal increasing sequences in the array = IS
@SuppressWarnings("unchecked")
public class Exercise16_NaturalMergesort {

    public static void main(String[] args) {
        Comparable[] array = ArrayGenerator.generateRandomArray(1000);
        naturalMergesort(array);
    }

    private static void naturalMergesort(Comparable[] array) {
        if (array == null || array.length == 1) {
            return;
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
                } else {
                    high = i - 1;
                    BottomUpMergeSort.merge(array, aux, low, middle, high);
                    middle = high;
                }
            }
        }

        if (high != array.length - 1) {
            BottomUpMergeSort.merge(array, aux, low, middle, array.length - 1);
        }
    }
}
