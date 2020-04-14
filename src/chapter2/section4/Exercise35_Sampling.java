package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 27/03/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for fixing the random() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/116
public class Exercise35_Sampling {

    private class Node {
        double weight;
        double cumulativeWeight;

        public Node(double weight) {
            this.weight = weight;
        }
    }

    private Node[] nodes;
    private double sum = 0;

    private Exercise35_Sampling(double[] probabilities) {
        nodes = new Node[probabilities.length + 1];

        for (int i = 1; i <= probabilities.length; i++) {
            double weight = probabilities[i - 1];
            nodes[i] = new Node(weight);
            sum += weight;
        }
        computeCumulativeWeights();
    }

    private void computeCumulativeWeights() {
        for(int i = nodes.length - 1; i >= 2; i--) {
            nodes[i / 2].cumulativeWeight += nodes[i].cumulativeWeight + nodes[i].weight;
        }
    }

    private int random() {
        double randomValue = StdRandom.uniform(0, sum);
        int index = 1;

        while (randomValue < nodes[index].cumulativeWeight) {
            index *= 2;
            double leftSubtreeWeight = nodes[index].cumulativeWeight + nodes[index].weight;

            if (randomValue >= leftSubtreeWeight) {
                randomValue -= leftSubtreeWeight;
                index++;
            }
        }
        return index - 1;
    }

    private void changeKey(int index, double value) {
        index++;
        double difference = value - nodes[index].weight;
        nodes[index].weight = value;

        sum += difference;
        updateCumulativeWeights(index / 2, difference);
    }

    private void updateCumulativeWeights(int index, double difference) {
        while (index >= 1) {
            nodes[index].cumulativeWeight += difference;
            index /= 2;
        }
    }

    public static void main(String[] args) {
        double[] weights = { 5, 1, 3, 4, 2, 20 };
        Exercise35_Sampling sampling = new Exercise35_Sampling(weights);

        sampling.changeKey(5, 5);

        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < 20000; i++) {
            int index = sampling.random();
            result.put(index, result.getOrDefault(index, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            StdOut.println("Key = " + entry.getKey() +
                    " (value = " + weights[entry.getKey()] +
                    ") count = " + entry.getValue());
        }
    }

}
