package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;
import util.ArrayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 30/03/17.
 */
//Use the VM Option of -Xmx20g to create a 10^9 size array
//This implementation uses array sizes of 10^3, 10^6 and 10^8 for the experiments
public class Exercise41_MultiwayHeaps {

    private class DWayPriorityQueue {
        private Comparable[] array;
        private int numberOfChildrenPerNode;

        DWayPriorityQueue(Comparable[] array, int numberOfChildrenPerNode) {
            this.array = array;
            this.numberOfChildrenPerNode = numberOfChildrenPerNode;
        }

        private void heapSort() {
            constructHeap();
            sortdown();
        }

        private void constructHeap() {
            // No need to sink the leaves
            for(int i = array.length / numberOfChildrenPerNode + 1; i >= 1; i--) {
                sink(array, i, array.length - 1);
            }
        }

        private void sortdown() {
            int endIndex = array.length - 1;

            while (endIndex > 1) {
                ArrayUtil.exchange(array, 1, endIndex);
                endIndex--;
                sink(array, 1, endIndex);
            }
        }

        // children of n: ((n * d - (d - 2)), ..., (n * d + 1))
        private void sink(Comparable[] array, int index, int endIndex) {
            int smallestChildIndex = (index * numberOfChildrenPerNode - (numberOfChildrenPerNode - 2));
            int highestChildIndex = (index * numberOfChildrenPerNode + 1);

            while (smallestChildIndex <= endIndex) {

                int biggestChildIndex = smallestChildIndex;

                for(int childIndex = smallestChildIndex + 1; childIndex <= highestChildIndex; childIndex++) {

                    numberOfCompares++;
                    if (childIndex <= endIndex && ArrayUtil.less(array[biggestChildIndex], array[childIndex])) {
                        biggestChildIndex = childIndex;
                    }
                }

                numberOfCompares++;
                if (ArrayUtil.more(array[biggestChildIndex], array[index])) {
                    ArrayUtil.exchange(array, index, biggestChildIndex);
                } else {
                    break;
                }

                index = biggestChildIndex;
                smallestChildIndex = (index * numberOfChildrenPerNode - (numberOfChildrenPerNode - 2));
                highestChildIndex = (index * numberOfChildrenPerNode + 1);
            }
        }
    }

    private static long numberOfCompares;

    public static void main(String[] args) {
        int[] arraySizes = {1000, 1000000, 100000000};

        Map<Integer, Comparable[] > allInputArrays = new HashMap<>();
        for(int i = 0; i < arraySizes.length; i++) {
            Comparable[] array = ArrayGenerator.generateDistinctValuesShuffledArray(arraySizes[i]);
            array[0] = null; //0 index is not used on heaps
            allInputArrays.put(i, array);
        }

        Exercise41_MultiwayHeaps multiwayHeaps = new Exercise41_MultiwayHeaps();
        multiwayHeaps.doExperiment(allInputArrays);
    }

    private void doExperiment(Map<Integer, Comparable[]> allInputArrays) {

        StdOut.printf("%13s %25s %25s %25s\n", "Array Size | ","Number of Compares Std Impl | ", "Number of Compares 3-Ary Heap | "
                , "Number of Compares 4-Ary Heap");

        for(int i = 0; i < allInputArrays.size(); i++) {
            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] arrayCopy1 = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, arrayCopy1, 0, originalArray.length);
            Comparable[] arrayCopy2 = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, arrayCopy2, 0, originalArray.length);

            numberOfCompares = 0;
            //3-ary heap
            DWayPriorityQueue threeWayPriorityQueue = new DWayPriorityQueue(originalArray, 3);
            threeWayPriorityQueue.heapSort();
            long numberOfCompares3AryHeap = numberOfCompares;

            numberOfCompares = 0;
            //4-ary heap
            DWayPriorityQueue fourWayPriorityQueue = new DWayPriorityQueue(arrayCopy1, 4);
            fourWayPriorityQueue.heapSort();
            long numberOfCompares4AryHeap = numberOfCompares;

            numberOfCompares = 0;
            //Standard implementation - binary heap
            DWayPriorityQueue twoWayPriorityQueue = new DWayPriorityQueue(arrayCopy2, 2);
            twoWayPriorityQueue.heapSort();

            printResults(originalArray.length, numberOfCompares, numberOfCompares3AryHeap, numberOfCompares4AryHeap);
        }
    }

    private void printResults(int arraySize, long numberOfComparesBinaryHeap, long numberOfCompares3AryHeap, long numberOfCompares4AryHeap) {
        StdOut.printf("%10d %30d %32d %32d\n", arraySize, numberOfComparesBinaryHeap, numberOfCompares3AryHeap, numberOfCompares4AryHeap);
    }
}
