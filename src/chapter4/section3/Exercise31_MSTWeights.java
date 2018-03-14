package chapter4.section3;

import chapter1.section3.Queue;
import chapter1.section5.UnionFind;
import chapter2.section4.IndexMinPriorityQueue;
import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 11/11/17.
 */
public class Exercise31_MSTWeights {

    public class LazyPrimMSTWeight {

        private boolean[] marked; // minimum spanning tree vertices
        private Queue<Edge> minimumSpanningTree;
        private PriorityQueueResize<Edge> priorityQueue; // crossing (and ineligible) edges

        private double weight;

        public LazyPrimMSTWeight(EdgeWeightedGraph edgeWeightedGraph) {
            priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);
            marked = new boolean[edgeWeightedGraph.vertices()];
            minimumSpanningTree = new Queue<>();

            visit(edgeWeightedGraph, 0); // assumes the graph is connected

            while (!priorityQueue.isEmpty()) {
                Edge edge = priorityQueue.deleteTop(); // Get lowest-weight edge from priority queue
                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                // Skip if ineligible
                if (marked[vertex1] && marked[vertex2]) {
                    continue;
                }

                // Add edge to the minimum spanning tree
                minimumSpanningTree.enqueue(edge);
                weight += edge.weight();

                // Add vertex to the minimum spanning tree
                if (!marked[vertex1]) {
                    visit(edgeWeightedGraph, vertex1);
                }
                if (!marked[vertex2]) {
                    visit(edgeWeightedGraph, vertex2);
                }
            }
        }

        private void visit(EdgeWeightedGraph edgeWeightedGraph, int vertex) {
            // Mark vertex and add to priority queue all edges from vertex to unmarked vertices
            marked[vertex] = true;

            for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                if (!marked[edge.other(vertex)]) {
                    priorityQueue.insert(edge);
                }
            }
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }
    }

    public class PrimMSTWeight {

        private Edge[] edgeTo; // shortest edge from tree vertex
        private double[] distTo; // distTo[vertex] = edgeTo[vertex].weight()
        private boolean[] marked; // true if vertex is on the minimum spanning tree
        private IndexMinPriorityQueue<Double> priorityQueue; // eligible crossing edges

        private double weight;

        public PrimMSTWeight(EdgeWeightedGraph edgeWeightedGraph) {
            edgeTo = new Edge[edgeWeightedGraph.vertices()];
            distTo = new double[edgeWeightedGraph.vertices()];
            marked = new boolean[edgeWeightedGraph.vertices()];

            for(int vertex = 0; vertex < edgeWeightedGraph.vertices(); vertex++) {
                distTo[vertex] = Double.POSITIVE_INFINITY;
            }
            priorityQueue = new IndexMinPriorityQueue<>(edgeWeightedGraph.vertices());

            // Initialize priority queue with 0, weight 0
            distTo[0] = 0;
            priorityQueue.insert(0, 0.0);

            while (!priorityQueue.isEmpty()) {
                visit(edgeWeightedGraph, priorityQueue.deleteMin()); // Add closest vertex to the minimum spanning tree
            }
        }

        private void visit(EdgeWeightedGraph edgeWeightedGraph, int vertex) {
            // Add vertex to the minimum spanning tree; update data structures
            marked[vertex] = true;

            for(Edge edge : edgeWeightedGraph.adjacent(vertex)) {
                int otherVertex = edge.other(vertex);
                if (marked[otherVertex]) {
                    continue; // vertex-otherVertex is ineligible
                }

                if (edge.weight() < distTo[otherVertex]) {
                    // Edge edge is the new best connection from the minimum spanning tree to otherVertex
                    if (distTo[otherVertex] != Double.POSITIVE_INFINITY) {
                        weight -= distTo[otherVertex];
                    }
                    weight += edge.weight();

                    edgeTo[otherVertex] = edge;
                    distTo[otherVertex] = edge.weight();

                    if (priorityQueue.contains(otherVertex)) {
                        priorityQueue.decreaseKey(otherVertex, distTo[otherVertex]);
                    } else {
                        priorityQueue.insert(otherVertex, distTo[otherVertex]);
                    }
                }
            }
        }

        public Iterable<Edge> edges() {
            Queue<Edge> minimumSpanningTree = new Queue<>();

            for(int vertex = 1; vertex < edgeTo.length; vertex++) {
                minimumSpanningTree.enqueue(edgeTo[vertex]);
            }

            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }
    }

    public class KruskalMSTWeight {

        private Queue<Edge> minimumSpanningTree;
        private double weight;

        public KruskalMSTWeight(EdgeWeightedGraph edgeWeightedGraph) {
            minimumSpanningTree = new Queue<>();
            PriorityQueueResize<Edge> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

            for(Edge edge : edgeWeightedGraph.edges()) {
                priorityQueue.insert(edge);
            }

            UnionFind unionFind = new UnionFind(edgeWeightedGraph.vertices());

            while (!priorityQueue.isEmpty() && minimumSpanningTree.size() < edgeWeightedGraph.vertices() - 1) {
                Edge edge = priorityQueue.deleteTop(); // Get lowest-weight edge from priority queue
                int vertex1 = edge.either();
                int vertex2 = edge.other(vertex1);

                // Ignore ineligible edges
                if (unionFind.connected(vertex1, vertex2)) {
                    continue;
                }

                unionFind.union(vertex1, vertex2);
                minimumSpanningTree.enqueue(edge); // Add edge to the minimum spanning tree

                weight += edge.weight();
            }
        }

        public Iterable<Edge> edges() {
            return minimumSpanningTree;
        }

        public double lazyWeight() {
            double weight = 0;

            for(Edge edge : edges()) {
                weight += edge.weight();
            }

            return weight;
        }

        public double eagerWeight() {
            return weight;
        }

    }

    public static void main(String[] args) {
        Exercise31_MSTWeights mstWeights = new Exercise31_MSTWeights();

        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(5);
        edgeWeightedGraph.addEdge(new Edge(0, 1, 0.42));
        edgeWeightedGraph.addEdge(new Edge(0, 3, 0.5));
        edgeWeightedGraph.addEdge(new Edge(1, 2, 0.12));
        edgeWeightedGraph.addEdge(new Edge(1, 4, 0.91));
        edgeWeightedGraph.addEdge(new Edge(2, 3, 0.72));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.8));
        edgeWeightedGraph.addEdge(new Edge(3, 4, 0.82));
        edgeWeightedGraph.addEdge(new Edge(4, 4, 0.1));

        Exercise31_MSTWeights.LazyPrimMSTWeight lazyPrimMSTWeight =
                mstWeights.new LazyPrimMSTWeight(edgeWeightedGraph);

        Exercise31_MSTWeights.PrimMSTWeight primMSTWeight =
                mstWeights.new PrimMSTWeight(edgeWeightedGraph);

        Exercise31_MSTWeights.KruskalMSTWeight kruskalMSTWeight =
                mstWeights.new KruskalMSTWeight(edgeWeightedGraph);

        StdOut.println("Expected MST weight: 1.84\n");
        StdOut.println("Lazy Prim MST lazy weight: " + lazyPrimMSTWeight.lazyWeight());
        StdOut.println("Lazy Prim MST eager weight: " + lazyPrimMSTWeight.eagerWeight());

        StdOut.println("Eager Prim MST lazy weight: " + primMSTWeight.lazyWeight());
        StdOut.println("Eager Prim MST eager weight: " + primMSTWeight.eagerWeight());

        StdOut.println("Kruskal MST lazy weight: " + kruskalMSTWeight.lazyWeight());
        StdOut.println("Kruskal MST eager weight: " + kruskalMSTWeight.eagerWeight());
    }

}
