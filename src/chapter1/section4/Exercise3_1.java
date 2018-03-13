package chapter1.section4;

import edu.princeton.cs.algs4.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 9/27/16.
 */
//Both standard and log-log plots
public class Exercise3_1 {

    private static List<Point2D> standardPlotPoints;
    private static List<Point2D> logLogPlotPoints;
    private static int currentXMaxScale;
    private static int currentYMaxScale;

    public static void main(String[] args) {
        currentXMaxScale = 50;
        currentYMaxScale = 5;
        initializeCanvas(currentXMaxScale, currentYMaxScale);

        standardPlotPoints = new ArrayList<>(5);
        logLogPlotPoints = new ArrayList<>(5);

        // Print table of running times
        for(int n = 250; true; n += n) {
            //Print time for problem size n
            double time = timeTrial(n);

            rescaleIfNecessary(n, time);

            drawPointAndLine(n, time, standardPlotPoints, true, false);
            drawPointAndLine(Math.log(n), Math.log(time), logLogPlotPoints, false, false);

            StdOut.printf("%7d %5.1f\n", n, time);
        }
    }

    public static double timeTrial(int n) {
        // Time ThreeSum.count() for N random 6-digit ints.
        int MAX = 1000000;
        int[] array = new int[n];

        for(int i = 0; i < n; i++) {
            array[i] = StdRandom.uniform(-MAX, MAX);
        }
        Stopwatch timer = new Stopwatch();
        int count = ThreeSum.count(array);
        return timer.elapsedTime();
    }

    private static void initializeCanvas(int currentXMaxScale, int currentYMaxScale) {
        StdDraw.setCanvasSize(300, 300);
        StdDraw.setXscale(0, currentXMaxScale);
        StdDraw.setYscale(0, currentYMaxScale);
        StdDraw.setPenRadius(.005);
    }

    private static void drawPointAndLine(double x, double y, List<Point2D> pointList, boolean isStandard, boolean isRescale) {

        if (y == Double.NEGATIVE_INFINITY) {
            y = 0;
        }

        Point2D point = new Point2D(x, y);
        if (!isRescale) {
            pointList.add(point);
        }

        if (isStandard) {
            StdDraw.setPenColor(Color.BLUE);
        } else {
            StdDraw.setPenColor(Color.GREEN);
        }

        if (pointList.size() > 1) {
            Point2D previousPoint = pointList.get(pointList.size() - 2);
            StdDraw.line(previousPoint.x(), previousPoint.y(), point.x(), point.y());
        }

        StdDraw.point(x, y);
    }

    private static void rescaleIfNecessary(int n, double time) {
        if (n > currentXMaxScale || time > currentYMaxScale) {
            if (n > currentXMaxScale) {
                if (currentXMaxScale * 2 > n) {
                    currentXMaxScale = currentXMaxScale * 2;
                } else {
                    currentXMaxScale = (int) (1.5 * n);
                }
            }
            if (time > currentYMaxScale) {
                currentYMaxScale = currentYMaxScale * 2;
            }

            rescaleCanvas();
        }
    }

    private static void rescaleCanvas() {
        StdDraw.clear();

        StdDraw.setXscale(0, currentXMaxScale);
        StdDraw.setYscale(0, currentYMaxScale);

        //Redraw plot points and lines

        for(int i = 0; i < standardPlotPoints.size(); i++) {
            drawPointAndLine(standardPlotPoints.get(i).x(), standardPlotPoints.get(i).y(), standardPlotPoints, true, true);
        }

        for(int i = 0; i < logLogPlotPoints.size(); i++) {
            drawPointAndLine(logLogPlotPoints.get(i).x(), logLogPlotPoints.get(i).y(), logLogPlotPoints, false, true);
        }
    }
}
