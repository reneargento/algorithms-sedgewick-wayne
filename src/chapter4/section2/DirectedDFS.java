package chapter4.section2;

import chapter1.section3.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/10/17.
 */
public class DirectedDFS {

    private boolean[] visited;

    public DirectedDFS(Digraph digraph, int source) {
        visited = new boolean[digraph.vertices()];
        dfs(digraph, source);
    }

    public DirectedDFS(Digraph digraph, Iterable<Integer> sources) {
        visited = new boolean[digraph.vertices()];

        for(int source : sources) {
            if (!visited[source]) {
                dfs(digraph, source);
            }
        }
    }

    private void dfs(Digraph digraph, int source) {
        visited[source] = true;

        for(int neighbor : digraph.adjacent(source)) {
            if (!visited[neighbor]) {
                dfs(digraph, neighbor);
            }
        }
    }

    public boolean marked(int vertex) {
        return visited[vertex];
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));

        Bag<Integer> sources = new Bag<>();
        for(int i = 1; i < args.length; i++) {
            sources.add(Integer.parseInt(args[i]));
        }

        DirectedDFS reachable = new DirectedDFS(digraph, sources);

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (reachable.visited[vertex]) {
                StdOut.print(vertex + " ");
            }
        }

        StdOut.println();
    }
}
