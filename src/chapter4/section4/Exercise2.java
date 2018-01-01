package chapter4.section4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 29/11/17.
 */
public class Exercise2 {

    public class EdgeWeightedDigraphWithInConstructor extends EdgeWeightedDigraph {

        public EdgeWeightedDigraphWithInConstructor(In in) {
            super(in.readInt());
            int edges = in.readInt();

            if (edges < 0) {
                throw new IllegalArgumentException("Number of edges must be nonnegative");
            }

            for(int i = 0; i < edges; i++) {
                int vertexFrom = in.readInt();
                int vertexTo = in.readInt();
                double weight = in.readDouble();

                DirectedEdge edge = new DirectedEdge(vertexFrom, vertexTo, weight);
                addEdge(edge);
            }
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

    /**
     * File content:
     6
     5
     2 5 33
     3 2 14
     4 5 89
     4 5 86
     3 0 15
     */

    public static void main(String[] args) {
        String filePath = Constants.FILES_PATH + Constants.EWD_FILE;
        EdgeWeightedDigraphWithInConstructor edgeWeightedDigraphWithInConstructor =
                new Exercise2().new EdgeWeightedDigraphWithInConstructor(new In(filePath));

        StdOut.println(edgeWeightedDigraphWithInConstructor);

        StdOut.println("Expected:\n" +
                "0: \n" +
                "1: \n" +
                "2: 2->5 33.00 \n" +
                "3: 3->0 15.00 3->2 14.00 \n" +
                "4: 4->5 86.00 4-5 89.00 \n" +
                "5: ");
    }

}
