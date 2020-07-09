package chapter1.section5;

/**
 * Created by Rene Argento on 09/12/16.
 */
// Thanks to Jinchul81 (https://github.com/Jinchul81) for fixing the time complexity description on the connected() method:
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/164
public class QuickUnion implements UF {

    private int[] id;
    private int count;

    public QuickUnion(int size) {
        id = new int[size];
        count = size;

        for(int i = 0; i < id.length; i++) {
            id[i] = i;
        }
    }

    public int count() {
        return count;
    }

    // O(n)
    public int find(int site) {
        while(site != id[site]) {
            site = id[site];
        }

        return site;
    }

    // O(n)
    public boolean connected(int site1, int site2) {
        return find(site1) == find(site2);
    }

    // O(n)
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
