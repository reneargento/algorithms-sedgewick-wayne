package chapter3.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rene Argento on 01/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise34_All23Trees {

    private static Map<Integer, Integer> differentTreeStructures = new HashMap<>();

    private Set<RedBlackBST<Integer, Integer>> generateTrees(int height, Set<RedBlackBST<Integer, Integer>> heightMinus1Trees,
                                                             Set<Integer> keysInTree) {
        Set<RedBlackBST<Integer, Integer>> generatedTrees = new HashSet<>();

        int nodesInLevel = (int) Math.pow(2, height);
        int structuresToGenerate = differentTreeStructures.get(height);

        int nodesToAdd;
        //Used to change the insertion order and generate different tree structures
        int insertionsBeforeSwitchToLeftSide = 1; //always 1
        int insertionsBeforeSwitchToRightSide = 1; //starts at 1
        int insertions;

        while (structuresToGenerate > 0) {

            for(RedBlackBST<Integer, Integer> previousTree : heightMinus1Trees) {

                RedBlackBST<Integer, Integer> currentTree = copyTree(previousTree);

                boolean addToLeftSubtree = true;
                nodesToAdd = nodesInLevel;
                insertions = 0;

                while (nodesToAdd > 0) {
                    addKeyToTree(currentTree, addToLeftSubtree, keysInTree);
                    nodesToAdd--;
                    insertions++;

                    if ((addToLeftSubtree && insertions == insertionsBeforeSwitchToRightSide)
                            || (!addToLeftSubtree && insertions == insertionsBeforeSwitchToLeftSide)) {
                        addToLeftSubtree = !addToLeftSubtree;
                        insertions = 0;
                    }
                }

                generatedTrees.add(currentTree);
                structuresToGenerate--;
                if (structuresToGenerate == 0) {
                    break;
                }

                insertionsBeforeSwitchToRightSide++;
            }
        }

        return generatedTrees;
    }

    private void addKeyToTree(RedBlackBST<Integer, Integer> currentTree, boolean addToLeftSubtree, Set<Integer> keysInTree) {
        int median = currentTree.select(currentTree.size() / 2);
        int lowerBound;
        int higherBound;

        if (addToLeftSubtree) {
            lowerBound = 0;
            higherBound = median;
        } else {
            lowerBound = median;
            higherBound = Integer.MAX_VALUE;
        }

        int randomKey = StdRandom.uniform(lowerBound, higherBound);

        while (keysInTree.contains(randomKey)) {
            randomKey = StdRandom.uniform(lowerBound, higherBound);
        }

        keysInTree.add(randomKey);
        currentTree.put(randomKey, randomKey);
    }

    private RedBlackBST<Integer, Integer> copyTree(RedBlackBST<Integer, Integer> tree) {
        RedBlackBST.Node root = tree.root;

        if (root == null) {
            return null;
        }

        Queue<RedBlackBST.Node> queue = new Queue<>();
        queue.enqueue(tree.root);

        RedBlackBST.Node newRoot = new RedBlackBST().new Node(root.key, root.value, root.size, root.color);
        Queue<RedBlackBST.Node> newTreeQueue = new Queue<>();
        newTreeQueue.enqueue(newRoot);

        while (!queue.isEmpty()) {
            RedBlackBST.Node current = queue.dequeue();
            RedBlackBST.Node currentNewTree = newTreeQueue.dequeue();

            if (current.left != null) {
                currentNewTree.left = new RedBlackBST().new Node(current.left.key, current.left.value,
                        current.left.size, current.left.color);

                queue.enqueue(current.left);
                newTreeQueue.enqueue(currentNewTree.left);
            }
            if (current.right != null) {
                currentNewTree.right = new RedBlackBST().new Node(current.right.key, current.right.value,
                        current.right.size, current.right.color);

                queue.enqueue(current.right);
                newTreeQueue.enqueue(currentNewTree.right);
            }
        }

        RedBlackBST<Integer, Integer> copyTree = new RedBlackBST<>();
        copyTree.root = newRoot;
        return copyTree;
    }

    public static void main(String[] args) {
        Exercise34_All23Trees all23Trees = new Exercise34_All23Trees();

        Set<RedBlackBST<Integer, Integer>> generatedTrees = new HashSet<>();
        Set<Integer> keysInTree = new HashSet<>();

        Set<RedBlackBST<Integer, Integer>> treesOfHeight1 = new HashSet<>();
        RedBlackBST<Integer, Integer> height1Tree = new RedBlackBST<>();
        for(int i = 0; i < 3; i++) {
            int randomKey = StdRandom.uniform(Integer.MAX_VALUE);
            height1Tree.put(randomKey, randomKey);
            keysInTree.add(randomKey);
        }

        treesOfHeight1.add(height1Tree);

        differentTreeStructures.put(2, 2);
        differentTreeStructures.put(3, 7);
        //According to http://algs4.cs.princeton.edu/errata/errata-printing8.php
        // there are 112 structurally different 2-3 trees of height 4
        differentTreeStructures.put(4, 112);

        //Height = 2
        Set<RedBlackBST<Integer, Integer>> treesOfHeight2 = all23Trees.generateTrees(2, treesOfHeight1, keysInTree);
        generatedTrees.addAll(treesOfHeight2);

        //Height = 3
        Set<RedBlackBST<Integer, Integer>> treesOfHeight3 = all23Trees.generateTrees(3, treesOfHeight2, keysInTree);
        generatedTrees.addAll(treesOfHeight3);

        //Height = 4
        Set<RedBlackBST<Integer, Integer>> treesOfHeight4 = all23Trees.generateTrees(4, treesOfHeight3, keysInTree);
        generatedTrees.addAll(treesOfHeight4);

        StdOut.println("Number of trees generated: " + generatedTrees.size());
    }

}
