package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 07/12/16.
 */
//Also known as union by rank
    //Components are only united when they have the same height,
// guaranteeing a logarithmic upper bound on the height of the trees for N sites
    // The size of a tree will be at most 2^height. Therefore, the height can increase at most lg N times.
public class Exercise14_WeightedQUHeight {

    private class WeightedQuickUnionByHeight {

        int[] leaders;
        int[] ranks;
        int components;

        int maxHeight;

        public WeightedQuickUnionByHeight(int size) {
            leaders = new int[size];
            ranks = new int[size];
            components = size;

            maxHeight = 0;

            //Initialization
            for(int i = 0; i < size; i++) {
                leaders[i] = i;
                ranks[i] = 0;
            }
        }

        public int count() {
            return components;
        }

        public boolean connected(int site1, int site2) {
            return find(site1) == find(site2);
        }

        //O(lg n)
        //no path compression in this exercise
        public int find(int site) {
            while(site != leaders[site]) {
                site = leaders[site];
            }

            return site;
        }

        //O(lg n)
        public void union(int site1, int site2) {

            int leader1 = leaders[site1];
            int leader2 = leaders[site2];

            if(leader1 == leader2) {
                return;
            }

            if(ranks[leader1] < ranks[leader2]) {
                leaders[leader1] = leader2;
            } else if (ranks[leader2] < ranks[leader1]){
                leaders[leader2] = leader1;
            } else {
                leaders[leader1] = leaders[leader2];
                ranks[leader1]++;

                if(ranks[leader1] > maxHeight) {
                    maxHeight = ranks[leader1];
                }
            }

            components--;
        }

    }

    public static void main(String[] args) {
        WeightedQuickUnionByHeight weightedQuickUnionByHeight = new Exercise14_WeightedQUHeight().new WeightedQuickUnionByHeight(19);

        weightedQuickUnionByHeight.union(0, 1);
        weightedQuickUnionByHeight.union(0, 2);
        weightedQuickUnionByHeight.union(0, 3);
        weightedQuickUnionByHeight.union(6, 7);
        weightedQuickUnionByHeight.union(8, 9);
        weightedQuickUnionByHeight.union(6, 8);
        weightedQuickUnionByHeight.union(0, 6);
        weightedQuickUnionByHeight.union(10, 11);
        weightedQuickUnionByHeight.union(10, 12);
        weightedQuickUnionByHeight.union(10, 13);
        weightedQuickUnionByHeight.union(10, 14);
        weightedQuickUnionByHeight.union(10, 15);
        weightedQuickUnionByHeight.union(10, 16);
        weightedQuickUnionByHeight.union(10, 17);
        weightedQuickUnionByHeight.union(10, 18);
        weightedQuickUnionByHeight.union(0, 10);

        StdOut.println("Components: " + weightedQuickUnionByHeight.count() + " Expected: 3");
        StdOut.println("Maximum height: " + weightedQuickUnionByHeight.maxHeight + " Expected: Equal or less than 5 for N = 19" +
                " (lg 19 = 5)");
    }

}
