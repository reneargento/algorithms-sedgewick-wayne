package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.Constants;
import util.FileUtil;

/**
 * Created by Rene Argento on 09/03/18.
 */
// The original text in the Tale of Two Cities file is:
// "It is a far, far better thing that I do, than I have ever done"
public class Exercise39_Timings {

    private void doExperiment() {
        String taleOfTwoCitiesFile = Constants.FILES_PATH + Constants.TALE_OF_TWO_CITIES_FILE;
        String taleOfTwoCitiesText = FileUtil.getAllCharactersFromFile(taleOfTwoCitiesFile, true, true);
        String pattern = "it is a far far better thing that I do than I have ever done";

        String[] substringSearchMethods = {
                SubstringSearch.BRUTEFORCE_METHOD,
                SubstringSearch.KNUTH_MORRIS_PRATT_METHOD,
                SubstringSearch.BOYER_MOORE_METHOD,
                SubstringSearch.RABIN_KARP_METHOD
        };

        StdOut.printf("%20s %10s\n", "Method |", "Time spent");

        for (int substringSearchMethod = 0; substringSearchMethod < substringSearchMethods.length; substringSearchMethod++) {
            SubstringSearch substringSearch;

            switch (substringSearchMethod) {
                case SubstringSearch.BRUTEFORCE:
                    substringSearch = new BruteForceSubstringSearch(pattern);
                    break;
                case SubstringSearch.KNUTH_MORRIS_PRATT:
                    substringSearch = new KnuthMorrisPratt(pattern);
                    break;
                case SubstringSearch.BOYER_MOORE:
                    substringSearch = new BoyerMoore(pattern);
                    break;
                default:
                    substringSearch = new RabinKarp(pattern, true);
                    break;
            }

            Stopwatch stopwatch = new Stopwatch();
            substringSearch.search(taleOfTwoCitiesText);
            double timeSpent = stopwatch.elapsedTime();

            printResults(substringSearchMethods[substringSearchMethod], timeSpent);
        }
    }

    private void printResults(String substringSearchMethod, double timeSpent) {
        StdOut.printf("%18s %12.2f\n", substringSearchMethod, timeSpent);
    }

    public static void main(String[] args) {
        new Exercise39_Timings().doExperiment();
    }

}
