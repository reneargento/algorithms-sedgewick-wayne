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
@SuppressWarnings("unchecked")
public class Exercise17_LinkedListSort<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node next;

        Node() {
        }

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

    public static void main(String[] args) {
        Exercise17_LinkedListSort<Comparable> linkedList = createList();

        Exercise17_LinkedListSort.Node newSourceNode = mergesort(linkedList.first);
        linkedList.first = newSourceNode;

        StringJoiner sortedList = new StringJoiner(" ");
        while(newSourceNode != null) {
            sortedList.add(String.valueOf(newSourceNode.item));
            newSourceNode = newSourceNode.next;
        }

        StdOut.println("Sorted list: " + sortedList.toString());
        StdOut.println("Expected: -10 -9 0 1 5 20 55");
    }

    private static Exercise17_LinkedListSort<Comparable> createList() {
        Exercise17_LinkedListSort<Comparable> linkedList = new Exercise17_LinkedListSort<>();

        linkedList.add(-9);
        linkedList.add(20);
        linkedList.add(1);
        linkedList.add(5);
        linkedList.add(55);
        linkedList.add(-10);
        linkedList.add(0);

        return linkedList;
    }

    private static Exercise17_LinkedListSort.Node mergesort(Exercise17_LinkedListSort<Comparable>.Node sourceNode) {
        if (sourceNode == null || sourceNode.next == null) {
            return sourceNode;
        }

        Exercise17_LinkedListSort<Comparable>.Node low = sourceNode;
        Exercise17_LinkedListSort<Comparable>.Node middle = sourceNode;
        Exercise17_LinkedListSort<Comparable>.Node high = sourceNode;
        Exercise17_LinkedListSort<Comparable>.Node currentNode = sourceNode;

        boolean secondSubArray = false;

        while (currentNode != null && currentNode.next != null) {

            if (currentNode.item.compareTo(currentNode.next.item) > 0) {
                if (!secondSubArray) {
                    middle = currentNode;
                    secondSubArray = true;
                } else {
                    high = currentNode;
                    low = merge(low, middle, high);
                    middle = high;
                }
            }
            currentNode = currentNode.next;
        }

        if (high.next != null && currentNode != null) {
            low = merge(low, middle, currentNode);
        }

        return low;
    }

    private static Exercise17_LinkedListSort<Comparable>.Node merge(Exercise17_LinkedListSort<Comparable>.Node low,
                                                                    Exercise17_LinkedListSort<Comparable>.Node middle,
                                                                    Exercise17_LinkedListSort<Comparable>.Node high) {
        Exercise17_LinkedListSort<Comparable>.Node leftNode = low;
        Exercise17_LinkedListSort<Comparable>.Node rightNode = middle.next;
        Exercise17_LinkedListSort<Comparable>.Node newLow;
        Exercise17_LinkedListSort<Comparable>.Node afterLastNode = high.next;
        Exercise17_LinkedListSort<Comparable>.Node aux;

        if (leftNode.item.compareTo(rightNode.item) <= 0) {
            newLow = leftNode;
            aux = leftNode;
            leftNode = leftNode.next;
        } else {
            newLow = rightNode;
            aux = rightNode;
            rightNode = rightNode.next;
        }

        while(leftNode != middle.next && rightNode != high.next) {
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

        return newLow;
    }
}
