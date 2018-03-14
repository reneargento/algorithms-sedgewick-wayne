package chapter3.section1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import util.ArrayGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 28/04/17.
 */
public class Exercise33_DriverSelfOrganizingSearch {

    public static void main(String[] args) {
        int[] arraySizes = {1000, 10000, 100000, 1000000};

        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();
        for(int i = 0; i < arraySizes.length; i++) {
            Comparable[] array = ArrayGenerator.generateOrderedArray(arraySizes[i]);
            allInputArrays.put(i, array);
        }

        Exercise33_DriverSelfOrganizingSearch driverSelfOrganizingSearch = new Exercise33_DriverSelfOrganizingSearch();
        driverSelfOrganizingSearch.doExperiment(allInputArrays);
    }

    private void doExperiment(Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%10s %25s %25s\n", "Array Size | ", "Self-Organizing Search Time | ", "Default Binary Search Time");

        for(int i = 0; i < allInputArrays.size(); i++) {
            BinarySearchSymbolTable<Integer, Integer> binarySearchSymbolTable = new BinarySearchSymbolTable<>();
            Exercise22_SelfOrganizingSearch selfOrganizingSearch = new Exercise22_SelfOrganizingSearch();
            Exercise22_SelfOrganizingSearch.ArraySTSelfOrganizing<Integer, Integer> arraySTSelfOrganizing =
                    selfOrganizingSearch.new ArraySTSelfOrganizing<>(2);

            Comparable[] array = allInputArrays.get(i);

            double[] probabilityDistribution = new double[array.length];
            for(int p = 0; p < array.length; p++) {
                probabilityDistribution[p] = Math.pow((1.0 / 2.0), p + 1);
            }

            //Self-Organizing Search Symbol Table
            Stopwatch selfOrganizingSearchTimer = new Stopwatch();

            for(Comparable key : array) {
                int randomValue = StdRandom.uniform(0, 2);
                arraySTSelfOrganizing.put((int) key, randomValue);
            }

            for(int search = 0; search < 10 * array.length; search++) {

                double keyProbabilityToSearch = StdRandom.uniform();

                for(int p = 0; p < probabilityDistribution.length; p++) {
                    keyProbabilityToSearch -= probabilityDistribution[p];

                    if (keyProbabilityToSearch <= 0) {
                        //Search hit
                        arraySTSelfOrganizing.get((int) array[p]);
                        break;
                    }
                }
            }

            double selfOrganizingSearchRunningTime = selfOrganizingSearchTimer.elapsedTime();

            //Binary Search Symbol Table
            Stopwatch binarySearchTimer = new Stopwatch();

            for(Comparable key : array) {
                int randomValue = StdRandom.uniform(0, 2);
                binarySearchSymbolTable.put((int) key, randomValue);
            }

            for(int search = 0; search < 10 * array.length; search++) {

                double keyProbabilityToSearch = StdRandom.uniform();

                for(int p = 0; p < probabilityDistribution.length; p++) {
                    keyProbabilityToSearch -= probabilityDistribution[p];

                    if (keyProbabilityToSearch <= 0) {
                        //Search hit
                        binarySearchSymbolTable.get((int) array[p]);
                        break;
                    }
                }
            }

            double binarySearchRunningTime = binarySearchTimer.elapsedTime();

            printResults(array.length, selfOrganizingSearchRunningTime, binarySearchRunningTime);
        }
    }

    private void printResults(int arraySize, double selfOrganizingSearchRunningTime, double binarySearchRunningTime) {
        StdOut.printf("%10d %30.2f %29.2f\n", arraySize, selfOrganizingSearchRunningTime, binarySearchRunningTime);
    }

}
