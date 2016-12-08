package chapter1.section3.linked.list;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by rene on 8/15/16.
 */
public class Exercise33_1_Deque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node previous;
        Node next;
    }

    private Node first;
    private Node last;
    private int size;

    public Exercise33_1_Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void pushLeft(Item item) {
        Node oldFirst = first;

        first = new Node();
        first.item = item;
        first.next = oldFirst;

        if (oldFirst != null) {
            oldFirst.previous = first;
        } else {
            last = first;
        }

        size++;
    }

    public void pushRight(Item item) {
        Node oldLast = last;

        last = new Node();
        last.item = item;
        last.previous = oldLast;

        if (oldLast != null) {
            oldLast.next = last;
        } else {
            first = last;
        }

        size++;
    }

    public Item popLeft() {

        if (isEmpty()) {
            throw new RuntimeException("Deque underflow");
        }

        Item item = first.item;

        first = first.next;

        if(first != null) {
            first.previous = null;
        } else {
            last = null;
        }

        size--;

        return item;
    }

    public Item popRight() {
        if(isEmpty()){
            throw new RuntimeException("Deque underflow");
        }

        Item item = last.item;

        last = last.previous;

        if(last != null) {
            last.next = null;
        } else {
            first = null;
        }
        size--;

        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        int index = 0;
        Node current = first;

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public Item next() {
            index++;

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Exercise33_1_Deque<String> deque = new Exercise33_1_Deque<>();

        deque.testPushLeft();
        deque.testPushRight();
        deque.testPopLeft();
        deque.testPopRight();
    }

    private void testPushLeft() {
        StdOut.println("Test Push Left");

        Exercise33_1_Deque<String> deque = new Exercise33_1_Deque<>();
        deque.pushLeft("a");
        deque.pushLeft("b");
        deque.pushLeft("c");

        for(String string : deque) {
            StdOut.println(string);
        }
    }

    private void testPushRight() {
        StdOut.println("Test Push Right");

        Exercise33_1_Deque<String> deque = new Exercise33_1_Deque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        for(String string : deque) {
            StdOut.println(string);
        }
    }

    private void testPopLeft() {
        StdOut.println("Test Pop Left");

        Exercise33_1_Deque<String> deque = new Exercise33_1_Deque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        deque.popLeft();
        deque.popLeft();

        for(String string : deque) {
            StdOut.println(string);
        }
    }

    private void testPopRight() {
        StdOut.println("Test Pop Right");

        Exercise33_1_Deque<String> deque = new Exercise33_1_Deque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        deque.popRight();
        deque.popRight();

        for(String string : deque) {
            StdOut.println(string);
        }
    }
}
