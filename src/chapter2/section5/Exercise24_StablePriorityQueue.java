package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 15/04/17.
 */
//Based on http://algs4.cs.princeton.edu/25applications/StableMinPQ.java.html
@SuppressWarnings("unchecked")
public class Exercise24_StablePriorityQueue {

    public enum Orientation {
        MAX, MIN;
    }

    private class PriorityQueueStable<Key extends Comparable<Key>> {

        private Key[] priorityQueue;
        private int size = 0; // in priorityQueue[1..n] with pq[0] unused
        private Orientation orientation;

        private long[] timestamp;
        private int currentTimestamp = 0;

        PriorityQueueStable(Orientation orientation) {
            priorityQueue = (Key[]) new Comparable[2];
            timestamp = new long[2];
            this.orientation = orientation;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public Key peek() {
            return priorityQueue[1];
        }

        public void insert(Key key) {

            if (size == priorityQueue.length - 1) {
                resize(priorityQueue.length * 2);
            }

            size++;
            priorityQueue[size] = key;
            timestamp[size] = ++currentTimestamp;

            swim(size);
        }

        public Key deleteTop() {

            if (size == 0) {
                throw new RuntimeException("Priority queue underflow");
            }

            size--;

            Key top = priorityQueue[1];

            exchange(1, size + 1);
            priorityQueue[size + 1] = null;
            timestamp[size + 1] = 0;

            sink(1);

            if (size == priorityQueue.length / 4) {
                resize(priorityQueue.length / 2);
            }

            return top;
        }

        private void swim(int index) {
            while(index / 2 >= 1) {
                if ((orientation == Orientation.MAX && less(index / 2, index))
                        || (orientation == Orientation.MIN && more(index / 2, index))) {
                    exchange(index / 2, index);
                } else {
                    break;
                }

                index = index / 2;
            }
        }

        private void sink(int index) {
            while (index * 2 <= size) {
                int selectedChildIndex = index * 2;

                if (index * 2 + 1 <= size &&
                        ((orientation == Orientation.MAX && less(index * 2, index * 2 + 1))
                                || (orientation == Orientation.MIN && more(index * 2, index * 2 + 1)))) {
                    selectedChildIndex = index * 2 + 1;
                }

                if ((orientation == Orientation.MAX && more(selectedChildIndex, index))
                        || (orientation == Orientation.MIN && less(selectedChildIndex, index))) {
                    exchange(index, selectedChildIndex);
                } else {
                    break;
                }

                index = selectedChildIndex;
            }
        }

        private void resize(int newSize) {
            Key[] newPriorityQueue = (Key[]) new Comparable[newSize];
            System.arraycopy(priorityQueue, 1, newPriorityQueue, 1, size);
            priorityQueue = newPriorityQueue;

            long[] newTimestamp = new long[newSize];
            System.arraycopy(timestamp, 1, newTimestamp, 1, size);
            timestamp = newTimestamp;
        }

        private boolean less(int key1Index, int key2Index) {
            int compare = priorityQueue[key1Index].compareTo(priorityQueue[key2Index]);

            if (compare < 0) {
                return true;
            } else if (compare > 0) {
                return false;
            } else {
                return timestamp[key1Index] < timestamp[key2Index];
            }
        }

        private boolean more(int key1Index, int key2Index) {
            int compare = priorityQueue[key1Index].compareTo(priorityQueue[key2Index]);

            if (compare > 0) {
                return true;
            } else if (compare < 0) {
                return false;
            } else {
                return timestamp[key1Index] > timestamp[key2Index];
            }
        }

        private void exchange(int key1Index, int key2Index) {
            Key tempKey = priorityQueue[key1Index];
            priorityQueue[key1Index] = priorityQueue[key2Index];
            priorityQueue[key2Index] = tempKey;

            long tempTimestamp = timestamp[key1Index];
            timestamp[key1Index] = timestamp[key2Index];
            timestamp[key2Index] = tempTimestamp;
        }
    }

    private class Tuple implements Comparable<Tuple> {

        private String value;
        private int id;

        Tuple(String value, int id) {
            this.value = value;
            this.id = id;
        }

        @Override
        public int compareTo(Tuple that) {
            return this.value.compareTo(that.value);
        }

        @Override
        public String toString() {
            return value + " " + id;
        }
    }

    public static void main(String[] args) {
        Exercise24_StablePriorityQueue stablePriorityQueue = new Exercise24_StablePriorityQueue();
        PriorityQueueStable<Tuple> priorityQueueStable = stablePriorityQueue.new PriorityQueueStable<>(Orientation.MIN);

        // Insert a bunch of strings
        String text = "it was the best of times it was the worst of times it was the "
                + "age of wisdom it was the age of foolishness it was the epoch "
                + "belief it was the epoch of incredulity it was the season of light "
                + "it was the season of darkness it was the spring of hope it was the "
                + "winter of despair";

        String[] strings = text.split(" ");

        for (int i = 0; i < strings.length; i++) {
            priorityQueueStable.insert(stablePriorityQueue.new Tuple(strings[i], i));
        }

        // Delete and print each key
        while (!priorityQueueStable.isEmpty()) {
            StdOut.println(priorityQueueStable.deleteTop());
        }
    }

}
