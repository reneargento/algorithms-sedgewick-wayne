package chapter5.section2;

import edu.princeton.cs.algs4.*;
import util.Constants;

/**
 * Created by Rene Argento on 18/02/18.
 */
public class Exercise26_Indexing {

    public class LookupIndex {

        public void lookupIndex(TrieInterface<Queue<String>> trie1, TrieInterface<Queue<String>> trie2, String filePath,
                                String separator, int numberOfQueries) {
            In in = new In(filePath);

            // Using Java's set to avoid duplicate keys and to transform it in an array with the toArray() method later
            java.util.Set<String> keys = new java.util.HashSet<>();

            boolean isTitleRow = true;

            while (in.hasNextLine()) {

                String[] tokens = in.readLine().split(separator);

                if (isTitleRow) {
                    isTitleRow = false;
                    continue;
                }

                String key = tokens[0];

                for(int i = 1; i < tokens.length; i++) {
                    String value = tokens[i];

                    if (!trie1.contains(key)) {
                        trie1.put(key, new Queue<>());
                    }
                    if (!trie2.contains(value)) {
                        trie2.put(value, new Queue<>());
                    }

                    trie1.get(key).enqueue(value);
                    trie2.get(value).enqueue(key);

                    keys.add(value);
                }
                keys.add(key);
            }

            String[] keysArray = keys.toArray(new String[keys.size()]);

            // Queries
            for(int i = 0; i < numberOfQueries; i++) {
                // Randomly chooses if this query will be a key hit or a key miss
                int keyHit = StdRandom.uniform(2);
                boolean isKeyHit = keyHit == 1;

                String query;

                if (!isKeyHit) {
                    query = generateRandomKey();
                } else {
                    int randomKeyIndex = StdRandom.uniform(keysArray.length);
                    query = keysArray[randomKeyIndex];
                }

                if (trie1.contains(query)) {
                    for(String value : trie1.get(query)) {
                        //Loop through values
                    }
                }

                if (trie2.contains(query)) {
                    for(String value : trie2.get(query)) {
                        //Loop through values
                    }
                }
            }
        }

        private String generateRandomKey() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < 5; i++) {
                int ascIIRandomValue = StdRandom.uniform(Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX,
                        Constants.ASC_II_LOWERCASE_LETTERS_FINAL_INDEX + 1);
                stringBuilder.append(((char) ascIIRandomValue));
            }

            return stringBuilder.toString();
        }
    }

    private static final String LARGE_INPUT_FILE_PATH1 = Constants.FILES_PATH + Constants.SURNAMES_CSV_FILE;
    private static final String LARGE_INPUT_FILE_PATH2 = Constants.FILES_PATH + Constants.SDSS_CSV_FILE;
    private static final String SEPARATOR = ",";

    private void doExperiment() {
        LookupIndex lookupIndex = new LookupIndex();

        int[] numberOfQueries = {100000, 1000000, 10000000, 100000000, 1000000000};
        String[] filePaths = {LARGE_INPUT_FILE_PATH1, LARGE_INPUT_FILE_PATH2};
        String[] fileNames = {Constants.SURNAMES_CSV_FILE, Constants.SDSS_CSV_FILE};
        String[] dataStructures = {"Trie", "Ternary Search Trie"};

        StdOut.printf("%21s %19s %20s %10s\n", "Data structure |", "Large input | ", "Number of queries | ",
                "Time spent");

        for (int trieType = 0; trieType < 2; trieType++) {
            for(int q = 0; q < numberOfQueries.length; q++) {
                for(int f = 0; f < filePaths.length; f++) {

                    TrieInterface<Queue<String>> trie1;
                    TrieInterface<Queue<String>> trie2;

                    if (trieType == 0) {
                        trie1 = new Trie<>();
                        trie2 = new Trie<>();
                    } else {
                        trie1 = new TernarySearchTrie<>();
                        trie2 = new TernarySearchTrie<>();
                    }

                    Stopwatch stopwatch = new Stopwatch();
                    lookupIndex.lookupIndex(trie1, trie2, filePaths[f], SEPARATOR, numberOfQueries[q]);
                    double timeSpent = stopwatch.elapsedTime();

                    printResults(dataStructures[trieType], fileNames[f], numberOfQueries[q], timeSpent);
                }
            }
        }
    }

    private void printResults(String dataStructure, String fileName, int numberOfQueries, double timeSpent) {
        StdOut.printf("%19s %18s %20d %13.2f\n", dataStructure, fileName, numberOfQueries, timeSpent);
    }

    public static void main(String[] args) {
        new Exercise26_Indexing().doExperiment();
    }

}
