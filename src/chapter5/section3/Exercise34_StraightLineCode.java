package chapter5.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 07/03/18.
 */
// The exercise description is not very clear on how the rules on the straight-line program work.
// I was able to get the expected results for the example pattern using the following rules:
// Consider that the best (highest) reachable mismatch state (computed by the DFA in Knuth-Morris-Pratt algorithm)
// is b and the second best (highest than all states, except b) reachable mismatch state is b2.
// 1- If there is a mismatch on state i:
//   1.1- If b < i, go to b.
//   1.2- If b == i, go to b2.
//                   Set (i + 1) state's best mismatch state to i.
// Note that b is never higher than i in a mismatch, since that only happens when a match occurs (and b = i + 1).
public class Exercise34_StraightLineCode {

    // Generates the machine code in O(M * R), where M is the pattern length and R is the alphabet size
    public static String generateStraightLineProgram(String pattern) {

        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Invalid pattern");
        }

        int alphabetSize = 256;
        int patternLength = pattern.length();

        int[] bestMismatchState = computeBestMismatchStates(pattern, alphabetSize);

        StringBuilder straightLineProgram = new StringBuilder();

        // Line 1
        straightLineProgram.append("    int i = -1;\n");
        // Line 2
        straightLineProgram.append("sm: i++;\n");

        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            straightLineProgram.append("s").append(patternIndex)
                    .append(": if (txt[i]) != '").append(pattern.charAt(patternIndex))
                    .append("' goto s");

            if (patternIndex == 0) {
                straightLineProgram.append("m");
            } else {
                straightLineProgram.append(bestMismatchState[patternIndex]);
            }

            straightLineProgram.append(";\n");
        }

        straightLineProgram.append("    return i-").append(patternLength + 2).append(";\n");

        return straightLineProgram.toString();
    }

    private static int[] computeBestMismatchStates(String pattern, int alphabetSize) {
        KnuthMorrisPratt knuthMorrisPratt = new KnuthMorrisPratt(pattern);
        int patternLength = pattern.length();

        int[] bestMismatchStates = new int[patternLength];

        // Used to guarantee that a label will not redirect the code to itself.
        // If label i's best mismatch state is i, set its "best mismatch state" to the second best mismatch state and
        // set label i+1's best mismatch state to i.
        boolean isPreviousStateAvailable = false;

        for (int patternIndex = 1; patternIndex < patternLength; patternIndex++) {

            boolean updateBestMismatchState = true;

            if (isPreviousStateAvailable) {
                bestMismatchStates[patternIndex] = patternIndex - 1;
                isPreviousStateAvailable = false;

                // No other mismatch state can be higher than (patternIndex - 1)
                updateBestMismatchState = false;
            }

            for (int currentChar = 0; currentChar < alphabetSize; currentChar++) {
                int nextState = knuthMorrisPratt.dfa[currentChar][patternIndex];

                if (nextState == patternIndex + 1) {
                    continue;
                }

                if (nextState == patternIndex) {
                    isPreviousStateAvailable = true;
                    continue;
                }

                if (updateBestMismatchState) {
                    bestMismatchStates[patternIndex] = Math.max(bestMismatchStates[patternIndex], nextState);
                }
            }
        }

        return bestMismatchStates;
    }

    public static void main(String[] args) {
        String pattern1 = "AABAAA";
        String straightLineProgram1 = Exercise34_StraightLineCode.generateStraightLineProgram(pattern1);
        StdOut.println("Straight line program 1:");
        StdOut.println(straightLineProgram1);

        StdOut.println("Expected: \n" +
                "    int i = -1;\n" +
                "sm: i++;\n" +
                "s0: if (txt[i]) != 'A' goto sm;\n" +
                "s1: if (txt[i]) != 'A' goto s0;\n" +
                "s2: if (txt[i]) != 'B' goto s0;\n" +
                "s3: if (txt[i]) != 'A' goto s2;\n" +
                "s4: if (txt[i]) != 'A' goto s0;\n" +
                "s5: if (txt[i]) != 'A' goto s3;\n" +
                "    return i-8;\n");

        String pattern2 = "RENERRR";
        String straightLineProgram2 = Exercise34_StraightLineCode.generateStraightLineProgram(pattern2);
        StdOut.println("Straight line program 2:");
        StdOut.println(straightLineProgram2);

        StdOut.println("Expected: \n" +
                "    int i = -1;\n" +
                "sm: i++;\n" +
                "s0: if (txt[i]) != 'R' goto sm;\n" +
                "s1: if (txt[i]) != 'E' goto s0;\n" +
                "s2: if (txt[i]) != 'N' goto s1;\n" +
                "s3: if (txt[i]) != 'E' goto s1;\n" +
                "s4: if (txt[i]) != 'R' goto s0;\n" +
                "s5: if (txt[i]) != 'R' goto s2;\n" +
                "s6: if (txt[i]) != 'R' goto s2;\n" +
                "    return i-9;");
    }

}