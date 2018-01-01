package chapter4.section1;

/**
 * Created by Rene Argento on 13/10/17.
 */
public interface GraphInterface {

    int vertices();
    int edges();
    Iterable<Integer> adjacent(int vertex);

}
