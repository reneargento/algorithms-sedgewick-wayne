package chapter5.section2;

import chapter1.section3.Queue;
import chapter1.section3.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 26/01/18.
 */
public class Exercise5 {

    @SuppressWarnings("unchecked")
    public static class TrieIterative<Value> {

        private static final int R = 256; // radix
        private Node root = new Node();

        private static class Node {
            private Object value;
            private Node[] next = new Node[R];
            private int size;
        }

        private class NodeWithInformation {
            private Node node;
            private StringBuilder prefix;
            private int digit;
            private boolean mustBeEqualDigit;

            NodeWithInformation(Node node, StringBuilder prefix) {
                this.node = node;
                this.prefix = prefix;
            }

            NodeWithInformation(Node node, StringBuilder prefix, int digit, boolean mustBeEqualDigit) {
                this.node = node;
                this.prefix = prefix;
                this.digit = digit;
                this.mustBeEqualDigit = mustBeEqualDigit;
            }
        }

        public int size() {
            return size(root);
        }

        private int size(Node node) {
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

            Node node = getNode(key);

            if (node == null) {
                return null;
            }
            return (Value) node.value;
        }

        private Node getNode(String key) {
            Node currentNode = root;
            int digit = 0;

            while (currentNode != null) {
                if (digit == key.length()) {
                    break;
                }

                char nextChar = key.charAt(digit); // Use digitTh key char to identify subtrie.
                currentNode = currentNode.next[nextChar];
                digit++;
            }

            return currentNode;
        }

        public void put(String key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            boolean isNewKey = !contains(key);

            Node parent = root;
            Node currentNode = root;
            int digit = 0;
            char nextChar = key.charAt(0);

            while (digit <= key.length()) {

                if (currentNode == null) {
                    currentNode = new Node();
                    parent.next[nextChar] = currentNode;
                }

                parent = currentNode;

                if (isNewKey) {
                    currentNode.size = currentNode.size + 1;
                }

                if (digit == key.length()) {
                    currentNode.value = value;
                    return;
                }

                nextChar = key.charAt(digit); // Use digitTh key char to identify subtrie.
                currentNode = currentNode.next[nextChar];
                digit++;
            }
        }

        public void delete(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            Node parent;
            Node currentNode = root;
            int digit = 0;

            while (currentNode != null) {
                parent = currentNode;
                currentNode.size = currentNode.size - 1;

                if (digit == key.length()) {
                    currentNode.value = null;
                    return;
                } else {
                    char nextChar = key.charAt(digit);
                    currentNode = currentNode.next[nextChar];

                    if (currentNode != null && currentNode.size == 1) {
                        parent.next[nextChar] = null;
                        return;
                    }

                    digit++;
                }
            }
        }

        public Iterable<String> keys() {
            return keysWithPrefix("");
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("Prefix cannot be null");
            }

            Queue<String> keysWithPrefix = new Queue<>();
            Node nodeWithPrefix = getNode(prefix);

            if (nodeWithPrefix == null) {
                return keysWithPrefix;
            }

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(nodeWithPrefix, new StringBuilder(prefix)));

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;

                if (currentNode.value != null) {
                    keysWithPrefix.enqueue(currentPrefix.toString());
                }

                // Since we are using a stack to iterate over all keys, start with the last letters in order to get
                // keys in alphabetical order
                for (char nextChar = R - 1; true; nextChar--) {
                    if (currentNode.next[nextChar] != null) {
                        stack.push(new NodeWithInformation(currentNode.next[nextChar],
                                new StringBuilder(currentPrefix).append(nextChar)));
                    }

                    // nextChar value never becomes less than zero in the for loop, so we need this extra validation
                    if (nextChar == 0) {
                        break;
                    }
                }
            }

            return keysWithPrefix;
        }

        public Iterable<String> keysThatMatch(String pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern cannot be null");
            }

            Queue<String> keysThatMatch = new Queue<>();

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(root, new StringBuilder()));

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;

                int digit = currentPrefix.length();
                if (digit == pattern.length() && currentNode.value != null) {
                    keysThatMatch.enqueue(currentPrefix.toString());
                }

                if (digit == pattern.length()) {
                    continue;
                }

                char nextCharInPattern = pattern.charAt(digit);

                for (char nextChar = R - 1; true; nextChar--) {
                    if (nextCharInPattern == '.' || nextCharInPattern == nextChar) {
                        if (currentNode.next[nextChar] != null) {
                            stack.push(new NodeWithInformation(currentNode.next[nextChar],
                                    new StringBuilder(currentPrefix).append(nextChar)));
                        }
                    }

                    // nextChar value never becomes less than zero in the for loop, so we need this extra validation
                    if (nextChar == 0) {
                        break;
                    }
                }
            }

            return keysThatMatch;
        }

        public String longestPrefixOf(String query) {
            if (query == null) {
                throw new IllegalArgumentException("Query cannot be null");
            }

            if (isEmpty()) {
                return null;
            }

            Node currentNode = root;
            int length = 0;
            int digit = 0;

            while (currentNode != null) {

                if (currentNode.value != null) {
                    length = digit;
                }

                if (digit == query.length()) {
                    break;
                }

                char nextChar = query.charAt(digit);

                if (currentNode.next[nextChar] != null) {
                    currentNode = currentNode.next[nextChar];
                    digit++;
                } else {
                    break;
                }
            }

            return query.substring(0, length);
        }

        // Ordered methods

        // Returns the highest key in the symbol table smaller than or equal to key.
        public String floor(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (isEmpty()) {
                return null;
            }

            String lastKeyFound = null;

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(root, new StringBuilder()));

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;
                int currentDigit = currentNodeWithInformation.digit;

                if (currentDigit == 0) {
                    currentNodeWithInformation.mustBeEqualDigit = true;
                }

                boolean mustBeEqualDigit = currentNodeWithInformation.mustBeEqualDigit;

                // Highest keys will be on the top of the stack
                if (currentNode.value != null) {
                    String currentKey = currentPrefix.toString();

                    if (lastKeyFound != null && currentKey.compareTo(lastKeyFound) < 0) {
                        return lastKeyFound;
                    }

                    lastKeyFound = currentPrefix.toString();
                }

                char rightChar;

                if (mustBeEqualDigit && currentDigit < key.length()) {
                    rightChar = key.charAt(currentDigit);
                } else {
                    rightChar = R - 1;
                }

                for (char nextChar = 0; nextChar <= rightChar; nextChar++) {
                    if (currentNode.next[nextChar] != null) {
                        if (nextChar < rightChar) {
                            mustBeEqualDigit = false;
                        } else if (currentNodeWithInformation.mustBeEqualDigit && nextChar == rightChar) {
                            mustBeEqualDigit = true;
                        }

                        String currentKey = currentPrefix + String.valueOf(nextChar);
                        if (currentKey.compareTo(key) > 0) {
                            continue;
                        }

                        stack.push(new NodeWithInformation(currentNode.next[nextChar],
                                new StringBuilder(currentPrefix).append(nextChar), currentDigit + 1, mustBeEqualDigit));
                    }
                }
            }

            return lastKeyFound;
        }

        // Returns the smallest key in the symbol table greater than or equal to key.
        public String ceiling(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (isEmpty()) {
                return null;
            }

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(root, new StringBuilder()));

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;
                int currentDigit = currentNodeWithInformation.digit;

                if (currentDigit == 0) {
                    currentNodeWithInformation.mustBeEqualDigit = true;
                }

                boolean mustBeEqualDigit = currentNodeWithInformation.mustBeEqualDigit;

                // Lowest keys will be on the top of the stack
                if (currentNode.value != null && currentPrefix.toString().compareTo(key) >= 0) {
                    return currentPrefix.toString();
                }

                char leftChar;

                if (mustBeEqualDigit && currentDigit < key.length()) {
                    leftChar = key.charAt(currentDigit);
                } else {
                    leftChar = 0;
                }

                for (char nextChar = R - 1; true; nextChar--) {
                    if (currentNode.next[nextChar] != null) {
                        if (nextChar > leftChar) {
                            mustBeEqualDigit = false;
                        } else if (currentNodeWithInformation.mustBeEqualDigit && nextChar == leftChar) {
                            mustBeEqualDigit = true;
                        }

                        stack.push(new NodeWithInformation(currentNode.next[nextChar],
                                new StringBuilder(currentPrefix).append(nextChar), currentDigit + 1, mustBeEqualDigit));
                    }

                    if (nextChar == leftChar) {
                        break;
                    }
                }
            }

            return null;
        }

        public String select(int index) {
            if (index < 0 || index >= size()) {
                throw new IllegalArgumentException("Index cannot be negative and must be lower than trie size");
            }

            Node currentNode = root;
            StringBuilder prefix = new StringBuilder();

            while (currentNode != null) {

                if (currentNode.value != null) {
                    index--;

                    // Found the key with the target index
                    if (index == -1) {
                        return prefix.toString();
                    }
                }

                for (char nextChar = 0; nextChar < R; nextChar++) {
                    if (currentNode.next[nextChar] != null) {

                        if (index - size(currentNode.next[nextChar]) < 0) {
                            currentNode = currentNode.next[nextChar];
                            prefix.append(nextChar);
                            break;
                        } else {
                            index = index - size(currentNode.next[nextChar]);
                        }
                    }
                }

            }

            return null;
        }

        public int rank(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Node currentNode = root;
            int digit = 0;
            int size = 0;

            while (currentNode != null) {
                if (digit == key.length()) {
                    return size;
                }

                // If a prefix key was found, add 1 to rank
                if (currentNode.value != null) {
                    if (digit < key.length()) {
                        size++;
                    } else {
                        return size;
                    }
                }

                char currentChar = key.charAt(digit);

                for (char nextChar = 0; nextChar < currentChar; nextChar++) {
                    size += size(currentNode.next[nextChar]);
                }

                currentNode = currentNode.next[currentChar];
                digit++;
            }

            return size;
        }

        public String min() {
            if (isEmpty()) {
                return null;
            }

            Node currentNode = root;
            StringBuilder prefix = new StringBuilder();
            boolean hasNextCharacter = true;

            while (hasNextCharacter) {
                hasNextCharacter = false;

                for (char nextChar = 0; nextChar < R; nextChar++) {
                    if (currentNode.next[nextChar] != null) {
                        currentNode = currentNode.next[nextChar];
                        prefix.append(nextChar);

                        if (currentNode.value != null) {
                            return prefix.toString();
                        }

                        hasNextCharacter = true;
                        break;
                    }
                }
            }

            return null;
        }

        public String max() {
            if (isEmpty()) {
                return null;
            }

            Node currentNode = root;
            StringBuilder prefix = new StringBuilder();
            String maxKey = null;
            boolean hasNextCharacter = true;

            while (hasNextCharacter) {
                hasNextCharacter = false;

                for (char nextChar = R - 1; true; nextChar--) {
                    if (currentNode.next[nextChar] != null) {
                        currentNode = currentNode.next[nextChar];
                        prefix.append(nextChar);

                        if (currentNode.value != null) {
                            maxKey = prefix.toString();
                        }

                        hasNextCharacter = true;
                        break;
                    }

                    // nextChar value never becomes less than zero in the for loop, so we need this extra validation
                    if (nextChar == 0) {
                        break;
                    }
                }
            }

            return maxKey;
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

    public static class TernarySearchTrieIterative<Value> {

        private int size;
        private Node root;

        private class Node {
            private char character;
            private Value value;
            private int size;

            private Node left;
            private Node middle;
            private Node right;
        }

        private class NodeWithInformation {
            private Node node;
            private StringBuilder prefix;
            private int digit;

            NodeWithInformation(Node node, StringBuilder prefix) {
                this.node = node;
                this.prefix = prefix;
            }

            NodeWithInformation(Node node, int digit) {
                this.node = node;
                this.digit = digit;
            }

            NodeWithInformation(Node node, StringBuilder prefix, int digit) {
                this.node = node;
                this.prefix = prefix;
                this.digit = digit;
            }
        }

        private enum Direction {
            LEFT, MIDDLE, RIGHT;
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

            Node node = getNode(key);

            if (node == null) {
                return null;
            }

            return node.value;
        }

        private Node getNode(String key) {
            Node currentNode = root;
            int digit = 0;

            while (digit != key.length()) {
                if (currentNode == null) {
                    return null;
                }

                char currentChar = key.charAt(digit);

                if (currentChar < currentNode.character) {
                    currentNode = currentNode.left;
                } else if (currentChar > currentNode.character) {
                    currentNode = currentNode.right;
                } else if (digit < key.length() - 1) {
                    currentNode = currentNode.middle;
                    digit++;
                } else {
                    return currentNode;
                }
            }

            return null;
        }

        public void put(String key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            boolean isNewKey = !contains(key);
            int digit = 0;

            // Special case: putting the first key in the TST
            if (root == null) {
                root = new Node();
                root.character = key.charAt(digit);

                if (key.length() == 1) {
                    root.value = value;
                    root.size = 1;
                    return;
                }
            }

            Node parent = null;
            Node currentNode = root;
            Direction direction = Direction.LEFT;

            while (digit != key.length()) {
                char currentChar = key.charAt(digit);

                if (currentNode == null) {
                    currentNode = new Node();
                    currentNode.character = currentChar;

                    updateParentReference(parent, currentNode, direction);
                }

                parent = currentNode;

                if (currentChar < currentNode.character) {
                    currentNode = currentNode.left;
                    direction = Direction.LEFT;
                } else if (currentChar > currentNode.character) {
                    currentNode = currentNode.right;
                    direction = Direction.RIGHT;
                } else if (digit < key.length() - 1) {
                    if (isNewKey) {
                        currentNode.size = currentNode.size + 1;
                    }

                    currentNode = currentNode.middle;
                    digit++;
                    direction = Direction.MIDDLE;
                } else {
                    currentNode.value = value;

                    if (isNewKey) {
                        currentNode.size = currentNode.size + 1;
                        size++;
                    }
                    digit++;
                    direction = Direction.MIDDLE;
                }
            }
        }

        private void updateParentReference(Node parent, Node currentNode, Direction direction) {
            switch (direction) {
                case LEFT: parent.left = currentNode;
                    break;
                case MIDDLE: parent.middle = currentNode;
                    break;
                case RIGHT: parent.right = currentNode;
                    break;
            }
        }

        public void delete(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            int digit = 0;

            // Special case: deleting root
            if (root.character == key.charAt(0)
                    && root.size == 1) {
                if (root.left == null && root.right == null) {
                    root = null;
                } else if (root.left == null) {
                    root = root.right;
                } else if (root.right == null) {
                    root = root.left;
                } else {
                    Node aux = root;
                    root = min(aux.right);
                    root.right = deleteMin(aux.right);
                    root.left = aux.left;
                }

                return;
            }

            Node parent = null;
            Node currentNode = root;
            Direction direction = Direction.MIDDLE;

            while (digit != key.length()) {
                char currentChar = key.charAt(digit);

                if (currentChar == currentNode.character && currentNode.size == 1) {
                    if (currentNode.left == null && currentNode.right == null) {
                        updateParentReference(parent, null, direction);
                    } else if (currentNode.left == null) {
                        updateParentReference(parent, currentNode.right, direction);
                    } else if (currentNode.right == null) {
                        updateParentReference(parent, currentNode.left, direction);
                    } else {
                        Node aux = currentNode;
                        currentNode = min(aux.right);
                        currentNode.right = deleteMin(aux.right);
                        currentNode.left = aux.left;
                    }

                    break;
                }

                parent = currentNode;

                if (currentChar < currentNode.character) {
                    currentNode = currentNode.left;
                    direction = Direction.LEFT;
                } else if (currentChar > currentNode.character) {
                    currentNode = currentNode.right;
                    direction = Direction.RIGHT;
                } else {
                    if (digit == key.length() - 1) {
                        currentNode.value = null;
                    }

                    currentNode.size = currentNode.size - 1;

                    digit++;
                    currentNode = currentNode.middle;
                    direction = Direction.MIDDLE;
                }
            }

            size--;
        }

        public Iterable<String> keys() {
            return collect(root, new StringBuilder());
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("Prefix cannot be null");
            }

            Queue<String> keysWithPrefix = new Queue<>();

            Node nodeWithPrefix = getNode(prefix);

            if (nodeWithPrefix == null) {
                return keysWithPrefix;
            }

            if (nodeWithPrefix.value != null) {
                keysWithPrefix.enqueue(prefix);
            }

            Queue<String> otherKeys = collect(nodeWithPrefix.middle, new StringBuilder(prefix));

            for(String key : otherKeys) {
                keysWithPrefix.enqueue(key);
            }

            return keysWithPrefix;
        }

        private Queue<String> collect(Node node, StringBuilder prefix) {
            Queue<String> queue = new Queue<>();

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(node, new StringBuilder(prefix)));

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;

                StringBuilder prefixWithCharacter = new StringBuilder(currentPrefix).append(currentNode.character);

                if (currentNode.value != null) {
                    queue.enqueue(prefixWithCharacter.toString());
                }

                if (currentNode.right != null) {
                   stack.push(new NodeWithInformation(currentNode.right, currentPrefix));
                }

                if (currentNode.middle != null) {
                    stack.push(new NodeWithInformation(currentNode.middle, prefixWithCharacter));
                }

                if (currentNode.left != null) {
                    stack.push(new NodeWithInformation(currentNode.left, currentPrefix));
                }
            }

            return queue;
        }

        public Iterable<String> keysThatMatch(String pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern cannot be null");
            }

            if (isEmpty()) {
                return new Queue<>();
            }

            Queue<String> keysThatMatch = new Queue<>();

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(root, new StringBuilder()));

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;

                StringBuilder prefixWithCharacter = new StringBuilder(currentPrefix).append(currentNode.character);

                int digit = currentPrefix.length();
                char nextCharInPattern = pattern.charAt(digit);

                if (nextCharInPattern == '.' || nextCharInPattern > currentNode.character) {
                    if (currentNode.right != null) {
                        stack.push(new NodeWithInformation(currentNode.right, currentPrefix));
                    }
                }

                if (nextCharInPattern == '.' || nextCharInPattern == currentNode.character) {
                    if (digit == pattern.length() - 1 && currentNode.value != null) {
                        keysThatMatch.enqueue(prefixWithCharacter.toString());
                    } else if (digit < pattern.length() - 1 && currentNode.middle != null) {
                        stack.push(new NodeWithInformation(currentNode.middle, prefixWithCharacter));
                    }
                }

                if (nextCharInPattern == '.' || nextCharInPattern < currentNode.character) {
                    if (currentNode.left != null) {
                        stack.push(new NodeWithInformation(currentNode.left, currentPrefix));
                    }
                }
            }

            return keysThatMatch;
        }

        public String longestPrefixOf(String query) {
            if (query == null) {
                throw new IllegalArgumentException("Query cannot be null");
            }

            int length = search(query);
            return query.substring(0, length);
        }

        private int search(String query) {

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(root, 0));
            int length = 0;

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                int currentDigit = currentNodeWithInformation.digit;

                char nextChar = query.charAt(currentDigit);

                if (currentNode.value != null) {
                    length = currentDigit + 1;
                }

                if (nextChar < currentNode.character && currentNode.left != null) {
                    stack.push(new NodeWithInformation(currentNode.left, currentDigit));
                } else if (nextChar > currentNode.character && currentNode.right != null) {
                    stack.push(new NodeWithInformation(currentNode.right, currentDigit));
                } else {
                    if (nextChar == currentNode.character) {
                        if (currentDigit < query.length() - 1 && currentNode.middle != null) {
                            stack.push(new NodeWithInformation(currentNode.middle, currentDigit + 1));
                        } else {
                            return length;
                        }
                    }
                }
            }

            return length;
        }

        // Ordered methods

        // Returns the highest key in the symbol table smaller than or equal to key.
        public String floor(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(root, new StringBuilder(), 0));
            String lastKeyFound = null;
            boolean mustBeEqualDigit = true;

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;
                int currentDigit = currentNodeWithInformation.digit;

                StringBuilder prefixWithCharacter = new StringBuilder(currentPrefix).append(currentNode.character);

                char currentChar;
                if (currentDigit < key.length() && mustBeEqualDigit) {
                    currentChar = key.charAt(currentDigit);
                } else {
                    currentChar = Character.MAX_VALUE;
                    mustBeEqualDigit = false;
                }

                if (currentChar < currentNode.character && currentNode.left != null && mustBeEqualDigit) {
                    stack.push(new NodeWithInformation(currentNode.left, currentPrefix, currentDigit));
                } else if (!mustBeEqualDigit || currentChar >= currentNode.character) {
                    // Optimization: if current prefix is higher than the search key, left is the only way to go
                    if (prefixWithCharacter.toString().compareTo(key) > 0) {

                        if (currentNode.left != null) {
                            stack.push(new NodeWithInformation(currentNode.left, currentPrefix, currentDigit));
                        }
                        continue;
                    }

                    if (mustBeEqualDigit && currentChar > currentNode.character) {
                        mustBeEqualDigit = false;
                    }

                    if (currentNode.value != null) {
                        lastKeyFound = prefixWithCharacter.toString();
                    }

                    if (!mustBeEqualDigit && currentNode.right != null) {
                        stack.push(new NodeWithInformation(currentNode.right, currentPrefix, currentDigit));
                    }

                    if (currentNode.middle != null) {
                        stack.push(new NodeWithInformation(currentNode.middle, prefixWithCharacter, currentDigit + 1));
                    }
                }
            }

            return lastKeyFound;
        }

        // Returns the smallest key in the symbol table greater than or equal to key.
        public String ceiling(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Stack<NodeWithInformation> stack = new Stack<>();
            stack.push(new NodeWithInformation(root, new StringBuilder(), 0));
            boolean mustBeEqualDigit = true;

            while (!stack.isEmpty()) {
                NodeWithInformation currentNodeWithInformation = stack.pop();
                Node currentNode = currentNodeWithInformation.node;
                StringBuilder currentPrefix = currentNodeWithInformation.prefix;
                int currentDigit = currentNodeWithInformation.digit;

                StringBuilder prefixWithCharacter = new StringBuilder(currentPrefix).append(currentNode.character);

                char currentChar;
                if (currentDigit < key.length() && mustBeEqualDigit) {
                    currentChar = key.charAt(currentDigit);
                } else {
                    currentChar = 0;
                    mustBeEqualDigit = false;
                }

                if (currentChar > currentNode.character && currentNode.right != null && mustBeEqualDigit) {
                    stack.push(new NodeWithInformation(currentNode.right, currentPrefix, currentDigit));
                } else if (currentChar <= currentNode.character) {
                    if (mustBeEqualDigit && currentChar < currentNode.character) {
                        mustBeEqualDigit = false;
                    }

                    if (currentNode.value != null && prefixWithCharacter.toString().compareTo(key) >= 0) {
                        return prefixWithCharacter.toString();
                    }

                    if (currentNode.right != null) {
                        stack.push(new NodeWithInformation(currentNode.right, currentPrefix, currentDigit));
                    }

                    if (currentNode.middle != null) {
                        stack.push(new NodeWithInformation(currentNode.middle, prefixWithCharacter, currentDigit + 1));
                    }

                    if (!mustBeEqualDigit && currentNode.left != null) {
                        stack.push(new NodeWithInformation(currentNode.left, currentPrefix, currentDigit));
                    }
                }
            }

            return null;
        }

        public String select(int index) {
            if (index < 0 || index >= size()) {
                throw new IllegalArgumentException("Index cannot be negative and must be lower than TST size");
            }

            boolean found = false;
            String key = null;
            StringBuilder prefix = new StringBuilder();
            Node currentNode = root;

            while (!found) {
                int leftSubtreeSize = getTreeSize(currentNode.left);
                int tstSize = leftSubtreeSize + currentNode.size;

                if (index < leftSubtreeSize) {
                    currentNode = currentNode.left;
                } else if (index >= tstSize) {
                    currentNode = currentNode.right;
                    index = index - tstSize;
                } else {
                    index = index - leftSubtreeSize;

                    if (currentNode.value != null) {
                        if (index == 0) {
                            key = prefix.append(currentNode.character).toString();
                            found = true;
                        }
                        index--;
                    }

                    prefix.append(currentNode.character);

                    if (currentNode.middle != null) {
                        currentNode = currentNode.middle;
                    }
                }
            }

            return key;
        }

        public int rank(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int rank = 0;
            int digit = 0;
            int size = 0;

            Node currentNode = root;

            while (currentNode != null) {
                char currentChar = key.charAt(digit);

                if (currentChar < currentNode.character) {
                    currentNode = currentNode.left;
                } else  {
                    if (currentChar > currentNode.character) {
                        if (currentNode.value != null) {
                            size++;
                        }

                        rank += getTreeSize(currentNode.left) + getTreeSize(currentNode.middle);

                        currentNode = currentNode.right;
                    } else if (digit < key.length() - 1) {
                        // Is current key a prefix of the search key?
                        if (digit < key.length() - 1 && currentNode.value != null) {
                            size++;
                        }

                        rank += getTreeSize(currentNode.left);

                        currentNode = currentNode.middle;
                        digit++;
                    } else {
                        rank += getTreeSize(currentNode.left) + size;
                        break;
                    }
                }
            }

            return rank;
        }

        private int getTreeSize(Node node) {
            if (node == null) {
                return 0;
            }

            int size = 0;

            Stack<Node> stack = new Stack<>();
            stack.push(node);

            while (!stack.isEmpty()) {
                Node currentNode = stack.pop();

                size += currentNode.size;

                if (currentNode.right != null) {
                    stack.push(currentNode.right);
                }
                if (currentNode.left != null) {
                    stack.push(currentNode.left);
                }
            }

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
            if (node == null) {
                return null;
            }

            Node currentNode = node;

            while (currentNode.left != null) {
                currentNode = currentNode.left;
            }

            return currentNode;
        }

        public String max() {
            Node maxNode = max(root);

            if (maxNode == null) {
                return null;
            }

            StringBuilder maxKey = new StringBuilder();
            maxKey.append(maxNode.character);

            while (maxNode.value == null) {
                maxNode = maxNode.middle;

                while (maxNode.right != null) {
                    maxNode = maxNode.right;
                }
                maxKey.append(maxNode.character);
            }

            return maxKey.toString();
        }

        private Node max(Node node) {
            if (node == null) {
                return null;
            }

            Node currentNode = node;

            while (currentNode.right != null) {
                currentNode = currentNode.right;
            }

            return currentNode;
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
            Node currentNode = node;

            while (currentNode.left != null) {
                currentNode = currentNode.left;
            }

            return currentNode.right;
        }

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            String maxKey = max();
            delete(maxKey);
        }
    }

    public static void main(String[] args) {
        Exercise5 exercise5 = new Exercise5();
        exercise5.trieTests();
        exercise5.tstTests();
    }

    private void trieTests() {
        StdOut.println("********Trie tests********");
        TrieIterative<Integer> trieIterative = new TrieIterative<>();

        // Put tests
        trieIterative.put("Rene", 0);
        trieIterative.put("Re", 1);
        trieIterative.put("Re", 10);
        trieIterative.put("Algorithms", 2);
        trieIterative.put("Algo", 3);
        trieIterative.put("Algor", 4);
        trieIterative.put("Tree", 5);
        trieIterative.put("Trie", 6);
        trieIterative.put("TST", 7);
        trieIterative.put("Trie123", 8);
        trieIterative.put("Z-Function", 9);

        // Get tests
        StdOut.println("Get Re: " + trieIterative.get("Re"));
        StdOut.println("Expected: 10");
        StdOut.println("Get Algorithms: " + trieIterative.get("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Get Trie123: " + trieIterative.get("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Get Algori: " + trieIterative.get("Algori"));
        StdOut.println("Expected: null");
        StdOut.println("Get Zooom: " + trieIterative.get("Zooom"));
        StdOut.println("Expected: null");

        // Keys with prefix tests
        StdOut.println("\nKeys with prefix Alg");
        StringJoiner keysWithPrefix1 = new StringJoiner(" ");

        for(String key : trieIterative.keysWithPrefix("Alg")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println(keysWithPrefix1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with prefix T");
        StringJoiner keysWithPrefix2 = new StringJoiner(" ");

        for(String key : trieIterative.keysWithPrefix("T")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println(keysWithPrefix2.toString());
        StdOut.println("Expected: TST Tree Trie Trie123");

        StdOut.println("\nKeys with prefix R");
        StringJoiner keysWithPrefix3 = new StringJoiner(" ");

        for(String key : trieIterative.keysWithPrefix("R")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println(keysWithPrefix3.toString());
        StdOut.println("Expected: Re Rene");

        StdOut.println("\nKeys with prefix ZZZ");
        StringJoiner keysWithPrefix4 = new StringJoiner(" ");

        for(String key : trieIterative.keysWithPrefix("ZZZ")) {
            keysWithPrefix4.add(key);
        }
        StdOut.println(keysWithPrefix4.toString());
        StdOut.println("Expected: ");

        // Keys that match tests
        StdOut.println("\nKeys that match Alg..");
        StringJoiner keysThatMatch1 = new StringJoiner("Alg..");

        for(String key : trieIterative.keysThatMatch("Alg..")) {
            keysThatMatch1.add(key);
        }
        StdOut.println(keysThatMatch1.toString());
        StdOut.println("Expected: Algor");

        StdOut.println("\nKeys that match Re");
        StringJoiner keysThatMatch2 = new StringJoiner(" ");

        for(String key : trieIterative.keysThatMatch("Re")) {
            keysThatMatch2.add(key);
        }
        StdOut.println(keysThatMatch2.toString());
        StdOut.println("Expected: Re");

        StdOut.println("\nKeys that match Tr.e");
        StringJoiner keysThatMatch3 = new StringJoiner(" ");

        for(String key : trieIterative.keysThatMatch("Tr.e")) {
            keysThatMatch3.add(key);
        }
        StdOut.println(keysThatMatch3.toString());
        StdOut.println("Expected: Tree Trie");

        // Longest-prefix-of tests
        StdOut.println("\nLongest prefix of Re: " + trieIterative.longestPrefixOf("Re"));
        StdOut.println("Expected: Re");

        StdOut.println("Longest prefix of Algori: " + trieIterative.longestPrefixOf("Algori"));
        StdOut.println("Expected: Algor");

        StdOut.println("Longest prefix of Trie12345: " + trieIterative.longestPrefixOf("Trie12345"));
        StdOut.println("Expected: Trie123");

        StdOut.println("Longest prefix of Zooom: " + trieIterative.longestPrefixOf("Zooom"));
        StdOut.println("Expected: ");

        // Min tests
        StdOut.println("\nMin key: " + trieIterative.min());
        StdOut.println("Expected: Algo");

        // Max tests
        StdOut.println("\nMax key: " + trieIterative.max());
        StdOut.println("Expected: Z-Function");

        // Delete min and delete max tests
        trieIterative.put("ABCKey", 11);
        trieIterative.put("ZKey", 12);

        StdOut.println("\nKeys after ABCKey and ZKey insert: ");
        for(String key : trieIterative.keys()) {
            StdOut.println(key);
        }

        trieIterative.deleteMin();

        StdOut.println("\nKeys after deleteMin: ");
        for(String key : trieIterative.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ABCKey deleted");

        trieIterative.deleteMax();

        StdOut.println("\nKeys after deleteMax: ");
        for(String key : trieIterative.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ZKey deleted");

        // Floor tests
        StdOut.println("\nFloor of Re: " + trieIterative.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + trieIterative.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + trieIterative.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + trieIterative.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + trieIterative.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + trieIterative.floor("Zoom"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Floor of TAB: " + trieIterative.floor("TAB"));
        StdOut.println("Expected: Rene");

        // Ceiling tests
        StdOut.println("\nCeiling of Re: " + trieIterative.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + trieIterative.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + trieIterative.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + trieIterative.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + trieIterative.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + trieIterative.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Tro: " + trieIterative.ceiling("Tro"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Ceiling of Ruby: " + trieIterative.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        // Select tests
        StdOut.println("\nSelect 0: " + trieIterative.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + trieIterative.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + trieIterative.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + trieIterative.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + trieIterative.select(8));
        StdOut.println("Expected: Trie123");
        StdOut.println("Select 9: " + trieIterative.select(9));
        StdOut.println("Expected: Z-Function");

        // Rank tests
        StdOut.println("\nRank of R: " + trieIterative.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + trieIterative.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + trieIterative.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + trieIterative.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + trieIterative.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + trieIterative.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + trieIterative.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + trieIterative.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zzoom: " + trieIterative.rank("Zzoom"));
        StdOut.println("Expected: 10");

        // Delete tests
        trieIterative.delete("Z-Function");

        StdOut.println("\nKeys() after deleting Z-Function key");
        for(String key : trieIterative.keys()) {
            StdOut.println(key);
        }

        trieIterative.delete("Re");

        StdOut.println("\nKeys() after deleting Re key");
        for(String key : trieIterative.keys()) {
            StdOut.println(key);
        }

        trieIterative.delete("Rene");

        StdOut.println("\nKeys() after deleting Rene key");
        for(String key : trieIterative.keys()) {
            StdOut.println(key);
        }
    }

    private void tstTests() {
        StdOut.println("\n********Ternary Search Trie tests********");
        TernarySearchTrieIterative<Integer> ternarySearchTrieIterative = new TernarySearchTrieIterative<>();

        // Put tests
        ternarySearchTrieIterative.put("Rene", 0);
        ternarySearchTrieIterative.put("Re", 1);
        ternarySearchTrieIterative.put("Re", 10);
        ternarySearchTrieIterative.put("Algorithms", 2);
        ternarySearchTrieIterative.put("Algo", 3);
        ternarySearchTrieIterative.put("Algor", 4);
        ternarySearchTrieIterative.put("Tree", 5);
        ternarySearchTrieIterative.put("Trie", 6);
        ternarySearchTrieIterative.put("TST", 7);
        ternarySearchTrieIterative.put("Trie123", 8);
        ternarySearchTrieIterative.put("Z-Function", 9);

        // Get tests
        StdOut.println("Get Re: " + ternarySearchTrieIterative.get("Re"));
        StdOut.println("Expected: 10");
        StdOut.println("Get Algorithms: " + ternarySearchTrieIterative.get("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Get Trie123: " + ternarySearchTrieIterative.get("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Get Algori: " + ternarySearchTrieIterative.get("Algori"));
        StdOut.println("Expected: null");
        StdOut.println("Get Zooom: " + ternarySearchTrieIterative.get("Zooom"));
        StdOut.println("Expected: null");

        // Keys with prefix tests
        StdOut.println("\nKeys with prefix Alg");
        StringJoiner keysWithPrefix1 = new StringJoiner(" ");

        for(String key : ternarySearchTrieIterative.keysWithPrefix("Alg")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println(keysWithPrefix1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with prefix T");
        StringJoiner keysWithPrefix2 = new StringJoiner(" ");

        for(String key : ternarySearchTrieIterative.keysWithPrefix("T")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println(keysWithPrefix2.toString());
        StdOut.println("Expected: TST Tree Trie Trie123");

        StdOut.println("\nKeys with prefix R");
        StringJoiner keysWithPrefix3 = new StringJoiner(" ");

        for(String key : ternarySearchTrieIterative.keysWithPrefix("R")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println(keysWithPrefix3.toString());
        StdOut.println("Expected: Re Rene");

        StdOut.println("\nKeys with prefix ZZZ");
        StringJoiner keysWithPrefix4 = new StringJoiner(" ");

        for(String key : ternarySearchTrieIterative.keysWithPrefix("ZZZ")) {
            keysWithPrefix4.add(key);
        }
        StdOut.println(keysWithPrefix4.toString());
        StdOut.println("Expected: ");

        // Keys that match tests
        StdOut.println("\nKeys that match Alg..");
        StringJoiner keysThatMatch1 = new StringJoiner("Alg..");

        for(String key : ternarySearchTrieIterative.keysThatMatch("Alg..")) {
            keysThatMatch1.add(key);
        }
        StdOut.println(keysThatMatch1.toString());
        StdOut.println("Expected: Algor");

        StdOut.println("\nKeys that match Re");
        StringJoiner keysThatMatch2 = new StringJoiner(" ");

        for(String key : ternarySearchTrieIterative.keysThatMatch("Re")) {
            keysThatMatch2.add(key);
        }
        StdOut.println(keysThatMatch2.toString());
        StdOut.println("Expected: Re");

        StdOut.println("\nKeys that match Tr.e");
        StringJoiner keysThatMatch3 = new StringJoiner(" ");

        for(String key : ternarySearchTrieIterative.keysThatMatch("Tr.e")) {
            keysThatMatch3.add(key);
        }
        StdOut.println(keysThatMatch3.toString());
        StdOut.println("Expected: Tree Trie");

        // Longest-prefix-of tests
        StdOut.println("\nLongest prefix of Re: " + ternarySearchTrieIterative.longestPrefixOf("Re"));
        StdOut.println("Expected: Re");

        StdOut.println("Longest prefix of Algori: " + ternarySearchTrieIterative.longestPrefixOf("Algori"));
        StdOut.println("Expected: Algor");

        StdOut.println("Longest prefix of Trie12345: " + ternarySearchTrieIterative.longestPrefixOf("Trie12345"));
        StdOut.println("Expected: Trie123");

        StdOut.println("Longest prefix of Zooom: " + ternarySearchTrieIterative.longestPrefixOf("Zooom"));
        StdOut.println("Expected: ");

        // Min tests
        StdOut.println("\nMin key: " + ternarySearchTrieIterative.min());
        StdOut.println("Expected: Algo");

        // Max tests
        StdOut.println("\nMax key: " + ternarySearchTrieIterative.max());
        StdOut.println("Expected: Z-Function");

        // Delete min and delete max tests
        ternarySearchTrieIterative.put("ABCKey", 11);
        ternarySearchTrieIterative.put("ZKey", 12);

        StdOut.println("\nKeys after ABCKey and ZKey insert: ");
        for(String key : ternarySearchTrieIterative.keys()) {
            StdOut.println(key);
        }

        ternarySearchTrieIterative.deleteMin();

        StdOut.println("\nKeys after deleteMin: ");
        for(String key : ternarySearchTrieIterative.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ABCKey deleted");

        ternarySearchTrieIterative.deleteMax();

        StdOut.println("\nKeys after deleteMax: ");
        for(String key : ternarySearchTrieIterative.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ZKey deleted");

        // Floor tests
        StdOut.println("\nFloor of Re: " + ternarySearchTrieIterative.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + ternarySearchTrieIterative.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + ternarySearchTrieIterative.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + ternarySearchTrieIterative.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + ternarySearchTrieIterative.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + ternarySearchTrieIterative.floor("Zoom"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Floor of TAB: " + ternarySearchTrieIterative.floor("TAB"));
        StdOut.println("Expected: Rene");

        // Ceiling tests
        StdOut.println("\nCeiling of Re: " + ternarySearchTrieIterative.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + ternarySearchTrieIterative.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + ternarySearchTrieIterative.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + ternarySearchTrieIterative.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + ternarySearchTrieIterative.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + ternarySearchTrieIterative.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Tro: " + ternarySearchTrieIterative.ceiling("Tro"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Ceiling of Ruby: " + ternarySearchTrieIterative.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        // Select tests
        StdOut.println("\nSelect 0: " + ternarySearchTrieIterative.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + ternarySearchTrieIterative.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + ternarySearchTrieIterative.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + ternarySearchTrieIterative.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + ternarySearchTrieIterative.select(8));
        StdOut.println("Expected: Trie123");
        StdOut.println("Select 9: " + ternarySearchTrieIterative.select(9));
        StdOut.println("Expected: Z-Function");

        // Rank tests
        StdOut.println("\nRank of R: " + ternarySearchTrieIterative.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + ternarySearchTrieIterative.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + ternarySearchTrieIterative.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + ternarySearchTrieIterative.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + ternarySearchTrieIterative.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + ternarySearchTrieIterative.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + ternarySearchTrieIterative.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + ternarySearchTrieIterative.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zzoom: " + ternarySearchTrieIterative.rank("Zzoom"));
        StdOut.println("Expected: 10");

        // Delete tests
        ternarySearchTrieIterative.delete("Z-Function");

        StdOut.println("\nKeys() after deleting Z-Function key");
        for(String key : ternarySearchTrieIterative.keys()) {
            StdOut.println(key);
        }

        ternarySearchTrieIterative.delete("Re");

        StdOut.println("\nKeys() after deleting Re key");
        for(String key : ternarySearchTrieIterative.keys()) {
            StdOut.println(key);
        }

        ternarySearchTrieIterative.delete("Rene");

        StdOut.println("\nKeys() after deleting Rene key");
        for(String key : ternarySearchTrieIterative.keys()) {
            StdOut.println(key);
        }
    }

}