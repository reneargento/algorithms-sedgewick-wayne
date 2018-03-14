package chapter4.section3;

import chapter1.section3.Queue;
import chapter2.section4.IndexMinPriorityQueue;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

import java.awt.*;

/**
 * Created by Rene Argento on 10/11/17.
 */
public class Exercise27_Animations_Prim {

    private class Coordinate {
        double xCoordinate;
        double yCoordinate;

        Coordinate(double xCoordinate, double yCoordinate) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }
    }

    public class PrimMSTAnimations {

        private Edge[] edgeTo; // shortest edge from tree vertex
        private double[] distTo; // distTo[vertex] = edgeTo[vertex].weight()
        private boolean[] marked; // true if vertex is on the minimum spanning tree
        private IndexMinPriorityQueue<Double> priorityQueue; // eligible crossing edges

        private double weight;
        private double radiusOfCircleAroundVertex;
        private HashSet<Integer> verticesInMST;

        public PrimMSTAnimations(EdgeWeightedGraph edgeWeightedGraph, double xScaleLow, double xScaleHigh,
                                 double yScaleLow, double yScaleHigh, double radiusOfCircleAroundVertex) {
            edgeTo = new Edge[edgeWeightedGraph.vertices()];
            distTo = new double[edgeWeightedGraph.vertices()];
            marked = new boolean[edgeWeightedGraph.vertices()];
            this.radiusOfCircleAroundVertex = radiusOfCircleAroundVertex;
            verticesInMST = new HashSet<>();

            Coordinate[] randomCoordinates = getRandomCoordinates(edgeWeightedGraph);
            initCanvas(xScaleLow, xScaleHigh, yScaleLow, yScaleHigh);

            drawInitialVertices(randomCoordinates);
            drawInitialEdges(edgeWeightedGraph, randomCoordinates);

            for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedGraph.vertices());

            // Initialize priority queue with 0, weight 0
            distTo[0] = 0;
            priorityQueue.insert(0, 0.0);

            while (!priorityQueue.isEmpty()) {
                // Add closest vertex to the minimum spanning tree
                int vertexToVisit = priorityQueue.deleteMin();

                visit(edgeWeightedGraph, vertexToVisit, randomCoordinates);
                verticesInMST.add(vertexToVisit);

                // Draw current graph and MST
                if (!priorityQueue.isEmpty()) {
                    int nextVertexInMST = priorityQueue.minIndex();
                    int vertexConnectedToNextVertexInMST = edgeTo[nextVertexInMST].other(nextVertexInMST);

                    drawEdgeInMST(vertexConnectedToNextVertexInMST, nextVertexInMST, randomCoordinates);
                    drawCandidateEdgesToMST(edgeTo, randomCoordinates, nextVertexInMST);
                }
            }

            // Re-draw vertices to clean up the radius around their labels
            drawVerticesInMST(randomCoordinates);
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

        private void drawInitialVertices(Coordinate[] coordinates) {
            for(int vertexId = 0; vertexId < coordinates.length; vertexId++) {

                StdDraw.setPenColor(Color.LIGHT_GRAY);
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

        private void drawVerticesInMST(Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.002D);

            for(int vertexId : verticesInMST.keys()) {
                StdDraw.setPenColor(Color.WHITE);
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

        private void visit(EdgeWeightedGraph edgeWeightedGraph, int vertex, Coordinate[] coordinates) {
            // Add vertex to the minimum spanning tree; update data structures
            marked[vertex] = true;

            for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                int otherVertex = edge.other(vertex);
                if (marked[otherVertex]) {
                    // Only draw an ineligible edge if this edge is not part of the MST
                    if (edgeTo[vertex].other(vertex) != otherVertex) {
                        drawIneligibleEdge(vertex, otherVertex, coordinates);
                    }

                    continue; // vertex-otherVertex is ineligible
                }

                if (edge.weight() < distTo[otherVertex]) {
                    // If there is another edge candidate for the MST connected to otherVertex, draw it as ineligible
                    if (edgeTo[otherVertex] != null) {
                        int previousBestVertexConnectedToOtherVertex = edgeTo[otherVertex].other(otherVertex);
                        drawIneligibleEdge(previousBestVertexConnectedToOtherVertex, otherVertex, coordinates);
                    }

                    // Edge edge is the new best connection from the minimum spanning tree to otherVertex
                    if (distTo[otherVertex] != Double.POSITIVE_INFINITY) {
                        weight -= distTo[otherVertex];
                    }
                    weight += edge.weight();

                    edgeTo[otherVertex] = edge;
                    distTo[otherVertex] = edge.weight();

                    if (priorityQueue.contains(otherVertex)) {
                        priorityQueue.decreaseKey(otherVertex, distTo[otherVertex]);
                    } else {
                        priorityQueue.insert(otherVertex, distTo[otherVertex]);
                    }
                } else {
                    drawIneligibleEdge(vertex, otherVertex, coordinates);
                }
            }
        }

        public Iterable<Edge> edges() {
            Queue<Edge> minimumSpanningTree = new Queue<>();

            for(int vertex = 1; vertex < edgeTo.length; vertex++) {
                minimumSpanningTree.enqueue(edgeTo[vertex]);
            }

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

        private void drawInitialEdges(EdgeWeightedGraph edgeWeightedGraph, Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLACK);

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

        private void drawCandidateEdgesToMST(Edge[] edgeTo, Coordinate[] coordinates, int nextVertexInMST) {
            for(Edge edge : edgeTo) {
                if (edge == null) {
                    continue;
                }

                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                if (vertex1 == nextVertexInMST || vertex2 == nextVertexInMST) {
                    //Already colored and in MST
                    continue;
                }

                if (!marked[vertex1] || !marked[vertex2]) {
                    StdDraw.setPenRadius(0.002D);
                    StdDraw.setPenColor(Color.RED);

                    StdDraw.line(coordinates[vertex2].xCoordinate, coordinates[vertex2].yCoordinate,
                            coordinates[vertex1].xCoordinate, coordinates[vertex1].yCoordinate);
                }
            }

            drawVerticesInMST(coordinates);

            sleep();
        }

        private void drawEdgeInMST(int vertex1, int vertex2, Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.007D);
            StdDraw.setPenColor(Color.RED);

            StdDraw.line(coordinates[vertex1].xCoordinate, coordinates[vertex1].yCoordinate,
                    coordinates[vertex2].xCoordinate, coordinates[vertex2].yCoordinate);

            sleep();

            StdDraw.setPenColor(Color.BLACK);
            StdDraw.line(coordinates[vertex1].xCoordinate, coordinates[vertex1].yCoordinate,
                    coordinates[vertex2].xCoordinate, coordinates[vertex2].yCoordinate);
            drawVerticesInMST(coordinates);

            sleep();
        }

        private void drawIneligibleEdge(int vertex1, int vertex2, Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.005D);
            StdDraw.setPenColor(Color.LIGHT_GRAY);

            StdDraw.line(coordinates[vertex1].xCoordinate, coordinates[vertex1].yCoordinate,
                    coordinates[vertex2].xCoordinate, coordinates[vertex2].yCoordinate);
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

//        new Exercise27_Animations_Prim().new PrimMSTAnimations(edgeWeightedGraph, -0.1, 1.1,
//                        -0.1, 1.1, 0.04); // Use these dimensions for tinyEWG.txt
        new Exercise27_Animations_Prim().new PrimMSTAnimations(edgeWeightedGraph, -1, 1001,
                -1, 1001, 15);
    }

}
