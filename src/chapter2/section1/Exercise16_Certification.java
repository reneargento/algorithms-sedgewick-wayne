package chapter2.section1;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 29/01/17.
 */
public class Exercise16_Certification {

    public static void main(String[] args) {
        Comparable[] array = {2, 20, -1, -30, 30, 5, 6, 8, -99, -3, 0, 4, 4, 4};
        StdOut.println("Check: " + check(array));
        StdOut.println("Expected: true");
    }

    @SuppressWarnings("unchecked")
    private static boolean check(Comparable[] array) {

        //Insert all values in the map
        Map<Comparable, Integer> valuesMap = new HashMap<>();

        for(Comparable value : array) {
            int count = 0;

            if (valuesMap.containsKey(value)) {
                count = valuesMap.get(value);
            }

            count++;
            valuesMap.put(value, count);
        }

        Arrays.sort(array);

        //Check if array is sorted
        for(int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0) {
                return false;
            }
        }

        //Check if the initial set of objects is still in the array
        for(Comparable value : array) {
            if (valuesMap.containsKey(value)) {
                int count = valuesMap.get(value);
                count--;

                if (count == 0) {
                    valuesMap.remove(value);
                } else {
                    valuesMap.put(value, count);
                }
            } else {
                return false;
            }
        }

        if (valuesMap.size() > 0) {
            return false;
        }

        return true;
    }

}
