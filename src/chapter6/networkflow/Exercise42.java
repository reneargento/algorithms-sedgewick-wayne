package chapter6.networkflow;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.MathUtil;

/**
 * Created by Rene Argento on 17/10/18.
 */
public class Exercise42 {

    private class Point {
        private double x;
        private double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public FlowNetwork generateFlowNetworkOfNearbyPoints(int points, double distance) {
        Point[] pointCoordinates = new Point[points];

        // Generate points
        for (int i = 0; i < points; i++) {
            double randomX = StdRandom.uniform(0, 100.00);
            double randomY = StdRandom.uniform(0, 100.00);

            pointCoordinates[i] = new Point(randomX, randomY);
        }

        // Build flow network
        FlowNetwork flowNetwork = new FlowNetwork(points);
        Exercise41.RandomCapacity randomCapacity = new Exercise41().new RandomUniformCapacity();

        for (int point1 = 0; point1 < points; point1++) {
            for (int point2 = point1 + 1; point2 < points; point2++) {
                if (MathUtil.distanceBetweenPoints(pointCoordinates[point1].x, pointCoordinates[point1].y,
                        pointCoordinates[point2].x, pointCoordinates[point2].y) <= distance) {
                    double capacity = randomCapacity.getRandomCapacity();

                    flowNetwork.addEdge(new FlowEdge(point1, point2, capacity));
                    flowNetwork.addEdge(new FlowEdge(point2, point1, capacity));
                }
            }
        }

        return flowNetwork;
    }

    // Parameters example: 100 5
    public static void main(String[] args) {
        int points = Integer.parseInt(args[0]);
        double distance = Double.parseDouble(args[1]);

        FlowNetwork flowNetwork = new Exercise42().generateFlowNetworkOfNearbyPoints(points, distance);
        StdOut.println("Flow network:\n");
        StdOut.print(flowNetwork);
    }

}
