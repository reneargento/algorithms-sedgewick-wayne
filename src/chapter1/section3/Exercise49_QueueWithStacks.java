package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/28/16.
 */
// Based on the paper "Real Time Queue Operations in Pure LISP" by Robert Hood and Robert Melville
// Available at https://ecommons.cornell.edu/bitstream/handle/1813/6273/80-433.pdf

// Check http://stackoverflow.com/questions/5538192/how-to-implement-a-queue-with-three-stacks/ for a nice
// discussion about this topic and for an explanation of why a 3-stack solution may not exist.
// The best known solution is based on 6 stacks and this implementation is based on that solution.
public class Exercise49_QueueWithStacks<Item> implements Iterable<Item> {

    private Stack<Item> head;
    private Stack<Item> tail;

    private Stack<Item> reverseHead;
    private Stack<Item> reverseTail;

    private Stack<Item> tempTail;
    private Iterator<Item> headIteratorToReverse;
    private Iterator<Item> headIteratorToDequeue;

    private int size;

    private boolean isPerformingRecopy;
    private boolean finishedRecopyFirstPass;

    private int numberOfItemsDeletedDuringRecopy;

    public Exercise49_QueueWithStacks() {
        head = new Stack<>();
        tail = new Stack<>();
        reverseHead = new Stack<>();
        reverseTail = new Stack<>();
        tempTail = new Stack<>();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {

        if (!isPerformingRecopy) {
            tail.push(item);
        } else {
            tempTail.push(item);
            performRecopySteps();
        }

        size++;

        if (!isPerformingRecopy && tail.size() > head.size()) {
            startRecopy();
            performRecopySteps();
        }
    }

    public Item dequeue() {

        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }

        Item item;

        if (!isPerformingRecopy) {
            item = head.pop();
        } else {
            item = headIteratorToDequeue.next();
            numberOfItemsDeletedDuringRecopy++;

            performRecopySteps();
        }

        size--;

        if (!isPerformingRecopy && tail.size() > head.size()) {
            startRecopy();
            performRecopySteps();
        }

        return item;
    }

    // Perform 2 steps in the recopy process
    private void performRecopySteps() {
        if (!finishedRecopyFirstPass) {
            performRecopyFirstPassStep();

            if (!finishedRecopyFirstPass) {
                performRecopyFirstPassStep();
            } else {
                performRecopySecondPassStep();
            }
        } else {
            performRecopySecondPassStep();

            if (isPerformingRecopy) {
                performRecopySecondPassStep();
            }
        }
    }

    private void startRecopy() {
        isPerformingRecopy = true;
        headIteratorToReverse = head.iterator();
        headIteratorToDequeue = head.iterator();
    }

    private void performRecopyFirstPassStep() {
        if (tail.size() > 0) {
            reverseTail.push(tail.pop());
        }

        if (headIteratorToReverse.hasNext()) {
            reverseHead.push(headIteratorToReverse.next());
        }

        if (tail.isEmpty() && !headIteratorToReverse.hasNext()) {
            finishedRecopyFirstPass = true;
        }
    }

    private void performRecopySecondPassStep() {

        if (reverseHead.size() > numberOfItemsDeletedDuringRecopy) {
            reverseTail.push(reverseHead.pop());
        }

        if (reverseHead.size() == numberOfItemsDeletedDuringRecopy) {
            // Update main stacks
            head = reverseTail;
            tail = tempTail;

            // Clear temporary stacks
            reverseHead = new Stack<>();
            reverseTail = new Stack<>();
            tempTail = new Stack<>();

            numberOfItemsDeletedDuringRecopy = 0;

            // Recopy process done
            isPerformingRecopy = false;
            finishedRecopyFirstPass = false;
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new QueueWithStacksIterator();
    }

    private class QueueWithStacksIterator implements Iterator<Item> {

        private int index;
        private Stack<Item> reverseTailCopy;

        private Iterator<Item> headIterator;
        private Iterator<Item> reverseTailCopyIterator;

        public QueueWithStacksIterator() {
            index = 0;
            reverseTailCopy = new Stack<>();

            if (!isPerformingRecopy) {
                for(Item item : tail) {
                    reverseTailCopy.push(item);
                }

                headIterator = head.iterator();
            } else {
                // Get items in tail
                for (Item item : tempTail) {
                    reverseTailCopy.push(item);
                }

                for (Item item : tail) {
                    reverseTailCopy.push(item);
                }

                Stack<Item> reverseReverseTail = new Stack<>();

                for (Item item : reverseTail) {
                    reverseReverseTail.push(item);
                }
                for (Item item : reverseReverseTail) {
                    reverseTailCopy.push(item);
                }

                // Get items in head
                Stack<Item> reverseReverseHead = new Stack<>();
                for (Item item : reverseHead) {
                    reverseReverseHead.push(item);
                }

                Stack<Item> reverseHeadMinusDeletedItemsStack = new Stack<>();
                while (reverseReverseHead.size() > numberOfItemsDeletedDuringRecopy) {
                    reverseHeadMinusDeletedItemsStack.push(reverseReverseHead.pop());
                }

                Stack<Item> headIteratorStack = new Stack<>();
                while (!reverseHeadMinusDeletedItemsStack.isEmpty()){
                    headIteratorStack.push(reverseHeadMinusDeletedItemsStack.pop());
                }

                headIterator = headIteratorStack.iterator();
            }

            reverseTailCopyIterator = reverseTailCopy.iterator();
        }

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public Item next() {
            Item item;

            if (headIterator.hasNext()) {
                item = headIterator.next();
            } else  {
                item = reverseTailCopyIterator.next();
            }

            index++;
            return item;
        }
    }

    public static void main(String[] args) {

        Exercise49_QueueWithStacks<Integer> queueWithStacks = new Exercise49_QueueWithStacks<>();
        queueWithStacks.enqueue(0);

        queueWithStacks.dequeue();
        StdOut.println("Queue size: " + queueWithStacks.size());
        StdOut.println("Expected: 0");

        queueWithStacks.enqueue(1);
        queueWithStacks.enqueue(2);
        queueWithStacks.enqueue(3);

        StdOut.println("\nQueue size: " + queueWithStacks.size());
        StdOut.println("Expected: 3");

        StringJoiner queueItems1 = new StringJoiner(" ");
        for (int item : queueWithStacks) {
            queueItems1.add(String.valueOf(item));
        }

        StdOut.println("Queue items: " + queueItems1.toString());
        StdOut.println("Expected: 1 2 3");

        queueWithStacks.enqueue(4);

        StringJoiner queueItems2 = new StringJoiner(" ");
        for (int item : queueWithStacks) {
            queueItems2.add(String.valueOf(item));
        }

        StdOut.println("\nQueue items: " + queueItems2.toString());
        StdOut.println("Expected: 1 2 3 4");

        queueWithStacks.dequeue();
        queueWithStacks.dequeue();
        queueWithStacks.dequeue();

        StringJoiner queueItems3 = new StringJoiner(" ");
        for (int item : queueWithStacks) {
            queueItems3.add(String.valueOf(item));
        }

        StdOut.println("\nQueue size: " + queueWithStacks.size());
        StdOut.println("Expected: 1");

        StdOut.println("Queue items: " + queueItems3.toString());
        StdOut.println("Expected: 4");
    }

}