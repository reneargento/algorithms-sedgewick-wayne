package chapter2.section5;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

/**
 * Created by Rene Argento on 13/04/17.
 * Improved by UniverseObserver (https://github.com/UniverseObserver) on 21/07/20.
 */
public class Exercise21_MultidimensionalSort {

    private static class VectorSort implements Comparator<int[]> {
        public int dimension;

        public VectorSort(int dimension) {
            this.dimension = dimension;
        }

        public int compare(int[] a, int[] b) {
            if (a[dimension] > b[dimension]){
                return 1;
            } else if (a[dimension] < b[dimension]){
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static void sortByDimension(int[][] vectors, int currentDimension, int maxDimension, int leftBound,
                                       int rightBound) {
        VectorSort comparator = new VectorSort(currentDimension);
        sort(vectors, comparator, leftBound, rightBound);

        if (currentDimension == maxDimension) {
            return;
        }

        int leftBoundNew = leftBound;
        int rightBoundNew = -1;
        boolean hasDifferentValues = false;

        for (int i = leftBound; i < rightBound - 1; i++) {
            if (vectors[i][currentDimension] != vectors[i + 1][currentDimension]) {
                hasDifferentValues = true;
                rightBoundNew = i + 1;
                sortByDimension(vectors, currentDimension + 1, maxDimension, leftBoundNew, rightBoundNew);
                leftBoundNew = i + 1;
            }
        }

        if (!hasDifferentValues) {
            sortByDimension(vectors, currentDimension + 1, maxDimension, leftBound, rightBound);
        }
        if (rightBoundNew != -1) {
            sortByDimension(vectors, currentDimension + 1, maxDimension, rightBoundNew, rightBound);
        }
    }

    public static void sort(int[][] array, Comparator<int[]> comparator, int low, int high) {
        for (int i = low + 1; i < high; i++) {
            for (int j = i; j > low && less(array[j], array[j - 1], comparator); j--) {
                exchange(array, j, j - 1);
            }
        }
    }

    // Is value1 < value2 ?
    private static boolean less(int[] value1, int[] value2, Comparator<int[]> comparator) {
        return comparator.compare(value1, value2) < 0;
    }

    // Exchange array[i] and array[j]
    private static void exchange(Object[] array, int i, int j) {
        Object aux = array[i];
        array[i] = array[j];
        array[j] = aux;
    }

    public static void main(String[] args) {
        int[][] vectors = new int[25][5];

        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors[i].length; j++) {
                vectors[i][j] = StdRandom.uniform(0,3);
            }
        }

        sortByDimension(vectors, 0, vectors[0].length - 1, 0, vectors.length);
    }
}
