package Chapter1.Section3.Stack;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by rene on 8/27/16.
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
        StdOut.println("Test Push Left");

        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushLeft("a");
        deque.pushLeft("b");
        deque.pushLeft("c");

        for(String string : deque) {
            StdOut.println(string);
        }
    }

    private void testPushRight() {
        StdOut.println("Test Push Right");

        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushRight("a");
        deque.pushRight("b");
        deque.pushRight("c");

        for(String string : deque) {
            StdOut.println(string);
        }
    }

    private void testPopLeft() {
        StdOut.println("Test Pop Left");

        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushLeft("a");
        deque.pushLeft("b");
        deque.pushLeft("c");

        deque.popLeft();
        deque.popLeft();

        for(String string : deque) {
            StdOut.println(string);
        }
    }

    private void testPopRight() {
        StdOut.println("Test Pop Right");

        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
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
