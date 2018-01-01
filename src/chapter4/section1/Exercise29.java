package chapter4.section1;

import chapter1.section3.Bag;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 21/09/17.
 */
//The original Cycle algorithm considers parallel edges and self-loops as cycles
    //This version is modified to do not consider parallel edges and self-loops as cycles
@SuppressWarnings("unchecked")
public class Exercise29 {

    public class CycleThatDoesNotCountParallelEdgesOrSelfLoops {

        private boolean[] visited;
        private boolean hasCycle;

        public CycleThatDoesNotCountParallelEdgesOrSelfLoops(Graph graph) {
            visited = new boolean[graph.vertices()];

            //Pre-processing to remove parallel edges and self-loops
            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                Bag<Integer> newAdjacencyList = new Bag();
                HashSet<Integer> verticesInNewAdjacencyList = new HashSet<>();

                for(int neighbor : graph.getAdjacencyList()[vertex]) {
                    if(!verticesInNewAdjacencyList.contains(neighbor) && neighbor != vertex) {
                        verticesInNewAdjacencyList.add(neighbor);
                        newAdjacencyList.add(neighbor);
                    }
                }

                graph.updateAdjacencyList(vertex, newAdjacencyList);
            }

            for(int source = 0; source < graph.vertices(); source++) {
                if(!visited[source]) {
                    dfs(graph, source, source);
                }
            }
        }

        private void dfs(Graph graph, int vertex, int origin) {
            visited[vertex] = true;

            for(int neighbor : graph.adjacent(vertex)) {
                if(!visited[neighbor]) {
                    dfs(graph, neighbor, vertex);
                } else if(neighbor != origin) {
                    hasCycle = true;
                }
            }
        }

        public boolean hasCycle() {
            return hasCycle;
        }

    }

    public static void main(String[] args) {
        Exercise29 exercise29 = new Exercise29();

        Graph graphWithSimpleCycle = new Graph(4);
        graphWithSimpleCycle.addEdge(0, 1);
        graphWithSimpleCycle.addEdge(1, 2);
        graphWithSimpleCycle.addEdge(2, 3);
        graphWithSimpleCycle.addEdge(3, 0);

        CycleThatDoesNotCountParallelEdgesOrSelfLoops cycle1 =
                exercise29.new CycleThatDoesNotCountParallelEdgesOrSelfLoops(graphWithSimpleCycle);
        StdOut.println("Has cycle (simple cycle): " + cycle1.hasCycle() + " Expected: true");

        Graph graphWithParallelEdges = new Graph(4);
        graphWithParallelEdges.addEdge(0, 1);
        graphWithParallelEdges.addEdge(1, 2);
        graphWithParallelEdges.addEdge(2, 1);
        graphWithParallelEdges.addEdge(2, 3);

        CycleThatDoesNotCountParallelEdgesOrSelfLoops cycle2 =
                exercise29.new CycleThatDoesNotCountParallelEdgesOrSelfLoops(graphWithParallelEdges);
        StdOut.println("Has cycle (graph with parallel edges): " + cycle2.hasCycle() + " Expected: false");

        Graph graphWithSelfLoop = new Graph(4);
        graphWithSelfLoop.addEdge(0, 1);
        graphWithSelfLoop.addEdge(1, 2);
        graphWithSelfLoop.addEdge(2, 3);
        graphWithSelfLoop.addEdge(3, 3);

        CycleThatDoesNotCountParallelEdgesOrSelfLoops cycle3 =
                exercise29.new CycleThatDoesNotCountParallelEdgesOrSelfLoops(graphWithSelfLoop);
        StdOut.println("Has cycle (graph with self-loop): " + cycle3.hasCycle() + " Expected: false");
    }

}
