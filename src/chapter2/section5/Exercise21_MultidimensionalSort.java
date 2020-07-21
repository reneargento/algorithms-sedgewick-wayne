package chapter2.section5;

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Comparator;


public class Exercise21_MultidimensionalSort {

    public static void main(String[] args) {

        StdRandom.setSeed(1);

        var vectors = new int[25][];
        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = new int[5];
            for (int j = 0; j < vectors[i].length; j++) {
                vectors[i][j] = StdRandom.uniform(0,3);
            }
        }

        var breakpoints = new ArrayList<Integer>();
        breakpoints.add(0);
        breakpoints.add(vectors.length);

        sort_by_dim(vectors, 0, 4, 0, 25);

        System.out.println();

    }


    public static void sort_by_dim(int[][] vectors, int current_dim, int max_dim, int left_bound, int right_bound) {

        VecSort comparator = new VecSort(current_dim);
        sort(vectors, comparator, left_bound, right_bound);

        if (current_dim == max_dim)
            return;

        var left_bound_new = left_bound;
        var right_bound_new = -1;
        var flag = false;
        for (int i = left_bound; i < right_bound - 1; i++) {
            if (vectors[i][current_dim] == vectors[i+1][current_dim]) {
            } else {
                flag = true;
                right_bound_new = i + 1;
                sort_by_dim(vectors, current_dim+1, max_dim, left_bound_new, right_bound_new);
                left_bound_new = i + 1;
            }
        }
        if (!flag) {
            sort_by_dim(vectors, current_dim+1, max_dim, left_bound, right_bound);
        }
        if (right_bound_new != -1)
            sort_by_dim(vectors, current_dim+1, max_dim, right_bound_new, right_bound);

    }

    public static void sort(Object[] a, Comparator comparator, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j-1], comparator); j--) {
                exch(a, j, j-1);
            }
        }
    }

    // is v < w ?
    private static boolean less(Object v, Object w, Comparator comparator) {
        return comparator.compare(v, w) < 0;
    }
        
    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}

class VecSort implements Comparator<int[]> {

    public int dimension;

    public VecSort(int dimension) {
        this.dimension = dimension;
    }

    public int compare(int[] a, int[] b) {
        if (a[dimension] > b[dimension]){
            return 1;
        } else if (a[dimension] < b[dimension]){
            return -1;
        } else return 0;
    }
}
