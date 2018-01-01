package chapter4.section1;

import chapter1.section5.WeightedQuickUnion;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 16/09/17.
 */
public class Exercise8 {

    public class SearchUnionFind {

        private WeightedQuickUnion weightedQuickUnion;
        private int sourceVertex;

        SearchUnionFind(Graph graph, int sourceVertex) {
            weightedQuickUnion = new WeightedQuickUnion(graph.vertices());
            this.sourceVertex = sourceVertex;

            for(int vertex = 0; vertex < graph.vertices(); vertex++) {
                for(int neighbor : graph.adjacent(vertex)) {
                    weightedQuickUnion.union(vertex, neighbor);
                }
            }
        }

        public boolean marked(int vertex) {
            return weightedQuickUnion.connected(sourceVertex, vertex);
        }

        public int count() {
            int sourceVertexLeader = weightedQuickUnion.find(sourceVertex);
            return weightedQuickUnion.getSizes()[sourceVertexLeader];
        }

    }

    public static void main(String[] args) {
        Exercise8 exercise8 = new Exercise8();

        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        SearchUnionFind searchUnionFind = exercise8.new SearchUnionFind(graph, 1);
        StdOut.println("Count: " + searchUnionFind.count() + " Expected: 4");
        StdOut.println("Connected to 0: " + searchUnionFind.marked(0) + " Expected: true");
        StdOut.println("Connected to 1: " + searchUnionFind.marked(1) + " Expected: true");
        StdOut.println("Connected to 2: " + searchUnionFind.marked(2) + " Expected: true");
        StdOut.println("Connected to 3: " + searchUnionFind.marked(3) + " Expected: true");
        StdOut.println("Connected to 4: " + searchUnionFind.marked(4) + " Expected: false");
    }

}
