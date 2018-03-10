package chapter5.section3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 04/03/18.
 */
public class Exercise29_BufferingInBoyerMoore {

    public class BoyerMooreBuffer extends BoyerMoore {

        BoyerMooreBuffer(String pattern) {
            super(pattern);
        }

        public int search(In inputStream) {
            int textIndex;
            int patternLength = pattern.length();

            // Circular queue
            char[] buffer = new char[patternLength];

            int startBufferIndex = 0;
            int endBufferIndex = -1;
            int totalTextIndex = 0;

            int skip;

            for (textIndex = 0; inputStream.hasNextChar(); textIndex += skip) {

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

                skip = 0;

                for (int patternIndex = patternLength - 1; patternIndex >= 0; patternIndex--) {
                    int bufferIndex;

                    if (startBufferIndex + patternIndex >= buffer.length) {
                        bufferIndex = patternIndex - (buffer.length - startBufferIndex);
                    } else {
                        bufferIndex = startBufferIndex + patternIndex;
                    }

                    if (pattern.charAt(patternIndex) != buffer[bufferIndex]) {
                        skip = Math.max(1, patternIndex - right[buffer[bufferIndex]]);

                        if (startBufferIndex + skip >= buffer.length) {
                            startBufferIndex = skip - (buffer.length - startBufferIndex);
                        } else {
                            startBufferIndex += skip;
                        }

                        break;
                    }
                }

                if (skip == 0) {
                    return textIndex; // found
                }
            }

            // No need to clear the buffer since it is a local variable and will be collected by the garbage collector
            // once the method returns.
            return totalTextIndex;   // not found
        }

    }

    // File contents: abacadabrabracabracadabrabrabracad
    public static void main(String[] args) {
        Exercise29_BufferingInBoyerMoore bufferingInBoyerMoore = new Exercise29_BufferingInBoyerMoore();

        String filePath = Constants.FILES_PATH + Constants.STREAMING_FILE;

        String pattern1 = "abracadabra";
        In inputStream1 = new In(filePath);
        BoyerMooreBuffer boyerMooreBuffer1 = bufferingInBoyerMoore.new BoyerMooreBuffer(pattern1);
        int index1 = boyerMooreBuffer1.search(inputStream1);
        StdOut.println("Index 1: " + index1 + " Expected: 14");
        inputStream1.close();

        String pattern2 = "rab";
        In inputStream2 = new In(filePath);
        BoyerMooreBuffer boyerMooreBuffer2 = bufferingInBoyerMoore.new BoyerMooreBuffer(pattern2);
        int index2 = boyerMooreBuffer2.search(inputStream2);
        StdOut.println("Index 2: " + index2 + " Expected: 8");
        inputStream2.close();

        String pattern3 = "bcara";
        In inputStream3 = new In(filePath);
        BoyerMooreBuffer boyerMooreBuffer3 = bufferingInBoyerMoore.new BoyerMooreBuffer(pattern3);
        int index3 = boyerMooreBuffer3.search(inputStream3);
        StdOut.println("Index 3: " + index3 + " Expected: 34");
        inputStream3.close();

        String pattern4 = "rabrabracad";
        In inputStream4 = new In(filePath);
        BoyerMooreBuffer boyerMooreBuffer4 = bufferingInBoyerMoore.new BoyerMooreBuffer(pattern4);
        int index4 = boyerMooreBuffer4.search(inputStream4);
        StdOut.println("Index 4: " + index4 + " Expected: 23");
        inputStream4.close();

        String pattern5 = "abacad";
        In inputStream5 = new In(filePath);
        BoyerMooreBuffer boyerMooreBuffer5 = bufferingInBoyerMoore.new BoyerMooreBuffer(pattern5);
        int index5 = boyerMooreBuffer5.search(inputStream5);
        StdOut.println("Index 5: " + index5 + " Expected: 0");
        inputStream5.close();

        String pattern6 = "renerenerenerenerenerenerenerenerene";
        In inputStream6 = new In(filePath);
        BoyerMooreBuffer boyerMooreBuffer6 = bufferingInBoyerMoore.new BoyerMooreBuffer(pattern6);
        int index6 = boyerMooreBuffer6.search(inputStream6);
        StdOut.println("Index 6: " + index6 + " Expected: 34");
        inputStream5.close();
    }

}
