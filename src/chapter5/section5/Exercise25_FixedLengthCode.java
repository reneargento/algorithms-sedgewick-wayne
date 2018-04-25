package chapter5.section5;

import chapter3.section5.HashSet;
import chapter5.section1.Exercise12_Alphabet;
import edu.princeton.cs.algs4.*;

/**
 * Created by Rene Argento on 23/04/18.
 */
// Test results

// % javac -cp algs4.jar:. Exercise25_FixedLengthCode.java

// % more 5.5.25_text.txt
// abcd abbbc ccdda cbca cabcc abcb cbababd dbabcbad dbabcb
// abb acadda abcbca

// Original length: 74 characters * 8 bits = 592 bits

// % java -cp algs4.jar:. Exercise25_FixedLengthCode - < 5.5.25_text.txt | java -cp algs4.jar:. BinaryDump 0
// 320 bits

// Compression ratio: 320 / 592 = 54%

// % java -cp algs4.jar:. Exercise25_FixedLengthCode - < 5.5.25_text.txt | java -cp algs4.jar:. Exercise25_FixedLengthCode +
// abcd abbbc ccdda cbca cabcc abcb cbababd dbabcbad dbabcb
// abb acadda abcbca
public class Exercise25_FixedLengthCode {

    public static class RLE {

        private final static char EOF = '$'; // Using the $ character as EOF, but any other character could have been chosen

        public static void compress() {
            StringBuilder alpha = new StringBuilder();
            String string = BinaryStdIn.readString();

            HashSet<Character> distinctCharacters = new HashSet<>();

            // Get all unique characters from input
            for (int i = 0; i < string.length(); i++) {
                distinctCharacters.add(string.charAt(i));
            }

            for (Character character : distinctCharacters.keys()) {
                alpha.append(character);
            }

            alpha.append(EOF);

            String alphaString = alpha.toString();
            Exercise12_Alphabet.Alphabet alphabet = new Exercise12_Alphabet().new Alphabet(alphaString);

            // Print number of alpha characters
            BinaryStdOut.write(alphaString.length());

            // Print alpha characters
            for (int i = 0; i < alphaString.length(); i++) {
                BinaryStdOut.write(alphaString.charAt(i), 8);
            }

            // Use alphabet to encode input
            for (int i = 0; i < string.length(); i++) {
                int code = alphabet.toIndex(string.charAt(i));
                BinaryStdOut.write(code, alphabet.lgR());
            }

            // Write EOF
            int eofCode = alphabet.toIndex(EOF);
            BinaryStdOut.write(eofCode, alphabet.lgR());
            BinaryStdOut.close();
        }

        public static void expand() {
            int alphabetLength = BinaryStdIn.readInt();
            StringBuilder alpha = new StringBuilder();

            for (int i = 0; i < alphabetLength; i++) {
                char character = BinaryStdIn.readChar();
                alpha.append(character);
            }

            Exercise12_Alphabet.Alphabet alphabet = new Exercise12_Alphabet().new Alphabet(alpha.toString());
            int eofCode = alphabet.toIndex(EOF);

            while (true) {
                int code = BinaryStdIn.readInt(alphabet.lgR());

                if (code == eofCode) {
                    break;
                }

                char character = alphabet.toChar(code);
                BinaryStdOut.write(character, 8);
            }

            BinaryStdOut.close();
        }
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            RLE.compress();
        } else if (args[0].equals("+")) {
            RLE.expand();
        }
    }

}
