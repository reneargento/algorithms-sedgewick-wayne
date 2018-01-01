package chapter4.section1;

/**
 * Created by Rene Argento on 18/09/17.
 */
public interface ConnectedComponents {

    boolean connected(int vertex1, int vertex2);
    int id(int vertex);
    int count();

}
