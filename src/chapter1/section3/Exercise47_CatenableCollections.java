package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/23/16.
 */
// Thanks to patentfox (https://github.com/patentfox) for noting that the catenation operation should have constant time.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/285
// Thanks to Vivek Bhojawala (https://github.com/VBhojawala) for mentioning that the exercise should be solved using
// circular linked lists.
// Also thanks to emergencyd (https://github.com/emergencyd) for mentioning that the stack catenation was incorrect.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/12
public class Exercise47_CatenableCollections<Item> {

    private static class Queue<Item> extends LinkedListCircular<Item> {
        public void enqueue(Item item) {
            insertLast(item);
        }

        public Item dequeue() {
            return removeFirst();
        }
    }

    private static class Stack<Item> extends LinkedListCircular<Item> {
        public void push(Item item) {
            insertFirst(item);
        }

        public Item pop() {
            return removeFirst();
        }
    }

    private static class Steque<Item> extends LinkedListCircular<Item> {
        public void push(Item item) {
            insertFirst(item);
        }

        public Item pop() {
            return removeFirst();
        }

        public void enqueue(Item item) {
            insertLast(item);
        }
    }

    public LinkedListCircular<Item> catenation(LinkedListCircular<Item> collection1,
                                               LinkedListCircular<Item> collection2) {
        if (collection1 == null || collection2 == null) {
            return null;
        }
        if (collection1.isEmpty()) {
            return collection2;
        }
        if (collection2.isEmpty()) {
            return collection1;
        }

        LinkedListCircular<Item>.Node collection2FistNode = collection2.getFirstNode();
        collection2.last.next = collection1.last.next;
        collection1.last.next = collection2FistNode;
        collection2.size += collection1.size();

        collection1.last = null;
        collection1.size = 0;
        return collection2;
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

        LinkedListCircular<Integer> linkedListCircular = catenableCollections.catenation(queue1, queue2);

        StringJoiner linkedListItems = new StringJoiner(" ");
        for (int item : linkedListCircular) {
            linkedListItems.add(String.valueOf(item));
        }
        StdOut.println("Result after catenation: " + linkedListItems.toString());
        StdOut.println("Expected: 0 1 2 3 7 8 9");
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

        LinkedListCircular<Integer> linkedListCircular = catenableCollections.catenation(stack1, stack2);

        StringJoiner linkedListItems = new StringJoiner(" ");
        for (int item : linkedListCircular) {
            linkedListItems.add(String.valueOf(item));
        }
        StdOut.println("Result after catenation: " + linkedListItems.toString());
        StdOut.println("Expected: 3 2 1 0 9 8 7");
        StdOut.println();
    }

    private static void testStequeCatenation(Exercise47_CatenableCollections<Integer> catenableCollections) {
        Steque<Integer> steque1 = new Steque<>();
        steque1.enqueue(0);
        steque1.enqueue(1);
        steque1.enqueue(2);
        steque1.enqueue(3);

        Steque<Integer> steque2 = new Steque<>();
        steque2.push(7);
        steque2.push(8);
        steque2.push(9);

        LinkedListCircular<Integer> linkedListCircular = catenableCollections.catenation(steque1, steque2);

        StringJoiner linkedListItems = new StringJoiner(" ");
        for (int item : linkedListCircular) {
            linkedListItems.add(String.valueOf(item));
        }
        StdOut.println("Result after catenation: " + linkedListItems.toString());
        StdOut.println("Expected: 0 1 2 3 9 8 7");
    }
}
