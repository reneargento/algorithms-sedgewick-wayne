package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;
import util.FileUtil;

/**
 * Created by rene on 30/04/17.
 */
public class Exercise37_PutGetRatio {

    private static final String TALE_FILE_PATH = "/Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/tale_of_two_cities.txt";

    public static void main(String[] args) {
        new Exercise37_PutGetRatio().doExperiments();
    }

    private void doExperiments() {

        int[] numberOfBits = {10, 20, 30};
        String[] input = {"10-bit int values", "20-bit int values", "30-bit int values"};

        StdOut.printf("%21s %20s %20s %8s\n", "Input | ","Running Time Put | ", "Running Time Get | ", "Ratio");

        for(int i=0; i < numberOfBits.length; i++) {
            int lowerBoundValue = 0;
            int higherBoundValue = 0;

            if(numberOfBits[i] == 10) {
                lowerBoundValue = (int) Math.pow(2, 10);
                higherBoundValue = (int) Math.pow(2, 11) - 1;
            } else if(numberOfBits[i] == 20) {
                lowerBoundValue = (int) Math.pow(2, 20);
                higherBoundValue = (int) Math.pow(2, 21) - 1;
            } else if(numberOfBits[i] == 30) {
                lowerBoundValue = (int) Math.pow(2, 30);
                higherBoundValue = (int) Math.pow(2, 31) - 1;
            }

            int[] randomValues = ArrayGenerator.generateRandomIntegerArray(1000000, lowerBoundValue, higherBoundValue);

            double[] result = frequencyCounter(randomValues);

            printResults(input[i], result[0], result[1], result[2]);
        }

        String[] wordsInTale = FileUtil.getAllStringsFromFile(TALE_FILE_PATH);
        double[] result = frequencyCounter(wordsInTale, 0);
        String inputValue = "Tale of Two Cities";
        printResults(inputValue, result[0], result[1], result[2]);
    }

    private double[] frequencyCounter(int[] values) {

        double totalTimeSpentInPut = 0;
        double totalTimeSpentInGet = 0;
        Stopwatch timer;

        BinarySearchSymbolTable<Integer, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(Integer value : values) {

            if(!binarySearchSymbolTable.contains(value)) {
                timer = new Stopwatch();
                binarySearchSymbolTable.put(value, 1);
                totalTimeSpentInPut += timer.elapsedTime();
            } else {
                timer = new Stopwatch();
                binarySearchSymbolTable.put(value, binarySearchSymbolTable.get(value) + 1);
                totalTimeSpentInPut += timer.elapsedTime();
            }
        }

        int max = 0;
        timer = new Stopwatch();
        binarySearchSymbolTable.put(max, 0);
        totalTimeSpentInPut += timer.elapsedTime();

        for(Integer value : binarySearchSymbolTable.keys()) {
            timer = new Stopwatch();
            if(binarySearchSymbolTable.get(value) > binarySearchSymbolTable.get(max)) {
                totalTimeSpentInGet += timer.elapsedTime();
                max = value;
            }
        }

        timer = new Stopwatch();
        String maxFrequency = max + " " + binarySearchSymbolTable.get(max);
        totalTimeSpentInGet += timer.elapsedTime();

        double ratio = totalTimeSpentInPut / totalTimeSpentInGet;

        return new double[]{totalTimeSpentInPut, totalTimeSpentInGet, ratio};
    }

    private double[] frequencyCounter(String[] words, int minLength) {

        double totalTimeSpentInPut = 0;
        double totalTimeSpentInGet = 0;
        Stopwatch timer;

        BinarySearchSymbolTable<String, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();

        for(String word : words) {

            if(word.length() < minLength) {
                continue;
            }

            if(!binarySearchSymbolTable.contains(word)) {
                timer = new Stopwatch();
                binarySearchSymbolTable.put(word, 1);
                totalTimeSpentInPut += timer.elapsedTime();
            } else {
                timer = new Stopwatch();
                binarySearchSymbolTable.put(word, binarySearchSymbolTable.get(word) + 1);
                totalTimeSpentInPut += timer.elapsedTime();
            }
        }

        String max = "";
        timer = new Stopwatch();
        binarySearchSymbolTable.put(max, 0);
        totalTimeSpentInPut += timer.elapsedTime();

        for(String word : binarySearchSymbolTable.keys()) {
            timer = new Stopwatch();
            if(binarySearchSymbolTable.get(word) > binarySearchSymbolTable.get(max)) {
                totalTimeSpentInGet += timer.elapsedTime();
                max = word;
            }
        }

        timer = new Stopwatch();
        String maxFrequency = max + " " + binarySearchSymbolTable.get(max);
        totalTimeSpentInGet += timer.elapsedTime();

        double ratio = totalTimeSpentInPut / totalTimeSpentInGet;

        return new double[]{totalTimeSpentInPut, totalTimeSpentInGet, ratio};
    }

    private void printResults(String input, double totalTimeSpentInPut, double totalTimeSpentInGet, double ratio) {
        StdOut.printf("%18s %20.2f %20.2f %11.2f\n", input, totalTimeSpentInPut, totalTimeSpentInGet, ratio);
    }

}
