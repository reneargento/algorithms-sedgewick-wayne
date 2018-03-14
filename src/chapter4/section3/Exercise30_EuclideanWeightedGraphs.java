package chapter4.section3;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

/**
 * Created by Rene Argento on 11/11/17.
 */
@SuppressWarnings("unchecked")
public class Exercise30_EuclideanWeightedGraphs {

    public class EuclideanEdgeWeightedGraph implements EdgeWeightedGraphInterface {

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
        private Bag<Edge>[] adjacent;

        public EuclideanEdgeWeightedGraph(int vertices) {
            this.vertices = vertices;
            edges = 0;
            allVertices = new Vertex[vertices];
            adjacent = (Bag<Edge>[]) new Bag[vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new Bag<>();
            }
        }

        public int vertices() {
            return vertices;
        }

        public int edgesCount() {
            return edges;
        }

        public void addVertex(Vertex vertex) {
            allVertices[vertex.id] = vertex;
        }

        public void addEdge(Edge edge) {
            int vertexId1 = edge.either();
            int vertexId2 = edge.other(vertexId1);

            if (allVertices[vertexId1] == null || allVertices[vertexId2] == null) {
                throw new IllegalArgumentException("Vertex id not found");
            }

            adjacent[vertexId1].add(edge);
            adjacent[vertexId2].add(edge);
            edges++;
        }

        public void show(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh, double radiusOfCircleAroundVertex) {
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);

            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLACK);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                for(Edge edge : adjacent(vertexId)) {
                    int otherVertexId = edge.other(vertexId);
                    Vertex otherVertex = allVertices[otherVertexId];

                    if (otherVertexId >= vertexId) {
                        StdDraw.line(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                                otherVertex.xCoordinate, otherVertex.yCoordinate);
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

        public Iterable<Edge> adjacent(int vertexId) {
            return adjacent[vertexId];
        }

        public Iterable<Edge> edges() {
            Bag<Edge> edges = new Bag<>();

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(Edge edge : adjacent[vertex]) {
                    int otherVertex = edge.other(vertex);

                    if (otherVertex > vertex) {
                        edges.add(edge);
                    }
                }
            }

            return edges;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                for(Edge neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise30_EuclideanWeightedGraphs euclideanWeightedGraphs = new Exercise30_EuclideanWeightedGraphs();

        Exercise30_EuclideanWeightedGraphs.EuclideanEdgeWeightedGraph euclideanEdgeWeightedGraph =
                euclideanWeightedGraphs.new EuclideanEdgeWeightedGraph(7);

        EuclideanEdgeWeightedGraph.Vertex vertex0 = euclideanEdgeWeightedGraph.new Vertex(0, 6.1, 1.3);
        EuclideanEdgeWeightedGraph.Vertex vertex1 = euclideanEdgeWeightedGraph.new Vertex(1, 7.2, 2.5);
        EuclideanEdgeWeightedGraph.Vertex vertex2 = euclideanEdgeWeightedGraph.new Vertex(2, 8.4, 1.3);
        EuclideanEdgeWeightedGraph.Vertex vertex3 = euclideanEdgeWeightedGraph.new Vertex(3, 8.4, 15.3);
        EuclideanEdgeWeightedGraph.Vertex vertex4 = euclideanEdgeWeightedGraph.new Vertex(4, 6.1, 15.3);
        EuclideanEdgeWeightedGraph.Vertex vertex5 = euclideanEdgeWeightedGraph.new Vertex(5, 7.2, 5.2);
        EuclideanEdgeWeightedGraph.Vertex vertex6 = euclideanEdgeWeightedGraph.new Vertex(6, 7.2, 8.4);

        euclideanEdgeWeightedGraph.addVertex(vertex0);
        euclideanEdgeWeightedGraph.addVertex(vertex1);
        euclideanEdgeWeightedGraph.addVertex(vertex2);
        euclideanEdgeWeightedGraph.addVertex(vertex3);
        euclideanEdgeWeightedGraph.addVertex(vertex4);
        euclideanEdgeWeightedGraph.addVertex(vertex5);
        euclideanEdgeWeightedGraph.addVertex(vertex6);

        double distanceFromVertex0ToVertex1 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex0, vertex1);
        double distanceFromVertex2ToVertex1 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex2, vertex1);
        double distanceFromVertex0ToVertex2 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex0, vertex2);
        double distanceFromVertex3ToVertex6 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex3, vertex6);
        double distanceFromVertex4ToVertex6 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex4, vertex6);
        double distanceFromVertex3ToVertex4 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex3, vertex4);
        double distanceFromVertex1ToVertex5 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex1, vertex5);
        double distanceFromVertex5ToVertex6 = euclideanWeightedGraphs.getDistanceBetweenVertices(vertex5, vertex6);

        euclideanEdgeWeightedGraph.addEdge(new Edge(0, 1, distanceFromVertex0ToVertex1));
        euclideanEdgeWeightedGraph.addEdge(new Edge(2, 1, distanceFromVertex2ToVertex1));
        euclideanEdgeWeightedGraph.addEdge(new Edge(0, 2, distanceFromVertex0ToVertex2));
        euclideanEdgeWeightedGraph.addEdge(new Edge(3, 6, distanceFromVertex3ToVertex6));
        euclideanEdgeWeightedGraph.addEdge(new Edge(4, 6, distanceFromVertex4ToVertex6));
        euclideanEdgeWeightedGraph.addEdge(new Edge(3, 4, distanceFromVertex3ToVertex4));
        euclideanEdgeWeightedGraph.addEdge(new Edge(1, 5, distanceFromVertex1ToVertex5));
        euclideanEdgeWeightedGraph.addEdge(new Edge(5, 6, distanceFromVertex5ToVertex6));

        euclideanEdgeWeightedGraph.show(0, 15, -2, 18, 0.5);
        StdOut.println(euclideanEdgeWeightedGraph);
    }

    private double getDistanceBetweenVertices(EuclideanEdgeWeightedGraph.Vertex vertex1,
                                              EuclideanEdgeWeightedGraph.Vertex vertex2) {
        double xDifference = vertex1.xCoordinate - vertex2.xCoordinate;
        double yDifference = vertex1.yCoordinate - vertex2.yCoordinate;

        return Math.sqrt((xDifference * xDifference) + (yDifference * yDifference));
    }

}
