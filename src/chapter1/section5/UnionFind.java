package chapter1.section5;

/**
 * Created by Rene Argento on 08/12/16.
 */
public class UnionFind implements UF {

    private int[] leaders;
    private int[] ranks;

    private int components;

    public UnionFind(int size) {
        leaders = new int[size];
        ranks = new int[size];
        components = size;

        for(int i = 0; i < size; i++) {
            leaders[i]  = i;
            ranks[i] = 0;
        }
    }

    public int count() {
        return components;
    }

    public boolean connected(int site1, int site2) {
        return find(site1) == find(site2);
    }

    //O(inverse Ackermann function)
    public int find(int site) {
        if (site == leaders[site]) {
            return site;
        }

        return leaders[site] = find(leaders[site]);
    }

    //O(inverse Ackermann function)
    public void union(int site1, int site2) {

        int leader1 = find(site1);
        int leader2 = find(site2);

        if (leader1 == leader2) {
            return;
        }

        if (ranks[leader1] < ranks[leader2]) {
            leaders[leader1] = leader2;
        } else if (ranks[leader2] < ranks[leader1]) {
            leaders[leader2] = leader1;
        } else {
            leaders[leader1] = leader2;
            ranks[leader2]++;
        }

        components--;
    }

}
