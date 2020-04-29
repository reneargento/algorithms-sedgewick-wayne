package chapter4.section2;

import chapter1.section3.Bag;
import chapter1.section3.Stack;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 02/11/17.
 */
// The data source is a file containing vertices representing high energy phenomenology papers
// and edges representing citations from one paper to another.
// This data is part of the online research paper repository arXiv and was used as part of KDD Cup 2003.
// It can be downloaded here: http://www.cs.cornell.edu/projects/kddcup/datasets.html
// The idea of removing future or self-references to maintain the graph as a DAG and avoid cycles was taken from this paper:
// https://academic.oup.com/comnet/article-lookup/doi/10.1093/comnet/cnu039
@SuppressWarnings("unchecked")
public class Exercise51_RealWorldDAG {

    private class Digraph {
        private int vertices;
        private int edges;
        private SeparateChainingHashTable<Integer, Bag<Integer>> adjacent;
        private SeparateChainingHashTable<Integer, Integer> indegrees;
        private SeparateChainingHashTable<Integer, Integer> outdegrees;

        public Digraph() {
            vertices = 0;
            edges = 0;

            indegrees = new SeparateChainingHashTable<>();
            outdegrees = new SeparateChainingHashTable<>();

            adjacent = new SeparateChainingHashTable<>();
        }

        public Digraph(In in) {
            this();
            int edges = in.readInt();

            for(int i = 0; i < edges; i++) {
                int vertex1 = in.readInt();
                int vertex2 = in.readInt();
                addEdge(vertex1, vertex2);
            }
        }

        public int vertices() {
            return vertices;
        }

        public int edges() {
            return edges;
        }

        public void addVertex(int vertex) {
            if (!adjacent.contains(vertex)) {
                adjacent.put(vertex, new Bag<>());
                vertices++;
            }
        }

        public void addEdge(int vertex1, int vertex2) {
            if (!adjacent.contains(vertex1)) {
                adjacent.put(vertex1, new Bag<>());
                vertices++;
            }
            if (!adjacent.contains(vertex2)) {
                adjacent.put(vertex2, new Bag<>());
                vertices++;
            }

            adjacent.get(vertex1).add(vertex2);
            edges++;

            if (!outdegrees.contains(vertex1)) {
                outdegrees.put(vertex1, 0);
            }
            int currentVertex1Outdegree = outdegrees.get(vertex1);
            currentVertex1Outdegree++;
            outdegrees.put(vertex1, currentVertex1Outdegree);

            if (!indegrees.contains(vertex2)) {
                indegrees.put(vertex2, 0);
            }
            int currentVertex2Indegree = indegrees.get(vertex2);
            currentVertex2Indegree++;
            indegrees.put(vertex2, currentVertex2Indegree);
        }

        public Iterable<Integer> adjacent(int vertex) {
            return adjacent.get(vertex);
        }

        public int indegree(int vertex) {
            return indegrees.get(vertex);
        }

        public int outdegree(int vertex) {
            return outdegrees.get(vertex);
        }

        public Digraph reverse() {
            Digraph reverse = new Digraph();

            for(int vertex = 0; vertex < vertices; vertex++) {
                for(int neighbor : adjacent(vertex)) {
                    reverse.addEdge(neighbor, vertex);
                }
            }
            return reverse;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                if (adjacent(vertex) != null) {
                    for(int neighbor : adjacent(vertex)) {
                        stringBuilder.append(neighbor).append(" ");
                    }
                }
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
    }

    private class DirectedEdge {
        private int fromVertex;
        private int toVertex;

        DirectedEdge(int fromVertex, int toVertex) {
            this.fromVertex = fromVertex;
            this.toVertex = toVertex;
        }
    }

    private class DirectedCycle {

        private boolean visited[];
        private int[] edgeTo;
        private Stack<Integer> cycle; // vertices on  a cycle (if one exists)
        private boolean[] onStack; // vertices on recursive call stack

        public DirectedCycle(Digraph digraph) {
            onStack = new boolean[digraph.vertices()];
            edgeTo = new int[digraph.vertices()];
            visited = new boolean[digraph.vertices()];

            for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
                if (!visited[vertex]) {
                    dfs(digraph, vertex);
                }
            }
        }

        private void dfs(Digraph digraph, int vertex) {
            onStack[vertex] = true;
            visited[vertex] = true;

            if (digraph.adjacent(vertex) != null) {
                for(int neighbor : digraph.adjacent(vertex)) {
                    if (hasCycle()) {
                        return;
                    } else if (!visited[neighbor]) {
                        edgeTo[neighbor] = vertex;
                        dfs(digraph, neighbor);
                    } else if (onStack[neighbor]) {
                        cycle = new Stack<>();

                        for(int currentVertex = vertex; currentVertex != neighbor; currentVertex = edgeTo[currentVertex]) {
                            cycle.push(currentVertex);
                        }

                        cycle.push(neighbor);
                        cycle.push(vertex);
                    }
                }
            }
            onStack[vertex] = false;
        }

        public boolean hasCycle() {
            return cycle != null;
        }

        public Iterable<Integer> cycle() {
            return cycle;
        }
    }

    private Digraph randomRealDAG(int randomVerticesToChoose, int randomEdgesToChoose) {
        Digraph fullDigraph = new Digraph();
        SeparateChainingHashTable<Integer, Integer> realDAGToFullDAGMap =
                new SeparateChainingHashTable<>();

        readDAG(fullDigraph, realDAGToFullDAGMap);

        DirectedCycle directedCycle = new DirectedCycle(fullDigraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph is not a DAG");
        }

        Digraph randomSubDigraph = new Digraph();
        SeparateChainingHashTable<Integer, Integer> digraphToSubDigraphMap =
                new SeparateChainingHashTable<>();
        SeparateChainingHashTable<Integer, Integer> subDigraphToDigraphMap =
                new SeparateChainingHashTable<>();

        List<DirectedEdge> allSubDigraphEdges = new ArrayList<>();

        while (digraphToSubDigraphMap.size() < randomVerticesToChoose) {
            // Randomly choose a vertex between 1 and vertices
            int randomVertexId = 1 + StdRandom.uniform(fullDigraph.vertices);

            if (digraphToSubDigraphMap.contains(randomVertexId)) {
                continue;
            }

            int subDigraphVertexId1 = digraphToSubDigraphMap.size();
            digraphToSubDigraphMap.put(randomVertexId, subDigraphVertexId1);
            subDigraphToDigraphMap.put(subDigraphVertexId1, randomVertexId);

            randomSubDigraph.addVertex(subDigraphVertexId1);

            // Outward edges
            for(int neighbor : fullDigraph.adjacent(randomVertexId)) {
                if (digraphToSubDigraphMap.contains(neighbor)) {
                    int subDigraphVertexId2 = digraphToSubDigraphMap.get(neighbor);
                    allSubDigraphEdges.add(new DirectedEdge(subDigraphVertexId1, subDigraphVertexId2));
                }
            }
            // Inward edges
            for(int subDigraphVertexId = 0; subDigraphVertexId < randomSubDigraph.vertices(); subDigraphVertexId++) {
                int fullDigraphVertexId = subDigraphToDigraphMap.get(subDigraphVertexId);

                for(int neighbor : fullDigraph.adjacent(fullDigraphVertexId)) {
                    if (neighbor == randomVertexId) {
                        allSubDigraphEdges.add(new DirectedEdge(subDigraphVertexId, subDigraphVertexId1));
                    }
                }
            }
        }

        // Randomly choose E directed edges from the subdigraph induced by the random vertices
        if (randomEdgesToChoose > allSubDigraphEdges.size()) {
            throw new IllegalArgumentException("Not enough edges to choose");
        }

        // Randomly choose edges
        for(int edgeIndex = 0; edgeIndex < randomEdgesToChoose; edgeIndex++) {
            int randomEdgeId = StdRandom.uniform(edgeIndex, allSubDigraphEdges.size());

            DirectedEdge randomEdge = allSubDigraphEdges.get(randomEdgeId);
            allSubDigraphEdges.set(randomEdgeId, allSubDigraphEdges.get(edgeIndex));
            allSubDigraphEdges.set(edgeIndex, randomEdge);

            randomSubDigraph.addEdge(randomEdge.fromVertex, randomEdge.toVertex);
        }
        return randomSubDigraph;
    }

    private void readDAG(Digraph fullDigraph, SeparateChainingHashTable<Integer, Integer> realDAGToFullDAGMap) {
        String filePath = Constants.FILES_PATH + Constants.CITATION_DIGRAPH_FILE;
        String separator = " ";
        In in = new In(filePath);

        while (in.hasNextLine()) {
            String[] connection = in.readLine().split(separator);

            int paper1 = Integer.parseInt(connection[0]);
            int paper2 = Integer.parseInt(connection[1]);

            if (paper1 == paper2 || paper1 > paper2) {
                //Ignore self-citations and citations for papers in the future to avoid cycles
                // and maintain the digraph as a DAG
                continue;
            }

            if (!realDAGToFullDAGMap.contains(paper1)) {
                int paperVertex1Id = realDAGToFullDAGMap.size();
                realDAGToFullDAGMap.put(paper1, paperVertex1Id);
            }
            if (!realDAGToFullDAGMap.contains(paper2)) {
                int paperVertex2Id = realDAGToFullDAGMap.size();
                realDAGToFullDAGMap.put(paper2, paperVertex2Id);
            }

            int paperVertex1Id = realDAGToFullDAGMap.get(paper1);
            int paperVertex2Id = realDAGToFullDAGMap.get(paper2);

            fullDigraph.addEdge(paperVertex1Id, paperVertex2Id);
        }
    }

    // Example parameters:
    // 500 20
    public static void main(String[] args) {
        int randomVerticesToChoose = Integer.parseInt(args[0]);
        int randomEdgesToChoose = Integer.parseInt(args[1]);

        Digraph randomRealDAG = new Exercise51_RealWorldDAG().
                randomRealDAG(randomVerticesToChoose, randomEdgesToChoose);

        StdOut.println(randomRealDAG);
    }

}
