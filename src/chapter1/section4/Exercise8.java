package chapter1.section4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 9/28/16.
 */
// Thanks to Vivek Bhojawala (https://github.com/VBhojawala) for mentioning that the solution should use
// Arrays.sort(), as requested in the exercise. https://github.com/reneargento/algorithms-sedgewick-wayne/issues/6

//Quantity of numbers : Quantity of equal pairs
//        2                      1
//        3                      3
//        4                      6
//        5                      10
// The quantity of equal pairs is equal to:
// (n - 1) * n / 2
public class Exercise8 {

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] values = in.readAllInts();
        StdOut.println(countNumberOfPairs(values));

        // Tests
        int[] values1 = {1, 2, 4, 1, 2, 1, 2, 4, 5, 1, 2, 4, 5, 1, 2 ,5, 6, 7, 7, 8, 2, 1, 2, 4, 5};
        StdOut.println("Equal pairs 1: " + countNumberOfPairs(values1) + " Expected: 49");

        int[] values2 = {1, 1, 1};
        StdOut.println("Equal pairs 2: " + countNumberOfPairs(values2) + " Expected: 3");
    }

    // O(n lg n) solution
    private static int countNumberOfPairs(int[] values) {
        Arrays.sort(values);

        int count = 0;
        int currentFrequency = 1;

        for (int i = 1; i < values.length; i++) {
            if (values[i] == values[i - 1]) {
                currentFrequency++;
            } else {
                if (currentFrequency > 1) {
                    count += (currentFrequency - 1) * currentFrequency / 2;
                    currentFrequency = 1;
                }
            }
        }

        if (currentFrequency > 1) {
            count += (currentFrequency - 1) * currentFrequency / 2;
        }

        return count;
    }

    // O(n) solution
    private static int countNumberOfPairs2(int[] values) {

        Map<Integer, Integer> valuesMap = new HashMap<>();
        int equalNumbersCount = 0;

        for(int i = 0; i < values.length; i++) {
            int count = 0;
            if (valuesMap.containsKey(values[i])) {
                count = valuesMap.get(values[i]);
            }
            count++;
            valuesMap.put(values[i], count);
        }

        for(int numberKey : valuesMap.keySet()) {
            if (valuesMap.get(numberKey) > 1) {
                int n = valuesMap.get(numberKey);
                equalNumbersCount += (n - 1) * n / 2;
            }
        }

        return equalNumbersCount;
    }

}
