package chapter6.networkflow;

import chapter4.section1.Graph;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 25/10/18.
 */
// Reference: https://sarielhp.org/teach/2004/b/webpage/lec/20_flow_notes.pdf
// Note: this exercise is solved by exactly the same algorithm as the one used in exercise 6.47
public class Exercise48_DisjointPaths {

    public int geNumberOfDisjointPaths(Graph graph, int source, int target) {
        FlowNetwork flowNetwork = new FlowNetwork(graph.vertices());

        for (int vertex = 0; vertex < graph.vertices(); vertex++) {
            for (int neighbor : graph.adjacent(vertex)) {
                flowNetwork.addEdge(new FlowEdge(vertex, neighbor, 1));
            }
        }

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);
        return (int) fordFulkerson.maxFlowValue();
    }

    public static void main(String[] args) {
        Exercise48_DisjointPaths disjointPaths = new Exercise48_DisjointPaths();

        Graph graph1 = new Graph(5);
        graph1.addEdge(0, 1);
        graph1.addEdge(0, 2);
        graph1.addEdge(0, 3);
        graph1.addEdge(1, 4);
        graph1.addEdge(2, 4);
        graph1.addEdge(3, 4);

        int numberOfDisjointPaths1 = disjointPaths.geNumberOfDisjointPaths(graph1, 0, 4);
        StdOut.println("Number of disjoint paths 1: " + numberOfDisjointPaths1 + " Expected: 3");

        int numberOfDisjointPaths2 = disjointPaths.geNumberOfDisjointPaths(graph1, 4, 0);
        StdOut.println("Number of disjoint paths 2: " + numberOfDisjointPaths2 + " Expected: 3");

        int numberOfDisjointPaths3 = disjointPaths.geNumberOfDisjointPaths(graph1, 1, 4);
        StdOut.println("Number of disjoint paths 3: " + numberOfDisjointPaths3 + " Expected: 2");

        Graph graph2 = new Graph(5);
        graph2.addEdge(0, 1);
        graph2.addEdge(0, 2);
        graph2.addEdge(0, 3);
        graph2.addEdge(1, 4);
        graph2.addEdge(2, 1);
        graph2.addEdge(3, 4);

        int numberOfDisjointPaths4 = disjointPaths.geNumberOfDisjointPaths(graph2, 0, 4);
        StdOut.println("Number of disjoint paths 4: " + numberOfDisjointPaths4 + " Expected: 2");

        int numberOfDisjointPaths5 = disjointPaths.geNumberOfDisjointPaths(graph2, 4, 0);
        StdOut.println("Number of disjoint paths 5: " + numberOfDisjointPaths5 + " Expected: 2");
    }

}
