package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/27/16.
 */
public class Exercise48_TwoStacksDeque<Item> implements Iterable<Item>{

    private Stack<Item> leftStack;
    private Stack<Item> rightStack;

    public Exercise48_TwoStacksDeque() {
        leftStack = new Stack<>();
        rightStack = new Stack<>();
    }

    public boolean isLeftStackEmpty(){
        return leftStack.size() == 0;
    }

    public boolean isRightStackEmpty(){
        return rightStack.size() == 0;
    }

    public int leftStackSize() {
        return leftStack.size();
    }

    public int rightStackSize() {
        return rightStack.size();
    }

    public void pushLeft(Item item){
        leftStack.push(item);
    }

    public void pushRight(Item item) {
        rightStack.push(item);
    }

    public Item popLeft(){
        if(isLeftStackEmpty()) {
            throw new RuntimeException("Left stack underflow");
        }

        return leftStack.pop();
    }

    public Item popRight() {
        if(isRightStackEmpty()) {
            throw new RuntimeException("Right stack underflow");
        }

        return rightStack.pop();
    }

    @Override
    public Iterator<Item> iterator() {
        return new TwoStacksDequeIterator();
    }

    private class TwoStacksDequeIterator implements Iterator<Item> {

        private int index = 0;
        private Iterator<Item> leftStackIterator = leftStack.iterator();
        private Iterator<Item> rightStackIterator = rightStack.iterator();

        @Override
        public boolean hasNext() {
            return index < leftStack.size() + rightStack.size();
        }

        @Override
        public Item next() {

            if(index == 0 && leftStackSize() > 0) {
                StdOut.println("Left stack");
            }

            Item item;

            if (leftStackIterator.hasNext()){
                item = leftStackIterator.next();
            } else {
                if(index == leftStackSize()) {
                    StdOut.println("Right stack");
                }

                item = rightStackIterator.next();
            }

            index++;
            return item;
        }
    }

    public static void main(String[] args) {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();

        deque.testPushLeft();
        deque.testPushRight();
        deque.testPopLeft();
        deque.testPopRight();
    }

    private void testPushLeft() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushLeft("a");
        deque.pushLeft("b");
        deque.pushLeft("c");

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Test Push Left: " + dequeItems.toString());
        StdOut.println("Expected: c b a");
        StdOut.println();
    }

    private void testPushRight() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Test Push Right: " + dequeItems.toString());
        StdOut.println("Expected: c b a");
        StdOut.println();
    }

    private void testPopLeft() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushLeft("a");
        deque.pushLeft("b");
        deque.pushLeft("c");

        deque.popLeft();
        deque.popLeft();

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Test Pop Left: " + dequeItems.toString());
        StdOut.println("Expected: a");
        StdOut.println();
    }

    private void testPopRight() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        deque.popRight();
        deque.popRight();

        StringJoiner dequeItems = new StringJoiner(" ");
        for (String item : deque) {
            dequeItems.add(item);
        }

        StdOut.println("Test Pop Right: " + dequeItems.toString());
        StdOut.println("Expected: a");
    }

}
