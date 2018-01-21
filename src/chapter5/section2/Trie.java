package chapter5.section2;

import chapter1.section3.Queue;

/**
 * Created by Rene Argento on 20/01/18.
 */
@SuppressWarnings("unchecked")
public class Trie<Value> {

    private static final int R = 256; // radix
    private Node root = new Node();

    private static class Node {
        private Object value;
        private Node[] next = new Node[R];
    }

    public Value get(String key) {
        Node node = get(root, key, 0);

        if (node == null) {
            return null;
        }
        return (Value) node.value;
    }

    public Node get(Node node, String key, int digit) {
        if (node == null) {
            return null;
        }

        if(digit == key.length()) {
            return node;
        }

        char nextChar = key.charAt(digit); // Use digitTh key char to identify subtrie.
        return get(node.next[nextChar], key, digit + 1);
    }

    public void put(String key, Value value) {
        root = put(root, key, value, 0);
    }

    private Node put(Node node, String key, Value value, int digit) {
        if (node == null) {
            node = new Node();
        }

        if (digit == key.length()) {
            node.value = value;
            return node;
        }

        char nextChar = key.charAt(digit); // Use digitTh key char to identify subtrie.
        node.next[nextChar] = put(node.next[nextChar], key, value, digit + 1);

        return node;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> queue = new Queue<>();
        Node nodeWithPrefix = get(root, prefix, 0);
        collect(nodeWithPrefix, prefix, queue);

        return queue;
    }

    private void collect(Node node, String prefix, Queue<String> queue) {
        if (node == null) {
            return;
        }

        if (node.value != null) {
            queue.enqueue(prefix);
        }

        for (char nextChar = 0; nextChar < R; nextChar++) {
            collect(node.next[nextChar], prefix + nextChar, queue);
        }
    }

    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<>();
        collect(root, "", pattern, queue);
        return queue;
    }

    private void collect(Node node, String prefix, String pattern, Queue<String> queue) {
        if (node == null) {
            return;
        }

        int digit = prefix.length();
        if (digit == pattern.length() && node.value != null) {
            queue.enqueue(prefix);
        }

        if (digit == pattern.length()) {
            return;
        }

        char nextCharInPattern = pattern.charAt(digit);

        for (char nextChar = 0; nextChar < R; nextChar++) {
            if (nextCharInPattern == '.' || nextCharInPattern == nextChar) {
                collect(node.next[nextChar], prefix + nextChar, pattern, queue);
            }
        }
    }

    public String longestPrefixOf(String key) {
        int length = search(root, key, 0, 0);
        return key.substring(0, length);
    }

    private int search(Node node, String key, int digit, int length) {
        if (node == null) {
            return length;
        }

        if (node.value != null) {
            length = digit;
        }

        if (digit == key.length()) {
            return length;
        }

        char nextChar = key.charAt(digit);
        return search(node.next[nextChar], key, digit + 1, length);
    }

    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node node, String key, int digit) {
        if (node == null) {
            return null;
        }

        if (digit == key.length()) {
            node.value = null;
        } else {
            char nextChar = key.charAt(digit);
            node.next[nextChar] = delete(node.next[nextChar], key, digit + 1);
        }

        if (node.value != null) {
            return node;
        }

        for (char nextChar = 0; nextChar < R; nextChar++) {
            if (node.next[nextChar] != null) {
                return node;
            }
        }

        return null;
    }

}
