package chapter4.section2;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import util.DrawUtilities;
import util.DrawUtilities.Coordinate;

import java.awt.*;

/**
 * Created by Rene Argento on 26/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise38_EuclideanDigraphs {

    public class EuclideanDigraph {

        public class Vertex {
            protected int id;
            private String name;
            protected Coordinate coordinates;

            Vertex(int id, double xCoordinate, double yCoordinate) {
                this(id, String.valueOf(id), xCoordinate, yCoordinate);
            }

            Vertex(int id, String name, double xCoordinate, double yCoordinate) {
                this.id = id;
                this.name = name;
                coordinates = new DrawUtilities().new Coordinate(xCoordinate, yCoordinate);
            }

            public void updateName(String name) {
                this.name = name;
            }
        }

        private final int vertices;
        private int edges;
        private Vertex[] allVertices;
        private Bag<Integer>[] adjacent;

        private int[] indegrees;
        private int[] outdegrees;

        public EuclideanDigraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            allVertices = new Vertex[vertices];
            adjacent = (Bag<Integer>[]) new Bag[vertices];

            indegrees = new int[vertices];
            outdegrees = new int[vertices];

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

            edges++;
            outdegrees[vertexId1]++;
            indegrees[vertexId2]++;
        }

        public void show(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh,
                         double radiusOfCircleAroundVertex, double padding, double arrowLength) {
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);

            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLUE);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                if (allVertices[vertexId] != null) {
                    double xCoordinate = allVertices[vertexId].coordinates.getXCoordinate();
                    double yCoordinate = allVertices[vertexId].coordinates.getYCoordinate();

                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.filledCircle(xCoordinate, yCoordinate, radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.circle(xCoordinate, yCoordinate, radiusOfCircleAroundVertex);

                    StdDraw.setPenColor(Color.BLUE);
                    StdDraw.text(xCoordinate, yCoordinate, allVertices[vertexId].name);
                }
            }

            StdDraw.setPenColor(Color.BLACK);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                for(Integer neighbor : adjacent(vertexId)) {
                    Vertex neighborVertex = allVertices[neighbor];

                    DrawUtilities.drawArrow(allVertices[vertexId].coordinates, neighborVertex.coordinates,
                            padding, arrowLength);
                }
            }
        }

        public Iterable<Integer> adjacent(int vertexId) {
            return adjacent[vertexId];
        }

        public int indegree(int vertex) {
            return indegrees[vertex];
        }

        public int outdegree(int vertex) {
            return outdegrees[vertex];
        }

        public EuclideanDigraph reverse() {
            EuclideanDigraph reverse = new EuclideanDigraph(vertices);

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(Integer neighbor : adjacent(vertex)) {
                    reverse.addEdge(neighbor, vertex);
                }
            }

            return reverse;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                for(Integer neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise38_EuclideanDigraphs euclideanDigraphs = new Exercise38_EuclideanDigraphs();

        EuclideanDigraph euclideanDigraph = euclideanDigraphs.new EuclideanDigraph(7);

        EuclideanDigraph.Vertex vertex0 = euclideanDigraph.new Vertex(0, 6.1, 1.3);
        EuclideanDigraph.Vertex vertex1 = euclideanDigraph.new Vertex(1, 7.2, 2.5);
        EuclideanDigraph.Vertex vertex2 = euclideanDigraph.new Vertex(2, 8.4, 1.3);
        EuclideanDigraph.Vertex vertex3 = euclideanDigraph.new Vertex(3, 8.4, 15.3);
        EuclideanDigraph.Vertex vertex4 = euclideanDigraph.new Vertex(4, 6.1, 15.3);
        EuclideanDigraph.Vertex vertex5 = euclideanDigraph.new Vertex(5, 7.2, 5.2);
        EuclideanDigraph.Vertex vertex6 = euclideanDigraph.new Vertex(6, 7.2, 8.4);

        euclideanDigraph.addVertex(vertex0);
        euclideanDigraph.addVertex(vertex1);
        euclideanDigraph.addVertex(vertex2);
        euclideanDigraph.addVertex(vertex3);
        euclideanDigraph.addVertex(vertex4);
        euclideanDigraph.addVertex(vertex5);
        euclideanDigraph.addVertex(vertex6);

        euclideanDigraph.addEdge(0, 1);
        euclideanDigraph.addEdge(2, 1);
        euclideanDigraph.addEdge(0, 2);
        euclideanDigraph.addEdge(3, 6);
        euclideanDigraph.addEdge(4, 6);
        euclideanDigraph.addEdge(3, 4);
        euclideanDigraph.addEdge(1, 5);
        euclideanDigraph.addEdge(5, 6);

        euclideanDigraph.show(0, 15, 0, 20, 0.5,
                0.08, 0.4);
        StdOut.println(euclideanDigraph);
    }

}
