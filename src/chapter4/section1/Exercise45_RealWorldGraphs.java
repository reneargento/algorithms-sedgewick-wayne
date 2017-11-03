package chapter4.section1;

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
 * Created by rene on 09/10/17.
 */
// The data source is a file containing information about United States flight distances
    // It can be downloaded here: http://networkrepository.com/inf-USAir97.php
@SuppressWarnings("unchecked")
public class Exercise45_RealWorldGraphs {

    private class WeightedGraph {

        private class Edge {
            int vertex1;
            int vertex2;
            double weight;

            Edge(int vertex1, int vertex2, double weight) {
                this.vertex1 = vertex1;
                this.vertex2 = vertex2;
                this.weight = weight;
            }
        }

        private int vertices;
        private int edges;
        private SeparateChainingHashTable<Integer, Bag<Edge>> adjacent;

        public WeightedGraph() {
            this.vertices = 0;
            this.edges = 0;
            adjacent = new SeparateChainingHashTable<>();
        }

        public int vertices() {
            return vertices;
        }

        public int edges() {
            return edges;
        }

        public void addVertex(int vertex) {
            if(!adjacent.contains(vertex)) {
                adjacent.put(vertex, new Bag<>());
                vertices++;
            }
        }

        public void addEdge(int vertex1, int vertex2, double weight) {
            if(!adjacent.contains(vertex1)) {
                adjacent.put(vertex1, new Bag<>());
                vertices++;
            }
            if(!adjacent.contains(vertex2)) {
                adjacent.put(vertex2, new Bag<>());
                vertices++;
            }

            Edge edge1 = new Edge(vertex1, vertex2, weight);
            Edge edge2 = new Edge(vertex2, vertex1, weight);

            adjacent.get(vertex1).add(edge1);
            adjacent.get(vertex2).add(edge2);
            edges++;
        }

        public Iterable<Edge> adjacent(int vertex) {
            return adjacent.get(vertex);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int vertex = 0; vertex < vertices(); vertex++) {
                stringBuilder.append(vertex).append(": ");

                if(adjacent(vertex) != null) {
                    for(Edge neighbor : adjacent(vertex)) {
                        stringBuilder.append(neighbor.vertex2).append(" ");
                    }
                }

                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    private WeightedGraph randomRealGraph(int randomVerticesToChoose, int randomEdgesToChoose) {
        String filePath = Constants.FILES_PATH + Constants.US_AIR_FILE;
        String separator = " ";

        In in = new In(filePath);
        String[] firstLine = in.readLine().split(separator);
        int vertices = Integer.parseInt(firstLine[0]);
        int edges = Integer.parseInt(firstLine[2]);

        WeightedGraph fullGraph = new WeightedGraph();

        for(int edge = 0; edge < edges; edge++) {
            String[] connection = in.readLine().split(separator);

            int city1 = Integer.parseInt(connection[0]);
            int city2 = Integer.parseInt(connection[1]);
            double distance = Double.parseDouble(connection[2]);

            fullGraph.addEdge(city1, city2, distance);
        }

        WeightedGraph randomSubGraph = new WeightedGraph();
        SeparateChainingHashTable<Integer, Integer> graphToSubGraphMap =
                new SeparateChainingHashTable<>();

        List<WeightedGraph.Edge> allSubGraphEdges = new ArrayList<>();

        for(int vertex = 0; vertex < randomVerticesToChoose; vertex++) {
            // Randomly choose a vertex between 1 and vertices
            int randomVertexId = StdRandom.uniform(vertices) + 1;

            if(graphToSubGraphMap.contains(randomVertexId)) {
                continue;
            }

            int subGraphVertexId = graphToSubGraphMap.size();
            graphToSubGraphMap.put(randomVertexId, subGraphVertexId);

            randomSubGraph.addVertex(subGraphVertexId);

            for(WeightedGraph.Edge edge : fullGraph.adjacent(randomVertexId)) {
                allSubGraphEdges.add(randomSubGraph.new Edge(subGraphVertexId, edge.vertex2, edge.weight));
            }
        }

        // Randomly choose E edges from the subgraph induced by the random vertices
        if(randomEdgesToChoose > allSubGraphEdges.size()) {
            throw new IllegalArgumentException("Not enough edges to choose");
        }

        WeightedGraph.Edge[] allSubGraphEdgesArray = new WeightedGraph.Edge[allSubGraphEdges.size()];
        int allSubGraphEdgesArrayIndex = 0;
        HashSet<Integer> edgesChosen = new HashSet<>();

        for(WeightedGraph.Edge edge : allSubGraphEdges) {
            allSubGraphEdgesArray[allSubGraphEdgesArrayIndex++] = edge;
        }

        for(int edge = 0; edge < randomEdgesToChoose; edge++) {
            // Randomly choose an edge
            int randomEdgeId = StdRandom.uniform(allSubGraphEdgesArray.length);

            if(edgesChosen.contains(randomEdgeId)) {
                continue;
            }

            edgesChosen.add(randomEdgeId);

            WeightedGraph.Edge randomEdge = allSubGraphEdgesArray[randomEdgeId];

            if(!graphToSubGraphMap.contains(randomEdge.vertex2)) {
                int subGraphNeighborVertexId = graphToSubGraphMap.size();
                graphToSubGraphMap.put(randomEdge.vertex2, subGraphNeighborVertexId);
            }

            int subGraphNeighborVertexId = graphToSubGraphMap.get(randomEdge.vertex2);
            randomSubGraph.addEdge(randomEdge.vertex1, subGraphNeighborVertexId, randomEdge.weight);
        }

        return randomSubGraph;
    }

    // Example parameters:
    // 20 20
    public static void main(String[] args) {
        int randomVerticesToChoose = Integer.parseInt(args[0]);
        int randomEdgesToChoose = Integer.parseInt(args[1]);

        WeightedGraph randomRealGraph = new Exercise45_RealWorldGraphs().
                randomRealGraph(randomVerticesToChoose, randomEdgesToChoose);

        StdOut.println(randomRealGraph);
    }

}
