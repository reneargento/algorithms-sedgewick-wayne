package Chapter1.Section3.LinkedList;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by rene on 8/15/16.
 */
public class Exercise32_Steque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node previous;
        Node next;
    }

    private Node first;
    private Node last;
    private int size;

    public boolean isEmpty(){
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

        if (oldFirst != null) {
            oldFirst.previous = first;
        } else {
            last = first;
        }

        size++;
    }

    public Item pop() {
        if (isEmpty()) {
            throw new RuntimeException("Steque underflow");
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

    public void enqueue(Item item) {
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;

        if(oldLast != null) {
            oldLast.next = last;
        } else {
            first = last;
        }

        size++;
    }

    public Iterator<Item> iterator() {
        return new StequeIterator();
    }

    private class StequeIterator implements Iterator<Item>{

        Node current = first;
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Item next() {
            Item item = current.item;
            current = current.next;

            index++;

            return item;
        }
    }

    public static void main(String[] args){
        Exercise32_Steque<Integer> steque = new Exercise32_Steque<>();
        steque.push(1);
        steque.push(2);
        steque.push(3);
        steque.pop();
        steque.enqueue(5);
        steque.enqueue(6);

        for (int number : steque) {
            StdOut.println(number);
        }
    }

}
