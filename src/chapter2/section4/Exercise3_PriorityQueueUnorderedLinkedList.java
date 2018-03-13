package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 16/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3_PriorityQueueUnorderedLinkedList {

    private class PriorityQueueUnorderedLinkedList<Key extends Comparable<Key>> {

        private class Node {
            Key key;
            Node next;
        }

        private Node priorityQueue;
        private int size = 0;

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        //O(1)
        public void insert(Key key) {

            Node oldFirst = priorityQueue;

            priorityQueue = new Node();
            priorityQueue.key = key;
            priorityQueue.next = oldFirst;

            size++;
        }

        //O(N)
        public Key removeMax() {
            if (isEmpty()) {
                throw new RuntimeException("Priority queue underflow");
            }

            Key maxValue = priorityQueue.key;

            Node currentNode = priorityQueue;
            currentNode = currentNode.next;

            //Find out max value
            while (currentNode != null) {
                if (ArrayUtil.less(maxValue, currentNode.key)) {
                    maxValue = currentNode.key;
                }

                currentNode = currentNode.next;
            }

            if (maxValue == priorityQueue.key) {
                //First element is the max value
                priorityQueue = priorityQueue.next;
            } else {
                currentNode = priorityQueue;

                while (currentNode.next.key != maxValue) {
                    currentNode = currentNode.next;
                }

                if (currentNode.next.next == null) {
                    currentNode.next = null;
                } else {
                    currentNode.next = currentNode.next.next;
                }
            }

            size--;

            return maxValue;
        }
    }

    public static void main(String[] args) {
        testPriorityQueueUnorderedLinkedList();
    }

    private static void testPriorityQueueUnorderedLinkedList() {

        PriorityQueueUnorderedLinkedList<Integer> priorityQueueUnorderedLinkedList = new Exercise3_PriorityQueueUnorderedLinkedList().new PriorityQueueUnorderedLinkedList<>();
        priorityQueueUnorderedLinkedList.insert(2);
        priorityQueueUnorderedLinkedList.insert(10);
        priorityQueueUnorderedLinkedList.insert(4);
        priorityQueueUnorderedLinkedList.insert(1);

        StdOut.println("Max value: " + priorityQueueUnorderedLinkedList.removeMax() + " - Expected: 10");
        StdOut.println("Max value: " + priorityQueueUnorderedLinkedList.removeMax() + " - Expected: 4");
        StdOut.println("Max value: " + priorityQueueUnorderedLinkedList.removeMax() + " - Expected: 2");
        StdOut.println("Max value: " + priorityQueueUnorderedLinkedList.removeMax() + " - Expected: 1");
    }

}
