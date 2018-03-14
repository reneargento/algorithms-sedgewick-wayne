package chapter3.section5;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by Rene Argento on 19/08/17.
 */
public class Exercise28_UniQueue {

    private class UniQueue<Item> implements Iterable<Item> {

        private Queue<Item> queue;
        private HashSet<Item> existenceSet;

        UniQueue() {
            existenceSet = new HashSet<>();
            queue = new Queue<>();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public int size() {
            return queue.size();
        }

        public void enqueue(Item item) {
            if (existenceSet.contains(item)) {
                return;
            }

            queue.enqueue(item);
            existenceSet.add(item);
        }

        public Item dequeue() {
            return queue.dequeue();
        }

        public Iterator<Item> iterator() {
            return queue.iterator();
        }

    }

    public static void main(String[] args) {
        Exercise28_UniQueue exercise28_uniQueue = new Exercise28_UniQueue();
        UniQueue<Integer> uniQueue = exercise28_uniQueue.new UniQueue<>();

        StdOut.println("IsEmpty: " + uniQueue.isEmpty() + " Expected: true");

        uniQueue.enqueue(0);
        uniQueue.enqueue(1);
        uniQueue.enqueue(2);
        uniQueue.enqueue(4);
        uniQueue.enqueue(8);

        StdOut.println("Size: " + uniQueue.size() + " Expected: 5");

        uniQueue.enqueue(2);
        StdOut.println("Size: " + uniQueue.size() + " Expected: 5");

        uniQueue.dequeue();
        StdOut.println("Size: " + uniQueue.size() + " Expected: 4");

        uniQueue.enqueue(8);
        StdOut.println("Size: " + uniQueue.size() + " Expected: 4");

        uniQueue.enqueue(16);
        StdOut.println("Size: " + uniQueue.size() + " Expected: 5");

        StdOut.println("\nItems:");
        for(int item : uniQueue) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 1 2 4 8 16");
    }

}
