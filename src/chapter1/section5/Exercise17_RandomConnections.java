package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 08/12/16.
 */
public class Exercise17_RandomConnections {

    public static void main(String[] args) {
        int numberOfSites = Integer.parseInt(args[0]);

        int numberOfConnections = erdosRenyi(numberOfSites);

        StdOut.println();
        StdOut.println("Number of connections: " + numberOfConnections);
    }

    private static int erdosRenyi(int numberOfSites) {
        return count(numberOfSites);
    }

    private static int count(int numberOfSites) {
        int connectionsGenerated = 0;

        UnionFind unionFind = new UnionFind(numberOfSites);

        while(unionFind.count() != 1) {

            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            if(!unionFind.connected(randomSite1, randomSite2)) {
                unionFind.union(randomSite1, randomSite2);

                connectionsGenerated++;
                StdOut.println("Connections generated: " + connectionsGenerated);
            }
        }

        return connectionsGenerated;
    }

}
