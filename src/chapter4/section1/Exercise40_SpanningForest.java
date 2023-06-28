package chapter4.section1;

import chapter1.section3.Bag;
import chapter3.section1.SequentialSearchSymbolTable;

public class Exercise40_SpanningForest {

    public static void main(String[] args) {
        Graph graph1 = new Graph(10);
        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        graph1.addEdge(3, 4);
        graph1.addEdge(4, 0);
        graph1.addEdge(4, 5);
        graph1.addEdge(5, 6);
        graph1.addEdge(6, 7);
        graph1.addEdge(7, 8);
        graph1.addEdge(8, 3);
        graph1.addEdge(3, 9);
        graph1.addEdge(7, 9);

        SpanningForest spanningForest1 = new SpanningForest(graph1);
        Graph spanningTree1 = spanningForest1.spanningForest();
        System.out.println(spanningTree1);


        Graph graph2 = new Graph(11);
        // 1st component
        graph2.addEdge(0, 1);
        graph2.addEdge(0, 2);
        graph2.addEdge(1, 3);
        graph2.addEdge(3, 4);
        graph2.addEdge(2, 4);
        graph2.addEdge(2, 5);
        graph2.addEdge(4, 5);

        // 2nd component
        graph2.addEdge(6, 7);
        graph2.addEdge(6, 8);
        graph2.addEdge(7, 8);
        graph2.addEdge(7, 9);
        graph2.addEdge(8, 9);
        graph2.addEdge(8, 10);
        graph2.addEdge(9, 10);


        SpanningForest spanningForest2 = new SpanningForest(graph2);
        Graph spanningTree2 = spanningForest2.spanningForest();
        System.out.println(spanningTree2);
    }

    private static class SpanningForest {

        private final boolean[] marked;
        private Graph spanningForest;

        public SpanningForest(Graph graph) {
            this.marked = new boolean[graph.vertices()];

            computeSpanningForest(graph);
        }

        private void computeSpanningForest(Graph graph) {
            SequentialSearchSymbolTable<Integer, Bag<Integer>> spanningTreeVertices = new SequentialSearchSymbolTable<>();

            for (int s = 0; s < graph.vertices(); ++s) {
                if (!marked[s]) {
                    dfs(graph, s, s, spanningTreeVertices);
                }
            }

            this.spanningForest = makeSpanningTree(spanningTreeVertices);
        }

        private void dfs(Graph graph, int parent, int v, SequentialSearchSymbolTable<Integer, Bag<Integer>> spanningTreeVertices) {
            marked[v] = true;

            if (!spanningTreeVertices.contains(parent)) {
                spanningTreeVertices.put(parent, new Bag<>());
            }
            spanningTreeVertices.get(parent).add(v);

            for (int w : graph.adjacent(v)) {
                if (!marked[w]) {
                    dfs(graph, v, w, spanningTreeVertices);
                }
            }
        }

        private Graph makeSpanningTree(SequentialSearchSymbolTable<Integer, Bag<Integer>> spanningTreeVertices) {
            int maxIndex = 0;
            for (int v : spanningTreeVertices.keys()) {
                if (v > maxIndex) {
                    maxIndex = v;
                }
            }

            Graph spanningTree = new Graph(maxIndex + 1);
            for (int v : spanningTreeVertices.keys()) {
                Bag<Integer> neighbors = spanningTreeVertices.get(v);
                for (int w : neighbors) {
                    spanningTree.addEdge(v, w);
                }
            }

            return spanningTree;
        }

        public Graph spanningForest() {
            return spanningForest;
        }

    }

}
