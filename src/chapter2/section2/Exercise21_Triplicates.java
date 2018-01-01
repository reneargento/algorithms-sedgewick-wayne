package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/02/17.
 */
@SuppressWarnings("unchecked")
public class Exercise21_Triplicates {

    public static void main(String[] args) {

        Comparable[][] names1 = generateArray1();
        String name1 = checkIfThereAreCommonNames(names1);

        StdOut.print("Check 1: ");
        if(name1 == null) {
            StdOut.println("No common name");
        } else {
            StdOut.println(name1);
        }
        StdOut.println("Expected: No common name");

        StdOut.println();

        Comparable[][] names2 = generateArray2();
        String name2 = checkIfThereAreCommonNames(names2);

        StdOut.print("Check 2: ");
        if(name2 == null) {
            StdOut.println("No common name");
        } else {
            StdOut.println(name2);
        }
        StdOut.println("Expected: Rene");
    }

    private static Comparable[][] generateArray1() {
        Comparable[][] names = new Comparable[3][4];

        Comparable[] array1 = new Comparable[4];

        array1[0] = "Name1";
        array1[1] = "Name4";
        array1[2] = "Name3";
        array1[3] = "Name2";

        Comparable[] array2 = new Comparable[4];

        array2[0] = "Name5";
        array2[1] = "Name8";
        array2[2] = "Name6";
        array2[3] = "Name7";

        Comparable[] array3 = new Comparable[4];

        array3[0] = "Name1";
        array3[1] = "Name5";
        array3[2] = "Name2";
        array3[3] = "Name9";

        names[0] = array1;
        names[1] = array2;
        names[2] = array3;

        return names;
    }

    private static Comparable[][] generateArray2() {
        Comparable[][] names = new Comparable[3][4];

        Comparable[] array1 = new Comparable[4];

        array1[0] = "Sedgewick";
        array1[1] = "Zord";
        array1[2] = "Bill";
        array1[3] = "Rene";

        Comparable[] array2 = new Comparable[4];

        array2[0] = "Zord";
        array2[1] = "Wayne";
        array2[2] = "Rene";
        array2[3] = "Larry";

        Comparable[] array3 = new Comparable[4];

        array3[0] = "Rene";
        array3[1] = "Sergey";
        array3[2] = "Zord";
        array3[3] = "Elon";

        names[0] = array1;
        names[1] = array2;
        names[2] = array3;

        return names;
    }


    private static String checkIfThereAreCommonNames(Comparable[][] names) {

        if(names == null || names.length == 0) {
            return null;
        }

        String commonName = null;

        mergesort(names);

        int list1Index = 0;
        int list2Index = 0;
        int list3Index = 0;

        int numberOfNames = names[0].length;
        boolean[] incrementIndex = new boolean[3];

        while (list1Index < numberOfNames && list2Index < numberOfNames && list3Index < numberOfNames) {
            if(names[0][list1Index].equals(names[1][list2Index])
                    && names[1][list2Index].equals(names[2][list3Index])) {
                commonName = names[0][list1Index].toString();
                break;
            } else {

                for(int i=0; i < incrementIndex.length; i++) {
                    incrementIndex[i] = false;
                }

                if(names[0][list1Index].compareTo(names[1][list2Index]) < 0) {
                    incrementIndex[0] = true;
                } else if(names[0][list1Index].compareTo(names[2][list3Index]) < 0) {
                    incrementIndex[0] = true;
                }

                if(names[1][list2Index].compareTo(names[0][list1Index]) < 0) {
                    incrementIndex[1] = true;
                } else if(names[1][list2Index].compareTo(names[2][list3Index]) < 0) {
                    incrementIndex[1] = true;
                }

                if(names[2][list3Index].compareTo(names[0][list1Index]) < 0) {
                    incrementIndex[2] = true;
                } else if(names[2][list3Index].compareTo(names[1][list2Index]) < 0) {
                    incrementIndex[2] = true;
                }

                if(incrementIndex[0]) {
                    list1Index++;
                }
                if(incrementIndex[1]) {
                    list2Index++;
                }
                if(incrementIndex[2]) {
                    list3Index++;
                }
            }
        }

        return commonName;
    }

    private static void mergesort(Comparable[][] names) {

        Comparable[] aux = new Comparable[names[0].length];

        for(Comparable[] nameArray : names) {
            mergesort(nameArray, aux, 0, nameArray.length - 1);
        }

    }

    private static void mergesort(Comparable[] names, Comparable[] aux, int low, int high) {

        if(low >= high) {
            return;
        }

        int middle = low + (high - low) / 2;

        mergesort(names, aux, low, middle);
        mergesort(names, aux, middle + 1, high);

        merge(names, aux, low, middle, high);
    }

    private static void merge(Comparable[] names, Comparable[] aux, int low, int middle, int high) {

        for(int i=low; i <= high; i++) {
            aux[i] = names[i];
        }

        int leftIndex = low;
        int rightIndex = middle + 1;
        int arrayIndex = low;

        while (leftIndex <= middle && rightIndex <= high) {
            if(aux[leftIndex].compareTo(aux[rightIndex]) <= 0) {
                names[arrayIndex] = aux[leftIndex];
                leftIndex++;
            } else {
                names[arrayIndex] = aux[rightIndex];
                rightIndex++;
            }
            arrayIndex++;
        }

        while(leftIndex <= middle) {
            names[arrayIndex] = aux[leftIndex];

            leftIndex++;
            arrayIndex++;
        }
    }
}
