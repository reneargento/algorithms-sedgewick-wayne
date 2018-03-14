package chapter3.section2;

import chapter3.section1.BinarySearchSymbolTable;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 13/06/17.
 */
public class Exercise46_CrossoverToBinarySearchTrees {

    public static void main(String[] args) {
        new Exercise46_CrossoverToBinarySearchTrees().doExperiment();
    }

    private void doExperiment() {

        boolean tenTimesFasterNFound = false;
        boolean aHundredTimesFasterNFound = false;
        boolean aThousandTimesFasterNFound = false;

        for(int i = 1; true; i++) {

            double[] keys = new double[i];
            for(int j = 0; j < i; j++) {
                keys[j] = StdRandom.uniform();
            }

            //BST symbol table
            long bstStartTime = System.nanoTime();
            BinarySearchTree<Double, Double> binarySearchTree = new BinarySearchTree<>();

            for(int j = 0; j < i; j++) {
                binarySearchTree.put(keys[j], keys[j]);
            }
            long bstRunningTime = System.nanoTime() - bstStartTime;

            //Binary search symbol table
            long binarySearchSTStartTime = System.nanoTime();
            BinarySearchSymbolTable<Double, Double> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

            for(int j = 0; j < i; j++) {
                binarySearchSymbolTable.put(keys[j], keys[j]);
            }
            long binarySearchSTRunningTime = System.nanoTime() - binarySearchSTStartTime;

            if (binarySearchSTRunningTime == bstRunningTime) {
                continue;
            }

            if (binarySearchSTRunningTime >= bstRunningTime * 10
                    && !tenTimesFasterNFound) {
                StdOut.println("Value of N for which building a symbol table using a BST becomes 10 times faster than building a symbol table using a binary search symbol table: " + i);
                tenTimesFasterNFound = true;
            }
            if (binarySearchSTRunningTime >= bstRunningTime * 100
                    && !aHundredTimesFasterNFound) {
                StdOut.println("Value of N for which building a symbol table using a BST becomes 100 times faster than building a symbol table using a binary search symbol table: " + i);
                aHundredTimesFasterNFound = true;
            }
            if (binarySearchSTRunningTime >= bstRunningTime * 1000
                    && !aThousandTimesFasterNFound) {
                StdOut.println("Value of N for which building a symbol table using a BST becomes 1000 times faster than building a symbol table using a binary search symbol table: " + i);
                aThousandTimesFasterNFound = true;
            }

            if (aThousandTimesFasterNFound) {
                break;
            }
        }
    }
}
