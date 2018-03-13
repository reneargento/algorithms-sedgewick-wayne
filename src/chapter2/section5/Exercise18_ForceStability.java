package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 11/04/17.
 */
public class Exercise18_ForceStability {

    private class Wrapper implements Comparable<Wrapper> {

        private Comparable keyValue;
        private int originalIndex;

        Wrapper(Comparable keyValue, int originalIndex) {
            this.keyValue = keyValue;
            this.originalIndex = originalIndex;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(Wrapper that) {
            int compare = this.keyValue.compareTo(that.keyValue);

            //Different keys
            if (compare != 0) {
                return compare;
            }

            //Equal keys
            if (this.originalIndex < that.originalIndex) {
                return -1;
            } else if (this.originalIndex > that.originalIndex) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        Comparable[] array = {2, 20, -1, -30, 30, 5, 6, 8, -99, -3, 0, 4, 4, 4};

        String originalArray = Arrays.toString(array);
        StdOut.println("Original array: " + originalArray);

        new Exercise18_ForceStability().sortInAStableWay(array);

        String sortedArray = Arrays.toString(array);
        StdOut.println("Sorted array: " + sortedArray);
    }

    private void sortInAStableWay(Comparable[] array) {

        Wrapper[] wrappedKeys = new Wrapper[array.length];
        int wrappedKeysIndex = 0;
        for(int i = 0; i < array.length; i++) {
            Wrapper wrapper = new Wrapper(array[i], i);
            wrappedKeys[wrappedKeysIndex++] = wrapper;
        }

        Arrays.sort(wrappedKeys);

        for(int i = 0; i < array.length; i++) {
            array[i] = wrappedKeys[i].keyValue;
        }
    }

}
