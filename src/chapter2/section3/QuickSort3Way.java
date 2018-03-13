package chapter2.section3;

import edu.princeton.cs.algs4.StdRandom;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 09/03/17.
 */
public class QuickSort3Way {

    public static void quickSort3Way(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort3Way(array, 0, array.length - 1);
    }

    @SuppressWarnings("unchecked")
    private static void quickSort3Way(Comparable[] array, int low, int high) {
        if (low >= high) {
            return;
        }

        int lowerThan = low;
        int i = low + 1;
        int greaterThan = high;

        Comparable pivot = array[low];

        while (i <= greaterThan) {
            int compare = array[i].compareTo(pivot);

            if (compare < 0) {
                ArrayUtil.exchange(array, lowerThan, i);
                lowerThan++;
                i++;
            } else if (compare > 0) {
                ArrayUtil.exchange(array, i, greaterThan);
                greaterThan--;
            } else {
                i++;
            }
        }

        // Now array[low..lowerThan - 1] < pivot = array[lowerThan..greaterThan] < array[greaterThan + 1..high]
        quickSort3Way(array, low, lowerThan - 1);
        quickSort3Way(array, greaterThan + 1, high);
    }

}
