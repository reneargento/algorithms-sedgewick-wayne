package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 9/30/16.
 */
public class Exercise16_ClosestPair {

    public static void main(String[] args) {
        double[] array1 = {-5.2, 9.4, 20, -10, 21.1, 40, 50, -20};
        double[] array2 = {-4, -3, 0, 10, 20};
        double[] array3 = {-10, -3, 0, 2, 4, 20};

        double[] closestPair1 = closestPair(array1);
        double[] closestPair2 = closestPair(array2);
        double[] closestPair3 = closestPair(array3);

        StdOut.println("Closest pair: " + closestPair1[0] + " " + closestPair1[1] + " Expected: 20.0 21.1");
        StdOut.println("Closest pair: " + closestPair2[0] + " " + closestPair2[1] + " Expected: -4.0 -3.0");
        StdOut.println("Closest pair: " + closestPair3[0] + " " + closestPair3[1] + " Expected: 0.0 2.0");
    }

    private static double[] closestPair(double[] array) {
        double[] closestPair = new double[2];

        double currentMinimumDifference = Double.MAX_VALUE;

        Arrays.sort(array);

        for(int i = 0; i < array.length - 1; i++) {
            if (Math.abs(array[i] - array[i + 1]) < currentMinimumDifference) {
                currentMinimumDifference = Math.abs(array[i] - array[i + 1]);

                closestPair[0] = array[i];
                closestPair[1] = array[i + 1];
            }
        }

        return closestPair;
    }

}
