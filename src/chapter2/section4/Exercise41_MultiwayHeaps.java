package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;
import util.ArrayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rene on 30/03/17.
 */
//Use the VM Option of -Xmx20g to create a 10^9 size array
//This implementation uses array sizes of 10^3, 10^6 and 10^8 for the experiments
public class Exercise41_MultiwayHeaps {

    private enum HeapType {
        BINARY, TERNARY, QUATERNARY;
    }

    private static long numberOfCompares;

    public static void main(String[] args) {
        int[] arraySizes = {1000, 1000000, 100000000};

        Map<Integer, Comparable[]> allInputArrays = new HashMap<>();
        for(int i=0; i < 3; i++) {
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

        for(int i=0; i < 3; i++) {
            Comparable[] originalArray = allInputArrays.get(i);
            Comparable[] arrayCopy1 = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, arrayCopy1, 0, originalArray.length);
            Comparable[] arrayCopy2 = new Comparable[originalArray.length];
            System.arraycopy(originalArray, 0, arrayCopy2, 0, originalArray.length);

            numberOfCompares = 0;
            //3-ary heap
            heapSort(originalArray, HeapType.TERNARY, 3);
            long numberOfCompares3AryHeap = numberOfCompares;

            numberOfCompares = 0;
            //4-ary heap
            heapSort(arrayCopy1, HeapType.QUATERNARY, 4);
            long numberOfCompares4AryHeap = numberOfCompares;

            numberOfCompares = 0;
            //Standard implementation - binary heap
            heapSort(arrayCopy2, HeapType.BINARY, 2);

            printResults(originalArray.length, numberOfCompares, numberOfCompares3AryHeap, numberOfCompares4AryHeap);
        }
    }

    private void heapSort(Comparable[] array, HeapType heapType, int indexOfLeavesStart) {
        constructHeap(array, heapType, indexOfLeavesStart);
        sortdown(array, heapType);
    }

    private void constructHeap(Comparable[] array, HeapType heapType, int indexOfLeavesStart) {
        for(int i = array.length / indexOfLeavesStart + 1; i >= 1; i--) {
            sink(array, i, array.length - 1, heapType);
        }
    }

    private void sortdown(Comparable[] array, HeapType heapType) {
        int endIndex = array.length - 1;

        while (endIndex > 1){
            ArrayUtil.exchange(array, 1, endIndex);
            endIndex--;
            sink(array, 1, endIndex, heapType);
        }
    }

    private void sink(Comparable[] array, int index, int endIndex, HeapType heapType) {
        switch (heapType) {
            case BINARY: sinkBinaryHeap(array, index, endIndex);
                break;
            case TERNARY: sinkTernaryHeap(array, index, endIndex);
                break;
            case QUATERNARY: sinkQuaternaryHeap(array, index, endIndex);
                break;
        }
    }

    //Standard implementation - Heapsort based on a complete heap-ordered binary tree
    private void sinkBinaryHeap(Comparable[] array, int index, int endIndex) {
        while (index * 2 <= endIndex) {
            int biggestChildIndex = index * 2;

            if(index * 2 + 1 <= endIndex) {
                numberOfCompares++;
                if(ArrayUtil.more(array[index * 2 + 1], array[biggestChildIndex])) {
                    biggestChildIndex = index * 2 + 1;
                }
            }

            numberOfCompares++;
            if(ArrayUtil.less(array[index], array[biggestChildIndex])) {
                ArrayUtil.exchange(array, index, biggestChildIndex);
            } else {
                break;
            }

            index = biggestChildIndex;
        }
    }

    //Heapsort based on a complete heap-ordered 3-ary tree
    private void sinkTernaryHeap(Comparable[] array, int index, int endIndex) {
        while (index * 3 - 1 <= endIndex) {
            int biggestChildIndex = index * 3 - 1;

            if(index * 3 <= endIndex) {
                numberOfCompares++;
                if(ArrayUtil.more(array[index * 3], array[biggestChildIndex])) {
                    biggestChildIndex = index * 3;
                }
            }
            if(index * 3 + 1 <= endIndex) {
                numberOfCompares++;
                if(ArrayUtil.more(array[index * 3 + 1], array[biggestChildIndex])) {
                    biggestChildIndex = index * 3 + 1;
                }
            }

            numberOfCompares++;
            if(ArrayUtil.less(array[index], array[biggestChildIndex])) {
                ArrayUtil.exchange(array, index, biggestChildIndex);
            } else {
                break;
            }

            index = biggestChildIndex;
        }
    }

    //Heapsort based on a complete heap-ordered 4-ary tree
    private void sinkQuaternaryHeap(Comparable[] array, int index, int endIndex) {
        while (index * 4 - 2 <= endIndex) {
            int biggestChildIndex = index * 4 - 2;

            if(index * 4 - 1 <= endIndex) {
                numberOfCompares++;
                if(ArrayUtil.more(array[index * 4 - 1], array[biggestChildIndex])) {
                    biggestChildIndex = index * 4 - 1;
                }
            }
            if(index * 4 <= endIndex) {
                numberOfCompares++;
                if(ArrayUtil.more(array[index * 4], array[biggestChildIndex])) {
                    biggestChildIndex = index * 4;
                }
            }
            if(index * 4 + 1 <= endIndex) {
                numberOfCompares++;
                if(ArrayUtil.more(array[index * 4 + 1], array[biggestChildIndex])) {
                    biggestChildIndex = index * 4 + 1;
                }
            }

            numberOfCompares++;
            if(ArrayUtil.less(array[index], array[biggestChildIndex])) {
                ArrayUtil.exchange(array, index, biggestChildIndex);
            } else {
                break;
            }

            index = biggestChildIndex;
        }
    }

    private void printResults(int arraySize, long numberOfComparesBinaryHeap, long numberOfCompares3AryHeap, long numberOfCompares4AryHeap) {
        StdOut.printf("%10d %30d %32d %32d\n", arraySize, numberOfComparesBinaryHeap, numberOfCompares3AryHeap, numberOfCompares4AryHeap);
    }
}
