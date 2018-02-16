package chapter5.section2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 08/02/18.
 */
public class Exercise18_Whitelist {

    // Outputs any key that is not in the whitelist and ignores any key that is in the whitelist
    private void whitelistSection1Version(String fileName, String[] keys) {

        String filePath = Constants.FILES_PATH + fileName;
        String[] keysInWhitelist = FileUtil.getAllStringsFromFile(filePath);

        StringJoiner output = new StringJoiner(" ");

        if (keysInWhitelist == null) {
            for(String key : keys) {
                output.add(key);
            }

            StdOut.println(output);
            return;
        }

        TernarySearchTrie<Integer> ternarySearchTrie = new TernarySearchTrie<>();

        for (String key : keysInWhitelist) {
            ternarySearchTrie.put(key, 0); // Value is not used
        }

        for (String key : keys) {
            if (!ternarySearchTrie.contains(key)) {
                output.add(key);
            }
        }

        StdOut.println(output);
    }

    // Outputs any key that is in the whitelist and ignores any key that is not in the whitelist
    private void whitelistSection3Version(String fileName, String[] keys) {
        String filePath = Constants.FILES_PATH + fileName;
        String[] keysInWhitelist = FileUtil.getAllStringsFromFile(filePath);

        if (keysInWhitelist == null) {
            return;
        }

        TernarySearchTrie<Integer> ternarySearchTrie = new TernarySearchTrie<>();

        for (String key : keysInWhitelist) {
            ternarySearchTrie.put(key, 0); // Value is not used
        }

        StringJoiner output = new StringJoiner(" ");

        for (String key : keys) {
            if (ternarySearchTrie.contains(key)) {
                output.add(key);
            }
        }

        StdOut.println(output);
    }

    // Parameters example: 5.2.18_whitelist.txt
    // Standard input text: it was the best of times, it was the worst of times

    // Whitelist file contents
    // was it the of
    public static void main(String[] args) {
        Exercise18_Whitelist whitelist = new Exercise18_Whitelist();

        String fileName = args[0];
        String[] keys = StdIn.readAllStrings();

        StdOut.println("Whitelist as defined in section 1.1");
        whitelist.whitelistSection1Version(fileName, keys);

        StdOut.println("Expected: best times, worst times");

        StdOut.println("\nWhitelist as defined in section 3.5");
        whitelist.whitelistSection3Version(fileName, keys);

        StdOut.println("Expected: it was the of it was the of");
    }

}
