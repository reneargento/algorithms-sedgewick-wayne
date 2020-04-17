package chapter1.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/23/16.
 */
// Thanks to Vivek Bhojawala (https://github.com/VBhojawala) for mentioning that the exercise should be solved using
// circular linked lists.
// Also thanks to emergencyd (https://github.com/emergencyd) for mentioning that the stack catenation was incorrect.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/12
public class Exercise47_CatenableCollections<Item> {

    public LinkedListCircular<Item> catenationQueues(Queue<Item> queue1, Queue<Item> queue2) {
        if (queue1 == null || queue2 == null) {
            return null;
        }
        LinkedListCircular<Item> linkedListCircular = new LinkedListCircular<>();

        while (!queue1.isEmpty()) {
            Item item = queue1.dequeue();
            linkedListCircular.insert(item);
        }

        while (!queue2.isEmpty()) {
            Item item = queue2.dequeue();
            linkedListCircular.insert(item);
        }
        return linkedListCircular;
    }

    public LinkedListCircular<Item> catenationStacks(Stack<Item> stack1, Stack<Item> stack2) {
        if (stack1 == null || stack2 == null) {
            return null;
        }
        LinkedListCircular<Item> linkedListCircular = new LinkedListCircular<>();

        while (!stack1.isEmpty()) {
            linkedListCircular.insert(stack1.pop());
        }

        while (!stack2.isEmpty()) {
            linkedListCircular.insert(stack2.pop());
        }
        return linkedListCircular;
    }

    public LinkedListCircular<Item> catenationSteques(Exercise32_Steque<Item> steque1, Exercise32_Steque<Item> steque2) {
        if (steque1 == null || steque2 == null) {
            return null;
        }
        LinkedListCircular<Item> linkedListCircular = new LinkedListCircular<>();

        while (!steque1.isEmpty()) {
            Item item = steque1.pop();
            linkedListCircular.insert(item);
        }

        while (!steque2.isEmpty()) {
            Item item = steque2.pop();
            linkedListCircular.insert(item);
        }
        return linkedListCircular;
    }

    public static void main(String[] args) {
        Exercise47_CatenableCollections<Integer> catenableCollections = new Exercise47_CatenableCollections<>();

        StdOut.println("Catenation of queues");
        testQueueCatenation(catenableCollections);

        StdOut.println("Catenation of stacks");
        testStackCatenation(catenableCollections);

        StdOut.println("Catenation of steques");
        testStequeCatenation(catenableCollections);
    }

    private static void testQueueCatenation(Exercise47_CatenableCollections<Integer> catenableCollections) {
        Queue<Integer> queue1 = new Queue<>();
        queue1.enqueue(0);
        queue1.enqueue(1);
        queue1.enqueue(2);
        queue1.enqueue(3);

        Queue<Integer> queue2 = new Queue<>();
        queue2.enqueue(7);
        queue2.enqueue(8);
        queue2.enqueue(9);

        LinkedListCircular<Integer> linkedListCircular = catenableCollections.catenationQueues(queue1, queue2);

        StringJoiner linkedListItems = new StringJoiner(" ");
        for (int item : linkedListCircular) {
            linkedListItems.add(String.valueOf(item));
        }
        StdOut.println("Result after catenation: " + linkedListItems.toString());
        StdOut.println("Expected: 0 1 2 3 7 8 9");

        StdOut.println();

        StringJoiner queue1Items = new StringJoiner(" ");
        for (int item : queue1) {
            queue1Items.add(String.valueOf(item));
        }
        StdOut.println("Queue 1 after catenation: " + queue1Items.toString());
        StdOut.println("Expected: ");

        StdOut.println();

        StringJoiner queue2Items = new StringJoiner(" ");
        for (int item : queue2) {
            queue1Items.add(String.valueOf(item));
        }
        StdOut.println("Queue 2 after catenation: " + queue2Items.toString());
        StdOut.println("Expected: ");
        StdOut.println();
    }

    private static void testStackCatenation(Exercise47_CatenableCollections<Integer> catenableCollections) {
        Stack<Integer> stack1 = new Stack<>();
        stack1.push(0);
        stack1.push(1);
        stack1.push(2);
        stack1.push(3);

        Stack<Integer> stack2 = new Stack<>();
        stack2.push(7);
        stack2.push(8);
        stack2.push(9);

        LinkedListCircular<Integer> linkedListCircular = catenableCollections.catenationStacks(stack1, stack2);

        StringJoiner linkedListItems = new StringJoiner(" ");
        for (int item : linkedListCircular) {
            linkedListItems.add(String.valueOf(item));
        }
        StdOut.println("Result after catenation: " + linkedListItems.toString());
        StdOut.println("Expected: 3 2 1 0 9 8 7");

        StdOut.println();

        StringJoiner stack1Items = new StringJoiner(" ");
        for (int item : stack1) {
            stack1Items.add(String.valueOf(item));
        }
        StdOut.println("Stack 1 after catenation: " + stack1.toString());
        StdOut.println("Expected: ");

        StdOut.println();

        StringJoiner stack2Items = new StringJoiner(" ");
        for (int item : stack2) {
            stack2Items.add(String.valueOf(item));
        }
        StdOut.println("Stack 2 after catenation: " + stack2Items.toString());
        StdOut.println("Expected: ");
        StdOut.println();
    }

    private static void testStequeCatenation(Exercise47_CatenableCollections<Integer> catenableCollections) {
        Exercise32_Steque<Integer> steque1 = new Exercise32_Steque<>();
        steque1.enqueue(0);
        steque1.enqueue(1);
        steque1.enqueue(2);
        steque1.enqueue(3);

        Exercise32_Steque<Integer> steque2 = new Exercise32_Steque<>();
        steque2.push(7);
        steque2.push(8);
        steque2.push(9);

        LinkedListCircular<Integer> linkedListCircular = catenableCollections.catenationSteques(steque1, steque2);

        StringJoiner linkedListItems = new StringJoiner(" ");
        for (int item : linkedListCircular) {
            linkedListItems.add(String.valueOf(item));
        }
        StdOut.println("Result after catenation: " + linkedListItems.toString());
        StdOut.println("Expected: 0 1 2 3 9 8 7");

        StdOut.println();

        StringJoiner steque1Items = new StringJoiner(" ");
        for (int item : steque1) {
            steque1Items.add(String.valueOf(item));
        }
        StdOut.println("Steque 1 after catenation: " + steque1Items.toString());
        StdOut.println("Expected: ");

        StdOut.println();

        StringJoiner steque2Items = new StringJoiner(" ");
        for (int item : steque2) {
            steque2Items.add(String.valueOf(item));
        }
        StdOut.println("Steque 2 after catenation: " + steque2Items.toString());
        StdOut.println("Expected: ");
    }

}
