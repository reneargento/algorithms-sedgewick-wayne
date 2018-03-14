package chapter3.section5;

import chapter3.section4.LinearProbingHashTable;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.Constants;

/**
 * Created by Rene Argento on 20/08/17.
 */
public class Exercise32_Dictionary {

    public class LookupCSV {

        public void lookup(String filePath, int keyField, int valueField, int numberOfQueries) {
            In in = new In(filePath);

            LinearProbingHashTable<String, String> symbolTable = new LinearProbingHashTable<>(997);

            // Using Java's set to avoid duplicate keys and to transform it in an array with the toArray() method later
            java.util.Set<String> keys = new java.util.HashSet<>();

            boolean isTitleRow = true;

            while (in.hasNextLine()) {
                String line = in.readLine();

                if (isTitleRow) {
                    isTitleRow = false;
                    continue;
                }

                String[] tokens = line.split(",");
                String key = tokens[keyField];
                String value = tokens[valueField];
                symbolTable.put(key, value);

                keys.add(key);
            }

            String[] keysArray = keys.toArray(new String[keys.size()]);

            // Queries
            for(int i = 0; i < numberOfQueries; i++) {
                // Randomly chooses if this will be a key hit or a key miss query
                int keyHit = StdRandom.uniform(2);
                boolean isKeyHit = keyHit == 1;

                String query;

                if (!isKeyHit) {
                    query = generateRandomKey();
                } else {
                    int randomKeyIndex = StdRandom.uniform(keysArray.length);
                    query = keysArray[randomKeyIndex];
                }

                if (symbolTable.contains(query)) {
                    symbolTable.get(query);
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

    private void doExperiment() {
        LookupCSV lookupCSV = new LookupCSV();

        int[] numberOfQueries = {100000, 1000000, 10000000, 100000000, 1000000000};
        String[] filePaths = {LARGE_INPUT_FILE_PATH1, LARGE_INPUT_FILE_PATH2};
        String[] fileNames = {Constants.SURNAMES_CSV_FILE, Constants.SDSS_CSV_FILE};

        StdOut.printf("%18s %20s %10s\n", "Large input | ", "Number of queries | ", "Time spent");

        for(int q = 0; q < numberOfQueries.length; q++) {
            for(int f = 0; f < filePaths.length; f++) {
                In in = new In(filePaths[f]);
                String line = in.readLine();
                String[] tokens = line.split(",");

                int randomKeyIndex = StdRandom.uniform(tokens.length);
                int randomValueIndex = StdRandom.uniform(tokens.length);

                Stopwatch stopwatch = new Stopwatch();
                lookupCSV.lookup(filePaths[f], randomKeyIndex, randomValueIndex, numberOfQueries[q]);
                double timeSpent = stopwatch.elapsedTime();

                printResults(fileNames[f], numberOfQueries[q], timeSpent);
            }
        }
    }

    private void printResults(String fileName, int numberOfQueries, double timeSpent) {
        StdOut.printf("%15s %20d %13.2f\n", fileName, numberOfQueries, timeSpent);
    }

    public static void main(String[] args) {
        new Exercise32_Dictionary().doExperiment();
    }

}
