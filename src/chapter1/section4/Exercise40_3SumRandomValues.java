package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rene Argento on 26/11/16.
 */
public class Exercise40_3SumRandomValues {

    private static int generateRandomValuesAndThreeSumCount(int n) {
        //Compute threeSumCount() for n random 6-digit ints.
        int max = 1000000;
        int[] values = new int[n];

        for(int i = 0; i < n; i++) {
            values[i] = StdRandom.uniform(-max, max);
        }

        return threeSumCount(values);
    }

    private static int threeSumCount(int[] values) {
        //Count triples that sum to 0
        Map<Integer, List<Integer>> numbers = new HashMap<>();

        for(int i = 0; i < values.length; i++) {
            List<Integer> indexesOfNumber = numbers.get(values[i]);

            if (indexesOfNumber == null) {
                List<Integer> indexes = new ArrayList<>();
                indexes.add(i);

                numbers.put(values[i], indexes);
            } else {
                indexesOfNumber.add(i);
            }
        }

        int count = 0;

        for(int i = 0; i < values.length - 1; i++) {
            for(int j = i + 1; j < values.length; j++) {
                int neededNumberForATriple = (values[i] + values[j]) * -1;

                List<Integer> indexes = numbers.get(neededNumberForATriple);

                if (indexes != null) {
                    for(int index : indexes) {
                        if (index > i && index > j) {
                            count++;
                            break;
                        }
                    }
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {

        int previousCount = generateRandomValuesAndThreeSumCount(125);

        StdOut.printf("%6s %7s %6s\n", "N", "Triples", "Ratio");

        for(int n = 250; n <= 16000; n += n) {
            int count = generateRandomValuesAndThreeSumCount(n);
            double ratio = (double) count / (double) previousCount;
            previousCount = count;

            StdOut.printf("%6d %6d %5.1f\n", n, count, ratio);
        }
    }

}
