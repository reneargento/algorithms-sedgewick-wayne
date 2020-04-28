package chapter4.section2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.List;

/**
 * Created by Rene Argento on 02/11/17.
 */
public class Exercise52_Reachability {

    private void generateDigraphsAndDoExperiments(int experiments, int vertices, int edges) {
        StdOut.printf("%25s %15s %15s %15s %31s\n", "Digraph type | ", "Experiments | ", "Vertices | ", "Edges | ",
                "Average number of vertices reachable");

        int totalVerticesReached = 0;

        // Digraph model 1: Random digraphs
        String digraphType = "Random digraph";

        Exercise45_RandomDigraphs exercise45_randomDigraphs = new Exercise45_RandomDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomDigraph = exercise45_randomDigraphs.erdosRenyiDigraph(vertices, edges);
            int experimentResult = doExperiment(randomDigraph);
            totalVerticesReached += experimentResult;
        }

        double averageNumberOfVerticesReachable = totalVerticesReached / (double) experiments;
        printResults(digraphType, experiments, vertices, edges, averageNumberOfVerticesReachable);

        totalVerticesReached = 0;

        // Digraph model 2: Random simple digraphs
        digraphType = "Random simple digraph";

        Exercise46_RandomSimpleDigraphs exercise46_randomSimpleDigraphs = new Exercise46_RandomSimpleDigraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            DigraphInterface randomSimpleDigraph = exercise46_randomSimpleDigraphs.randomDigraph(vertices, edges);
            int experimentResult = doExperiment(randomSimpleDigraph);
            totalVerticesReached += experimentResult;
        }

        averageNumberOfVerticesReachable = totalVerticesReached / (double) experiments;
        printResults(digraphType, experiments, vertices, edges, averageNumberOfVerticesReachable);

        totalVerticesReached = 0;

        // Digraph model 3: Random sparse digraphs
        digraphType = "Random sparse digraph";

        Exercise47_RandomSparseDigraphs exercise47_randomSparseDigraphs = new Exercise47_RandomSparseDigraphs();

        List<DigraphInterface> randomSparseDigraphs = exercise47_randomSparseDigraphs.randomSparseDigraph(experiments, vertices);

        for(DigraphInterface randomSparseDigraph : randomSparseDigraphs) {
            int experimentResult = doExperiment(randomSparseDigraph);
            totalVerticesReached += experimentResult;
        }

        int edgesInSparseDigraphs = vertices * 3;

        averageNumberOfVerticesReachable = totalVerticesReached / (double) experiments;
        printResults(digraphType, experiments, vertices, edgesInSparseDigraphs, averageNumberOfVerticesReachable);
    }

    private int doExperiment(DigraphInterface digraph) {

        int totalVerticesReached = 0;

        int randomSourceVertex = StdRandom.uniform(digraph.vertices());
        boolean[] visited = new boolean[digraph.vertices()];

        dfs(digraph, randomSourceVertex, visited);

        for(int vertex = 0; vertex < digraph.vertices(); vertex++) {
            if (visited[vertex]) {
                totalVerticesReached++;
            }
        }

        return totalVerticesReached;
    }

    private void dfs(DigraphInterface digraph, int sourceVertex, boolean[] visited) {
        visited[sourceVertex] = true;

        for(int neighbor : digraph.adjacent(sourceVertex)) {
            if (!visited[neighbor]) {
                dfs(digraph, neighbor, visited);
            }
        }
    }

    private void printResults(String digraphType, int experiments, int vertices, int edges,
                              double averageNumberOfVerticesReachable) {
        StdOut.printf("%22s %15d %15d %15d %39.2f\n", digraphType, experiments, vertices, edges,
                averageNumberOfVerticesReachable);
    }

    // Parameters example: 1000 100 300
    //                     1000 300 100
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        new Exercise52_Reachability().generateDigraphsAndDoExperiments(experiments, vertices, edges);
    }

}
