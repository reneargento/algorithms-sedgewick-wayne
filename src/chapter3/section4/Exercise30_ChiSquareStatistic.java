package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 23/07/17.
 */
public class Exercise30_ChiSquareStatistic {

    class SeparateChainingHashTableChiSquare<Key, Value> extends SeparateChainingHashTable<Key, Value> {

        SeparateChainingHashTableChiSquare(int initialSize, int averageListSize) {
            super(initialSize, averageListSize);
        }

        double computeChiSquareStatistic() {
            double sizeOverKeyCount = size / (double) keysSize;
            double keyCountOverSize = keysSize / (double) size;

            int[] keyCountByHashValue = new int[symbolTable.length];
            for(int bucket = 0; bucket < symbolTable.length; bucket++) {
                keyCountByHashValue[bucket] = symbolTable[bucket].size;
            }

            double fSum = 0;
            for(int i = 0; i < keyCountByHashValue.length; i++) {
                fSum += Math.pow(keyCountByHashValue[i] - keyCountOverSize, 2);
            }

            return sizeOverKeyCount * fSum;
        }
    }

    public static void main(String[] args) {
        Exercise30_ChiSquareStatistic chiSquareStatistic = new Exercise30_ChiSquareStatistic();
        SeparateChainingHashTableChiSquare<Integer, Integer> separateChainingHashTableChiSquare =
                chiSquareStatistic.new SeparateChainingHashTableChiSquare<>(100, 20);

        for(int key = 0; key < 10000; key ++) {
            int randomIntegerKey = StdRandom.uniform(Integer.MAX_VALUE);
            separateChainingHashTableChiSquare.put(randomIntegerKey, randomIntegerKey);
        }

        double lowerBound = separateChainingHashTableChiSquare.size - Math.sqrt(separateChainingHashTableChiSquare.size);
        double upperBound = separateChainingHashTableChiSquare.size + Math.sqrt(separateChainingHashTableChiSquare.size);

        double constant = separateChainingHashTableChiSquare.keysSize / (double) separateChainingHashTableChiSquare.size;
        double probability = 1 - (1 / constant);

        double chiSquareStatisticValue = separateChainingHashTableChiSquare.computeChiSquareStatistic();

        StdOut.println("M - sqrt(M) = " + String.format("%.2f", lowerBound));
        StdOut.println("M + sqrt(M) = " + String.format("%.2f", upperBound));
        StdOut.println("Chi square statistic = " + String.format("%.2f", chiSquareStatisticValue));
        StdOut.println("Probability = " + String.format("%.2f%%", probability * 100));

        StdOut.print("Produces random values: ");
        if (lowerBound <= chiSquareStatisticValue && chiSquareStatisticValue <= upperBound) {
            StdOut.print("True");
        } else {
            StdOut.print("False");
        }
    }

}
