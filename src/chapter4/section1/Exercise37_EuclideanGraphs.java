package chapter4.section1;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

/**
 * Created by Rene Argento on 07/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise37_EuclideanGraphs {

    public class EuclideanGraph {

        public class Vertex {
            protected int id;
            protected String name;
            protected double xCoordinate;
            protected double yCoordinate;

            Vertex(int id, double xCoordinate, double yCoordinate) {
                this(id, String.valueOf(id), xCoordinate, yCoordinate);
            }

            Vertex(int id, String name, double xCoordinate, double yCoordinate) {
                this.id = id;
                this.name = name;
                this.xCoordinate = xCoordinate;
                this.yCoordinate = yCoordinate;
            }

            public void updateName(String name) {
                this.name = name;
            }
        }

        private final int vertices;
        private int edges;
        private Vertex[] allVertices;
        private Bag<Integer>[] adjacent;

        public EuclideanGraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            allVertices = new Vertex[vertices];
            adjacent = (Bag<Integer>[]) new Bag[vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new Bag<>();
            }
        }

        public int vertices() {
            return vertices;
        }

        public int edges() {
            return edges;
        }

        public void addVertex(Vertex vertex) {
            allVertices[vertex.id] = vertex;
        }

        public void addEdge(int vertexId1, int vertexId2) {
            if (allVertices[vertexId1] == null || allVertices[vertexId2] == null) {
                throw new IllegalArgumentException("Vertex id not found");
            }

            adjacent[vertexId1].add(vertexId2);
            adjacent[vertexId2].add(vertexId1);
            edges++;
        }

        public void show(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh, double radiusOfCircleAroundVertex) {
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);

            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLACK);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                for(int neighbor : adjacent(vertexId)) {
                    Vertex neighborVertex = allVertices[neighbor];

                    if (neighbor >= vertexId) {
                        StdDraw.line(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                                neighborVertex.xCoordinate, neighborVertex.yCoordinate);
                    }
                }
            }

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                if (allVertices[vertexId] != null) {

                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.filledCircle(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.circle(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLUE);
                    StdDraw.text(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            allVertices[vertexId].name);
                }
            }
        }

        public int degree(int vertex) {
            return adjacent[vertex].size();
        }

        public Iterable<Integer> adjacent(int vertexId) {
            return adjacent[vertexId];
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                for(int neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise37_EuclideanGraphs euclideanGraphs = new Exercise37_EuclideanGraphs();

        EuclideanGraph euclideanGraph = euclideanGraphs.new EuclideanGraph(7);

        EuclideanGraph.Vertex vertex0 = euclideanGraph.new Vertex(0, 6.1, 1.3);
        EuclideanGraph.Vertex vertex1 = euclideanGraph.new Vertex(1, 7.2, 2.5);
        EuclideanGraph.Vertex vertex2 = euclideanGraph.new Vertex(2, 8.4, 1.3);
        EuclideanGraph.Vertex vertex3 = euclideanGraph.new Vertex(3, 8.4, 15.3);
        EuclideanGraph.Vertex vertex4 = euclideanGraph.new Vertex(4, 6.1, 15.3);
        EuclideanGraph.Vertex vertex5 = euclideanGraph.new Vertex(5, 7.2, 5.2);
        EuclideanGraph.Vertex vertex6 = euclideanGraph.new Vertex(6, 7.2, 8.4);

        euclideanGraph.addVertex(vertex0);
        euclideanGraph.addVertex(vertex1);
        euclideanGraph.addVertex(vertex2);
        euclideanGraph.addVertex(vertex3);
        euclideanGraph.addVertex(vertex4);
        euclideanGraph.addVertex(vertex5);
        euclideanGraph.addVertex(vertex6);

        euclideanGraph.addEdge(0, 1);
        euclideanGraph.addEdge(2, 1);
        euclideanGraph.addEdge(0, 2);
        euclideanGraph.addEdge(3, 6);
        euclideanGraph.addEdge(4, 6);
        euclideanGraph.addEdge(3, 4);
        euclideanGraph.addEdge(1, 5);
        euclideanGraph.addEdge(5, 6);

        euclideanGraph.show(0, 15, -2, 18, 0.5);
        StdOut.println(euclideanGraph);
    }

}
