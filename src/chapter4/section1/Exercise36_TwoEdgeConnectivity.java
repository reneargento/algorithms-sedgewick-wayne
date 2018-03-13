package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 01/10/17.
 */
// Based on http://algs4.cs.princeton.edu/41graph/Bridge.java.html
public class Exercise36_TwoEdgeConnectivity {

    private class Edge {
        int vertex1;
        int vertex2;

        Edge(int vertex1, int vertex2) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }
    }

    private static int count;

    private int[] low; // low[v] = lowest preorder of any vertex connected to v
    private int[] time; // time[v] = order in which dfs examines v

    private List<Edge> findBridges(Graph graph) {
        low = new int[graph.vertices()];
        time = new int[graph.vertices()];

        List<Edge> bridges = new ArrayList<>();

        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            low[vertex] = -1;
            time[vertex] = -1;
        }

        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            if (time[vertex] == -1) {
                dfs(graph, vertex, vertex, bridges);
            }
        }

        return bridges;
    }

    private void dfs(Graph graph, int currentVertex, int sourceVertex, List<Edge> bridges) {
        time[currentVertex] = count;
        low[currentVertex] = count;
        count++;

        for(int neighbor : graph.adjacent(currentVertex)) {
            if (time[neighbor] == -1) {
                dfs(graph, neighbor, currentVertex, bridges);

                low[currentVertex] = Math.min(low[currentVertex], low[neighbor]);

                if (low[neighbor] == time[neighbor]) {
                    bridges.add(new Edge(currentVertex, neighbor));
                }

            } else if (neighbor != sourceVertex) {
                low[currentVertex] = Math.min(low[currentVertex], time[neighbor]);
            }
        }
    }

    public static void main(String[] args) {
        Exercise36_TwoEdgeConnectivity twoEdgeConnectivity = new Exercise36_TwoEdgeConnectivity();

        Graph graph1 = new Graph(4);
        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        graph1.addEdge(3, 0);

        List<Edge> bridges1 = twoEdgeConnectivity.findBridges(graph1);

        if (bridges1.size() == 0) {
            StdOut.println("Graph is two-edge connected");
        } else {
            StdOut.println("Bridges");

            for(Edge edge : bridges1) {
                StdOut.println(edge.vertex1 + "-" + edge.vertex2);
            }
        }
        StdOut.println("Expected: Graph is two-edge connected\n");

        Graph graph2 = new Graph(6);
        graph2.addEdge(0, 1);
        graph2.addEdge(2, 1);
        graph2.addEdge(0, 2);
        graph2.addEdge(3, 5);
        graph2.addEdge(4, 5);
        graph2.addEdge(3, 4);
        graph2.addEdge(1, 5);

        List<Edge> bridges2 = twoEdgeConnectivity.findBridges(graph2);

        if (bridges2.size() == 0) {
            StdOut.println("Graph is two-edge connected");
        } else {
            StdOut.println("Bridges");

            for(Edge edge : bridges2) {
                StdOut.println(edge.vertex1 + "-" + edge.vertex2);
            }
        }
        StdOut.println("Expected: 1-5\n");

        Graph graph3 = new Graph(7);
        graph3.addEdge(0, 1);
        graph3.addEdge(2, 1);
        graph3.addEdge(0, 2);
        graph3.addEdge(3, 6);
        graph3.addEdge(4, 6);
        graph3.addEdge(3, 4);
        graph3.addEdge(1, 5);
        graph3.addEdge(5, 6);

        List<Edge> bridges3 = twoEdgeConnectivity.findBridges(graph3);

        if (bridges3.size() == 0) {
            StdOut.println("Graph is two-edge connected");
        } else {
            StdOut.println("Bridges");

            for(Edge edge : bridges3) {
                StdOut.println(edge.vertex1 + "-" + edge.vertex2);
            }
        }
        StdOut.println("Expected: 5-6");
        StdOut.println("Expected: 1-5");
    }

}
