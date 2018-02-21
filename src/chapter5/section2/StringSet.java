package chapter5.section2;

import java.util.*;

/**
 * Created by Rene Argento on 08/02/18.
 */
public class StringSet {

    protected class Node {
        protected Map<Character, Node> next = new HashMap<>();
        protected boolean isKey;
    }

    protected Node root = new Node();
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

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
        return contains(node.next.get(nextChar), key, digit + 1);
    }

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

            if (childNode != null) {
                node.next.put(nextChar, childNode);
            } else {
                node.next.remove(nextChar);
            }
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

        for (Character character : node.next.keySet()) {
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
