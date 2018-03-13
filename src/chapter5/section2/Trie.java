package chapter5.section2;

import chapter1.section3.Queue;

/**
 * Created by Rene Argento on 20/01/18.
 */
@SuppressWarnings("unchecked")
public class Trie<Value> implements TrieInterface<Value> {

    protected static final int R = 256; // radix
    protected Node root = new Node();

    protected static class Node {
        protected Object value;
        protected Node[] next = new Node[R];
        protected int size;
    }

    public int size() {
        return size(root);
    }

    protected int size(Node node) {
        if (node == null) {
            return 0;
        }

        return node.size;
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

        if (value == null) {
            delete(key);
            return;
        }

        boolean isNewKey = false;

        if (!contains(key)) {
            isNewKey = true;
        }

        root = put(root, key, value, 0, isNewKey);
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

        Queue<String> keysWithPrefix = new Queue<>();
        Node nodeWithPrefix = get(root, prefix, 0);
        collect(nodeWithPrefix, new StringBuilder(prefix), keysWithPrefix);

        return keysWithPrefix;
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

        Queue<String> keysThatMatch = new Queue<>();
        collect(root, new StringBuilder(), pattern, keysThatMatch);
        return keysThatMatch;
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

    // Ordered methods

    // Returns the highest key in the symbol table smaller than or equal to key.
    public String floor(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return floor(root, key, 0, new StringBuilder(), null, true);
    }

    private String floor(Node node, String key, int digit, StringBuilder prefix, String lastKeyFound,
                         boolean mustBeEqualDigit) {
        if (node == null) {
            return null;
        }

        if (prefix.toString().compareTo(key) > 0) {
            return lastKeyFound;
        }

        if (node.value != null) {
            lastKeyFound = prefix.toString();
        }

        char currentChar;

        if (mustBeEqualDigit && digit < key.length()) {
            currentChar = key.charAt(digit);
        } else {
            currentChar = R - 1;
        }

        for (char nextChar = currentChar; true; nextChar--) {
            if (node.next[nextChar] != null) {
                if (nextChar < currentChar) {
                    mustBeEqualDigit = false;
                }

                lastKeyFound = floor(node.next[nextChar], key, digit + 1, prefix.append(nextChar), lastKeyFound, mustBeEqualDigit);

                if (lastKeyFound != null) {
                    return lastKeyFound;
                }
                prefix.deleteCharAt(prefix.length() - 1);
            }

            // nextChar value never becomes less than zero in the for loop, so we need this extra validation
            if (nextChar == 0) {
                break;
            }
        }

        return lastKeyFound;
    }

    // Returns the smallest key in the symbol table greater than or equal to key.
    public String ceiling(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return ceiling(root, key, 0, new StringBuilder(), true);
    }

    private String ceiling(Node node, String key, int digit, StringBuilder prefix, boolean mustBeEqualDigit) {
        if (node == null) {
            return null;
        }

        if (node.value != null && prefix.toString().compareTo(key) >= 0) {
            return prefix.toString();
        }

        char currentChar;

        if (mustBeEqualDigit && digit < key.length()) {
            currentChar = key.charAt(digit);
        } else {
            currentChar = 0;
        }

        for (char nextChar = currentChar; nextChar < R; nextChar++) {
            if (node.next[nextChar] != null) {
                if (nextChar > currentChar) {
                    mustBeEqualDigit = false;
                }

                String keyFound = ceiling(node.next[nextChar], key, digit + 1, prefix.append(nextChar),
                        mustBeEqualDigit);

                if (keyFound != null) {
                    return keyFound;
                }
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        return null;
    }

    public String select(int index) {
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index cannot be negative and must be lower than trie size");
        }

        return select(root, index, new StringBuilder());
    }

    private String select(Node node, int index, StringBuilder prefix) {
        if (node == null) {
            return null;
        }

        if (node.value != null) {
            index--;

            // Found the key with the target index
            if (index == -1) {
                return prefix.toString();
            }
        }

        for (char nextChar = 0; nextChar < R; nextChar++) {
            if (node.next[nextChar] != null) {
                if (index - size(node.next[nextChar]) < 0) {
                    return select(node.next[nextChar], index, prefix.append(nextChar));
                } else {
                    index = index - size(node.next[nextChar]);
                }
            }
        }

        return null;
    }

    public int rank(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return rank(root, key, 0, 0);
    }

    private int rank(Node node, String key, int digit, int size) {
        if (node == null || digit == key.length()) {
            return size;
        }

        // If a prefix key was found, add 1 to rank
        if (node.value != null) {
            if (digit < key.length()) {
                size++;
            } else {
                return size;
            }
        }

        char currentChar = key.charAt(digit);

        for (char nextChar = 0; nextChar < currentChar; nextChar++) {
            size += size(node.next[nextChar]);
        }

        return rank(node.next[currentChar], key, digit + 1, size);
    }

    public String min() {
        if (isEmpty()) {
            return null;
        }

        return min(root, new StringBuilder());
    }

    private String min(Node node, StringBuilder prefix) {

        if (node.value != null) {
            return prefix.toString();
        }

        for (char nextChar = 0; nextChar < R; nextChar++) {
            if (node.next[nextChar] != null) {
                return min(node.next[nextChar], prefix.append(nextChar));
            }
        }

        return prefix.toString();
    }

    public String max() {
        if (isEmpty()) {
            return null;
        }

        return max(root, new StringBuilder());
    }

    private String max(Node node, StringBuilder prefix) {

        for (char nextChar = R - 1; true; nextChar--) {
            if (node.next[nextChar] != null) {
                return max(node.next[nextChar], prefix.append(nextChar));
            }

            // nextChar value never becomes less than zero in the for loop, so we need this extra validation
            if (nextChar == 0) {
                break;
            }
        }

        return prefix.toString();
    }

    public void deleteMin() {
        if (isEmpty()) {
            return;
        }

        String minKey = min();
        delete(minKey);
    }

    public void deleteMax() {
        if (isEmpty()) {
            return;
        }

        String maxKey = max();
        delete(maxKey);
    }

}
