package chapter1.section4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 20/11/16.
 */
public class Exercise28_StackWithAQueue<Item> {

    private Queue<Item> queue;

    public Exercise28_StackWithAQueue() {
        queue = new Queue<>();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    //O(1)
    public void push(Item item) {
        queue.enqueue(item);
    }

    //O(n)
    public Item pop() {

        if (queue.isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }

        int currentSize = size();
        for(int i = 0; i < currentSize - 1; i++) {
            queue.enqueue(queue.dequeue());
        }

        return queue.dequeue();
    }


    public static void main(String[] args) {
        Exercise28_StackWithAQueue<Integer> exercise28_stackWithAQueue = new Exercise28_StackWithAQueue<>();

        StdOut.println("IsEmpty: " + exercise28_stackWithAQueue.isEmpty() + " Expected: true");

        exercise28_stackWithAQueue.push(1);
        exercise28_stackWithAQueue.push(2);
        exercise28_stackWithAQueue.push(3);

        StdOut.println(exercise28_stackWithAQueue.pop());
        StdOut.println(exercise28_stackWithAQueue.pop());

        exercise28_stackWithAQueue.push(4);
        exercise28_stackWithAQueue.push(5);

        StdOut.println("Size: " + exercise28_stackWithAQueue.size() + " Expected: 3");
        StdOut.println("IsEmpty: " + exercise28_stackWithAQueue.isEmpty() + " Expected: false");

        StdOut.println(exercise28_stackWithAQueue.pop());
        StdOut.println(exercise28_stackWithAQueue.pop());

        StdOut.println("Expected output from pop(): 3 2 5 4");
    }



}
