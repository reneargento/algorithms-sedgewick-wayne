package chapter1.section5;

/**
 * Created by Rene Argento on 09/12/16.
 */
public class WeightedQuickUnion implements UF{

    private int id[];
    private int size[];
    private int count;

    public WeightedQuickUnion(int size) {
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
        while(site != id[site]) {
            site = id[site];
        }

        return site;
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

    public int[] getSizes() {
        return size;
    }

}