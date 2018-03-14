package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 01/05/17.
 */
public class Exercise40_CrossoverToBinarySearch {

    public static void main(String[] args) {
        new Exercise40_CrossoverToBinarySearch().doExperiment();
    }

    private void doExperiment() {
        SequentialSearchSymbolTable<Integer, Integer> sequentialSearchSymbolTable = new SequentialSearchSymbolTable<>();
        BinarySearchSymbolTable<Integer, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        boolean tenTimesFasterNFound = false;
        boolean aHundredTimesFasterNFound = false;
        boolean aThousandTimesFasterNFound = false;

        int maxKey = 100000;

        for(int i = 1; i <= maxKey; i++) {

            sequentialSearchSymbolTable.put(i, i);
            binarySearchSymbolTable.put(i, i);

            int searchKeyNotFound = maxKey + 1;

            long sequentialSearchStart = System.nanoTime();
            sequentialSearchSymbolTable.get(searchKeyNotFound);
            long sequentialSearchRunningTime = System.nanoTime() - sequentialSearchStart;

            long binarySearchStart = System.nanoTime();
            binarySearchSymbolTable.get(searchKeyNotFound);
            long binarySearchRunningTime = System.nanoTime() - binarySearchStart;

            if (sequentialSearchRunningTime == binarySearchRunningTime) {
                continue;
            }

            if (sequentialSearchRunningTime >= binarySearchRunningTime * 10
                    && !tenTimesFasterNFound) {
                StdOut.println("Value of N for which binary search becomes 10 times faster than sequential search: " + i);
                tenTimesFasterNFound = true;
            }
            if (sequentialSearchRunningTime >= binarySearchRunningTime * 100
                    && !aHundredTimesFasterNFound) {
                StdOut.println("Value of N for which binary search becomes 100 times faster than sequential search: " + i);
                aHundredTimesFasterNFound = true;
            }
            if (sequentialSearchRunningTime >= binarySearchRunningTime * 1000
                    && !aThousandTimesFasterNFound) {
                StdOut.println("Value of N for which binary search becomes 1000 times faster than sequential search: " + i);
                aThousandTimesFasterNFound = true;
            }

            if (aThousandTimesFasterNFound) {
                break;
            }
        }
    }

}
