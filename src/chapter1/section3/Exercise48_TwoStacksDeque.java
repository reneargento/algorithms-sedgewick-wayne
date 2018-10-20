package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by Rene Argento on 8/27/16.
 */
// Thanks to shimshahs (https://github.com/shimshahs) for mentioning that the exercise was implemented on the other
// way around (two stacks implementing a deque instead of a deque implementing two stacks) at
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/23
public class Exercise48_TwoStacksDeque<Item> implements Iterable<Item>{

    private Exercise33_1_Deque<Item> deque;
    private int stack1Size; // left stack
    private int stack2Size; // right stack

    public Exercise48_TwoStacksDeque() {
        deque = new Exercise33_1_Deque<>();
    }

    public boolean isStack1Empty() {
        return stack1Size == 0;
    }

    public boolean isStack2Empty() {
        return stack2Size == 0;
    }

    public int stack1Size() {
        return stack1Size;
    }

    public int stack2Size() {
        return stack2Size;
    }

    public void pushStack1(Item item) {
        deque.pushLeft(item);
        stack1Size++;
    }

    public void pushStack2(Item item) {
        deque.pushRight(item);
        stack2Size++;
    }

    public Item popStack1() {
        if (isStack1Empty()) {
            throw new RuntimeException("Stack 1 underflow");
        }

        stack1Size--;
        return deque.popLeft();
    }

    public Item popStack2() {
        if (isStack2Empty()) {
            throw new RuntimeException("Stack 2 underflow");
        }

        stack2Size--;
        return deque.popRight();
    }

    @Override
    public Iterator<Item> iterator() {
        return new TwoStacksDequeIterator();
    }

    @SuppressWarnings("unchecked")
    private class TwoStacksDequeIterator implements Iterator<Item> {

        private int index;
        private Item[] items;
        private boolean hasNext;

        TwoStacksDequeIterator() {
            items = (Item[]) new Object[deque.size()];
            int currentIndex = 0;
            hasNext = true;

            for(Item item : deque) {
                items[currentIndex++] = item;
            }

            if (isStack1Empty() && !isStack2Empty()) {
                index = deque.size() - 1;
            }
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Item next() {
            if (index == 0 && stack1Size() > 0) {
                StdOut.println("Stack 1");
            }
            if (index == deque.size() - 1 && stack2Size() > 0) {
                StdOut.println("Stack 2");
            }

            Item item = items[index];

            if (isIteratingStack1(index)) {
                if (index != stack1Size - 1) {
                    index++;
                } else if (!isStack2Empty()) {
                    index = deque.size() - 1;
                } else {
                    hasNext = false;
                }
            } else {
                if (index != stack1Size) {
                    index--;
                } else {
                    hasNext = false;
                }
            }

            return item;
        }

        private boolean isIteratingStack1(int index) {
            return index < stack1Size;
        }
    }

    public static void main(String[] args) {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();

        deque.testPushStack1();
        deque.testPushStack2();
        deque.testPopStack1();
        deque.testPopStack2();
        deque.testMixedOperations();
    }

    private void testPushStack1() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushStack1("a");
        deque.pushStack1("b");
        deque.pushStack1("c");

        StdOut.println("Test Push on stack 1:");
        for (String item : deque) {
            StdOut.println(item);
        }

        StdOut.println("Expected: c b a");
        StdOut.println();
    }

    private void testPushStack2() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushStack2("a");
        deque.pushStack2("b");
        deque.pushStack2("c");

        StdOut.println("Test Push on stack 2:");
        for (String item : deque) {
            StdOut.println(item);
        }

        StdOut.println("Expected: c b a");
        StdOut.println();
    }

    private void testPopStack1() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushStack1("a");
        deque.pushStack1("b");
        deque.pushStack1("c");

        deque.popStack1();
        deque.popStack1();

        StdOut.println("Test Pop on stack 1:");
        for (String item : deque) {
            StdOut.println(item);
        }

        StdOut.println("Expected: a");
        StdOut.println();
    }

    private void testPopStack2() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushStack2("a");
        deque.pushStack2("b");
        deque.pushStack2("c");

        deque.popStack2();
        deque.popStack2();

        StdOut.println("Test Pop on stack 2:");
        for (String item : deque) {
            StdOut.println(item);
        }

        StdOut.println("Expected: a");
        StdOut.println();
    }

    private void testMixedOperations() {
        Exercise48_TwoStacksDeque<String> deque = new Exercise48_TwoStacksDeque<>();
        deque.pushStack1("rene");
        deque.pushStack2("a");
        deque.pushStack1("stack");
        deque.pushStack2("b");
        deque.pushStack1("deque");
        deque.pushStack2("c");

        StdOut.println("Test stack 1 and stack 2 together:");
        for (String item : deque) {
            StdOut.println(item);
        }

        StdOut.println("Expected: Stack 1 - deque stack rene");
        StdOut.println("Expected: Stack 2 - c b a");

        StdOut.println();
        StdOut.println("Test stack 1 and stack 2 after pop:");

        deque.popStack1();
        deque.popStack2();

        for (String item : deque) {
            StdOut.println(item);
        }

        StdOut.println("Expected: Stack 1 - stack rene");
        StdOut.println("Expected: Stack 2 - b a");
    }

}
