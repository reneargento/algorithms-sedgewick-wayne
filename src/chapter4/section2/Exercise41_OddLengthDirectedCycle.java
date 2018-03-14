package chapter4.section2;

import chapter4.section1.OptimizedGraph;
import chapter4.section1.TwoColor;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rene Argento on 28/10/17.
 */
@SuppressWarnings("unchecked")
public class Exercise41_OddLengthDirectedCycle {

    // A digraph G has an odd-length directed cycle if and only if one (or more) of its strong components
    // (when treated as an undirected graph) is nonbipartite

    // Proof
    // If the digraph G has an odd-length directed cycle, then this cycle will be entirely contained in one of the
    // strong components. When the strong component is treated as an undirected graph, the odd-length directed cycle
    // becomes an odd-length cycle.
    // An undirected graph is bipartite if and only if it has no odd-length cycle.

    // Suppose a strong component of G is nonbipartite (when treated as an undirected graph). This means that there is
    // an odd-length cycle C in the strong component, ignoring direction.
    // If C is a directed cycle, then we are done.
    // Otherwise, if and edge v->w is pointing in the "wrong" direction, we can replace it with an odd-length path that
    // is pointing in the opposite direction (which preserves the parity of the number of edges in the cycle).
    // To see how, note that there exists a directed path P from w to v because v and w are in the same strong component.
    // If P has odd length, then we replace edge v->w by P; if P has even length, then this path P combined with
    // v->w is an odd-length cycle.
    // O(V + E)
    public boolean hasOddLengthDirectedCycle(Digraph digraph) {
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(digraph);

        Set<Integer>[] strongComponents = (Set<Integer>[]) new HashSet[kosarajuSharirSCC.count()];

        for(int scc = 0; scc < strongComponents.length; scc++) {
            strongComponents[scc] = new HashSet<>();
        }

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            int strongComponentId = kosarajuSharirSCC.id(vertex);
            strongComponents[strongComponentId].add(vertex);
        }

        for(int scc = 0; scc < strongComponents.length; scc++) {
            // Uses optimized graph to make the algorithm O(V + E) instead of O(V^2)
            // (No need to initialize all adjacency lists for the subGraphs)
            OptimizedGraph subGraph = new OptimizedGraph(digraph.vertices());

            for(int vertex : strongComponents[scc]) {
                for(int neighbor : digraph.adjacent(vertex)) {
                    if (strongComponents[scc].contains(neighbor)) {
                        subGraph.addEdge(vertex, neighbor);
                    }
                }
            }

            TwoColor twoColor = new TwoColor(subGraph);
            if (!twoColor.isBipartite()) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Exercise41_OddLengthDirectedCycle oddLengthDirectedCycle = new Exercise41_OddLengthDirectedCycle();

        Digraph digraph1 = new Digraph(4);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(2, 3);
        digraph1.addEdge(3, 0);

        StdOut.println("Has odd length directed cycle: " + oddLengthDirectedCycle.hasOddLengthDirectedCycle(digraph1)
                + " Expected: false");

        Digraph digraph2 = new Digraph(5);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(1, 2);
        digraph2.addEdge(2, 0);
        digraph2.addEdge(3, 4);

        StdOut.println("Has odd length directed cycle: " + oddLengthDirectedCycle.hasOddLengthDirectedCycle(digraph2)
                + " Expected: true");

        Digraph digraph3 = new Digraph(10);
        digraph3.addEdge(0, 1);
        digraph3.addEdge(1, 2);
        digraph3.addEdge(3, 4);
        digraph3.addEdge(4, 6);
        digraph3.addEdge(6, 8);
        digraph3.addEdge(8, 5);
        digraph3.addEdge(5, 9);
        digraph3.addEdge(9, 4);
        digraph3.addEdge(7, 0);

        StdOut.println("Has odd length directed cycle: " + oddLengthDirectedCycle.hasOddLengthDirectedCycle(digraph3)
                + " Expected: true");

        Digraph digraph4 = new Digraph(5);
        digraph4.addEdge(0, 1);
        digraph4.addEdge(1, 0);
        digraph4.addEdge(2, 3);
        digraph4.addEdge(3, 4);

        StdOut.println("Has odd length directed cycle: " + oddLengthDirectedCycle.hasOddLengthDirectedCycle(digraph4)
                + " Expected: false");
    }

}
