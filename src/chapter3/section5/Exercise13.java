package chapter3.section5;

import chapter3.section3.RedBlackBST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import util.Constants;

/**
 * Created by Rene Argento on 06/08/17.
 */
public class Exercise13 {

    // Parameters example: 0: csv_file.txt
    //                     1: 0
    //                     2: 1

    // Queries: arnold fzkey
    //          rachel wrong

    // Output expected:
    // arnold 200
    // dijkstra 10
    // dwayne 201
    // fenwick 202
    //
    // rene 5
    // sedgewick 9
    // wayne 10

    private void rangeLookupCSV(String[] args) {
        String filePath = Constants.FILES_PATH + args[0];

        In in = new In(filePath);
        int keyField = Integer.parseInt(args[1]);
        int valueField = Integer.parseInt(args[2]);

        RedBlackBST<String, String> symbolTable = new RedBlackBST<>();

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] tokens = line.split(",");
            String key = tokens[keyField];
            String value = tokens[valueField];

            symbolTable.put(key, value);
        }

        while (!StdIn.isEmpty()) {
            String queryKey1 = StdIn.readString();
            String queryKey2 = StdIn.readString();

            for(String key : symbolTable.keys(queryKey1, queryKey2)) {
                StdOut.println(key + " " + symbolTable.get(key));
            }
            StdOut.println();
        }
    }

    public static void main(String[] args) {
        new Exercise13().rangeLookupCSV(args);
    }

}
