package chapter2.section2;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 13/02/17.
 */
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
        for(Comparable item : mergedQueue) {
            StdOut.print(item + " ");
        }
    }

    public static Queue<Comparable> mergeQueues(Queue<Comparable> queue1, Queue<Comparable> queue2) {
        Queue<Comparable> mergedQueue = new Queue<>();

        Comparable[] queue1Array = new Comparable[queue1.size()];
        Comparable[] queue2Array = new Comparable[queue2.size()];

        int array1Index = 0;
        for(Comparable value : queue1) {
            queue1Array[array1Index++] = value;
        }

        int array2Index = 0;
        for(Comparable value : queue2) {
            queue2Array[array2Index++] = value;
        }

        int leftIndex = 0;
        int rightIndex = 0;

        while(leftIndex < queue1Array.length && rightIndex < queue2Array.length) {
            if(queue1Array[leftIndex].compareTo(queue2Array[rightIndex]) <= 0) {
                mergedQueue.enqueue(queue1Array[leftIndex++]);
            } else {
                mergedQueue.enqueue(queue2Array[rightIndex++]);
            }
        }

        while(leftIndex < queue1Array.length) {
            mergedQueue.enqueue(queue1Array[leftIndex++]);
        }

        while(rightIndex < queue2Array.length) {
            mergedQueue.enqueue(queue2Array[rightIndex++]);
        }

        return mergedQueue;
    }

}
