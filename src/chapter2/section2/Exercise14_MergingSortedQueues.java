package chapter2.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 13/02/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for suggesting a simpler method to merge the queues:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/107
@SuppressWarnings("unchecked")
public class Exercise14_MergingSortedQueues {

    public static void main(String[] args) {

        Queue<Comparable> queue1 = new Queue<>();
        queue1.enqueue(1);
        queue1.enqueue(3);
        queue1.enqueue(5);
        queue1.enqueue(7);
        queue1.enqueue(9);

        Queue<Comparable> queue2 = new Queue<>();
        queue2.enqueue(2);
        queue2.enqueue(4);
        queue2.enqueue(6);
        queue2.enqueue(8);

        Queue<Comparable> mergedQueue = mergeQueues(queue1, queue2);

        StdOut.print("Merged queues: ");
        for(Comparable item : mergedQueue) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 1 2 3 4 5 6 7 8 9");
    }

    public static Queue<Comparable> mergeQueues(Queue<Comparable> queue1, Queue<Comparable> queue2) {
        Queue<Comparable> mergedQueue = new Queue<>();

        while(!queue1.isEmpty() && !queue2.isEmpty()) {
            if (queue1.peek().compareTo(queue2.peek()) <= 0) {
                mergedQueue.enqueue(queue1.dequeue());
            } else {
                mergedQueue.enqueue(queue2.dequeue());
            }
        }

        while(!queue1.isEmpty()) {
            mergedQueue.enqueue(queue1.dequeue());
        }
        while(!queue2.isEmpty()) {
            mergedQueue.enqueue(queue2.dequeue());
        }
        return mergedQueue;
    }

}
