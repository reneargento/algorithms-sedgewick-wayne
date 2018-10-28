package chapter6.networkflow;

import chapter4.section1.Graph;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/10/18.
 */
// Reference: https://sarielhp.org/teach/2004/b/webpage/lec/20_flow_notes.pdf
public class Exercise47_STConnectivity {

    public int getMinNumberOfEdgesToDisconnect(Graph graph, int source, int target) {
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
        Exercise47_STConnectivity stConnectivity = new Exercise47_STConnectivity();

        Graph graph1 = new Graph(5);
        graph1.addEdge(0, 1);
        graph1.addEdge(0, 2);
        graph1.addEdge(0, 3);
        graph1.addEdge(1, 4);
        graph1.addEdge(2, 4);
        graph1.addEdge(3, 4);

        int numberOfEdgesToDisconnect1 = stConnectivity.getMinNumberOfEdgesToDisconnect(graph1, 0, 4);
        StdOut.println("Number of edges to disconnect graph 1: " + numberOfEdgesToDisconnect1 + " Expected: 3");

        int numberOfEdgesToDisconnect2 = stConnectivity.getMinNumberOfEdgesToDisconnect(graph1, 4, 0);
        StdOut.println("Number of edges to disconnect graph 2: " + numberOfEdgesToDisconnect2 + " Expected: 3");

        int numberOfEdgesToDisconnect3 = stConnectivity.getMinNumberOfEdgesToDisconnect(graph1, 1, 4);
        StdOut.println("Number of edges to disconnect graph 3: " + numberOfEdgesToDisconnect3 + " Expected: 2");

        Graph graph2 = new Graph(5);
        graph2.addEdge(0, 1);
        graph2.addEdge(0, 2);
        graph2.addEdge(0, 3);
        graph2.addEdge(1, 4);
        graph2.addEdge(2, 1);
        graph2.addEdge(3, 4);

        int numberOfEdgesToDisconnect4 = stConnectivity.getMinNumberOfEdgesToDisconnect(graph2, 0, 4);
        StdOut.println("Number of edges to disconnect graph 4: " + numberOfEdgesToDisconnect4 + " Expected: 2");

        int numberOfEdgesToDisconnect5 = stConnectivity.getMinNumberOfEdgesToDisconnect(graph2, 4, 0);
        StdOut.println("Number of edges to disconnect graph 5: " + numberOfEdgesToDisconnect5 + " Expected: 2");
    }

}
