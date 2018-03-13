package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 26/03/17.
 */
public class Exercise30_DynamicMedianFinding {

    private class DynamicMedianFindingHeap<Key extends Comparable<Key>> {

        private PriorityQueueResize<Key> minPriorityQueue;
        private PriorityQueueResize<Key> maxPriorityQueue;

        private int size;

        DynamicMedianFindingHeap() {
            minPriorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);
            maxPriorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MAX);
            size = 0;
        }

        //O(lg N)
        public void insert(Key key) {

            if (size == 0 || ArrayUtil.less(key, maxPriorityQueue.peek())) {
                maxPriorityQueue.insert(key);
            } else {
                minPriorityQueue.insert(key);
            }

            if (minPriorityQueue.size() > maxPriorityQueue.size() + 1) {
                Key keyToBeMoved = minPriorityQueue.deleteTop();
                maxPriorityQueue.insert(keyToBeMoved);
            } else if (maxPriorityQueue.size() > minPriorityQueue.size() + 1) {
                Key keyToBeMoved = maxPriorityQueue.deleteTop();
                minPriorityQueue.insert(keyToBeMoved);
            }

            size++;
        }

        //O(1)
        public Key findTheMedian() {
            Key median;

            if (minPriorityQueue.size() > maxPriorityQueue.size()) {
                median = minPriorityQueue.peek();
            } else {
                median = maxPriorityQueue.peek();
            }

            return median;
        }

        //O(lg N)
        public Key deleteMedian() {
            Key median;

            if (minPriorityQueue.size() > maxPriorityQueue.size()) {
                median = minPriorityQueue.deleteTop();
            } else {
                median = maxPriorityQueue.deleteTop();
            }

            size--;

            return median;
        }
    }

    public static void main(String[] args) {
        Exercise30_DynamicMedianFinding.DynamicMedianFindingHeap<Integer> dynamicMedianFindingHeap =
                new Exercise30_DynamicMedianFinding().new DynamicMedianFindingHeap<>();

        dynamicMedianFindingHeap.insert(1);
        dynamicMedianFindingHeap.insert(2);
        dynamicMedianFindingHeap.insert(3);
        dynamicMedianFindingHeap.insert(4);
        dynamicMedianFindingHeap.insert(5);
        dynamicMedianFindingHeap.insert(6);
        dynamicMedianFindingHeap.insert(7);

        StdOut.println("Median: " + dynamicMedianFindingHeap.findTheMedian() + " Expected: 4");
        StdOut.println("Delete Median: " + dynamicMedianFindingHeap.deleteMedian() + " Expected: 4");

        //When we have an even number of values, pick the left one
        StdOut.println("Median: " + dynamicMedianFindingHeap.findTheMedian() + " Expected: 3");

        dynamicMedianFindingHeap.deleteMedian();
        dynamicMedianFindingHeap.insert(99);
        dynamicMedianFindingHeap.insert(100);

        StdOut.println("Median: " + dynamicMedianFindingHeap.findTheMedian() + " Expected: 6");
    }

}
