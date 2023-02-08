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

    int size;
    Node last;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Node getFirstNode() {
        if (isEmpty()) {
            return null;
        }
        return last.next;
    }

    public Item get(int index) {
        if (isEmpty()) {
            return null;
        }
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (size() - 1));
        }

        Node current = last.next;
        int currentIndex = 0;

        while (currentIndex < index) {
            currentIndex++;
            current = current.next;
        }
        return current.item;
    }

    public void insertLast(Item item) {
        Node oldLast = last;
        last = new Node(item);

        if (isEmpty()) {
            last.next = last;
        } else {
            last.next = oldLast.next;
            oldLast.next = last;
        }
        size++;
    }

    public void insertFirst(Item item) {
        Node newFirst = new Node(item);

        if (isEmpty()) {
            last = newFirst;
            last.next = last;
        } else {
            Node oldFirst = last.next;
            last.next = newFirst;
            newFirst.next = oldFirst;
        }
        size++;
    }

    public Item removeFirst() {
        return remove(0);
    }

    public Item remove(int index) {
        if (isEmpty()) {
            return null;
        }
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (size() - 1));
        }
        Item item;

        if (index == 0) {
            item = last.next.item;

            if (size() > 1) {
                last.next = last.next.next;
            } else {
                last = null;
            }
        } else {
            Node current = last.next;
            int currentIndex = 0;

            while (currentIndex < index - 1) {
                currentIndex++;
                current = current.next;
            }
            item = current.next.item;

            if (current.next == last) {
                last = current;
            }
            current.next = current.next.next;
        }
        size--;
        return item;
    }

    public void remove(Item item) {
        if (isEmpty()) {
            return;
        }

        if (item.equals(last.next.item)) {
            if (size() > 1) {
                last.next = last.next.next;
            } else {
                last = null;
            }
            size--;
        } else {
            Node current = last.next;
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
        Node currentNode = last.next;

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