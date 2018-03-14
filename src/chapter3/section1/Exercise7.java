package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise7 {

    private static final int UPPER_BOUND_EXCLUSIVE = 1000;

    public static void main(String[] args) {
        new Exercise7().doExperiment();
    }

    private void doExperiment() {
        int[] arraySizes = {10, 100, 1000, 10000, 100000, 1000000};

        for(int i = 0; i < arraySizes.length; i++) {
            int[] randomArray = ArrayGenerator.generateRandomIntegerArray(arraySizes[i], UPPER_BOUND_EXCLUSIVE);
            int distinctKeys = frequencyCounter(randomArray);

            StdOut.println("Number of distinct keys among " + arraySizes[i] + " random nonnegative integers less than "
            + UPPER_BOUND_EXCLUSIVE + ": " + distinctKeys);
        }
    }

    private int frequencyCounter(int[] integers) {

        BinarySearchSymbolTable<Integer, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(int integer : integers) {
            if (!binarySearchSymbolTable.contains(integer)) {
                binarySearchSymbolTable.put(integer, 1);
            } else {
                binarySearchSymbolTable.put(integer, binarySearchSymbolTable.get(integer) + 1);
            }
        }

        return binarySearchSymbolTable.size();
    }

}
