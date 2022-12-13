package chapter3.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 27/05/17.
 */
// Thanks to ckwastra (https://github.com/ckwastra) for suggesting a O(1) solution.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/277
public class Exercise8 {

    public static void main(String[] args) {
        StdOut.printf("Test 0: %.2f Expected: 0.00\n", optCompares(0));
        StdOut.printf("Test 1: %.2f Expected: 1.00\n", optCompares(1));
        StdOut.printf("Test 2: %.2f Expected: 1.50\n", optCompares(2));
        StdOut.printf("Test 3: %.2f Expected: 1.67\n", optCompares(3));
        StdOut.printf("Test 7: %.2f Expected: 2.43\n", optCompares(7));
        StdOut.printf("Test 8: %.2f Expected: 2.63\n", optCompares(8));
        StdOut.printf("Test 15: %.2f Expected: 3.27\n", optCompares(15));
        StdOut.printf("Test 16: %.2f Expected: 3.38\n", optCompares(16));
    }

    // O(1)
    // Consider H = height of the tree.
    // For nodes on the first H levels, the total path length is:
    // SUM(from i = 1 to H - 1) i * 2^i = (H - 2) * 2^H + 2
    // The total path in the last level is:
    // (N - (2^H - 1)) * H = (N - 2^H + 1) * H
    // Adding them, we have:
    // (H - 2) * 2^H + 2 + (N - 2^H + 1) * H = (N + 1) * H - 2^(H + 1) + 2
    private static double optCompares(int n) {
        if (n == 0) {
            return 0;
        }
        int height = (int) (Math.log(n) / Math.log(2));
        return ((n + 1) * height - Math.pow(2, height + 1) + 2) / n + 1;
    }

    // O(h)
    private static double optCompares2(int n) {
        if (n == 0) {
            return 0;
        }
        int totalCompares = 0;
        int height = (int) (Math.log(n) / Math.log(2));
        int numberOfNodesBeforeLastLevel = 0;

        // Compute the compares in all levels, except the last (because the last level may not be complete)
        for (int i = 1; i <= height; i++) {
            totalCompares += i * Math.pow(2, i - 1);
            numberOfNodesBeforeLastLevel += Math.pow(2, i - 1);
        }

        // Add compares required to reach the nodes in the last level
        int nodesInLastLevel = n - numberOfNodesBeforeLastLevel;
        totalCompares += nodesInLastLevel * (height + 1);

        // Total compares is the internal path length
        return (totalCompares / (double) n);
    }
}
