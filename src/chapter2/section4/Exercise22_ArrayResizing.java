package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 20/03/17.
 */
public class Exercise22_ArrayResizing {

    @SuppressWarnings("unchecked")
    private class PriorityQueueResize<Key extends Comparable<Key>> {

        private Key[] priorityQueue; // heap-ordered complete binary tree
        private int size = 0; // in priorityQueue[1..n] with pq[0] unused

        int totalArrayAccesses;
        int totalArrayAccessesForInsert;
        int totalArrayAccessesForRemoveMaximum;

        int totalItemsInserted;
        int totalItemsRemoved;

        private PriorityQueueResize() {
            priorityQueue = (Key[]) new Comparable[2];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void insert(Key key) {
            totalArrayAccesses = 0;
            totalItemsInserted++;

            if (size == priorityQueue.length - 1) {
                resize(priorityQueue.length * 2, true);
            }

            size++;

            priorityQueue[size] = key;
            totalArrayAccesses++;
            totalArrayAccessesForInsert++;

            swim(size);
        }

        public Key deleteMax() {
            totalArrayAccesses = 0;
            totalItemsRemoved++;

            if (size == 0) {
                throw new RuntimeException("Priority queue underflow");
            }

            size--;

            Key max = priorityQueue[1];
            totalArrayAccesses++;
            totalArrayAccessesForRemoveMaximum++;

            ArrayUtil.exchange(priorityQueue, 1, size + 1);
            totalArrayAccesses += 4;
            totalArrayAccessesForRemoveMaximum += 4;

            priorityQueue[size + 1] = null;
            totalArrayAccesses++;
            totalArrayAccessesForRemoveMaximum++;

            sink(1);

            if (size == priorityQueue.length / 4) {
                resize(priorityQueue.length / 2, false);
            }

            return max;
        }

        private void swim(int index) {
            while(index / 2 >= 1) {

                totalArrayAccesses += 2;
                totalArrayAccessesForInsert += 2;
                if (ArrayUtil.less(priorityQueue[index / 2], priorityQueue[index])) {
                    ArrayUtil.exchange(priorityQueue, index / 2, index);
                    totalArrayAccesses += 4;
                    totalArrayAccessesForInsert += 4;
                } else {
                    break;
                }

                index = index / 2;
            }
        }

        private void sink(int index) {
            while (index * 2 <= size) {
                int highestChildIndex = index * 2;

                totalArrayAccesses += 2;
                totalArrayAccessesForRemoveMaximum += 2;
                if (index * 2 + 1 <= size && ArrayUtil.less(priorityQueue[index * 2], priorityQueue[index * 2 + 1])) {
                    highestChildIndex = index * 2 + 1;
                }

                totalArrayAccesses += 2;
                totalArrayAccessesForRemoveMaximum += 2;
                if (ArrayUtil.more(priorityQueue[highestChildIndex], priorityQueue[index])) {
                    ArrayUtil.exchange(priorityQueue, index, highestChildIndex);
                    totalArrayAccesses += 4;
                    totalArrayAccessesForRemoveMaximum += 4;
                } else {
                    break;
                }

                index = highestChildIndex;
            }
        }

        private void resize(int newSize, boolean increasing) {
            Key[] newPriorityQueue = (Key[]) new Comparable[newSize];

            System.arraycopy(priorityQueue, 1, newPriorityQueue, 1, size);

            totalArrayAccesses += 2 * size;
            if (increasing) {
                totalArrayAccessesForInsert += 2 * size;
            } else {
                totalArrayAccessesForRemoveMaximum += 2 * size;
            }

            priorityQueue = newPriorityQueue;
        }

    }

    public static void main(String[] args) {
        PriorityQueueResize<Integer> priorityQueue = new Exercise22_ArrayResizing().new PriorityQueueResize<>();

        priorityQueue.insert(10);

        StdOut.println("Array accesses in insert: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() - 1));

        priorityQueue.insert(4);

        StdOut.println("Array accesses in insert: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() - 1));

        priorityQueue.insert(2);

        StdOut.println("Array accesses in insert: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() - 1));

        priorityQueue.deleteMax();

        StdOut.println("Array accesses in remove the maximum: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() + 1));

        priorityQueue.insert(11);

        StdOut.println("Array accesses in insert: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() - 1));

        priorityQueue.insert(6);

        StdOut.println("Array accesses in insert: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() - 1));

        priorityQueue.deleteMax();

        StdOut.println("Array accesses in remove the maximum: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() + 1));

        priorityQueue.deleteMax();

        StdOut.println("Array accesses in remove the maximum: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() + 1));

        priorityQueue.deleteMax();

        StdOut.println("Array accesses in remove the maximum: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() + 1));

        priorityQueue.deleteMax();

        StdOut.println("Array accesses in remove the maximum: " + priorityQueue.totalArrayAccesses
                + " N = " + (priorityQueue.size() + 1));

        //Amortized analysis
        int lgInsertedItems = (int) (Math.log10(priorityQueue.totalItemsInserted) / Math.log10(2));
        int lgRemovedItems = (int) (Math.log10(priorityQueue.totalItemsRemoved) / Math.log10(2));

        double twoPowerLgArrayAccessesForInsertMinus1 = Math.ceil(Math.pow(2, Math.log(priorityQueue.totalArrayAccessesForInsert) / Math.log(2) - 1));
        double twoPowerLgArrayAccessesForRemoveMaximumMinus1 = Math.ceil(Math.pow(2, Math.log(priorityQueue.totalArrayAccessesForRemoveMaximum) / Math.log(2) - 1));

        StdOut.println();
        StdOut.println("Amortized array accesses in insert: " + priorityQueue.totalArrayAccessesForInsert
                + ": ceil(2^((lg ArrayAccess) - 1)) * lg N = " + (twoPowerLgArrayAccessesForInsertMinus1 * lgInsertedItems));
        StdOut.println("Amortized array accesses in remove the maximum: " + priorityQueue.totalArrayAccessesForRemoveMaximum
                + ": ceil(2^((lg ArrayAccess) - 1)) * lg N = " + (twoPowerLgArrayAccessesForRemoveMaximumMinus1 * lgRemovedItems));

        StdOut.println();
        StdOut.println("Proposition: In a N-key priority queue, the heap algorithms require no more than" +
                " ceil(2^((lg ArrayAccesses) - 1)) * lg N amortized array accesses for insert and for remove the maximum.");
    }

}