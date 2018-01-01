package chapter3.section4;

import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 21/07/17.
 */
public class Exercise22 {

    private static final int HASH_TABLE_SIZE = 997;

    private class Point2D {
        private double x;
        private double y;

        Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = (31 * hash + ((Double) x).hashCode()) % HASH_TABLE_SIZE;
            hash = (31 * hash + ((Double) y).hashCode()) % HASH_TABLE_SIZE;
            return hash;
        }
    }

    private class Interval {
        private double min;
        private double max;

        Interval(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = (31 * hash + ((Double) min).hashCode()) % HASH_TABLE_SIZE;
            hash = (31 * hash + ((Double) max).hashCode()) % HASH_TABLE_SIZE;
            return hash;
        }
    }

    private class Interval2D {
        private Interval1D x;
        private Interval1D y;

        Interval2D(Interval1D x, Interval1D y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = (31 * hash + x.hashCode()) % HASH_TABLE_SIZE;
            hash = (31 * hash + y.hashCode()) % HASH_TABLE_SIZE;
            return hash;
        }
    }

    private class Date {
        private int day;
        private int month;
        private int year;

        Date(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = (31 * hash + day) % HASH_TABLE_SIZE;
            hash = (31 * hash + month) % HASH_TABLE_SIZE;
            hash = (31 * hash + year) % HASH_TABLE_SIZE;
            return hash;
        }
    }

    public static void main(String[] args) {
        Exercise22 exercise22 = new Exercise22();
        Point2D point2D = exercise22.new Point2D(20, 13);
        StdOut.println(point2D.hashCode());

        Interval interval = exercise22.new Interval(1, 999);
        StdOut.println(interval.hashCode());

        Interval1D interval1D1 = new Interval1D(10.2, 30.20);
        Interval1D interval1D2 = new Interval1D(2, 1000);
        Interval2D interval2D = exercise22.new Interval2D(interval1D1, interval1D2);
        StdOut.println(interval2D.hashCode());

        Date date = exercise22.new Date(18, 4, 1989);
        StdOut.println(date.hashCode());
    }

}
