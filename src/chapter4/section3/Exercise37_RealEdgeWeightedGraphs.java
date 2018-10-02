package chapter4.section3;

import chapter1.section3.Bag;
import chapter3.section4.SeparateChainingHashTable;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 12/11/17.
 */
// The data source is a file containing information about flights between United States airports in 1997.
// It can be downloaded here: http://networkrepository.com/inf-USAir97.php
@SuppressWarnings("unchecked")
public class Exercise37_RealEdgeWeightedGraphs {

    private class EdgeWeightedGraph {

        private int vertices;
        private int edges;
        private SeparateChainingHashTable<Integer, Bag<Edge>> adjacent;

        public EdgeWeightedGraph() {
            vertices = 0;
            edges = 0;
            adjacent = new SeparateChainingHashTable<>();
        }

        public int vertices() {
            return vertices;
        }

        public int edgesCount() {
            return edges;
        }

        public void addVertex(int vertex) {
            if (!adjacent.contains(vertex)) {
                adjacent.put(vertex, new Bag<>());
                vertices++;
            }
        }

        public void addEdge(Edge edge) {
            int vertex1 = edge.either();
            int vertex2 = edge.other(vertex1);

            if (!adjacent.contains(vertex1)) {
                adjacent.put(vertex1, new Bag<>());
                vertices++;
            }
            if (!adjacent.contains(vertex2)) {
                adjacent.put(vertex2, new Bag<>());
                vertices++;
            }

            adjacent.get(vertex1).add(edge);
            adjacent.get(vertex2).add(edge);
            edges++;
        }

        public Iterable<Edge> adjacent(int vertex) {
            return adjacent.get(vertex);
        }

        public Iterable<Edge> edges() {
            Bag<Edge> edges = new Bag<>();

            for(int vertex = 0; vertex < vertices; vertex++) {
                if (adjacent.get(vertex) == null) {
                    continue;
                }

                for(Edge edge : adjacent.get(vertex)) {
                    if (edge.other(vertex) > vertex) {
                        edges.add(edge);
                    }
                }
            }

            return edges;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                if (adjacent.get(vertex) != null) {
                    for(Edge edge : adjacent(vertex)) {
                        stringBuilder.append(edge).append(" ");
                    }
                }

                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    private EdgeWeightedGraph randomRealEdgeWeightedGraph(int randomVerticesToChoose, int randomEdgesToChoose) {
        String filePath = Constants.FILES_PATH + Constants.US_AIR_FILE;
        String separator = " ";

        In in = new In(filePath);
        String[] firstLine = in.readLine().split(separator);
        int vertices = Integer.parseInt(firstLine[0]);
        int edges = Integer.parseInt(firstLine[2]);

        EdgeWeightedGraph fullGraph = new EdgeWeightedGraph();

        for(int edge = 0; edge < edges; edge++) {
            String[] connection = in.readLine().split(separator);

            int city1 = Integer.parseInt(connection[0]);
            int city2 = Integer.parseInt(connection[1]);
            double distance = Double.parseDouble(connection[2]);

            Edge newEdge = new Edge(city1, city2, distance);
            fullGraph.addEdge(newEdge);
        }

        EdgeWeightedGraph randomSubGraph = new EdgeWeightedGraph();
        SeparateChainingHashTable<Integer, Integer> graphToSubGraphMap = new SeparateChainingHashTable<>();

        List<Edge> allSubGraphEdges = new ArrayList<>();
        HashSet<Integer> chosenVertices = new HashSet<>();

        for(int vertex = 0; vertex < randomVerticesToChoose; vertex++) {
            // Randomly choose a vertex between 1 and vertices
            int randomVertexId = StdRandom.uniform(vertices) + 1;

            if (chosenVertices.contains(randomVertexId)) {
                continue;
            }
            chosenVertices.add(randomVertexId);

            int subGraphVertexId1 = graphToSubGraphMap.size();
            graphToSubGraphMap.put(randomVertexId, subGraphVertexId1);
            randomSubGraph.addVertex(subGraphVertexId1);

            for(Edge edge : fullGraph.adjacent(randomVertexId)) {
                int vertexId2 = edge.other(randomVertexId);

                int subGraphVertexId2;

                if (!graphToSubGraphMap.contains(vertexId2)) {
                    subGraphVertexId2 = graphToSubGraphMap.size();
                    graphToSubGraphMap.put(vertexId2, subGraphVertexId2);
                    randomSubGraph.addVertex(subGraphVertexId2);
                } else {
                    subGraphVertexId2 = graphToSubGraphMap.get(vertexId2);
                }

                allSubGraphEdges.add(new Edge(subGraphVertexId1, subGraphVertexId2, edge.weight()));
            }
        }

        // Randomly choose E edges from the subgraph induced by the random vertices
        if (randomEdgesToChoose > allSubGraphEdges.size()) {
            throw new IllegalArgumentException("Not enough edges to choose from the induced subgraph");
        }

        Edge[] allSubGraphEdgesArray = new Edge[allSubGraphEdges.size()];
        int allSubGraphEdgesArrayIndex = 0;
        HashSet<Integer> edgesChosen = new HashSet<>();

        for(Edge edge : allSubGraphEdges) {
            allSubGraphEdgesArray[allSubGraphEdgesArrayIndex++] = edge;
        }

        for(int edge = 0; edge < randomEdgesToChoose; edge++) {
            // Randomly choose an edge
            int randomEdgeId = StdRandom.uniform(allSubGraphEdgesArray.length);

            if (edgesChosen.contains(randomEdgeId)) {
                continue;
            }

            edgesChosen.add(randomEdgeId);

            Edge randomEdge = allSubGraphEdgesArray[randomEdgeId];

            int subGraphVertexId1 = randomEdge.either();
            int subGraphVertexId2 = randomEdge.other(subGraphVertexId1);
            Edge newEdge = new Edge(subGraphVertexId1, subGraphVertexId2, randomEdge.weight());

            randomSubGraph.addEdge(newEdge);
        }

        return randomSubGraph;
    }

    // Example parameters:
    // 20 20
    public static void main(String[] args) {
        int randomVerticesToChoose = Integer.parseInt(args[0]);
        int randomEdgesToChoose = Integer.parseInt(args[1]);

        EdgeWeightedGraph randomRealEdgeWeightedGraph = new Exercise37_RealEdgeWeightedGraphs().
                randomRealEdgeWeightedGraph(randomVerticesToChoose, randomEdgesToChoose);

        StdOut.println(randomRealEdgeWeightedGraph);
    }

}
