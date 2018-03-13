package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/12/16.
 */
// The change in WeightedQuickFind will slightly improve performance.
    // Find() will still be O(1) and union() operation will still be O(n), but there will be less parent updates in the
    // union operation (at most n / 2).
public class Exercise11 {

    private class WeightedQuickFind {
        int[] id;
        int[] size;
        int count;

        public WeightedQuickFind(int size) {
            id = new int[size];
            this.size = new int[size];
            count = size;

            for(int i = 0; i < id.length; i++) {
                id[i] = i;
            }

            for(int i = 0; i < this.size.length; i++) {
                this.size[i] = 1;
            }
        }

        public int count() {
            return count;
        }

        public boolean connected(int site1, int site2) {
            return find(site1) == find(site2);
        }

        //O(1)
        public int find(int site) {
            return id[site];
        }

        //O(n)
        public void union(int site1, int site2) {
            int parentId1 = find(site1);
            int parentId2 = find(site2);

            if (parentId1 == parentId2) {
                return;
            }

            int parentIdToUpdate;
            int newParentId;

            if (size[parentId1] < size[parentId2]) {
                parentIdToUpdate = parentId1;
                newParentId = parentId2;
            } else {
                parentIdToUpdate = parentId2;
                newParentId = parentId1;
            }

            for(int i = 0; i < id.length; i++) {
                if (id[i] == parentIdToUpdate) {
                    id[i] = newParentId;
                }
            }

            size[newParentId] += size[parentIdToUpdate];

            count--;
        }

    }

    public static void main(String[] args) {
        WeightedQuickFind weightedQuickFind = new Exercise11().new WeightedQuickFind(10);
        weightedQuickFind.union(0, 9);
        weightedQuickFind.union(1, 8);
        weightedQuickFind.union(2, 7);
        weightedQuickFind.union(3, 0);
        weightedQuickFind.union(4, 3);

        StdOut.println("Components: " + weightedQuickFind.count + " Expected: 5");
    }

}
