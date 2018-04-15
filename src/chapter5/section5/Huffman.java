package chapter5.section5;

import chapter2.section4.PriorityQueueResize;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by Rene Argento on 13/04/18.
 */
public class Huffman {

    // Huffman trie node
    private static class Node implements  Comparable<Node> {
        private char character; // unused for internal nodes
        private int frequency;  // unused for expand
        private final Node left;
        private final Node right;

        Node(char character, int frequency, Node left, Node right) {
            this.character = character;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

    private static final int R = 256; // ASCII alphabet

    public static void compress() {
        // Read input
        String string = BinaryStdIn.readString();
        char[] input = string.toCharArray();

        // Tabulate frequency counts
        int[] frequencies = new int[R];
        for (int i = 0; i < input.length; i++) {
            frequencies[input[i]]++;
        }

        // Build Huffman code trie
        Node root = buildTrie(frequencies);

        // Build code table
        String[] codeTable = new String[R];
        buildCode(codeTable, root, "");

        // Print trie for decoder
        writeTrie(root);

        // Print number of characters
        BinaryStdOut.write(input.length);

        // Use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {
            String codeword = codeTable[input[i]];

            for (int j = 0; j < codeword.length(); j++) {
                if (codeword.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                } else if (codeword.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                } else {
                    throw new IllegalStateException("Illegal state");
                }
            }
        }

        BinaryStdOut.close();
    }

    private static Node buildTrie(int[] frequencies) {
        PriorityQueueResize<Node> priorityQueue = new PriorityQueueResize<>(PriorityQueueResize.Orientation.MIN);

        for (char character = 0; character < R; character++) {
            if (frequencies[character] > 0) {
                priorityQueue.insert(new Node(character, frequencies[character], null, null));
            }
        }

        // Special case in case there is only one character with a nonzero frequency
        if (priorityQueue.size() == 1) {
            if (frequencies['\0'] == 0) {
                priorityQueue.insert(new Node('\0', 0, null, null));
            } else {
                priorityQueue.insert(new Node('\1', 0, null, null));
            }
        }

        while (priorityQueue.size() > 1) {
            // Merge two smallest trees
            Node tree1 = priorityQueue.deleteTop();
            Node tree2 = priorityQueue.deleteTop();

            Node parent = new Node('\0', tree1.frequency + tree2.frequency, tree1, tree2);
            priorityQueue.insert(parent);
        }

        return priorityQueue.deleteTop();
    }

    private static String[] buildCode(Node root) {
        // Make a lookup table from trie
        String[] codeTable = new String[R];
        buildCode(codeTable, root, "");
        return codeTable;
    }

    private static void buildCode(String[] codeTable, Node node, String string) {
        if (node.isLeaf()) {
            codeTable[node.character] = string;
            return;
        }

        buildCode(codeTable, node.left, string + '0');
        buildCode(codeTable, node.right, string + '1');
    }

    private static void writeTrie(Node node) {
        // Write bitstring-encoded trie
        if (node.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(node.character, 8);
            return;
        }

        BinaryStdOut.write(false);
        writeTrie(node.left);
        writeTrie(node.right);
    }

    public static void expand() {
        Node root = readTrie();
        int length = BinaryStdIn.readInt();

        for (int i = 0; i < length; i++) {
            // Expand ith codeword
            Node node = root;

            while (!node.isLeaf()) {
                if (BinaryStdIn.readBoolean()) {
                    node = node.right;
                } else {
                    node = node.left;
                }
            }

            BinaryStdOut.write(node.character, 8);
        }

        BinaryStdOut.close();
    }

    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();

        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), 0, null, null);
        } else {
            return new Node('\0', 0, readTrie(), readTrie());
        }
    }

}
