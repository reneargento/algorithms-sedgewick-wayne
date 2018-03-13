package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Rene Argento on 09/04/17.
 */
public class Exercise8 {

    private class StringFrequency implements Comparable<StringFrequency>{
        String string;
        int frequency;

        StringFrequency(String string, int frequency) {
            this.string = string;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(StringFrequency that) {
            if (this.frequency > that.frequency) {
                return -1;
            } else if (this.frequency < that.frequency) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> stringList = new ArrayList<>();

        /**
         * Testcase
         *
         test
         test
         begin
         begin
         test
         begin
         abc
         abc
         test
         end
         */
        while(scanner.hasNext()) {
            stringList.add(scanner.next());
        }

        Exercise8 exercise8 = new Exercise8();
        StringFrequency[] frequencies = exercise8.frequency2(stringList);
        for(StringFrequency frequency : frequencies) {
            StdOut.println(frequency.string + " " + frequency.frequency + " occurrence(s)");
        }

        StdOut.println();
        StdOut.println("Expected: \n" +
                "test 4 occurrence(s)\n" +
                "begin 3 occurrence(s)\n" +
                "abc 2 occurrence(s)\n" +
                "end 1 occurrence(s)");
    }

    private StringFrequency[] frequency(List<String> strings) {
        Map<String, Integer> occurrenciesMap = new HashMap<>();

        for(String string : strings) {
            int frequency = 0;

            if (occurrenciesMap.containsKey(string)) {
                frequency = occurrenciesMap.get(string);
            }

            frequency++;
            occurrenciesMap.put(string, frequency);
        }

        StringFrequency[] stringFrequencies = new StringFrequency[occurrenciesMap.size()];
        int stringFrequenciesIndex = 0;

        for(String key : occurrenciesMap.keySet()) {
            int frequency = occurrenciesMap.get(key);

            StringFrequency stringFrequency = new StringFrequency(key, frequency);
            stringFrequencies[stringFrequenciesIndex++] = stringFrequency;
        }

        Arrays.sort(stringFrequencies);
        return stringFrequencies;
    }

    //Optimized for space - no need to use a HashMap
    //Based on http://algs4.cs.princeton.edu/25applications/Frequency.java.html
    private StringFrequency[] frequency2(List<String> strings) {

        StringFrequency[] stringFrequencies = new StringFrequency[strings.size()];
        int stringFrequenciesIndex = 0;

        Collections.sort(strings);
        String currentString = strings.get(0);
        int frequency = 1;

        for(int i = 1; i < strings.size(); i++) {
            if (!currentString.equals(strings.get(i))) {
                stringFrequencies[stringFrequenciesIndex++] = new StringFrequency(currentString, frequency);
                currentString = strings.get(i);
                frequency = 1;
            } else {
                frequency++;
            }
        }

        stringFrequencies[stringFrequenciesIndex++] = new StringFrequency(currentString, frequency);

        Arrays.sort(stringFrequencies, 0, stringFrequenciesIndex);
        StringFrequency[] stringFrequenciesOutput = new StringFrequency[stringFrequenciesIndex];
        System.arraycopy(stringFrequencies, 0, stringFrequenciesOutput, 0, stringFrequenciesIndex);

        return stringFrequenciesOutput;
    }

}
