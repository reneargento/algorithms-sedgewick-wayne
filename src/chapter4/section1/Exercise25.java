package chapter4.section1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

import java.util.Calendar;

/**
 * Created by Rene Argento on 19/09/17.
 */
public class Exercise25 {

    private class DegreesOfSeparationRecentMovies {

        private void buildGraphAndGetInput(String filePath, String separator, String sourceMovie, String yearsOld) {
            SymbolGraph movieSymbolGraph = new SymbolGraph(filePath, separator);
            Graph graph = movieSymbolGraph.graph();

            if (!movieSymbolGraph.contains(sourceMovie)) {
                StdOut.println(sourceMovie + " not in database.");
                return;
            }

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int yearsOldToConsider = Integer.parseInt(yearsOld);

            int sourceVertex = movieSymbolGraph.index(sourceMovie);
            BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, sourceVertex);

            while (!StdIn.isEmpty()) {
                String sink = StdIn.readLine();

                //All movie names in the graph end with "(YYYY)"
                int movieYear = Integer.parseInt(sink.substring(sink.length() - 5, sink.length() - 1));

                if (currentYear - movieYear > yearsOldToConsider) {
                    StdOut.println("Ignoring old movie");
                    continue;
                }

                if (movieSymbolGraph.contains(sink)) {
                    int destinationVertex = movieSymbolGraph.index(sink);

                    if (breadthFirstPaths.hasPathTo(destinationVertex)) {
                        for(int vertexInPath : breadthFirstPaths.pathTo(destinationVertex)) {
                            StdOut.println("    " + movieSymbolGraph.name(vertexInPath));
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

    // args[0] - The file name
    // args[1] - The lines separator
    // args[2] - The source movie
    // args[3] - Years - Ignore movies that are more than Years years old
    //Arguments example: movies.txt / "Altered States (1980)" 15
    public static void main(String[] args) {
        String filePath = Constants.FILES_PATH + args[0];
        new Exercise25().new DegreesOfSeparationRecentMovies().buildGraphAndGetInput(filePath, args[1], args[2], args[3]);
    }

}
