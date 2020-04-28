package chapter4.section1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 14/10/17.
 */
public class Exercise51_TwoColorable {

    private class TwoColor {

        private boolean[] visited;
        private boolean[] color;
        private boolean isTwoColorable = true;

        private int edgesExamined;

        public TwoColor(GraphInterface graph) {
            visited = new boolean[graph.vertices()];
            color = new boolean[graph.vertices()];
            edgesExamined = 0;

            for(int source = 0; source < graph.vertices(); source++) {
                if (!visited[source]) {
                    dfs(graph, source);
                }
            }
        }

        private void dfs(GraphInterface graph, int vertex) {
            visited[vertex] = true;

            for(int neighbor : graph.adjacent(vertex)) {
                if (isTwoColorable) {
                    edgesExamined++;
                }

                if (!visited[neighbor]) {
                    color[neighbor] = !color[vertex];
                    dfs(graph, neighbor);
                } else if (color[neighbor] == color[vertex]) {
                    isTwoColorable = false;
                }
            }
        }

        public boolean isBipartite() {
            return isTwoColorable;
        }

    }

    private void generateGraphsAndDoExperiments(int experiments, int vertices, int edges) {
        StdOut.printf("%25s %15s\n", "Graph type | ", "Edges examined");

        int totalEdgesExamined = 0;

        // Graph model 1: Random graphs
        String graphType = "Random graph";

        Exercise40_RandomGraphs exercise40_randomGraphs = new Exercise40_RandomGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomGraph = exercise40_randomGraphs.erdosRenyiGraph(vertices, edges);
            totalEdgesExamined += doExperiment(randomGraph);
        }

        computeAndPrintResults(graphType, experiments, totalEdgesExamined);
        totalEdgesExamined = 0;

        // Graph model 2: Random simple graphs
        graphType = "Random simple graph";

        Exercise41_RandomSimpleGraphs exercise41_randomSimpleGraphs = new Exercise41_RandomSimpleGraphs();

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomSimpleGraph = exercise41_randomSimpleGraphs.randomSimpleGraph(vertices, edges);
            totalEdgesExamined += doExperiment(randomSimpleGraph);
        }

        computeAndPrintResults(graphType, experiments, totalEdgesExamined);
        totalEdgesExamined = 0;

        // Graph model 3: Random interval graphs
        graphType = "Random interval graph";

        Exercise46_RandomIntervalGraphs exercise46_randomIntervalGraphs = new Exercise46_RandomIntervalGraphs();
        double defaultLength = 0.3;

        for(int experiment = 0; experiment < experiments; experiment++) {
            GraphInterface randomIntervalGraph = exercise46_randomIntervalGraphs.generateIntervalGraph(vertices, defaultLength);
            totalEdgesExamined += doExperiment(randomIntervalGraph);
        }

        computeAndPrintResults(graphType, experiments, totalEdgesExamined);
    }

    private int doExperiment(GraphInterface graph) {
        TwoColor twoColor = new TwoColor(graph);
        return twoColor.edgesExamined;
    }

    private void computeAndPrintResults(String graphType, int experiments, int totalEdgesExamined) {
        int averageNumberOfEdgesExamined = totalEdgesExamined / experiments;
        printResults(graphType, averageNumberOfEdgesExamined);
    }

    private void printResults(String graphType, int averageNumberOfEdgesExamined) {
        StdOut.printf("%22s %18d\n", graphType, averageNumberOfEdgesExamined);
    }

    // Parameters example: 1000 100 300
    //                     1000 300 100
    public static void main(String[] args) {
        int experiments = Integer.parseInt(args[0]);
        int vertices = Integer.parseInt(args[1]);
        int edges = Integer.parseInt(args[2]);

        new Exercise51_TwoColorable().generateGraphsAndDoExperiments(experiments, vertices, edges);
    }

}
