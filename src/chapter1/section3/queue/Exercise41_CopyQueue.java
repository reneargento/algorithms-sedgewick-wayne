package chapter1.section3.queue;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by rene on 8/22/16.
 */
public class Exercise41_CopyQueue<Item> extends Queue<Item>{

    public Exercise41_CopyQueue(Queue<Item> queue) {
        for(Item item : queue) {
            enqueue(item);
        }
    }

    public static void main(String[] args){
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

        StdOut.println("Original Queue");
        for(int item : originalQueue){
            StdOut.print(item + " ");
        }

        StdOut.println();
        StdOut.println("Queue Copy");
        for(int item : queueCopy){
            StdOut.print(item + " ");
        }
    }

}
