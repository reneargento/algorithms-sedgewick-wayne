package chapter5.section5;

import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by Rene Argento on 13/04/18.
 */
public class Genome {

    public static void compress() {
        Alphabet DNA = Alphabet.DNA;
        String string = BinaryStdIn.readString();
        int length = string.length();

        BinaryStdOut.write(length);

        for (int i = 0; i < length; i++) {
            // Write two-bit code for char
            int code = DNA.toIndex(string.charAt(i));
            BinaryStdOut.write(code, DNA.lgR());
        }
        BinaryStdOut.close();
    }

    public static void expand() {
        Alphabet DNA = Alphabet.DNA;
        int width = DNA.lgR();
        int length = BinaryStdIn.readInt();

        for (int i = 0; i < length; i++) {
            // Read 2 bits; write char
            char character = BinaryStdIn.readChar(width);
            BinaryStdOut.write(DNA.toChar(character), 8);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            compress();
        } else if (args[0].equals("+")) {
            expand();
        }
    }

}
