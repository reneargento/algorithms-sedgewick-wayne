package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 30/07/17.
 */
public class Exercise42_DoubleHashing {

    private void doExperiment() {
        LinearProbingHashTable<Integer, Integer> linearProbingHashTable = new LinearProbingHashTable<>(10);
        DoubleHashingHashTable<Integer, Integer> doubleHashingHashTable = new DoubleHashingHashTable<>(10);

        StdOut.printf("%12s %20s %20s\n", "Operation | ","Linear-probing HT time | ", "Double hashing HT time");

        //Put tests
        int[] randomKeysPut = new int[1000000];

        for(int i = 0; i < randomKeysPut.length; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            randomKeysPut[i] = randomKey;
        }

        Stopwatch stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length; i++) {
            linearProbingHashTable.put(randomKeysPut[i], randomKeysPut[i]);
        }
        double timeSpentOnPutLinearProbing = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length; i++) {
            doubleHashingHashTable.put(randomKeysPut[i], randomKeysPut[i]);
        }
        double timeSpentOnPutDoubleHashing = stopwatch.elapsedTime();

        printResults("Put", timeSpentOnPutLinearProbing, timeSpentOnPutDoubleHashing);

        //Get tests
        int[] randomKeysGet = new int[500000];

        for(int i = 0; i < randomKeysGet.length; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            randomKeysGet[i] = randomKey;
        }

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysGet.length; i++) {
            linearProbingHashTable.get(randomKeysGet[i]);
        }
        double timeSpentOnGetLinearProbing = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysGet.length; i++) {
            doubleHashingHashTable.get(randomKeysGet[i]);
        }
        double timeSpentOnGetDoubleHashing = stopwatch.elapsedTime();

        printResults("Get", timeSpentOnGetLinearProbing, timeSpentOnGetDoubleHashing);

        //Delete tests
        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length / 2; i++) {
            linearProbingHashTable.delete(randomKeysPut[i]);
        }
        double timeSpentOnDeleteLinearProbing = stopwatch.elapsedTime();

        stopwatch = new Stopwatch();
        for(int i = 0; i < randomKeysPut.length / 2; i++) {
            doubleHashingHashTable.delete(randomKeysPut[i]);
        }
        double timeSpentOnDeleteDoubleHashing = stopwatch.elapsedTime();

        printResults("Delete", timeSpentOnDeleteLinearProbing, timeSpentOnDeleteDoubleHashing);
    }

    private void printResults(String operation, double linearProbingTime, double doubleHashingTime) {
        StdOut.printf("%9s %25.2f %25.2f\n", operation, linearProbingTime, doubleHashingTime);
    }

    public static void main(String[] args) {
        new Exercise42_DoubleHashing().doExperiment();
    }

}
