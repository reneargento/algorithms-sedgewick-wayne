package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 26/03/17.
 */
//Based on http://algs4.cs.princeton.edu/24pq/IndexMaxPQ.java.html
public class Exercise33_IndexPriorityQueueImplementation {

    private class IndexMinPQ<Key extends Comparable<Key>> {

        private Key[] keys;
        private int[] pq; //Holds the indices of the keys
        private int[] qp; //Inverse of pq -> qp[i] gives the position of i in pq[] (the index j such that pq[j] is i).
                            // qp[pq[i]] = pq[qp[i]] = i
        private int size = 0;

        @SuppressWarnings("unchecked")
        public IndexMinPQ(int size) {
            keys = (Key[]) new Comparable[size + 1];
            pq = new int[size + 1];
            qp = new int[size + 1];

            for(int i = 0; i < qp.length; i++) {
                qp[i] = -1;
            }
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public boolean contains(int index) {
            return qp[index] != -1;
        }

        //Return key associated with index
        public Key keyOf(int index) {
            if (!contains(index)) {
                throw new NoSuchElementException("Index is not in the priority queue");
            }

            return keys[index];
        }

        public void insert(int index, Key key) {
            if (contains(index)) {
                throw new IllegalArgumentException("Index is already in the priority queue");
            }

            if (size != keys.length - 1) {
                size++;

                keys[index] = key;
                pq[size] = index;
                qp[index] = size;

                swim(size);
            }
        }

        //Remove a minimal key and return its index
        public int deleteMin() {
            if (size == 0) {
                throw new NoSuchElementException("Priority queue underflow");
            }

            int minElementIndex = pq[1];
            exchange(1, size);

            size--;
            sink(1);

            keys[pq[size + 1]] = null;
            qp[pq[size + 1]] = -1;

            return minElementIndex;
        }

        public Key minKey() {
            if (size == 0) {
                throw new NoSuchElementException("Priority queue underflow");
            }

            return keys[pq[1]];
        }

        private void swim(int index) {
            while(index / 2 >= 1 && more(index / 2, index)) {
                exchange(index / 2, index);
                index = index / 2;
            }
        }

        private void sink(int index) {
            while (index * 2 <= size) {
                int selectedChildIndex = index * 2;

                if (index * 2 + 1 <= size && more(index * 2, index * 2 + 1)) {
                    selectedChildIndex = index * 2 + 1;
                }

                if (less(selectedChildIndex, index)) {
                    exchange(index, selectedChildIndex);
                } else {
                    break;
                }

                index = selectedChildIndex;
            }
        }

        private boolean less(int keyIndex1, int keyIndex2) {
            return keys[pq[keyIndex1]].compareTo(keys[pq[keyIndex2]]) < 0;
        }

        private boolean more(int keyIndex1, int keyIndex2) {
            return keys[pq[keyIndex1]].compareTo(keys[pq[keyIndex2]]) > 0;
        }

        private void exchange(int keyIndex1, int keyIndex2) {
            int temp = pq[keyIndex1];
            pq[keyIndex1] = pq[keyIndex2];
            pq[keyIndex2] = temp;

            qp[pq[keyIndex1]] = keyIndex1;
            qp[pq[keyIndex2]] = keyIndex2;
        }
    }

    public static void main(String[] args) {
        // Insert a bunch of strings
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        Exercise33_IndexPriorityQueueImplementation.IndexMinPQ<String> priorityQueue =
                new Exercise33_IndexPriorityQueueImplementation().new IndexMinPQ<>(strings.length);

        for (int i = 0; i < strings.length; i++) {
            priorityQueue.insert(i, strings[i]);
        }

        StdOut.println("Key of index 4: " + priorityQueue.keyOf(4) + " Expected: of");

        // Delete and print each key
        while (!priorityQueue.isEmpty()) {
            String key = priorityQueue.minKey();
            int index = priorityQueue.deleteMin();
            StdOut.println(index + " " + key);
        }
    }

}
