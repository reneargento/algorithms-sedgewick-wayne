package chapter2.section5;

import chapter2.section1.InsertionSort;
import chapter2.section1.SelectionSort;
import chapter2.section1.ShellSort;
import chapter2.section2.TopDownMergeSort;
import chapter2.section3.QuickSort;
import chapter2.section4.HeapSort;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/04/17.
 */
// Thanks to dbf256 (https://github.com/dbf256) for finding an issue on the heapsort copy array construction.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/50
public class Exercise11 {

    private class PermutationNumber implements Comparable<PermutationNumber>{
        private int value;
        private int originalIndex;

        PermutationNumber(int value, int originalIndex) {
            this.value = value;
            this.originalIndex = originalIndex;
        }

        @Override
        public int compareTo(PermutationNumber that) {
            if (this.value < that.value) {
                return -1;
            } else if (this.value > that.value) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        Exercise11 exercise11 = new Exercise11();
        PermutationNumber number1 = exercise11.new PermutationNumber(0, 0);
        PermutationNumber number2 = exercise11.new PermutationNumber(0, 1);
        PermutationNumber number3 = exercise11.new PermutationNumber(0, 2);
        PermutationNumber number4 = exercise11.new PermutationNumber(0, 3);
        PermutationNumber number5 = exercise11.new PermutationNumber(0, 4);
        PermutationNumber number6 = exercise11.new PermutationNumber(0, 5);
        PermutationNumber number7 = exercise11.new PermutationNumber(0, 6);
        PermutationNumber number8 = exercise11.new PermutationNumber(0, 7);

        PermutationNumber[] array = {number1, number2, number3, number4, number5, number6, number7, number8};
        exercise11.computePermutations(array);
    }

    private void computePermutations(PermutationNumber[] array) {
        // Insertion sort
        PermutationNumber[] insertionSortCopy = new PermutationNumber[array.length];
        System.arraycopy(array, 0, insertionSortCopy, 0, array.length);
        InsertionSort.insertionSort(insertionSortCopy);

        StdOut.println("Insertion Sort");
        int[] insertionSortPermutation = new int[array.length];
        for(int i = 0; i < insertionSortPermutation.length; i++) {
            insertionSortPermutation[insertionSortCopy[i].originalIndex] = i;
        }
        printPermutation(insertionSortPermutation);

        // Selection sort
        PermutationNumber[] selectionSortCopy = new PermutationNumber[array.length];
        System.arraycopy(array, 0, selectionSortCopy, 0, array.length);
        SelectionSort.selectionSort(selectionSortCopy);

        StdOut.println();
        StdOut.println("Selection Sort");
        int[] selectionSortPermutation = new int[array.length];
        for(int i = 0; i < selectionSortPermutation.length; i++) {
            selectionSortPermutation[selectionSortCopy[i].originalIndex] = i;
        }
        printPermutation(selectionSortPermutation);

        // Shell sort
        PermutationNumber[] shellSortCopy = new PermutationNumber[array.length];
        System.arraycopy(array, 0, shellSortCopy, 0, array.length);
        ShellSort.shellSort(shellSortCopy);

        StdOut.println();
        StdOut.println("Shell Sort");
        int[] shellSortPermutation = new int[array.length];
        for(int i = 0; i < shellSortPermutation.length; i++) {
            shellSortPermutation[shellSortCopy[i].originalIndex] = i;
        }
        printPermutation(shellSortPermutation);

        // Merge sort
        PermutationNumber[] mergeSortCopy = new PermutationNumber[array.length];
        System.arraycopy(array, 0, mergeSortCopy, 0, array.length);
        TopDownMergeSort.mergeSort(mergeSortCopy);

        StdOut.println();
        StdOut.println("Merge Sort");
        int[] mergeSortPermutation = new int[array.length];
        for(int i = 0; i < mergeSortPermutation.length; i++) {
            mergeSortPermutation[mergeSortCopy[i].originalIndex] = i;
        }
        printPermutation(mergeSortPermutation);

        // Quick sort
        PermutationNumber[] quickSortCopy = new PermutationNumber[array.length];
        System.arraycopy(array, 0, quickSortCopy, 0, array.length);
        QuickSort.quickSort(quickSortCopy);

        StdOut.println();
        StdOut.println("Quick Sort");
        int[] quickSortPermutation = new int[array.length];
        for(int i = 0; i < quickSortPermutation.length; i++) {
            quickSortPermutation[quickSortCopy[i].originalIndex] = i;
        }
        printPermutation(quickSortPermutation);

        // Heap sort
        PermutationNumber[] heapSortCopy = new PermutationNumber[array.length + 1];
        System.arraycopy(array, 0, heapSortCopy, 1, array.length);
        HeapSort.heapSort(heapSortCopy);

        StdOut.println();
        StdOut.println("Heap Sort");
        int[] heapSortPermutation = new int[array.length];
        for(int i = 0; i < heapSortPermutation.length; i++) {
            heapSortPermutation[heapSortCopy[i + 1].originalIndex] = i;
        }
        printPermutation(heapSortPermutation);
    }

    private void printPermutation(int[] permutation) {
        for(int i = 0; i < permutation.length; i++) {
            StdOut.println("Original index: " + i + " - New index: " + permutation[i]);
        }
    }
}
