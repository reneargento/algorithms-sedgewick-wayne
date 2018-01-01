package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Rene Argento on 24/03/17.
 */
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

        int n = 1000;//1000000;
        PriorityQueue<ValueForComputation> priorityQueue = new PriorityQueue<>(n, PriorityQueue.Orientation.MIN);

        computationalNumberTheory.initPriorityQueue(priorityQueue, n);
        computationalNumberTheory.computeAllElements(priorityQueue, n);
    }

    private void initPriorityQueue(PriorityQueue<ValueForComputation> priorityQueue, int n) {

        for(int i=0; i <= n; i++) {
            BigInteger iValue = BigInteger.valueOf(i);
            BigInteger value = iValue.multiply(iValue).multiply(iValue);

            ValueForComputation valueForComputation = new ValueForComputation(value, i, 0);

            priorityQueue.insert(valueForComputation);
        }
    }

    private void computeAllElements(PriorityQueue<ValueForComputation> priorityQueue, int n) {

        Map<BigInteger, List<ValueForComputation>> valuesMap = new HashMap<>();

        while (!priorityQueue.isEmpty()) {
            ValueForComputation smallestValue = priorityQueue.deleteTop();

            StdOut.println("Smallest Item: " + smallestValue.value);

            checkSumOfDistinctValues(valuesMap, smallestValue);

            if(smallestValue.j < n) {
                BigInteger iValue = BigInteger.valueOf(smallestValue.i);
                BigInteger jValue = BigInteger.valueOf(smallestValue.j + 1);

                BigInteger newComputedValue = iValue.multiply(iValue).multiply(iValue).add(
                                jValue.multiply(jValue).multiply(jValue));

                ValueForComputation newValue = new ValueForComputation(newComputedValue, smallestValue.i, smallestValue.j + 1);
                priorityQueue.insert(newValue);
            }
        }
    }

    private void checkSumOfDistinctValues(Map<BigInteger, List<ValueForComputation>> valuesMap, ValueForComputation smallestValue) {
        List<ValueForComputation> values = new ArrayList<>();

        if(valuesMap.containsKey(smallestValue.value)) {
            values = valuesMap.get(smallestValue.value);
        }

        //Check if values are distinct and a^3 + b^3 = c^3 + d^3
        for(ValueForComputation valueForComputation : values) {

            if(valueForComputation.value.equals(smallestValue.value)
                    && valueForComputation.i != valueForComputation.j
                    && valueForComputation.i != smallestValue.i
                    && valueForComputation.i != smallestValue.j
                    && valueForComputation.j != smallestValue.i
                    && valueForComputation.j != smallestValue.j
                    && smallestValue.i != smallestValue.j) {
                StdOut.println("a^3 + b^3 = c^3 + d^3: A: " + valueForComputation.i
                + " B: " + valueForComputation.j + " C: " + smallestValue.i + " D: " + smallestValue.j);
            }
        }

        values.add(smallestValue);
        valuesMap.put(smallestValue.value, values);
    }

}
