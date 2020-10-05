package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 25/03/17.
 */
//Based on: http://eranle.blogspot.com.br/2012/08/min-max-heap-java-implementation.html
// Thanks to YRFT (https://github.com/YRFT) for finding that the method deleteItem() also needs to call the swim() method:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/181
@SuppressWarnings("unchecked")
public class Exercise29_MinMaxPriorityQueue  {

    private enum Orientation {
        MAX, MIN;
    }

    private class PQNode implements Comparable<PQNode>{
        Comparable key;
        int minHeapIndex;
        int maxHeapIndex;

        @Override
        public int compareTo(PQNode other) {
            return key.compareTo(other.key);
        }
    }

    private class MinMaxPriorityQueue<Key extends Comparable<Key>> {
        private PQNode[] minPriorityQueue;
        private PQNode[] maxPriorityQueue;
        private int size = 0;

        MinMaxPriorityQueue() {
            minPriorityQueue = new PQNode[2];
            maxPriorityQueue = new PQNode[2];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void insert(Key key) {
            if (size == minPriorityQueue.length - 1) {
                resize(minPriorityQueue.length * 2);
            }

            PQNode pqNode = new PQNode();
            pqNode.key = key;

            size++;

            insertOnMinHeap(pqNode);
            insertOnMaxHeap(pqNode);
        }

        private void insertOnMinHeap(PQNode pqNode) {
            minPriorityQueue[size] = pqNode;
            swim(minPriorityQueue, size, Orientation.MIN);
        }

        private void insertOnMaxHeap(PQNode pqNode) {
            maxPriorityQueue[size] = pqNode;
            swim(maxPriorityQueue, size, Orientation.MAX);
        }

        //O(1)
        public Comparable findMin() {
            if (size == 0) {
                return null;
            }

            return minPriorityQueue[1].key;
        }

        //O(1)
        public Comparable findMax() {
            if (size == 0) {
                return null;
            }

            return maxPriorityQueue[1].key;
        }

        //O(lg N)
        public Comparable deleteMax() {
            if (size == 0) {
                throw new RuntimeException("Priority queue underflow");
            }

            size--;

            PQNode max = maxPriorityQueue[1];

            deleteTopItem(maxPriorityQueue, Orientation.MAX);
            deleteItem(minPriorityQueue, Orientation.MIN, max.minHeapIndex);

            if (size == minPriorityQueue.length / 4) {
                resize(minPriorityQueue.length / 2);
            }

            return max.key;
        }

        //O(lg N)
        public Comparable deleteMin() {
            if (size == 0) {
                throw new RuntimeException("Priority queue underflow");
            }

            size--;

            PQNode min = minPriorityQueue[1];

            deleteTopItem(minPriorityQueue, Orientation.MIN);
            deleteItem(maxPriorityQueue, Orientation.MAX, min.maxHeapIndex);

            if (size == minPriorityQueue.length / 4) {
                resize(minPriorityQueue.length / 2);
            }

            return min.key;
        }

        private void deleteTopItem(PQNode[] priorityQueue, Orientation orientation) {
            deleteItem(priorityQueue, orientation, 1);
        }

        private void deleteItem(PQNode[] priorityQueue, Orientation orientation, int index) {
            ArrayUtil.exchange(priorityQueue, index, size + 1);
            priorityQueue[size + 1] = null;

            if (index == size + 1) {
                //We deleted the last value, so no need to sink
                return;
            }

            sink(priorityQueue, index, orientation);
            swim(priorityQueue, index, orientation);
        }

        private void swim(PQNode[] priorityQueue, int index, Orientation orientation) {
            while(index / 2 >= 1) {
                if ((orientation == Orientation.MAX && ArrayUtil.less(priorityQueue[index / 2], priorityQueue[index]))
                        || (orientation == Orientation.MIN && ArrayUtil.more(priorityQueue[index / 2], priorityQueue[index]))) {
                    ArrayUtil.exchange(priorityQueue, index / 2, index);

                    if (orientation == Orientation.MIN) {
                        priorityQueue[index].minHeapIndex = index;
                        priorityQueue[index / 2].minHeapIndex = index / 2;
                    } else {
                        priorityQueue[index].maxHeapIndex = index;
                        priorityQueue[index / 2].maxHeapIndex = index / 2;
                    }
                } else {
                    break;
                }

                index = index / 2;
            }

            //Even if there were no exchanges, we still need to update the index
            if (orientation == Orientation.MIN) {
                priorityQueue[index].minHeapIndex = index;
            } else {
                priorityQueue[index].maxHeapIndex = index;
            }
        }

        private void sink(PQNode[] priorityQueue, int index, Orientation orientation) {
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

                if ((orientation == Orientation.MAX && ArrayUtil.more(priorityQueue[selectedChildIndex], priorityQueue[index]))
                        || (orientation == Orientation.MIN && ArrayUtil.less(priorityQueue[selectedChildIndex], priorityQueue[index]))) {
                    ArrayUtil.exchange(priorityQueue, index, selectedChildIndex);

                    if (orientation == Orientation.MIN) {
                        priorityQueue[index].minHeapIndex = index;
                        priorityQueue[selectedChildIndex].minHeapIndex = selectedChildIndex;
                    } else {
                        priorityQueue[index].maxHeapIndex = index;
                        priorityQueue[selectedChildIndex].maxHeapIndex = selectedChildIndex;
                    }
                } else {
                    break;
                }

                index = selectedChildIndex;
            }

            //Even if there were no exchanges, we still need to update the index
            if (orientation == Orientation.MIN) {
                priorityQueue[index].minHeapIndex = index;
            } else {
                priorityQueue[index].maxHeapIndex = index;
            }
        }

        private void resize(int newSize) {
            //Min heap
            PQNode[] newMinPriorityQueue = new PQNode[newSize];
            System.arraycopy(minPriorityQueue, 1, newMinPriorityQueue, 1, size);
            minPriorityQueue = newMinPriorityQueue;

            //Max heap
            PQNode[] newMaxPriorityQueue = new PQNode[newSize];
            System.arraycopy(maxPriorityQueue, 1, newMaxPriorityQueue, 1, size);
            maxPriorityQueue = newMaxPriorityQueue;
        }
    }

    public static void main(String[] args) {
        Exercise29_MinMaxPriorityQueue.MinMaxPriorityQueue<Integer> minMaxPriorityQueue =
                new Exercise29_MinMaxPriorityQueue().new MinMaxPriorityQueue();

        minMaxPriorityQueue.insert(10);
        minMaxPriorityQueue.insert(2);
        minMaxPriorityQueue.insert(40);
        minMaxPriorityQueue.insert(1);

        StdOut.println("Delete Max: " + minMaxPriorityQueue.deleteMax() + " Expected: 40");
        StdOut.println("Delete Min: " + minMaxPriorityQueue.deleteMin() + " Expected: 1");

        StdOut.println("Find Max: " + minMaxPriorityQueue.findMax() + " Expected: 10");
        StdOut.println("Find Min: " + minMaxPriorityQueue.findMin() + " Expected: 2");

        minMaxPriorityQueue.insert(99);
        minMaxPriorityQueue.insert(-1);

        StdOut.println("Find Max: " + minMaxPriorityQueue.findMax() + " Expected: 99");
        StdOut.println("Find Min: " + minMaxPriorityQueue.findMin() + " Expected: -1");
    }

}
