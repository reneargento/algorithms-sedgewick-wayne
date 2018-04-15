package chapter5.section5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by Rene Argento on 13/04/18.
 */
public class RunLengthEncoding {

    private static final int LG_R = 8;

    public static void compress() {
        char count = 0;
        boolean old = false;

        while (!BinaryStdIn.isEmpty()) {
            boolean bit = BinaryStdIn.readBoolean();

            if (bit != old) {
                BinaryStdOut.write(count, LG_R);
                count = 0;
                old = !old;
            } else {
                if (count == 255) {
                    BinaryStdOut.write(count, LG_R);
                    count = 0;
                    BinaryStdOut.write(count, LG_R);
                }
            }
            count++;
        }

        BinaryStdOut.write(count);
        BinaryStdOut.close();
    }

    public static void expand() {
        boolean bit = false;

        while (!BinaryStdIn.isEmpty()) {
            char count = BinaryStdIn.readChar();

            for (int i = 0; i < count; i++) {
                BinaryStdOut.write(bit);
            }
            bit = !bit;
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
