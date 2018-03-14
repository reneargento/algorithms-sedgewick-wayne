package chapter4.section1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 18/09/17.
 */
public class DegreesOfSeparation {

    public static void main(String[] args) {
        SymbolGraph symbolGraph = new SymbolGraph(args[0], args[1]);

        Graph graph = symbolGraph.graph();

        String source = args[2];
        if (!symbolGraph.contains(source)) {
            StdOut.println(source + " not in database.");
            return;
        }

        int sourceVertex = symbolGraph.index(source);
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, sourceVertex);

        while (!StdIn.isEmpty()) {
            String sink = StdIn.readLine();

            if (symbolGraph.contains(sink)) {
                int destinationVertex = symbolGraph.index(sink);

                if (breadthFirstPaths.hasPathTo(destinationVertex)) {
                    for(int vertexInPath : breadthFirstPaths.pathTo(destinationVertex)) {
                        StdOut.println("    " + symbolGraph.name(vertexInPath));
                    }
                } else {
                    StdOut.println("Not connected");
                }
            } else {
                StdOut.println("Not in database.");
            }
        }
    }

}
