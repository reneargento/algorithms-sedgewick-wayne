package chapter4.section1;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

/**
 * Created by rene on 07/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise37_EuclideanGraphs {

    public class EuclideanGraph {

        public class Vertex {
            int id;
            double xCoordinate;
            double yCoordinate;

            Vertex(int id, double xCoordinate, double yCoordinate) {
                this.id = id;
                this.xCoordinate = xCoordinate;
                this.yCoordinate = yCoordinate;
            }
        }

        private final int vertices;
        private int edges;
        private Vertex[] allVertices;
        private Bag<Vertex>[] adjacent;

        public EuclideanGraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            allVertices = new Vertex[vertices];
            adjacent = (Bag<Vertex>[]) new Bag[vertices];

            for(int i = 0; i < vertices; i++) {
                adjacent[i] = new Bag<>();
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

        public void addEdge(Vertex vertex1, Vertex vertex2) {
            if(allVertices[vertex1.id] == null) {
                allVertices[vertex1.id] = vertex1;
            }
            if(allVertices[vertex2.id] == null) {
                allVertices[vertex2.id] = vertex2;
            }

            adjacent[vertex1.id].add(vertex2);
            adjacent[vertex2.id].add(vertex1);
            edges++;
        }

        public void show(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh, double radiusToCleanAroundVertex) {
            // Set canvas size
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);

            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLACK);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                for(Vertex neighbor : adjacent(vertexId)) {
                    StdDraw.line(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            neighbor.xCoordinate, neighbor.yCoordinate);
                }
            }

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                if(allVertices[vertexId] != null) {

                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.filledCircle(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            radiusToCleanAroundVertex);

                    StdDraw.setPenColor(Color.BLUE);
                    StdDraw.text(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            String.valueOf(allVertices[vertexId].id));
                }
            }
        }

        public int degree(int vertex) {
            return adjacent[vertex].size();
        }

        public Iterable<Vertex> adjacent(int vertexId) {
            return adjacent[vertexId];
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                for(Vertex neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor.id).append(" ");
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

        euclideanGraph.addEdge(vertex0, vertex1);
        euclideanGraph.addEdge(vertex2, vertex1);
        euclideanGraph.addEdge(vertex0, vertex2);
        euclideanGraph.addEdge(vertex3, vertex6);
        euclideanGraph.addEdge(vertex4, vertex6);
        euclideanGraph.addEdge(vertex3, vertex4);
        euclideanGraph.addEdge(vertex1, vertex5);
        euclideanGraph.addEdge(vertex5, vertex6);

        euclideanGraph.show(0, 15, 0, 20, 0.5);
        StdOut.println(euclideanGraph);
    }

}
