package util;

/**
 * Created by Rene Argento on 28/12/17.
 */
public class MathUtil {

    public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
