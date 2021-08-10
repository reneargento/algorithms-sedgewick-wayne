package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/12/16.
 */
// Thanks to DominicPeng0125 (https://github.com/DominicPeng0125) for mentioning that the path compression should be iterative.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/204
public class Exercise12_QuickUnionPathCompression {

    private class QuickUnionPathCompression {
        int[] id;
        int count;

        public QuickUnionPathCompression(int size) {
            id = new int[size];
            count = size;

            for(int i = 0; i < id.length; i++) {
                id[i] = i;
            }
        }

        public int count() {
            return count;
        }

        // amortized O(lg n)
        public int find(int site) {
            int root = site;
            while (root != id[root]) {
                root = id[root];
            }

            while (site != id[site]) {
                int nextParent = id[site];
                id[site] = root;
                site = nextParent;
            }
            return root;
        }

        // O(1)
        public boolean connected(int site1, int site2) {
            return find(site1) == find(site2);
        }

        // amortized O(lg n)
        public void union(int site1, int site2) {
            int leaderId1 = find(site1);
            int leaderId2 = find(site2);

            if (leaderId1 == leaderId2) {
                return;
            }

            id[leaderId1] = leaderId2;

            count--;
        }
    }

    public static void main(String[] args) {
        QuickUnionPathCompression quickUnionPathCompression = new Exercise12_QuickUnionPathCompression().new QuickUnionPathCompression(10);
        quickUnionPathCompression.union(0, 1);
        quickUnionPathCompression.union(2, 3);
        quickUnionPathCompression.union(3, 4);
        quickUnionPathCompression.union(2, 4);

        StdOut.println("Components: " + quickUnionPathCompression.count + " Expected: 7");

        // Sequence of input pairs to produce a path of length 4:
        //0 1
        //2 3
        //4 5
        //6 7
        //6 4
        //4 2
        //4 0

        // Path: 6 -> 7 -> 5 -> 3 -> 1

        QuickUnionPathCompression quickUnionPathCompression2 = new Exercise12_QuickUnionPathCompression().new QuickUnionPathCompression(10);
        quickUnionPathCompression2.union(0, 1);
        quickUnionPathCompression2.union(2, 3);
        quickUnionPathCompression2.union(4, 5);
        quickUnionPathCompression2.union(6, 7);
        quickUnionPathCompression2.union(6, 4);
        quickUnionPathCompression2.union(4, 2);
        quickUnionPathCompression2.union(4, 0);

        StdOut.println("Components: " + quickUnionPathCompression2.count + " Expected: 3");
    }

}
