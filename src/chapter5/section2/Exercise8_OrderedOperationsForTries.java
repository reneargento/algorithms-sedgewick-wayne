package chapter5.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/01/18.
 */
public class Exercise8_OrderedOperationsForTries {

    public class TrieOrdered<Value> extends Trie<Value> {

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

    public class TernarySearchTrieOrdered<Value> extends TernarySearchTrie<Value> {

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

            StringBuilder prefixWithCharacter = new StringBuilder(prefix).append(node.character);

            char currentChar;
            if (digit < key.length() && mustBeEqualDigit) {
                currentChar = key.charAt(digit);
            } else {
                currentChar = Character.MAX_VALUE;
                mustBeEqualDigit = false;
            }

            if (currentChar < node.character && mustBeEqualDigit) {
                return floor(node.left, key, digit, prefix, lastKeyFound, true);
            } else if (!mustBeEqualDigit || currentChar >= node.character) {
                // Optimization: if current prefix is higher than the search key, left is the only way to go
                if (prefixWithCharacter.toString().compareTo(key) > 0) {

                    if (node.left != null) {
                        return floor(node.left, key, digit, prefix, lastKeyFound, mustBeEqualDigit);
                    }
                    return lastKeyFound;
                }

                if (mustBeEqualDigit && currentChar > node.character) {
                    mustBeEqualDigit = false;
                }

                // Check child nodes in the order: right, middle, current, left
                String rightKey = floor(node.right, key, digit, prefix, lastKeyFound, mustBeEqualDigit);
                if (rightKey != null) {
                    return rightKey;
                }

                String middleKey = floor(node.middle, key, digit + 1, prefixWithCharacter, null, mustBeEqualDigit);
                if (middleKey != null) {
                    return middleKey;
                }

                if (node.value != null && prefixWithCharacter.toString().compareTo(key) <= 0) {
                    return prefixWithCharacter.toString();
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

            if (contains(key)) {
                return key;
            }

            return ceiling(root, key, 0, new StringBuilder(), null, true);
        }

        private String ceiling(Node node, String key, int digit, StringBuilder prefix, String lastKeyFound,
                               boolean mustBeEqualDigit) {
            if (node == null) {
                return lastKeyFound;
            }

            StringBuilder prefixWithCharacter = new StringBuilder(prefix).append(node.character);

            char currentChar;
            if (digit < key.length() && mustBeEqualDigit) {
                currentChar = key.charAt(digit);
            } else {
                currentChar = 0;
                mustBeEqualDigit = false;
            }

            if (currentChar > node.character && mustBeEqualDigit) {
                return ceiling(node.right, key, digit, prefix, lastKeyFound, true);
            } else if (!mustBeEqualDigit || currentChar <= node.character) {
                if (mustBeEqualDigit && currentChar < node.character) {
                    mustBeEqualDigit = false;
                }

                // Check child nodes in the order: left, current, middle, right
                if (!mustBeEqualDigit) {
                    lastKeyFound = ceiling(node.left, key, digit, prefix, null, false);
                    if (lastKeyFound != null) {
                        return lastKeyFound;
                    }
                }

                if (node.value != null && prefixWithCharacter.toString().compareTo(key) >= 0) {
                    return prefixWithCharacter.toString();
                }

                String middleKey = ceiling(node.middle, key, digit + 1, prefixWithCharacter, null, mustBeEqualDigit);
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
                        return prefix.append(node.character).toString();
                    }
                    index--;
                }

                prefix.append(node.character);
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

            char currentChar = key.charAt(digit);

            if (currentChar < node.character) {
                return rank(node.left, key, digit, size);
            } else {
                if (currentChar > node.character) {
                    if (node.value != null) {
                        size++;
                    }

                    return getTreeSize(node.left) + getTreeSize(node.middle) + rank(node.right, key, digit, size);
                } else if (digit < key.length() - 1) {
                    // Is current key a prefix of the search key?
                    if (digit < key.length() - 1 && node.value != null) {
                        size++;
                    }

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
            if (isEmpty()) {
                return null;
            }

            Node minNode = min(root);

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
            if (isEmpty()) {
                return null;
            }

            Node maxNode = max(root);

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

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            String maxKey = max();
            delete(maxKey);
        }

    }

    public static void main(String[] args) {
        Exercise8_OrderedOperationsForTries orderedOperationsForTries = new Exercise8_OrderedOperationsForTries();
        orderedOperationsForTries.trieTests();
        orderedOperationsForTries.tstTests();
    }

    private void trieTests() {
        StdOut.println("********Trie tests********");
        TrieOrdered<Integer> trieOrdered = new TrieOrdered<>();

        trieOrdered.put("Rene", 0);
        trieOrdered.put("Re", 1);
        trieOrdered.put("Algorithms", 2);
        trieOrdered.put("Algo", 3);
        trieOrdered.put("Algor", 4);
        trieOrdered.put("Tree", 5);
        trieOrdered.put("Trie", 6);
        trieOrdered.put("TST", 7);
        trieOrdered.put("Trie123", 8);

        StdOut.println("Floor of Re: " + trieOrdered.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + trieOrdered.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + trieOrdered.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + trieOrdered.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + trieOrdered.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + trieOrdered.floor("Zoom"));
        StdOut.println("Expected: Trie123");
        StdOut.println("Floor of TAB: " + trieOrdered.floor("TAB"));
        StdOut.println("Expected: Rene");

        StdOut.println("\nCeiling of Re: " + trieOrdered.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + trieOrdered.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + trieOrdered.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + trieOrdered.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + trieOrdered.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + trieOrdered.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Ruby: " + trieOrdered.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        StdOut.println("\nSelect 0: " + trieOrdered.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + trieOrdered.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + trieOrdered.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + trieOrdered.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + trieOrdered.select(8));
        StdOut.println("Expected: Trie123");

        StdOut.println("\nRank of R: " + trieOrdered.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + trieOrdered.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + trieOrdered.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + trieOrdered.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + trieOrdered.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + trieOrdered.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + trieOrdered.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + trieOrdered.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zoom: " + trieOrdered.rank("Zoom"));
        StdOut.println("Expected: 9");

        StdOut.println("\nMin key: " + trieOrdered.min());
        StdOut.println("Expected: Algo");

        StdOut.println("\nMax key: " + trieOrdered.max());
        StdOut.println("Expected: Trie123");

        StdOut.println("\nKeys after deleteMin():");
        trieOrdered.deleteMin();

        for(String key : trieOrdered.keys()) {
            StdOut.println(key);
        }

        StdOut.println("\nExpected:\n" +
                "Algor\n" +
                "Algorithms\n" +
                "Re\n" +
                "Rene\n" +
                "TST\n" +
                "Tree\n" +
                "Trie\n" +
                "Trie123");

        StdOut.println("\nKeys after deleteMax():");
        trieOrdered.deleteMax();

        for(String key : trieOrdered.keys()) {
            StdOut.println(key);
        }

        StdOut.println("\nExpected:\n" +
                "Algor\n" +
                "Algorithms\n" +
                "Re\n" +
                "Rene\n" +
                "TST\n" +
                "Tree\n" +
                "Trie");
    }

    private void tstTests() {
        StdOut.println("\n********Ternary Search Trie tests********");
        TernarySearchTrieOrdered<Integer> ternarySearchTrieOrdered = new TernarySearchTrieOrdered<>();

        ternarySearchTrieOrdered.put("Rene", 0);
        ternarySearchTrieOrdered.put("Re", 1);
        ternarySearchTrieOrdered.put("Algorithms", 2);
        ternarySearchTrieOrdered.put("Algo", 3);
        ternarySearchTrieOrdered.put("Algor", 4);
        ternarySearchTrieOrdered.put("Tree", 5);
        ternarySearchTrieOrdered.put("Trie", 6);
        ternarySearchTrieOrdered.put("TST", 7);
        ternarySearchTrieOrdered.put("Trie123", 8);

        StdOut.println("Floor of Re: " + ternarySearchTrieOrdered.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + ternarySearchTrieOrdered.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + ternarySearchTrieOrdered.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + ternarySearchTrieOrdered.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + ternarySearchTrieOrdered.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + ternarySearchTrieOrdered.floor("Zoom"));
        StdOut.println("Expected: Trie123");
        StdOut.println("Floor of TAB: " + ternarySearchTrieOrdered.floor("TAB"));
        StdOut.println("Expected: Rene");

        StdOut.println("\nCeiling of Re: " + ternarySearchTrieOrdered.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + ternarySearchTrieOrdered.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + ternarySearchTrieOrdered.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + ternarySearchTrieOrdered.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + ternarySearchTrieOrdered.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + ternarySearchTrieOrdered.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Ruby: " + ternarySearchTrieOrdered.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        StdOut.println("\nSelect 0: " + ternarySearchTrieOrdered.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + ternarySearchTrieOrdered.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + ternarySearchTrieOrdered.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + ternarySearchTrieOrdered.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + ternarySearchTrieOrdered.select(8));
        StdOut.println("Expected: Trie123");

        StdOut.println("\nRank of R: " + ternarySearchTrieOrdered.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + ternarySearchTrieOrdered.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + ternarySearchTrieOrdered.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + ternarySearchTrieOrdered.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + ternarySearchTrieOrdered.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + ternarySearchTrieOrdered.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + ternarySearchTrieOrdered.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + ternarySearchTrieOrdered.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zoom: " + ternarySearchTrieOrdered.rank("Zoom"));
        StdOut.println("Expected: 9");

        StdOut.println("\nMin key: " + ternarySearchTrieOrdered.min());
        StdOut.println("Expected: Algo");

        StdOut.println("\nMax key: " + ternarySearchTrieOrdered.max());
        StdOut.println("Expected: Trie123");

        StdOut.println("\nKeys after deleteMin():");
        ternarySearchTrieOrdered.deleteMin();

        for(String key : ternarySearchTrieOrdered.keys()) {
            StdOut.println(key);
        }

        StdOut.println("\nExpected:\n" +
                "Algor\n" +
                "Algorithms\n" +
                "Re\n" +
                "Rene\n" +
                "TST\n" +
                "Tree\n" +
                "Trie\n" +
                "Trie123");

        StdOut.println("\nKeys after deleteMax():");
        ternarySearchTrieOrdered.deleteMax();

        for(String key : ternarySearchTrieOrdered.keys()) {
            StdOut.println(key);
        }

        StdOut.println("\nExpected:\n" +
                "Algor\n" +
                "Algorithms\n" +
                "Re\n" +
                "Rene\n" +
                "TST\n" +
                "Tree\n" +
                "Trie");
    }

}
