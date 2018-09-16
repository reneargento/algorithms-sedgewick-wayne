package chapter6.suffixarrays;

import edu.princeton.cs.algs4.StdOut;
import util.Constants;
import util.FileUtil;

/**
 * Created by Rene Argento on 12/09/18.
 */
// Based on https://algs4.cs.princeton.edu/63suffix/LongestCommonSubstringConcatenate.java.html
// and https://algs4.cs.princeton.edu/63suffix/LongestCommonSubstring.java.html
// Method 1 runs in O(N).
// Methods 2 and 3 run on average in linear time, but have a worst case of O(N^2) when the texts have long common
// substrings. Example: when both texts are equal.
public class Exercise31_LongestCommonSubstring {

    // Method 1
    private String longestCommonSubstringMethod1(String fileName1, String fileName2) {
        String fileContent1 = getStringTextFromFile(fileName1);
        String fileContent2 = getStringTextFromFile(fileName2);

        char inexistentCharacter = getInexistentCharacter(fileContent1, fileContent2);
        String mergedTexts = fileContent1 + inexistentCharacter + fileContent2;

        int text1Length = fileContent1.length();

        SuffixArrayLinearTime suffixArray = new SuffixArrayLinearTime(mergedTexts);
        int[] suffixes = suffixArray.getSuffixes();
        int[] lcpArray = KasaiAlgorithm.buildLCPArray(suffixes, mergedTexts);

        int highestLCPLength = 0;
        int targetSuffixIndex = 0;

        for(int i = 0; i < mergedTexts.length() - 1; i++) {
            // Both suffixes are from text 1
            if (suffixArray.index(i) < text1Length && suffixArray.index(i + 1) < text1Length) {
                continue;
            }

            // Both suffixes are from text 2
            if (suffixArray.index(i) > text1Length && suffixArray.index(i + 1) > text1Length) {
                continue;
            }

            // Suffixes are from different texts
            int longestCommonPrefixLength = lcpArray[i];
            if (longestCommonPrefixLength > highestLCPLength) {
                highestLCPLength = longestCommonPrefixLength;
                targetSuffixIndex = i;
            }
        }

        return mergedTexts.substring(suffixArray.index(targetSuffixIndex),
                suffixArray.index(targetSuffixIndex) + highestLCPLength);
    }

    // Method 2
    private String longestCommonSubstringMethod2(String fileName1, String fileName2) {
        String fileContent1 = getStringTextFromFile(fileName1);
        String fileContent2 = getStringTextFromFile(fileName2);

        char inexistentCharacter = getInexistentCharacter(fileContent1, fileContent2);
        String mergedTexts = fileContent1 + inexistentCharacter + fileContent2;

        int text1Length = fileContent1.length();

        SuffixArrayLinearTime suffixArray = new SuffixArrayLinearTime(mergedTexts);

        int highestLCPLength = 0;
        int targetSuffixIndex = 0;

        for(int i = 1; i < mergedTexts.length(); i++) {
            // Both suffixes are from text 1
            if (suffixArray.index(i) < text1Length && suffixArray.index(i - 1) < text1Length) {
                continue;
            }

            // Both suffixes are from text 2
            if (suffixArray.index(i) > text1Length && suffixArray.index(i - 1) > text1Length) {
                continue;
            }

            // Suffixes are from different texts
            int longestCommonPrefixLength = suffixArray.longestCommonPrefix(i);
            if (longestCommonPrefixLength > highestLCPLength) {
                highestLCPLength = longestCommonPrefixLength;
                targetSuffixIndex = i;
            }
        }

        return mergedTexts.substring(suffixArray.index(targetSuffixIndex),
                suffixArray.index(targetSuffixIndex) + highestLCPLength);
    }

    private char getInexistentCharacter(String string1, String string2) {
        char[] charactersInString1 = new char[256];
        char[] charactersInString2 = new char[256];

        for (int i = 0; i < string1.length(); i++) {
            charactersInString1[string1.charAt(i)]++;
        }

        for (int i = 0; i < string2.length(); i++) {
            charactersInString2[string2.charAt(i)]++;
        }

        for (int i = 0; i < charactersInString1.length; i++) {
            if (charactersInString1[i] == 0 && charactersInString2[i] == 0) {
                return (char) i;
            }
        }

        throw new IllegalStateException("All characters appear in both texts");
    }

    // Method 3
    private String longestCommonSubstringMethod3(String fileName1, String fileName2) {
        String fileContent1 = getStringTextFromFile(fileName1);
        String fileContent2 = getStringTextFromFile(fileName2);

        SuffixArrayLinearTime suffixArray1 = new SuffixArrayLinearTime(fileContent1);
        SuffixArrayLinearTime suffixArray2 = new SuffixArrayLinearTime(fileContent2);

        int highestLCPLength = 0;
        int targetSuffixIndex = 0;

        int index1 = 0;
        int index2 = 0;
        while (index1 < fileContent1.length() && index2 < fileContent2.length()) {
            int longestCommonPrefixLength = longestCommonPrefixLength(fileContent1, suffixArray1.index(index1),
                    fileContent2, suffixArray2.index(index2));

            if (longestCommonPrefixLength > highestLCPLength) {
                highestLCPLength = longestCommonPrefixLength;
                targetSuffixIndex = index1;
            }

            if (compare(fileContent1, suffixArray1.index(index1), fileContent2, suffixArray2.index(index2)) < 0) {
                index1++;
            } else {
                index2++;
            }
        }

        return fileContent1.substring(suffixArray1.index(targetSuffixIndex),
                suffixArray1.index(targetSuffixIndex) + highestLCPLength);
    }

    private int longestCommonPrefixLength(String text1, int index1, String text2, int index2) {
        int text1Length = text1.length() - index1;
        int text2Length = text2.length() - index2;
        int minLength = Math.min(text1Length, text2Length);

        for (int i = 0; i < minLength; i++) {
            if (text1.charAt(index1 + i) != text2.charAt(index2 + i)) {
                return i;
            }
        }

        return minLength;
    }

    private int compare(String text1, int index1, String text2, int index2) {
        int text1Length = text1.length() - index1;
        int text2Length = text2.length() - index2;
        int minLength = Math.min(text1Length, text2Length);

        for (int i = 0; i < minLength; i++) {
            if (text1.charAt(index1 + i) < text2.charAt(index2 + i)) {
                return -1;
            } else if (text1.charAt(index1 + i) > text2.charAt(index2 + i)) {
                return 1;
            }
        }

        return text1Length - text2Length;
    }

    private String getStringTextFromFile(String fileName) {
        String filePath = Constants.FILES_PATH + fileName;
        String fileContent = FileUtil.getAllCharactersFromFile(filePath, true, false);

        if (fileContent == null) {
            return "";
        }

        return fileContent.trim().replaceAll("\\s+", " ");
    }

    // Parameters example: tale_of_two_cities.txt war_and_peace.txt
    // Expected: his elbows on his knees and his h
    public static void main(String[] args) {
        String fileName1 = args[0];
        String fileName2 = args[1];

        Exercise31_LongestCommonSubstring longestCommonSubstring = new Exercise31_LongestCommonSubstring();

        String longestCommonSubstring1 = longestCommonSubstring.longestCommonSubstringMethod1(fileName1, fileName2);
        StdOut.println("Longest common substring 1: " + longestCommonSubstring1);

        String longestCommonSubstring2 = longestCommonSubstring.longestCommonSubstringMethod2(fileName1, fileName2);
        StdOut.println("Longest common substring 2: " + longestCommonSubstring2);

        String longestCommonSubstring3 = longestCommonSubstring.longestCommonSubstringMethod3(fileName1, fileName2);
        StdOut.println("Longest common substring 3: " + longestCommonSubstring3);
    }

}
