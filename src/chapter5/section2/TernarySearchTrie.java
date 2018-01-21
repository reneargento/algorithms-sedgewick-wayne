package chapter5.section2;

/**
 * Created by Rene Argento on 21/01/18.
 */
public class TernarySearchTrie<Value> {

    private Node root;

    private class Node {
        char character;
        Node left;
        Node middle;
        Node right;
        Value value;
    }

    public Value get(String key) {
        Node node = get(root, key, 0);

        if (node == null) {
            return null;
        }
        return node.value;
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

    public void put(String key, Value value) {
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
        } else if (digit < key.length() - 1){
            node.middle = put(node.middle, key, value, digit + 1);
        } else {
            node.value = value;
        }

        return node;
    }

}
