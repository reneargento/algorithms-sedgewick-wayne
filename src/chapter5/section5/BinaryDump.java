package chapter5.section5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 13/04/18.
 */
public class BinaryDump {

    BinaryDump(String[] args) {
        main(args);
    }

    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        int count;

        for (count = 0; !BinaryStdIn.isEmpty(); count++) {
            if (width == 0) {
                BinaryStdIn.readBoolean();
                continue;
            }

            if (count != 0 && count % width == 0) {
                StdOut.println();
            }

            if (BinaryStdIn.readBoolean()) {
                StdOut.print("1");
            } else {
                StdOut.print("0");
            }
        }

        StdOut.println();
        StdOut.println(count + " bits");
    }

}
