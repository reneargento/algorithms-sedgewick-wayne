package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/28/16.
 */
//Based on the paper "Real Time Queue Operations in Pure LISP" by Robert Hood and Robert Melville
    //Available at https://ecommons.cornell.edu/bitstream/handle/1813/6273/80-433.pdf
    //Check http://stackoverflow.com/questions/5538192/how-to-implement-a-queue-with-three-stacks/ for a nice
    //discussion about this topic.
public class Exercise49_QueueWithStacks<Item> implements Iterable<Item>{

    private Stack<Item> head;
    private Stack<Item> tail;

    private Stack<Item> reverseHead;
    private Stack<Item> reverseTail;

    private Stack<Item> tempHead; //Used during recopy operations
    private Stack<Item> tempTail; //Used during recopy operations

    private int size;

    private boolean isPerformingRecopy;
    private boolean waitingSecondRecopyPass;

    private int numberOfItemsDeletedDuringRecopy;

    public Exercise49_QueueWithStacks() {
        head = new Stack<>();
        tail = new Stack<>();
        reverseHead = new Stack<>();
        reverseTail = new Stack<>();
        tempHead = new Stack<>();
        tempTail = new Stack<>();

        size = 0;
        isPerformingRecopy = false;
        waitingSecondRecopyPass = false;
        numberOfItemsDeletedDuringRecopy = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {

        if(!isPerformingRecopy) {
            tail.push(item);
        } else {
            tempTail.push(item);

            if(waitingSecondRecopyPass) {
                new Thread(this::performSecondRecopyPass).run();
            }
        }

        size++;

        if(!isPerformingRecopy && tail.size() > head.size()) {
            new Thread(this::performFirstRecopyPass).run();
        }
    }

    public Item dequeue(){

        if(isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }

        Item item;

        if(!isPerformingRecopy) {
            item = head.pop();
        } else {
            item = tempHead.pop();
            numberOfItemsDeletedDuringRecopy++;

            if(waitingSecondRecopyPass) {
                new Thread(this::performSecondRecopyPass).run();
            }
        }

        size--;

        if(!isPerformingRecopy && tail.size() > head.size()) {
            new Thread(this::performFirstRecopyPass).run();
        }

        return item;
    }

    private void performFirstRecopyPass() {
        isPerformingRecopy = true;

        while (tail.size() > 0) {
            reverseTail.push(tail.pop());
        }

        while (head.size() > 0) {
            reverseHead.push(head.pop());
        }

        //Keep a copy of head for pop operations to be performed during recopy process
        for(Item item : reverseHead) {
            tempHead.push(item);
        }

        waitingSecondRecopyPass = true;
    }

    private void performSecondRecopyPass() {
        waitingSecondRecopyPass = false;

        while (reverseHead.size() > numberOfItemsDeletedDuringRecopy) {
            reverseTail.push(reverseHead.pop());
        }

        Stack<Item> temp = head;
        head = reverseTail;
        reverseTail = temp;

        Stack<Item> temp2 = tail;
        tail = tempTail;
        tempTail = temp2;

        //Clear reverseHead and tempHead stacks
        while (reverseHead.size() > 0){
            reverseHead.pop();
        }
        while (tempHead.size() > 0) {
            tempHead.pop();
        }
        numberOfItemsDeletedDuringRecopy = 0;

        //Recopy process done
        isPerformingRecopy = false;
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

            if(!isPerformingRecopy) {
                for(Item item : tail) {
                    reverseTailCopy.push(item);
                }

                headIterator = head.iterator();
            } else {
                for (Item item : reverseTail) {
                    reverseTailCopy.push(item);
                }

                headIterator = tempHead.iterator();
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

            if(headIterator.hasNext()) {
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
        queueWithStacks.enqueue(1);
        queueWithStacks.enqueue(2);
        queueWithStacks.enqueue(3);

        queueWithStacks.dequeue();
        queueWithStacks.dequeue();
        queueWithStacks.dequeue();

        StringJoiner queueItems = new StringJoiner(" ");
        for (int item : queueWithStacks) {
            queueItems.add(String.valueOf(item));
        }

        StdOut.println("Queue items: " + queueItems.toString());
        StdOut.println("Expected: 3");
    }

}
