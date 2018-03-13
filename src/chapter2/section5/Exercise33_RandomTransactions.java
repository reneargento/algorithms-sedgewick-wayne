package chapter2.section5;

import chapter2.section1.Exercise21_ComparableTransactions;
import chapter2.section1.ShellSort;
import chapter2.section2.TopDownMergeSort;
import chapter2.section3.QuickSort;
import chapter2.section4.HeapSort;
import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 18/04/17.
 */
public class Exercise33_RandomTransactions {

    private final String SHELL_SORT = "ShellSort";
    private final String MERGE_SORT = "MergeSort";
    private final String QUICK_SORT = "QuickSort";
    private final String HEAP_SORT = "HeapSort";

    public static void main(String[] args) {

        Exercise33_RandomTransactions randomTransactions = new Exercise33_RandomTransactions();

        int numberOfObjects = 0;

        if (args.length > 0) {
            numberOfObjects = Integer.parseInt(args[0]);
        }

        randomTransactions.doExperiment(numberOfObjects);
    }

    private void doExperiment(int numberOfObjects) {

        int[] values;
        if (numberOfObjects != 0) {
            values = new int[]{numberOfObjects};
        } else {
            values = new int[]{1000, 10000, 100000, 1000000};
        }

        String[] sortAlgorithms = {SHELL_SORT, MERGE_SORT, QUICK_SORT, HEAP_SORT};

        StdOut.printf("%13s %13s %13s\n", "Number of Transactions | ","Sort Method | ", "Time Spent");

        for(int n = 0; n < values.length; n++) {
            Exercise21_ComparableTransactions[] transactions = generateRandomTransactions(values[n]);

            for(int i = 0; i < sortAlgorithms.length; i++) {
                Exercise21_ComparableTransactions[] transactionsCopy = new Exercise21_ComparableTransactions[transactions.length];
                System.arraycopy(transactions, 0, transactionsCopy, 0, transactions.length);

                Stopwatch timer = new Stopwatch();

                switch (sortAlgorithms[i]) {
                    case SHELL_SORT:
                        ShellSort.shellSort(transactionsCopy);
                        break;
                    case MERGE_SORT:
                        TopDownMergeSort.mergeSort(transactionsCopy);
                        break;
                    case QUICK_SORT:
                        QuickSort.quickSort(transactionsCopy);
                        break;
                    case HEAP_SORT:
                        HeapSort.heapSort(transactionsCopy);
                        break;
                }
                double runningTime = timer.elapsedTime();

                printResults(values[n], sortAlgorithms[i], runningTime);
            }
        }
    }

    private Exercise21_ComparableTransactions[] generateRandomTransactions(int numberOfObjects) {
        Exercise21_ComparableTransactions[] transactions = new Exercise21_ComparableTransactions[numberOfObjects];

        for(int i = 0; i < numberOfObjects; i++) {
            String who = "Client " + (i + 1);

            int month = StdRandom.uniform(12) + 1;
            int maxDay = month == 2 ? 28 : 30;
            int day = StdRandom.uniform(maxDay) + 1;
            int year = StdRandom.uniform(1900, 2018);
            Date date = new Date(month, day, year);

            double amount = (double) Math.round(StdRandom.uniform(0.0, 1000000.0) * 100) / 100;

            Exercise21_ComparableTransactions transaction = new Exercise21_ComparableTransactions(who, date, amount);
            transactions[i] = transaction;
        }

        return transactions;
    }

    private void printResults(int numberOfTransactions, String sortMethod, double timeSpent) {
        StdOut.printf("%22d %14s %16.2f\n", numberOfTransactions, sortMethod, timeSpent);
    }

}
