package chapter5.section2;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;

/**
 * Created by Rene Argento on 17/02/18.
 */
public class Exercise22_TypingMonkeys {

    public double[] getFrequencyDistribution(int numberOfWordsToGenerate, double probability) {
        int wordsGenerated = 0;

        SeparateChainingHashTable<Integer, Integer> frequencyMap = new SeparateChainingHashTable<>();
        int maxWordLength = 0;

        StringSet wordsGeneratedStringSet = new StringSet();

        while (wordsGenerated < numberOfWordsToGenerate) {
            StringBuilder currentWord = new StringBuilder();

            while (true) {
                double currentProbability = StdRandom.uniform();

                int characterIndexToAppend = (int) (currentProbability / probability);

                // Valid characters are in the range [0, 25]
                boolean shouldFinishWord = characterIndexToAppend >= 26;

                if (shouldFinishWord) {

                    String word = currentWord.toString();
                    if (wordsGeneratedStringSet.contains(word)) {
                        // Word was already generated, so we do not count it
                        break;
                    } else {
                        wordsGeneratedStringSet.add(word);
                    }

                    wordsGenerated++;

                    int currentWordLength = currentWord.length();

                    if (currentWordLength > maxWordLength) {
                        maxWordLength = currentWordLength;
                    }

                    int frequencyCount = 0;

                    if (frequencyMap.contains(currentWordLength)) {
                        frequencyCount = frequencyMap.get(currentWordLength);
                    }

                    frequencyCount++;

                    frequencyMap.put(currentWordLength, frequencyCount);
                    break;
                }

                int nextCharacterIndex = Constants.ASC_II_UPPERCASE_LETTERS_INITIAL_INDEX + characterIndexToAppend;
                currentWord.append((char) nextCharacterIndex);
            }
        }

        double[] frequencies = new double[maxWordLength + 1];

        for(int wordLength : frequencyMap.keys()) {
            double wordLengthFrequency = frequencyMap.get(wordLength) / (double) numberOfWordsToGenerate;
            frequencies[wordLength] = wordLengthFrequency;
        }

        return frequencies;
    }

    // Parameters example: 0.025 1000
    public static void main(String[] args) {
        double probability = Double.parseDouble(args[0]);
        int numberOfWordsToGenerate = Integer.parseInt(args[1]);

        if (probability >= 0.03846) {
            throw new IllegalArgumentException("Probability must be less than 0.03846 (which is 1 / 26)");
        }

        // Release the monkey
        double[] frequencies = new Exercise22_TypingMonkeys().getFrequencyDistribution(numberOfWordsToGenerate, probability);

        StdOut.println("Frequency distribution estimate\n");
        for(int i = 0; i < frequencies.length; i++) {
            StdOut.printf("%12s %.3f\n", "Length " + i + ": ", frequencies[i]);
        }
    }

}
