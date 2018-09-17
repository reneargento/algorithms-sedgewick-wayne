package chapter5.section2;

import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;

/**
 * Created by Rene Argento on 07/02/18.
 */
public class Exercise16_DocumentSimilarity {

    private static class KGramInformation {
        private int frequencyInFile1;
        private int frequencyInFile2;

        KGramInformation(int frequencyInFile1, int frequencyInFile2) {
            this.frequencyInFile1 = frequencyInFile1;
            this.frequencyInFile2 = frequencyInFile2;
        }
    }

    private static final int FILE1_ID = 1;
    private static final int FILE2_ID = 2;

    public static double computeKSimilarity(int k, String fileName1, String fileName2) {
        String filePath1 = Constants.FILES_PATH + fileName1;
        String filePath2 = Constants.FILES_PATH + fileName2;

        String charactersInFile1 = FileUtil.getAllCharactersFromFile(filePath1, false, false);
        String charactersInFile2 = FileUtil.getAllCharactersFromFile(filePath2, false, false);

        if (charactersInFile1 == null || charactersInFile2 == null) {
            return Double.POSITIVE_INFINITY;
        }

        TernarySearchTrie<KGramInformation> ternarySearchTrie = new TernarySearchTrie<>();

        int numberOfKGramsInFile1 = countAndPutKGramsInTST(charactersInFile1, ternarySearchTrie, k, FILE1_ID);
        int numberOfKGramsInFile2 = countAndPutKGramsInTST(charactersInFile2, ternarySearchTrie, k, FILE2_ID);

        double sumOfDistances = 0;

        for (String key : ternarySearchTrie.keys()) {
            KGramInformation kGramInformation = ternarySearchTrie.get(key);

            double frequency1 = kGramInformation.frequencyInFile1 / (double) numberOfKGramsInFile1;
            double frequency2 = kGramInformation.frequencyInFile2 / (double) numberOfKGramsInFile2;

            sumOfDistances += Math.pow(frequency1 - frequency2, 2);
        }

        return Math.sqrt(sumOfDistances);
    }

    private static int countAndPutKGramsInTST(String wordsInFile, TernarySearchTrie<KGramInformation> ternarySearchTrie,
                                              int k, int fileId) {
        int numberOfKGramsInFile = 0;

        for(int word = 0; word <= wordsInFile.length() - k; word++) {
            StringBuilder kGram = new StringBuilder();
            numberOfKGramsInFile++;

            for (int currentK = word; currentK < word + k; currentK++) {
                kGram.append(wordsInFile.charAt(currentK));
            }

            String kGramValue = kGram.toString();

            if (ternarySearchTrie.contains(kGramValue)) {
                KGramInformation kGramInformation = ternarySearchTrie.get(kGramValue);

                if (fileId == FILE1_ID) {
                    kGramInformation.frequencyInFile1++;
                } else if (fileId == FILE2_ID) {
                    kGramInformation.frequencyInFile2++;
                }
            } else {
                if (fileId == FILE1_ID) {
                    ternarySearchTrie.put(kGramValue, new KGramInformation(1, 0));
                } else if (fileId == FILE2_ID) {
                    ternarySearchTrie.put(kGramValue, new KGramInformation(0, 1));
                }
            }
        }

        return numberOfKGramsInFile;
    }

    // Parameters example: 3 5.2.16_file1.txt 5.2.16_file2.txt 5.2.16_file3.txt

    // File contents
    //
    // 5.2.16_file1.txt
    // this is a file with some strings
    //
    // 5.2.16_file2.txt
    // this is a combination of words that mention a few sorting algorithms
    // such as bubblesort and quicksort algorithms
    // one of them is a fast sorting algorithm
    // one of them is not
    //
    // 5.2.16_file3.txt
    // this is a file with some nice strings

    // Expected output:
    //  0.00   0.22   0.09
    //  0.22   0.00   0.20
    //  0.09   0.20   0.00
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        int numberOfFiles = args.length - 1;
        String[] fileNames = new String[numberOfFiles];

        for(int i = 1; i < args.length; i++) {
            fileNames[i - 1] = args[i];
        }

        double[][] kSimilarityMatrix = new double[numberOfFiles][numberOfFiles];

        for (int file1 = 0; file1 < numberOfFiles; file1++) {
            for (int file2 = file1 + 1; file2 < numberOfFiles; file2++) {
                double kSimilarity = computeKSimilarity(k, fileNames[file1], fileNames[file2]);

                kSimilarityMatrix[file1][file2] = kSimilarity;
                kSimilarityMatrix[file2][file1] = kSimilarity;
            }
        }

        StdOut.println("K-Similarity matrix");

        for (int file1 = 0; file1 < numberOfFiles; file1++) {
            for (int file2 = 0; file2 < numberOfFiles; file2++) {
                StdOut.printf("%5.2f  ", kSimilarityMatrix[file1][file2]);
            }
            StdOut.println();
        }

    }

}
