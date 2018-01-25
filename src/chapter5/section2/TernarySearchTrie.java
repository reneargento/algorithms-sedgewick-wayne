package chapter5.section2;

import chapter1.section3.Queue;

/**
 * Created by Rene Argento on 21/01/18.
 */
public class TernarySearchTrie<Value> {

    private int size;
    protected Node root;

    protected class Node {
        char character;
        Value value;
        int size;

        Node left;
        Node middle;
        Node right;
    }

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
        return node.value;
    }

    protected Node get(Node node, String key, int digit) {
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
        char currentChar = key.charAt(digit);

        if (node == null) {
            node = new Node();
            node.character = currentChar;
        }

        if (currentChar < node.character) {
            node.left = put(node.left, key, value, digit, isNewKey);
        } else if (currentChar > node.character) {
            node.right = put(node.right, key, value, digit, isNewKey);
        } else if (digit < key.length() - 1){
            node.middle = put(node.middle, key, value, digit + 1, isNewKey);

            if (isNewKey) {
                node.size = node.size + 1;
            }
        } else {
            node.value = value;

            if (isNewKey) {
                node.size = node.size + 1;
                size++;
            }
        }

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
        if (node == null) {
            return null;
        }

        if (digit == key.length() - 1) {
            node.size = node.size - 1;
            node.value = null;
        } else {
            char nextChar = key.charAt(digit);

            if (nextChar < node.character) {
                node.left = delete(node.left, key, digit);
            } else if (nextChar > node.character) {
                node.right = delete(node.right, key, digit);
            } else {
                node.size = node.size - 1;
                node.middle = delete(node.middle, key, digit + 1);
            }
        }

        if (node.size == 0) {
            if (node.left == null && node.right == null) {
                return null;
            } else if(node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node aux = node;
                node = min(aux.right);
                node.right = deleteMin(aux.right);
                node.left = aux.left;
            }
        }

        return node;
    }

    private Node min(Node node) {
        if(node.left == null) {
            return node;
        }

        return min(node.left);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return node.right;
        }

        node.left = deleteMin(node.left);
        return node;
    }

    public Iterable<String> keys() {
        Queue<String> queue = new Queue<>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }

        Queue<String> queue = new Queue<>();

        Node nodeWithPrefix = get(root, prefix, 0);

        if (nodeWithPrefix == null) {
            return queue;
        }

        if (nodeWithPrefix.value != null) {
            queue.enqueue(prefix);
        }

        collect(nodeWithPrefix.middle, new StringBuilder(prefix), queue);
        return queue;
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

        Queue<String> queue = new Queue<>();
        collect(root, new StringBuilder(), pattern, queue);
        return queue;
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
            }
            if (digit < pattern.length() - 1) {
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
            length = digit;
        }

        char nextChar = query.charAt(digit);

        if (nextChar < node.character) {
            return search(node.left, query, digit, length);
        } else if (nextChar > node.character) {
            return search(node.right, query, digit, length);
        } else if (digit < query.length() - 1) {
            return search(node.middle, query, digit + 1, length);
        } else {
            return length + 1;
        }
    }

}
