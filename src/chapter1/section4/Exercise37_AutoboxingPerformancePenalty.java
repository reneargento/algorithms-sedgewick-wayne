package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 26/11/16.
 */
@SuppressWarnings("unchecked")
public class Exercise37_AutoboxingPerformancePenalty {

    private static class Exercise37_AutoboxingPerformanceFixCpInt {

        private int[] values;
        private int size;

        public Exercise37_AutoboxingPerformanceFixCpInt(int capacity) {
            values = new int[capacity];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void push(int item) {

            if (size == values.length) {
                return;
            }

            values[size] = item;
            size++;
        }

        public int pop() {

            if (isEmpty()) {
                throw new RuntimeException("Stack underflow");
            }

            int item = values[size - 1];
            size--;
            return item;
        }

    }

    private static class Exercise37_AutoboxingPerformanceFixCpGeneric<Item> {

        private Item[] values;
        private int size;

        @SuppressWarnings("unchecked")
        public Exercise37_AutoboxingPerformanceFixCpGeneric(int capacity) {
            values = (Item[]) new Object[capacity];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void push(Item item) {

            if (size == values.length) {
                return;
            }

            values[size] = item;
            size++;
        }

        public Item pop() {

            if (isEmpty()) {
                throw new RuntimeException("Stack underflow");
            }

            Item item = values[size - 1];
            size--;
            return item;
        }
    }

    private static Exercise37_AutoboxingPerformanceFixCpInt fixedCapacityInteger = new Exercise37_AutoboxingPerformanceFixCpInt(20);
    private static Exercise37_AutoboxingPerformanceFixCpGeneric<Integer> fixedCapacityGeneric = new Exercise37_AutoboxingPerformanceFixCpGeneric<>(20);

    private static double timeTrial(long trials, boolean useGenericStack) {

        Stopwatch timer = new Stopwatch();

        //Trials only do a push and a pop operation
        for(int i = 0; i < trials; i++) {
            if (!useGenericStack) {
                fixedCapacityInteger.push(i);
                fixedCapacityInteger.pop();
            } else {
                fixedCapacityGeneric.push(i);
                fixedCapacityGeneric.pop();
            }
        }

        return timer.elapsedTime();
    }

    public static void main(String[] args) {
        //Using a fixed capacity stack of primitive ints
        StdOut.println("Fixed capacity stack of primitive ints");

        double prev = timeTrial(125, false);

        for(int n = 250; n <= 1000000000; n+=n) {
            double time = timeTrial(n, false);

            StdOut.printf("%6d %7.1f ", n, time);
            StdOut.printf("%5.1f\n", time / prev);
            prev = time;
        }

        //Using a fixed capacity stack of ints with boxing/unboxing to Integer
        StdOut.println("\nFixed capacity stack of ints with boxing/unboxing to Integer");

        prev = timeTrial(125, true);

        for(int n = 250; n < 1000000000; n+=n) {
            double time = timeTrial(n, true);

            StdOut.printf("%6d %7.1f ", n, time);
            StdOut.printf("%5.1f\n", time / prev);
            prev = time;
        }
    }
}
