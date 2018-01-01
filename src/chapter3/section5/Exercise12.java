package chapter3.section5;

import chapter3.section3.RedBlackBST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 06/08/17.
 */
public class Exercise12 {

    //Arguments: 0: /Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/csv_file.txt
    //           1: 0
    //           2: 1

    //Queries: rene
    //         sedgewick
    //         wayne

    //Output expected:
    // rene
    // 1 3 5
    // sedgewick
    // 9
    // wayne
    // 9 10
    private void lookupCSV(String[] args) {
        In in = new In(args[0]);
        int keyField = Integer.parseInt(args[1]);
        int valueField = Integer.parseInt(args[2]);

        RedBlackBST<String, List<String>> symbolTable = new RedBlackBST<>();

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] tokens = line.split(",");
            String key = tokens[keyField];
            String value = tokens[valueField];

            if(!symbolTable.contains(key)) {
                symbolTable.put(key, new ArrayList<>());
            }
            symbolTable.get(key).add(value);
        }

        while (!StdIn.isEmpty()) {
            String query = StdIn.readString();

            boolean isFirstValue = true;

            if(symbolTable.contains(query)) {
                for(String value : symbolTable.get(query)) {
                    if(isFirstValue) {
                        isFirstValue = false;
                    } else {
                        StdOut.print(" ");
                    }

                    StdOut.print(value);
                }
                StdOut.println();
            }
        }
    }

    public static void main(String[] args) {
        new Exercise12().lookupCSV(args);
    }

}
