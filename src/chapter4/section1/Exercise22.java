package chapter4.section1;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 17/09/17.
 */
public class Exercise22 {

    private void getKevinBaconNumbers() {
        String filePath = Constants.FILES_PATH + Constants.MOVIES_FILE;
        String separator = "/";
        String kevinBaconName = "Bacon, Kevin";

        SymbolGraph movieSymbolGraph = new SymbolGraph(filePath, separator);
        Graph graph = movieSymbolGraph.graph();
        int baconId = movieSymbolGraph.index(kevinBaconName);

        //Get Kevin Bacon numbers
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, baconId);

        //2017 Oscar nominees
        /**
         * ACTOR IN A LEADING ROLE
         Casey Affleck, Manchester by the Sea
         Andrew Garfield, Hacksaw Ridge
         Ryan Gosling, La La Land
         Viggo Mortensen, Captain Fantastic
         Denzel Washington, Fences

         =============================
         ACTRESS IN A LEADING ROLE
         Isabelle Huppert, Elle
         Ruth Negga, Loving
         Natalie Portman, Jackie
         Emma Stone, La La Land
         Meryl Streep, Florence Foster Jenkins

         =============================
         ACTOR IN A SUPPORTING ROLE
         Mahershala Ali, Moonlight
         Jeff Bridges, Hell or High Water
         Lucas Hedges, Manchester by the Sea
         Dev Patel, Lion
         Michael Shannon, Nocturnal Animals

         =============================
         ACTRESS IN A SUPPORTING ROLE
         Viola Davis, Fences
         Naomie Harris, Moonlight
         Nicole Kidman, Lion
         Octavia Spencer, Hidden Figures
         Michelle Williams, Manchester by the Sea
         */
        List<String> oscarNominees = new ArrayList<>();
        oscarNominees.add("Affleck, Casey");
        oscarNominees.add("Garfield, Andrew"); //Not in the movies.txt file
        oscarNominees.add("Gosling, Ryan (I)");
        oscarNominees.add("Mortensen, Viggo");
        oscarNominees.add("Washington, Denzel");

        oscarNominees.add("Huppert, Isabelle");
        oscarNominees.add("Negga, Ruth");
        oscarNominees.add("Portman, Natalie");
        oscarNominees.add("Stone, Emma"); //Not in the movies.txt file
        oscarNominees.add("Streep, Meryl");

        oscarNominees.add("Ali, Mahershala"); //Not in the movies.txt file
        oscarNominees.add("Bridges, Jeff (I)");
        oscarNominees.add("Hedges, Lucas"); //Not in the movies.txt file
        oscarNominees.add("Patel, Dev"); //Not in the movies.txt file
        oscarNominees.add("Shannon, Michael (V)");

        oscarNominees.add("Davis, Viola (I)");
        oscarNominees.add("Harris, Naomie");
        oscarNominees.add("Kidman, Nicole");
        oscarNominees.add("Spencer, Octavia");
        oscarNominees.add("Williams, Michelle (I)");

        StdOut.println("Kevin Bacon numbers:\n");

        SeparateChainingHashTable<String, Integer> baconNumbers = getKevinBaconNumbersFromGraph(oscarNominees,
                movieSymbolGraph, breadthFirstPaths);
        for(String oscarNominee : baconNumbers.keys()) {
            StdOut.println(oscarNominee + ": " + baconNumbers.get(oscarNominee));
        }
    }

    private SeparateChainingHashTable<String, Integer> getKevinBaconNumbersFromGraph(List<String> oscarNominees,
                                                                                     SymbolGraph movieSymbolGraph,
                                                                                     BreadthFirstPaths breadthFirstPaths) {
        SeparateChainingHashTable<String, Integer> symbolTable = new SeparateChainingHashTable<>();
        for(String oscarNominee : oscarNominees) {
            int kevinBaconNumber = Integer.MAX_VALUE;

            if (movieSymbolGraph.contains(oscarNominee)) {
                int nomineeVertexId = movieSymbolGraph.index(oscarNominee);
                //Divide by 2 because the relation in the graph is
                // Actor ---- Movie ---- Actor
                // So a distance of 2 in the graph is actually a Kevin Bacon number of 1
                kevinBaconNumber = breadthFirstPaths.distTo(nomineeVertexId) / 2;
            }

            symbolTable.put(oscarNominee, kevinBaconNumber);
        }

        return symbolTable;
    }

    public static void main(String[] args) {
        new Exercise22().getKevinBaconNumbers();
    }

}
