package chapter5.section2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;

/**
 * Created by Rene Argento on 08/02/18.
 */
public class Exercise17_SpellChecking {

    private void spellChecker(String dictionaryFileName) {
        String filePath = Constants.FILES_PATH + dictionaryFileName;
        String[] wordsInDictionary = FileUtil.getAllStringsFromFile(filePath);

        String[] text = StdIn.readAllStrings();

        StdOut.println("Words not in the dictionary:");

        if (wordsInDictionary == null) {
            for (String word : text) {
                StdOut.println(word);
            }

            return;
        }

        StringSet stringSet = new StringSet();

        for (String word : wordsInDictionary) {
            stringSet.add(word);
        }

        for (String word : text) {
            if (!stringSet.contains(word)) {
                StdOut.println(word);
            }
        }
    }

    // Parameters example: 3.1.26_Dictionary.txt
    // This is the same dictionary file used in exercise 3.1.26
    // Standard input text: Djikstra sorting method Test good Algorithms

    // Expected output:
    // sorting
    // method
    // good

    // Dictionary file contents

    // Algorithms
    // ABC
    // Djikstra
    // Test
    // Rene
    // Binary
    // Sort
    public static void main(String[] args) {
        String fileName = args[0];
        new Exercise17_SpellChecking().spellChecker(fileName);
    }

}
