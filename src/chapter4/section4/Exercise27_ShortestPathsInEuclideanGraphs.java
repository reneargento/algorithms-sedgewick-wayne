package chapter4.section4;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter2.section4.IndexMinPriorityQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import util.DrawUtilities;
import util.DrawUtilities.Coordinate;

import java.awt.*;

/**
 * Created by Rene Argento on 07/12/17.
 */
// Based on the article "Shortest paths in euclidean graphs" by Robert Sedgewick & Jeffrey Scott Vitter, 1985
    // And slides https://www.cs.princeton.edu/courses/archive/spr10/cos226/lectures/15-44ShortestPaths-2x2.pdf
    // And http://www.informit.com/articles/article.aspx?p=169575&seqNum=6
public class Exercise27_ShortestPathsInEuclideanGraphs {

    @SuppressWarnings("unchecked")
    public class EuclideanEdgeWeightedDigraph implements EdgeWeightedDigraphInterface {

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
        private Bag<DirectedEdge>[] adjacent;

        public EuclideanEdgeWeightedDigraph(int vertices) {
            this.vertices = vertices;
            edges = 0;
            allVertices = new Vertex[vertices];
            adjacent = (Bag<DirectedEdge>[]) new Bag[vertices];

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

        public int outdegree(int vertex) {
            return adjacent[vertex].size();
        }

        public Vertex getVertex(int vertexId) {
            return allVertices[vertexId];
        }

        public void addVertex(Vertex vertex) {
            allVertices[vertex.id] = vertex;
        }

        public void addEdge(DirectedEdge edge) {
            int vertexId1 = edge.from();
            int vertexId2 = edge.to();

            if (allVertices[vertexId1] == null || allVertices[vertexId2] == null) {
                throw new IllegalArgumentException("Vertex id not found");
            }

            adjacent[vertexId1].add(edge);
            edges++;
        }

        public void show(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh,
                         double radiusOfCircleAroundVertex, double padding, double arrowLength) {
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);

            StdDraw.setPenRadius(0.002D);

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
                for(DirectedEdge edge : adjacent(vertexId)) {
                    int otherVertexId = edge.to();
                    Vertex neighborVertex = allVertices[otherVertexId];

                    DrawUtilities.drawArrow(allVertices[vertexId].coordinates, neighborVertex.coordinates, padding,
                            arrowLength);
                }
            }
        }

        public Iterable<DirectedEdge> adjacent(int vertexId) {
            return adjacent[vertexId];
        }

        public Iterable<DirectedEdge> edges() {
            Bag<DirectedEdge> edges = new Bag<>();

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(DirectedEdge edge : adjacent[vertex]) {
                    edges.add(edge);
                }
            }

            return edges;
        }

        public EuclideanEdgeWeightedDigraph reverse() {
            EuclideanEdgeWeightedDigraph reverse = new EuclideanEdgeWeightedDigraph(vertices);

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(DirectedEdge edge : adjacent(vertex)) {
                    int neighbor = edge.to();
                    reverse.addEdge(new DirectedEdge(neighbor, vertex, edge.weight()));
                }
            }

            return reverse;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                for(DirectedEdge neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public class DijkstraSPEuclideanGraph {

        private DirectedEdge[] edgeTo;  // last edge on path to vertex
        private double[] distTo;        // length of path to vertex
        private IndexMinPriorityQueue<Double> priorityQueue;

        private int source;
        private boolean shortestDistanceComputed[]; // used to avoid recomputing shortest distances to the same vertex
        private double finalDistanceTo[];           // length of path to a vertex that has already been computed
        private EuclideanEdgeWeightedDigraph euclideanEdgeWeightedDigraph;

        public DijkstraSPEuclideanGraph(EuclideanEdgeWeightedDigraph euclideanEdgeWeightedDigraph, int source) {
            priorityQueue = new IndexMinPriorityQueue<>(euclideanEdgeWeightedDigraph.vertices());

            shortestDistanceComputed = new boolean[euclideanEdgeWeightedDigraph.vertices()];
            finalDistanceTo = new double[euclideanEdgeWeightedDigraph.vertices()];
            this.euclideanEdgeWeightedDigraph = euclideanEdgeWeightedDigraph;
            this.source = source;
        }

        // O(V) due to the use of the Euclidean Heuristic
        private void computeSourceSinkShortestPath(int target) {

            edgeTo = new DirectedEdge[euclideanEdgeWeightedDigraph.vertices()];

            distTo = new double[euclideanEdgeWeightedDigraph.vertices()];
            for(int vertex = 0; vertex < euclideanEdgeWeightedDigraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }

            double distanceFromSourceToTarget = getDistanceBetweenVertices(source, target);

            distTo[source] = distanceFromSourceToTarget;
            priorityQueue.insert(source, distanceFromSourceToTarget);

            while (!priorityQueue.isEmpty()) {
                int vertexToRelax = priorityQueue.deleteMin();

                if (vertexToRelax == target) {
                    break;
                }

                relax(euclideanEdgeWeightedDigraph, vertexToRelax, target);
            }

            finalDistanceTo[target] = distTo[target];
        }

        // O(degree(V))
        private void relax(EuclideanEdgeWeightedDigraph euclideanEdgeWeightedDigraph, int vertex, int target) {

            double distanceFromVertexToTarget = getDistanceBetweenVertices(vertex, target);

            for(DirectedEdge edge : euclideanEdgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                //Euclidean heuristic
                double distanceFromNeighborToTarget = getDistanceBetweenVertices(neighbor, target);

                double distanceToTargetPassingThroughNeighbor = distTo[vertex] + edge.weight()
                        + distanceFromNeighborToTarget - distanceFromVertexToTarget;

                if (distTo[neighbor] > distanceToTargetPassingThroughNeighbor) {
                    distTo[neighbor] = distanceToTargetPassingThroughNeighbor;
                    edgeTo[neighbor] = edge;

                    if (priorityQueue.contains(neighbor)) {
                        priorityQueue.decreaseKey(neighbor, distTo[neighbor]);
                    } else {
                        priorityQueue.insert(neighbor, distTo[neighbor]);
                    }
                }
            }
        }

        private double getDistanceBetweenVertices(int vertex1, int vertex2) {
            EuclideanEdgeWeightedDigraph.Vertex point1 = euclideanEdgeWeightedDigraph.getVertex(vertex1);
            EuclideanEdgeWeightedDigraph.Vertex point2 = euclideanEdgeWeightedDigraph.getVertex(vertex2);

            return Math.sqrt(Math.pow(point1.coordinates.getXCoordinate() - point2.coordinates.getXCoordinate(), 2) +
                    Math.pow(point1.coordinates.getYCoordinate() - point2.coordinates.getYCoordinate(), 2));
        }

        // O(V)
        public double distTo(int vertex) {
            if (!shortestDistanceComputed[vertex]) {
                computeSourceSinkShortestPath(vertex);
                shortestDistanceComputed[vertex] = true;
            }

            return finalDistanceTo[vertex];
        }

        // O(V)
        public boolean hasPathTo(int vertex) {
            if (!shortestDistanceComputed[vertex]) {
                computeSourceSinkShortestPath(vertex);
                shortestDistanceComputed[vertex] = true;
            }

            return finalDistanceTo[vertex] < Double.POSITIVE_INFINITY;
        }

        // O(V)
        public Iterable<DirectedEdge> pathTo(int vertex) {
            if (!shortestDistanceComputed[vertex]) {
                computeSourceSinkShortestPath(vertex);
                shortestDistanceComputed[vertex] = true;
            }

            if (!hasPathTo(vertex)) {
                return null;
            }

            Stack<DirectedEdge> path = new Stack<>();
            for(DirectedEdge edge = edgeTo[vertex]; edge != null; edge = edgeTo[edge.from()]) {
                path.push(edge);
            }

            return path;
        }
    }

    private double getDistanceBetweenVertices(EuclideanEdgeWeightedDigraph.Vertex vertex1,
                                              EuclideanEdgeWeightedDigraph.Vertex vertex2) {
        return Math.sqrt(Math.pow(vertex1.coordinates.getXCoordinate() - vertex2.coordinates.getXCoordinate(), 2) +
                Math.pow(vertex1.coordinates.getYCoordinate() - vertex2.coordinates.getYCoordinate(), 2));
    }

    public static void main(String[] args) {
        Exercise27_ShortestPathsInEuclideanGraphs shortestPathsInEuclideanGraphs =
                new Exercise27_ShortestPathsInEuclideanGraphs();

        EuclideanEdgeWeightedDigraph euclideanEdgeWeightedDigraph =
                shortestPathsInEuclideanGraphs.new EuclideanEdgeWeightedDigraph(7);

        EuclideanEdgeWeightedDigraph.Vertex vertex0 = euclideanEdgeWeightedDigraph.new Vertex(0, 6.1, 1.3);
        EuclideanEdgeWeightedDigraph.Vertex vertex1 = euclideanEdgeWeightedDigraph.new Vertex(1, 7.2, 2.5);
        EuclideanEdgeWeightedDigraph.Vertex vertex2 = euclideanEdgeWeightedDigraph.new Vertex(2, 8.4, 1.3);
        EuclideanEdgeWeightedDigraph.Vertex vertex3 = euclideanEdgeWeightedDigraph.new Vertex(3, 8.4, 15.3);
        EuclideanEdgeWeightedDigraph.Vertex vertex4 = euclideanEdgeWeightedDigraph.new Vertex(4, 6.1, 15.3);
        EuclideanEdgeWeightedDigraph.Vertex vertex5 = euclideanEdgeWeightedDigraph.new Vertex(5, 7.2, 5.2);
        EuclideanEdgeWeightedDigraph.Vertex vertex6 = euclideanEdgeWeightedDigraph.new Vertex(6, 7.2, 8.4);

        euclideanEdgeWeightedDigraph.addVertex(vertex0);
        euclideanEdgeWeightedDigraph.addVertex(vertex1);
        euclideanEdgeWeightedDigraph.addVertex(vertex2);
        euclideanEdgeWeightedDigraph.addVertex(vertex3);
        euclideanEdgeWeightedDigraph.addVertex(vertex4);
        euclideanEdgeWeightedDigraph.addVertex(vertex5);
        euclideanEdgeWeightedDigraph.addVertex(vertex6);

        double distanceFromVertex0ToVertex1 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex0, vertex1);
        double distanceFromVertex2ToVertex1 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex2, vertex1);
        double distanceFromVertex0ToVertex2 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex0, vertex2);
        double distanceFromVertex3ToVertex6 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex3, vertex6);
        double distanceFromVertex4ToVertex6 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex4, vertex6);
        double distanceFromVertex3ToVertex4 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex3, vertex4);
        double distanceFromVertex1ToVertex5 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex1, vertex5);
        double distanceFromVertex5ToVertex6 = shortestPathsInEuclideanGraphs.getDistanceBetweenVertices(vertex5, vertex6);

        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(0, 1, distanceFromVertex0ToVertex1));
        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(2, 1, distanceFromVertex2ToVertex1));
        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(0, 2, distanceFromVertex0ToVertex2));
        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(3, 6, distanceFromVertex3ToVertex6));
        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(4, 6, distanceFromVertex4ToVertex6));
        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(3, 4, distanceFromVertex3ToVertex4));
        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(1, 5, distanceFromVertex1ToVertex5));
        euclideanEdgeWeightedDigraph.addEdge(new DirectedEdge(5, 6, distanceFromVertex5ToVertex6));

        DijkstraSPEuclideanGraph dijkstraSPEuclideanGraph =
                shortestPathsInEuclideanGraphs.new DijkstraSPEuclideanGraph(euclideanEdgeWeightedDigraph, 0);

        euclideanEdgeWeightedDigraph.show(0, 15, 0, 20,
                0.5, 0.08, 0.5);

        for(int vertex = 0; vertex < euclideanEdgeWeightedDigraph.vertices(); vertex++) {
            StdOut.printf("Distance to vertex %d: %.2f\n", vertex, dijkstraSPEuclideanGraph.distTo(vertex));
        }

        StdOut.println("\nExpected distances");
        StdOut.println("Vertex 0: 0.0");
        StdOut.println("Vertex 1: 1.63");
        StdOut.println("Vertex 2: 2.30");
        StdOut.println("Vertex 3: Infinity");
        StdOut.println("Vertex 4: Infinity");
        StdOut.println("Vertex 5: 4.33");
        StdOut.println("Vertex 6: 7.53");

        for(int vertex = 0; vertex < euclideanEdgeWeightedDigraph.vertices(); vertex++) {
            StdOut.print("\nPath from vertex 0 to vertex " + vertex + ": ");

            if (dijkstraSPEuclideanGraph.hasPathTo(vertex)) {
                for(DirectedEdge edge : dijkstraSPEuclideanGraph.pathTo(vertex)) {
                    StdOut.print(edge.from() + "->" + edge.to() + " ");
                }
            } else {
                StdOut.print("There is no path to vertex " + vertex);
            }
        }

        StdOut.println("\n\nExpected paths");
        StdOut.println("Vertex 0: ");
        StdOut.println("Vertex 1: 0->1");
        StdOut.println("Vertex 2: 0->2");
        StdOut.println("Vertex 3: There is no path to vertex 3");
        StdOut.println("Vertex 4 There is no path to vertex 4");
        StdOut.println("Vertex 5: 0->1 1->5");
        StdOut.println("Vertex 6: 0->1 1->5 5->6");
    }

}
