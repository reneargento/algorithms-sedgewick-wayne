package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Rene Argento on 24/03/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for fixing a bug and improving this algorithm.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/112
public class Exercise25_ComputationalNumberTheory {

    private class ValueForComputation implements Comparable<ValueForComputation>{
        BigInteger value;
        int i;
        int j;

        private ValueForComputation(BigInteger value, int i, int j) {
            this.value = value;
            this.i = i;
            this.j = j;
        }

        @Override
        public int compareTo(ValueForComputation other) {
            return this.value.compareTo(other.value);
        }
    }

    public static void main(String[] args) {
        Exercise25_ComputationalNumberTheory computationalNumberTheory = new Exercise25_ComputationalNumberTheory();

        int n = 1000; //1000000;
        PriorityQueue<ValueForComputation> priorityQueue = new PriorityQueue<>(n + 1, PriorityQueue.Orientation.MIN);

        computationalNumberTheory.initPriorityQueue(priorityQueue, n);
        computationalNumberTheory.computeAllElements(priorityQueue, n);
    }

    private void initPriorityQueue(PriorityQueue<ValueForComputation> priorityQueue, int n) {
        for(int i = 0; i <= n; i++) {
            BigInteger iValue = BigInteger.valueOf(i);
            BigInteger value = iValue.multiply(iValue).multiply(iValue);

            ValueForComputation valueForComputation = new ValueForComputation(value, i, 0);
            priorityQueue.insert(valueForComputation);
        }
    }

    private void computeAllElements(PriorityQueue<ValueForComputation> priorityQueue, int n) {
        List<ValueForComputation> equalResults = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {

            ValueForComputation smallestValue = priorityQueue.deleteTop();
            insertNextElement(priorityQueue, smallestValue, n);
            getEqualValues(priorityQueue, smallestValue, equalResults, n);
            printPairs(equalResults);
            equalResults.clear();
        }
    }

    private void insertNextElement(PriorityQueue<ValueForComputation> priorityQueue, ValueForComputation value,
                                   int maxJValue) {
        if (value.j >= maxJValue) {
            return;
        }
        BigInteger iValue = BigInteger.valueOf(value.i);
        BigInteger jValue = BigInteger.valueOf(value.j + 1);

        BigInteger newComputedValue = iValue.multiply(iValue).multiply(iValue).add(
                jValue.multiply(jValue).multiply(jValue));

        ValueForComputation newValue = new ValueForComputation(newComputedValue, value.i, value.j + 1);
        priorityQueue.insert(newValue);
    }

    private void getEqualValues(PriorityQueue<ValueForComputation> priorityQueue, ValueForComputation currentValue,
                                List<ValueForComputation> equalResults, int n) {
        equalResults.add(currentValue);

        while (!priorityQueue.isEmpty() && priorityQueue.peek().value.equals(currentValue.value)) {
            ValueForComputation equalValue = priorityQueue.deleteTop();
            equalResults.add(equalValue);
            insertNextElement(priorityQueue, equalValue, n);
        }
    }

    private void printPairs(List<ValueForComputation> elements) {
        if (elements.size() < 2) {
            return;
        }

        for (int i = 0; i < elements.size(); i++) {
            ValueForComputation value1 = elements.get(i);
            for (int j = i + 1; j < elements.size(); j++) {
                ValueForComputation value2 = elements.get(j);

                if (areUniqueElements(value1, value2)) {
                    StdOut.println("a^3 + b^3 = c^3 + d^3: A: " + value1.i
                            + " B: " + value1.j + " C: " + value2.i + " D: " + value2.j);
                    StdOut.println("a^3 + b^3 = c^3 + d^3: A: " + value2.i
                            + " B: " + value2.j + " C: " + value1.i + " D: " + value1.j);
                }
            }
        }
    }

    private boolean areUniqueElements(ValueForComputation value1, ValueForComputation value2) {
        return value1.i != value1.j
                && value1.i != value2.i
                && value1.i != value2.j
                && value1.j != value2.i
                && value1.j != value2.j
                && value2.i != value2.j;
    }
}
