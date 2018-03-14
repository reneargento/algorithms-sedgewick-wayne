package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 01/05/17.
 */
public class Exercise41_CrossoverToInterpolationSearch {

    public static void main(String[] args) {
        new Exercise41_CrossoverToInterpolationSearch().doExperiment();
    }

    private void doExperiment() {
        BinarySearchSymbolTable<Integer, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        Exercise24_InterpolationSearch interpolationSearch = new Exercise24_InterpolationSearch();
        Exercise24_InterpolationSearch.BinarySearchSymbolTable<Integer> interpolationSearchSymbolTable =
                interpolationSearch.new BinarySearchSymbolTable<>();

        boolean oneTimeFasterNFound = false;
        boolean twoTimesFasterNFound = false;
        boolean tenTimesFasterNFound = false;

        int maxKey = 100000;
        int maxValue = 10;

        for(int i = 1; i <= maxKey; i++) {
            int randomValue = StdRandom.uniform(maxValue);

            binarySearchSymbolTable.put(i, randomValue);
            interpolationSearchSymbolTable.put(i, randomValue);

            int searchKeyNotFound = maxKey + 1;

            long binarySearchStart = System.nanoTime();
            binarySearchSymbolTable.get(searchKeyNotFound);
            long binarySearchRunningTime = System.nanoTime() - binarySearchStart;

            long interpolationSearchStart = System.nanoTime();
            interpolationSearchSymbolTable.get(searchKeyNotFound);
            long interpolationSearchRunningTime = System.nanoTime() - interpolationSearchStart;

            if (binarySearchRunningTime >= interpolationSearchRunningTime
                    && !oneTimeFasterNFound) {
                StdOut.println("Value of N for which interpolation search becomes the same speed as binary search: " + i);
                oneTimeFasterNFound = true;
            }
            if (binarySearchRunningTime >= interpolationSearchRunningTime * 2
                    && !twoTimesFasterNFound) {
                StdOut.println("Value of N for which interpolation search becomes 2 times faster than binary search: " + i);
                twoTimesFasterNFound = true;
            }
            if (binarySearchRunningTime >= interpolationSearchRunningTime * 10
                    && !tenTimesFasterNFound) {
                StdOut.println("Value of N for which interpolation search becomes 10 times faster than binary search: " + i);
                tenTimesFasterNFound = true;
            }

            if (tenTimesFasterNFound) {
                break;
            }
        }
    }

}
