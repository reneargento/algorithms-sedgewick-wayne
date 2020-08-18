package chapter2.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rene Argento on 19/02/17.
 */
// Thanks to YRFT (https://github.com/YRFT) for suggesting an improved algorithm to this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/176
@SuppressWarnings("unchecked")
public class Exercise21_Triplicates {

    public static void main(String[] args) {
        List<List<String>> names1 = generateNames1();
        String name1 = checkIfThereAreCommonNames(names1);
        printResult(1, name1, "Rene");
        StdOut.println();

        List<List<String>> names2 = generateNames2();
        String name2 = checkIfThereAreCommonNames(names2);
        printResult(2, name2, "No common name");
    }

    private static void printResult(int indexCheck, String name, String expected) {
        StdOut.print("Check " + indexCheck + ": ");
        if (name == null) {
            StdOut.println("No common name");
        } else {
            StdOut.println(name);
        }
        StdOut.println("Expected: " + expected);
    }

    private static List<List<String>> generateNames1() {
        List<String> namesList1 = new ArrayList<>();
        namesList1.add("Sedgewick");
        namesList1.add("Zord");
        namesList1.add("Bill");
        namesList1.add("Rene");

        List<String> namesList2 = new ArrayList<>();
        namesList2.add("Zord");
        namesList2.add("Wayne");
        namesList2.add("Rene");
        namesList2.add("Larry");

        List<String> namesList3 = new ArrayList<>();
        namesList3.add("Rene");
        namesList3.add("Sergey");
        namesList3.add("Zord");
        namesList3.add("Elon");

        List<List<String>> names = new ArrayList<>();
        names.add(namesList1);
        names.add(namesList2);
        names.add(namesList3);

        return names;
    }

    private static List<List<String>> generateNames2() {
        List<String> namesList1 = new ArrayList<>();
        namesList1.add("Name1");
        namesList1.add("Name4");
        namesList1.add("Name3");
        namesList1.add("Name2");

        List<String> namesList2 = new ArrayList<>();
        namesList2.add("Name5");
        namesList2.add("Name8");
        namesList2.add("Name6");
        namesList2.add("Name7");

        List<String> namesList3 = new ArrayList<>();
        namesList3.add("Name1");
        namesList3.add("Name5");
        namesList3.add("Name2");
        namesList3.add("Name9");

        List<List<String>> names = new ArrayList<>();
        names.add(namesList1);
        names.add(namesList2);
        names.add(namesList3);

        return names;
    }

    private static String checkIfThereAreCommonNames(List<List<String>> names) {
        for (List<String> nameList : names) {
            if (nameList == null || nameList.isEmpty()) {
                return null;
            }
        }

        for (List<String> nameList : names) {
            Collections.sort(nameList);
        }

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            indexes.add(0);
        }

        String commonName = null;

        while (true) {
            if (indexes.get(0) >= names.get(0).size()) {
                break;
            }

            String maxName = findMaxName(names, indexes);
            boolean commonNameFound = areAllCurrentNamesEqual(names, indexes);

            if (commonNameFound) {
                commonName = maxName;
                break;
            }

            for (int i = 0; i < names.size(); i++) {
                int index = indexes.get(i);

                while (true) {
                    String name = names.get(i).get(index);

                    if (name.compareTo(maxName) < 0) {
                        index++;
                        indexes.set(i, index);
                    } else {
                        break;
                    }

                    if (index >= names.get(i).size()) {
                        return null;
                    }
                }
            }
        }

        return commonName;
    }

    private static boolean areAllCurrentNamesEqual(List<List<String>> names, List<Integer> indexes) {
        for (int i = 0; i < names.size() - 1; i++) {
            int index1 = indexes.get(i);
            String name1 = names.get(i).get(index1);

            int index2 = indexes.get(i + 1);
            String name2 = names.get(i + 1).get(index2);

            if (!name1.equals(name2)) {
                return false;
            }
        }

        return true;
    }

    private static String findMaxName(List<List<String>> names, List<Integer> indexes) {
        String maxName = names.get(0).get(indexes.get(0));

        for (int i = 1; i < names.size(); i++) {
            int index = indexes.get(i);
            String name = names.get(i).get(index);

            if (name.compareTo(maxName) > 0) {
                maxName = name;
            }
        }

        return maxName;
    }
}
