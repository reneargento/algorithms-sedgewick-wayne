package chapter4.section2;

/**
 * Created by Rene Argento on 18/10/17.
 */
public class TransitiveClosure {

    private DirectedDFS[] allVerticesDFS;

    public TransitiveClosure(Digraph digraph) {
        allVerticesDFS = new DirectedDFS[digraph.vertices()];

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            allVerticesDFS[vertex] = new DirectedDFS(digraph, vertex);
        }
    }

    public boolean reachable(int vertex1, int vertex2) {
        return allVerticesDFS[vertex1].marked(vertex2);
    }

}
