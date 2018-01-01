package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 29/07/17.
 */
public class Exercise41_DoubleProbing {

    private void doExperiment() {
        SeparateChainingHashTable<Integer, Integer> separateChainingHashTable = new SeparateChainingHashTable<>();
        DoubleProbingHashTable<Integer, Integer> doubleProbingHashTable = new DoubleProbingHashTable<>();

        StdOut.printf("%12s %20s %20s\n", "Operation | ","Separate-chaining HT time | ", "Double probing HT time");

        //Put tests
        int[] randomKeysPut = new int[1000000];

        for(int i = 0; i < randomKeysPut.length; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            randomKeysPut[i] = randomKey;
        }

        Stopwatch stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length; i++) {
            separateChainingHashTable.put(randomKeysPut[i], randomKeysPut[i]);
        }
        double timeSpentOnPutSeparateChaining = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length; i++) {
            doubleProbingHashTable.put(randomKeysPut[i], randomKeysPut[i]);
        }
        double timeSpentOnPutDoubleProbing = stopwatch.elapsedTime();

        printResults("Put", timeSpentOnPutSeparateChaining, timeSpentOnPutDoubleProbing);

        //Get tests
        int[] randomKeysGet = new int[500000];

        for(int i = 0; i < randomKeysGet.length; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            randomKeysGet[i] = randomKey;
        }

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysGet.length; i++) {
            separateChainingHashTable.get(randomKeysGet[i]);
        }
        double timeSpentOnGetSeparateChaining = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysGet.length; i++) {
            doubleProbingHashTable.get(randomKeysGet[i]);
        }
        double timeSpentOnGetDoubleProbing = stopwatch.elapsedTime();

        printResults("Get", timeSpentOnGetSeparateChaining, timeSpentOnGetDoubleProbing);

        //Delete tests
        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length / 2; i++) {
            separateChainingHashTable.delete(randomKeysPut[i]);
        }
        double timeSpentOnDeleteSeparateChaining = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length / 2; i++) {
            doubleProbingHashTable.delete(randomKeysPut[i]);
        }
        double timeSpentOnDeleteDoubleProbing = stopwatch.elapsedTime();

        printResults("Delete", timeSpentOnDeleteSeparateChaining, timeSpentOnDeleteDoubleProbing);
    }

    private void printResults(String operation, double separateChainingTime, double doubleProbingTime) {
        StdOut.printf("%9s %28.2f %25.2f\n", operation, separateChainingTime, doubleProbingTime);
    }

    public static void main(String[] args) {
        new Exercise41_DoubleProbing().doExperiment();
    }

}
