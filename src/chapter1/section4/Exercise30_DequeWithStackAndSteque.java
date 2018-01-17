package chapter1.section4;

import chapter1.section3.Exercise32_Steque;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

/**
 * Created by Rene Argento on 20/11/16.
 */
public class Exercise30_DequeWithStackAndSteque<Item> {

    //Stack will handle left operations
    private Stack<Item> stack;
    //Steque will handle right operations
    private Exercise32_Steque<Item> steque;

    public Exercise30_DequeWithStackAndSteque() {
        stack = new Stack<>();
        steque = new Exercise32_Steque<>();
    }

    //O(1)
    public int size() {
        return stack.size() + steque.size();
    }

    //O(1)
    public boolean isEmpty() {
        return stack.isEmpty() && steque.isEmpty();
    }

    //O(1)
    public void pushLeft(Item item) {
        stack.push(item);
    }

    //Amortized O(1)
    public Item popLeft() {
        if (isEmpty()) {
            throw new RuntimeException("Deque underflow");
        }

        if (stack.isEmpty()) {
            while (!steque.isEmpty()) {
                stack.push(steque.pop());
            }
        }

        return stack.pop();
    }

    //O(1)
    public void pushRight(Item item) {
        steque.push(item);
    }

    //Amortized O(1)
    public Item popRight() {
        if (isEmpty()) {
            throw new RuntimeException("Deque underflow");
        }

        if (steque.isEmpty()) {
            while (!stack.isEmpty()) {
                steque.push(stack.pop());
            }
        }

        return steque.pop();
    }

    public static void main(String[] args) {
        Exercise30_DequeWithStackAndSteque<Integer> exercise30_dequeWithStackAndSteque = new Exercise30_DequeWithStackAndSteque<>();
        StdOut.println("IsEmpty: " + exercise30_dequeWithStackAndSteque.isEmpty() + " Expected: true");

        exercise30_dequeWithStackAndSteque.pushLeft(1);
        exercise30_dequeWithStackAndSteque.pushLeft(2);
        exercise30_dequeWithStackAndSteque.pushLeft(3);

        StdOut.println(exercise30_dequeWithStackAndSteque.popLeft());
        StdOut.println(exercise30_dequeWithStackAndSteque.popLeft());

        exercise30_dequeWithStackAndSteque.pushRight(7);
        exercise30_dequeWithStackAndSteque.pushRight(8);

        StdOut.println("Size: " + exercise30_dequeWithStackAndSteque.size() + " Expected: 3");
        StdOut.println("IsEmpty: " + exercise30_dequeWithStackAndSteque.isEmpty() + " Expected: false");

        StdOut.println(exercise30_dequeWithStackAndSteque.popLeft());
        StdOut.println(exercise30_dequeWithStackAndSteque.popLeft());
        StdOut.println(exercise30_dequeWithStackAndSteque.popRight());

        StdOut.println("Expected output from pop(): 3 2 1 7 8");

    }

}
