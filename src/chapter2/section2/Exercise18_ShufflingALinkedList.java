package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

/**
 * Created by Rene Argento on 18/02/17.
 */
@SuppressWarnings("unchecked")
public class Exercise18_ShufflingALinkedList<Item> {

    private class Node {
        Item item;
        Exercise18_ShufflingALinkedList.Node next;

        Node() {
        }

        Node(Item item) {
            this.item = item;
        }
    }

    private int size;
    private Exercise18_ShufflingALinkedList.Node first;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void add(Item item) {
        Exercise18_ShufflingALinkedList.Node newNode = new Exercise18_ShufflingALinkedList.Node(item);
        newNode.next = first;

        first = newNode;

        size++;
    }

    public Iterator<Item> iterator() {
        return new Exercise18_ShufflingALinkedList.ListIterator();
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
        Exercise18_ShufflingALinkedList<Comparable> linkedList = createList();

        Exercise18_ShufflingALinkedList<Comparable>.Node newHead = shuffle(linkedList.first);
        linkedList.first = newHead;

        Exercise18_ShufflingALinkedList.Node current = newHead;

        while(current != null) {
            StdOut.print(current.item + " ");

            current = current.next;
        }
    }

    private static Exercise18_ShufflingALinkedList<Comparable> createList() {
        Exercise18_ShufflingALinkedList<Comparable> linkedList = new Exercise18_ShufflingALinkedList<>();

        for(int i=10; i >= 0; i--) {
            linkedList.add(i);
        }

        return linkedList;
    }

    private static Exercise18_ShufflingALinkedList.Node shuffle(Exercise18_ShufflingALinkedList.Node firstHalf) {

        if(firstHalf == null || firstHalf.next == null) {
            return firstHalf;
        }

        Exercise18_ShufflingALinkedList.Node middle = getMiddle(firstHalf);
        Exercise18_ShufflingALinkedList.Node secondHalf = middle.next;

        middle.next = null;

        return shuffleItems(shuffle(firstHalf), shuffle(secondHalf));
    }

    private static Exercise18_ShufflingALinkedList.Node getMiddle(Exercise18_ShufflingALinkedList<Comparable>.Node source) {

        if(source == null) {
            return null;
        }

        Exercise18_ShufflingALinkedList.Node slow = source;
        Exercise18_ShufflingALinkedList.Node fast = source;

        while(fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private static Exercise18_ShufflingALinkedList.Node shuffleItems(Exercise18_ShufflingALinkedList.Node firstHalf, Exercise18_ShufflingALinkedList.Node secondHalf) {

        Exercise18_ShufflingALinkedList.Node dummyHead = new Exercise18_ShufflingALinkedList().new Node();
        Exercise18_ShufflingALinkedList.Node current = dummyHead;

        while(firstHalf != null && secondHalf != null) {
            int random = StdRandom.uniform(2); //Returns 0 or 1

            if(random == 0) {
                current.next = firstHalf;
                firstHalf = firstHalf.next;
            } else {
                current.next = secondHalf;
                secondHalf = secondHalf.next;
            }

            current = current.next;
        }

        current.next = firstHalf == null ? secondHalf : firstHalf;

        return dummyHead.next;
    }

}
