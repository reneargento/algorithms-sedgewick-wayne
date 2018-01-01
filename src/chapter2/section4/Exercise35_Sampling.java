package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

/**
 * Created by Rene Argento on 27/03/17.
 */
public class Exercise35_Sampling {

    private class Node {
        double weight;
        double cumulativeWeight;
    }

    private Node[] nodes;
    private double sum = 0;

    private Exercise35_Sampling(double[] p) {

        nodes = new Node[p.length + 1];

        //Used to initialize the nodes with highest weights on the top
        Arrays.sort(p);

        int nodesIndex = 1;
        for(int i = p.length; i >= 1; i--) {
            Node node = new Node();
            node.weight = p[i - 1];
            node.cumulativeWeight = p[i - 1];
            nodes[nodesIndex++] = node;

            sum += p[i - 1];
        }

        computeCumulativeWeights();
    }

    private int random() {

        int currentIndex = 1;

        //Traverse the tree

        //Check which node is closer to the random generated number
        //If it is the parent, it will be selected
        //Otherwise, move to the branch of the subtree with the node closest to the generated number
        while(currentIndex * 2 < nodes.length) {

            double randomNumber = StdRandom.uniform(0, sum);

            double distanceFromParent = Math.abs(nodes[currentIndex].cumulativeWeight - randomNumber);
            double distanceFromLeftChild = Math.abs(nodes[currentIndex * 2].cumulativeWeight - randomNumber);
            double distanceFromRightChild = Double.MAX_VALUE;

            if(currentIndex * 2 + 1 < nodes.length) {
                distanceFromRightChild = Math.abs(nodes[currentIndex * 2 + 1].cumulativeWeight - randomNumber);
            }

            int closestChildIndex;
            double distanceFromClosestChild;

            if(distanceFromLeftChild < distanceFromRightChild) {
                closestChildIndex = currentIndex * 2;
                distanceFromClosestChild = distanceFromLeftChild;
            } else if(distanceFromRightChild < distanceFromLeftChild) {
                closestChildIndex = currentIndex * 2 + 1;
                distanceFromClosestChild = distanceFromRightChild;
            } else {
                //Distance is the same, choose any child randomly
                int randomChild = StdRandom.uniform(2);
                if(randomChild == 0) {
                    closestChildIndex = currentIndex * 2;
                    distanceFromClosestChild = distanceFromLeftChild;
                } else {
                    closestChildIndex = currentIndex * 2 + 1;
                    distanceFromClosestChild = distanceFromRightChild;
                }
            }

            if(distanceFromParent < distanceFromClosestChild) {
                //If the parent is closer to the random number, select it
                return currentIndex;
            } else {
                //Otherwise, move to the branch of the child closest to the random number
                currentIndex = closestChildIndex;
            }
        }

        return currentIndex;
    }

    private void changeKey(int index, double value) {
        double difference = value - nodes[index].weight;
        nodes[index].weight = value;

        sum += difference;

        updateCumulativeWeights(index, difference);
    }

    private void computeCumulativeWeights() {
        for(int i = nodes.length - 1; i >= 2; i--) {
            nodes[i / 2].cumulativeWeight += nodes[i].cumulativeWeight;
        }
    }

    private void updateCumulativeWeights(int startIndex, double difference) {
        while (startIndex >= 1) {
            nodes[startIndex].cumulativeWeight += difference;

            startIndex = startIndex / 2;
        }
    }

    public static void main(String[] args) {
        double[] weights = {1, 2.2, 3.4, 4.1, 5.9, 6};

        Exercise35_Sampling sampling = new Exercise35_Sampling(weights);

        sampling.changeKey(4, 1);

        StdOut.println("Random generated indices:");

        for(int i=0; i < 20; i++) {
            StdOut.println(sampling.random());
        }
    }

}
