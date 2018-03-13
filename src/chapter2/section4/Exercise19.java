package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 19/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise19 {

    private class MaxPQ {

        private Comparable[] priorityQueue;

        MaxPQ(Comparable[] array) {
            priorityQueue = array;

            //Heapify - O(N)
            int startPosition = (array.length - 1) / 2; //Position 0 is not used

            for(int i = startPosition; i >= 1; i--) {
                sink(i);
            }
        }

        private void sink(int index) {

            int indexOfBiggestChild;

            while(index * 2 < priorityQueue.length) {

                indexOfBiggestChild = index * 2;

                if (indexOfBiggestChild + 1 < priorityQueue.length &&
                        priorityQueue[indexOfBiggestChild].compareTo(priorityQueue[indexOfBiggestChild + 1]) < 0) {
                    indexOfBiggestChild = indexOfBiggestChild + 1;
                }

                if (priorityQueue[index].compareTo(priorityQueue[indexOfBiggestChild]) < 0) {
                    ArrayUtil.exchange(priorityQueue, index, indexOfBiggestChild);
                } else {
                    break;
                }

                index = indexOfBiggestChild;
            }
        }

        Comparable[] getPriorityQueue() {
            return priorityQueue;
        }

    }

    public static void main(String[] args) {
        // Position 0 is not used, so -9999 element is ignored
        Comparable[] array = {-9999, 0, -1, 1, -2, 2, -3, 3, -4, 4, -5, 5, -6, 6, -7, 7, -8, 8, -9, 9};

        String initialArray = Arrays.toString(array);
        StdOut.println("Initial array: " + initialArray);

        MaxPQ maxPQ = new Exercise19().new MaxPQ(array);

        Comparable[] priorityQueue = maxPQ.getPriorityQueue();

        StringJoiner maxHeapArray = new StringJoiner(" ");

        for(int i = 1; i < priorityQueue.length; i++) {
            maxHeapArray.add(String.valueOf(priorityQueue[i]));
        }
        StdOut.println("Max heap array: " + maxHeapArray);
    }

}
