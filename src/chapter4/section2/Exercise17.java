package chapter4.section2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 22/10/17.
 */
//The digraph on page 591 is located at https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
public class Exercise17 {

    public static void main(String[] args) {
        String filePath = Constants.FILES_PATH + Constants.MEDIUM_DIGRAPH_FILE;

        Digraph digraph = new Digraph(new In(filePath));
        KosarajuSharirSCC kosarajuSharirSCC = new KosarajuSharirSCC(digraph);

        StdOut.println("Number of strong components: " + kosarajuSharirSCC.count());
    }

}
