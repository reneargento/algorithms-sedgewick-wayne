package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

/**
 * Created by Rene Argento on 20/11/16.
 */
public class Exercise27_QueueWith2Stacks<Item> {

    private Stack<Item> tailStack;
    private Stack<Item> headStack;

    public Exercise27_QueueWith2Stacks() {
        tailStack = new Stack<>();
        headStack = new Stack<>();
    }

    //O(1)
    public int size() {
        return headStack.size() + tailStack.size();
    }

    //O(1)
    public boolean isEmpty() {
        return headStack.isEmpty() && tailStack.isEmpty();
    }

    //O(1)
    public void enqueue(Item item) {
        tailStack.push(item);
    }

    //Amortized O(1)
    public Item dequeue() {

        if (headStack.isEmpty()) {
            moveAllItemsFromTailToHead();
        }

        return headStack.pop();
    }

    private void moveAllItemsFromTailToHead() {

        while(!tailStack.isEmpty()) {
            headStack.push(tailStack.pop());
        }

    }

    public static void main(String[] args) {

        Exercise27_QueueWith2Stacks<String> exercise27_queueWith2Stacks = new Exercise27_QueueWith2Stacks<>();

        StdOut.println("IsEmpty: " + exercise27_queueWith2Stacks.isEmpty() + " Expected: true");
        StdOut.println("Size: " + exercise27_queueWith2Stacks.size() + " Expected: 0");

        exercise27_queueWith2Stacks.enqueue("A");
        exercise27_queueWith2Stacks.enqueue("B");
        StdOut.println(exercise27_queueWith2Stacks.dequeue());
        StdOut.println(exercise27_queueWith2Stacks.dequeue());

        exercise27_queueWith2Stacks.enqueue("C");
        exercise27_queueWith2Stacks.enqueue("D");
        exercise27_queueWith2Stacks.enqueue("E");
        exercise27_queueWith2Stacks.enqueue("F");

        StdOut.println("Size: " + exercise27_queueWith2Stacks.size() + " Expected: 4");

        StdOut.println(exercise27_queueWith2Stacks.dequeue());
        StdOut.println(exercise27_queueWith2Stacks.dequeue());

        StdOut.println("Expected output from dequeue(): A B C D");

        StdOut.println("IsEmpty: " + exercise27_queueWith2Stacks.isEmpty() + " Expected: false");
        StdOut.println("Size: " + exercise27_queueWith2Stacks.size() + " Expected: 2");
    }

}
