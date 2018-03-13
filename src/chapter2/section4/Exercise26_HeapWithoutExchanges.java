package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 25/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise26_HeapWithoutExchanges {

    private enum Orientation {
        MAX, MIN;
    }

    private class PriorityQueue<Key extends Comparable<Key>> {

        private Key[] priorityQueue;
        private int size = 0; // in priorityQueue[1..n] with pq[0] unused
        private Orientation orientation;

        PriorityQueue(int size, Orientation orientation) {
            priorityQueue = (Key[]) new Comparable[size + 1];
            this.orientation = orientation;
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

                priorityQueue[size] = key;
                swim(size);
            }
        }

        Key deleteTop() {
            if (size == 0) {
                throw new RuntimeException("Priority queue underflow");
            }

            size--;

            Key topElement = priorityQueue[1];
            ArrayUtil.exchange(priorityQueue, 1, size + 1);

            priorityQueue[size + 1] = null;
            sink(1);

            return topElement;
        }

        private void swim(int index) {
            Key aux = priorityQueue[index];

            boolean exchangeRequired = false;

            while(index / 2 >= 1) {
                if ((orientation == Orientation.MAX && ArrayUtil.less(priorityQueue[index / 2], aux))
                        || (orientation == Orientation.MIN && ArrayUtil.more(priorityQueue[index / 2], aux))) {
                    priorityQueue[index] = priorityQueue[index / 2];
                    exchangeRequired = true;
                } else {
                    break;
                }

                index = index / 2;
            }

            if (exchangeRequired) {
                priorityQueue[index] = aux;
            }
        }

        private void sink(int index) {

            Key aux = priorityQueue[index];

            while (index * 2 <= size) {
                int selectedChildIndex = index * 2;

                if (index * 2 + 1 <= size &&
                        (
                         (orientation == Orientation.MAX && ArrayUtil.less(priorityQueue[index * 2], priorityQueue[index * 2 + 1]))
                               || (orientation == Orientation.MIN && ArrayUtil.more(priorityQueue[index * 2], priorityQueue[index * 2 + 1]))
                        )
                        ) {
                    selectedChildIndex = index * 2 + 1;
                }

                if ((orientation == Orientation.MAX && ArrayUtil.more(priorityQueue[selectedChildIndex], aux))
                        || (orientation == Orientation.MIN && ArrayUtil.less(priorityQueue[selectedChildIndex], aux))) {
                    priorityQueue[index] = priorityQueue[selectedChildIndex];
                } else {
                    break;
                }

                index = selectedChildIndex;
            }

            //No need to check if an exchange is required.
            //The value of index is only updated when an exchange happens.
            priorityQueue[index] = aux;
        }
    }

    public static void main(String[] args) {
        Exercise26_HeapWithoutExchanges.PriorityQueue<Integer> priorityQueue =
                new Exercise26_HeapWithoutExchanges().new PriorityQueue(4, Orientation.MAX);

        priorityQueue.insert(10);
        priorityQueue.insert(2);
        priorityQueue.insert(7);
        priorityQueue.insert(20);

        StdOut.println("Size: " + priorityQueue.size());
        StdOut.println("isEmpty: " + priorityQueue.isEmpty());

        StdOut.println("Item removed: " + priorityQueue.deleteTop() + " Expected: 20");
        StdOut.println("Item removed: " + priorityQueue.deleteTop() + " Expected: 10");
        StdOut.println("Item removed: " + priorityQueue.deleteTop() + " Expected: 7");
        StdOut.println("Item removed: " + priorityQueue.deleteTop() + " Expected: 2");
    }

}
