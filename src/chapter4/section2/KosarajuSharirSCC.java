package chapter4.section2;

/**
 * Created by Rene Argento on 18/10/17.
 */
public class KosarajuSharirSCC {

    private boolean[] visited; // reached vertices
    private int[] id; // component identifiers
    private int count; // number of strong components

    private DigraphInterface digraph;

    public KosarajuSharirSCC(DigraphInterface digraph) {
        visited = new boolean[digraph.vertices()];
        id = new int[digraph.vertices()];
        this.digraph = digraph;

        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(digraph.reverse());

        for(int vertex : depthFirstOrder.reversePostOrder()) {
            if (!visited[vertex]) {
                dfs(digraph, vertex);
                count++;
            }
        }
    }

    private void dfs(DigraphInterface digraph, int vertex) {
        visited[vertex] = true;
        id[vertex] = count;

        for(int neighbor : digraph.adjacent(vertex)) {
            if (!visited[neighbor]) {
                dfs(digraph, neighbor);
            }
        }
    }

    public Digraph getKernelDAG() {
        Digraph kernelDAG = new Digraph(count());

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            for(int neighbor : digraph.adjacent(vertex)) {
                if (id(vertex) != id(neighbor)) {
                    kernelDAG.addEdge(id(vertex), id(neighbor));
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
