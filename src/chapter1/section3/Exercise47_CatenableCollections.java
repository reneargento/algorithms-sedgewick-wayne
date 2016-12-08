package chapter1.section3;

import chapter1.section3.linked.list.Exercise32_Steque;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by rene on 8/23/16.
 */
public class Exercise47_CatenableCollections<Item> {

    public void catenationQueues(Queue<Item> queue1, Queue<Item> queue2){

        if(queue1 == null || queue2 == null){
            return;
        }

        while(queue2.size() > 0) {
            Item item = queue2.dequeue();
            queue1.enqueue(item);
        }
    }

    public void catenationStacks(Stack<Item> stack1, Stack<Item> stack2) {

        if(stack1 == null || stack2 == null) {
            return;
        }

        Stack<Item> temp = new Stack<>();

        while(stack2.size() > 0) {
            Item item = stack2.pop();
            temp.push(item);
        }

        while (temp.size() > 0) {
            Item item = temp.pop();
            stack1.push(item);
        }
    }

    public void catenationSteques(Exercise32_Steque<Item> steque1, Exercise32_Steque<Item> steque2) {

        if(steque1 == null || steque2 == null) {
            return;
        }

        while (steque2.size() != 0) {
            Item item = steque2.pop();
            steque1.enqueue(item);
        }
    }

    public static void main(String[] args){

        Exercise47_CatenableCollections<Integer> catenableCollections = new Exercise47_CatenableCollections<>();

        testQueueCatenation(catenableCollections);
        testStackCatenation(catenableCollections);
        testStequeCatenation(catenableCollections);
    }

    private static void testQueueCatenation(Exercise47_CatenableCollections<Integer> catenableCollections) {
        //Queue catenation test

        Queue<Integer> queue1 = new Queue<>();
        queue1.enqueue(0);
        queue1.enqueue(1);
        queue1.enqueue(2);
        queue1.enqueue(3);

        Queue<Integer> queue2 = new Queue<>();
        queue2.enqueue(7);
        queue2.enqueue(8);
        queue2.enqueue(9);

        catenableCollections.catenationQueues(queue1, queue2);

        StdOut.println("Queue 1 after catenation");
        for(int item : queue1){
            StdOut.print(item + " ");
        }

        StdOut.println();

        StdOut.println("Queue 2 after catenation");
        for(int item : queue2){
            StdOut.print(item + " ");
        }
    }

    private static void testStackCatenation(Exercise47_CatenableCollections<Integer> catenableCollections) {
        //Stack catenation test

        Stack<Integer> stack1 = new Stack<>();
        stack1.push(0);
        stack1.push(1);
        stack1.push(2);
        stack1.push(3);

        Stack<Integer> stack2 = new Stack<>();
        stack2.push(7);
        stack2.push(8);
        stack2.push(9);

        catenableCollections.catenationStacks(stack1, stack2);

        StdOut.println("Stack 1 after catenation");
        for(int item : stack1){
            StdOut.print(item + " ");
        }

        StdOut.println();

        StdOut.println("Stack 2 after catenation");
        for(int item : stack2){
            StdOut.print(item + " ");
        }
    }

    private static void testStequeCatenation(Exercise47_CatenableCollections<Integer> catenableCollections) {
        //Stack catenation test

        Exercise32_Steque<Integer> steque1 = new Exercise32_Steque<>();
        steque1.enqueue(0);
        steque1.enqueue(1);
        steque1.enqueue(2);
        steque1.enqueue(3);

        Exercise32_Steque<Integer> steque2 = new Exercise32_Steque<>();
        steque2.enqueue(7);
        steque2.enqueue(8);
        steque2.enqueue(9);

        catenableCollections.catenationSteques(steque1, steque2);

        StdOut.println("Steque 1 after catenation");
        for(int item : steque1){
            StdOut.print(item + " ");
        }

        StdOut.println();

        StdOut.println("Steque 2 after catenation");
        for(int item : steque2){
            StdOut.print(item + " ");
        }
    }

}
