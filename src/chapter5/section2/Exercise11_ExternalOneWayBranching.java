package chapter5.section2;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 01/02/18.
 */
public class Exercise11_ExternalOneWayBranching {

    @SuppressWarnings("unchecked")
    public static class TrieNoExternalOneWayBranching<Value> {

        protected static final int R = 256;
        protected Node root = new Node();

        protected static class Node {
            Object value;
            Node[] next = new Node[R];
            int size;
            String characters; // Used to eliminate external one-way branching
        }

        private class NodeWithInformation {
            private Node node;
            private StringBuilder prefix;

            NodeWithInformation(Node node, StringBuilder prefix) {
                this.node = node;
                this.prefix = prefix;
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

            if (digit == key.length() && node.characters == null) {
                return node;
            }

            int nodeCharactersLength = 1;
            if (node.characters != null) {
                nodeCharactersLength = node.characters.length();

                if (digit - 1 + nodeCharactersLength > key.length()) {
                    return null;
                }

                String currentPrefix = key.substring(digit - 1, digit - 1 + nodeCharactersLength);
                int compare = compareStrings(currentPrefix, node.characters);

                if (compare == 0) {
                    if (digit - 1 + nodeCharactersLength == key.length()) {
                        return node;
                    } // If characters match but this is not the end of the key, continue below
                } else {
                    return null;
                }
            }

            char nextChar;
            if (node.characters != null) {
                nextChar = key.charAt(digit - 1 + nodeCharactersLength);
            } else {
                nextChar = key.charAt(digit);
            }

            return get(node.next[nextChar], key, digit + nodeCharactersLength);
        }

        protected int compareStrings(String string1, String string2) {

            int minLength = Math.min(string1.length(), string2.length());

            for(int i = 0; i < minLength; i++) {
                if (string1.charAt(i) < string2.charAt(i)) {
                    return -1;
                } else if (string1.charAt(i) > string2.charAt(i)) {
                    return 1;
                }
            }

            return string1.length() - string2.length();
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

                // Characters field is only filled if there is more than one character to be stored
                if (digit != key.length()) {
                    // No risk of StringIndexOutOfBoundsException because digit 0 is always the root (that has no value)
                    node.characters = key.substring(digit - 1, key.length());
                }

                node.value = value;
                node.size = 1;

                return node;
            }

            if (isNewKey) {
                node.size = node.size + 1;
            }

            if (node.characters != null) {
                int nodeCharactersLength = node.characters.length();

                // If the key already exists, there is no need to break the node into smaller components
                if (!isNewKey && digit - 1 + nodeCharactersLength == key.length()) {
                    node.value = value;
                    return node;
                }

                Node parentNode = new Node();
                parentNode.size = 2;

                Node currentNode = parentNode;
                int maxLength = Math.max(nodeCharactersLength, key.length() - digit + 1);

                // Key is new
                for (int index = 1; index < maxLength; index++) {

                    if (index < nodeCharactersLength && digit - 1 + index < key.length()) {
                        char existingNodeCharacter = node.characters.charAt(index);
                        char newNodeCurrentCharacter = key.charAt(digit - 1 + index);

                        if (existingNodeCharacter == newNodeCurrentCharacter) {
                            Node newNode = new Node();
                            newNode.size = 2;

                            currentNode.next[existingNodeCharacter] = newNode;
                            currentNode = newNode;
                        } else {
                            splitNodes(node, currentNode, key, value, index, digit - 1);
                            return parentNode;
                        }
                    } else {
                        splitNodes(node, currentNode, key, value, index, digit - 1);
                        return parentNode;
                    }
                }
            } else {
                if (digit == key.length()) {
                    node.value = value;
                    return node;
                }

                char nextChar = key.charAt(digit);
                node.next[nextChar] = put(node.next[nextChar], key, value, digit + 1, isNewKey);
            }

            return node;
        }

        private void splitNodes(Node originalNode, Node splitParentNode, String key, Value value, int index, int digit) {

            // The first child node will have as characters the substring before the split point
            if (index < originalNode.characters.length()) {
                String remainingCharacters = originalNode.characters.substring(index, originalNode.characters.length());
                Node firstChild = new Node();

                if (remainingCharacters.length() > 1) {
                    firstChild.characters = remainingCharacters;
                }

                firstChild.size = 1;
                firstChild.value = originalNode.value;

                char firstChildIndexChar = originalNode.characters.charAt(index);
                splitParentNode.next[firstChildIndexChar] = firstChild;
            } else {
                if (originalNode.value != null) {
                    splitParentNode.value = originalNode.value;
                }
            }

            // The other node will have as characters the substring on and after the split point
            if (index + digit < key.length()) {
                String secondChildKey = key.substring(index + digit, key.length());
                Node secondChild = new Node();

                if (secondChildKey.length() > 1) {
                    secondChild.characters = secondChildKey;
                }

                secondChild.size = 1;
                secondChild.value = value;

                char secondChildIndexChar = secondChildKey.charAt(0);
                splitParentNode.next[secondChildIndexChar] = secondChild;
            } else {
                splitParentNode.value = value;
            }
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

            int nodeCharactersLength = 1;

            if (node.characters != null) {
                nodeCharactersLength = node.characters.length();
            }

            if (digit - 1 + nodeCharactersLength == key.length()) {
                node.value = null;
            } else {
                char nextChar = key.charAt(digit - 1 + nodeCharactersLength);
                node.next[nextChar] = delete(node.next[nextChar], key, digit + nodeCharactersLength);
            }

            if (node.value != null) {
                return node;
            }

            if (node.size == 0) {
                return null;
            }

            // Merge nodes on the way back to avoid external one-way branching
            // If we got here, the node key has a null value
            if (node.size == 1 && digit != 0) {
                if (node.characters == null) {
                    node.characters = String.valueOf(key.charAt(digit - 1));
                }

                for (char nextChar = 0; nextChar < R; nextChar++) {
                    if (node.next[nextChar] != null) {
                        if (node.next[nextChar].characters != null) {
                            node.characters = node.characters + node.next[nextChar].characters;
                        } else {
                            node.characters = node.characters + nextChar;
                        }

                        node.value = node.next[nextChar].value;
                        node.next[nextChar] = null;
                        break;
                    }
                }
            }

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

            NodeWithInformation nodeWithInformation = new NodeWithInformation(root, new StringBuilder());
            NodeWithInformation nodeWithPrefix = getNodeWithPrefix(nodeWithInformation, prefix, 0);

            if (nodeWithPrefix == null) {
                return keysWithPrefix;
            }

            String realPrefix = nodeWithPrefix.prefix.toString();

            collect(nodeWithPrefix.node, new StringBuilder(realPrefix), keysWithPrefix);

            return keysWithPrefix;
        }

        private NodeWithInformation getNodeWithPrefix(NodeWithInformation nodeWithInformation, String key, int digit) {
            if (nodeWithInformation.node == null) {
                return null;
            }

            if (digit == key.length() && nodeWithInformation.node.characters == null) {
                return nodeWithInformation;
            }

            int nodeCharactersLength = 1;
            if (nodeWithInformation.node.characters != null) {
                nodeCharactersLength = nodeWithInformation.node.characters.length() - 1;

                int substringRightIndex = digit + nodeCharactersLength;
                if (substringRightIndex > key.length()) {
                    substringRightIndex = key.length();
                }

                String currentKeyCharacters = key.substring(digit - 1, substringRightIndex);
                String nodeCharactersSubstring = nodeWithInformation.node.characters.substring(0, currentKeyCharacters.length());

                int compare = compareStrings(currentKeyCharacters, nodeCharactersSubstring);

                if (compare == 0) {
                    if (digit + nodeCharactersLength == key.length()) {
                        return nodeWithInformation;
                    } // If characters match but this is not the end of the key, continue below
                } else {
                    return null;
                }
            }

            char nextChar;
            if (nodeWithInformation.node.characters != null) {
                if (digit + nodeCharactersLength < key.length()) {
                    nextChar = key.charAt(digit + nodeCharactersLength);
                } else {
                    return nodeWithInformation;
                }
            } else {
                nextChar = key.charAt(digit);
            }

            Node nextNode = nodeWithInformation.node.next[nextChar];
            nodeWithInformation.node = nextNode;

            if (nextNode != null ) {
                if (nextNode.characters != null) {
                    nodeWithInformation.prefix.append(nextNode.characters);
                } else {
                    nodeWithInformation.prefix.append(nextChar);
                }
            }

            return getNodeWithPrefix(nodeWithInformation, key, digit + nodeCharactersLength);
        }

        private void collect(Node node, StringBuilder prefix, Queue<String> queue) {
            if (node == null) {
                return;
            }

            if (node.value != null) {
                queue.enqueue(prefix.toString());
            }

            for (char nextChar = 0; nextChar < R; nextChar++) {

                if (node.next[nextChar] != null) {

                    String nextNodeCharacters;

                    if (node.next[nextChar].characters != null) {
                        nextNodeCharacters = node.next[nextChar].characters;
                    } else {
                        nextNodeCharacters = String.valueOf(nextChar);
                    }

                    int nextNodeCharactersLength = nextNodeCharacters.length();

                    prefix.append(nextNodeCharacters);
                    collect(node.next[nextChar], prefix, queue);
                    prefix.delete(prefix.length() - nextNodeCharactersLength, prefix.length());
                }
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

            if (digit < pattern.length()) {
                char nextCharInPattern = pattern.charAt(digit);

                for (char nextChar = 0; nextChar < R; nextChar++) {
                    if (nextCharInPattern == '.' || nextCharInPattern == nextChar) {

                        if (node.next[nextChar] != null) {

                            String nextNodeCharacters;

                            if (node.next[nextChar].characters != null) {
                                nextNodeCharacters = node.next[nextChar].characters;
                            } else {
                                nextNodeCharacters = String.valueOf(nextChar);
                            }

                            int nextNodeCharactersLength = nextNodeCharacters.length();

                            prefix.append(nextNodeCharacters);
                            collect(node.next[nextChar], prefix, pattern, queue);
                            prefix.delete(prefix.length() - nextNodeCharactersLength, prefix.length());
                        }
                    }
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

            if (node.next[nextChar] != null) {
                String nextNodeCharacters;

                if (node.next[nextChar].characters != null) {
                    nextNodeCharacters = node.next[nextChar].characters;
                } else {
                    nextNodeCharacters = String.valueOf(nextChar);
                }

                int nextNodeCharactersLength = nextNodeCharacters.length();

                int substringRightIndex = digit + nextNodeCharactersLength;
                if (substringRightIndex > query.length()) {
                    return length;
                }

                String currentPrefix = query.substring(digit, substringRightIndex);
                int compare = compareStrings(currentPrefix, nextNodeCharacters);

                if (compare == 0 && digit + nextNodeCharactersLength <= query.length()) {
                    return search(node.next[nextChar], query, digit + nextNodeCharactersLength, length);
                }
            }

            return length;
        }

        // Ordered methods

         //Returns the highest key in the symbol table smaller than or equal to key.
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

                    String nextNodeCharacters;

                    if (node.next[nextChar].characters != null) {
                        nextNodeCharacters = node.next[nextChar].characters;
                    } else {
                        nextNodeCharacters = String.valueOf(nextChar);
                    }

                    int nextNodeCharactersLength = nextNodeCharacters.length();

                    lastKeyFound = floor(node.next[nextChar], key, digit + nextNodeCharactersLength,
                            prefix.append(nextNodeCharacters), lastKeyFound, mustBeEqualDigit);

                    if (lastKeyFound != null) {
                        return lastKeyFound;
                    }
                    prefix.delete(prefix.length() - nextNodeCharactersLength, prefix.length());
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

                    String nextNodeCharacters;

                    if (node.next[nextChar].characters != null) {
                        nextNodeCharacters = node.next[nextChar].characters;
                    } else {
                        nextNodeCharacters = String.valueOf(nextChar);
                    }

                    int nextNodeCharactersLength = nextNodeCharacters.length();

                    String keyFound = ceiling(node.next[nextChar], key, digit + nextNodeCharactersLength,
                            prefix.append(nextNodeCharacters), mustBeEqualDigit);

                    if (keyFound != null) {
                        return keyFound;
                    }
                    prefix.delete(prefix.length() - nextNodeCharactersLength, prefix.length());
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

                    String nextNodeCharacters;

                    if (node.next[nextChar].characters != null) {
                        nextNodeCharacters = node.next[nextChar].characters;
                    } else {
                        nextNodeCharacters = String.valueOf(nextChar);
                    }

                    if (index - size(node.next[nextChar]) < 0) {
                        return select(node.next[nextChar], index, prefix.append(nextNodeCharacters));
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

            return rank(root, key, 0, 0, new StringBuilder());
        }

        private int rank(Node node, String key, int digit, int size, StringBuilder currentPrefix) {
            if (node == null) {
                return size;
            }

            // If a prefix key or a lower key was found, add 1 to rank
            if (node.value != null) {
                if (currentPrefix.toString().compareTo(key) < 0) {
                    size++;
                } else {
                    return size;
                }
            }

            if (digit >= key.length()) {
                return size;
            }

            char currentChar = key.charAt(digit);

            for (char nextChar = 0; nextChar < currentChar; nextChar++) {
                size += size(node.next[nextChar]);
            }

            if (node.next[currentChar] == null) {
                return size;
            }

            String nextNodeCharacters;

            if (node.next[currentChar].characters != null) {
                nextNodeCharacters = node.next[currentChar].characters;
            } else {
                nextNodeCharacters = String.valueOf(currentChar);
            }

            int nextNodeCharactersLength = nextNodeCharacters.length();

            currentPrefix.append(nextNodeCharacters);
            return rank(node.next[currentChar], key, digit + nextNodeCharactersLength, size, currentPrefix);
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
                    if (node.next[nextChar].characters != null) {
                        prefix.append(node.next[nextChar].characters);
                    } else {
                        prefix.append(nextChar);
                    }

                    return min(node.next[nextChar], prefix);
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
                    if (node.next[nextChar].characters != null) {
                        prefix.append(node.next[nextChar].characters);
                    } else {
                        prefix.append(nextChar);
                    }

                    return max(node.next[nextChar], prefix);
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

    public static class TernarySearchTrieNoExternalOneWayBranching<Value> {

        protected int size;
        protected Node root;

        protected class Node {
            String characters;
            Value value;
            int size;

            Node left;
            Node middle;
            Node right;
        }

        private class NodeWithInformation {
            private Node node;
            private StringBuilder prefix;

            NodeWithInformation(Node node, StringBuilder prefix) {
                this.node = node;
                this.prefix = prefix;
            }
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

        private Node get(Node node, String key, int digit) {
            if (node == null) {
                return null;
            }

            int nodeCharactersLength = node.characters.length();

            int substringRightIndex = digit + nodeCharactersLength;
            if (substringRightIndex > key.length()) {
                substringRightIndex = key.length();
            }

            String currentKeyCharacters = key.substring(digit, substringRightIndex);
            int compare = compareStrings(currentKeyCharacters, node.characters);

            if (compare < 0) {
                return get(node.left, key, digit);
            } else if (compare > 0) {
                return get(node.right, key, digit);
            } else if (digit + nodeCharactersLength < key.length()) {
                return get(node.middle, key, digit + nodeCharactersLength);
            } else {
                return node;
            }
        }

        protected int compareStrings(String string1, String string2) {

            int minLength = Math.min(string1.length(), string2.length());

            for(int i = 0; i < minLength; i++) {
                if (string1.charAt(i) < string2.charAt(i)) {
                    return -1;
                } else if (string1.charAt(i) > string2.charAt(i)) {
                    return 1;
                }
            }

            return string1.length() - string2.length();
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
                size++;
            }

            root = put(root, key, value, 0, isNewKey);
        }

        private Node put(Node node, String key, Value value, int digit, boolean isNewKey) {

            if (node == null) {
                node = new Node();
                node.characters = key.substring(digit, key.length());
                node.value = value;
                node.size = 1;

                return node;
            }

            char currentChar = key.charAt(digit);

            if (currentChar < node.characters.charAt(0)) {
                node.left = put(node.left, key, value, digit, isNewKey);
            } else if (currentChar > node.characters.charAt(0)) {
                node.right = put(node.right, key, value, digit, isNewKey);
            } else {

                if (node.characters.length() > 1) {
                    int nodeCharactersLength = node.characters.length();

                    // If the key already exists, there is no need to break the node into smaller components
                    if (!isNewKey && digit + nodeCharactersLength == key.length()) {
                        node.value = value;
                        return node;
                    }

                    Node parentNode = new Node();
                    parentNode.left = node.left;
                    parentNode.right = node.right;
                    parentNode.characters = String.valueOf(node.characters.charAt(0));
                    parentNode.size = 2;

                    Node currentNode = parentNode;
                    int maxLength = Math.max(nodeCharactersLength, key.length() - digit);

                    // Key is new
                    for (int index = 1; index < maxLength; index++) {

                        if (index < nodeCharactersLength && digit + index < key.length()) {
                            char existingNodeCharacter = node.characters.charAt(index);
                            char newNodeCurrentCharacter = key.charAt(digit + index);

                            if (existingNodeCharacter == newNodeCurrentCharacter) {
                                Node newNode = new Node();
                                newNode.characters = String.valueOf(existingNodeCharacter);
                                newNode.size = 2;

                                currentNode.middle = newNode;
                                currentNode = newNode;
                            } else {
                                boolean isNewNodeLeftChild = newNodeCurrentCharacter < existingNodeCharacter;
                                splitNodes(node, currentNode, key, value, index, digit, isNewNodeLeftChild);
                                return parentNode;
                            }
                        } else {
                            boolean isNewNodeLeftChild = index < key.length();
                            splitNodes(node, currentNode, key, value, index, digit, isNewNodeLeftChild);
                            return parentNode;
                        }
                    }
                }

                if (digit + node.characters.length() < key.length()) {
                    if (node.characters.length() == 1) {
                        node.middle = put(node.middle, key, value, digit + 1, isNewKey);
                    }
                } else {
                    node.value = value;
                }

                if (isNewKey) {
                    node.size = node.size + 1;
                }
            }

            return node;
        }

        private void splitNodes(Node originalNode, Node splitParentNode, String key, Value value, int index, int digit,
                                boolean isNewNodeLeftChild) {

            // The current node will have as characters the substring before the split point
            if (index < originalNode.characters.length()) {
                String remainingCharacters = originalNode.characters.substring(index, originalNode.characters.length());
                Node middleChild = new Node();
                middleChild.characters = remainingCharacters;
                middleChild.size = 1;
                middleChild.value = originalNode.value;

                splitParentNode.middle = middleChild;
            } else {
                if (originalNode.value != null) {
                    splitParentNode.value = originalNode.value;
                }
            }

            // The other node will have as characters the substring on and after the split point
            if (index + digit < key.length()) {
                String otherChildKey = key.substring(index + digit, key.length());
                Node newChild = new Node();
                newChild.characters = otherChildKey;
                newChild.size = 1;
                newChild.value = value;

                if (splitParentNode.middle == null) {
                    splitParentNode.middle = newChild;
                } else if (isNewNodeLeftChild) {
                    splitParentNode.middle.left = newChild;
                } else {
                    splitParentNode.middle.right = newChild;
                }
            } else {
                splitParentNode.value = value;
            }
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

            int nodeCharactersLength = node.characters.length();

            int substringRightIndex = digit + nodeCharactersLength;
            if (substringRightIndex > key.length()) {
                substringRightIndex = key.length();
            }

            String currentKeyCharacters = key.substring(digit, substringRightIndex);
            int compare = compareStrings(currentKeyCharacters, node.characters);

            if (compare < 0) {
                node.left = delete(node.left, key, digit);
            } else if (compare > 0) {
                node.right = delete(node.right, key, digit);
            } else if (digit + nodeCharactersLength < key.length()) {
                node.size = node.size - 1;
                node.middle = delete(node.middle, key, digit + nodeCharactersLength);
            } else {
                node.size = node.size - 1;
                node.value = null;
            }

            if (node.size == 0) {
                if (node.left == null && node.right == null) {
                    return null;
                } else if (node.left == null) {
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

            // Merge nodes on the way back to avoid external one-way branching
            if (node.middle != null && node.size == 1 && node.value == null) {
                node.characters = node.characters + node.middle.characters;
                node.value = node.middle.value;
                node.middle = null;
            }

            return node;
        }

        public Iterable<String> keys() {
            Queue<String> keys = new Queue<>();
            collect(root, new StringBuilder(), keys);
            return keys;
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("Prefix cannot be null");
            }

            Queue<String> keysWithPrefix = new Queue<>();

            NodeWithInformation nodeWithInformation = new NodeWithInformation(root, new StringBuilder());
            NodeWithInformation nodeWithPrefix = getNodeWithPrefix(nodeWithInformation, prefix, 0);

            if (nodeWithPrefix == null || nodeWithPrefix.node == null) {
                return keysWithPrefix;
            }

            String realPrefix = nodeWithPrefix.prefix.toString();

            if (nodeWithPrefix.node.value != null) {
                keysWithPrefix.enqueue(realPrefix);
            }

            collect(nodeWithPrefix.node.middle, new StringBuilder(realPrefix), keysWithPrefix);
            return keysWithPrefix;
        }

        private NodeWithInformation getNodeWithPrefix(NodeWithInformation nodeWithInformation, String key, int digit) {
            if (nodeWithInformation.node == null) {
                return null;
            }

            int nodeCharactersLength = nodeWithInformation.node.characters.length();

            int substringRightIndex = digit + nodeCharactersLength;
            if (substringRightIndex > key.length()) {
                substringRightIndex = key.length();
            }

            String currentKeyCharacters = key.substring(digit, substringRightIndex);
            String nodeCharactersSubstring = nodeWithInformation.node.characters.substring(0, currentKeyCharacters.length());

            int compare = compareStrings(currentKeyCharacters, nodeCharactersSubstring);

            if (compare < 0) {
                nodeWithInformation.node = nodeWithInformation.node.left;
                return getNodeWithPrefix(nodeWithInformation, key, digit);
            } else if (compare > 0) {
                nodeWithInformation.node = nodeWithInformation.node.right;
                return getNodeWithPrefix(nodeWithInformation, key, digit);
            } else if (digit + nodeCharactersLength < key.length()) {
                nodeWithInformation.prefix.append(nodeWithInformation.node.characters);
                nodeWithInformation.node = nodeWithInformation.node.middle;

                return getNodeWithPrefix(nodeWithInformation, key,digit + nodeCharactersLength);
            } else {
                nodeWithInformation.prefix.append(nodeWithInformation.node.characters);
                return nodeWithInformation;
            }
        }

        private void collect(Node node, StringBuilder prefix, Queue<String> queue) {
            if (node == null) {
                return;
            }

            collect(node.left, prefix, queue);

            if (node.value != null) {
                queue.enqueue(prefix.toString() + node.characters);
            }

            collect(node.middle, prefix.append(node.characters), queue);

            int nodeCharactersLength = node.characters.length();
            prefix.delete(prefix.length() - nodeCharactersLength, prefix.length());

            collect(node.right, prefix, queue);
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

            int nodeCharactersLength = node.characters.length();

            int substringRightIndex = digit + nodeCharactersLength;
            if (substringRightIndex > pattern.length()) {
                substringRightIndex = pattern.length();
            }

            String currentPatternCharacters = pattern.substring(digit, substringRightIndex);
            int compare = compareToPattern(node.characters, currentPatternCharacters);

            boolean hasWildcard = currentPatternCharacters.contains(".");

            if (hasWildcard || compare > 0) {
                collect(node.left, prefix, pattern, queue);
            }
            if (compare == 0) {
                if (digit + nodeCharactersLength == pattern.length() && node.value != null) {
                    queue.enqueue(prefix.toString() + node.characters);
                } else if (digit + nodeCharactersLength < pattern.length()) {
                    collect(node.middle, prefix.append(node.characters), pattern, queue);
                    prefix.delete(prefix.length() - nodeCharactersLength, prefix.length());
                }
            }
            if (hasWildcard || compare < 0) {
                collect(node.right, prefix, pattern, queue);
            }
        }

        private int compareToPattern(String currentCharacters, String patternCharacters) {

            int minLength = Math.min(currentCharacters.length(), patternCharacters.length());

            for(int i = 0; i < minLength; i++) {
                if (patternCharacters.charAt(i) == '.') {
                    continue;
                }

                if (currentCharacters.charAt(i) < patternCharacters.charAt(i)) {
                    return -1;
                } else if (currentCharacters.charAt(i) > patternCharacters.charAt(i)) {
                    return 1;
                }
            }

            return currentCharacters.length() - patternCharacters.length();
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

            int nodeCharactersLength = node.characters.length();

            int substringRightIndex = digit + nodeCharactersLength;
            if (substringRightIndex > query.length()) {
                substringRightIndex = query.length();
            }

            String currentQueryCharacters = query.substring(digit, substringRightIndex);
            int compare = compareStrings(currentQueryCharacters, node.characters);

            if (compare < 0) {
                return search(node.left, query, digit, length);
            } else if (compare > 0) {
                return search(node.right, query, digit, length);
            } else {
                if (node.value != null) {
                    length = digit + nodeCharactersLength;
                }

                if (digit + nodeCharactersLength < query.length()) {
                    return search(node.middle, query, digit + nodeCharactersLength, length);
                } else {
                    return length;
                }
            }
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
                return lastKeyFound;
            }

            int nodeCharactersLength = node.characters.length();

            StringBuilder prefixWithCharacters = new StringBuilder(prefix).append(node.characters);

            String currentKeyCharacters;
            if (digit + nodeCharactersLength < key.length() && mustBeEqualDigit) {
                currentKeyCharacters = key.substring(digit, digit + nodeCharactersLength);
            } else {
                currentKeyCharacters = String.valueOf(Character.MAX_VALUE);
                mustBeEqualDigit = false;
            }

            int compare = currentKeyCharacters.compareTo(node.characters);

            if (compare < 0 && mustBeEqualDigit) {
                return floor(node.left, key, digit, prefix, lastKeyFound, true);
            } else if (!mustBeEqualDigit || compare >= 0) {
                // Optimization: if current prefix is higher than the search key, left is the only way to go
                if (prefixWithCharacters.toString().compareTo(key) > 0) {

                    if (node.left != null) {
                        return floor(node.left, key, digit, prefix, lastKeyFound, mustBeEqualDigit);
                    }
                    return lastKeyFound;
                }

                if (mustBeEqualDigit && compare > 0) {
                    mustBeEqualDigit = false;
                }

                // Check child nodes in the order: right, middle, current, left
                String rightKey = floor(node.right, key, digit, prefix, lastKeyFound, mustBeEqualDigit);
                if (rightKey != null) {
                    return rightKey;
                }

                String middleKey = floor(node.middle, key, digit + nodeCharactersLength, prefixWithCharacters,
                        null, mustBeEqualDigit);
                if (middleKey != null) {
                    return middleKey;
                }

                if (node.value != null && prefixWithCharacters.toString().compareTo(key) <= 0) {
                    return prefixWithCharacters.toString();
                }

                String leftKey = floor(node.left, key, digit, prefix, lastKeyFound, mustBeEqualDigit);
                if (leftKey != null) {
                    return leftKey;
                }
            }

            return null;
        }

        // Returns the smallest key in the symbol table greater than or equal to key.
        public String ceiling(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            return ceiling(root, key, 0, new StringBuilder(), null, true);
        }

        private String ceiling(Node node, String key, int digit, StringBuilder prefix, String lastKeyFound,
                               boolean mustBeEqualDigit) {
            if (node == null) {
                return lastKeyFound;
            }

            int nodeCharactersLength = node.characters.length();

            StringBuilder prefixWithCharacters = new StringBuilder(prefix).append(node.characters);

            String currentKeyCharacters;
            if (digit + nodeCharactersLength < key.length() && mustBeEqualDigit) {
                currentKeyCharacters = key.substring(digit, digit + nodeCharactersLength);
            } else {
                currentKeyCharacters = String.valueOf((char) 0);
                mustBeEqualDigit = false;
            }

            int compare = currentKeyCharacters.compareTo(node.characters);

            if (compare > 0 && mustBeEqualDigit) {
                return ceiling(node.right, key, digit, prefix, lastKeyFound, true);
            } else if (!mustBeEqualDigit || compare <= 0) {
                if (mustBeEqualDigit && compare < 0) {
                    mustBeEqualDigit = false;
                }

                // Check child nodes in the order: left, current, middle, right
                if (!mustBeEqualDigit) {
                    lastKeyFound = ceiling(node.left, key, digit, prefix, null, false);
                    if (lastKeyFound != null) {
                        return lastKeyFound;
                    }
                }

                if (node.value != null && prefixWithCharacters.toString().compareTo(key) >= 0) {
                    return prefixWithCharacters.toString();
                }

                String middleKey = ceiling(node.middle, key, digit + nodeCharactersLength, prefixWithCharacters,
                        null, mustBeEqualDigit);
                if (middleKey != null) {
                    return middleKey;
                }

                String rightKey = ceiling(node.right, key, digit, prefix, null, mustBeEqualDigit);
                if (rightKey != null) {
                    return rightKey;
                }
            }

            return null;
        }

        public String select(int index) {
            if (index < 0 || index >= size()) {
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
                index = index - leftSubtreeSize;

                if (node.value != null) {
                    if (index == 0) {
                        return prefix.append(node.characters).toString();
                    } else {
                        index--;
                    }
                }

                prefix.append(node.characters);
                return select(node.middle, index, prefix);
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

            int nodeCharactersLength = node.characters.length();

            int substringRightIndex = digit + nodeCharactersLength;
            if (substringRightIndex > key.length()) {
                substringRightIndex = key.length();
            }

            String currentKeyCharacters = key.substring(digit, substringRightIndex);
            int compare = compareStrings(currentKeyCharacters, node.characters);

            if (compare < 0) {
                return rank(node.left, key, digit, size);
            } else {
                if (compare > 0) {
                    if (node.value != null) {
                        size++;
                    }

                    return getTreeSize(node.left) + getTreeSize(node.middle) + rank(node.right, key, digit, size);
                } else if (digit + nodeCharactersLength < key.length()) {
                    // Is current key a prefix of the search key?
                    if (digit + nodeCharactersLength < key.length() && node.value != null) {
                        size++;
                    }

                    return getTreeSize(node.left) + rank(node.middle, key, digit + nodeCharactersLength, size);
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
            if (isEmpty()) {
                return null;
            }

            Node minNode = min(root);

            StringBuilder minKey = new StringBuilder();
            minKey.append(minNode.characters);

            while (minNode.value == null) {
                minNode = minNode.middle;

                while (minNode.left != null) {
                    minNode = minNode.left;
                }
                minKey.append(minNode.characters);
            }

            return minKey.toString();
        }

        protected Node min(Node node) {
            if (node.left == null) {
                return node;
            }

            return min(node.left);
        }

        public String max() {
            if (isEmpty()) {
                return null;
            }

            Node maxNode = max(root);

            StringBuilder maxKey = new StringBuilder();
            maxKey.append(maxNode.characters);

            // Verify if size is different than 1 to avoid getting max key prefixes instead of the max key
            while (maxNode.size != 1 || maxNode.value == null) {
                maxNode = maxNode.middle;

                while (maxNode.right != null) {
                    maxNode = maxNode.right;
                }
                maxKey.append(maxNode.characters);
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
        protected Node deleteMin(Node node) {
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

    public static void main(String[] args) {
        Exercise11_ExternalOneWayBranching externalOneWayBranching = new Exercise11_ExternalOneWayBranching();
        externalOneWayBranching.trieTests();
        externalOneWayBranching.tstTests();
    }

    private void trieTests() {
        StdOut.println("********Trie tests********");
        TrieNoExternalOneWayBranching<Integer> trieNoExternalOneWayBranching = new TrieNoExternalOneWayBranching<>();

        // Put tests
        trieNoExternalOneWayBranching.put("Rene", 0);
        trieNoExternalOneWayBranching.put("Re", 1);
        trieNoExternalOneWayBranching.put("Re", 10);
        trieNoExternalOneWayBranching.put("Algorithms", 2);
        trieNoExternalOneWayBranching.put("Algo", 3);
        trieNoExternalOneWayBranching.put("Algor", 4);
        trieNoExternalOneWayBranching.put("Tree", 5);
        trieNoExternalOneWayBranching.put("Trie", 6);
        trieNoExternalOneWayBranching.put("TST", 7);
        trieNoExternalOneWayBranching.put("Trie123", 8);
        trieNoExternalOneWayBranching.put("Z-Function", 9);

        // Get tests
        StdOut.println("Get Re: " + trieNoExternalOneWayBranching.get("Re"));
        StdOut.println("Expected: 10");
        StdOut.println("Get Algorithms: " + trieNoExternalOneWayBranching.get("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Get Trie123: " + trieNoExternalOneWayBranching.get("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Get Algori: " + trieNoExternalOneWayBranching.get("Algori"));
        StdOut.println("Expected: null");
        StdOut.println("Get Zooom: " + trieNoExternalOneWayBranching.get("Zooom"));
        StdOut.println("Expected: null");

        // Keys() test
        StdOut.println("\nAll keys");
        for(String key : trieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Keys with prefix tests
        StdOut.println("\nKeys with prefix Alg");
        StringJoiner keysWithPrefix1 = new StringJoiner(" ");

        for(String key : trieNoExternalOneWayBranching.keysWithPrefix("Alg")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println(keysWithPrefix1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with prefix T");
        StringJoiner keysWithPrefix2 = new StringJoiner(" ");

        for(String key : trieNoExternalOneWayBranching.keysWithPrefix("T")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println(keysWithPrefix2.toString());
        StdOut.println("Expected: TST Tree Trie Trie123");

        StdOut.println("\nKeys with prefix R");
        StringJoiner keysWithPrefix3 = new StringJoiner(" ");

        for(String key : trieNoExternalOneWayBranching.keysWithPrefix("R")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println(keysWithPrefix3.toString());
        StdOut.println("Expected: Re Rene");

        StdOut.println("\nKeys with prefix ZZZ");
        StringJoiner keysWithPrefix4 = new StringJoiner(" ");

        for(String key : trieNoExternalOneWayBranching.keysWithPrefix("ZZZ")) {
            keysWithPrefix4.add(key);
        }
        StdOut.println(keysWithPrefix4.toString());
        StdOut.println("Expected: ");

        // Keys that match tests
        StdOut.println("\nKeys that match Alg..");
        StringJoiner keysThatMatch1 = new StringJoiner("Alg..");

        for(String key : trieNoExternalOneWayBranching.keysThatMatch("Alg..")) {
            keysThatMatch1.add(key);
        }
        StdOut.println(keysThatMatch1.toString());
        StdOut.println("Expected: Algor");

        StdOut.println("\nKeys that match Re");
        StringJoiner keysThatMatch2 = new StringJoiner(" ");

        for(String key : trieNoExternalOneWayBranching.keysThatMatch("Re")) {
            keysThatMatch2.add(key);
        }
        StdOut.println(keysThatMatch2.toString());
        StdOut.println("Expected: Re");

        StdOut.println("\nKeys that match Tr.e");
        StringJoiner keysThatMatch3 = new StringJoiner(" ");

        for(String key : trieNoExternalOneWayBranching.keysThatMatch("Tr.e")) {
            keysThatMatch3.add(key);
        }
        StdOut.println(keysThatMatch3.toString());
        StdOut.println("Expected: Tree Trie");

        // Longest-prefix-of tests
        StdOut.println("\nLongest prefix of Re: " + trieNoExternalOneWayBranching.longestPrefixOf("Re"));
        StdOut.println("Expected: Re");

        StdOut.println("Longest prefix of Algori: " + trieNoExternalOneWayBranching.longestPrefixOf("Algori"));
        StdOut.println("Expected: Algor");

        StdOut.println("Longest prefix of Trie12345: " + trieNoExternalOneWayBranching.longestPrefixOf("Trie12345"));
        StdOut.println("Expected: Trie123");

        StdOut.println("Longest prefix of Zooom: " + trieNoExternalOneWayBranching.longestPrefixOf("Zooom"));
        StdOut.println("Expected: ");

        // Min tests
        StdOut.println("\nMin key: " + trieNoExternalOneWayBranching.min());
        StdOut.println("Expected: Algo");

        // Max tests
        StdOut.println("\nMax key: " + trieNoExternalOneWayBranching.max());
        StdOut.println("Expected: Z-Function");

        // Delete min and delete max tests
        trieNoExternalOneWayBranching.put("ABCKey", 11);
        trieNoExternalOneWayBranching.put("ZKey", 12);

        StdOut.println("\nKeys after ABCKey and ZKey insert: ");
        for(String key : trieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        trieNoExternalOneWayBranching.deleteMin();

        StdOut.println("\nKeys after deleteMin: ");
        for(String key : trieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ABCKey deleted");

        trieNoExternalOneWayBranching.deleteMax();

        StdOut.println("\nKeys after deleteMax: ");
        for(String key : trieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ZKey deleted");

        // Floor tests
        StdOut.println("\nFloor of Re: " + trieNoExternalOneWayBranching.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + trieNoExternalOneWayBranching.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + trieNoExternalOneWayBranching.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + trieNoExternalOneWayBranching.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + trieNoExternalOneWayBranching.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + trieNoExternalOneWayBranching.floor("Zoom"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Floor of TAB: " + trieNoExternalOneWayBranching.floor("TAB"));
        StdOut.println("Expected: Rene");

        // Ceiling tests
        StdOut.println("\nCeiling of Re: " + trieNoExternalOneWayBranching.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + trieNoExternalOneWayBranching.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + trieNoExternalOneWayBranching.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + trieNoExternalOneWayBranching.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + trieNoExternalOneWayBranching.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + trieNoExternalOneWayBranching.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Tro: " + trieNoExternalOneWayBranching.ceiling("Tro"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Ceiling of Ruby: " + trieNoExternalOneWayBranching.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        // Select tests
        StdOut.println("\nSelect 0: " + trieNoExternalOneWayBranching.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + trieNoExternalOneWayBranching.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + trieNoExternalOneWayBranching.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + trieNoExternalOneWayBranching.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + trieNoExternalOneWayBranching.select(8));
        StdOut.println("Expected: Trie123");
        StdOut.println("Select 9: " + trieNoExternalOneWayBranching.select(9));
        StdOut.println("Expected: Z-Function");

        // Rank tests
        StdOut.println("\nRank of R: " + trieNoExternalOneWayBranching.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + trieNoExternalOneWayBranching.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + trieNoExternalOneWayBranching.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + trieNoExternalOneWayBranching.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + trieNoExternalOneWayBranching.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + trieNoExternalOneWayBranching.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + trieNoExternalOneWayBranching.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + trieNoExternalOneWayBranching.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zzoom: " + trieNoExternalOneWayBranching.rank("Zzoom"));
        StdOut.println("Expected: 10");

        // Delete tests
        trieNoExternalOneWayBranching.delete("Z-Function");

        StdOut.println("\nKeys() after deleting Z-Function key");
        for(String key : trieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        trieNoExternalOneWayBranching.delete("Rene");

        StdOut.println("\nKeys() after deleting Rene key");
        for(String key : trieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Note that "Re" node is now a composite node

        trieNoExternalOneWayBranching.delete("Re");

        StdOut.println("\nKeys() after deleting Re key");
        for(String key : trieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }
    }

    private void tstTests() {
        StdOut.println("\n********Ternary Search Trie tests********");
        TernarySearchTrieNoExternalOneWayBranching<Integer> ternarySearchTrieNoExternalOneWayBranching =
                new TernarySearchTrieNoExternalOneWayBranching<>();

        // Put tests
        ternarySearchTrieNoExternalOneWayBranching.put("Rene", 0);
        ternarySearchTrieNoExternalOneWayBranching.put("Re", 1);
        ternarySearchTrieNoExternalOneWayBranching.put("Re", 10);
        ternarySearchTrieNoExternalOneWayBranching.put("Algorithms", 2);
        ternarySearchTrieNoExternalOneWayBranching.put("Algo", 3);
        ternarySearchTrieNoExternalOneWayBranching.put("Algor", 4);
        ternarySearchTrieNoExternalOneWayBranching.put("Tree", 5);
        ternarySearchTrieNoExternalOneWayBranching.put("Trie", 6);
        ternarySearchTrieNoExternalOneWayBranching.put("TST", 7);
        ternarySearchTrieNoExternalOneWayBranching.put("Trie123", 8);
        ternarySearchTrieNoExternalOneWayBranching.put("Z-Function", 9);

        // Get tests
        StdOut.println("Get Re: " + ternarySearchTrieNoExternalOneWayBranching.get("Re"));
        StdOut.println("Expected: 10");
        StdOut.println("Get Algorithms: " + ternarySearchTrieNoExternalOneWayBranching.get("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Get Trie123: " + ternarySearchTrieNoExternalOneWayBranching.get("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Get Algori: " + ternarySearchTrieNoExternalOneWayBranching.get("Algori"));
        StdOut.println("Expected: null");
        StdOut.println("Get Zooom: " + ternarySearchTrieNoExternalOneWayBranching.get("Zooom"));
        StdOut.println("Expected: null");

        // Keys() test
        StdOut.println("\nAll keys");
        for(String key : ternarySearchTrieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Keys with prefix tests
        StdOut.println("\nKeys with prefix Alg");
        StringJoiner keysWithPrefix1 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoExternalOneWayBranching.keysWithPrefix("Alg")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println(keysWithPrefix1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with prefix T");
        StringJoiner keysWithPrefix2 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoExternalOneWayBranching.keysWithPrefix("T")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println(keysWithPrefix2.toString());
        StdOut.println("Expected: TST Tree Trie Trie123");

        StdOut.println("\nKeys with prefix R");
        StringJoiner keysWithPrefix3 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoExternalOneWayBranching.keysWithPrefix("R")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println(keysWithPrefix3.toString());
        StdOut.println("Expected: Re Rene");

        StdOut.println("\nKeys with prefix ZZZ");
        StringJoiner keysWithPrefix4 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoExternalOneWayBranching.keysWithPrefix("ZZZ")) {
            keysWithPrefix4.add(key);
        }
        StdOut.println(keysWithPrefix4.toString());
        StdOut.println("Expected: ");

        // Keys that match tests
        StdOut.println("\nKeys that match Alg..");
        StringJoiner keysThatMatch1 = new StringJoiner("Alg..");

        for(String key : ternarySearchTrieNoExternalOneWayBranching.keysThatMatch("Alg..")) {
            keysThatMatch1.add(key);
        }
        StdOut.println(keysThatMatch1.toString());
        StdOut.println("Expected: Algor");

        StdOut.println("\nKeys that match Re");
        StringJoiner keysThatMatch2 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoExternalOneWayBranching.keysThatMatch("Re")) {
            keysThatMatch2.add(key);
        }
        StdOut.println(keysThatMatch2.toString());
        StdOut.println("Expected: Re");

        StdOut.println("\nKeys that match Tr.e");
        StringJoiner keysThatMatch3 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoExternalOneWayBranching.keysThatMatch("Tr.e")) {
            keysThatMatch3.add(key);
        }
        StdOut.println(keysThatMatch3.toString());
        StdOut.println("Expected: Tree Trie");

        // Longest-prefix-of tests
        StdOut.println("\nLongest prefix of Re: " + ternarySearchTrieNoExternalOneWayBranching.longestPrefixOf("Re"));
        StdOut.println("Expected: Re");

        StdOut.println("Longest prefix of Algori: " + ternarySearchTrieNoExternalOneWayBranching.longestPrefixOf("Algori"));
        StdOut.println("Expected: Algor");

        StdOut.println("Longest prefix of Trie12345: " + ternarySearchTrieNoExternalOneWayBranching.longestPrefixOf("Trie12345"));
        StdOut.println("Expected: Trie123");

        StdOut.println("Longest prefix of Zooom: " + ternarySearchTrieNoExternalOneWayBranching.longestPrefixOf("Zooom"));
        StdOut.println("Expected: ");

        // Min tests
        StdOut.println("\nMin key: " + ternarySearchTrieNoExternalOneWayBranching.min());
        StdOut.println("Expected: Algo");

        // Max tests
        StdOut.println("\nMax key: " + ternarySearchTrieNoExternalOneWayBranching.max());
        StdOut.println("Expected: Z-Function");

        // Delete min and delete max tests
        ternarySearchTrieNoExternalOneWayBranching.put("ABCKey", 11);
        ternarySearchTrieNoExternalOneWayBranching.put("ZKey", 12);

        StdOut.println("\nKeys after ABCKey and ZKey insert: ");
        for(String key : ternarySearchTrieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        ternarySearchTrieNoExternalOneWayBranching.deleteMin();

        StdOut.println("\nKeys after deleteMin: ");
        for(String key : ternarySearchTrieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ABCKey deleted");

        ternarySearchTrieNoExternalOneWayBranching.deleteMax();

        StdOut.println("\nKeys after deleteMax: ");
        for(String key : ternarySearchTrieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ZKey deleted");

        // Floor tests
        StdOut.println("\nFloor of Re: " + ternarySearchTrieNoExternalOneWayBranching.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + ternarySearchTrieNoExternalOneWayBranching.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + ternarySearchTrieNoExternalOneWayBranching.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + ternarySearchTrieNoExternalOneWayBranching.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + ternarySearchTrieNoExternalOneWayBranching.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + ternarySearchTrieNoExternalOneWayBranching.floor("Zoom"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Floor of TAB: " + ternarySearchTrieNoExternalOneWayBranching.floor("TAB"));
        StdOut.println("Expected: Rene");

        // Ceiling tests
        StdOut.println("\nCeiling of Re: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Tro: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("Tro"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Ceiling of Ruby: " + ternarySearchTrieNoExternalOneWayBranching.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        // Select tests
        StdOut.println("\nSelect 0: " + ternarySearchTrieNoExternalOneWayBranching.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + ternarySearchTrieNoExternalOneWayBranching.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + ternarySearchTrieNoExternalOneWayBranching.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + ternarySearchTrieNoExternalOneWayBranching.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + ternarySearchTrieNoExternalOneWayBranching.select(8));
        StdOut.println("Expected: Trie123");
        StdOut.println("Select 9: " + ternarySearchTrieNoExternalOneWayBranching.select(9));
        StdOut.println("Expected: Z-Function");

        // Rank tests
        StdOut.println("\nRank of R: " + ternarySearchTrieNoExternalOneWayBranching.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + ternarySearchTrieNoExternalOneWayBranching.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + ternarySearchTrieNoExternalOneWayBranching.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + ternarySearchTrieNoExternalOneWayBranching.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + ternarySearchTrieNoExternalOneWayBranching.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + ternarySearchTrieNoExternalOneWayBranching.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + ternarySearchTrieNoExternalOneWayBranching.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + ternarySearchTrieNoExternalOneWayBranching.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zzoom: " + ternarySearchTrieNoExternalOneWayBranching.rank("Zzoom"));
        StdOut.println("Expected: 10");

        // Delete tests
        ternarySearchTrieNoExternalOneWayBranching.delete("Z-Function");

        StdOut.println("\nKeys() after deleting Z-Function key");
        for(String key : ternarySearchTrieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        ternarySearchTrieNoExternalOneWayBranching.delete("Rene");

        StdOut.println("\nKeys() after deleting Rene key");
        for(String key : ternarySearchTrieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Note that "Re" node is now a composite node

        ternarySearchTrieNoExternalOneWayBranching.delete("Re");

        StdOut.println("\nKeys() after deleting Re key");
        for(String key : ternarySearchTrieNoExternalOneWayBranching.keys()) {
            StdOut.println(key);
        }
    }

}
