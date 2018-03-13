package chapter1.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 04/12/16.
 */
public class Exercise7 {

    private class QuickFind {

        int[] id;
        int count;

        public QuickFind(int size) {
            id = new int[size];
            count = size;

            for(int i = 0; i < id.length; i++) {
                id[i] = i;
            }
        }

        public int count() {
            return count;
        }

        //O(1)
        public int find(int site) {
            return id[site];
        }

        //O(1)
        public boolean connected(int site1, int site2) {
            return find(site1) == find(site2);
        }

        //O(n)
        public void union(int site1, int site2) {
            int leaderId1 = find(site1);
            int leaderId2 = find(site2);

            if (leaderId1 == leaderId2) {
                return;
            }

            for(int i = 0; i < id.length; i++) {
                if (id[i] == leaderId1) {
                    id[i] = leaderId2;
                }
            }

            count--;
        }
    }

    private class QuickUnion {

        int[] id;
        int count;

        public QuickUnion(int n) {
            id = new int[n];
            count = n;

            for(int i = 0; i < id.length; i++) {
                id[i] = i;
            }
        }

        public int count() {
            return count;
        }

        //O(n)
        public int find(int site) {
            while(site != id[site]) {
                site = id[site];
            }

            return site;
        }

        //O(1)
        public boolean connected(int site1, int site2) {
            return find(site1) == find(site2);
        }

        //O(n)
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
        QuickFind quickFind = new Exercise7().new QuickFind(5);
        quickFind.union(0, 3);
        quickFind.union(1, 2);
        quickFind.union(1, 3);

        StdOut.println("Components: " + quickFind.count + " Expected: 2");

        QuickUnion quickUnion = new Exercise7().new QuickUnion(10);
        quickUnion.union(0, 4);
        quickUnion.union(1, 9);
        quickUnion.union(2, 3);

        StdOut.println("Components: " + quickUnion.count + " Expected: 7");
    }

}
