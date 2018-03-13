package chapter2.section4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Stack;

/**
 * Created by Rene Argento on 25/03/17.
 */
public class Exercise28_SelectionFilter {

    private class Point implements Comparable<Point>{
        double x, y, z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int compareTo(Point other) {
            //Distance to 0,0 (origin)
            double euclideanDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
            double otherPointEuclideanDistance = Math.sqrt(Math.pow(other.x, 2) + Math.pow(other.y, 2) +
                    Math.pow(other.z, 2));

            if (euclideanDistance < otherPointEuclideanDistance) {
                return -1;
            } else if (euclideanDistance > otherPointEuclideanDistance) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "x = " + x + " y = " + y + " z = " + z;
        }
    }

    // Parameter example: 10000
    public static void main(String[] args) {

        int m = Integer.parseInt(args[0]);

        PriorityQueue<Point> priorityQueue = new PriorityQueue<>(m + 1, PriorityQueue.Orientation.MAX);

        while (StdIn.hasNextLine()) {
            String pointsLine = StdIn.readLine();

            String[] pointsString = pointsLine.split(" ");
            double x = Double.parseDouble(pointsString[0]);
            double y = Double.parseDouble(pointsString[1]);
            double z = Double.parseDouble(pointsString[2]);

            Point point = new Exercise28_SelectionFilter().new Point(x, y, z);
            priorityQueue.insert(point);

            if (priorityQueue.size() > m) {
                priorityQueue.deleteTop();
            }
        }

        Stack<Point> pointsStack = reversePointsOrder(priorityQueue);
        printPoints(pointsStack);

        //Estimate running time
        int initialArraySize = 100000; //10^5
        int numberOfExperiments = 3; //10^5, 10^6, 10^7
        doExperimentToEstimateRunningTime(initialArraySize, numberOfExperiments);
    }

    private static Stack<Point> reversePointsOrder(PriorityQueue<Point> priorityQueue) {
        Stack<Point> stack = new Stack<>();

        while (priorityQueue.size() > 0) {
            stack.push(priorityQueue.deleteTop());
        }

        return stack;
    }

    private static void printPoints(Stack<Point> pointsStack) {
        while (pointsStack.size() > 0) {
            StdOut.println(pointsStack.pop());
        }
    }

    private static void doExperimentToEstimateRunningTime(int arraySize, int numberOfExperiments) {

        int m = 10000; //10^4

        for(int i = 0; i < numberOfExperiments; i++) {

            PriorityQueue<Point> priorityQueue = new PriorityQueue<>(m + 1, PriorityQueue.Orientation.MAX);

            Point[] pointArray = generateRandomPointsArray(arraySize);

            Stopwatch timer = new Stopwatch();

            for(Point point : pointArray) {
                priorityQueue.insert(point);

                if (priorityQueue.size() > m) {
                    priorityQueue.deleteTop();
                }
            }

            Stack<Point> pointsStack = reversePointsOrder(priorityQueue);
            printPoints(pointsStack);

            double runningTime = timer.elapsedTime();
            StdOut.println("Running time for N = " + arraySize + " and M = " + m + ": " + runningTime);

            arraySize *= 10;
        }
    }

    private static Point[] generateRandomPointsArray(int length) {
        Point[] array = new Point[length];

        for(int i = 0; i < length; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            double z = StdRandom.uniform();

            Point point = new Exercise28_SelectionFilter().new Point(x, y, z);
            array[i] = point;
        }

        return array;
    }

}
