package chapter4.section4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 30/12/17.
 */
@SuppressWarnings("unchecked")
public class KosarajuSharirSCCWeighted {

    private boolean[] visited; // reached vertices
    private int[] id;          // component identifiers
    private int count;         // number of strong components

    private EdgeWeightedDigraphInterface edgeWeightedDigraph;

    public KosarajuSharirSCCWeighted(EdgeWeightedDigraphInterface edgeWeightedDigraph) {
        visited = new boolean[edgeWeightedDigraph.vertices()];
        id = new int[edgeWeightedDigraph.vertices()];
        this.edgeWeightedDigraph = edgeWeightedDigraph;

        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(edgeWeightedDigraph.reverse());

        for(int vertex : depthFirstOrder.reversePostOrder()) {
            if (!visited[vertex]) {
                dfs(edgeWeightedDigraph, vertex);
                count++;
            }
        }
    }

    private void dfs(EdgeWeightedDigraphInterface edgeWeightedDigraph, int vertex) {
        visited[vertex] = true;
        id[vertex] = count;

        for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
            int neighbor = edge.to();

            if (!visited[neighbor]) {
                dfs(edgeWeightedDigraph, neighbor);
            }
        }
    }

    public List<Integer>[] getSCCs() {
        List<Integer>[] stronglyConnectedComponents = (List<Integer>[]) new ArrayList[count];

        for(int scc = 0; scc < count; scc++) {
            stronglyConnectedComponents[scc] = new ArrayList<>();
        }

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            stronglyConnectedComponents[id(vertex)].add(vertex);
        }

        return stronglyConnectedComponents;
    }

    public EdgeWeightedDigraphInterface getKernelDAG() {
        EdgeWeightedDigraphInterface kernelDAG = new EdgeWeightedDigraph(count());

        for(int vertex = 0; vertex < edgeWeightedDigraph.vertices(); vertex++) {
            for(DirectedEdge edge : edgeWeightedDigraph.adjacent(vertex)) {
                int neighbor = edge.to();

                if (id(vertex) != id(neighbor)) {
                    kernelDAG.addEdge(new DirectedEdge(id(vertex), id(neighbor), edge.weight()));
                }
            }
        }

        return kernelDAG;
    }

    public boolean stronglyConnected(int vertex1, int vertex2) {
        return id[vertex1] == id[vertex2];
    }

    public int id(int vertex) {
        return id[vertex];
    }

    public int count() {
        return count;
    }

}
