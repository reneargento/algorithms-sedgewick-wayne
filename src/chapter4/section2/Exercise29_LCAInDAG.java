package chapter4.section2;

import chapter1.section3.Queue;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/10/17.
 */
public class Exercise29_LCAInDAG {

    private Digraph digraph;
    private int[] maxDistances;

    //Preprocess to
    // 1- Find all sources in the digraph
    // 2- Compute the height of all vertices (max distance from any source)
    // O(S * (V + E)) where S is the number of sources = O(VE)
    public Exercise29_LCAInDAG(Digraph digraph) {
        this.digraph = digraph;
        maxDistances = new int[digraph.vertices()];
        HashSet<Integer> sources = new HashSet<>();

        // 1- Find the sources in the graph
        int[] indegrees = new int[digraph.vertices()];

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            for(int neighbor : digraph.adjacent(vertex)) {
                indegrees[neighbor]++;
            }
        }

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (indegrees[vertex] == 0) {
                sources.add(vertex);
            }
        }

        // 2- Find the height of all vertices (the length of the longest distance from a source)
        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            maxDistances[vertex] = -1;
        }

        for(int source : sources.keys()) {
            int[] distanceFromCurrentSource = new int[digraph.vertices()];

            for(int vertex = 0; vertex < distanceFromCurrentSource.length; vertex++) {
                distanceFromCurrentSource[vertex] = Integer.MAX_VALUE;
            }

            Queue<Integer> sourceDistanceQueue = new Queue<>();
            sourceDistanceQueue.enqueue(source);
            distanceFromCurrentSource[source] = 0;

            if (distanceFromCurrentSource[source] > maxDistances[source]) {
                maxDistances[source] = distanceFromCurrentSource[source];
            }

            while (!sourceDistanceQueue.isEmpty()) {
                int currentVertex = sourceDistanceQueue.dequeue();

                for(int neighbor : digraph.adjacent(currentVertex)) {
                    distanceFromCurrentSource[neighbor] = distanceFromCurrentSource[currentVertex] + 1;
                    sourceDistanceQueue.enqueue(neighbor);

                    if (distanceFromCurrentSource[neighbor] > maxDistances[neighbor]) {
                        maxDistances[neighbor] = distanceFromCurrentSource[neighbor];
                    }
                }
            }
        }
    }

    //O(V + E)
    public int getLCA(int vertex1, int vertex2) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        // 1- Reverse graph
        Digraph reverseDigraph = digraph.reverse();

        // 2- Do a BFS from vertex1 to find all its ancestors
        HashSet<Integer> vertex1Ancestors = new HashSet<>();

        Queue<Integer> queue = new Queue<>();
        queue.enqueue(vertex1);

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            vertex1Ancestors.add(currentVertex);

            for(int neighbor : reverseDigraph.adjacent(currentVertex)) {
                queue.enqueue(neighbor);
            }
        }

        // 3- Do a BFS from vertex2 to find all its ancestors and see which ones are common ancestors to vertex1
        HashSet<Integer> commonAncestors = new HashSet<>();

        queue.enqueue(vertex2);

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            if (vertex1Ancestors.contains(currentVertex)) {
                commonAncestors.add(currentVertex);
            }

            for(int neighbor : reverseDigraph.adjacent(currentVertex)) {
                queue.enqueue(neighbor);
            }
        }

        // 4- Find the height of all common ancestors (the length of the longest distance from a source)
        // The common ancestor with greatest height is an LCA of vertex1 and vertex2
        int maxDistance = -1;
        int lowestCommonAncestor = -1;

        for(int commonAncestor : commonAncestors.keys()) {
            if (maxDistances[commonAncestor] > maxDistance) {
                maxDistance = maxDistances[commonAncestor];
                lowestCommonAncestor = commonAncestor;
            }
        }

        return lowestCommonAncestor;
    }

    public static void main(String[] args) {
        Digraph digraph1 = new Digraph(5);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(0, 3);
        digraph1.addEdge(3, 4);

        Exercise29_LCAInDAG lcaInDAG1 = new Exercise29_LCAInDAG(digraph1);
        int lca1 = lcaInDAG1.getLCA(2, 4);
        if (lca1 == -1) {
            StdOut.print("LCA in digraph 1: There is no LCA in this DAG");
        } else {
            StdOut.print("LCA in digraph 1: " + lca1);
        }
        StdOut.println(" Expected: 0");

        Digraph digraph2 = new Digraph(5);
        digraph2.addEdge(0, 1);
        digraph2.addEdge(0, 2);
        digraph2.addEdge(2, 3);
        digraph2.addEdge(2, 4);

        Exercise29_LCAInDAG lcaInDAG2 = new Exercise29_LCAInDAG(digraph2);
        int lca2 = lcaInDAG2.getLCA(3, 4);
        if (lca2 == -1) {
            StdOut.print("LCA in digraph 2: There is no LCA in this DAG");
        } else {
            StdOut.print("LCA in digraph 2: " + lca2);
        }
        StdOut.println(" Expected: 2");


        Digraph digraph3 = new Digraph(9);
        digraph3.addEdge(0, 1);
        digraph3.addEdge(1, 2);
        digraph3.addEdge(1, 3);

        digraph3.addEdge(4, 5);
        digraph3.addEdge(5, 6);
        digraph3.addEdge(6, 8);
        digraph3.addEdge(6, 7);
        digraph3.addEdge(7, 2);
        digraph3.addEdge(8, 3);

        Exercise29_LCAInDAG lcaInDAG3 = new Exercise29_LCAInDAG(digraph3);
        int lca3 = lcaInDAG3.getLCA(2, 3);
        if (lca3 == -1) {
            StdOut.print("LCA in digraph 3: There is no LCA in this DAG");
        } else {
            StdOut.print("LCA in digraph 3: " + lca3);
        }
        StdOut.println(" Expected: 6");


        Digraph digraph4 = new Digraph(9);
        digraph4.addEdge(0, 1);
        digraph4.addEdge(1, 3);
        digraph4.addEdge(1, 4);
        digraph4.addEdge(4, 5);
        digraph4.addEdge(5, 6);
        digraph4.addEdge(6, 2);

        digraph4.addEdge(7, 8);
        digraph4.addEdge(8, 3);
        digraph4.addEdge(7, 2);

        Exercise29_LCAInDAG lcaInDAG4 = new Exercise29_LCAInDAG(digraph4);
        int lca4 = lcaInDAG4.getLCA(2, 3);
        if (lca4 == -1) {
            StdOut.print("LCA in digraph 4: There is no LCA in this DAG");
        } else {
            StdOut.print("LCA in digraph 4: " + lca4);
        }
        StdOut.println(" Expected: 1");


        Digraph digraph5 = new Digraph(4);
        digraph5.addEdge(0, 1);
        digraph5.addEdge(1, 2);

        Exercise29_LCAInDAG lcaInDAG5 = new Exercise29_LCAInDAG(digraph5);
        int lca5 = lcaInDAG5.getLCA(2, 3);
        if (lca5 == -1) {
            StdOut.print("LCA in digraph 5: There is no LCA in this DAG");
        } else {
            StdOut.print("LCA in digraph 5: " + lca5);
        }
        StdOut.println(" Expected: There is no LCA in this DAG");
    }

}
