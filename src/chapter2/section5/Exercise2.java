package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 09/04/17.
 */
public class Exercise2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> wordList = new ArrayList<>();

        /**
         * Testcase
         *
         begin
         end
         test
         afterthought
         Brazil
         thought
         after
         beginend
         abc
         */
        while(scanner.hasNext()) {
            wordList.add(scanner.next());
        }

        Exercise2 exercise2 = new Exercise2();
        List<String> compoundWords = exercise2.getCompoundWords(wordList);

        if (compoundWords.size() > 0) {
            StdOut.println("Compound words:");

            for(String compoundWord : compoundWords) {
                StdOut.println(compoundWord);
            }
        }
    }

    // O(n^2)
    private List<String> getCompoundWords(List<String> wordList) {
        Set<String> wordsSet = new HashSet<>();

        String[] words = new String[wordList.size()];
        int index = 0;

        for(String word : wordList) {
            words[index++] = word;
            wordsSet.add(word);
        }

        Arrays.sort(words);

        List<String> compoundWords = new ArrayList<>();

        for(int i = 0; i < words.length; i++) {
            for(int j = i + 1; j < words.length; j++) {
                // Does words[j] contain words[i] in its beginning?
                if (words[j].length() >= words[i].length() && words[j].startsWith(words[i])) {

                    String restOfTheWord = words[j].substring(words[i].length());
                    if (wordsSet.contains(restOfTheWord)) {
                        compoundWords.add(words[j]);
                    }
                } else {
                    break;
                }
            }
        }

        return compoundWords;
    }
}
