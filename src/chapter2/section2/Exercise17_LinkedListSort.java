package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 15/02/17.
 */
// Thanks to dbf256 (https://github.com/dbf256) for mentioning that this exercise had an incorrect implementation of
// natural sort.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/42
// Thanks to ckwastra (https://github.com/ckwastra) for noticing that the subarray selection was not being done optimally.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/261
public class Exercise17_LinkedListSort  {

    private static class LinkedList<Item> implements Iterable<Item> {
        private class Node {
            Item item;
            Node next;

            Node(Item item) {
                this.item = item;
            }
        }

        private int size;
        private Node first;

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void add(Item item) {
            Node newNode = new Node(item);
            newNode.next = first;

            first = newNode;
            size++;
        }

        public Iterator<Item> iterator() {
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Item> {
            Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                Item item = current.item;
                current = current.next;

                return item;
            }
        }
    }

    private static class MergeResult {
        LinkedList<Integer>.Node newLow;
        LinkedList<Integer>.Node newHigh;
        LinkedList<Integer>.Node afterLastNode;

        public MergeResult(LinkedList<Integer>.Node newLow, LinkedList<Integer>.Node newHigh,
                           LinkedList<Integer>.Node afterLastNode) {
            this.newLow = newLow;
            this.newHigh = newHigh;
            this.afterLastNode = afterLastNode;
        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> linkedList = createList();

        LinkedList<Integer>.Node newSourceNode = mergesort(linkedList.first);
        linkedList.first = newSourceNode;

        StringJoiner sortedList = new StringJoiner(" ");
        while (newSourceNode != null) {
            sortedList.add(String.valueOf(newSourceNode.item));
            newSourceNode = newSourceNode.next;
        }

        StdOut.println("Sorted list: " + sortedList.toString());
        StdOut.println("Expected: -10 -9 0 1 5 20 55");
    }

    private static LinkedList<Integer> createList() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(-9);
        linkedList.add(20);
        linkedList.add(1);
        linkedList.add(5);
        linkedList.add(55);
        linkedList.add(-10);
        linkedList.add(0);
        return linkedList;
    }

    private static LinkedList<Integer>.Node mergesort(LinkedList<Integer>.Node sourceNode) {
        if (sourceNode == null || sourceNode.next == null) {
            return sourceNode;
        }

        LinkedList<Integer>.Node beforeLow = null;
        LinkedList<Integer>.Node low = sourceNode;
        LinkedList<Integer>.Node middle;
        LinkedList<Integer>.Node high;

        while (true) {
            middle = findSortedSubArray(low);
            if (middle.next == null) {
                if (low.equals(sourceNode)) // Array is sorted
                    break;
                else {
                    low = sourceNode;
                    beforeLow = null;
                    continue;
                }
            }
            high = findSortedSubArray(middle.next);
            MergeResult mergeResult = merge(low, middle, high);
            if (low == sourceNode) {
                sourceNode = mergeResult.newLow;
            }

            if (beforeLow != null) {
                beforeLow.next = mergeResult.newLow;
            } else {
                beforeLow = mergeResult.newHigh;
            }

            if (mergeResult.afterLastNode == null) {
                low = sourceNode;
                beforeLow = null;
            } else {
                low = mergeResult.afterLastNode;
            }
        }
        return low;
    }

    private static LinkedList<Integer>.Node findSortedSubArray(LinkedList<Integer>.Node currentNode) {
        while (currentNode.next != null) {
            if (currentNode.item.compareTo(currentNode.next.item) > 0) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private static MergeResult merge(LinkedList<Integer>.Node low, LinkedList<Integer>.Node middle,
                                     LinkedList<Integer>.Node high) {
        LinkedList<Integer>.Node leftNode = low;
        LinkedList<Integer>.Node rightNode = middle.next;
        LinkedList<Integer>.Node newLow;
        LinkedList<Integer>.Node newHigh;
        LinkedList<Integer>.Node afterLastNode = high.next;
        LinkedList<Integer>.Node aux;

        if (middle.item.compareTo(high.item) > 0) {
            newHigh = middle;
        } else {
            newHigh = high;
        }

        if (leftNode.item.compareTo(rightNode.item) <= 0) {
            newLow = leftNode;
            aux = leftNode;
            leftNode = leftNode.next;
        } else {
            newLow = rightNode;
            aux = rightNode;
            rightNode = rightNode.next;
        }

        while (leftNode != middle.next && rightNode != high.next) {
            if (leftNode.item.compareTo(rightNode.item) <= 0) {
                aux.next = leftNode;
                aux = leftNode;
                leftNode = leftNode.next;
            } else {
                aux.next = rightNode;
                aux = rightNode;
                rightNode = rightNode.next;
            }
        }

        if (leftNode == middle.next) {
            aux.next = rightNode;
        } else {
            aux.next = leftNode;
            middle.next = afterLastNode;
        }
        return new MergeResult(newLow, newHigh, afterLastNode);
    }
}
