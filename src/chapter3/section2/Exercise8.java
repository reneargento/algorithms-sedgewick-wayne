package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 27/05/17.
 */
public class Exercise8 {

    public static void main(String[] args) {
        StdOut.println("Test 0: " + optCompares(0) + " Expected: 0");
        StdOut.println("Test 1: " + optCompares(1) + " Expected: 2");
        StdOut.println("Test 2: " + optCompares(2) + " Expected: 2");
        StdOut.println("Test 3: " + optCompares(3) + " Expected: 2");
        StdOut.println("Test 7: " + optCompares(7) + " Expected: 3");
        StdOut.println("Test 8: " + optCompares(8) + " Expected: 3");
        StdOut.println("Test 15: " + optCompares(15) + " Expected: 4");
        StdOut.println("Test 16: " + optCompares(16) + " Expected: 4");
    }

    //O(h)
    private static int optCompares(int n) {
        if (n == 0) {
            return 0;
        }

        int totalCompares = 0;

        int heightMinus1 = (int) (Math.log(n) / Math.log(2));
        int numberOfNodesBeforeLastLevel = 0;

        //Compute the compares in all levels, except the last (because the last level may not be complete)
        for(int i = 1; i <= heightMinus1; i++) {
            totalCompares += i * Math.pow(2, i - 1);

            numberOfNodesBeforeLastLevel += Math.pow(2, i - 1);
        }

        //Add compares required to reach the nodes in the last level
        int nodesInLastLevel = n - numberOfNodesBeforeLastLevel;
        totalCompares += nodesInLastLevel * (heightMinus1 + 1);

        //Total compares is the internal path length
        int numberOfComparesRequiredByRandomSearchHit = (totalCompares / n) + 1;

        return numberOfComparesRequiredByRandomSearchHit;
    }

}
