package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 9/30/16.
 */
public class Exercise17_FarthestPair {

    public static void main(String[] args) {
        double[] array1 = {-5.2, 9.4, 20, -10, 21.1, 40, 50, -20};
        double[] array2 = {-4, -3, 0, 10, 20};
        double[] array3 = {-10, -3, 0, 2, 4, 20};

        double[] farthestPair1 = farthestPair(array1);
        double[] farthestPair2 = farthestPair(array2);
        double[] farthestPair3 = farthestPair(array3);

        StdOut.println("Farthest pair: " + farthestPair1[0] + " " + farthestPair1[1] + " Expected: -20.0 50.0");
        StdOut.println("Farthest pair: " + farthestPair2[0] + " " + farthestPair2[1] + " Expected: -4.0 20.0");
        StdOut.println("Farthest pair: " + farthestPair3[0] + " " + farthestPair3[1] + " Expected: -10.0 20.0");
    }

    private static double[] farthestPair(double[] array) {
        double[] farthestPair = new double[2];

        if (array.length == 0) {
            throw new RuntimeException("Array cannot be null");
        }

        double currentMin = array[0];
        double currentMax = array[0];

        farthestPair[0] = array[0];
        farthestPair[1] = array[0];

        for(int i = 1; i < array.length; i++) {
            if (array[i] < currentMin) {
                currentMin = array[i];
                farthestPair[0] = array[i];
            }

            if (array[i] > currentMax) {
                currentMax = array[i];
                farthestPair[1] = array[i];
            }
        }

        return farthestPair;
    }

}
