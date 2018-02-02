package chapter5.section2;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 31/01/18.
 */
public class Exercise6 {

    public interface StringSETAPI {
        void add(String key);
        void delete(String key);
        boolean contains(String key);
        boolean isEmpty();
        int size();
        String toString();
    }

    public class StringSET implements StringSETAPI {

        private class Node {
            private SeparateChainingHashTable<Character, Node> next = new SeparateChainingHashTable<>();
            boolean isKey;
        }

        private Node root = new Node();
        private int size;

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean contains(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            return contains(root, key, 0);
        }

        private boolean contains(Node node, String key, int digit) {
            if (node == null) {
                return false;
            }

            if (digit == key.length()) {
                return node.isKey;
            }

            char nextChar = key.charAt(digit);

            if (node.next.contains(nextChar)) {
                return contains(node.next.get(nextChar), key, digit + 1);
            } else {
                return false;
            }
        }

        @Override
        public void add(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (contains(key)) {
                return;
            }

            root = add(root, key, 0);
            size++;
        }

        private Node add(Node node, String key, int digit) {
            if (node == null) {
                node = new Node();
            }

            if (digit == key.length()) {
                node.isKey = true;
                return node;
            }

            char nextChar = key.charAt(digit);

            Node nextNode = add(node.next.get(nextChar), key, digit + 1);
            node.next.put(nextChar, nextNode);
            return node;
        }

        @Override
        public void delete(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            root = delete(root, key, 0);
            size--;
        }

        private Node delete(Node node, String key, int digit) {

            if (digit == key.length()) {
                node.isKey = false;
            } else {
                char nextChar = key.charAt(digit);
                Node childNode = delete(node.next.get(nextChar), key, digit + 1);
                node.next.put(nextChar, childNode);
            }

            if (node.isKey || node.next.size() > 0) {
                return node;
            }

            return null;
        }

        // O(n lg n) due to sort - the hash map in the nodes saves memory, but does not necessarily store subsets in order.
        // So a sort is required to return the keys in order.
        public Iterable<String> keys() {
            List<String> keys = new ArrayList<>();
            keys(root, new StringBuilder(), keys);

            Collections.sort(keys);

            return keys;
        }

        private void keys(Node node, StringBuilder prefix, List<String> keys) {
            if (node == null) {
                return;
            }

            if (node.isKey) {
                keys.add(prefix.toString());
            }

            for (Character character : node.next.keys()) {
                keys(node.next.get(character), new StringBuilder(prefix).append(character), keys);
            }
        }

        @Override
        public String toString() {
            StringJoiner keys = new StringJoiner(", ");

            for(String key : keys()) {
                keys.add(key);
            }

            return "{ " + keys.toString() + " }";
        }

    }

    public static void main(String[] args) {
        StringSET stringSET = new Exercise6().new StringSET();

        StdOut.println("Is string set empty: " + stringSET.isEmpty());
        StdOut.println("Expected: true");

        StdOut.println("\nSize: " + stringSET.size());
        StdOut.println("Expected: 0");

        StdOut.println("\nToString:\n" + stringSET);

        stringSET.add("Rene");
        stringSET.add("Re");
        stringSET.add("Algorithms");
        stringSET.add("Algo");
        stringSET.add("Algor");
        stringSET.add("Tree");
        stringSET.add("Trie");
        stringSET.add("TST");
        stringSET.add("Trie123");

        StdOut.println("\nIs string set empty: " + stringSET.isEmpty());
        StdOut.println("Expected: false");

        StdOut.println("\nSize: " + stringSET.size());
        StdOut.println("Expected: 9");

        StdOut.println("\nToString:\n" + stringSET);

        // Adding a key that already exists
        stringSET.add("Algorithms");

        StdOut.println("\nSize after adding key that already exists: " + stringSET.size());
        StdOut.println("Expected: 9");

        StdOut.println("\nContains key Sedgewick: " + stringSET.contains("Sedgewick"));
        StdOut.println("Expected: false");

        StdOut.println("\nContains key Rene: " + stringSET.contains("Rene"));
        StdOut.println("Expected: true");

        StdOut.println("\nContains key Z-Function: " + stringSET.contains("Z-Function"));
        StdOut.println("Expected: false");

        StdOut.println("\nContains key Algorithms: " + stringSET.contains("Algorithms"));
        StdOut.println("Expected: true");

        stringSET.delete("Algorithms");

        StdOut.println("\nContains key Algorithms (after delete): " + stringSET.contains("Algorithms"));
        StdOut.println("Expected: false");

        stringSET.delete("Re");
        StdOut.println("\nSize after deletes: " + stringSET.size());
        StdOut.println("Expected: 7");

        StdOut.println("\nToString:\n" + stringSET);
    }

}