package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/15/16.
 */
public class Exercise33_2_Deque<Item> implements Iterable<Item>{

    private Item[] array;
    private int size;
    private int first;
    private static final int DEFAULT_SIZE = 10;

    @SuppressWarnings("unchecked")
    public Exercise33_2_Deque() {
        array = (Item[]) new Object[DEFAULT_SIZE];
        first = DEFAULT_SIZE / 2;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void pushLeft(Item item) {
        array[first] = item;
        first--;
        size++;

        if (first < 0) {
            if (size > array.length / 2) {
                resize(array.length * 2);
            } else {
                resize(array.length); // Shift items to center
            }
        }
    }

    public void pushRight(Item item) {
        int endIndex = first + size + 1;
        array[endIndex] = item;
        size++;

        if (endIndex == array.length - 1) {
            if (size > array.length / 2) {
                resize(array.length * 2);
            } else {
                resize(array.length); // Shift items to center
            }
        }
    }

    public Item popLeft() {
        if (isEmpty()) {
            throw new RuntimeException("Deque underflow");
        }

        Item item = array[first + 1];
        array[first + 1] = null; // avoid loitering
        first++;
        size--;

        if (size == array.length / 4) {
            resize(array.length / 2);
        }
        return item;
    }

    public Item popRight() {
        if (isEmpty()) {
            throw new RuntimeException("Deque underflow");
        }

        int endIndex = first + size;
        Item item = array[endIndex];
        array[endIndex] = null; // avoid loitering
        size--;

        if (size == array.length / 4) {
            resize(array.length / 2);
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        int startPosition = (newSize / 2) - (size / 2);
        Item[] newArray = (Item[]) new Object[newSize];

        System.arraycopy(array, first + 1, newArray, startPosition, size);
        array = newArray;
        first = startPosition - 1;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        int index = first + 1;
        int last = first + size;

        @Override
        public boolean hasNext() {
            return index <= last;
        }

        @Override
        public Item next() {
            Item item = array[index];
            index++;
            return item;
        }
    }


    public static void main(String[] args) {
        Exercise33_2_Deque<String> deque = new Exercise33_2_Deque<>();

        deque.testPushLeft();
        deque.testPushRight();
        deque.testPopLeft();
        deque.testPopRight();
    }

    private void testPushLeft() {
        StdOut.println("Test Push Left");

        Exercise33_2_Deque<String> deque = new Exercise33_2_Deque<>();
        deque.pushLeft("a");
        deque.pushLeft("b");
        deque.pushLeft("c");

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Deque items: " + dequeItems.toString());
        StdOut.println("Expected: c b a");
    }

    private void testPushRight() {
        StdOut.println("\nTest Push Right");

        Exercise33_2_Deque<String> deque = new Exercise33_2_Deque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Deque items: " + dequeItems.toString());
        StdOut.println("Expected: a b c");
    }

    private void testPopLeft() {
        StdOut.println("\nTest Pop Left");

        Exercise33_2_Deque<String> deque = new Exercise33_2_Deque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        deque.popLeft();
        deque.popLeft();

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Deque items: " + dequeItems.toString());
        StdOut.println("Expected: c");
    }

    private void testPopRight() {
        StdOut.println("\nTest Pop Right");

        Exercise33_2_Deque<String> deque = new Exercise33_2_Deque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        deque.popRight();
        deque.popRight();

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Deque items: " + dequeItems.toString());
        StdOut.println("Expected: a");
    }
}
