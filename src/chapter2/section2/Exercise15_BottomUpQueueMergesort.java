package chapter2.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 13/02/17.
 */
public class Exercise15_BottomUpQueueMergesort {

    public static void main(String[] args) {

        Comparable[] array = generateRandomArray(10);

        Queue<Queue<Comparable>> mergedQueue = bottomUpQueueMergesort(array);

        StdOut.println("Merged queues:");
        for(Comparable item : mergedQueue.peek()) {
            StdOut.print(item + " ");
        }
    }

    private static Comparable[] generateRandomArray(int arrayLength) {
        Comparable[] array = new Comparable[arrayLength];

        for(int i = 0; i < arrayLength; i++) {
            array[i] = StdRandom.uniform();
        }

        return array;
    }

    private static Queue<Queue<Comparable>> bottomUpQueueMergesort(Comparable[] array) {

        Queue<Queue<Comparable>> sortedQueues = new Queue<>();

        for(Comparable value : array) {
            Queue<Comparable> queue = new Queue<>();
            queue.enqueue(value);

            sortedQueues.enqueue(queue);
        }

        while(sortedQueues.size() > 1) {
            Queue<Comparable> queue1 = sortedQueues.dequeue();
            Queue<Comparable> queue2 = sortedQueues.dequeue();

            Queue<Comparable> mergedQueue = Exercise14_MergingSortedQueues.mergeQueues(queue1, queue2);
            sortedQueues.enqueue(mergedQueue);
        }

        return sortedQueues;
    }
}
