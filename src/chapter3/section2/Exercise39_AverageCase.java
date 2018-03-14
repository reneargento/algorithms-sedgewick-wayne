package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 10/06/17.
 */
public class Exercise39_AverageCase {

    private class BinarySearchTreeCompareCount<Key extends Comparable<Key>, Value> extends BinarySearchTree<Key, Value> {

        private class SearchAnalysis {
            long compares;
            boolean keyFound;

            private SearchAnalysis(long compares, boolean keyFound) {
                this.compares = compares;
                this.keyFound = keyFound;
            }
        }

        private int numberOfCompares;

        public SearchAnalysis getKey(Key key) {
            numberOfCompares = 0;

            return get(root, key);
        }

        private SearchAnalysis get(Node node, Key key) {
            if (node == null) {
                return new SearchAnalysis(numberOfCompares, false);
            }

            numberOfCompares++;

            int compare = key.compareTo(node.key);
            if (compare < 0) {
                return get(node.left, key);
            } else if (compare > 0) {
                return get(node.right, key);
            } else {
                return new SearchAnalysis(numberOfCompares, true);
            }
        }
    }

    private int totalNumberOfComparesForSearchHits;
    private int totalNumberOfComparesForSearchMisses;
    private int numberOfSearchHits;
    private int numberOfSearchMisses;

    private void doExperiment() {
        int[] binaryTreeSizes = {10000, 100000, 1000000};
        int numberOfExperiments = 100;

        for(int i = 0; i < binaryTreeSizes.length; i++) {
            List<Long> numberOfComparesSearchHits = new ArrayList<>();
            List<Long> numberOfComparesSearchMisses = new ArrayList<>();

            numberOfSearchHits = 0;
            numberOfSearchMisses = 0;

            totalNumberOfComparesForSearchHits = 0;
            totalNumberOfComparesForSearchMisses = 0;

            for(int j = 0; j < numberOfExperiments; j++) {
                computeAverageNumberOfComparesInSearch(binaryTreeSizes[i], numberOfComparesSearchHits, numberOfComparesSearchMisses);
            }

            double averageNumberOfCompareSearchHits = totalNumberOfComparesForSearchHits / (double) Math.max(1, numberOfSearchHits);
            double averageNumberOfCompareSearchMisses = totalNumberOfComparesForSearchMisses / (double) Math.max(1, numberOfSearchMisses);

            long totalSquareDeviationFromTheMeanSearchHits = 0;
            long totalSquareDeviationFromTheMeanSearchMisses = 0;

            //Standard deviation for search hits
            for(int j = 0; j < numberOfComparesSearchHits.size(); j++) {
                long numberOfCompares = numberOfComparesSearchHits.get(j);

                long squareDeviationFromTheMean = (long) Math.pow((numberOfCompares - averageNumberOfCompareSearchHits), 2);
                totalSquareDeviationFromTheMeanSearchHits += squareDeviationFromTheMean;
            }
            double varianceForSearchHits = totalSquareDeviationFromTheMeanSearchHits / averageNumberOfCompareSearchHits;
            double standardDeviationSearchHits = Math.sqrt(varianceForSearchHits);

            //Standard deviation for search misses
            for(int j = 0; j < numberOfComparesSearchMisses.size(); j++) {
                long numberOfCompares = numberOfComparesSearchMisses.get(j);

                long squareDeviationFromTheMean = (long) Math.pow((numberOfCompares - averageNumberOfCompareSearchMisses), 2);
                totalSquareDeviationFromTheMeanSearchMisses += squareDeviationFromTheMean;
            }
            double varianceForSearchMisses = totalSquareDeviationFromTheMeanSearchMisses / averageNumberOfCompareSearchMisses;
            double standardDeviationSearchMisses = Math.sqrt(varianceForSearchMisses);

            double expectedAverageNumberOfCompares = (1.39 * (Math.log(binaryTreeSizes[i]) / Math.log(2))) - 1.85;

            StdOut.println("Experiments with N = " + binaryTreeSizes[i]);
            StdOut.printf("Average number of compares for search hits: %.2f", averageNumberOfCompareSearchHits);
            StdOut.printf("\nAverage number of compares for search misses: %.2f", averageNumberOfCompareSearchMisses);
            StdOut.printf("\nExpected average number of compares for both search hits and misses: %.2f", expectedAverageNumberOfCompares);
            StdOut.printf("\nStandard deviation of compares for search hits: %.2f", standardDeviationSearchHits);
            StdOut.printf("\nStandard deviation of compares for search misses: %.2f\n\n", standardDeviationSearchMisses);
        }
    }

    private void computeAverageNumberOfComparesInSearch(int treeSize, List<Long> numberOfComparesSearchHits,
                                                        List<Long> numberOfComparesSearchMisses) {
        int numberOfSearches = 20;
        int maxValue = 1000000;
        BinarySearchTreeCompareCount<Integer, Integer> binarySearchTree = new BinarySearchTreeCompareCount<>();

        for(int i = 0; i < treeSize; i++) {
            int randomValue = StdRandom.uniform(maxValue);
            binarySearchTree.put(randomValue, randomValue);
        }

        for(int i = 0; i < numberOfSearches; i++) {
            int randomValue = StdRandom.uniform(maxValue);
            BinarySearchTreeCompareCount.SearchAnalysis searchAnalysis = binarySearchTree.getKey(randomValue);

            if (searchAnalysis.keyFound) {
                numberOfComparesSearchHits.add(searchAnalysis.compares);

                numberOfSearchHits++;
                totalNumberOfComparesForSearchHits += searchAnalysis.compares;
            } else {
                numberOfComparesSearchMisses.add(searchAnalysis.compares);

                numberOfSearchMisses++;
                totalNumberOfComparesForSearchMisses += searchAnalysis.compares;
            }
        }
    }

    public static void main(String[] args) {
        new Exercise39_AverageCase().doExperiment();
    }

}
