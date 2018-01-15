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
    private int last;

    @SuppressWarnings("unchecked")
    public Exercise33_2_Deque() {
        array = (Item[]) new Object[1];
        size = 0;
        first = -1;
        last = -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void pushLeft(Item item) {
        if(size == array.length) {
            resize(size * 2);
        }

        if(first == 0) {
            moveArrayRight();
        }

        if(isEmpty()) {
            first = 0;
            last = 0;
            array[first] = item;
        } else {
            array[first - 1] = item;
            first = first - 1;
        }

        size++;
    }

    public void pushRight(Item item) {
        if (size == array.length){
            resize(size * 2);
        }

        if(last == array.length - 1){
            moveArrayLeft();
        }

        if(isEmpty()){
            first = 0;
            last = 0;
            array[last] = item;
        } else {
            array[last + 1] = item;
            last = last + 1;
        }

        size++;
    }

    public Item popLeft() {
        if(isEmpty()){
            throw new RuntimeException("Deque underflow");
        }

        Item item = array[first];
        array[first] = null; //avoid loitering

        size--;

        if(isEmpty()){
            first = -1;
            last = -1;
        } else {
            first = first + 1;
        }

        if(size == array.length / 4) {
            resize(array.length / 2);
        }

        return item;
    }

    public Item popRight(){
        if(isEmpty()){
            throw new RuntimeException("Deque underflow");
        }

        Item item = array[last];
        array[last] = null; //avoid loitering

        size--;

        if(isEmpty()){
            first = -1;
            last = -1;
        } else {
            last = last - 1;
        }

        if(size == array.length / 4){
            resize(array.length / 2);
        }

        return item;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];

        int j = 0;
        for(int i=first; i <= last; i++) {
            newArray[j] = array[i];
            j++;
        }

        first = 0;
        last = size-1;

        array = newArray;
    }

    private void moveArrayRight() {
        for (int i=last; i >= first; i--){
            array[i + 1] = array[i];
        }
        first++;
        last++;
    }

    private void moveArrayLeft() {
        for (int i = first; i <= last; i++) {
            array[i - 1] = array[i];
        }
        first--;
        last--;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        int index = first;

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
