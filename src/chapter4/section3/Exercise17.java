package chapter4.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 08/11/17.
 */
public class Exercise17 {

    public class EdgeWeightedGraphWithToString extends EdgeWeightedGraph{

        public EdgeWeightedGraphWithToString(int vertices) {
            super(vertices);
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
        Exercise17.EdgeWeightedGraphWithToString edgeWeightedGraph = new Exercise17().new EdgeWeightedGraphWithToString(6);
        edgeWeightedGraph.addEdge(new Edge(2, 5, 33));
        edgeWeightedGraph.addEdge(new Edge(3, 2, 14));
        edgeWeightedGraph.addEdge(new Edge(4, 5, 89));
        edgeWeightedGraph.addEdge(new Edge(4, 5, 86));
        edgeWeightedGraph.addEdge(new Edge(3, 0, 15));
        StdOut.println(edgeWeightedGraph);

        StdOut.println("Expected:\n" +
                "0: 3-0 15.00000 \n" +
                "1: \n" +
                "2: 3-2 14.00000 2-5 33.00000 \n" +
                "3: 3-0 15.00000 3-2 14.00000 \n" +
                "4: 4-5 86.00000 4-5 89.00000 \n" +
                "5: 4-5 86.00000 4-5 89.00000 2-5 33.00000");
    }

}
