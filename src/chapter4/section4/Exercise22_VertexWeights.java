package chapter4.section4;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 05/12/17.
 */
public class Exercise22_VertexWeights {

    public class WeightedVertex {

        private int id;
        private double weight;

        WeightedVertex(int id, double weight) {
            this.id = id;
            this.weight = weight;
        }
    }

    @SuppressWarnings("unchecked")
    public class VertexWeightedDigraph {

        private final int verticesCount;
        private int edges;
        private Bag<Integer>[] adjacent;
        private WeightedVertex[] vertices;

        public VertexWeightedDigraph(int verticesCount) {
            this.verticesCount = verticesCount;
            edges = 0;
            adjacent = (Bag<Integer>[]) new Bag[verticesCount];
            vertices = new WeightedVertex[verticesCount];

            for(int vertex = 0; vertex < verticesCount; vertex++) {
                adjacent[vertex] = new Bag<>();
            }
        }

        public void setVertex(WeightedVertex weightedVertex) {
            if (weightedVertex == null) {
                throw new IllegalArgumentException("Weighted vertex cannot be null");
            }

            if (weightedVertex.id < 0 || weightedVertex.id >= verticesCount) {
                throw new IllegalArgumentException("Invalid vertex id");
            }

            vertices[weightedVertex.id] = weightedVertex;
        }

        public WeightedVertex[] vertices() {
            return vertices;
        }

        public int verticesCount() {
            return verticesCount;
        }

        public int edgesCount() {
            return edges;
        }

        public void addEdge(int vertexFrom, int vertexTo) {
            adjacent[vertexFrom].add(vertexTo);
            edges++;
        }

        public Iterable<Integer> adjacent(int vertex) {
            return adjacent[vertex];
        }

        public Iterable<Integer> edges() {
            Bag<Integer> bag = new Bag<>();

            for(int vertex = 0; vertex < verticesCount; vertex++) {
                for(int neighbor : adjacent[vertex]) {
                    bag.add(neighbor);
                }
            }

            return bag;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < verticesCount(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                for(int neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public class DijkstraVertexWeightedDigraph {

        private DijkstraSP dijkstraSP;
        private int verticesCount;

        DijkstraVertexWeightedDigraph(VertexWeightedDigraph vertexWeightedDigraph, int source) {
            if (vertexWeightedDigraph == null) {
                throw new IllegalArgumentException("Vertex weighted digraph cannot be null");
            }

            verticesCount = vertexWeightedDigraph.verticesCount;

            EdgeWeightedDigraph edgeWeightedDigraph =
                    new EdgeWeightedDigraph(verticesCount + vertexWeightedDigraph.edgesCount());

            for(WeightedVertex weightedVertex : vertexWeightedDigraph.vertices) {
                edgeWeightedDigraph.addEdge(new DirectedEdge(weightedVertex.id, weightedVertex.id + verticesCount,
                        weightedVertex.weight));
            }

            for(int vertex = 0; vertex < verticesCount; vertex++) {
                for(int neighbor : vertexWeightedDigraph.adjacent(vertex)) {
                    edgeWeightedDigraph.addEdge(new DirectedEdge(vertex + verticesCount, neighbor, 0));
                }
            }

            dijkstraSP = new DijkstraSP(edgeWeightedDigraph, source);
        }

        public double distTo(int vertex) {
            int target = vertex + verticesCount;
            return dijkstraSP.distTo(target);
        }

        public boolean hasPathTo(int vertex) {
            int target = vertex + verticesCount;
            return dijkstraSP.hasPathTo(target);
        }

        public Iterable<DirectedEdge> pathTo(int vertex) {
            int target = vertex + verticesCount;
            return dijkstraSP.pathTo(target);
        }

    }

    public static void main(String[] args) {
        Exercise22_VertexWeights exercise22 = new Exercise22_VertexWeights();

        VertexWeightedDigraph vertexWeightedDigraph = exercise22.new VertexWeightedDigraph(7);

        vertexWeightedDigraph.setVertex(exercise22.new WeightedVertex(0, 5));
        vertexWeightedDigraph.setVertex(exercise22.new WeightedVertex(1, 10));
        vertexWeightedDigraph.setVertex(exercise22.new WeightedVertex(2, 2));
        vertexWeightedDigraph.setVertex(exercise22.new WeightedVertex(3, 3));
        vertexWeightedDigraph.setVertex(exercise22.new WeightedVertex(4, 3));
        vertexWeightedDigraph.setVertex(exercise22.new WeightedVertex(5, 12));
        vertexWeightedDigraph.setVertex(exercise22.new WeightedVertex(6, 4));

        vertexWeightedDigraph.addEdge(0, 1);
        vertexWeightedDigraph.addEdge(1, 2);
        vertexWeightedDigraph.addEdge(2, 1);
        vertexWeightedDigraph.addEdge(1, 3);
        vertexWeightedDigraph.addEdge(2, 6);
        vertexWeightedDigraph.addEdge(2, 4);
        vertexWeightedDigraph.addEdge(4, 5);

        DijkstraVertexWeightedDigraph dijkstraVertexWeighted = exercise22.new DijkstraVertexWeightedDigraph(vertexWeightedDigraph, 0);

        StdOut.println("Distance to 0: " + dijkstraVertexWeighted.distTo(0) + " Expected: 5.0");
        StdOut.println("Distance to 1: " + dijkstraVertexWeighted.distTo(1) + " Expected: 15.0");
        StdOut.println("Distance to 2: " + dijkstraVertexWeighted.distTo(2) + " Expected: 17.0");
        StdOut.println("Distance to 3: " + dijkstraVertexWeighted.distTo(3) + " Expected: 18.0");
        StdOut.println("Distance to 4: " + dijkstraVertexWeighted.distTo(4) + " Expected: 20.0");
        StdOut.println("Distance to 5: " + dijkstraVertexWeighted.distTo(5) + " Expected: 32.0");
        StdOut.println("Distance to 6: " + dijkstraVertexWeighted.distTo(6) + " Expected: 21.0");

        StdOut.println("\nHas path to 5: " + dijkstraVertexWeighted.hasPathTo(5) + " Expected: true");

        StdOut.print("\nPath to 5: ");

        for(DirectedEdge edge : dijkstraVertexWeighted.pathTo(5)) {
            StdOut.print(edge.from() + "->" + edge.to() + " ");
        }

        StdOut.println("\nExpected:  0->7 7->1 1->8 8->2 2->9 9->4 4->11 11->5 5->12");
    }

}
