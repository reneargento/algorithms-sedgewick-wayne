package chapter1.section5;

/**
 * Created by Rene Argento on 09/12/16.
 */
public class QuickFind implements UF {

    private int[] id;
    private int count;

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
