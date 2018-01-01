package chapter1.section3;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Rene Argento on 8/22/16.
 */
public class Exercise40_MoveToFront<Item> implements Iterable<Item>{

    private class Node {
        Item item;
        Node next;
    }

    private Node first;
    private int size;

    private Map<Item, Boolean> existingCharactersHashTable;

    public Exercise40_MoveToFront() {
        first = null;
        size = 0;
        existingCharactersHashTable = new HashMap<>();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void insert(Item item) {
        if(existingCharactersHashTable.containsKey(item)){
            delete(item);
        }

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;

        existingCharactersHashTable.put(item, true);
        size++;
    }

    private void delete(Item item) {
        if(isEmpty()){
            return;
        }

        Node current = first;

        if(current.item.equals(item)){
            first = current.next;
            size--;
        } else{
            for(current = first; current.next != null; current = current.next) {
                if(current.next.item.equals(item)){
                    break;
                }
            }

            if(current.next != null) {
                current.next = current.next.next;
                size--;
            }
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new MoveToFrontIterator();
    }

    private class MoveToFrontIterator implements Iterator<Item> {

        Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    public static void main (String[] args) {
        Exercise40_MoveToFront<Character> moveToFront = new Exercise40_MoveToFront<>();

        while (StdIn.hasNextChar()){
            moveToFront.insert(StdIn.readChar());
        }

        // Test data
//        moveToFront.insert('a');
//        moveToFront.insert('b');
//        moveToFront.insert('c');
//        moveToFront.insert('d');
//        moveToFront.insert('c');
//        moveToFront.insert('d');
//        moveToFront.insert('z');

        for (char character : moveToFront) {
            StdOut.println(character);
        }
    }

}
