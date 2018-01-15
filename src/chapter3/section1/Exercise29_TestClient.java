package chapter3.section1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 27/04/17.
 */
//Based on http://algs4.cs.princeton.edu/31elementary/TestBinarySearchST.java.html
public class Exercise29_TestClient {

    public static void main(String[] args) {
        BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

         // Parameter example: S E A R C H E X A M P L E
        for(int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            binarySearchSymbolTable.put(key, i);
        }

        String[] keys = new String[binarySearchSymbolTable.size()];
        int keyArrayIndex = 0;
        for(String key : binarySearchSymbolTable.keys()) {
            keys[keyArrayIndex++] = key;
        }

        StdOut.println("size = " + binarySearchSymbolTable.size());
        StdOut.println("min  = " + binarySearchSymbolTable.min());
        StdOut.println("max  = " + binarySearchSymbolTable.max());
        StdOut.println();


        // Print keys in order using keys()
        StdOut.println("Testing keys()");
        StdOut.println("--------------------------------");
        for (String key : binarySearchSymbolTable.keys()) {
            StdOut.println(key + " " + binarySearchSymbolTable.get(key));
        }
        StdOut.println();


        // Print keys in order using select
        StdOut.println("Testing select");
        StdOut.println("--------------------------------");
        for (int i = 0; i < binarySearchSymbolTable.size(); i++) {
            StdOut.println(i + " " + binarySearchSymbolTable.select(i));
        }
        StdOut.println();

        // Test rank, floor, ceiling
        StdOut.println("key rank floor ceil");
        StdOut.println("-------------------");
        for (char i = 'A'; i <= 'Z'; i++) {
            String key = String.valueOf(i);
            StdOut.printf("%2s %4d %4s %4s\n", key, binarySearchSymbolTable.rank(key),
                    binarySearchSymbolTable.floor(key), binarySearchSymbolTable.ceiling(key));
        }
        StdOut.println();

        // Test range search and range count
        String[] from = { "A", "Z", "X", "0", "B", "C" };
        String[] to   = { "Z", "A", "X", "Z", "G", "L" };
        StdOut.println("range search");
        StdOut.println("-------------------");
        for (int i = 0; i < from.length; i++) {
            StdOut.printf("%s-%s (%2d) : ", from[i], to[i], binarySearchSymbolTable.size(from[i], to[i]));
            for (String key : binarySearchSymbolTable.keys(from[i], to[i])) {
                StdOut.print(key + " ");
            }
            StdOut.println();
        }
        StdOut.println();

        // Delete the smallest keys
        for (int i = 0; i < binarySearchSymbolTable.size() / 2; i++) {
            binarySearchSymbolTable.deleteMin();
        }
        StdOut.println("After deleting the smallest " + binarySearchSymbolTable.size() / 2 + " keys");
        StdOut.println("--------------------------------");
        for (String key : binarySearchSymbolTable.keys()) {
            StdOut.println(key + " " + binarySearchSymbolTable.get(key));
        }
        StdOut.println();

        // Delete the max key
        binarySearchSymbolTable.deleteMax();
        StdOut.println("After deleting the max key");
        StdOut.println("--------------------------------");
        for (String key : binarySearchSymbolTable.keys()) {
            StdOut.println(key + " " + binarySearchSymbolTable.get(key));
        }
        StdOut.println();

        // Delete all the remaining keys
        while (!binarySearchSymbolTable.isEmpty()) {
            binarySearchSymbolTable.delete(binarySearchSymbolTable.select(binarySearchSymbolTable.size() / 2));
        }
        StdOut.println("After deleting the remaining keys");
        StdOut.println("Size: " + binarySearchSymbolTable.size());
        StdOut.println();

        StdOut.println("After adding back N keys");
        StdOut.println("--------------------------------");
        for (int i = 0; i < keys.length; i++) {
            binarySearchSymbolTable.put(keys[i], i);
        }

        for (String key : binarySearchSymbolTable.keys()) {
            StdOut.println(key + " " + binarySearchSymbolTable.get(key));
        }
        StdOut.println();
    }

}
