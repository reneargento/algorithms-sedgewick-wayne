package chapter5.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 22/01/18.
 */
public class Exercise10_Size {

    @SuppressWarnings("unchecked")
    public static class TrieWithSize<Value> extends Trie<Value> {

        private NodeWithSize root = new NodeWithSize();

        private static class NodeWithSize extends Node {
            private int size;
            private NodeWithSize[] next = new NodeWithSize[R];
        }

        public int size() {
            return size(root);
        }

        private int size(NodeWithSize nodeWithSize) {
            if (nodeWithSize == null) {
                return 0;
            }

            return nodeWithSize.size;
        }

        public boolean contains(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            return get(key) != null;
        }

        @Override
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

        private Node get(NodeWithSize node, String key, int digit) {
            if (node == null) {
                return null;
            }

            if (digit == key.length()) {
                return node;
            }

            char nextChar = key.charAt(digit); // Use digitTh key char to identify subtrie.
            return get(node.next[nextChar], key, digit + 1);
        }

        @Override
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

        private NodeWithSize put(NodeWithSize node, String key, Value value, int digit, boolean isNewKey) {
            if (node == null) {
                node = new NodeWithSize();
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

        @Override
        public void delete(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            root = delete(root, key, 0);
        }

        private NodeWithSize delete(NodeWithSize node, String key, int digit) {
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

    public static class TernarySearchTrieWithSize<Value> extends TernarySearchTrie<Value> {

        private int size;
        private NodeWithSize root;

        public class NodeWithSize extends Node {
            private int size;

            private NodeWithSize left;
            private NodeWithSize middle;
            private NodeWithSize right;
        }

        public int size() {
            return size;
        }

        public boolean contains(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            return get(key) != null;
        }

        @Override
        public Value get(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (key.length() == 0) {
                throw new IllegalArgumentException("Key must have a positive length");
            }

            NodeWithSize node = get(root, key, 0);

            if (node == null) {
                return null;
            }
            return node.value;
        }

        private NodeWithSize get(NodeWithSize node, String key, int digit) {
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

        @Override
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

        private NodeWithSize put(NodeWithSize node, String key, Value value, int digit, boolean isNewKey) {
            char currentChar = key.charAt(digit);

            if (node == null) {
                node = new NodeWithSize();
                node.character = currentChar;
            }

            if (currentChar < node.character) {
                node.left = put(node.left, key, value, digit, isNewKey);
            } else if (currentChar > node.character) {
                node.right = put(node.right, key, value, digit, isNewKey);
            } else if (digit < key.length() - 1) {
                node.middle = put(node.middle, key, value, digit + 1, isNewKey);

                if (isNewKey) {
                    node.size = node.size + 1;
                }
            } else {
                node.value = value;

                if (isNewKey) {
                    node.size = node.size + 1;
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

        private NodeWithSize delete(NodeWithSize node, String key, int digit) {
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
                } else if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    NodeWithSize aux = node;
                    node = min(aux.right);
                    node.right = deleteMin(aux.right);
                    node.left = aux.left;
                }
            }

            return node;
        }

        private NodeWithSize min(NodeWithSize node) {
            if (node.left == null) {
                return node;
            }

            return min(node.left);
        }

        private NodeWithSize deleteMin(NodeWithSize node) {
            if (node.left == null) {
                return node.right;
            }

            node.left = deleteMin(node.left);
            return node;
        }
    }

    // Considering ' ' as root node when printing
    private void printNodeSizes(TrieWithSize.NodeWithSize currentNode, char currentChar) {
        if (currentNode == null) {
            return;
        }

        StdOut.println("Node character: " + currentChar + " Size: " + currentNode.size);

        for(char nextChar = 0; nextChar < TrieWithSize.R; nextChar++) {
            if (currentNode.next[nextChar] != null) {
                printNodeSizes(currentNode.next[nextChar], nextChar);
            }
        }
    }

    private void printNodeSizes(TernarySearchTrieWithSize.NodeWithSize currentNode) {
        if (currentNode == null) {
            return;
        }

        printNodeSizes(currentNode.left);

        StdOut.println("Node character: " + currentNode.character + " Size: " + currentNode.size);
        printNodeSizes(currentNode.middle);
        printNodeSizes(currentNode.right);
    }

    public static void main(String[] args) {
        Exercise10_Size exercise10_size = new Exercise10_Size();

        StdOut.println("********Trie tests********");
        TrieWithSize<Integer> trieWithSize = new TrieWithSize<>();

        trieWithSize.put("Rene", 0);
        trieWithSize.put("Re", 1);
        trieWithSize.put("Algorithms", 2);
        trieWithSize.put("Algo", 3);
        trieWithSize.put("Algor", 4);
        trieWithSize.put("Tree", 5);
        trieWithSize.put("Trie", 6);
        trieWithSize.put("TST", 7);

        StdOut.println("Size of Trie: " + trieWithSize.size());
        StdOut.println("Expected: 8");

        StdOut.println();
        exercise10_size.printNodeSizes(trieWithSize.root, ' ');

        StdOut.println("\nExpected:\n" +
                "Node character:   Size: 8\n" +
                "Node character: A Size: 3\n" +
                "Node character: l Size: 3\n" +
                "Node character: g Size: 3\n" +
                "Node character: o Size: 3\n" +
                "Node character: r Size: 2\n" +
                "Node character: i Size: 1\n" +
                "Node character: t Size: 1\n" +
                "Node character: h Size: 1\n" +
                "Node character: m Size: 1\n" +
                "Node character: s Size: 1\n" +
                "Node character: R Size: 2\n" +
                "Node character: e Size: 2\n" +
                "Node character: n Size: 1\n" +
                "Node character: e Size: 1\n" +
                "Node character: T Size: 3\n" +
                "Node character: S Size: 1\n" +
                "Node character: T Size: 1\n" +
                "Node character: r Size: 2\n" +
                "Node character: e Size: 1\n" +
                "Node character: e Size: 1\n" +
                "Node character: i Size: 1\n" +
                "Node character: e Size: 1");

        StdOut.println("\nDeleted Trie key");
        trieWithSize.delete("Trie");

        exercise10_size.printNodeSizes(trieWithSize.root, ' ');
        StdOut.println("Expected: no 'i' and 'e' nodes after 'Tr'");

        StdOut.println("\nSize of Trie: " + trieWithSize.size());
        StdOut.println("Expected: 7");

        StdOut.println("\nDeleted Re key");
        trieWithSize.delete("Re");

        exercise10_size.printNodeSizes(trieWithSize.root, ' ');
        StdOut.println("Expected: 'R' and 'e' with size 1");

        StdOut.println("\nSize of Trie: " + trieWithSize.size());
        StdOut.println("Expected: 6");

        StdOut.println("\nDeleted Algo key");
        trieWithSize.delete("Algo");

        exercise10_size.printNodeSizes(trieWithSize.root, ' ');
        StdOut.println("Expected: 'A', 'l', 'g', 'o' and 'r' nodes with size 2");

        StdOut.println("\nSize of Trie: " + trieWithSize.size());
        StdOut.println("Expected: 5");

        StdOut.println("\n********Ternary Search Trie tests********");
        TernarySearchTrieWithSize<Integer> tstWithSize = new TernarySearchTrieWithSize<>();

        tstWithSize.put("Rene", 0);
        tstWithSize.put("Re", 1);
        tstWithSize.put("Algorithms", 2);
        tstWithSize.put("Algo", 3);
        tstWithSize.put("Algor", 4);
        tstWithSize.put("Tree", 5);
        tstWithSize.put("Trie", 6);
        tstWithSize.put("TST", 7);

        StdOut.println("Size of TST: " + tstWithSize.size());
        StdOut.println("Expected: 8");

        StdOut.println();
        exercise10_size.printNodeSizes(tstWithSize.root);

        StdOut.println("\nExpected:\n" +
                "Node character: A Size: 3\n" +
                "Node character: l Size: 3\n" +
                "Node character: g Size: 3\n" +
                "Node character: o Size: 3\n" +
                "Node character: r Size: 2\n" +
                "Node character: i Size: 1\n" +
                "Node character: t Size: 1\n" +
                "Node character: h Size: 1\n" +
                "Node character: m Size: 1\n" +
                "Node character: s Size: 1\n" +
                "Node character: R Size: 2\n" +
                "Node character: e Size: 2\n" +
                "Node character: n Size: 1\n" +
                "Node character: e Size: 1\n" +
                "Node character: T Size: 3\n" +
                "Node character: S Size: 1\n" +
                "Node character: T Size: 1\n" +
                "Node character: r Size: 2\n" +
                "Node character: e Size: 1\n" +
                "Node character: e Size: 1\n" +
                "Node character: i Size: 1\n" +
                "Node character: e Size: 1");

        StdOut.println("\nDeleted Trie key");
        tstWithSize.delete("Trie");

        exercise10_size.printNodeSizes(tstWithSize.root);
        StdOut.println("Expected: no 'i' and 'e' nodes after 'Tr'");

        StdOut.println("\nSize of TST: " + tstWithSize.size());
        StdOut.println("Expected: 7");

        StdOut.println("\nDeleted Re key");
        tstWithSize.delete("Re");

        exercise10_size.printNodeSizes(tstWithSize.root);
        StdOut.println("Expected: 'R' and 'e' with size 1");

        StdOut.println("\nSize of TST: " + tstWithSize.size());
        StdOut.println("Expected: 6");

        StdOut.println("\nDeleted Algo key");
        tstWithSize.delete("Algo");

        exercise10_size.printNodeSizes(tstWithSize.root);
        StdOut.println("Expected: 'A', 'l', 'g', 'o' and 'r' nodes with size 2");

        StdOut.println("\nSize of TST: " + tstWithSize.size());
        StdOut.println("Expected: 5");
    }

}
