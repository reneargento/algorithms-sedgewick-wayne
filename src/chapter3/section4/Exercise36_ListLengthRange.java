package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 28/07/17.
 */
public class Exercise36_ListLengthRange {

    private void doExperiment() {
        int[] keySizes = {1000, 10000, 100000, 1000000};

        for(int keySizeIndex = 0; keySizeIndex < keySizes.length; keySizeIndex++) {
            int numberOfKeys = keySizes[keySizeIndex];
            StdOut.println("N = " + numberOfKeys);

            SeparateChainingHashTableFixedSize<Integer, Integer> separateChainingHashTableFixedSize =
                    new SeparateChainingHashTableFixedSize<>(numberOfKeys / 100);

            for(int i = 0; i < numberOfKeys; i++) {
                int randomIntegerKey = StdRandom.uniform(Integer.MAX_VALUE);
                separateChainingHashTableFixedSize.put(randomIntegerKey, randomIntegerKey);
            }

            int shortestListLength = Integer.MAX_VALUE;
            int longestListLength = Integer.MIN_VALUE;

            for(SeparateChainingHashTableFixedSize.SequentialSearchSymbolTable list :
                    separateChainingHashTableFixedSize.symbolTable) {
                if (list != null) {
                    if (list.size() < shortestListLength) {
                        shortestListLength = list.size();
                    }
                    if (list.size() > longestListLength) {
                        longestListLength = list.size();
                    }
                }
            }

            StdOut.println("Length of the shortest list: " + shortestListLength);
            StdOut.println("Length of the longest list: " + longestListLength);
        }
    }

    public static void main(String[] args) {
        Exercise36_ListLengthRange listLengthRange = new Exercise36_ListLengthRange();
        listLengthRange.doExperiment();
    }

}
