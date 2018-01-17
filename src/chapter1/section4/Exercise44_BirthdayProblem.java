package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rene Argento on 27/11/16.
 */
public class Exercise44_BirthdayProblem {

    //Instead of getting 1 number from the command line input, we can test with a range of numbers for more accurate results
    private final int INITIAL_NUMBER_SIZE = 4; // 2^2 = 4
    private final int FINAL_NUMBER_SIZE = 268435456; // 2^28 = 268435456

    public static void main(String[] args) {
        Exercise44_BirthdayProblem birthdayProblem = new Exercise44_BirthdayProblem();
        List<Integer> numbersGeneratedBeforeFirstDuplicate = birthdayProblem.runExperiments();
        birthdayProblem.printResults(numbersGeneratedBeforeFirstDuplicate);
    }

    private List<Integer> runExperiments() {

        List<Integer> numbersGeneratedBeforeFirstDuplicate = new LinkedList<>();

        for(int n = INITIAL_NUMBER_SIZE; n <= FINAL_NUMBER_SIZE; n *= 2) {
            int numbersGenerated = birthdayProblem(n);
            numbersGeneratedBeforeFirstDuplicate.add(numbersGenerated);
        }

        return numbersGeneratedBeforeFirstDuplicate;
    }

    private int birthdayProblem(int n) {
        Map<Integer, Boolean> numbersGenerated = new HashMap<>();

        int numbersGeneratedCount = 0;

        //Repeat until we find a repeated value
        while(true) {
            int number = StdRandom.uniform(0, n);

            numbersGeneratedCount++;

            if (numbersGenerated.containsKey(number)) {
                break;
            } else {
                numbersGenerated.put(number, true);
            }
        }

        return numbersGeneratedCount - 1;
    }

    //For accuracy, the closer to 1, the better
    private void printResults(List<Integer> numbersGeneratedBeforeFirstDuplicate) {
        StdOut.printf("%12s %17s %29s %8s\n", "N |", "Numbers Generated Before Repeat |", "Result Expected by Hypothesis |",
                "Accuracy");

        int numberSize = INITIAL_NUMBER_SIZE;

        for(int numbersGenerated : numbersGeneratedBeforeFirstDuplicate) {
            int resultExpectedByHypothesis = (int) Math.round(Math.sqrt(Math.PI * numberSize / 2));

            StdOut.printf("%10d %15d %29d", numberSize, numbersGenerated, resultExpectedByHypothesis);
            double accuracy = (double) numbersGenerated / (double) resultExpectedByHypothesis;
            StdOut.printf("%29.1f\n", accuracy);

            numberSize *= 2;
        }
    }

}
