package chapter5.section3;

/**
 * Created by Rene Argento on 02/03/18.
 */
public interface SubstringSearch {

    int BRUTEFORCE = 0;
    int KNUTH_MORRIS_PRATT = 1;
    int BOYER_MOORE = 2;
    int RABIN_KARP = 3;

    String BRUTEFORCE_METHOD = "Bruteforce";
    String KNUTH_MORRIS_PRATT_METHOD = "Knuth-Morris-Pratt";
    String BOYER_MOORE_METHOD = "Boyer-Moore";
    String RABIN_KARP_METHOD = "Rabin-Karp";

    int search(String text);
    int count(String text);
    Iterable<Integer> findAll(String text);

}
