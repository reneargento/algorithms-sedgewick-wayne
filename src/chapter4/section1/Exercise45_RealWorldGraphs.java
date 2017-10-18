package chapter4.section1;

import chapter1.section3.Bag;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

/**
 * Created by rene on 09/10/17.
 */
// The data source is a file containing information about United States flight distances
    // It can be downloaded here: http://networkrepository.com/inf-USAir97.php
@SuppressWarnings("unchecked")
public class Exercise45_RealWorldGraphs {

    private class WeightedGraph {

        private class Edge {
            int otherVertex;
            double weight;

            Edge(int otherVertex, double weight) {
                this.otherVertex = otherVertex;
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

        public void addEdge(int vertex1, int vertex2, double weight) {
            if(!adjacent.contains(vertex1)) {
                adjacent.put(vertex1, new Bag<>());
                vertices++;
            }
            if(!adjacent.contains(vertex2)) {
                adjacent.put(vertex2, new Bag<>());
                vertices++;
            }

            Edge edge1 = new Edge(vertex1, weight);
            Edge edge2 = new Edge(vertex2, weight);

            adjacent.get(vertex1).add(edge2);
            adjacent.get(vertex2).add(edge1);
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

                for(Edge neighbor : adjacent(vertex)) {
                    stringBuilder.append(neighbor.otherVertex).append(" ");
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    private WeightedGraph randomRealGraph(int randomVerticesToChoose) {
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

        for(int vertex = 0; vertex < randomVerticesToChoose; vertex++) {
            // Randomly choose a vertex between 1 and vertices
            int randomVertexId = StdRandom.uniform(vertices) + 1;

            if(!graphToSubGraphMap.contains(randomVertexId)) {
                int subGraphVertexId = graphToSubGraphMap.size();
                graphToSubGraphMap.put(randomVertexId, subGraphVertexId);
            }

            int subGraphCurrentVertexId = graphToSubGraphMap.get(randomVertexId);

            for(WeightedGraph.Edge edge : fullGraph.adjacent(randomVertexId)) {

                if(!graphToSubGraphMap.contains(edge.otherVertex)) {
                    int subGraphVertexId = graphToSubGraphMap.size();
                    graphToSubGraphMap.put(edge.otherVertex, subGraphVertexId);
                }

                int subGraphNeighborVertexId = graphToSubGraphMap.get(edge.otherVertex);

                randomSubGraph.addEdge(subGraphCurrentVertexId, subGraphNeighborVertexId, edge.weight);
            }
        }

        return randomSubGraph;
    }

    public static void main(String[] args) {
        int randomVerticesToChoose = Integer.parseInt(args[0]);
        WeightedGraph randomRealGraph = new Exercise45_RealWorldGraphs().randomRealGraph(randomVerticesToChoose);

        StdOut.println(randomRealGraph);
    }

}
