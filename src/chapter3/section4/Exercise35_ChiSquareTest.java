package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

/**
 * Created by Rene Argento on 27/07/17.
 */
public class Exercise35_ChiSquareTest extends Exercise30_ChiSquareStatistic {

    private class SeparateChainingHashTableChiSquareTest<Key, Value> extends SeparateChainingHashTableChiSquare<Key, Value> {

        SeparateChainingHashTableChiSquareTest(int initialSize, int averageListSize) {
            super(initialSize, averageListSize);
        }

        public int hash(Key key) {
            int hash = key.hashCode() & 0x7fffffff;
            return hash % size;
        }
    }

    public static void main(String[] args) {
        Exercise35_ChiSquareTest chiSquareTest = new Exercise35_ChiSquareTest();

        int numberOfKeys = 1000000;

        StdOut.println("Integer key type hash function");
        SeparateChainingHashTableChiSquareTest<Integer, Integer> separateChainingHashTableIntegerChiSquareTest =
                chiSquareTest.new SeparateChainingHashTableChiSquareTest<>(20, 5);

        for(int i = 0; i < numberOfKeys; i++) {
            int randomIntegerKey = StdRandom.uniform(Integer.MAX_VALUE);
            separateChainingHashTableIntegerChiSquareTest.put(randomIntegerKey, randomIntegerKey);
        }

        double chiSquareStatistic1 = separateChainingHashTableIntegerChiSquareTest.computeChiSquareStatistic();
        double lowerBound1 = separateChainingHashTableIntegerChiSquareTest.size - Math.sqrt(separateChainingHashTableIntegerChiSquareTest.size);
        double upperBound1 = separateChainingHashTableIntegerChiSquareTest.size + Math.sqrt(separateChainingHashTableIntegerChiSquareTest.size);

        double constant1 = separateChainingHashTableIntegerChiSquareTest.keysSize / (double) separateChainingHashTableIntegerChiSquareTest.size;
        double probability1 = 1 - (1 / constant1);

        StdOut.print("Produces random values: ");
        if (lowerBound1 <= chiSquareStatistic1 && chiSquareStatistic1 <= upperBound1) {
            StdOut.println("True");
        } else {
            StdOut.println("False");
        }
        StdOut.println("Probability = " + String.format("%.2f%%", probability1 * 100));

        StdOut.println("\nDouble key type hash function");
        SeparateChainingHashTableChiSquareTest<Double, Double> separateChainingHashTableDoubleChiSquareTest =
                chiSquareTest.new SeparateChainingHashTableChiSquareTest<>(20, 5);

        for(int i = 0; i < numberOfKeys; i++) {
            double randomDoubleKey = StdRandom.uniform();
            separateChainingHashTableDoubleChiSquareTest.put(randomDoubleKey, randomDoubleKey);
        }

        double chiSquareStatistic2 = separateChainingHashTableDoubleChiSquareTest.computeChiSquareStatistic();
        double lowerBound2 = separateChainingHashTableDoubleChiSquareTest.size - Math.sqrt(separateChainingHashTableDoubleChiSquareTest.size);
        double upperBound2 = separateChainingHashTableDoubleChiSquareTest.size + Math.sqrt(separateChainingHashTableDoubleChiSquareTest.size);

        double constant2 = separateChainingHashTableDoubleChiSquareTest.keysSize / (double) separateChainingHashTableDoubleChiSquareTest.size;
        double probability2 = 1 - (1 / constant2);

        StdOut.print("Produces random values: ");
        if (lowerBound2 <= chiSquareStatistic2 && chiSquareStatistic2 <= upperBound2) {
            StdOut.println("True");
        } else {
            StdOut.println("False");
        }
        StdOut.println("Probability = " + String.format("%.2f%%", probability2 * 100));

        StdOut.println("\nString key type hash function");
        SeparateChainingHashTableChiSquareTest<String, String> separateChainingHashTableStringChiSquareTest =
                chiSquareTest.new SeparateChainingHashTableChiSquareTest<>(20, 5);

        for(int i = 0; i < numberOfKeys; i++) {
            StringBuilder string = new StringBuilder();

            for(int c = 0; c < 10; c++) {
                //Generate random char between 'A' and 'z'
                char currentChar = (char) StdRandom.uniform(Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX,
                        Constants.ASC_II_LOWERCASE_LETTERS_FINAL_INDEX + 1);
                string.append(currentChar);
            }

            String stringKey = string.toString();
            separateChainingHashTableStringChiSquareTest.put(stringKey, stringKey);
        }

        double chiSquareStatistic3 = separateChainingHashTableStringChiSquareTest.computeChiSquareStatistic();
        double lowerBound3 = separateChainingHashTableStringChiSquareTest.size - Math.sqrt(separateChainingHashTableStringChiSquareTest.size);
        double upperBound3 = separateChainingHashTableStringChiSquareTest.size + Math.sqrt(separateChainingHashTableStringChiSquareTest.size);

        double constant3 = separateChainingHashTableStringChiSquareTest.keysSize / (double) separateChainingHashTableStringChiSquareTest.size;
        double probability3 = 1 - (1 / constant3);

        StdOut.print("Produces random values: ");
        if (lowerBound3 <= chiSquareStatistic3 && chiSquareStatistic3 <= upperBound3) {
            StdOut.println("True");
        } else {
            StdOut.println("False");
        }
        StdOut.println("Probability = " + String.format("%.2f%%", probability3 * 100));
    }
}
