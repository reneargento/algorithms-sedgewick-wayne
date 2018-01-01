package chapter4.section1;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 17/09/17.
 */
public class Exercise23 {

    private class MovieSymbolGraph {

        private class Vertex {
            String name;
            boolean isMovie;

            Vertex(String name, boolean isMovie) {
                this.name = name;
                this.isMovie = isMovie;
            }

            @Override
            public int hashCode() {
                return name.hashCode();
            }

            @Override
            public boolean equals(Object other) {
                return other instanceof Vertex && name.equals(((Vertex) other).name);
            }
        }

        private SeparateChainingHashTable<Vertex, Integer> vertexToIdMap;
        private Vertex[] keys;
        private Graph graph;

        public MovieSymbolGraph(String stream, String separator) {
            vertexToIdMap = new SeparateChainingHashTable<>();

            //First pass
            In in = new In(stream);

            while (in.hasNextLine()) {
                String[] vertices = in.readLine().split(separator);

                // Movie vertex
                Vertex movieVertex = new Vertex(vertices[0], true);

                if(!vertexToIdMap.contains(movieVertex)) {
                    vertexToIdMap.put(movieVertex, vertexToIdMap.size());
                }

                for(int i = 1; i < vertices.length; i++) {
                    Vertex actorVertex = new Vertex(vertices[i], false);

                    if(!vertexToIdMap.contains(actorVertex)) {
                        vertexToIdMap.put(actorVertex, vertexToIdMap.size());
                    }
                }
            }

            keys = new Vertex[vertexToIdMap.size()];

            for(Vertex vertex : vertexToIdMap.keys()) {
                keys[vertexToIdMap.get(vertex)] = vertex;
            }

            graph = new Graph(vertexToIdMap.size());
            //Seconds pass
            in = new In(stream);

            while (in.hasNextLine()) {
                String[] vertices = in.readLine().split(separator);

                int movieVertex = vertexToIdMap.get(new Vertex(vertices[0], true));
                for(int i = 1; i < vertices.length; i++) {
                    graph.addEdge(movieVertex, vertexToIdMap.get(new Vertex(vertices[i], false)));
                }
            }
        }

        public boolean contains(String vertexName) {
            return vertexToIdMap.contains(new Vertex(vertexName, false)); //isMovie is not used here
        }

        public int index(String vertexName) {
            return vertexToIdMap.get(new Vertex(vertexName, false)); //isMovie is not used here
        }

        public Vertex vertexInformation(int vertexId) {
            return keys[vertexId];
        }

        public Graph graph() {
            return graph;
        }
    }

    private void baconHistogram() {
        String filePath = Constants.FILES_PATH + Constants.MOVIES_FILE;
        String separator = "/";
        String kevinBaconName = "Bacon, Kevin";

        final int MAX_BACON = 100;

        MovieSymbolGraph movieSymbolGraph = new MovieSymbolGraph(filePath, separator);
        Graph graph = movieSymbolGraph.graph();
        int kevinBaconId = movieSymbolGraph.index(kevinBaconName);

        //Get Kevin Bacon numbers
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, kevinBaconId);

        int[] histogram = new int[MAX_BACON + 1];

        for(int vertex = 0; vertex < graph.vertices(); vertex++) {
            if(movieSymbolGraph.vertexInformation(vertex).isMovie) {
                continue;
            }

            int kevinBaconNumber = breadthFirstPaths.distTo(vertex);

            if(kevinBaconNumber == Integer.MAX_VALUE) {
                kevinBaconNumber = MAX_BACON;
            } else {
                //Divide by 2 because the relation in the graph is
                // Actor ---- Movie ---- Actor
                // So a distance of 2 in the graph is actually a Kevin Bacon number of 1
                kevinBaconNumber /= 2;
            }

            histogram[kevinBaconNumber]++;
        }

        //Print histogram
        for(int i = 0; i < histogram.length; i++) {
            if(histogram[i] == 0) {
                break;
            }
            StdOut.printf("%3d %8d\n", i, histogram[i]);
        }
        StdOut.printf("Inf %8d\n", histogram[MAX_BACON]);
    }

    public static void main(String[] args) {
        new Exercise23().baconHistogram();
    }

}
