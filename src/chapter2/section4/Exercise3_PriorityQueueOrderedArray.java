package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 16/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3_PriorityQueueOrderedArray {

    private class PriorityQueueOrderedArray<Key extends Comparable<Key>> {

        private Key[] priorityQueue;
        private int size = 0;

        PriorityQueueOrderedArray(int size) {
            priorityQueue = (Key[]) new Comparable[size];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        //O(N)
        public void insert(Key key) {
            if (size == priorityQueue.length) {
                throw new RuntimeException("Priority queue is full");
            }

            int i = size - 1;

            while (i >= 0 && ArrayUtil.less(key, priorityQueue[i])) {
                priorityQueue[i + 1] = priorityQueue[i];
                i--;
            }

            priorityQueue[i + 1] = key;
            size++;
        }

        //O(1)
        public Key removeMax() {
            if (isEmpty()) {
                throw new RuntimeException("Priority queue underflow");
            }

            Key maxValue = priorityQueue[size - 1];

            priorityQueue[size - 1] = null;
            size--;

            return maxValue;
        }
    }

    public static void main(String[] args) {
        testPriorityQueueOrderedArray();
    }

    private static void testPriorityQueueOrderedArray() {

        PriorityQueueOrderedArray<Integer> priorityQueueOrderedArray = new Exercise3_PriorityQueueOrderedArray().new PriorityQueueOrderedArray<>(5);
        priorityQueueOrderedArray.insert(2);
        priorityQueueOrderedArray.insert(10);
        priorityQueueOrderedArray.insert(4);
        priorityQueueOrderedArray.insert(1);

        StdOut.println("Max value: " + priorityQueueOrderedArray.removeMax() + " - Expected: 10");
        StdOut.println("Max value: " + priorityQueueOrderedArray.removeMax() + " - Expected: 4");
        StdOut.println("Max value: " + priorityQueueOrderedArray.removeMax() + " - Expected: 2");
        StdOut.println("Max value: " + priorityQueueOrderedArray.removeMax() + " - Expected: 1");
    }

}
