package chapter3.section5;

import chapter1.section3.Bag;
import chapter3.section3.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 06/08/17.
 */
public class Exercise14 {

    private static RedBlackBST<String, Bag<String>> invert(RedBlackBST<String, Bag<String>> symbolTable) {
        RedBlackBST<String, Bag<String>> inverseSymbolTable = new RedBlackBST<>();

        for(String key : symbolTable.keys()) {
            Bag<String> values = symbolTable.get(key);

            for(String newKey : values) {
                if (!inverseSymbolTable.contains(newKey)) {
                    inverseSymbolTable.put(newKey, new Bag<>());
                }
                inverseSymbolTable.get(newKey).add(key);
            }
        }

        return inverseSymbolTable;
    }

    public static void main(String[] args) {
        RedBlackBST<String, Bag<String>> redBlackBST = new RedBlackBST<>();
        Bag<String> colorsBag = new Bag<>();
        colorsBag.add("red");
        colorsBag.add("green");
        colorsBag.add("blue");
        redBlackBST.put("Colors", colorsBag);

        Bag<String> sortsBag = new Bag<>();
        sortsBag.add("mergesort");
        sortsBag.add("quicksort");
        sortsBag.add("heapsort");
        redBlackBST.put("Sorts", sortsBag);

        Bag<String> mixedBag = new Bag<>();
        mixedBag.add("mergesort");
        mixedBag.add("blue");
        mixedBag.add("algorithms");
        redBlackBST.put("Mixed Bag", mixedBag);

        RedBlackBST<String, Bag<String>> inverseSymbolTable = Exercise14.invert(redBlackBST);
        for(String key : inverseSymbolTable.keys()) {
            StdOut.println(key);

            for(String value : inverseSymbolTable.get(key)) {
                StdOut.println("  " + value);
            }
        }

        //Test
        StdOut.println("\nTests");
        StdOut.println("\nred key");
        for(String value : inverseSymbolTable.get("red")) {
            StdOut.println(value);
        }
        StdOut.println("Expected: \nColors");

        StdOut.println("\nblue key");
        for(String value : inverseSymbolTable.get("blue")) {
            StdOut.println(value);
        }
        StdOut.println("Expected: \nMixed Bag\nColors");

        StdOut.println("\nquicksort key");
        for(String value : inverseSymbolTable.get("quicksort")) {
            StdOut.println(value);
        }
        StdOut.println("Expected: \nSorts");

        StdOut.println("\nmergesort key");
        for(String value : inverseSymbolTable.get("mergesort")) {
            StdOut.println(value);
        }
        StdOut.println("Expected: \nSorts\nMixed Bag");
    }

}
