package util;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Rene Argento on 29/06/2018.
 */
public class StatsUtil {

    public static void plotBars(double[] values, double halfWidth) {
        for (int i = 0; i < values.length; i++) {
            StdDraw.filledRectangle(i, values[i] / 2, halfWidth, values[i] / 2);
        }
    }

}