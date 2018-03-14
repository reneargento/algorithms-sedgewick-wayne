package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 30/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise43_ReachableVertexInDigraph {

    // If a vertex is reachable from every other vertex in a digraph,
    // it must be a vertex in the last (considering the topological order) strong component of the digraph.
    public boolean hasVertexReachableFromEveryOtherVertex(Digraph digraph) {
        // 1- Compute the strong components
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(digraph);

        List<Integer>[] strongComponents = (List<Integer>[]) new ArrayList[kosarajuSharirSCC.count()];
        for(int scc = 0; scc < strongComponents.length; scc++) {
            strongComponents[scc] = new ArrayList<>();
        }

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            int sccId = kosarajuSharirSCC.id(vertex);
            strongComponents[sccId].add(vertex);
        }

        // 2- The Kosaraju-Sharir algorithm already outputs the strong components in reverse topological order
        int vertexInLastStrongComponent = strongComponents[0].get(0);

        // 3- Do a DFS from any vertex in the last strong component on the reverse digraph
        Digraph reverseDigraph = digraph.reverse();
        boolean[] visited = new boolean[digraph.vertices()];

        dfs(reverseDigraph, vertexInLastStrongComponent, visited);

        // 4- If all vertices were visited, then the vertex is reachable from every other vertex
        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (!visited[vertex]) {
                return false;
            }
        }

        return true;
    }

    private void dfs(Digraph digraph, int vertex, boolean[] visited) {
        visited[vertex] = true;

        for(int neighbor : digraph.adjacent(vertex)) {
            if (!visited[neighbor]) {
                dfs(digraph, neighbor, visited);
            }
        }
    }

    // Another method: compute the kernel DAG based on the strong components.
    // If there is only one vertex with outdegree == 0 in the kernel DAG,
    // then there is one (or more) vertex reachable from every other vertex
    // (the reachable vertices are the vertices in the last strong component in the topological order)
    public boolean hasVertexReachableFromEveryOtherVertex2(Digraph digraph) {
        // 1- Compute the strong components
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(digraph);

        // 2 - Compute the kernel DAG
        Digraph kernelDAG = kosarajuSharirSCC.getKernelDAG();

        // 3- Check if there is only one sink in the kernel DAG
        int sinks = 0;

        for(int vertex = 0; vertex < kernelDAG.vertices(); vertex++) {
            if (kernelDAG.outdegree(vertex) == 0) {
                sinks++;
            }
        }

        return sinks == 1;
    }

    public static void main(String[] args) {
        Exercise43_ReachableVertexInDigraph reachableVertexInDigraph = new Exercise43_ReachableVertexInDigraph();

        Digraph digraph1 = new Digraph(4);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 3);
        digraph1.addEdge(3, 0);

        StdOut.println("Reachable 1: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex(digraph1)
                + " Expected: true");
        StdOut.println("Reachable 1: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex2(digraph1)
                + " Expected: true");

        Digraph digraph2 = new Digraph(4);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(2, 3);

        StdOut.println("Reachable 2: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex(digraph2)
                + " Expected: false");
        StdOut.println("Reachable 2: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex2(digraph2)
                + " Expected: false");

        Digraph digraph3 = new Digraph(5);
        digraph3.addEdge(0, 1);
        digraph3.addEdge(0, 2);
        digraph3.addEdge(4, 3);
        digraph3.addEdge(3, 1);

        StdOut.println("Reachable 3: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex(digraph3)
                + " Expected: false");
        StdOut.println("Reachable 3: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex2(digraph3)
                + " Expected: false");

        Digraph digraph4 = new Digraph(5);
        digraph4.addEdge(0, 1);
        digraph4.addEdge(0, 2);
        digraph4.addEdge(2, 1);
        digraph4.addEdge(4, 3);
        digraph4.addEdge(3, 1);

        StdOut.println("Reachable 4: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex(digraph4)
                + " Expected: true");
        StdOut.println("Reachable 4: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex2(digraph4)
                + " Expected: true");

        Digraph digraph5 = new Digraph(6);
        digraph5.addEdge(0, 1);
        digraph5.addEdge(0, 2);
        digraph5.addEdge(2, 0);
        digraph5.addEdge(3, 4);
        digraph5.addEdge(4, 5);
        digraph5.addEdge(5, 3);

        StdOut.println("Reachable 5: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex(digraph5)
                + " Expected: false");
        StdOut.println("Reachable 5: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex2(digraph5)
                + " Expected: false");

        Digraph digraph6 = new Digraph(6);
        digraph6.addEdge(0, 1);
        digraph6.addEdge(0, 2);
        digraph6.addEdge(2, 0);
        digraph6.addEdge(3, 4);
        digraph6.addEdge(4, 5);
        digraph6.addEdge(5, 3);
        digraph6.addEdge(2, 3);
        digraph6.addEdge(1, 0);

        StdOut.println("Reachable 6: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex(digraph6)
                + " Expected: true");
        StdOut.println("Reachable 6: " + reachableVertexInDigraph.hasVertexReachableFromEveryOtherVertex2(digraph6)
                + " Expected: true");
    }

}
