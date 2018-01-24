package chapter5.section2;

import chapter1.section3.Queue;

/**
 * Created by Rene Argento on 20/01/18.
 */
@SuppressWarnings("unchecked")
public class Trie<Value> {

    protected static final int R = 256; // radix
    private Node root = new Node();

    protected static class Node {
        protected Object value;
        private Node[] next = new Node[R];
        private int size;
    }

    public int size() {
        return size(root);
    }

    private int size(Node nodeWithSize) {
        if (nodeWithSize == null) {
            return 0;
        }

        return nodeWithSize.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return get(key) != null;
    }

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Key must have a positive length");
        }

        Node node = get(root, key, 0);

        if (node == null) {
            return null;
        }
        return (Value) node.value;
    }

    private Node get(Node node, String key, int digit) {
        if (node == null) {
            return null;
        }

        if (digit == key.length()) {
            return node;
        }

        char nextChar = key.charAt(digit); // Use digitTh key char to identify subtrie.
        return get(node.next[nextChar], key, digit + 1);
    }

    public void put(String key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        boolean isNewKey = false;

        if (!contains(key)) {
            isNewKey = true;
        }

        if (value == null) {
            delete(key);
        } else {
            root = put(root, key, value, 0, isNewKey);
        }
    }

    private Node put(Node node, String key, Value value, int digit, boolean isNewKey) {
        if (node == null) {
            node = new Node();
        }

        if (isNewKey) {
            node.size = node.size + 1;
        }

        if (digit == key.length()) {
            node.value = value;
            return node;
        }

        char nextChar = key.charAt(digit); // Use digitTh key char to identify subtrie.
        node.next[nextChar] = put(node.next[nextChar], key, value, digit + 1, isNewKey);

        return node;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }

        Queue<String> queue = new Queue<>();
        Node nodeWithPrefix = get(root, prefix, 0);
        collect(nodeWithPrefix, new StringBuilder(prefix), queue);

        return queue;
    }

    private void collect(Node node, StringBuilder prefix, Queue<String> queue) {
        if (node == null) {
            return;
        }

        if (node.value != null) {
            queue.enqueue(prefix.toString());
        }

        for (char nextChar = 0; nextChar < R; nextChar++) {
            prefix.append(nextChar);
            collect(node.next[nextChar], prefix, queue);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public Iterable<String> keysThatMatch(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }

        Queue<String> queue = new Queue<>();
        collect(root, new StringBuilder(), pattern, queue);
        return queue;
    }

    private void collect(Node node, StringBuilder prefix, String pattern, Queue<String> queue) {
        if (node == null) {
            return;
        }

        int digit = prefix.length();
        if (digit == pattern.length() && node.value != null) {
            queue.enqueue(prefix.toString());
        }

        if (digit == pattern.length()) {
            return;
        }

        char nextCharInPattern = pattern.charAt(digit);

        for (char nextChar = 0; nextChar < R; nextChar++) {
            if (nextCharInPattern == '.' || nextCharInPattern == nextChar) {
                prefix.append(nextChar);
                collect(node.next[nextChar], prefix, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
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
            length = digit;
        }

        if (digit == query.length()) {
            return length;
        }

        char nextChar = query.charAt(digit);
        return search(node.next[nextChar], query, digit + 1, length);
    }

    public void delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (!contains(key)) {
            return;
        }

        root = delete(root, key, 0);
    }

    private Node delete(Node node, String key, int digit) {
        if (node == null) {
            return null;
        }

        node.size = node.size - 1;

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
