package chapter1.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to pmfer2016 (https://github.com/pmfer2016) for reporting that the int type is not enough for
// holding all the Fibonacci values.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/283
public class Exercise19 {

    public static void main(String[] args) {
//		for (int n = 0; n < 90; n++) {
//			StdOut.println(n + " - " + F(n));
//		}
        for (int n = 0; n < 90; n++) {
            long[] values;

            if (n == 0 || n == 1) {
                values = new long[2];
            } else {
                values = new long[n + 1];
            }

            values[0] = 0;
            values[1] = 1;
            StdOut.println(n + ": " + enhancedF(n, values));
        }
    }

    private static int F(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return F(n - 1) + F(n - 2);
    }

    private static long enhancedF(int n, long[] values) {
        if (n == 0) return values[0];
        if (n == 1) return values[1];

        for (int i = 2; i <= n; i++) {
            values[i] = values[i - 2] + values[i - 1];
        }
        return values[n];
    }
}
