package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 8/23/16.
 */
// Adapted by AspectLeft (https://github.com/AspectLeft) -> Added the correct implementation
// for canAPermutationBeGenerated() on 12/25/17
// Thanks to shftdlt (https://github.com/shftdlt) for spotting a bug in the code.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/2
public class Exercise45_StackGenerability {

    private static boolean willTheStackUnderflow(String[] inputValues) {
        int itemsCount = 0;

        for(String input : inputValues) {
            if (input.equals("-")) {
                itemsCount--;
            } else {
                itemsCount++;
            }

            if (itemsCount < 0) {
                return true;
            }
        }

        return false;
    }

    private static boolean canAPermutationBeGenerated(String[] permutation) {
        Stack<Integer> stack = new Stack<>();
        int current = 0;

        for (String value: permutation) {
            int integerValue = Integer.parseInt(value);

            if (stack.isEmpty() || integerValue > stack.peek()) {
                while (current < integerValue) {
                    stack.push(current);
                    current++;
                }

                current++;
            } else if (integerValue == stack.peek()) {
                stack.pop();
            } else {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        //"Will the stack underflow?" tests
        String input1Values = "0 1 2 - - -";
        String[] input1 = input1Values.split("\\s");

        StdOut.print("Input 1 - Will Underflow? ");
        StdOut.println(willTheStackUnderflow(input1) + " Expected: false");

        String input2Values = "0 1 2 - - - 3 4 5 - - 6 - - -";
        String[] input2 = input2Values.split("\\s");

        StdOut.print("Input 2 - Will Underflow? ");
        StdOut.println(willTheStackUnderflow(input2) + " Expected: true");

        String input3Values = "0 - - 1 2 -";
        String[] input3 = input3Values.split("\\s");

        StdOut.print("Input 3 - Will Underflow? ");
        StdOut.println(willTheStackUnderflow(input3) + " Expected: true");

        //"Can permutation be generated?" tests
        StdOut.println("\nCan a permutation be generated?");
        StdOut.print("Input: 4 3 2 1 0 9 8 7 6 5 - ");
        StdOut.println(canAPermutationBeGenerated("4 3 2 1 0 9 8 7 6 5".split(" ")) + " Expected: true");
        StdOut.print("Input: 4 6 8 7 5 3 2 9 0 1 - ");
        StdOut.println(canAPermutationBeGenerated("4 6 8 7 5 3 2 9 0 1".split(" ")) + " Expected: false");
        StdOut.print("Input: 2 5 6 7 4 8 9 3 1 0 - ");
        StdOut.println(canAPermutationBeGenerated("2 5 6 7 4 8 9 3 1 0".split(" ")) + " Expected: true");
        StdOut.print("Input: 4 3 2 1 0 5 6 7 8 9 - ");
        StdOut.println(canAPermutationBeGenerated("4 3 2 1 0 5 6 7 8 9".split(" ")) + " Expected: true");
        StdOut.print("Input: 1 2 3 4 5 6 9 8 7 0 - ");
        StdOut.println(canAPermutationBeGenerated("1 2 3 4 5 6 9 8 7 0".split(" ")) + " Expected: true");
        StdOut.print("Input: 0 4 6 5 3 8 1 7 2 9 - ");
        StdOut.println(canAPermutationBeGenerated("0 4 6 5 3 8 1 7 2 9".split(" ")) + " Expected: false");
        StdOut.print("Input: 1 4 7 9 8 6 5 3 0 2 - ");
        StdOut.println(canAPermutationBeGenerated("1 4 7 9 8 6 5 3 0 2".split(" ")) + " Expected: false");
        StdOut.print("Input: 2 1 4 3 6 5 8 7 9 0 - ");
        StdOut.println(canAPermutationBeGenerated("2 1 4 3 6 5 8 7 9 0".split(" ")) + " Expected: true");
        StdOut.print("Input: 4 3 2 1 0 5 9 7 8 6 - ");
        StdOut.println(canAPermutationBeGenerated("4 3 2 1 0 5 9 7 8 6".split(" ")) + " Expected: false");
    }

}
