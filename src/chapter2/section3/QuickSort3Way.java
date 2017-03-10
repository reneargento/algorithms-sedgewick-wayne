package chapter2.section3;

import edu.princeton.cs.algs4.StdRandom;
import util.ArraySortUtil;

/**
 * Created by rene on 09/03/17.
 */
public class QuickSort3Way {

    public static void quickSort3Way(Comparable[] array) {
        StdRandom.shuffle(array);
        quickSort3Way(array, 0, array.length - 1);
    }

    @SuppressWarnings("unchecked")
    private static void quickSort3Way(Comparable[] array, int low, int high) {
        if(low >= high) {
            return;
        }

        int lt = low;
        int i = low + 1;
        int gt = high;

        Comparable pivot = array[low];

        while (i <= gt) {
            int compare = array[i].compareTo(pivot);

            if(compare < 0) {
                ArraySortUtil.exchange(array, lt, i);
                lt++;
                i++;
            } else if(compare > 0) {
                ArraySortUtil.exchange(array, i, gt);
                gt--;
            } else {
                i++;
            }
        }

        //Now a[low..lt - 1] < pivot = a[lt..gt] < a[gt + 1..high]
        quickSort3Way(array, low, lt - 1);
        quickSort3Way(array, gt + 1, high);
    }

}
