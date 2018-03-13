package chapter2.section5;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Rene Argento on 15/04/17.
 */
public class Exercise25_PointsInThePlane {

    static class Point2D implements Comparable<Point2D> {

        private double x;
        private double y;

        Point2D(double x, double y) {
            if (Double.isInfinite(x) || Double.isInfinite(y)) {
                throw new IllegalArgumentException("Coordinates must be finite");
            }

            if (Double.isNaN(x) || Double.isNaN(y)) {
                throw new IllegalArgumentException("Coordinates cannot be NaN");
            }

            if (x == 0.0) {
                this.x = 0.0;  // convert -0.0 to +0.0
            } else {
                this.x = x;
            }

            if (y == 0.0) {
                this.y = 0.0;  // convert -0.0 to +0.0
            } else {
                this.y = y;
            }
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }

        //Returns the polar radius of this point.
        public double r() {
            return Math.sqrt(x * x + y * y);
        }

        //Returns the angle of this point in polar coordinates.
        public double theta() {
            return Math.atan2(y, x);
        }

        private double angleTo(Point2D that) {
            double distanceX = that.x - this.x;
            double distanceY = that.y - this.y;

            return Math.atan2(distanceY, distanceX);
        }

        public double distance(Point2D that) {
            double distanceX = this.x - that.x;
            double distanceY = this.y - that.y;

            return Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        }

        public void draw() {
            StdDraw.point(x, y);
        }

        @Override
        public int compareTo(Point2D that) {
            if (this.y < that.y) {
                return -1;
            }
            if (this.y > that.y) {
                return 1;
            }
            if (this.x < that.x) {
                return -1;
            }
            if (this.x > that.x) {
                return 1;
            }
            return 0;
        }

        Comparator<Point2D> distanceFromPointComparator = new Comparator<Point2D>() {
            @Override
            public int compare(Point2D point2D1, Point2D point2D2) {
                double distanceFromThisPoint1 = distance(point2D1);
                double distanceFromThisPoint2 = distance(point2D2);

                if (distanceFromThisPoint1 < distanceFromThisPoint2) {
                    return -1;
                } else if (distanceFromThisPoint1 > distanceFromThisPoint2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        Comparator<Point2D> polarAngleFromThisPointComparator = new Comparator<Point2D>() {
            @Override
            public int compare(Point2D point2D1, Point2D point2D2) {
                double distanceXPoint1 = point2D1.x - x;
                double distanceYPoint1 = point2D1.y - y;
                double distanceXPoint2 = point2D2.x - x;
                double distanceYPoint2 = point2D2.y - y;

                if (distanceYPoint1 >= 0 && distanceYPoint2 < 0) {
                    return -1;  // point2D1 is above; point2D2 is below
                } else if (distanceYPoint2 >= 0 && distanceYPoint1 < 0) {
                    return 1;  // point2D1 below; point2D2 above
                } else if (distanceYPoint1 == 0 && distanceYPoint2 == 0) {   // 3-collinear and horizontal
                    if (distanceXPoint1 >= 0 && distanceXPoint2 < 0) {
                        return -1;
                    } else if (distanceXPoint2 >= 0 && distanceXPoint1 < 0) {
                        return 1;
                    } else {
                        return  0;
                    }
                } else {
                    return -counterClockwise(Point2D.this, point2D1, point2D2); // both above or below
                }
            }
        };

        //Returns 1 if point1 → point2 → point3 is a counterclockwise turn
        public int counterClockwise(Point2D point1, Point2D point2, Point2D point3) {
            double area2 = (point2.x - point1.x) * (point3.y - point1.y) - (point2.y - point1.y) * (point3.x - point1.x);

            if (area2 < 0) {
                return -1;
            } else if (area2 > 0) {
                return 1;
            } else {
                return  0;
            }
        }
    }

    private static class XOrder implements Comparator<Point2D>{
        @Override
        public int compare(Point2D point2D1, Point2D point2D2) {
            if (point2D1.x < point2D2.x) {
                return -1;
            } else if (point2D1.x > point2D2.x) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static class YOrder implements Comparator<Point2D>{
        @Override
        public int compare(Point2D point2D1, Point2D point2D2) {
            if (point2D1.y < point2D2.y) {
                return -1;
            } else if (point2D1.y > point2D2.y) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static class DistanceFromOriginOrder implements Comparator<Point2D> {
        @Override
        public int compare(Point2D point2D1, Point2D point2D2) {
            double distanceFromOriginPoint2D1 = point2D1.distance(new Point2D(0, 0));
            double distanceFromOriginPoint2D2 = point2D2.distance(new Point2D(0, 0));

            if (distanceFromOriginPoint2D1 < distanceFromOriginPoint2D2) {
                return -1;
            } else if (distanceFromOriginPoint2D1 > distanceFromOriginPoint2D2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        Point2D point2D1 = new Point2D(0.2, 1.3);
        Point2D point2D2 = new Point2D(92.12, 140.82);
        Point2D point2D3 = new Point2D(20, 22.0);
        Point2D point2D4 = new Point2D(30, 0);

        Point2D[] points = {point2D1, point2D2, point2D3, point2D4};

        //X coordinate order
        StdOut.println("Order by X coordinate");
        Arrays.sort(points, new XOrder());

        for(Point2D point2D : points) {
            StdOut.print(point2D.x + " ");
        }

        StdOut.println("\nExpected: 0.2 20.0 30.0 92.12");

        //Y coordinate order
        StdOut.println("\nOrder by Y coordinate");
        Arrays.sort(points, new YOrder());

        for(Point2D point2D : points) {
            StdOut.print(point2D.y + " ");
        }

        StdOut.println("\nExpected: 0.0 1.3 22.0 140.82");

        //Distance from origin order
        StdOut.println("\nOrder by distance from origin");
        Arrays.sort(points, new DistanceFromOriginOrder());

        for(Point2D point2D : points) {
            StdOut.print(point2D.x + " ");
        }

        StdOut.println("\nExpected: 0.2 20.0 30.0 92.12");

        //Distance from specified point
        StdOut.println("\nOrder by distance from specified point");
        Arrays.sort(points, point2D2.distanceFromPointComparator);

        for(Point2D point2D : points) {
            StdOut.print(point2D.x + " ");
        }

        StdOut.println("\nExpected: 92.12 20.0 30.0 0.2");

        //Polar angle distance from specified point
        StdOut.println("\nOrder by polar angle distance from specified point");
        Arrays.sort(points, point2D2.polarAngleFromThisPointComparator);

        for(Point2D point2D : points) {
            StdOut.print(point2D.x + " ");
        }

        StdOut.println("\nExpected: 92.12 0.2 20.0 30.0");
    }

}
