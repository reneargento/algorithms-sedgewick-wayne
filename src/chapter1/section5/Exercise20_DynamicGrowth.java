package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 08/12/16.
 */
public class Exercise20_DynamicGrowth {

    private class DynamicWeightedQuickUnion {

        private int[] leaders;
        private int[] size;

        private int numberOfSites;
        private int count;

        public int count() {
            return count;
        }

        public int getNumberOfSites() {
            return numberOfSites;
        }

        public boolean connected(int site1, int site2) {
            if (leaders == null || site1 >= numberOfSites || site2 >= numberOfSites) {
                throw new RuntimeException("Site does not exist");
            }

            return find(site1) == find(site2);
        }

        public int find(int site) {
            if (leaders == null || site >= numberOfSites) {
                throw new RuntimeException("Site does not exist");
            }

            while(site != leaders[site]) {
                site = leaders[site];
            }

            return site;
        }

        public void union(int site1, int site2) {
            if (leaders == null || site1 >= numberOfSites || site2 >= numberOfSites) {
                throw new RuntimeException("Site does not exist");
            }

            int leader1 = leaders[site1];
            int leader2 = leaders[site2];

            if (leader1 == leader2) {
                return;
            }

            if (size[leader1] < size[leader2]) {
                leaders[leader1] = leader2;
                size[leader2] += size[leader1];
            } else {
                leaders[leader2] = leader1;
                size[leader1] += size[leader2];
            }

            count--;
        }

        public int newSite() {
            if (leaders == null) {
                leaders = new int[1];
                size = new int[1];
            }

            if (numberOfSites == leaders.length) {
                resizeArray(numberOfSites * 2);
            }

            int newSiteId = numberOfSites;
            leaders[newSiteId] = newSiteId;
            size[newSiteId] = 1;

            numberOfSites++;
            count++;

            return newSiteId;
        }

        private void resizeArray(int newSize) {
            int[] newLeadersArray = new int[newSize];
            int[] newSizeArray = new int[newSize];

            for(int i = 0; i < numberOfSites; i++) {
                newLeadersArray[i] = leaders[i];
                newSizeArray[i] = size[i];
            }

            leaders = newLeadersArray;
            size = newSizeArray;
        }

    }

    public static void main(String[] args) {
        Exercise20_DynamicGrowth dynamicGrowth = new Exercise20_DynamicGrowth();

        DynamicWeightedQuickUnion dynamicWeightedQuickUnion = dynamicGrowth.new DynamicWeightedQuickUnion();

        //Add five sites
        dynamicWeightedQuickUnion.newSite();
        dynamicWeightedQuickUnion.newSite();
        dynamicWeightedQuickUnion.newSite();
        dynamicWeightedQuickUnion.newSite();
        dynamicWeightedQuickUnion.newSite();


        while(dynamicWeightedQuickUnion.count() != 1) {

            int randomSite1 = StdRandom.uniform(dynamicWeightedQuickUnion.getNumberOfSites());
            int randomSite2 = StdRandom.uniform(dynamicWeightedQuickUnion.getNumberOfSites());

            if (!dynamicWeightedQuickUnion.connected(randomSite1, randomSite2)) {
                dynamicWeightedQuickUnion.union(randomSite1, randomSite2);

                StdOut.println("United sites " + randomSite1 + " and " + randomSite2);
            }
        }
    }

}
