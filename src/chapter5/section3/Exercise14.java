package chapter5.section3;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 25/02/18.
 */
public class Exercise14 {

    private interface SubstringSearchCharArray {
        int search(char[] text);
        int count(char[] text);
        Iterable<Integer> findAll(char[] text);
    }

    public class BruteForceSubstringSearchCharArray implements SubstringSearchCharArray {

        private char[] pattern;
        private int patternLength;

        public BruteForceSubstringSearchCharArray(char[] pattern) {
            this.pattern = pattern;
            patternLength = pattern.length;
        }

        // Search for pattern in text.
        // Returns the index of the first occurrence of the pattern string in the text string or textLength if no such match.
        public int search(char[] text) {
            return searchFromIndex(text, 0);
        }

        // Searches for the pattern in the text starting at specified index.
        private int searchFromIndex(char[] text, int textStartIndex) {
            int textLength = text.length;

            for (int textIndex = textStartIndex; textIndex <= textLength - patternLength; textIndex++) {
                int patternIndex;

                for (patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                    if (text[textIndex + patternIndex] != pattern[patternIndex]) {
                        break;
                    }
                }

                if (patternIndex == patternLength) {
                    return textIndex;  // found
                }
            }

            return textLength;        // not found
        }

        // Alternate implementation
        public int search2(char[] text) {
            int textLength = text.length;

            int textIndex;
            int patternIndex;

            for (textIndex = 0, patternIndex = 0; textIndex < textLength && patternIndex < patternLength; textIndex++) {

                if (text[textIndex] == pattern[patternIndex]) {
                    patternIndex++;
                } else {
                    textIndex -= patternIndex;
                    patternIndex = 0;
                }
            }

            if (patternIndex == patternLength) {
                return textIndex - patternLength;  // found
            } else {
                return textLength;                 // not found
            }
        }

        // Count the occurrences of pattern in the text
        public int count(char[] text) {
            int count = 0;

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                count++;
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return count;
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(char[] text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                offsets.enqueue(occurrenceIndex);
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public class KnuthMorrisPrattCharArray implements SubstringSearchCharArray {

        private char[] pattern;
        private int[][] dfa;  // deterministic-finite-automaton

        public KnuthMorrisPrattCharArray(char[] pattern) {
            if (pattern == null || pattern.length == 0) {
                throw new IllegalArgumentException("Invalid pattern");
            }

            // Build DFA from pattern
            this.pattern = pattern;

            int patternLength = pattern.length;
            int alphabetSize = 256;

            dfa = new int[alphabetSize][patternLength];
            dfa[pattern[0]][0] = 1;

            int restartState = 0;

            for (int patternIndex = 1; patternIndex < patternLength; patternIndex++) {
                // Compute dfa[][patternIndex]
                for (int currentChar = 0; currentChar < alphabetSize; currentChar++) {
                    dfa[currentChar][patternIndex] = dfa[currentChar][restartState]; // Copy mismatch cases
                }
                dfa[pattern[patternIndex]][patternIndex] = patternIndex + 1;  // Set match case
                restartState = dfa[pattern[patternIndex]][restartState];      // Update restart state
            }
        }

        // Search for pattern in text.
        // Returns the index of the first occurrence of the pattern string in the text string or textLength if no such match.
        public int search(char[] text) {
            return searchFromIndex(text, 0);
        }

        // Searches for the pattern in the text starting at specified index.
        private int searchFromIndex(char[] text, int textStartIndex) {
            int textIndex;
            int patternIndex;
            int textLength = text.length;
            int patternLength = pattern.length;

            for (textIndex = textStartIndex, patternIndex = 0; textIndex < textLength && patternIndex < patternLength;
                 textIndex++) {
                patternIndex = dfa[text[textIndex]][patternIndex];
            }
            if (patternIndex == patternLength) {
                return textIndex - patternLength; // found
            } else {
                return textLength;                // not found
            }
        }

        // Count the occurrences of pattern in the text
        public int count(char[] text) {
            int count = 0;

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                count++;
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return count;
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(char[] text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                offsets.enqueue(occurrenceIndex);
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public class BoyerMooreCharArray implements SubstringSearchCharArray {

        private int[] right;
        private char[] pattern;

        public BoyerMooreCharArray(char[] pattern) {
            this.pattern = pattern;
            int alphabetSize = 256;

            right = new int[alphabetSize];

            for (int currentChar = 0; currentChar < alphabetSize; currentChar++) {
                right[currentChar] = -1; // -1 for chars not in pattern
            }

            for (int patternIndex = 0; patternIndex < pattern.length; patternIndex++)  {
                right[pattern[patternIndex]] = patternIndex; // rightmost position for chars in pattern
            }
        }

        // Search for pattern in the text.
        // Returns the index of the first occurrence of the pattern string in the text string or textLength if no such match.
        public int search(char[] text) {
            return searchFromIndex(text, 0);
        }

        // Searches for the pattern in the text starting at specified index.
        private int searchFromIndex(char[] text, int textStartIndex) {
            int textLength = text.length;
            int patternLength = pattern.length;

            int skip;

            for (int textIndex = textStartIndex; textIndex <= textLength - patternLength; textIndex += skip) {
                // Does the pattern match the text at position textIndex?
                skip = 0;

                for (int patternIndex = patternLength - 1; patternIndex >= 0; patternIndex--) {
                    if (pattern[patternIndex] != text[textIndex + patternIndex]) {
                        skip = Math.max(1, patternIndex - right[text[textIndex + patternIndex]]);
                        break;
                    }
                }
                if (skip == 0) {
                    return textIndex; // found
                }
            }

            return textLength;        // not found
        }

        // Count the occurrences of pattern in the text
        public int count(char[] text) {
            int count = 0;

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                count++;
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return count;
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(char[] text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                offsets.enqueue(occurrenceIndex);
                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public class RabinKarpCharArray implements SubstringSearchCharArray {

        private char[] pattern;        // Only needed in the Las Vegas version
        private long patternHash;
        private int patternLength;
        private long largePrimeNumber; // a large prime, small enough to avoid long overflow
        private int alphabetSize = 256;
        private long rm;               // rm = alphabetSize^(patternLength - 1) % largePrimeNumber
        private boolean isMonteCarloVersion;

        public RabinKarpCharArray(char[] pattern, boolean isMonteCarloVersion) {
            this.pattern = pattern;
            patternLength = pattern.length;
            this.isMonteCarloVersion = isMonteCarloVersion;

            largePrimeNumber = longRandomPrime();

            rm = 1;
            for (int patternIndex = 1; patternIndex <= patternLength - 1; patternIndex++) {
                rm = (rm * alphabetSize) % largePrimeNumber;  // Compute alphabetSize^(patternLength - 1) % largePrimeNumber
            }                                                 // for use in removing leading digit.

            patternHash = hash(pattern);
        }

        // A random 31-bit prime
        private long longRandomPrime() {
            BigInteger prime = BigInteger.probablePrime(31, new Random());
            return prime.longValue();
        }

        private boolean check(char[] text, int textIndex) {
            if (isMonteCarloVersion) {
                return true;
            }

            // Las Vegas version
            for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                if (pattern[patternIndex] != text[textIndex + patternIndex]) {
                    return false;
                }
            }

            return true;
        }

        // Horner's method applied to modular hashing
        private long hash(char[] key) {
            // Compute hash for key[0..patternLength - 1]
            long hash = 0;

            for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                hash = (hash * alphabetSize + key[patternIndex]) % largePrimeNumber;
            }

            return hash;
        }

        // Search for a hash match in the text.
        // Returns the index of the first occurrence of the pattern string in the text string or textLength if no such match.
        public int search(char[] text) {
            return searchFromIndex(text, 0);
        }

        // Searches for the pattern in the text starting at specified index
        private int searchFromIndex(char[] text, int textStartIndex) {

            char[] eligibleText;

            if (textStartIndex != 0) {
                if (text.length - textStartIndex < 0) {
                    return textStartIndex + text.length; // no match
                }

                eligibleText = new char[text.length - textStartIndex];
                int eligibleTextIndex = 0;

                for (int originalTextIndex = textStartIndex; originalTextIndex < text.length; originalTextIndex++) {
                    eligibleText[eligibleTextIndex++] = text[originalTextIndex];
                }
            } else {
                eligibleText = text;
            }

            int textLength = eligibleText.length;

            if (textLength < patternLength) {
                return textStartIndex + textLength;  // no match
            }

            long textHash = hash(eligibleText);

            if (patternHash == textHash && check(eligibleText, 0)) {
                return textStartIndex;  // match
            }

            for (int textIndex = patternLength; textIndex < textLength; textIndex++) {
                // Remove leading character, add trailing character, check for match
                textHash = (textHash + largePrimeNumber - rm * eligibleText[textIndex - patternLength] % largePrimeNumber)
                        % largePrimeNumber;
                textHash = (textHash * alphabetSize + eligibleText[textIndex]) % largePrimeNumber;

                int offset = textIndex - patternLength + 1;

                if (patternHash == textHash && check(eligibleText, offset)) {
                    return textStartIndex + offset;  // match
                }
            }

            return textStartIndex + textLength;      // no match
        }

        // Count the occurrences of pattern in the text
        public int count(char[] text) {
            int count = 0;

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                count++;

                if (occurrenceIndex + 1 >= text.length) {
                    break;
                }

                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return count;
        }

        // Finds all the occurrences of pattern in the text
        public Iterable<Integer> findAll(char[] text) {
            Queue<Integer> offsets = new Queue<>();

            int occurrenceIndex = searchFromIndex(text, 0);

            while (occurrenceIndex != text.length) {
                offsets.enqueue(occurrenceIndex);

                if (occurrenceIndex + 1 >= text.length) {
                    break;
                }

                occurrenceIndex = searchFromIndex(text, occurrenceIndex + 1);
            }

            return offsets;
        }
    }

    public static void main(String[] args) {
        Exercise14 exercise14 = new Exercise14();

        StdOut.println("*** Bruteforce implementation tests ***");
        exercise14.test(SubstringSearch.BRUTEFORCE);

        StdOut.println("*** Knuth-Morris-Pratt tests ***");
        exercise14.test(SubstringSearch.KNUTH_MORRIS_PRATT);

        StdOut.println("*** Boyer-Moore tests ***");
        exercise14.test(SubstringSearch.BOYER_MOORE);

        StdOut.println("*** Rabin-Karp tests ***");
        exercise14.test(SubstringSearch.RABIN_KARP);
    }

    private void test(int substringSearchMethodId) {

        String text = "abcdrenetestreneabdreneabcdd";
        char[] textCharArray = text.toCharArray();

        char[] pattern1 = {'r', 'e', 'n', 'e'};
        SubstringSearchCharArray substringSearchCharArray1 = createSubstringSearchCharArray(substringSearchMethodId, pattern1);

        if (substringSearchCharArray1 == null) {
            return;
        }

        int search1 = substringSearchCharArray1.search(textCharArray);
        StdOut.println("Index 1: " + search1 + " Expected: 4");

        if (substringSearchCharArray1 instanceof BruteForceSubstringSearchCharArray) {
            int search2 = ((BruteForceSubstringSearchCharArray) substringSearchCharArray1).search2(textCharArray);
            StdOut.println("Index 1.2: " + search2 + " Expected: 4");
        }

        int count1 = substringSearchCharArray1.count(textCharArray);
        StdOut.println("Count 1: " + count1 + " Expected: 3");

        StringJoiner offsets1 = new StringJoiner(", ");
        for (int offset : substringSearchCharArray1.findAll(textCharArray)) {
            offsets1.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 1: " + offsets1.toString());
        StdOut.println("Expected: 4, 12, 19\n");


        char[] pattern2 = {'a', 'b', 'c', 'd'};
        SubstringSearchCharArray substringSearchCharArray2 = createSubstringSearchCharArray(substringSearchMethodId, pattern2);

        int search2 = substringSearchCharArray2.search(textCharArray);
        StdOut.println("Index 2: " + search2 + " Expected: 0");

        if (substringSearchCharArray2 instanceof BruteForceSubstringSearchCharArray) {
            int search3 = ((BruteForceSubstringSearchCharArray) substringSearchCharArray2).search2(textCharArray);
            StdOut.println("Index 2.2: " + search3 + " Expected: 0");
        }

        int count2 = substringSearchCharArray2.count(textCharArray);
        StdOut.println("Count 2: " + count2 + " Expected: 2");

        StringJoiner offsets2 = new StringJoiner(", ");
        for (int offset : substringSearchCharArray2.findAll(textCharArray)) {
            offsets2.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 2: " + offsets2.toString());
        StdOut.println("Expected: 0, 23\n");


        char[] pattern3 = {'d'};
        SubstringSearchCharArray substringSearchCharArray3 = createSubstringSearchCharArray(substringSearchMethodId, pattern3);

        int search3 = substringSearchCharArray3.search(textCharArray);
        StdOut.println("Index 3: " + search3 + " Expected: 3");

        if (substringSearchCharArray3 instanceof BruteForceSubstringSearchCharArray) {
            int search4 = ((BruteForceSubstringSearchCharArray) substringSearchCharArray3).search2(textCharArray);
            StdOut.println("Index 3.2: " + search4 + " Expected: 3");
        }

        int count3 = substringSearchCharArray3.count(textCharArray);
        StdOut.println("Count 3: " + count3 + " Expected: 4");

        StringJoiner offsets3 = new StringJoiner(", ");
        for (int offset : substringSearchCharArray3.findAll(textCharArray)) {
            offsets3.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 3: " + offsets3.toString());
        StdOut.println("Expected: 3, 18, 26, 27\n");


        char[] pattern4 = {'z', 'z', 'z'};
        SubstringSearchCharArray substringSearchCharArray4 = createSubstringSearchCharArray(substringSearchMethodId, pattern4);

        int search4 = substringSearchCharArray4.search(textCharArray);
        StdOut.println("Index 4: " + search4 + " Expected: 28");

        if (substringSearchCharArray4 instanceof BruteForceSubstringSearchCharArray) {
            int search5 = ((BruteForceSubstringSearchCharArray) substringSearchCharArray4).search2(textCharArray);
            StdOut.println("Index 4.2: " + search5 + " Expected: 28");
        }

        int count4 = substringSearchCharArray4.count(textCharArray);
        StdOut.println("Count 4: " + count4 + " Expected: 0");

        StringJoiner offsets4 = new StringJoiner(", ");
        for (int offset : substringSearchCharArray4.findAll(textCharArray)) {
            offsets4.add(String.valueOf(offset));
        }

        StdOut.println("Offsets 4: " + offsets4.toString());
        StdOut.println("Expected: \n");
    }

    private SubstringSearchCharArray createSubstringSearchCharArray(int substringSearchMethodId, char[] pattern) {
        SubstringSearchCharArray substringSearchCharArray = null;

        switch (substringSearchMethodId) {
            case SubstringSearch.BRUTEFORCE:
                substringSearchCharArray = new BruteForceSubstringSearchCharArray(pattern);
                break;
            case SubstringSearch.KNUTH_MORRIS_PRATT:
                substringSearchCharArray = new KnuthMorrisPrattCharArray(pattern);
                break;
            case SubstringSearch.BOYER_MOORE:
                substringSearchCharArray = new BoyerMooreCharArray(pattern);
                break;
            case SubstringSearch.RABIN_KARP:
                substringSearchCharArray = new RabinKarpCharArray(pattern, true);
                break;
        }

        return substringSearchCharArray;
    }

}
