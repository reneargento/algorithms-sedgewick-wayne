package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Rene Argento on 15/04/17.
 */
public class Exercise27_OneDimensionalIntervals {

    private class Interval1D {

        private double left;
        private double right;

        Interval1D(double left, double right) {
            if (right < left) {
                throw new IllegalArgumentException("Right cannot be smaller than left");
            }

            this.left = left;
            this.right = right;
        }

        public double left() {
            return left;
        }

        public double right() {
            return right;
        }

        public double length() {
            return Math.abs(right - left);
        }

        public boolean contains(double x) {
            return left <= x && x <= right;
        }

        public boolean intersects(Interval1D that) {
            if (this.right < that.left) {
                return false;
            }
            if (this.left > that.right) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "(" + left + ", " + right + ")";
        }
    }

    private Comparator<Interval1D> comparatorByLeftEndpoint() {
        return new Comparator<Interval1D>() {
            @Override
            public int compare(Interval1D interval1, Interval1D interval2) {
                if (interval1.left < interval2.left) {
                    return -1;
                } else if (interval1.left > interval2.left) {
                    return 1;
                }

                return 0;
            }
        };
    }

    private Comparator<Interval1D> comparatorByRightEndpoint() {
        return new Comparator<Interval1D>() {
            @Override
            public int compare(Interval1D interval1, Interval1D interval2) {
                if (interval1.right < interval2.right) {
                    return -1;
                } else if (interval1.right > interval2.right) {
                    return 1;
                }

                return 0;
            }
        };
    }

    private Comparator<Interval1D> comparatorByLength() {
        return new Comparator<Interval1D>() {
            @Override
            public int compare(Interval1D interval1, Interval1D interval2) {
                if (interval1.length() < interval2.length()) {
                    return -1;
                } else if (interval1.length() > interval2.length()) {
                    return 1;
                }

                return 0;
            }
        };
    }

    public static void main(String[] args) {
        Exercise27_OneDimensionalIntervals oneDimensionalIntervals = new Exercise27_OneDimensionalIntervals();

        Interval1D interval1 = oneDimensionalIntervals.new Interval1D(40, 100);
        Interval1D interval2 = oneDimensionalIntervals.new Interval1D(90, 95);
        Interval1D interval3 = oneDimensionalIntervals.new Interval1D(-10, 30);
        Interval1D interval4 = oneDimensionalIntervals.new Interval1D(30, 45.2);
        Interval1D interval5 = oneDimensionalIntervals.new Interval1D(22.34, 22.38);

        Interval1D[] interval1Ds = {interval1, interval2, interval3, interval4, interval5};

        //Order by left endpoint
        StdOut.println("Order by left endpoint");

        Arrays.sort(interval1Ds, oneDimensionalIntervals.comparatorByLeftEndpoint());
        for(Interval1D interval1D : interval1Ds) {
            StdOut.print(interval1D + " ");
        }

        StdOut.println("\nExpected: (-10.0, 30.0) (22.34, 22.38) (30.0, 45.2) (40.0, 100.0) (90.0, 95.0)");

        //Order by right endpoint
        StdOut.println("\nOrder by right endpoint");

        Arrays.sort(interval1Ds, oneDimensionalIntervals.comparatorByRightEndpoint());
        for(Interval1D interval1D : interval1Ds) {
            StdOut.print(interval1D + " ");
        }

        StdOut.println("\nExpected: (22.34, 22.38) (-10.0, 30.0) (30.0, 45.2) (90.0, 95.0) (40.0, 100.0)");

        //Order by length
        StdOut.println("\nOrder by length");

        Arrays.sort(interval1Ds, oneDimensionalIntervals.comparatorByLength());
        for(Interval1D interval1D : interval1Ds) {
            StdOut.print(interval1D + " ");
        }

        StdOut.println("\nExpected: (22.34, 22.38) (90.0, 95.0) (30.0, 45.2) (-10.0, 30.0) (40.0, 100.0)");
    }

}
