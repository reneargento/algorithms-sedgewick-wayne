package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 09/04/17.
 */
// Thanks to Vivek Bhojawala (https://github.com/VBhojawala) for suggesting a simpler code to solve this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/15
public class Exercise2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> wordList = new ArrayList<>();

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

        // Expected:
        // afterthought
        // beginend
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
    private List<String> getCompoundWords(ArrayList<String> wordList) {
        Collections.sort(wordList);
        Set<String> wordsSet = new HashSet<>(wordList);

        List<String> compoundWords = new ArrayList<>();

        for(int i = 0; i < wordList.size(); i++) {
            for(int j = i + 1; j < wordList.size(); j++) {
                if (wordList.get(j).startsWith(wordList.get(i))) {

                    String restOfTheWord = wordList.get(j).substring(wordList.get(i).length());
                    if (wordsSet.contains(restOfTheWord)) {
                        compoundWords.add(wordList.get(j));
                    }
                } else {
                    break;
                }
            }
        }

        return compoundWords;
    }
}
