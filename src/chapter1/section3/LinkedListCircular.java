package chapter1.section3;

import java.util.Iterator;

/**
 * Created by Rene Argento on 11/04/18.
 */
// Thanks to Adeboye (https://github.com/Adeboye) for finding a bug on the remove methods:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/185
public class LinkedListCircular<Item> implements Iterable<Item> {

    public class Node {
        public Item item;
        public Node next;

        public Node(Item item) {
            this.item = item;
        }
    }

    private int size;
    private Node first;
    private Node last;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Node getFirstNode() {
        return first;
    }

    public Item get(int index) {
        if (isEmpty()) {
            return null;
        }

        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (size() - 1));
        }

        Node current = first;
        int currentIndex = 0;

        while (currentIndex < index) {
            currentIndex++;
            current = current.next;
        }

        return current.item;
    }

    public void insert(Item item) {
        Node oldLast = last;

        last = new Node(item);
        last.item = item;

        if (!isEmpty()) {
            last.next = oldLast.next;
            oldLast.next = last;
        } else {
            first = last;
            last.next = first;
        }

        size++;
    }

    public void remove(int index) {
        if (isEmpty()) {
            return;
        }

        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (size() - 1));
        }

        if (index == 0) {
            if (size() > 1) {
                first = first.next;
                last.next = first;
            } else {
                first = null;
                last = null;
            }
        } else {
            Node current = first;
            int currentIndex = 0;

            while (currentIndex < index - 1) {
                currentIndex++;
                current = current.next;
            }

            if (current.next == last) {
                last = current;
            }

            current.next = current.next.next;
        }
        size--;
    }

    public void remove(Item item) {
        if (isEmpty()) {
            return;
        }

        if (item.equals(first.item)) {
            if (size() > 1) {
                first = first.next;
                last.next = first;
            } else {
                first = null;
                last = null;
            }

            size--;
        } else {
            Node current = first;

            while (current != last && !current.next.item.equals(item)) {
                current = current.next;
            }

            if (current != last) {
                if (current.next == last) {
                    last = current;
                }

                current.next = current.next.next;
                size--;
            }
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<Item> {
        int index = 0;
        Node currentNode = first;

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public Item next() {
            Item item = currentNode.item;
            currentNode = currentNode.next;

            index++;
            return item;
        }
    }
}