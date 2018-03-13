package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/23/16.
 */
public class Exercise42_CopyStack<Item> extends Stack<Item> {

    public Exercise42_CopyStack(Stack<Item> stack) {
        Stack<Item> temp = new Stack<>();

        for(Item item : stack) {
            temp.push(item);
        }

        for(Item item : temp) {
            push(item);
        }
    }

    public static void main(String[] args) {
        Stack<Integer> originalStack = new Stack<>();
        originalStack.push(1);
        originalStack.push(2);
        originalStack.push(3);
        originalStack.push(4);

        Exercise42_CopyStack<Integer> stackCopy = new Exercise42_CopyStack<>(originalStack);

        stackCopy.push(5);
        stackCopy.push(6);

        originalStack.pop();
        originalStack.pop();

        stackCopy.pop();

        StringJoiner originalStackItems = new StringJoiner(" ");
        for (int item : originalStack) {
            originalStackItems.add(String.valueOf(item));
        }

        StdOut.println("Original Stack: " + originalStackItems.toString());
        StdOut.println("Expected: 2 1");

        StdOut.println();

        StringJoiner copyStackItems = new StringJoiner(" ");
        for (int item : stackCopy) {
            copyStackItems.add(String.valueOf(item));
        }

        StdOut.println("Stack Copy: " + copyStackItems.toString());
        StdOut.println("Expected: 5 4 3 2 1");
    }

}
