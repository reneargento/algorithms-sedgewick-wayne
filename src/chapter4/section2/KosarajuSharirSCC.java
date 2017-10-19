package chapter4.section2;

/**
 * Created by rene on 18/10/17.
 */
public class KosarajuSharirSCC {

    private boolean[] visited; // reached vertices
    private int[] id; // component identifiers
    private int count; // number of strong components

    public KosarajuSharirSCC(Digraph digraph) {
        visited = new boolean[digraph.vertices()];
        id = new int[digraph.vertices()];

        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(digraph.reverse());

        for(int vertex : depthFirstOrder.reversePostOrder()) {
            if(!visited[vertex]) {
                dfs(digraph, vertex);
                count++;
            }
        }
    }

    private void dfs(Digraph digraph, int vertex) {
        visited[vertex] = true;
        id[vertex] = count;

        for(int neighbor : digraph.adjacent(vertex)) {
            if(!visited[neighbor]) {
                dfs(digraph, neighbor);
            }
        }
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
