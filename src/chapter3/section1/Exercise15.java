package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class Exercise15 {

    public static void main(String[] args) {
        new Exercise15().computePercentageTimeSpentOnInsertions();
    }

    private void computePercentageTimeSpentOnInsertions() {
        int[] numberOfSearches = {1000, 1000000, 1000000000};

        BinarySearchSymbolTable<Integer, String> binarySearchSymbolTable = new BinarySearchSymbolTable<>();
        for(int i = 0; i < 10000; i++) {
            binarySearchSymbolTable.put(i, "Value " + i);
        }

        StdOut.printf("%13s %25s\n", "Searches", "Percentage of total time spent on insertions");

        for(int i = 0; i < numberOfSearches.length; i++) {
            int numberOfInsertions = numberOfSearches[i] / 1000;

            Stopwatch totalTimeTimer = new Stopwatch();

            for(int search = 0; search < numberOfSearches[i]; search++) {
                int randomValueToSearch = StdRandom.uniform(10000);
                binarySearchSymbolTable.get(randomValueToSearch);
            }

            Stopwatch insertionsTimer = new Stopwatch();

            for(int insertion = 0; insertion < numberOfInsertions; insertion++) {
                int randomValueToInsert = StdRandom.uniform(100000);
                binarySearchSymbolTable.put(randomValueToInsert, "Value " + randomValueToInsert);
            }

            double insertionsTime = insertionsTimer.elapsedTime();
            double totalTime = totalTimeTimer.elapsedTime();

            double percentageOfTimeOnInsertions = (insertionsTime / totalTime) * 100;

            printResults(numberOfSearches[i], percentageOfTimeOnInsertions);
        }
    }

    private void printResults(int numberOfSearches, double percentageOfTimeOnInsertions) {
        StdOut.printf("%13d %43.2f%%\n", numberOfSearches, percentageOfTimeOnInsertions);
    }

}
