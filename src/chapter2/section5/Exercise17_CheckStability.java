package chapter2.section5;

import chapter2.section3.QuickSort;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 11/04/17.
 */
@SuppressWarnings("unchecked")
public class Exercise17_CheckStability {

    private class Wrapper implements Comparable<Wrapper> {

        Comparable keyValue;
        int originalIndex;

        Wrapper(Comparable keyValue, int originalIndex) {
            this.keyValue = keyValue;
            this.originalIndex = originalIndex;
        }

        @Override
        public int compareTo(Wrapper that) {
            return this.keyValue.compareTo(that.keyValue);
        }
    }

    public static void main(String[] args) {
        Exercise17_CheckStability checkStability = new Exercise17_CheckStability();

        Comparable[] array = {2, 20, -1, -30, 30, 5, 6, 8, -99, -3, 0, 4, 4, 4};
        StdOut.println("Check: " + checkStability.check(array, true) + " Expected: true");
        StdOut.println("Check: " + checkStability.check(array, false) + " Expected: false");
    }

    private boolean check(Comparable[] array, boolean stable) {

        Wrapper[] wrappedKeys = new Wrapper[array.length];
        int wrappedKeysIndex = 0;
        for(int i = 0; i < array.length; i++) {
            Wrapper wrapper = new Wrapper(array[i], i);
            wrappedKeys[wrappedKeysIndex++] = wrapper;
        }

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

        if (stable) {
            //Mergesort
            Arrays.sort(array);
            Arrays.sort(wrappedKeys);
        } else {
            QuickSort.quickSort(array);
            QuickSort.quickSort(wrappedKeys);
        }

        //Check if array is sorted
        for(int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i+1]) > 0) {
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

        //Check if the elements were sorted in a stable manner
        for(int i = 0; i < wrappedKeys.length - 1; i++) {
            if (wrappedKeys[i].keyValue.compareTo(wrappedKeys[i+1].keyValue) == 0 &&
                    wrappedKeys[i].originalIndex > wrappedKeys[i+1].originalIndex) {
                return false;
            }
        }

        return true;
    }

}
