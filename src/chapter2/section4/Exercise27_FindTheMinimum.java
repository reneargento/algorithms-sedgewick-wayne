package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 25/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise27_FindTheMinimum {

    private class PriorityQueue<Key extends Comparable<Key>> {

        private Key[] priorityQueue;
        private int size = 0; // in priorityQueue[1..n] with pq[0] unused

        private Key min;

        PriorityQueue(int size) {
            priorityQueue = (Key[]) new Comparable[size + 1];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void insert(Key key) {
            if (size != priorityQueue.length - 1) {
                size++;

                if (min == null || ArrayUtil.less(key, min)) {
                    min = key;
                }

                priorityQueue[size] = key;
                swim(size);
            }
        }

        Key deleteMax() {
            if (size == 0) {
                throw new RuntimeException("Priority queue underflow");
            }

            size--;

            Key max = priorityQueue[1];
            ArrayUtil.exchange(priorityQueue, 1, size + 1);

            if (max == min) {
                min = null;
            }

            priorityQueue[size + 1] = null;
            sink(1);

            return max;
        }

        private void swim(int index) {
            while(index / 2 >= 1 && ArrayUtil.less(priorityQueue[index / 2], priorityQueue[index])) {
                ArrayUtil.exchange(priorityQueue, index / 2, index);

                index = index / 2;
            }
        }

        private void sink(int index) {
            while (index * 2 <= size) {
                int selectedChildIndex = index * 2;

                if (index * 2 + 1 <= size && ArrayUtil.less(priorityQueue[index * 2], priorityQueue[index * 2 + 1])) {
                    selectedChildIndex = index * 2 + 1;
                }

                if (ArrayUtil.more(priorityQueue[selectedChildIndex], priorityQueue[index])) {
                    ArrayUtil.exchange(priorityQueue, index, selectedChildIndex);
                } else {
                    break;
                }

                index = selectedChildIndex;
            }
        }

        public Key min() {
            return min;
        }
    }

    public static void main(String[] args) {
        Exercise27_FindTheMinimum.PriorityQueue<Integer> priorityQueue =
                new Exercise27_FindTheMinimum().new PriorityQueue(5);

        StdOut.println("Min: " + priorityQueue.min() + " Expected: null");

        priorityQueue.insert(10);

        StdOut.println("Min: " + priorityQueue.min() + " Expected: 10");

        priorityQueue.insert(2);

        StdOut.println("Min: " + priorityQueue.min() + " Expected: 2");

        priorityQueue.insert(7);

        StdOut.println("Min: " + priorityQueue.min() + " Expected: 2");

        priorityQueue.insert(20);
        priorityQueue.insert(1);

        StdOut.println("Min: " + priorityQueue.min() + " Expected: 1");

        StdOut.println("Item removed: " + priorityQueue.deleteMax());
        StdOut.println("Item removed: " + priorityQueue.deleteMax());
        StdOut.println("Item removed: " + priorityQueue.deleteMax());
        StdOut.println("Item removed: " + priorityQueue.deleteMax());

        StdOut.println("Min: " + priorityQueue.min() + " Expected: 1");

        StdOut.println("Item removed: " + priorityQueue.deleteMax());

        priorityQueue.insert(99);
        StdOut.println("Min: " + priorityQueue.min() + " Expected: 99");
    }
}
