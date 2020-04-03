package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

/**
 * Created by Rene Argento on 20/11/16.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for finding a bug on the popLeft() and popRight()
// methods: https://github.com/reneargento/algorithms-sedgewick-wayne/issues/104
// Based on http://stackoverflow.com/questions/23585523/implementing-deque-using-3-stacks-amortized-time-o1
public class Exercise31_DequeWith3Stacks<Item> {

    private Stack<Item> headStack;
    private Stack<Item> middleStack;
    private Stack<Item> tailStack;

    public Exercise31_DequeWith3Stacks() {
        headStack = new Stack<>();
        middleStack = new Stack<>();
        tailStack = new Stack<>();
    }

    // O(1)
    public int size() {
        return headStack.size() + middleStack.size() + tailStack.size();
    }

    // O(1)
    public boolean isEmpty() {
        return headStack.isEmpty() && middleStack.isEmpty() && tailStack.isEmpty();
    }

    // O(1)
    public void pushLeft(Item item) {
        headStack.push(item);
    }

    // Amortized O(1)
    public Item popLeft() {
        if (isEmpty()) {
            throw new RuntimeException("Deque underflow");
        }
        Item item = null;

        if (!headStack.isEmpty()) {
            item = headStack.pop();
        } else {
            if (!tailStack.isEmpty()) {
                moveHalfItems(tailStack, headStack);
                item = headStack.pop();
            }
        }

        return item;
    }

    // O(1)
    public void pushRight(Item item) {
        tailStack.push(item);
    }

    // Amortized O(1)
    public Item popRight() {
        if (isEmpty()) {
            throw new RuntimeException("Deque underflow");
        }
        Item item = null;

        if (!tailStack.isEmpty()) {
            item = tailStack.pop();
        } else {
            if (!headStack.isEmpty()) {
                moveHalfItems(headStack, tailStack);
                item = tailStack.pop();
            }
        }

        return item;
    }

    private void moveHalfItems(Stack<Item> fullStack, Stack<Item> emptyStack) {
        int fullStackHalfSize = fullStack.size() / 2;

        // Move half items from fullStack to middle stack
        for(int i = 0; i < fullStackHalfSize; i++) {
            middleStack.push(fullStack.pop());
        }

        // Move the other half items from fullStack to emptyStack
        while(!fullStack.isEmpty()) {
            emptyStack.push(fullStack.pop());
        }

        // Return all items from middle stack to fullStack
        while(!middleStack.isEmpty()) {
            fullStack.push(middleStack.pop());
        }
    }

    public static void main(String[] args) {
        Exercise31_DequeWith3Stacks<Integer> exercise31_dequeWith3Stacks = new Exercise31_DequeWith3Stacks<>();
        StdOut.println("IsEmpty: " + exercise31_dequeWith3Stacks.isEmpty() + " Expected: true");

        exercise31_dequeWith3Stacks.pushLeft(1);
        exercise31_dequeWith3Stacks.pushLeft(2);
        exercise31_dequeWith3Stacks.pushLeft(3);
        exercise31_dequeWith3Stacks.pushLeft(4);

        StdOut.println(exercise31_dequeWith3Stacks.popRight());
        StdOut.println(exercise31_dequeWith3Stacks.popLeft());
        StdOut.println(exercise31_dequeWith3Stacks.popLeft());

        StdOut.println("Expected output from pop(): 1 4 3");

        exercise31_dequeWith3Stacks.pushRight(7);
        exercise31_dequeWith3Stacks.pushRight(8);

        StdOut.println("Size: " + exercise31_dequeWith3Stacks.size() + " Expected: 3");
        StdOut.println("IsEmpty: " + exercise31_dequeWith3Stacks.isEmpty() + " Expected: false");

        StdOut.println(exercise31_dequeWith3Stacks.popLeft());
        StdOut.println(exercise31_dequeWith3Stacks.popLeft());
        StdOut.println(exercise31_dequeWith3Stacks.popRight());

        StdOut.println("Expected output from pop(): 2 7 8");
    }

}
