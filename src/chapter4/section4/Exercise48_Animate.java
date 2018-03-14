package chapter4.section4;

import chapter1.section3.Stack;
import chapter2.section4.IndexMinPriorityQueue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;
import util.DrawUtilities;
import util.DrawUtilities.Coordinate;

import java.awt.*;

/**
 * Created by Rene Argento on 26/12/17.
 */
public class Exercise48_Animate {

    public class DijkstraSPAnimations {

        private DirectedEdge[] edgeTo;                      // last edge on path to vertex
        private double[] distTo;                            // length of path to vertex
        private IndexMinPriorityQueue<Double> priorityQueue;

        private boolean[] relaxed;                          // vertices already relaxed

        private double radiusOfCircleAroundVertex;
        private double arrowLength;
        private double padding;

        // O(V^2) due to animation drawing
        public DijkstraSPAnimations(EdgeWeightedDigraph edgeWeightedDigraph, int source, double xScaleLow,
                                    double xScaleHigh, double yScaleLow, double yScaleHigh,
                                    double radiusOfCircleAroundVertex, double arrowLength, double padding) {
            edgeTo = new DirectedEdge[edgeWeightedDigraph.vertices()];
            distTo = new double[edgeWeightedDigraph.vertices()];
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedDigraph.vertices());

            relaxed = new boolean[edgeWeightedDigraph.vertices()];

            this.radiusOfCircleAroundVertex = radiusOfCircleAroundVertex;
            this.arrowLength = arrowLength;
            this.padding = padding;

            Coordinate[] randomCoordinates = getRandomCoordinates(edgeWeightedDigraph);
            initCanvas(xScaleLow, xScaleHigh, yScaleLow, yScaleHigh);

            drawInitialVertices(randomCoordinates);
            drawInitialEdges(edgeWeightedDigraph, randomCoordinates);

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            distTo[source] = 0;
            priorityQueue.insert(source, 0.0);

            while (!priorityQueue.isEmpty()) {
                int nextVertexToRelax = priorityQueue.deleteMin();

                drawVerticesInShortestPath(randomCoordinates);

                if (edgeTo[nextVertexToRelax] != null) {
                    drawEdgeInShortestPath(edgeTo[nextVertexToRelax].from(), nextVertexToRelax, randomCoordinates);
                }

                relax(edgeWeightedDigraph, nextVertexToRelax, randomCoordinates);
            }

            drawVerticesInShortestPath(randomCoordinates);
            drawAllEdgesInShortestPath(randomCoordinates);
        }

        private void relax(EdgeWeightedDigraph edgeWeightedDigraph, int vertex, Coordinate[] randomCoordinates) {
            relaxed[vertex] = true;

            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (distTo[neighbor] > distTo[vertex] + edge.weight()) {
                    distTo[neighbor] = distTo[vertex] + edge.weight();
                    edgeTo[neighbor] = edge;

                    if (priorityQueue.contains(neighbor)) {
                        priorityQueue.decreaseKey(neighbor, distTo[neighbor]);
                    } else {
                        priorityQueue.insert(neighbor, distTo[neighbor]);
                    }
                } else {
                    drawIneligibleEdge(vertex, neighbor, randomCoordinates);
                }
            }

            drawCandidateEdgesToShortestPath(randomCoordinates);
        }

        public double distTo(int vertex) {
            return distTo[vertex];
        }

        public DirectedEdge edgeTo(int vertex) {
            return edgeTo[vertex];
        }

        public boolean hasPathTo(int vertex) {
            return distTo[vertex] < Double.POSITIVE_INFINITY;
        }

        public Iterable<DirectedEdge> pathTo(int vertex) {
            if (!hasPathTo(vertex)) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeTo[vertex]; edge != null; edge = edgeTo[edge.from()]) {
                path.push(edge);
            }

            return path;
        }

        private void initCanvas(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh) {
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);
        }

        private Coordinate[] getRandomCoordinates(EdgeWeightedDigraph edgeWeightedDigraph) {
            Coordinate[] vertexCoordinates = new Coordinate[edgeWeightedDigraph.vertices()];
            DrawUtilities drawUtilities = new DrawUtilities();

            int valueToExpand = 5;

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                double randomXCoordinate = StdRandom.uniform() * valueToExpand;
                double randomYCoordinate = StdRandom.uniform() * valueToExpand;

                vertexCoordinates[vertex] = drawUtilities.new Coordinate(randomXCoordinate, randomYCoordinate);
            }

            return vertexCoordinates;
        }

        private void drawInitialVertices(Coordinate[] coordinates) {
            for(int vertex = 0; vertex < coordinates.length; vertex++) {

                StdDraw.setPenColor(Color.LIGHT_GRAY);
                StdDraw.filledCircle(coordinates[vertex].getXCoordinate(), coordinates[vertex].getYCoordinate(),
                        radiusOfCircleAroundVertex);

                StdDraw.setPenColor(Color.BLACK);
                StdDraw.circle(coordinates[vertex].getXCoordinate(), coordinates[vertex].getYCoordinate(),
                        radiusOfCircleAroundVertex);

                StdDraw.text(coordinates[vertex].getXCoordinate(), coordinates[vertex].getYCoordinate(),
                        String.valueOf(vertex));
            }
        }

        private void drawInitialEdges(EdgeWeightedDigraph edgeWeightedDigraph, Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLACK);

            for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
                for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                    int neighbor = edge.to();
                    DrawUtilities.drawArrow(coordinates[vertex], coordinates[neighbor], padding, arrowLength);
                }
            }
        }

        private void drawVerticesInShortestPath(Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.002D);

            for(int vertex = 0; vertex < coordinates.length; vertex++) {
                if (relaxed[vertex]) {
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.filledCircle(coordinates[vertex].getXCoordinate(), coordinates[vertex].getYCoordinate(),
                            radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.circle(coordinates[vertex].getXCoordinate(), coordinates[vertex].getYCoordinate(),
                            radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.text(coordinates[vertex].getXCoordinate(), coordinates[vertex].getYCoordinate(),
                            String.valueOf(vertex));
                }
            }
        }

        private void drawCandidateEdgesToShortestPath(Coordinate[] coordinates) {
            int nextVertexInShortestPath = -1;

            if (!priorityQueue.isEmpty()) {
                nextVertexInShortestPath = priorityQueue.minIndex();
            }

            for(DirectedEdge edge : edgeTo) {
                if (edge == null) {
                    continue;
                }

                int vertex1 = edge.from();
                int vertex2 = edge.to();

                // If both vertices are already relaxed, the edge is already in the shortest path
                if (relaxed[vertex2]) {
                    continue;
                }

                if (nextVertexInShortestPath != -1 && edge == edgeTo[nextVertexInShortestPath]) {
                    // Favorite candidate
                    StdDraw.setPenRadius(0.007D);
                } else {
                    StdDraw.setPenRadius(0.002D);
                }
                StdDraw.setPenColor(Color.RED);
                DrawUtilities.drawArrow(coordinates[vertex1], coordinates[vertex2], padding, arrowLength);
            }

            sleep();
        }

        private void drawEdgeInShortestPath(int vertex1, int vertex2, Coordinate[] coordinates) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.007D);

            DrawUtilities.drawArrow(coordinates[vertex1], coordinates[vertex2], padding, arrowLength);
            sleep();
        }

        private void drawAllEdgesInShortestPath(Coordinate[] coordinates) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.007D);

            for(int vertex = 0; vertex < coordinates.length; vertex++) {
                if (edgeTo[vertex] != null) {
                    DrawUtilities.drawArrow(coordinates[edgeTo[vertex].from()], coordinates[vertex], padding, arrowLength);
                }
            }
        }

        private void drawIneligibleEdge(int vertex1, int vertex2, Coordinate[] coordinates) {
            StdDraw.setPenRadius(0.005D);
            StdDraw.setPenColor(Color.LIGHT_GRAY);

            DrawUtilities.drawArrow(coordinates[vertex1], coordinates[vertex2], padding, arrowLength);
        }

        private void sleep() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // tinyEWD.txt file contents
    // 8
    // 15
    // 4 5 0.35
    // 5 4 0.35
    // 4 7 0.37
    // 5 7 0.28
    // 7 5 0.28
    // 5 1 0.32
    // 0 4 0.38
    // 0 2 0.26
    // 7 3 0.39
    // 1 3 0.29
    // 2 7 0.34
    // 6 2 0.40
    // 3 6 0.52
    // 6 0 0.58
    // 6 4 0.93

    public static void main(String[] args) {
        String filePath = Constants.FILES_PATH + Constants.TINY_EWD_FILE;
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(new In(filePath));

        new Exercise48_Animate().new DijkstraSPAnimations(edgeWeightedDigraph, 0,-0.2, 5.2,
                        -0.2, 5.2, 0.2, 0.08,0.05);
    }

}
