package chapter5.section3;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 02/03/18.
 */
public class Exercise24_FindAllOccurrences {

    public class BruteForceSubstringSearchFindAll extends BruteForceSubstringSearch {

        BruteForceSubstringSearchFindAll(String pattern) {
            super(pattern);
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(String text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length()) {
                offsets.enqueue(occurrenceIndex);
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public class KnuthMorrisPrattFindAll extends KnuthMorrisPratt {

        KnuthMorrisPrattFindAll(String pattern) {
            super(pattern);
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(String text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length()) {
                offsets.enqueue(occurrenceIndex);
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public class BoyerMooreFindAll extends BoyerMoore {

        BoyerMooreFindAll(String pattern) {
            super(pattern);
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(String text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length()) {
                offsets.enqueue(occurrenceIndex);
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public class RabinKarpFindAll extends RabinKarp {

        RabinKarpFindAll(String pattern, boolean isMonteCarloVersion) {
            super(pattern, isMonteCarloVersion);
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(String text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length()) {
                offsets.enqueue(occurrenceIndex);

                if (occurrenceIndex + 1 >= text.length()) {
                    break;
                }

                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public static void main(String[] args) {
        Exercise24_FindAllOccurrences findAllOccurrences = new Exercise24_FindAllOccurrences();

        StdOut.println("*** Bruteforce implementation tests ***");
        findAllOccurrences.test(SubstringSearch.BRUTEFORCE);

        StdOut.println("*** Knuth-Morris-Pratt tests ***");
        findAllOccurrences.test(SubstringSearch.KNUTH_MORRIS_PRATT);

        StdOut.println("*** Boyer-Moore tests ***");
        findAllOccurrences.test(SubstringSearch.BOYER_MOORE);

        StdOut.println("*** Rabin-Karp tests ***");
        findAllOccurrences.test(SubstringSearch.RABIN_KARP);
    }

    private void test(int substringSearchMethodId) {

        String text = "abcdrenetestreneabdreneabcdd";

        String pattern1 = "rene";
        SubstringSearch substringSearch1 = createSubstringSearch(substringSearchMethodId, pattern1);

        if (substringSearch1 == null) {
            return;
        }

        StringJoiner offsets1 = new StringJoiner(", ");
        for (int offset : substringSearch1.findAll(text)) {
            offsets1.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 1: " + offsets1.toString());
        StdOut.println("Expected: 4, 12, 19\n");


        String pattern2 = "abcd";
        SubstringSearch substringSearch2 = createSubstringSearch(substringSearchMethodId, pattern2);

        StringJoiner offsets2 = new StringJoiner(", ");
        for (int offset : substringSearch2.findAll(text)) {
            offsets2.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 2: " + offsets2.toString());
        StdOut.println("Expected: 0, 23\n");


        String pattern3 = "d";
        SubstringSearch substringSearch3 = createSubstringSearch(substringSearchMethodId, pattern3);

        StringJoiner offsets3 = new StringJoiner(", ");
        for (int offset : substringSearch3.findAll(text)) {
            offsets3.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 3: " + offsets3.toString());
        StdOut.println("Expected: 3, 18, 26, 27\n");


        String pattern4 = "zzz";
        SubstringSearch substringSearch4 = createSubstringSearch(substringSearchMethodId, pattern4);

        StringJoiner offsets4 = new StringJoiner(", ");
        for (int offset : substringSearch4.findAll(text)) {
            offsets4.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 4: " + offsets4.toString());
        StdOut.println("Expected: \n");
    }

    private SubstringSearch createSubstringSearch(int substringSearchMethodId, String pattern) {
        SubstringSearch substringSearch = null;

        switch (substringSearchMethodId) {
            case SubstringSearch.BRUTEFORCE:
                substringSearch = new BruteForceSubstringSearchFindAll(pattern);
                break;
            case SubstringSearch.KNUTH_MORRIS_PRATT:
                substringSearch = new KnuthMorrisPrattFindAll(pattern);
                break;
            case SubstringSearch.BOYER_MOORE:
                substringSearch = new BoyerMooreFindAll(pattern);
                break;
            case SubstringSearch.RABIN_KARP:
                substringSearch = new RabinKarpFindAll(pattern, true);
                break;
        }

        return substringSearch;
    }

}
