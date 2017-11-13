package chapter4.section2;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

/**
 * Created by rene on 26/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise38_EuclideanDigraphs {

    public class EuclideanDigraph {

        public class Vertex {
            protected int id;
            private String name;
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
        private Bag<Vertex>[] adjacent;

        private int[] indegrees;
        private int[] outdegrees;

        public EuclideanDigraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            allVertices = new Vertex[vertices];
            adjacent = (Bag<Vertex>[]) new Bag[vertices];

            indegrees = new int[vertices];
            outdegrees = new int[vertices];

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

            edges++;
            outdegrees[vertex1.id]++;
            indegrees[vertex2.id]++;
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
                for(Vertex neighbor : adjacent(vertexId)) {

                    // Edges pointing up
                    if(allVertices[vertexId].yCoordinate < neighbor.yCoordinate) {
                        if(allVertices[vertexId].xCoordinate < neighbor.xCoordinate) {
                            // Edge pointing diagonally up and right
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding,
                                    neighbor.xCoordinate, neighbor.yCoordinate - padding, arrowWidth, arrowLength);
                        } else if(allVertices[vertexId].xCoordinate > neighbor.xCoordinate) {
                            // Edge pointing diagonally up and left
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding,
                                    neighbor.xCoordinate, neighbor.yCoordinate - padding, arrowWidth, arrowLength);
                        } else {
                            // Edge pointing up
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding * 2,
                                    neighbor.xCoordinate, neighbor.yCoordinate - padding, arrowWidth, arrowLength);
                        }
                    } if(allVertices[vertexId].yCoordinate > neighbor.yCoordinate) {
                        //Edges pointing down
                        if(allVertices[vertexId].xCoordinate < neighbor.xCoordinate) {
                            // Edge pointing diagonally down and right
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                    neighbor.xCoordinate, neighbor.yCoordinate + padding * 4, arrowWidth, arrowLength);
                        } else if(allVertices[vertexId].xCoordinate > neighbor.xCoordinate) {
                            // Edge pointing diagonally down and left
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                    neighbor.xCoordinate, neighbor.yCoordinate + padding * 4, arrowWidth, arrowLength);
                        } else {
                            // Edge pointing down
                            drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                    neighbor.xCoordinate, neighbor.yCoordinate + padding * 2, arrowWidth, arrowLength);
                        }
                    } else if(allVertices[vertexId].yCoordinate == neighbor.yCoordinate) {
                        // Horizontal edges
                        if(allVertices[vertexId].xCoordinate < neighbor.xCoordinate) {
                            // Edge pointing right
                            drawArrowLine(allVertices[vertexId].xCoordinate + padding * 2, allVertices[vertexId].yCoordinate,
                                    neighbor.xCoordinate - padding * 2, neighbor.yCoordinate, arrowWidth, arrowLength);
                        } else if(allVertices[vertexId].xCoordinate > neighbor.xCoordinate) {
                            // Edge pointing left
                            drawArrowLine(allVertices[vertexId].xCoordinate - padding * 2, allVertices[vertexId].yCoordinate,
                                    neighbor.xCoordinate + padding * 2, neighbor.yCoordinate, arrowWidth, arrowLength);
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

        public Iterable<Vertex> adjacent(int vertexId) {
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
                for(Vertex neighbor : adjacent(vertex)) {
                    reverse.addEdge(neighbor, allVertices[vertex]);
                }
            }

            return reverse;
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

        euclideanDigraph.addEdge(vertex0, vertex1);
        euclideanDigraph.addEdge(vertex2, vertex1);
        euclideanDigraph.addEdge(vertex0, vertex2);
        euclideanDigraph.addEdge(vertex3, vertex6);
        euclideanDigraph.addEdge(vertex4, vertex6);
        euclideanDigraph.addEdge(vertex3, vertex4);
        euclideanDigraph.addEdge(vertex1, vertex5);
        euclideanDigraph.addEdge(vertex5, vertex6);

        euclideanDigraph.show(0, 15, 0, 20, 0.08, 0.4);
        StdOut.println(euclideanDigraph);
    }

}
