package chapter5.section2;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 17/02/18.
 */
public class Exercise21_SubstringMatches {

    public class TernarySearchTrieWithValueList<Value> {

        private Node root;

        private class Node {
            char character;
            List<Value> values = new ArrayList<>();
            int size;

            Node left;
            Node middle;
            Node right;
        }

        public void put(String key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            // No need for delete() in this exercise
            if (value == null) {
                return;
            }

            root = put(root, key, value, 0);
        }

        private Node put(Node node, String key, Value value, int digit) {
            char currentChar = key.charAt(digit);

            if (node == null) {
                node = new Node();
                node.character = currentChar;
            }

            if (currentChar < node.character) {
                node.left = put(node.left, key, value, digit);
            } else if (currentChar > node.character) {
                node.right = put(node.right, key, value, digit);
            } else if (digit < key.length() - 1) {
                node.middle = put(node.middle, key, value, digit + 1);
                node.size = node.size + 1;
            } else {
                node.values.add(value);
                node.size = node.size + 1;
            }

            return node;
        }

        public Iterable<Value> keysWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("Prefix cannot be null");
            }

            Queue<Value> keysWithPrefix = new Queue<>();

            Node nodeWithPrefix = get(root, prefix, 0);

            if (nodeWithPrefix == null) {
                return keysWithPrefix;
            }

            if (nodeWithPrefix.values != null) {
                for(Value value : nodeWithPrefix.values) {
                    keysWithPrefix.enqueue(value);
                }
            }

            collect(nodeWithPrefix.middle, new StringBuilder(prefix), keysWithPrefix);
            return keysWithPrefix;
        }

        private void collect(Node node, StringBuilder prefix, Queue<Value> queue) {
            if (node == null) {
                return;
            }

            collect(node.left, prefix, queue);

            if (node.values != null) {
                for(Value value : node.values) {
                    queue.enqueue(value);
                }
            }

            collect(node.middle, prefix.append(node.character), queue);
            prefix.deleteCharAt(prefix.length() - 1);
            collect(node.right, prefix, queue);
        }

        private Node get(Node node, String key, int digit) {
            if (node == null) {
                return null;
            }

            char currentChar = key.charAt(digit);

            if (currentChar < node.character) {
                return get(node.left, key, digit);
            } else if (currentChar > node.character) {
                return get(node.right, key, digit);
            } else if (digit < key.length() - 1) {
                return get(node.middle, key, digit + 1);
            } else {
                return node;
            }
        }
    }

    public interface SubstringMatchAPI {
        void putAllSuffixes(String key);
        Iterable<String> keysWithSubstring(String substring);
    }

    public class SubstringMatch implements SubstringMatchAPI {

        private TernarySearchTrieWithValueList<String> ternarySearchTrie;

        SubstringMatch(List<String> strings) {
            ternarySearchTrie = new TernarySearchTrieWithValueList<>();

            for (String string : strings) {
                putAllSuffixes(string);
            }
        }

        @Override
        public void putAllSuffixes(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            for (int i = 0; i < key.length(); i++) {
                String suffix = key.substring(i, key.length());
                ternarySearchTrie.put(suffix, key);
            }
        }

        @Override
        public Iterable<String> keysWithSubstring(String substring) {

            HashSet<String> uniqueKeysWithSubstring = new HashSet<>();

            for (String key : ternarySearchTrie.keysWithPrefix(substring)) {
                uniqueKeysWithSubstring.add(key);
            }

            List<String> orderedKeys = new ArrayList<>();
            orderedKeys.addAll(uniqueKeysWithSubstring);

            Collections.sort(orderedKeys);

            return orderedKeys;
        }
    }

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();

        strings.add("Rene");
        strings.add("Reri");
        strings.add("Algorithms");
        strings.add("Algo");
        strings.add("Algor");
        strings.add("Tree");
        strings.add("Trie");
        strings.add("TST");
        strings.add("Trie123");
        strings.add("Z-Function");

        SubstringMatch substringMatch = new Exercise21_SubstringMatches().new SubstringMatch(strings);

        StdOut.println("Keys with substring Al");
        StringJoiner keysWithSubstring1 = new StringJoiner(" ");

        for(String key : substringMatch.keysWithSubstring("Al")) {
            keysWithSubstring1.add(key);
        }
        StdOut.println(keysWithSubstring1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with substring gor");
        StringJoiner keysWithSubstring2 = new StringJoiner(" ");

        for(String key : substringMatch.keysWithSubstring("gor")) {
            keysWithSubstring2.add(key);
        }
        StdOut.println(keysWithSubstring2.toString());
        StdOut.println("Expected: Algor Algorithms");

        StdOut.println("\nKeys with substring ri");
        StringJoiner keysWithSubstring3 = new StringJoiner(" ");

        for(String key : substringMatch.keysWithSubstring("ri")) {
            keysWithSubstring3.add(key);
        }
        StdOut.println(keysWithSubstring3.toString());
        StdOut.println("Expected: Algorithms Reri Trie Trie123");

        StdOut.println("\nKeys with substring e");
        StringJoiner keysWithSubstring4 = new StringJoiner(" ");

        for(String key : substringMatch.keysWithSubstring("e")) {
            keysWithSubstring4.add(key);
        }
        StdOut.println(keysWithSubstring4.toString());
        StdOut.println("Expected: Rene Reri Tree Trie Trie123");
    }

}
