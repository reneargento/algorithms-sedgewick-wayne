package chapter1.section5;

/**
 * Created by Rene Argento on 09/12/16.
 */
public interface UF {

    int count();
    int find(int site);
    boolean connected(int site1, int site2);
    void union(int site1, int site2);

}
