package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rene Argento on 27/11/16.
 */
public class Exercise43_ResizingArraysVersusLinkedList<Item> {

    private interface Stack<Item> {
        boolean isEmpty();
        int size();
        void push(Item item);
        Item pop();
    }

    private class Node {
        Item item;
        Node next;
    }

    private class StackWithLinkedList implements Stack<Item>{

        private Node first;
        private int size;

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void push(Item item) {

            Node oldFirst = first;

            first = new Node();
            first.item = item;
            first.next = oldFirst;

            size++;
        }

        public Item pop() {

            if (isEmpty()) {
                throw new RuntimeException("Stack underflow");
            }

            Item item = first.item;
            first = first.next;

            size--;

            return item;
        }
    }

    @SuppressWarnings("unchecked")
    private class StackWithResizingArray implements Stack<Item>{

        Item[] values;
        int size;

        public StackWithResizingArray(int initialSize) {
            values = (Item[]) new Object[initialSize];
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void push(Item item) {
            if (size == values.length) {
                resizeArray(size * 2);
            }

            values[size] = item;
            size++;
        }

        public Item pop() {
            if (isEmpty()) {
                throw new RuntimeException("Stack underflow");
            }

            Item item = values[size - 1];
            values[size - 1] = null; // to avoid loitering
            size--;

            if (size == values.length / 4) {
                resizeArray(values.length / 2);
            }

            return item;
        }

        private void resizeArray(int newCapacity) {

            Item[] newValues = (Item[]) new Object[newCapacity];

            for(int i = 0; i < size; i++) {
                newValues[i] = values[i];
            }

            values = newValues;
        }
    }

    private final int INITIAL_NUMBER_OF_OPERATIONS = 524288; // 2^19 = 524288
    private final int FINAL_NUMBER_OF_OPERATIONS = 67108864; // 2^26 = 67108864

    public static void main(String[] args) {
        Exercise43_ResizingArraysVersusLinkedList<Integer> resizingArrayXLinkedList = new Exercise43_ResizingArraysVersusLinkedList<>();

        //Resizing array stack
        Stack<Integer> resizingArrayStack = resizingArrayXLinkedList.new StackWithResizingArray(10);
        List<Double> resizingArrayRunningTimes = resizingArrayXLinkedList.runExperiments(resizingArrayStack);

        //Linked list stack
        Stack<Integer> linkedListStack = resizingArrayXLinkedList.new StackWithLinkedList();
        List<Double> linkedListRunningTimes = resizingArrayXLinkedList.runExperiments(linkedListStack);

        double[][] runningTimes = new double[resizingArrayRunningTimes.size()][2];

        for(int i = 0; i < resizingArrayRunningTimes.size(); i++) {
            runningTimes[i][0] = resizingArrayRunningTimes.get(i);
        }
        for(int i = 0; i < linkedListRunningTimes.size(); i++) {
            runningTimes[i][1] = linkedListRunningTimes.get(i);
        }

        resizingArrayXLinkedList.printResults(runningTimes);
    }

    private List<Double> runExperiments(Stack<Integer> stack) {

        List<Double> runningTimes = new LinkedList<>();

        for(int n = INITIAL_NUMBER_OF_OPERATIONS; n <= FINAL_NUMBER_OF_OPERATIONS; n += n) {
            double runningTime = timeTrial(n, stack);
            runningTimes.add(runningTime);
        }

        return runningTimes;
    }

    private double timeTrial(int n, Stack<Integer> stack) {
        int max = 1000000;
        int[] numbers = new int[n];

        for(int i = 0; i < n; i++) {
            numbers[i] = StdRandom.uniform(-max, max);
        }

        Stopwatch timer = new Stopwatch();

        //N pushes and pop operations

        for(int number : numbers) {
            stack.push(number);
        }

        while(!stack.isEmpty()) {
            stack.pop();
        }

        return timer.elapsedTime();
    }

    private void printResults(double[][] runningTimes) {
        StdOut.printf("%13s %7s %6s %9s\n", "N operations", "Array", "List", "Ratio");

        int numberOfOperations = INITIAL_NUMBER_OF_OPERATIONS;

        for(int i = 0; i < runningTimes.length; i++) {
            StdOut.printf("%13d %7.1f %6.1f", numberOfOperations, runningTimes[i][0], runningTimes[i][1]);
            StdOut.printf("%9.1f\n", runningTimes[i][0] / runningTimes[i][1]);

            numberOfOperations *= 2;
        }
    }
}
