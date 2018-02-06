package chapter5.section2;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 21/01/18.
 */
public class Exercise9_ExtendedOperationsForTSTs {

    public class TernarySearchTrieExtended<Value> extends TernarySearchTrie<Value> {

        public Iterable<String> keys() {
            Queue<String> keys = new Queue<>();
            collect(root, new StringBuilder(), keys);
            return keys;
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("Prefix cannot be null");
            }

            Queue<String> keysWithPrefix = new Queue<>();

            Node nodeWithPrefix = get(root, prefix, 0);

            if (nodeWithPrefix == null) {
                return keysWithPrefix;
            }

            if (nodeWithPrefix.value != null) {
                keysWithPrefix.enqueue(prefix);
            }

            collect(nodeWithPrefix.middle, new StringBuilder(prefix), keysWithPrefix);
            return keysWithPrefix;
        }

        private void collect(Node node, StringBuilder prefix, Queue<String> queue) {
            if (node == null) {
                return;
            }

            collect(node.left, prefix, queue);

            if (node.value != null) {
                queue.enqueue(prefix.toString() + node.character);
            }

            collect(node.middle, prefix.append(node.character), queue);
            prefix.deleteCharAt(prefix.length() - 1);
            collect(node.right, prefix, queue);
        }

        public Iterable<String> keysThatMatch(String pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern cannot be null");
            }

            Queue<String> keysThatMatch = new Queue<>();
            collect(root, new StringBuilder(), pattern, keysThatMatch);
            return keysThatMatch;
        }

        private void collect(Node node, StringBuilder prefix, String pattern, Queue<String> queue) {
            if (node == null) {
                return;
            }

            int digit = prefix.length();
            char nextCharInPattern = pattern.charAt(digit);

            if (nextCharInPattern == '.' || nextCharInPattern < node.character) {
                collect(node.left, prefix, pattern, queue);
            }
            if (nextCharInPattern == '.' || nextCharInPattern == node.character) {
                if (digit == pattern.length() - 1 && node.value != null) {
                    queue.enqueue(prefix.toString() + node.character);
                } else if (digit < pattern.length() - 1) {
                    collect(node.middle, prefix.append(node.character), pattern, queue);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
            if (nextCharInPattern == '.' || nextCharInPattern > node.character) {
                collect(node.right, prefix, pattern, queue);
            }
        }

        public String longestPrefixOf(String query) {
            if (query == null) {
                throw new IllegalArgumentException("Query cannot be null");
            }

            int length = search(root, query, 0, 0);
            return query.substring(0, length);
        }

        private int search(Node node, String query, int digit, int length) {
            if (node == null) {
                return length;
            }

            if (node.value != null) {
                length = digit + 1;
            }

            char nextChar = query.charAt(digit);

            if (nextChar < node.character) {
                return search(node.left, query, digit, length);
            } else if (nextChar > node.character) {
                return search(node.right, query, digit, length);
            } else if (digit < query.length() - 1) {
                return search(node.middle, query, digit + 1, length);
            } else {
                return length;
            }
        }

    }

    public static void main(String[] args) {
        TernarySearchTrieExtended<Integer> tstExtended = new Exercise9_ExtendedOperationsForTSTs().
                new TernarySearchTrieExtended<>();

        tstExtended.put("Rene", 0);
        tstExtended.put("Re", 1);
        tstExtended.put("Algorithms", 2);
        tstExtended.put("Algo", 3);
        tstExtended.put("Algor", 4);
        tstExtended.put("Tree", 5);
        tstExtended.put("Trie", 6);
        tstExtended.put("TST", 7);

        // keys() test

        StringJoiner initialKeys = new StringJoiner(" ");
        for(String key : tstExtended.keys()) {
            initialKeys.add(key);
        }
        StdOut.println("Initial keys: " + initialKeys.toString());
        StdOut.println("Expected:     Algo Algor Algorithms Re Rene TST Tree Trie");

        // keysWithPrefix() test

        StringJoiner keysWithPrefix1 = new StringJoiner(" ");
        for(String key : tstExtended.keysWithPrefix("A")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println("\nKeys with prefix A: " + keysWithPrefix1.toString());
        StdOut.println("Expected:           Algo Algor Algorithms");

        StringJoiner keysWithPrefix2 = new StringJoiner(" ");
        for(String key : tstExtended.keysWithPrefix("R")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println("Keys with prefix R: " + keysWithPrefix2.toString());
        StdOut.println("Expected:           Re Rene");

        StringJoiner keysWithPrefix3 = new StringJoiner(" ");
        for(String key : tstExtended.keysWithPrefix("Z")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println("Keys with prefix Z: " + keysWithPrefix3.toString());
        StdOut.println("Expected: ");

        // keysThatMatch() test

        StringJoiner keysThatMatch1 = new StringJoiner(" ");
        for(String key : tstExtended.keysThatMatch("Tr.e")) {
            keysThatMatch1.add(key);
        }
        StdOut.println("\nKeys that match pattern Tr.e: " + keysThatMatch1.toString());
        StdOut.println("Expected:                     Tree Trie");

        StringJoiner keysThatMatch2 = new StringJoiner(" ");
        for(String key : tstExtended.keysThatMatch("R.")) {
            keysThatMatch2.add(key);
        }
        StdOut.println("Keys that match pattern R.: " + keysThatMatch2.toString());
        StdOut.println("Expected:                   Re");

        // longestPrefixOf() test

        String longestPrefixOf1 = tstExtended.longestPrefixOf("Ren");
        StdOut.println("\nLongest prefix of Ren: " + longestPrefixOf1);
        StdOut.println("Expected: Re");

        String longestPrefixOf2 = tstExtended.longestPrefixOf("Algor");
        StdOut.println("Longest prefix of Algor: " + longestPrefixOf2);
        StdOut.println("Expected: Algor");

        String longestPrefixOf3 = tstExtended.longestPrefixOf("Quicksort");
        StdOut.println("Longest prefix of Quicksort: " + longestPrefixOf3);
        StdOut.println("Expected: ");
    }

}
