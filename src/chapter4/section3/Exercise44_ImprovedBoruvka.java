package chapter4.section3;

import chapter1.section3.DoublyLinkedListCircular;
import chapter1.section3.Queue;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 14/11/17.
 */
@SuppressWarnings("unchecked")
public class Exercise44_ImprovedBoruvka {

    // O(E lg V)
    public class BoruvkaMSTImproved {

        private Queue<Edge> minimumSpanningTree;
        private double weight;

        public BoruvkaMSTImproved(EdgeWeightedGraph edgeWeightedGraph) {
            minimumSpanningTree = new Queue<>();

            // Doubly circular linked list in which the first element is the subtree name / identifier
            DoublyLinkedListCircular<Integer>[] doublyLinkedListCircular =
                    (DoublyLinkedListCircular<Integer>[]) new DoublyLinkedListCircular[edgeWeightedGraph.vertices()];

            // Initialize doubly linked list
            for(int subtree = 0; subtree < edgeWeightedGraph.vertices(); subtree++) {
                doublyLinkedListCircular[subtree] = new DoublyLinkedListCircular<>();
                doublyLinkedListCircular[subtree].insertAtTheBeginning(subtree); // Name / identifier
            }

            // Repeats at most lg(V) times or until minimum spanning tree is complete
            for(int stage = 0; stage < edgeWeightedGraph.vertices(); stage = stage + stage) {
                if (minimumSpanningTree.size() == edgeWeightedGraph.vertices() - 1) {
                    break;
                }

                // For each tree in the forest, find its closest edge.
                // If edge weights are equal, ties are broken in favor of the first edge in EdgeWeightedGraph.edges()
                Edge[] closestEdges = new Edge[edgeWeightedGraph.vertices()];

                //O(E)
                for(Edge edge : edgeWeightedGraph.edges()) {
                    int vertex1 = edge.either();
                    int vertex2 = edge.other(vertex1);

                    int treeIdentifier1 = doublyLinkedListCircular[vertex1].first().item;
                    int treeIdentifier2 = doublyLinkedListCircular[vertex2].first().item;

                    // Check if vertices are part of the same tree
                    if (treeIdentifier1 == treeIdentifier2) {
                        continue;
                    }

                    if (closestEdges[treeIdentifier1] == null || edge.weight() < closestEdges[treeIdentifier1].weight()) {
                        closestEdges[treeIdentifier1] = edge;
                    }
                    if (closestEdges[treeIdentifier2] == null || edge.weight() < closestEdges[treeIdentifier2].weight()) {
                        closestEdges[treeIdentifier2] = edge;
                    }
                }

                // Add newly discovered edges to the MST
                // At most O(V) total subtree merges and renames per stage
                for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                    Edge closestEdge = closestEdges[vertex];

                    if (closestEdge != null) {
                        int vertex1 = closestEdge.either();
                        int vertex2 = closestEdge.other(vertex1);

                        int treeIdentifier1 = doublyLinkedListCircular[vertex1].first().item;
                        int treeIdentifier2 = doublyLinkedListCircular[vertex2].first().item;

                        if (treeIdentifier1 != treeIdentifier2) {
                            minimumSpanningTree.enqueue(closestEdge);
                            weight += closestEdge.weight();

                            // Merge subtrees and update references for all elements in the smallest subtree.
                            // The largest subtree elements will be updated automatically because they are pointing to the
                            // merged subtree.
                            int subsTree1Size = doublyLinkedListCircular[vertex1].size();
                            int subsTree2Size = doublyLinkedListCircular[vertex2].size();

                            HashSet<Integer> elementsToUpdate = new HashSet<>();

                            if (subsTree1Size <= subsTree2Size) {
                                for(int element : doublyLinkedListCircular[vertex1]) {
                                    elementsToUpdate.add(element);
                                }

                                doublyLinkedListCircular[vertex2].insertLinkedListAtTheEnd(doublyLinkedListCircular[vertex1]);

                                for(int element : elementsToUpdate.keys()) {
                                    doublyLinkedListCircular[element] = doublyLinkedListCircular[vertex2];
                                }
                            } else {
                                for(int element : doublyLinkedListCircular[vertex2]) {
                                    elementsToUpdate.add(element);
                                }

                                doublyLinkedListCircular[vertex1].insertLinkedListAtTheEnd(doublyLinkedListCircular[vertex2]);

                                for(int element : elementsToUpdate.keys()) {
                                    doublyLinkedListCircular[element] = doublyLinkedListCircular[vertex1];
                                }
                            }
                        }
                    }
                }
            }
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double weight() {
            return weight;
        }

    }

    public static void main(String[] args) {
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph.addEdge(new Edge(4, 4, 0.1));

        Exercise44_ImprovedBoruvka.BoruvkaMSTImproved boruvkaMSTImproved =
                new Exercise44_ImprovedBoruvka().new BoruvkaMSTImproved(edgeWeightedGraph);

        for(Edge edge : boruvkaMSTImproved.edges()) {
            StdOut.println(edge);
        }

        StdOut.println("\nExpected:\n" +
                "0-1 0.42000\n" +
                "1-2 0.12000\n" +
                "0-3 0.50000\n" +
                "3-4 0.80000");
    }

}
