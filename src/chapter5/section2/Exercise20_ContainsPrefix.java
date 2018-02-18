package chapter5.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/02/18.
 */
public class Exercise20_ContainsPrefix {

    public class StringSetContainsPrefix extends StringSet {

        public boolean containsPrefix(String prefix) {
            return containsPrefix(root, prefix, 0);
        }

        private boolean containsPrefix(Node node, String prefix, int digit) {
            if (node == null) {
                return false;
            }

            if (digit == prefix.length()) {
                return true;
            }

            char nextChar = prefix.charAt(digit);
            return containsPrefix(node.next.get(nextChar), prefix, digit + 1);
        }
    }

    public static void main(String[] args) {
        StringSetContainsPrefix stringSetContainsPrefix =
                new Exercise20_ContainsPrefix().new StringSetContainsPrefix();

        stringSetContainsPrefix.add("Rene");
        stringSetContainsPrefix.add("Re");
        stringSetContainsPrefix.add("Algorithms");
        stringSetContainsPrefix.add("Algo");
        stringSetContainsPrefix.add("Algor");
        stringSetContainsPrefix.add("Tree");
        stringSetContainsPrefix.add("Trie");
        stringSetContainsPrefix.add("TST");
        stringSetContainsPrefix.add("Trie123");
        stringSetContainsPrefix.add("Z-Function");

        StdOut.println("Contains prefix Alg: " + stringSetContainsPrefix.containsPrefix("Alg"));
        StdOut.println("Expected: true");

        StdOut.println("\nContains prefix Trie123: " + stringSetContainsPrefix.containsPrefix("Trie123"));
        StdOut.println("Expected: true");

        StdOut.println("\nContains prefix T: " + stringSetContainsPrefix.containsPrefix("T"));
        StdOut.println("Expected: true");

        StdOut.println("\nContains prefix R: " + stringSetContainsPrefix.containsPrefix("R"));
        StdOut.println("Expected: true");

        StdOut.println("\nContains prefix Algar: " + stringSetContainsPrefix.containsPrefix("Algar"));
        StdOut.println("Expected: false");

        StdOut.println("\nContains prefix ZZZ: " + stringSetContainsPrefix.containsPrefix("ZZZ"));
        StdOut.println("Expected: false");
    }

}
