package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/12/16.
 */
public class Exercise13_WeightedQUPathCompression {

    public class WeightedQuickUnionPathCompression implements UF {

        int id[];
        int size[];
        int count;

        public WeightedQuickUnionPathCompression(int size) {
            id = new int[size];
            this.size = new int[size];
            count = size;

            for(int i = 0; i < id.length; i++) {
                id[i] = i;
                this.size[i] = 1;
            }
        }

        public int count() {
            return count;
        }

        public boolean connected(int site1, int site2) {
            return find(site1) == find(site2);
        }

        //O(lg n)
        public int find(int site) {
            if (site == id[site]) {
                return site;
            }

            return id[site] = find(id[site]);
        }

        //O(lg n)
        public void union(int site1, int site2) {
            int parentId1 = find(site1);
            int parentId2 = find(site2);

            if (parentId1 == parentId2) {
                return;
            }

            if (size[parentId1] < size[parentId2]) {
                id[parentId1] = parentId2;
                size[parentId2] += size[parentId1];
            } else {
                id[parentId2] = parentId1;
                size[parentId1] += size[parentId2];
            }

            count--;
        }
    }

    public static void main(String[] args) {
        //Sequence of input pairs to produce a tree of height 4:
        //0 1
        //0 2
        //0 3
        //6 7
        //8 9
        //6 8
        //0 6
        //10 11
        //10 12
        //10 13
        //10 14
        //10 15
        //10 16
        //10 17
        //10 18
        //0 10

        //Path of height 4: 9 -> 8 -> 6 -> 0 -> 10

        WeightedQuickUnionPathCompression weightedQuickUnionPathCompression =
                new Exercise13_WeightedQUPathCompression().new WeightedQuickUnionPathCompression(19);
        weightedQuickUnionPathCompression.union(0, 1);
        weightedQuickUnionPathCompression.union(0, 2);
        weightedQuickUnionPathCompression.union(0, 3);
        weightedQuickUnionPathCompression.union(6, 7);
        weightedQuickUnionPathCompression.union(8, 9);
        weightedQuickUnionPathCompression.union(6, 8);
        weightedQuickUnionPathCompression.union(0, 6);
        weightedQuickUnionPathCompression.union(10, 11);
        weightedQuickUnionPathCompression.union(10, 12);
        weightedQuickUnionPathCompression.union(10, 13);
        weightedQuickUnionPathCompression.union(10, 14);
        weightedQuickUnionPathCompression.union(10, 15);
        weightedQuickUnionPathCompression.union(10, 16);
        weightedQuickUnionPathCompression.union(10, 17);
        weightedQuickUnionPathCompression.union(10, 18);
        weightedQuickUnionPathCompression.union(0, 10);

        StdOut.println("Components: " + weightedQuickUnionPathCompression.count() + " Expected: 3");
    }

}
