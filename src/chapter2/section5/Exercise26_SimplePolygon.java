package chapter2.section5;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;

/**
 * Created by Rene Argento on 15/04/17.
 */
public class Exercise26_SimplePolygon {

    public static void main(String[] args) {
        new Exercise26_SimplePolygon().drawSimplePolygon();
    }

    private void drawSimplePolygon() {

        Exercise25_PointsInThePlane.Point2D point2D1 = new Exercise25_PointsInThePlane.Point2D(70, 55);
        Exercise25_PointsInThePlane.Point2D point2D2 = new Exercise25_PointsInThePlane.Point2D(50, 20);
        Exercise25_PointsInThePlane.Point2D point2D3 = new Exercise25_PointsInThePlane.Point2D(20, 90);
        Exercise25_PointsInThePlane.Point2D point2D4 = new Exercise25_PointsInThePlane.Point2D(90, 75);
        Exercise25_PointsInThePlane.Point2D point2D5 = new Exercise25_PointsInThePlane.Point2D(40, 60);
        Exercise25_PointsInThePlane.Point2D point2D6 = new Exercise25_PointsInThePlane.Point2D(30, 40);

        Exercise25_PointsInThePlane.Point2D[] point2Ds = {point2D1, point2D2, point2D3, point2D4, point2D5, point2D6};

        Arrays.sort(point2Ds);

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 100);
        StdDraw.setPenRadius(0.005);
        StdDraw.enableDoubleBuffering();

        for(int i = 0; i < point2Ds.length; i++) {
            point2Ds[i].draw();
        }

        //Draw the first point in red
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.02);
        point2Ds[0].draw();

        Arrays.sort(point2Ds, point2Ds[0].polarAngleFromThisPointComparator);

        //Draw line segments from the first point to each other point, one at a time, in polar order
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);

        for(int i = 1; i < point2Ds.length; i++) {
            StdDraw.line(point2Ds[i - 1].x(), point2Ds[i - 1].y(), point2Ds[i].x(), point2Ds[i].y());
            StdDraw.show();
            StdDraw.pause(100);
        }

        //Draw last line and finish the polygon
        StdDraw.line(point2Ds[point2Ds.length - 1].x(), point2Ds[point2Ds.length - 1].y(), point2Ds[0].x(), point2Ds[0].y());
        StdDraw.show();
    }

}
