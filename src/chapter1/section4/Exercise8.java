package chapter1.section4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 9/28/16.
 */
public class Exercise8 {

    public static void main(String[] args){
        In in = new In(args[0]);
        int[] values = in.readAllInts();

        StdOut.println(countEqualNumbers(values));
    }

    private static int countEqualNumbers(int[] values) {

        Map<Integer, Integer> valuesMap = new HashMap<>();
        int equalNumbersCount = 0;

        for(int i = 0; i < values.length; i++) {
            int count = 0;
            if(valuesMap.containsKey(values[i])) {
                count = valuesMap.get(values[i]);
            }
            count++;
            valuesMap.put(values[i], count);
        }

        for(int numberKey : valuesMap.keySet()) {
            if(valuesMap.get(numberKey) > 1) {
                //Quantity of numbers : Quantity of equal pairs
                //        2                      1
                //        3                      3
                //        4                      6
                //        5                      10
                // The quantity of equal pairs is equal to:
                // (n - 1) * n / 2
                int n = valuesMap.get(numberKey);
                equalNumbersCount += (n - 1) * n / 2;
            }
        }

        return equalNumbersCount;
    }

}
