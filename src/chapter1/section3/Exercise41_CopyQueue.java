package chapter1.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/22/16.
 */
public class Exercise41_CopyQueue<Item> extends Queue<Item>{

    public Exercise41_CopyQueue(Queue<Item> queue) {
        for(Item item : queue) {
            enqueue(item);
        }
    }

    public static void main(String[] args) {
        Queue<Integer> originalQueue = new Queue<>();
        originalQueue.enqueue(1);
        originalQueue.enqueue(2);
        originalQueue.enqueue(3);
        originalQueue.enqueue(4);

        Exercise41_CopyQueue<Integer> queueCopy = new Exercise41_CopyQueue<>(originalQueue);
        queueCopy.enqueue(5);
        queueCopy.enqueue(99);

        originalQueue.dequeue();

        queueCopy.dequeue();
        queueCopy.dequeue();

        StringJoiner originalQueueItems = new StringJoiner(" ");
        for (int item : originalQueue) {
            originalQueueItems.add(String.valueOf(item));
        }

        StdOut.println("Original Queue: " + originalQueueItems.toString());
        StdOut.println("Expected: 2 3 4");

        StdOut.println();

        StringJoiner copyQueueItems = new StringJoiner(" ");
        for (int item : queueCopy) {
            copyQueueItems.add(String.valueOf(item));
        }

        StdOut.println("Queue Copy: " + copyQueueItems.toString());
        StdOut.println("Expected: 3 4 5 99");
    }

}
