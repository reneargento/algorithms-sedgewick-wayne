package chapter1.section4;

/**
 * Created by Rene Argento on 17/08/18.
 */
public class BinarySearch {

    public static int binarySearch(int[] array, int target) {
        if (array == null) {
            return -1;
        }

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int middle = low + (high - low) / 2;

            if (array[middle] < target) {
                low = middle + 1;
            } else if (array[middle] > target) {
                high = middle - 1;
            } else {
                return middle;
            }
        }

        return -1;
    }

}
