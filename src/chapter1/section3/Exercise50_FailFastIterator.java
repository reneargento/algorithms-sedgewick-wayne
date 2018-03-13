package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Created by Rene Argento on 8/28/16.
 */
public class Exercise50_FailFastIterator<Item> implements Iterable<Item>{

    private class Node {
        Item item;
        Node next;
    }

    private Node first;
    private int size;
    private int operationsCounter;

    public Exercise50_FailFastIterator() {
        first = null;
        size = 0;
        operationsCounter = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void push(Item item) {

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;

        operationsCounter++;

        size++;
    }

    public Item pop() {

        if (isEmpty()) {
            throw new RuntimeException("Stack underflow exception");
        }

        Item item = first.item;
        first = first.next;
        size--;

        operationsCounter++;

        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new FailFastIteratorIterator();
    }

    private class FailFastIteratorIterator implements Iterator<Item> {

        int currentOperationsCounter;
        Node current;

        public FailFastIteratorIterator() {
            current = first;
            currentOperationsCounter = operationsCounter;
        }

        @Override
        public boolean hasNext() {
            if (currentOperationsCounter != operationsCounter) {
                throw new ConcurrentModificationException();
            }

            return current != null;
        }

        @Override
        public Item next() {
            if (currentOperationsCounter != operationsCounter) {
                throw new ConcurrentModificationException();
            }

            Item item = current.item;
            current = current.next;

            return item;
        }
    }

    public static void main (String[] args) {
        StdOut.print("Expected: a java.util.ConcurrentModificationException to be thrown\n");

        Exercise50_FailFastIterator<String> failFastIterator = new Exercise50_FailFastIterator<>();

        failFastIterator.push("a");
        failFastIterator.push("b");
        failFastIterator.push("c");
        failFastIterator.push("d");

        failFastIterator.pop();

        for(String string : failFastIterator) {
            StdOut.print("Iterating at item " + string + " ");

            failFastIterator.push("z");
        }
    }

}
