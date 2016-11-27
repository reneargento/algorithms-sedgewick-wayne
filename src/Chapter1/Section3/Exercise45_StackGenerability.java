package Chapter1.Section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by rene on 8/23/16.
 */
public class Exercise45_StackGenerability {

    private static boolean willTheStackUnderflow(String[] inputValues) {
        int itemsCount = 0;

        for(String input : inputValues) {
            if(input.equals("-")){
                itemsCount--;
            } else {
                itemsCount++;
            }

            if(itemsCount < 0){
                return true;
            }
        }

        return false;
    }

    private static boolean canAPermutationBeGenerated(String[] inputValues) {

        int pushesCount = 0;
        int popsCount = 0;

        for(String input : inputValues) {
            if(input.equals("-")){
                popsCount++;
            } else{
                pushesCount++;
            }
        }

        return pushesCount >= popsCount;
    }

    public static void main(String[] args) {
        //TEST 1
        String input1Values = "0 1 2 - - -";
        String[] input1 = input1Values.split("\\s");

        StdOut.println("Input 1 - Will Underflow? (expected = false)");
        StdOut.println(willTheStackUnderflow(input1));

        StdOut.println("Input 1 - Can permutation be generated? (expected = true)");
        StdOut.println(canAPermutationBeGenerated(input1));

        //TEST 2
        String input2Values = "0 1 2 - - - 3 4 5 - - 6 - - -";
        String[] input2 = input2Values.split("\\s");

        StdOut.println("Input 2 - Will Underflow? (expected = true)");
        StdOut.println(willTheStackUnderflow(input2));

        StdOut.println("Input 2 - Can permutation be generated? (expected = false)");
        StdOut.println(canAPermutationBeGenerated(input2));

        //TEST 3
        String input3Values = "0 - - 1 2 -";
        String[] input3 = input3Values.split("\\s");

        StdOut.println("Input 3 - Will Underflow? (expected = true)");
        StdOut.println(willTheStackUnderflow(input3));

        StdOut.println("Input 3 - Can permutation be generated? (expected = true)");
        StdOut.println(canAPermutationBeGenerated(input3));
    }

}
