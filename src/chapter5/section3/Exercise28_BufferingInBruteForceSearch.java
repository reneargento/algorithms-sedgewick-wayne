package chapter5.section3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 03/03/18.
 */
public class Exercise28_BufferingInBruteForceSearch {

    public class BruteForceSubstringSearchBuffer extends BruteForceSubstringSearch {

        BruteForceSubstringSearchBuffer(String pattern) {
            super(pattern);
        }

        public int search(In inputStream) {
            int textIndex;

            // Circular queue
            char[] buffer = new char[patternLength];

            int startBufferIndex = 0;
            int endBufferIndex = -1;
            int totalTextIndex = 0;

            for (textIndex = 0; inputStream.hasNextChar(); textIndex++) {

                // Make sure the buffer has the required characters
                while (textIndex + patternLength - 1 >= totalTextIndex) {
                    if (inputStream.hasNextChar()) {

                        if (endBufferIndex + 1 == buffer.length) {
                            endBufferIndex = 0;
                        } else {
                            endBufferIndex = endBufferIndex + 1;
                        }

                        buffer[endBufferIndex] = inputStream.readChar();
                        totalTextIndex++;
                    } else {
                        return totalTextIndex; // not found
                    }
                }

                int patternIndex;

                for (patternIndex = 0; patternIndex < patternLength; patternIndex++) {
                    int bufferIndex;

                    if (startBufferIndex + patternIndex >= buffer.length) {
                        bufferIndex = patternIndex - (buffer.length - startBufferIndex);
                    } else {
                        bufferIndex = startBufferIndex + patternIndex;
                    }

                    if (buffer[bufferIndex] != pattern.charAt(patternIndex)) {
                        break;
                    }
                }

                if (startBufferIndex + 1 == buffer.length) {
                    startBufferIndex = 0;
                } else {
                    startBufferIndex = startBufferIndex + 1;
                }

                if (patternIndex == patternLength) {
                    return textIndex;    // found
                }
            }

            // No need to clear the buffer since it is a local variable and will be collected by the garbage collector
            // once the method returns.
            return totalTextIndex;     // not found
        }
    }

    // File contents: abacadabrabracabracadabrabrabracad
    public static void main(String[] args) {
        Exercise28_BufferingInBruteForceSearch bufferingInBruteForceSearch = new Exercise28_BufferingInBruteForceSearch();

        String filePath = Constants.FILES_PATH + Constants.STREAMING_FILE;

        String pattern1 = "abracadabra";
        In inputStream1 = new In(filePath);
        BruteForceSubstringSearchBuffer bruteForceSubstringSearchBuffer1 =
                bufferingInBruteForceSearch.new BruteForceSubstringSearchBuffer(pattern1);
        int index1 = bruteForceSubstringSearchBuffer1.search(inputStream1);
        StdOut.println("Index 1: " + index1 + " Expected: 14");
        inputStream1.close();

        String pattern2 = "rab";
        In inputStream2 = new In(filePath);
        BruteForceSubstringSearchBuffer bruteForceSubstringSearchBuffer2 =
                bufferingInBruteForceSearch.new BruteForceSubstringSearchBuffer(pattern2);
        int index2 = bruteForceSubstringSearchBuffer2.search(inputStream2);
        StdOut.println("Index 2: " + index2 + " Expected: 8");
        inputStream2.close();

        String pattern3 = "bcara";
        In inputStream3 = new In(filePath);
        BruteForceSubstringSearchBuffer bruteForceSubstringSearchBuffer3 =
                bufferingInBruteForceSearch.new BruteForceSubstringSearchBuffer(pattern3);
        int index3 = bruteForceSubstringSearchBuffer3.search(inputStream3);
        StdOut.println("Index 3: " + index3 + " Expected: 34");
        inputStream3.close();

        String pattern4 = "rabrabracad";
        In inputStream4 = new In(filePath);
        BruteForceSubstringSearchBuffer bruteForceSubstringSearchBuffer4 =
                bufferingInBruteForceSearch.new BruteForceSubstringSearchBuffer(pattern4);
        int index4 = bruteForceSubstringSearchBuffer4.search(inputStream4);
        StdOut.println("Index 4: " + index4 + " Expected: 23");
        inputStream4.close();

        String pattern5 = "abacad";
        In inputStream5 = new In(filePath);
        BruteForceSubstringSearchBuffer bruteForceSubstringSearchBuffer5 =
                bufferingInBruteForceSearch.new BruteForceSubstringSearchBuffer(pattern5);
        int index5 = bruteForceSubstringSearchBuffer5.search(inputStream5);
        StdOut.println("Index 5: " + index5 + " Expected: 0");
        inputStream5.close();

        String pattern6 = "renerenerenerenerenerenerenerenerene";
        In inputStream6 = new In(filePath);
        BruteForceSubstringSearchBuffer bruteForceSubstringSearchBuffer6 =
                bufferingInBruteForceSearch.new BruteForceSubstringSearchBuffer(pattern6);
        int index6 = bruteForceSubstringSearchBuffer6.search(inputStream6);
        StdOut.println("Index 6: " + index6 + " Expected: 34");
        inputStream5.close();
    }

}
