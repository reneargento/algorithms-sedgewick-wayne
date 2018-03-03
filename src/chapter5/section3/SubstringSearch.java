package chapter5.section3;

/**
 * Created by Rene Argento on 02/03/18.
 */
public interface SubstringSearch {

    int search(String text);
    int count(String text);
    Iterable<Integer> findAll(String text);

}
