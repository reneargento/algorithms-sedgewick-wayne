package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.*;

/**
 * Created by Rene Argento on 27/11/16.
 */
public class Exercise45_CouponCollectorProblem {

    private final int INITIAL_NUMBER_SIZE = 4; // 2^2 = 4
    private final int FINAL_NUMBER_SIZE;
    private float[] dpHarmonicNumbers;

    public Exercise45_CouponCollectorProblem(int maxNumberSize) {
        FINAL_NUMBER_SIZE = maxNumberSize;
        dpHarmonicNumbers = new float[maxNumberSize + 1];

        dpHarmonicNumbers[1] = 1;
    }

    public static void main(String[] args) {
        Exercise45_CouponCollectorProblem couponCollectorProblem = new Exercise45_CouponCollectorProblem(4194304); // 2^22 = 4194304
        List<Integer> numbersGeneratedBeforeAllPossibleValues = couponCollectorProblem.runExperiments();
        couponCollectorProblem.printResults(numbersGeneratedBeforeAllPossibleValues);
    }

    private List<Integer> runExperiments() {

        List<Integer> numbersGeneratedBeforeAllPossibleValues = new LinkedList<>();

        for(int n = INITIAL_NUMBER_SIZE; n <= FINAL_NUMBER_SIZE; n *= 2) {
            int numbersGenerated = couponCollectorProblem(n);
            numbersGeneratedBeforeAllPossibleValues.add(numbersGenerated);
        }

        return numbersGeneratedBeforeAllPossibleValues;
    }

    private int couponCollectorProblem(int n) {
        Set<Integer> numbersGenerated = new HashSet<>();

        int numbersGeneratedCount = 0;

        //Repeat until we generate all possible values
        while(true) {
            int number = StdRandom.uniform(0, n);
            numbersGenerated.add(number);

            numbersGeneratedCount++;

            if (numbersGenerated.size() == n) {
                break;
            }
        }

        return numbersGeneratedCount - 1;
    }

    //For accuracy, the closer to 1, the better
    private void printResults(List<Integer> numbersGeneratedBeforeAllPossibleValues) {
        StdOut.printf("%12s %21s %29s %8s\n", "N |", "Numbers Generated Before All Values |", "Result Expected by Hypothesis |",
                "Accuracy");

        int numberSize = INITIAL_NUMBER_SIZE;

        for(int numbersGenerated : numbersGeneratedBeforeAllPossibleValues) {
            int expectedResultByHypothesis = (int) Math.round(getExpectedResultByHypothesis(numberSize));

            StdOut.printf("%10d %19d %33d", numberSize, numbersGenerated, expectedResultByHypothesis);
            double accuracy = (double) numbersGenerated / (double) expectedResultByHypothesis;
            StdOut.printf("%25.1f\n", accuracy);

            numberSize *= 2;
        }
    }

    private double getExpectedResultByHypothesis(int numberSize) {
        return numberSize * getHarmonicNumber(numberSize);
    }

    private float getHarmonicNumber(int number) {

        if (dpHarmonicNumbers[number] != 0) {
            return dpHarmonicNumbers[number];
        }

        int index = number - 1;
        //Find last computed value
        while (dpHarmonicNumbers[index] == 0) {
            index--;
        }

        while (index <= number) {
            dpHarmonicNumbers[index] = ((float) 1 / (float) index) + dpHarmonicNumbers[index -1];
            index++;
        }

        return dpHarmonicNumbers[number];
    }
}
