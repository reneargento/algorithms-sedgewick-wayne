package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 03/06/17.
 */
public class Exercise25_PerfectBalance {

    public static void main(String[] args) {
        int[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        //In the test we are using an already sorted array, but any array could be used for this program
        Arrays.sort(keys);

        BinarySearchTree<Integer, Integer> binarySearchTree = new BinarySearchTree<>();
        Exercise25_PerfectBalance exercise25_perfectBalance = new Exercise25_PerfectBalance();
        exercise25_perfectBalance.insertBalancedValues(keys, binarySearchTree);
    }

    private void insertBalancedValues(int[] values, BinarySearchTree<Integer, Integer> binarySearchTree) {
        insertBalancedValues(values, binarySearchTree, 0, values.length - 1);
    }

    private void insertBalancedValues(int[] values, BinarySearchTree<Integer, Integer> binarySearchTree, int low, int high) {
        if (low > high) {
            return;
        }

        int middle = low + (high - low) / 2;

        binarySearchTree.put(values[middle], values[middle]);
        StdOut.print(values[middle] + " ");

        insertBalancedValues(values, binarySearchTree, low, middle - 1);
        insertBalancedValues(values, binarySearchTree, middle + 1, high);
    }

}
