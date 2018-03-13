package util;

/**
 * Created by Rene Argento on 04/03/17.
 */
@SuppressWarnings("unchecked")
public class ArrayUtil {

    public static boolean less(Comparable value1, Comparable value2) {
        if (value1.compareTo(value2) < 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean more(Comparable value1, Comparable value2) {
        if (value1.compareTo(value2) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void exchange(Comparable[] array, int position1, int position2) {
        Comparable temp = array[position1];
        array[position1] = array[position2];
        array[position2] = temp;
    }

    public static boolean isSorted(Comparable[] array) {
        for(int i = 0; i < array.length - 1; i++) {
            if (more(array[i], array[i+1])) {
                return false;
            }
        }

        return true;
    }

}
