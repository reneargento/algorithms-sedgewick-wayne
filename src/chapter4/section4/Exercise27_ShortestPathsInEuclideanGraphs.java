package chapter4.section4;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter2.section4.IndexMinPriorityQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

/**
 * Created by rene on 07/12/17.
 */
// Based on the article "Shortest paths in euclidean graphs" by Robert Sedgewick & Jeffrey Scott Vitter, 1985
    // And slides https://www.cs.princeton.edu/courses/archive/spr10/cos226/lectures/15-44ShortestPaths-2x2.pdf
    // And http://www.informit.com/articles/article.aspx?p=169575&seqNum=6
public class Exercise27_ShortestPathsInEuclideanGraphs {

    @SuppressWarnings("unchecked")
    public class EuclideanEdgeWeightedDigraph {

        public class Vertex {
            private int id;
            private String name;
            private double xCoordinate;
            private double yCoordinate;

            Vertex(int id, double xCoordinate, double yCoordinate) {
                this(id, String.valueOf(id), xCoordinate, yCoordinate);
            }

            Vertex(int id, String name, double xCoordinate, double yCoordinate) {
                this.id = id;
                this.name = name;
                this.xCoordinate = xCoordinate;
                this.yCoordinate = yCoordinate;
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

        public Vertex getVertex(int vertex) {
            return allVertices[vertex];
        }

        public void addVertex(Vertex vertex) {
            allVertices[vertex.id] = vertex;
        }

        public void addEdge(DirectedEdge edge) {
            int vertexId1 = edge.from();
            int vertexId2 = edge.to();

            if(allVertices[vertexId1] == null || allVertices[vertexId2] == null) {
                throw new IllegalArgumentException("Vertex id not found");
            }

            adjacent[vertexId1].add(edge);
            edges++;
        }

        public void show(double xScaleLow, double xScaleHigh, double yScaleLow, double yScaleHigh,
                         double padding, double arrowLength) {
            // Set canvas size
            StdDraw.setCanvasSize(500, 400);
            StdDraw.setXscale(xScaleLow, xScaleHigh);
            StdDraw.setYscale(yScaleLow, yScaleHigh);

            StdDraw.setPenRadius(0.002D);
            StdDraw.setPenColor(Color.BLACK);

            double arrowWidth = padding * 2;

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                for(DirectedEdge edge : adjacent(vertexId)) {
                    int neighbor = edge.to();
                    Vertex neighborVertex = allVertices[neighbor];

                    // Edges pointing up
                    if(allVertices[vertexId].yCoordinate < neighborVertex.yCoordinate) {
                        if(allVertices[vertexId].xCoordinate < neighborVertex.xCoordinate) {
                            // Edge pointing diagonally up and right
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding,
                                    neighborVertex.xCoordinate, neighborVertex.yCoordinate - padding, arrowWidth, arrowLength);
                        } else if(allVertices[vertexId].xCoordinate > neighborVertex.xCoordinate) {
                            // Edge pointing diagonally up and left
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding,
                                    neighborVertex.xCoordinate, neighborVertex.yCoordinate - padding, arrowWidth, arrowLength);
                        } else {
                            // Edge pointing up
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding * 2,
                                    neighborVertex.xCoordinate, neighborVertex.yCoordinate - padding, arrowWidth, arrowLength);
                        }
                    } if(allVertices[vertexId].yCoordinate > neighborVertex.yCoordinate) {
                        //Edges pointing down
                        if(allVertices[vertexId].xCoordinate < neighborVertex.xCoordinate) {
                            // Edge pointing diagonally down and right
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                    neighborVertex.xCoordinate, neighborVertex.yCoordinate + padding * 4, arrowWidth, arrowLength);
                        } else if(allVertices[vertexId].xCoordinate > neighborVertex.xCoordinate) {
                            // Edge pointing diagonally down and left
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                    neighborVertex.xCoordinate, neighborVertex.yCoordinate + padding * 4, arrowWidth, arrowLength);
                        } else {
                            // Edge pointing down
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                    neighborVertex.xCoordinate, neighborVertex.yCoordinate + padding * 2, arrowWidth, arrowLength);
                        }
                    } else if(allVertices[vertexId].yCoordinate == neighborVertex.yCoordinate) {
                        // Horizontal edges
                        if(allVertices[vertexId].xCoordinate < neighborVertex.xCoordinate) {
                            // Edge pointing right
                            drawArrowLine(allVertices[vertexId].xCoordinate + padding * 2, allVertices[vertexId].yCoordinate,
                                    neighborVertex.xCoordinate - padding * 2, neighborVertex.yCoordinate, arrowWidth, arrowLength);
                        } else if(allVertices[vertexId].xCoordinate > neighborVertex.xCoordinate) {
                            // Edge pointing left
                            drawArrowLine(allVertices[vertexId].xCoordinate - padding * 2, allVertices[vertexId].yCoordinate,
                                    neighborVertex.xCoordinate + padding * 2, neighborVertex.yCoordinate, arrowWidth, arrowLength);
                        }
                    }
                }
            }

            StdDraw.setPenColor(Color.BLUE);

            for(int vertexId = 0; vertexId < vertices; vertexId++) {
                if(allVertices[vertexId] != null) {
                    StdDraw.text(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                            allVertices[vertexId].name);
                }
            }
        }

        /**
         * Draw an arrow line between two points.
         * @param x1 x-position of first point.
         * @param y1 y-position of first point.
         * @param x2 x-position of second point.
         * @param y2 y-position of second point.
         * @param arrowWidth  the width of the arrow.
         * @param arrowHeight  the height of the arrow.
         */
        private void drawArrowLine(double x1, double y1, double x2, double y2, double arrowWidth, double arrowHeight) {
            double xDistance = x2 - x1;
            double yDistance = y2 - y1;
            double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

            double xm = distance - arrowWidth;
            double xn = xm;
            double ym = arrowHeight;
            double yn = -arrowHeight;
            double x;

            double sin = yDistance / distance;
            double cos = xDistance / distance;

            x = xm * cos - ym * sin + x1;
            ym = xm * sin + ym * cos + y1;
            xm = x;

            x = xn * cos - yn * sin + x1;
            yn = xn * sin + yn * cos + y1;
            xn = x;

            double[] xPoints = {x2, xm, xn};
            double[] yPoints = {y2, ym, yn};

            StdDraw.line(x1, y1, x2, y2);
            StdDraw.filledPolygon(xPoints, yPoints);
        }

        public Iterable<DirectedEdge> adjacent(int vertex) {
            return adjacent[vertex];
        }

        public Iterable<DirectedEdge> edges() {
            Bag<DirectedEdge> bag = new Bag<>();

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(DirectedEdge edge : adjacent[vertex]) {
                    bag.add(edge);
                }
            }

            return bag;
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

                if(vertexToRelax == target) {
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

                if(distTo[neighbor] > distanceToTargetPassingThroughNeighbor) {
                    distTo[neighbor] = distanceToTargetPassingThroughNeighbor;
                    edgeTo[neighbor] = edge;

                    if(priorityQueue.contains(neighbor)) {
                        priorityQueue.changeKey(neighbor, distTo[neighbor]);
                    } else {
                        priorityQueue.insert(neighbor, distTo[neighbor]);
                    }
                }
            }
        }

        private double getDistanceBetweenVertices(int vertex1, int vertex2) {
            EuclideanEdgeWeightedDigraph.Vertex point1 = euclideanEdgeWeightedDigraph.getVertex(vertex1);
            EuclideanEdgeWeightedDigraph.Vertex point2 = euclideanEdgeWeightedDigraph.getVertex(vertex2);

            return Math.sqrt(Math.pow(point1.xCoordinate - point2.xCoordinate, 2) +
                    Math.pow(point1.yCoordinate - point2.yCoordinate, 2));
        }

        // O(V)
        public double distTo(int vertex) {
            if(!shortestDistanceComputed[vertex]) {
                computeSourceSinkShortestPath(vertex);
                shortestDistanceComputed[vertex] = true;
            }

            return finalDistanceTo[vertex];
        }

        // O(V)
        public boolean hasPathTo(int vertex) {
            if(!shortestDistanceComputed[vertex]) {
                computeSourceSinkShortestPath(vertex);
                shortestDistanceComputed[vertex] = true;
            }

            return finalDistanceTo[vertex] < Double.POSITIVE_INFINITY;
        }

        // O(V)
        public Iterable<DirectedEdge> pathTo(int vertex) {
            if(!shortestDistanceComputed[vertex]) {
                computeSourceSinkShortestPath(vertex);
                shortestDistanceComputed[vertex] = true;
            }

            if(!hasPathTo(vertex)) {
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
        return Math.sqrt(Math.pow(vertex1.xCoordinate - vertex2.xCoordinate, 2) +
                Math.pow(vertex1.yCoordinate - vertex2.yCoordinate, 2));
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

        euclideanEdgeWeightedDigraph.show(0, 15, 0, 20, 0.08, 0.4);

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

            if(dijkstraSPEuclideanGraph.hasPathTo(vertex)) {
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
