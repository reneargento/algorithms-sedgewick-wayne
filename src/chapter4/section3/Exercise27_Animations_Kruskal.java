package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

import java.awt.*;

/**
 * Created by Rene Argento on 11/11/17.
 */
public class Exercise27_Animations_Kruskal {

    private class Coordinate {
        double xCoordinate;
        double yCoordinate;

        Coordinate(double xCoordinate, double yCoordinate) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }
    }

    public class KruskalMSTAnimations {

        private Queue<Edge> minimumSpanningTree;
        private double weight;

        private double radiusOfCircleAroundVertex;

        public KruskalMSTAnimations(EdgeWeightedGraph edgeWeightedGraph, double xScaleLow, double xScaleHigh,
                                    double yScaleLow, double yScaleHigh, double radiusOfCircleAroundVertex) {
            minimumSpanningTree = new Queue<>();
            PriorityQueueResize<Edge> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

            this.radiusOfCircleAroundVertex = radiusOfCircleAroundVertex;

            Coordinate[] randomCoordinates = getRandomCoordinates(edgeWeightedGraph);
            initCanvas(xScaleLow, xScaleHigh, yScaleLow, yScaleHigh);

            drawVertices(randomCoordinates, -1, unionFind);
            drawInitialEdges(edgeWeightedGraph, randomCoordinates);

            for(Edge edge : edgeWeightedGraph.edges()) {
                priorityQueue.insert(edge);
            }

            while (!priorityQueue.isEmpty() && minimumSpanningTree.size() < edgeWeightedGraph.vertices() - 1) {
                Edge edge = priorityQueue.deleteTop(); // Get lowest-weight edge from priority queue
                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                // Ignore ineligible edges
                if (unionFind.connected(vertex1, vertex2)) {
                    continue;
                }

                drawEdgeInMST(vertex1, vertex2, randomCoordinates);
                drawVertices(randomCoordinates, vertex1, unionFind);

                unionFind.union(vertex1, vertex2);
                minimumSpanningTree.enqueue(edge); // Add edge to the minimum spanning tree

                weight += edge.weight();
            }

            // Re-draw vertices to clean up the radius around their labels
            drawVertices(randomCoordinates, -1, unionFind);
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }

        private void initCanvas(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh) {
            // Set canvas size
//            StdDraw.setCanvasSize(500, 400); // Use this dimension for tinyEWG.txt
            StdDraw.setCanvasSize(1000, 1000);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);
        }

        private Coordinate[] getRandomCoordinates(EdgeWeightedGraph edgeWeightedGraph) {
            Coordinate[] vertexCoordinates = new Coordinate[edgeWeightedGraph.vertices()];

            for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                // tinyEWG coordinates
//                double randomXCoordinate = StdRandom.uniform();
//                double randomYCoordinate = StdRandom.uniform();

                double randomXCoordinate = StdRandom.uniform(1000);
                double randomYCoordinate = StdRandom.uniform(1000);

                vertexCoordinates[vertex] = new Coordinate(randomXCoordinate, randomYCoordinate);
            }

            return vertexCoordinates;
        }

        private void drawVertices(Coordinate[] coordinates, int cutSet1Vertex, UnionFind unionFind) {
            int cutSet1Id = -1;

            if (cutSet1Vertex != -1) {
                cutSet1Id = unionFind.find(cutSet1Vertex);
            }

            StdDraw.setPenRadius(0.002D);

            for(int vertexId = 0; vertexId < coordinates.length; vertexId++) {

                if (unionFind.find(vertexId) == cutSet1Id) {
                    StdDraw.setPenColor(Color.LIGHT_GRAY);
                } else {
                    StdDraw.setPenColor(Color.WHITE);
                }
                StdDraw.filledCircle(coordinates[vertexId].xCoordinate, coordinates[vertexId].yCoordinate,
                        radiusOfCircleAroundVertex);

                StdDraw.setPenColor(Color.BLACK);
                StdDraw.circle(coordinates[vertexId].xCoordinate, coordinates[vertexId].yCoordinate,
                        radiusOfCircleAroundVertex);

                StdDraw.setPenColor(Color.BLACK);
                StdDraw.text(coordinates[vertexId].xCoordinate, coordinates[vertexId].yCoordinate,
                        String.valueOf(vertexId));
            }
        }

        private void drawInitialEdges(EdgeWeightedGraph edgeWeightedGraph, Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.003D);
            StdDraw.setPenColor(Color.LIGHT_GRAY);

            for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                    int otherVertex = edge.other(vertex);

                    if (vertex > otherVertex) {
                        StdDraw.line(coordinates[vertex].xCoordinate, coordinates[vertex].yCoordinate,
                                coordinates[otherVertex].xCoordinate, coordinates[otherVertex].yCoordinate);
                    }
                }
            }

            sleep();
        }

        private void drawEdgeInMST(int vertex1, int vertex2, Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.005D);
            StdDraw.setPenColor(Color.RED);

            StdDraw.line(coordinates[vertex1].xCoordinate, coordinates[vertex1].yCoordinate,
                    coordinates[vertex2].xCoordinate, coordinates[vertex2].yCoordinate);

            sleep();

            StdDraw.setPenColor(Color.BLACK);
            StdDraw.line(coordinates[vertex1].xCoordinate, coordinates[vertex1].yCoordinate,
                    coordinates[vertex2].xCoordinate, coordinates[vertex2].yCoordinate);

            sleep();
        }

        private void sleep() {
            try {
                Thread.sleep(100);
//                Thread.sleep(1000); // Use this timer for tinyEWG.txt
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
//        String filePath = Constants.FILES_PATH + Constants.TINY_EWG_FILE;
        String filePath = Constants.FILES_PATH + Constants.MEDIUM_EWG_FILE;
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(new In(filePath));

//        new Exercise27_Animations_Kruskal().new KruskalMSTAnimations(edgeWeightedGraph, -0.1, 1.1,
//                        -0.1, 1.1, 0.04); // Use these dimensions for tinyEWG.txt
        new Exercise27_Animations_Kruskal().new KruskalMSTAnimations(edgeWeightedGraph, -1, 1001,
                -1, 1001, 15);
    }

}
