package chapter3.section5;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rene Argento on 08/08/17.
 */
public class Exercise21_InvertedConcordance {

    private class InvertedConcordance {

        int numberOfWords = 0;

        private Map<String, List<Integer>> readConcordanceFromInput() {
            Map<String, List<Integer>> concordanceMap = new HashMap<>();

            while (StdIn.hasNextLine()) {
                String concordanceLine = StdIn.readLine();
                String[] concordanceInformation = concordanceLine.split(" ");

                String key = concordanceInformation[0];
                concordanceMap.put(key, new ArrayList<>());

                for(int i = 1; i < concordanceInformation.length; i++) {
                    String noCommaValue = concordanceInformation[i];

                    if (noCommaValue.charAt(noCommaValue.length() - 1) == ',') {
                        noCommaValue = noCommaValue.substring(0, noCommaValue.length() - 1);
                    }

                    int position = Integer.parseInt(noCommaValue);
                    concordanceMap.get(key).add(position);

                    if (position > numberOfWords) {
                        numberOfWords = position;
                    }
                }
            }

            return concordanceMap;
        }

        private String buildTextFromConcordance(Map<String, List<Integer>> concordance) {
            String[] wordsInText = new String[numberOfWords + 1];

            for(String word : concordance.keySet()) {
                for(int position : concordance.get(word)) {
                    wordsInText[position] = word;
                }
            }

            StringBuilder text = new StringBuilder();
            boolean isFirstWord = true;

            for(String word : wordsInText) {
                if (isFirstWord) {
                    isFirstWord = false;
                } else {
                    text.append(" ");
                }

                text.append(word);
            }

            return text.toString();
        }

    }

    public static void main(String[] args) {
        //Test
        // This 0, 13
        // is 1, 14
        // a 2, 6, 15
        // text 3, 9
        // to 4
        // test 5
        // concordance. 7
        // The 8
        // has 10
        // many 11
        // words. 12
        // good 16
        // test. 17
        //
        //Expected output
        //  This is a text to test a concordance. The text has many words. This is a good test.

        Exercise21_InvertedConcordance exercise21_invertedConcordance = new Exercise21_InvertedConcordance();
        InvertedConcordance invertedConcordance = exercise21_invertedConcordance.new InvertedConcordance();

        Map<String, List<Integer>> concordance = invertedConcordance.readConcordanceFromInput();
        String text = invertedConcordance.buildTextFromConcordance(concordance);
        StdOut.println(text);
    }

}
