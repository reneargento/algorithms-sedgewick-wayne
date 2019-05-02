package chapter1.section4;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 20/11/16.
 */
// Thanks to shftdlt (https://github.com/shftdlt) for fixing a bug in this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/48
public class Exercise26_3Collinearity {

    public static void main(String[] args) {

        Exercise26_3Collinearity exercise26_3Collinearity = new Exercise26_3Collinearity();

        Point2D[] points = new Point2D[6];
        for(int i = 0; i < points.length; i++) {
            Point2D point = new Point2D(i, i + 1.5);
            points[i] = point;
        }

        int numberOfTriples1 = exercise26_3Collinearity.countTriples(points);
        StdOut.println("Number of triples: " + numberOfTriples1  + " Expected: 20");

        // Based on https://www.algebra.com/algebra/homework/Length-and-distance/Length-and-distance.faq.question.530663.html
        //(-3,4) (3,2) (6,1) are on the same line
        Point2D pointA = new Point2D(-3, 4);
        Point2D pointB = new Point2D(3, 2);
        Point2D pointC = new Point2D(6, 1);

        Point2D[] points2 = {pointA, pointB, pointC};

        int numberOfTriples2 = exercise26_3Collinearity.countTriples(points2);
        StdOut.println("Number of triples: " + numberOfTriples2  + " Expected: 1");

        Point2D pointD = new Point2D(6, 1);

        Point2D[] points3 = {pointA, pointB, pointC, pointD};

        int numberOfTriples3 = exercise26_3Collinearity.countTriples(points3);
        StdOut.println("Number of triples: " + numberOfTriples3  + " Expected: 4");

        // Case with cubic y coordinate
        Point2D pointE = new Point2D(1, 1);
        Point2D pointF = new Point2D(2, 8);
        Point2D pointG = new Point2D(-3, -27);

        Point2D[] points4 = {pointE, pointF, pointG};
        int numberOfTriples4 = exercise26_3Collinearity.countTriples(points4);
        StdOut.println("Number of triples: " + numberOfTriples4  + " Expected: 1");
    }

    // This algorithm can be reduced to 3-SUM since we are searching for
    // a combination of 3 points that can be summed to specific value
    private int countTriples(Point2D[] points) {

        boolean allYCoordinatesAreXCubic = true;

        for(Point2D point2D : points) {

            if (point2D.y() != Math.pow(point2D.x(), 3)) {
                allYCoordinatesAreXCubic = false;
                break;
            }
        }

        if (allYCoordinatesAreXCubic) {
            return countTriplesWithCubicYs(points);
        } else {
            return countTriplesUsingSlopes(points);
        }
    }

    // Based on http://stackoverflow.com/questions/3813681/checking-to-see-if-3-points-are-on-the-same-line
    private int countTriplesUsingSlopes(Point2D[] points) {
        // If (y1 - y2) * (x1 - x3) == (y1 - y3) * (x1 - x2) then the points are collinear

        Map<Double, List<Integer>> slopes = new HashMap<>();

        for(int i = 0; i < points.length; i++) {
            for(int j = i + 1; j < points.length; j++) {
                double slope;

                if (points[i].x() - points[j].x() == 0) {
                    slope = 0;
                } else {
                    slope = (points[i].y() - points[j].y()) / (points[i].x() - points[j].x());
                }
                List<Integer> pointIndexes = slopes.get(slope);

                if (pointIndexes == null) {
                    List<Integer> indexes = new ArrayList<>();
                    indexes.add(i);
                    indexes.add(j);

                    slopes.put(slope, indexes);
                } else {
                    if (!slopes.get(slope).contains(i)) {
                        slopes.get(slope).add(i);
                    }
                    if (!slopes.get(slope).contains(j)) {
                        slopes.get(slope).add(j);
                    }
                }
            }
        }

        int triples = 0;

        for(int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double slope;

                if (points[i].x() - points[j].x() == 0) {
                    slope = 0;
                } else {
                    slope = (points[i].y() - points[j].y()) / (points[i].x() - points[j].x());
                }

                List<Integer> pointIndexes = slopes.get(slope);

                if (pointIndexes != null) {

                    for (Integer index : pointIndexes) {
                        if (index > i && index > j) {
                            triples++;
                        }
                    }
                }
            }
        }

        return triples;
    }

    private int countTriplesWithCubicYs(Point2D[] points) {
        // Based on http://stackoverflow.com/questions/4179581/what-is-the-most-efficient-algorithm-to-find-a-straight-line-that-goes-through-m
        // If the points have the same slope, then they are collinear (on the same line)

        // If x + y + z = 0 then the slope of the line from x to y is
        // (y^3 - x^3) / (y - x) = y^2 + yx + x^2
        // And the slope of the line from x to z is
        //(z^3 - x^3) / (z - x) = z^2 + zx + x^2

        // Conversely, if the slope from x to y equals the slope from x to z then
        // y^2 + yx + x^2 = z^2 + zx + x^2

        // Which implies that
        // (y - z) (x + y + z) = 0
        // So either y = z or z = -x - y which suffices to prove that the reduction is valid

        // If there are duplicates in X, first
        // check whether x + 2y = 0 for any x and duplicate element y
        // (in linear time using hashing or O(n lg n) time using sorting),
        // and then remove the duplicates before reducing to the collinear point-finding problem.

        Map<Double, List<Integer>> pointsX = new HashMap<>();

        for(int i = 0; i < points.length; i++) {
            List<Integer> pointIndexes = pointsX.get(points[i].x());

            if (pointIndexes == null) {
                List<Integer> indexes = new ArrayList<>();
                indexes.add(i);

                pointsX.put(points[i].x(), indexes);
            } else {
                pointsX.get(points[i].x()).add(i);
            }
        }

        int triples = 0;

        for(int i = 0; i < points.length - 1; i++) {
            for(int j = i + 1; j < points.length; j++) {

                double complementPoint = -1 * (points[i].x() + points[j].x());
                List<Integer> pointIndexes = pointsX.get(complementPoint);

                if (pointIndexes != null) {

                    for (Integer index : pointIndexes) {
                        if (index > i && index > j) {
                            triples++;
                        }
                    }
                }
            }
        }

        return triples;
    }

}
