package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

/**
 * Created by Rene Argento on 20/11/16.
 */
public class Exercise29_StequeWith2Stacks<Item> {

    private Stack<Item> headStack;
    private Stack<Item> tailStack;

    public Exercise29_StequeWith2Stacks() {
        headStack = new Stack<>();
        tailStack = new Stack<>();
    }

    //O(1)
    public int size() {
        return headStack.size() + tailStack.size();
    }

    //O(1)
    public boolean isEmpty() {
        return headStack.isEmpty() && tailStack.isEmpty();
    }

    //O(1)
    public void push(Item item) {
        headStack.push(item);
    }

    //Amortized O(1)
    public Item pop() {

        if (isEmpty()) {
            throw new RuntimeException("Steque underflow");
        }

        if (headStack.size() == 0) {
            //Move all items from tail stack to head stack
            while(!tailStack.isEmpty()) {
                headStack.push(tailStack.pop());
            }
        }

        return headStack.pop();
    }

    //O(1)
    public void enqueue(Item item) {
        tailStack.push(item);
    }

    public static void main(String[] args) {
        Exercise29_StequeWith2Stacks<Integer> exercise29_stequeWith2Stacks = new Exercise29_StequeWith2Stacks<>();

        StdOut.println("IsEmpty: " + exercise29_stequeWith2Stacks.isEmpty() + " Expected: true");

        exercise29_stequeWith2Stacks.push(1);
        exercise29_stequeWith2Stacks.push(2);
        exercise29_stequeWith2Stacks.push(3);

        StdOut.println(exercise29_stequeWith2Stacks.pop());
        StdOut.println(exercise29_stequeWith2Stacks.pop());

        exercise29_stequeWith2Stacks.enqueue(4);
        exercise29_stequeWith2Stacks.enqueue(5);

        StdOut.println("Size: " + exercise29_stequeWith2Stacks.size() + " Expected: 3");
        StdOut.println("IsEmpty: " + exercise29_stequeWith2Stacks.isEmpty() + " Expected: false");

        StdOut.println(exercise29_stequeWith2Stacks.pop());
        StdOut.println(exercise29_stequeWith2Stacks.pop());

        StdOut.println("Expected output from pop(): 3 2 1 4");
    }

}
