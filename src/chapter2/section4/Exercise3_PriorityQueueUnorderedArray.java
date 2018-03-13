package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 15/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3_PriorityQueueUnorderedArray {

    private class PriorityQueueUnorderedArray<Key extends Comparable<Key>> {

        private Key[] priorityQueue;
        private int size = 0;

        PriorityQueueUnorderedArray(int size) {
            priorityQueue = (Key[]) new Comparable[size];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        //O(1)
        public void insert(Key key) {
            if (size == priorityQueue.length) {
                throw new RuntimeException("Priority queue is full");
            }

            priorityQueue[size] = key;
            size++;
        }

        //O(N)
        public Key removeMax() {
            if (isEmpty()) {
                throw new RuntimeException("Priority queue underflow");
            }

            int maxValueIndex = 0;

            for(int i = 1; i < size; i++) {
                if (ArrayUtil.less(priorityQueue[maxValueIndex], priorityQueue[i])) {
                    maxValueIndex = i;
                }
            }

            ArrayUtil.exchange(priorityQueue, maxValueIndex, size - 1);

            Key maxValue = priorityQueue[size - 1];

            priorityQueue[size - 1] = null;
            size--;

            return maxValue;
        }
    }

    public static void main(String[] args) {
        testPriorityQueueUnorderedArray();
    }

    private static void testPriorityQueueUnorderedArray() {

        PriorityQueueUnorderedArray<Integer> priorityQueueUnorderedArray = new Exercise3_PriorityQueueUnorderedArray().new PriorityQueueUnorderedArray<>(5);
        priorityQueueUnorderedArray.insert(2);
        priorityQueueUnorderedArray.insert(10);
        priorityQueueUnorderedArray.insert(4);
        priorityQueueUnorderedArray.insert(1);

        StdOut.println("Max value: " + priorityQueueUnorderedArray.removeMax() + " - Expected: 10");
        StdOut.println("Max value: " + priorityQueueUnorderedArray.removeMax() + " - Expected: 4");
        StdOut.println("Max value: " + priorityQueueUnorderedArray.removeMax() + " - Expected: 2");
        StdOut.println("Max value: " + priorityQueueUnorderedArray.removeMax() + " - Expected: 1");
    }

}
