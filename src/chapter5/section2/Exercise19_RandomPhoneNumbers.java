package chapter5.section2;

import chapter1.section3.LinkedList;
import chapter3.section5.HashSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import util.Constants;
import util.FileUtil;

import java.util.List;

/**
 * Created by Rene Argento on 16/02/18.
 */
@SuppressWarnings("unchecked")
public class Exercise19_RandomPhoneNumbers {

    public static class TrieDigitAlphabet<Value> {
        public static int R = 10;
        private Node root = new Node();

        private static class Node {
            private Object value;
            private Node[] next = new Node[R];
            private int size;
        }

        public boolean contains(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            return get(key) != null;
        }

        public Value get(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (key.length() == 0) {
                throw new IllegalArgumentException("Key must have a positive length");
            }

            Node node = get(root, key, 0);

            if (node == null) {
                return null;
            }
            return (Value) node.value;
        }

        private Node get(Node node, String key, int digit) {
            if (node == null) {
                return null;
            }

            if (digit == key.length()) {
                return node;
            }

            int nextDigit = Character.getNumericValue(key.charAt(digit)); // Use digitTh key char to identify subtrie.
            return get(node.next[nextDigit], key, digit + 1);
        }

        public void put(String key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            // No need for delete() in this exercise
            if (value == null) {
                return;
            }

            boolean isNewKey = false;

            if (!contains(key)) {
                isNewKey = true;
            }

            root = put(root, key, value, 0, isNewKey);
        }

        private Node put(Node node, String key, Value value, int digit, boolean isNewKey) {
            if (node == null) {
                node = new Node();
            }

            if (isNewKey) {
                node.size = node.size + 1;
            }

            if (digit == key.length()) {
                node.value = value;
                return node;
            }

            int nextDigit = Character.getNumericValue(key.charAt(digit)); // Use digitTh key char to identify subtrie.
            node.next[nextDigit] = put(node.next[nextDigit], key, value, digit + 1, isNewKey);

            return node;
        }
    }

    private HashSet<String> getAreaCodes(String areaCodesFileName) {
        HashSet<String> areaCodes = new HashSet<>();

        String filePath = Constants.FILES_PATH + areaCodesFileName;
        List<String> areaCodesInformation = FileUtil.getAllLinesFromFile(filePath);

        if (areaCodesInformation == null) {
            return areaCodes;
        }

        boolean headerLine = true;

        for(String areaCodeInformation : areaCodesInformation) {
            if (headerLine) {
                headerLine = false;
                continue;
            }

            String areaCode = areaCodeInformation.substring(0, 3);
            areaCodes.add(areaCode);
        }

        return areaCodes;
    }

    private LinkedList<String> generateRandomPhones(int numberOfPhones, HashSet<String> areaCodes) {

        LinkedList<String> randomPhones = new LinkedList<>();
        TrieDigitAlphabet<Integer> trie = new TrieDigitAlphabet<>();

        for(int i = 0; i < numberOfPhones; i++) {

            StringBuilder areaCode = new StringBuilder();
            boolean areaCodeGenerated = false;

            while (!areaCodeGenerated) {
                areaCode = new StringBuilder();

                for (int digit = 0; digit < 3; digit++) {
                    int digitValue = StdRandom.uniform(10);
                    areaCode.append(digitValue);
                }

                if (areaCodes.contains(areaCode.toString())) {
                    areaCodeGenerated = true;
                }
            }

            StringBuilder restOfPhoneNumber = new StringBuilder();

            for (int digit = 0; digit < 7; digit++) {
                int digitValue = StdRandom.uniform(10);
                restOfPhoneNumber.append(digitValue);
            }

            String phone = areaCode.toString() + restOfPhoneNumber.toString();
            String formattedPhone = "(" + areaCode.toString() + ") " +
                    restOfPhoneNumber.substring(0, 3) + "-" + restOfPhoneNumber.substring(3);

            // Avoid choosing the same number more than once
            if (trie.contains(phone)) {
                i--;
                continue;
            } else {
                trie.put(phone, 0); // value is not used
            }

            randomPhones.insert(formattedPhone);
        }


        return randomPhones;
    }

    private void printPhones(LinkedList<String> randomPhones) {
        StdOut.println("Random phone numbers");

        for(String phone : randomPhones) {
            StdOut.println(phone);
        }
    }

    // Area codes file is a txt file converted from phone-na.csv in https://introcs.cs.princeton.edu/java/data/

    // Parameters example: area_codes.txt 20
    public static void main(String[] args) {
        Exercise19_RandomPhoneNumbers randomPhoneNumbers = new Exercise19_RandomPhoneNumbers();

        String areaCodesFileName = args[0];
        int numberOfPhones = Integer.parseInt(args[1]);

        HashSet<String> areaCodes = randomPhoneNumbers.getAreaCodes(areaCodesFileName);
        LinkedList<String> randomPhones = randomPhoneNumbers.generateRandomPhones(numberOfPhones, areaCodes);
        randomPhoneNumbers.printPhones(randomPhones);
    }

}
