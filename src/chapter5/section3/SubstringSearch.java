package chapter5.section3;

/**
 * Created by Rene Argento on 02/03/18.
 */
public interface SubstringSearch {

    int BRUTEFORCE = 0;
    int KNUTH_MORRIS_PRATT = 1;
    int BOYER_MOORE = 2;
    int RABIN_KARP = 3;

    int search(String text);
    int count(String text);
    Iterable<Integer> findAll(String text);

}
