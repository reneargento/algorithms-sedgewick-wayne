package chapter5.section3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 03/03/18.
 */
public class Exercise25_Streaming {

    private interface SubstringSearchStreaming {
        int search(In inputStream);
    }

    public class KnuthMorrisPrattStreaming extends KnuthMorrisPratt implements SubstringSearchStreaming {

        KnuthMorrisPrattStreaming(String pattern) {
            super(pattern);
        }

        public int search(In inputStream) {
            // Uses only local variables, with no extra instance variables
            int patternIndex = 0;
            int textIndex = 0;

            while (inputStream.hasNextChar() && patternIndex < pattern.length()) {
                patternIndex = dfa[inputStream.readChar()][patternIndex];
                textIndex++;
            }

            if (patternIndex == pattern.length()) {
                return textIndex - pattern.length(); // found
            } else {
                return textIndex;                    // not found
            }
        }
    }

    public class RabinKarpStreaming extends RabinKarp implements  SubstringSearchStreaming {

        RabinKarpStreaming(String pattern) {
            super(pattern, true);
        }

        public int search(In inputStream) {
            // Uses only local variables, with no extra instance variables
            int textIndex;

            // Maintains a circular queue as a buffer of the last patternLength characters to use when
            // removing the leading digit in the rolling hash computation.
            char[] buffer = new char[patternLength];

            // Compute hash and initialize buffer
            long textHash = hash(inputStream, buffer);

            int endBufferIndex = buffer.length - 1;

            if (patternHash == textHash) {
                return 0;  // match
            }

            for (textIndex = patternLength; inputStream.hasNextChar(); textIndex++) {
                // Remove leading character, add trailing character, check for match
                int leadingDigitIndex;

                if (endBufferIndex + 1 - patternLength < 0) {
                    leadingDigitIndex = patternLength - (endBufferIndex + 1);
                    leadingDigitIndex = patternLength - leadingDigitIndex;
                } else {
                    leadingDigitIndex = endBufferIndex + 1 - patternLength;
                }

                char leadingDigit = buffer[leadingDigitIndex];
                char nextChar = inputStream.readChar();

                textHash = (textHash + largePrimeNumber - rm * leadingDigit % largePrimeNumber)
                        % largePrimeNumber;
                textHash = (textHash * alphabetSize + nextChar) % largePrimeNumber;

                int offset = textIndex - patternLength + 1;

                if (patternHash == textHash) {
                    return offset;  // match
                }

                if (endBufferIndex + 1 == buffer.length) {
                    endBufferIndex = 0;
                } else {
                    endBufferIndex++;
                }
                buffer[endBufferIndex] = nextChar;
            }

            return textIndex;       // no match
        }

        // Horner's method applied to modular hashing
        private long hash(In inputStream, char[] buffer) {
            // Compute hash for the first patternLength characters in inputStream
            long hash = 0;

            for (int patternIndex = 0; inputStream.hasNextChar() && patternIndex < patternLength; patternIndex++) {
                char nextChar = inputStream.readChar();
                buffer[patternIndex] = nextChar;

                hash = (hash * alphabetSize + nextChar) % largePrimeNumber;
            }

            return hash;
        }
    }

    public static void main(String[] args) {
        Exercise25_Streaming streaming = new Exercise25_Streaming();

        StdOut.println("*** Knuth-Morris-Pratt streaming tests ***");
        streaming.test(SubstringSearch.KNUTH_MORRIS_PRATT);

        StdOut.println("*** Rabin-Karp streaming tests ***");
        streaming.test(SubstringSearch.RABIN_KARP);
    }

    // File contents: abacadabrabracabracadabrabrabracad
    private void test(int substringSearchMethodId) {

        String filePath = Constants.FILES_PATH + Constants.STREAMING_FILE;

        String pattern1 = "abracadabra";
        In inputStream1 = new In(filePath);
        SubstringSearchStreaming substringSearchStreaming1 = createSubstringSearchStreaming(substringSearchMethodId,
                pattern1);
        int index1 = substringSearchStreaming1.search(inputStream1);
        StdOut.println("Index 1: " + index1 + " Expected: 14");
        inputStream1.close();

        String pattern2 = "rab";
        In inputStream2 = new In(filePath);
        SubstringSearchStreaming substringSearchStreaming2 = createSubstringSearchStreaming(substringSearchMethodId,
                pattern2);
        int index2 = substringSearchStreaming2.search(inputStream2);
        StdOut.println("Index 2: " + index2 + " Expected: 8");
        inputStream2.close();

        String pattern3 = "bcara";
        In inputStream3 = new In(filePath);
        SubstringSearchStreaming substringSearchStreaming3 = createSubstringSearchStreaming(substringSearchMethodId,
                pattern3);
        int index3 = substringSearchStreaming3.search(inputStream3);
        StdOut.println("Index 3: " + index3 + " Expected: 34");
        inputStream3.close();

        String pattern4 = "rabrabracad";
        In inputStream4 = new In(filePath);
        SubstringSearchStreaming substringSearchStreaming4 = createSubstringSearchStreaming(substringSearchMethodId,
                pattern4);
        int index4 = substringSearchStreaming4.search(inputStream4);
        StdOut.println("Index 4: " + index4 + " Expected: 23");
        inputStream4.close();

        String pattern5 = "abacad";
        In inputStream5 = new In(filePath);
        SubstringSearchStreaming substringSearchStreaming5 = createSubstringSearchStreaming(substringSearchMethodId,
                pattern5);
        int index5 = substringSearchStreaming5.search(inputStream5);
        StdOut.println("Index 5: " + index5 + " Expected: 0\n");
        inputStream5.close();
    }

    private SubstringSearchStreaming createSubstringSearchStreaming(int substringSearchMethodId, String pattern) {
        SubstringSearchStreaming substringSearchStreaming = null;

        switch (substringSearchMethodId) {
            case SubstringSearch.KNUTH_MORRIS_PRATT:
                substringSearchStreaming = new KnuthMorrisPrattStreaming(pattern);
                break;
            case SubstringSearch.RABIN_KARP:
                substringSearchStreaming = new RabinKarpStreaming(pattern);
                break;
        }

        return substringSearchStreaming;
    }

}
