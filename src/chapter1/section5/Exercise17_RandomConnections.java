package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 08/12/16.
 */
public class Exercise17_RandomConnections {

    public static void main(String[] args) {
        int numberOfSites = Integer.parseInt(args[0]);

        int numberOfConnections = erdosRenyi(numberOfSites, true);

        StdOut.println();
        StdOut.println("Number of connections generated: " + numberOfConnections);
    }

    public static int erdosRenyi(int numberOfSites, boolean verbose) {
        return count(numberOfSites, verbose);
    }

    private static int count(int numberOfSites, boolean verbose) {
        int connectionsGenerated = 0;

        UnionFind unionFind = new UnionFind(numberOfSites);

        while(unionFind.count() != 1) {

            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            connectionsGenerated++;

            if (verbose) {
                StdOut.println("Connection generated: " + randomSite1 + " - " + randomSite2);
            }

            if (!unionFind.connected(randomSite1, randomSite2)) {
                unionFind.union(randomSite1, randomSite2);
            }
        }

        return connectionsGenerated;
    }

}
