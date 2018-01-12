package chapter5.section1;

import chapter1.section3.Queue;
import chapter2.section1.InsertionSort;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 10/01/18.
 */
// Based on https://www.geeksforgeeks.org/bucket-sort-2/
@SuppressWarnings("unchecked")
public class Exercise11_QueueSort {

    // O(N^2), but runs in O(N) if items are uniformly distributed in the buckets
    public class BucketSort {
        private int alphabetSize = 256; // Extended ASCII characters
        private Queue<String>[] buckets;

        public void bucketSort(String[] strings) {
            buckets = new Queue[alphabetSize + 1];

            for(int bucket = 0; bucket < buckets.length; bucket++) {
                buckets[bucket] = new Queue();
            }

            // 1- Put strings in bins
            for(int i = 0; i < strings.length; i++) {
                int leadingDigitIndex = strings[i].charAt(0);
                buckets[leadingDigitIndex].enqueue(strings[i]);
            }

            // 2- Sort the sublists
            for(int bucket = 0; bucket < buckets.length; bucket++) {
                if (!buckets[bucket].isEmpty()) {
                    sortBucket(buckets[bucket]);
                }
            }

            // 3- Stitch together all the buckets
            int arrayIndex = 0;

            for(int r = 0; r <= alphabetSize; r++) {
                while (!buckets[r].isEmpty()) {
                    String currentString = buckets[r].dequeue();
                    strings[arrayIndex++] = currentString;
                }
            }
        }

        private void sortBucket(Queue<String> bucket) {
            String[] strings = new String[bucket.size()];
            int arrayIndex = 0;

            while (!bucket.isEmpty()) {
                strings[arrayIndex++] = bucket.dequeue();
            }

            InsertionSort.insertionSort(strings);

            for(String string : strings) {
                bucket.enqueue(string);
            }
        }
    }

    public static void main(String[] args) {
        BucketSort bucketSort = new Exercise11_QueueSort().new BucketSort();

        String[] array1 = {"Rene", "Argento", "Arg", "Alg", "Algorithms", "LSD", "MSD", "3WayStringQuickSort",
                "Dijkstra", "Floyd", "Warshall", "Johnson", "Sedgewick", "Wayne", "Bellman", "Ford", "BFS", "DFS"};
        bucketSort.bucketSort(array1);

        StringJoiner sortedArray1 = new StringJoiner(" ");

        for(String string : array1) {
            sortedArray1.add(string);
        }
        StdOut.println("Sorted array 1");
        StdOut.println(sortedArray1);
        StdOut.println("Expected: \n3WayStringQuickSort Alg Algorithms Arg Argento BFS Bellman DFS Dijkstra Floyd Ford " +
                "Johnson LSD MSD Rene Sedgewick Warshall Wayne\n");

        String[] array2 = {"QuickSort", "MergeSort", "ShellSort", "InsertionSort", "BubbleSort", "SelectionSort"};
        bucketSort.bucketSort(array2);

        StringJoiner sortedArray2 = new StringJoiner(" ");

        for(String string : array2) {
            sortedArray2.add(string);
        }
        StdOut.println("Sorted array 2");
        StdOut.println(sortedArray2);
        StdOut.println("Expected: \nBubbleSort InsertionSort MergeSort QuickSort SelectionSort ShellSort");
    }

}
