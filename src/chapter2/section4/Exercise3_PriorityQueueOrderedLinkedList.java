package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 16/03/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3_PriorityQueueOrderedLinkedList {

    private class PriorityQueueOrderedLinkedList<Key extends Comparable<Key>> {

        private class Node {
            Key key;
            Node next;
            Node previous;
        }

        private Node priorityQueue;
        private Node last;
        private int size = 0;

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        //O(N)
        public void insert(Key key) {

            //0 or 1 elements in the list
            if (size == 0 || ArrayUtil.less(key, priorityQueue.key)) {
                Node oldFirst = priorityQueue;

                priorityQueue = new Node();
                priorityQueue.key = key;
                priorityQueue.next = oldFirst;

                if (oldFirst != null) {
                    oldFirst.previous = priorityQueue;
                }

                if (priorityQueue.next == null) {
                    last = priorityQueue;
                }
            } else {
                Node current = priorityQueue;

                while (current.next != null && ArrayUtil.less(current.next.key, key)) {
                    current = current.next;
                }

                Node newNode = new Node();
                newNode.key = key;
                newNode.next = current.next;
                current.next = newNode;
                newNode.previous = current;

                if (newNode.next == null) {
                    last = newNode;
                } else {
                    newNode.next.previous = newNode;
                }
            }

            size++;
        }

        //O(1)
        public Key removeMax() {
            if (isEmpty()) {
                throw new RuntimeException("Priority queue underflow");
            }

            Key maxValue = last.key;

            last = last.previous;

            if (last != null) {
                last.next = null;
            }

            size--;

            return maxValue;
        }
    }

    public static void main(String[] args) {
        testPriorityQueueOrderedLinkedList();
    }

    private static void testPriorityQueueOrderedLinkedList() {

        PriorityQueueOrderedLinkedList<Integer> priorityQueueOrderedLinkedList = new Exercise3_PriorityQueueOrderedLinkedList().new PriorityQueueOrderedLinkedList<>();
        priorityQueueOrderedLinkedList.insert(2);
        priorityQueueOrderedLinkedList.insert(10);
        priorityQueueOrderedLinkedList.insert(4);
        priorityQueueOrderedLinkedList.insert(1);

        StdOut.println("Max value: " + priorityQueueOrderedLinkedList.removeMax() + " - Expected: 10");
        StdOut.println("Max value: " + priorityQueueOrderedLinkedList.removeMax() + " - Expected: 4");
        StdOut.println("Max value: " + priorityQueueOrderedLinkedList.removeMax() + " - Expected: 2");
        StdOut.println("Max value: " + priorityQueueOrderedLinkedList.removeMax() + " - Expected: 1");
    }

}
