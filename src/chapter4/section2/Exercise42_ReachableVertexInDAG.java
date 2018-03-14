package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 29/10/17.
 */
public class Exercise42_ReachableVertexInDAG {

    // If a vertex is reachable from every other vertex in a DAG, it must be the last vertex in the topological order
    public boolean hasVertexReachableFromEveryOtherVertex(Digraph digraph) {
        // 0- Check if it is a DAG
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        // 1- Get topological order
        Topological topological = new Topological(digraph);

        int[] topologicalOrderArray = new int[digraph.vertices()];
        int topologicalOrderArrayIndex = 0;

        for(int vertex : topological.order()) {
            topologicalOrderArray[topologicalOrderArrayIndex++] = vertex;
        }

        int possibleReachableVertex = topologicalOrderArray[topologicalOrderArray.length - 1];

        // 2- Invert graph and do a dfs from the last vertex in the topological order
        Digraph reverseDigraph = digraph.reverse();
        boolean[] visited = new boolean[digraph.vertices()];

        dfs(reverseDigraph, possibleReachableVertex, visited);

        // 3- Check if it reaches all other vertices
        for(int vertex = 0; vertex < visited.length; vertex++) {
            if (!visited[vertex]) {
                return false;
            }
        }

        return true;
    }

    private void dfs(Digraph digraph, int vertex, boolean[] visited) {
        visited[vertex] = true;

        for(int neighbor : digraph.adjacent(vertex)) {
            dfs(digraph, neighbor, visited);
        }
    }

    // Another method: if there is only one vertex with outdegree == 0 in the DAG,
    // then this is the vertex reachable from every other vertex
    public boolean hasVertexReachableFromEveryOtherVertex2(Digraph digraph) {
        // 0- Check if it is a DAG
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        // 1- Check if there is only one sink
        int sinks = 0;

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (digraph.outdegree(vertex) == 0) {
                sinks++;
            }
        }

        return sinks == 1;
    }

    public static void main(String[] args) {
        Exercise42_ReachableVertexInDAG reachableVertexInDAG = new Exercise42_ReachableVertexInDAG();

        Digraph digraph1 = new Digraph(4);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 3);

        StdOut.println("Reachable 1: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex(digraph1)
                + " Expected: true");
        StdOut.println("Reachable 1: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex2(digraph1)
                + " Expected: true");

        Digraph digraph2 = new Digraph(4);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(2, 3);

        StdOut.println("Reachable 2: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex(digraph2)
                + " Expected: false");
        StdOut.println("Reachable 2: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex2(digraph2)
                + " Expected: false");

        Digraph digraph3 = new Digraph(5);
        digraph3.addEdge(0, 1);
        digraph3.addEdge(0, 2);
        digraph3.addEdge(4, 3);
        digraph3.addEdge(3, 1);

        StdOut.println("Reachable 3: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex(digraph3)
                + " Expected: false");
        StdOut.println("Reachable 3: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex2(digraph3)
                + " Expected: false");

        Digraph digraph4 = new Digraph(5);
        digraph4.addEdge(0, 1);
        digraph4.addEdge(0, 2);
        digraph4.addEdge(2, 1);
        digraph4.addEdge(4, 3);
        digraph4.addEdge(3, 1);

        StdOut.println("Reachable 4: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex(digraph4)
                + " Expected: true");
        StdOut.println("Reachable 4: " + reachableVertexInDAG.hasVertexReachableFromEveryOtherVertex2(digraph4)
                + " Expected: true");
    }

}
