package chapter4.section3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 07/11/17.
 */
public class Exercise9 {

    public class EdgeWeightedGraphWithInputStreamConstructor extends EdgeWeightedGraph {

        public EdgeWeightedGraphWithInputStreamConstructor(In in) {
            super(in.readInt());
            int edges = in.readInt();

            if (edges < 0) {
                throw new IllegalArgumentException("Number of edges must be nonnegative");
            }

            for(int i = 0; i < edges; i++) {
                int vertex1 = in.readInt();
                int vertex2 = in.readInt();
                double weight = in.readDouble();

                Edge edge = new Edge(vertex1, vertex2, weight);
                addEdge(edge);
            }
        }
    }

    public static void main(String[] args) {
        String tinyEWGFilePath = Constants.FILES_PATH + Constants.TINY_EWG_FILE;
        Exercise9.EdgeWeightedGraphWithInputStreamConstructor edgeWeightedGraph =
                new Exercise9().new EdgeWeightedGraphWithInputStreamConstructor(new In(tinyEWGFilePath));
        StdOut.println(edgeWeightedGraph);
    }

}
