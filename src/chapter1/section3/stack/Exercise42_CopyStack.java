package chapter1.section3.stack;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by rene on 8/23/16.
 */
public class Exercise42_CopyStack<Item> extends Stack<Item> {

    public Exercise42_CopyStack(Stack<Item> stack) {
        Stack<Item> temp = new Stack<>();

        for(Item item : stack) {
            temp.push(item);
        }

        for(Item item : temp){
            push(item);
        }
    }

    public static void main(String[] args){
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

        StdOut.println("Original Stack");
        for(int item : originalStack){
            StdOut.print(item + " ");
        }

        StdOut.println();
        StdOut.println("Stack Copy");
        for(int item : stackCopy){
            StdOut.print(item + " ");
        }
    }

}
