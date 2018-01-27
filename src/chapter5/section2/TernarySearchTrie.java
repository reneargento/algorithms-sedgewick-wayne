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

    // Ordered methods

    // Returns the highest key in the symbol table smaller than or equal to key.
    public String floor(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return floor(root, key, 0, new StringBuilder(), null);
    }

    private String floor(Node node, String key, int digit, StringBuilder prefix, String lastKeyFound) {
        if (node == null) {
            return lastKeyFound;
        }

        char currentChar;
        if (digit < key.length()) {
            currentChar = key.charAt(digit);
        } else {
            currentChar = Character.MAX_VALUE;
        }

        if (currentChar < node.character) {
            return floor(node.left, key, digit, prefix, lastKeyFound);
        } else {
            if ((prefix.toString() + node.character).compareTo(key) > 0) {
                return lastKeyFound;
            }

            if (currentChar == node.character) {
                if (node.value != null) {
                    lastKeyFound = prefix.toString() + node.character;
                }

                return floor(node.middle, key, digit + 1, prefix.append(node.character), lastKeyFound);
            } else {
                if (node.right != null) {
                    String keyFound = floor(node.right, key, digit, prefix, lastKeyFound);

                    if (keyFound != null) {
                        return keyFound;
                    }
                }

                // There are no keys higher than key in this subTST, so we can get the highest key
                prefix.append(node.character);
                Node currentNode = node;

                while (currentNode.size != 1 || currentNode.value == null) {
                    currentNode = currentNode.middle;

                    while (currentNode.right != null) {
                        currentNode = currentNode.right;
                    }
                    prefix.append(currentNode.character);
                }

                return prefix.toString();
            }
        }
    }

    // Returns the smallest key in the symbol table greater than or equal to key.
    public String ceiling(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return ceiling(root, key, 0, new StringBuilder());
    }

    private String ceiling(Node node, String key, int digit, StringBuilder prefix) {
        if (node == null) {
            return null;
        }

        char currentChar;
        if (digit < key.length()) {
            currentChar = key.charAt(digit);
        } else {
            currentChar = 0;
        }

        if (currentChar > node.character) {
            return ceiling(node.right, key, digit, prefix);
        } else if (currentChar == node.character) {
            if (node.value != null && (prefix.toString() + node.character).compareTo(key) >= 0) {
                return prefix.toString() + node.character;
            }

            return ceiling(node.middle, key, digit + 1, prefix.append(node.character));
        } else {

            if (node.left != null) {
                String keyFound = ceiling(node.left, key, digit, prefix);

                if (keyFound != null) {
                    return keyFound;
                }
            }

            // There are no keys smaller than key in this subTST, so we can get the smallest key
            prefix.append(node.character);
            Node currentNode = node;

            while (currentNode.value == null) {
                currentNode = currentNode.middle;

                while (currentNode.left != null) {
                    currentNode = currentNode.left;
                }
                prefix.append(currentNode.character);
            }

            return prefix.toString();
        }

    }

    public String select(int index) {
        if(index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index cannot be negative and must be lower than TST size");
        }

        return select(root, index, new StringBuilder());
    }

    private String select(Node node, int index, StringBuilder prefix) {
        if (node == null) {
            return null;
        }

        int leftSubtreeSize = getTreeSize(node.left);
        int tstSize = leftSubtreeSize + node.size;

        if (index < leftSubtreeSize) {
            return select(node.left, index, prefix);
        } else if (index >= tstSize) {
            return select(node.right, index - tstSize, prefix);
        } else {
            if (node.value != null) {
                if (index == 0) {
                    return prefix.append(node.character).toString();
                }
                index--;
            }

            prefix.append(node.character);
            return select(node.middle, index - leftSubtreeSize, prefix);
        }
    }

    public int rank(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return rank(root, key, 0, 0);
    }

    private int rank(Node node, String key, int digit, int size) {
        if (node == null) {
            return size;
        }

        char currentChar = key.charAt(digit);

        if (currentChar < node.character) {
            return rank(node.left, key, digit, size);
        } else {
            // Is current key a prefix of the search key?
            if (digit < key.length() - 1 && node.value != null) {
                size++;
            }

            if (currentChar > node.character) {
                return getTreeSize(node.left) + getTreeSize(node.middle) +
                        rank(node.right, key, digit, size);
            } else if (digit < key.length() - 1) {
                return getTreeSize(node.left) + rank(node.middle, key, digit + 1, size);
            } else {
                return getTreeSize(node.left) + size;
            }
        }
    }

    private int getTreeSize(Node node) {
        if (node == null) {
            return 0;
        }

        int size = node.size;
        size += getTreeSize(node.left);
        size += getTreeSize(node.right);

        return size;
    }

    public String min() {
        Node minNode = min(root);

        if (minNode == null) {
            return null;
        }

        StringBuilder minKey = new StringBuilder();
        minKey.append(minNode.character);

        while (minNode.value == null) {
            minNode = minNode.middle;

            while (minNode.left != null) {
                minNode = minNode.left;
            }
            minKey.append(minNode.character);
        }

        return minKey.toString();
    }

    private Node min(Node node) {
        if (node.left == null) {
            return node;
        }

        return min(node.left);
    }

    public String max() {
        Node maxNode = max(root);

        if (maxNode == null) {
            return null;
        }

        StringBuilder maxKey = new StringBuilder();
        maxKey.append(maxNode.character);

        // Verify if size is different than 1 to avoid getting max key prefixes instead of the max key
        while (maxNode.size != 1 || maxNode.value == null) {
            maxNode = maxNode.middle;

            while (maxNode.right != null) {
                maxNode = maxNode.right;
            }
            maxKey.append(maxNode.character);
        }

        return maxKey.toString();
    }

    private Node max(Node node) {
        if (node.right == null) {
            return node;
        }

        return max(node.right);
    }

    public void deleteMin() {
        if (isEmpty()) {
            return;
        }

        String minKey = min();
        delete(minKey);
    }

    // Used only in delete()
    private Node deleteMin(Node node) {
        if (node.left == null) {
            return node.right;
        }

        node.left = deleteMin(node.left);
        return node;
    }

    public void deleteMax() {
        if (isEmpty()) {
            return;
        }

        String maxKey = max();
        delete(maxKey);
    }
}
