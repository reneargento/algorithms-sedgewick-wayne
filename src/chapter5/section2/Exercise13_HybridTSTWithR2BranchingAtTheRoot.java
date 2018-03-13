package chapter5.section2;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 05/02/18.
 */
public class Exercise13_HybridTSTWithR2BranchingAtTheRoot {

    @SuppressWarnings("unchecked")
    public class HybridTernarySearchTrie<Value> {

        private static final int R = 256;
        private int size;
        private TernarySearchTrie<Value>[][] ternarySearchTries;
        private static final int NULL_CHAR_INDEX = R;

        public HybridTernarySearchTrie() {
            // Columns have size R + 1 because there may be keys of length 1, which are equivalent to
            // row = character1, column = R
            ternarySearchTries = new TernarySearchTrie[R][R + 1];

            for (int tst1 = 0; tst1 < R; tst1++) {
                for (int tst2 = 0; tst2 <= R; tst2++) {
                    ternarySearchTries[tst1][tst2] = new TernarySearchTrie<>();
                }
            }
        }

        private TernarySearchTrie<Value> getTernarySearchTrie(String key) {
            TernarySearchTrie<Value> ternarySearchTrie;

            char character1 = key.charAt(0);

            if (key.length() == 1) {
                ternarySearchTrie = ternarySearchTries[character1][NULL_CHAR_INDEX];
            } else {
                char character2 = key.charAt(1);
                ternarySearchTrie = ternarySearchTries[character1][character2];
            }

            return ternarySearchTrie;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size() == 0;
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

            TernarySearchTrie<Value> ternarySearchTrie = getTernarySearchTrie(key);
            return ternarySearchTrie.get(key);
        }

        public void put(String key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (!contains(key)) {
                size++;
            }

            TernarySearchTrie<Value> ternarySearchTrie = getTernarySearchTrie(key);
            ternarySearchTrie.put(key, value);
        }

        public void delete(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            TernarySearchTrie<Value> ternarySearchTrie = getTernarySearchTrie(key);
            ternarySearchTrie.delete(key);
            size--;
        }

        public Iterable<String> keys() {
            Queue<String> keys = new Queue<>();

            for (char tst1 = 0; tst1 < R; tst1++) {

                // First add key of size 1
                getAllKeysInTST(tst1, NULL_CHAR_INDEX, keys);

                for (char tst2 = 0; tst2 < R; tst2++) {
                    getAllKeysInTST(tst1, tst2, keys);
                }
            }

            return keys;
        }

        private void getAllKeysInTST(int index1, int index2, Queue<String> keys) {

            if (ternarySearchTries[index1][index2].size() > 0) {
                for (String key : ternarySearchTries[index1][index2].keys()) {
                    keys.enqueue(key);
                }
            }
        }

        public Iterable<String> keysWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("Prefix cannot be null");
            }

            Queue<String> keysWithPrefix = new Queue<>();

            if (prefix.length() == 1) {
                char character1 = prefix.charAt(0);

                // Check if key of size 1 is in the hybrid ternary search trie
                getAllKeysInTST(character1, NULL_CHAR_INDEX, keysWithPrefix);

                // Also check for keys of size 2 or higher
                for (char tst = 0; tst < R; tst++) {
                    getAllKeysInTST(character1, tst, keysWithPrefix);
                }
            } else if (prefix.length() > 1) {
                char character1 = prefix.charAt(0);
                char character2 = prefix.charAt(1);

                getAllKeysInTST(character1, character2, keysWithPrefix);
            }

            return keysWithPrefix;
        }

        public Iterable<String> keysThatMatch(String pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern cannot be null");
            }

            Queue<String> keysThatMatch = new Queue<>();

            if (pattern.length() == 1) {
                char character = pattern.charAt(0);

                if (character != '.') {
                    getAllKeysInTST(character, NULL_CHAR_INDEX, keysThatMatch);
                } else {
                    for (char tst = 0; tst < R; tst++) {
                        getAllKeysInTST(tst, NULL_CHAR_INDEX, keysThatMatch);
                    }
                }
            } else if (pattern.length() > 1) {
                char character1 = pattern.charAt(0);
                char character2 = pattern.charAt(1);

                if (character1 != '.' && character2 != '.') {
                    TernarySearchTrie<Value> ternarySearchTrie = getTernarySearchTrie(pattern);

                    for (String key : ternarySearchTrie.keysThatMatch(pattern)) {
                        keysThatMatch.enqueue(key);
                    }
                } else if (character1 == '.' && character2 != '.') {

                    for (char tst = 0; tst < R; tst++) {
                        TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst][character2];

                        for (String key : ternarySearchTrie.keysThatMatch(pattern)) {
                            keysThatMatch.enqueue(key);
                        }
                    }
                } else if (character1 != '.' && character2 == '.') {

                    for (char tst = 0; tst < R; tst++) {
                        TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[character1][tst];

                        for (String key : ternarySearchTrie.keysThatMatch(pattern)) {
                            keysThatMatch.enqueue(key);
                        }
                    }
                } else {

                    for (char tst1 = 0; tst1 < R; tst1++) {
                        for (int tst2 = 0; tst2 < R; tst2++) {
                            TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst1][tst2];

                            for (String key : ternarySearchTrie.keysThatMatch(pattern)) {
                                keysThatMatch.enqueue(key);
                            }
                        }
                    }
                }
            }

            return keysThatMatch;
        }

        public String longestPrefixOf(String query) {
            if (query == null) {
                throw new IllegalArgumentException("Query cannot be null");
            }

            TernarySearchTrie<Value> ternarySearchTrie = getTernarySearchTrie(query);
            return ternarySearchTrie.longestPrefixOf(query);
        }

        public String floor(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (key.length() == 0) {
                return key;
            }

            String floorKey;

            char character1 = key.charAt(0);
            char character2;

            if (key.length() == 1) {
                character2 = NULL_CHAR_INDEX;
            } else {
                character2 = key.charAt(1);
            }

            boolean mustBeEqualDigit = true;

            for (char tst1 = character1; true; tst1--) {

                if (!mustBeEqualDigit) {
                    character2 = R - 1;
                }

                for (char tst2 = character2; true; tst2--) {
                    TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst1][tst2];
                    floorKey = ternarySearchTrie.floor(key);

                    if (floorKey != null) {
                        break;
                    }

                    // Before decrementing character1 value check for key of size 1
                    if (tst2 == 0) {
                        TernarySearchTrie<Value> ternarySearchTrieKeySize1 = ternarySearchTries[tst1][NULL_CHAR_INDEX];
                        floorKey = ternarySearchTrieKeySize1.floor(key);

                        mustBeEqualDigit = false;
                        break;
                    }
                }

                // tst1 value never becomes less than zero in the for loop, so we need this extra validation
                if (tst1 == 0) {
                    break;
                }

                if (floorKey != null) {
                    break;
                }
            }

            return floorKey;
        }

        public String ceiling(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (key.length() == 0) {
                return key;
            }

            String ceilingKey = null;

            char character1 = key.charAt(0);
            char character2;

            if (key.length() == 1) {
                character2 = NULL_CHAR_INDEX;
            } else {
                character2 = key.charAt(1);
            }

            boolean mustBeEqualDigit = true;

            for (char tst1 = character1; tst1 < R; tst1++) {

                if (!mustBeEqualDigit) {
                    character2 = 0;
                }

                for (char tst2 = character2; tst2 < R; tst2++) {

                    // Before beginning to check character2 value, check for key of size 1
                    if (tst2 == 0) {
                        TernarySearchTrie<Value> ternarySearchTrieKeySize1 = ternarySearchTries[tst1][NULL_CHAR_INDEX];
                        ceilingKey = ternarySearchTrieKeySize1.ceiling(key);

                        if (ceilingKey != null) {
                            break;
                        }
                    }

                    TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst1][tst2];
                    ceilingKey = ternarySearchTrie.ceiling(key);

                    if (ceilingKey != null) {
                        break;
                    }

                    if (tst2 == R - 1) {
                        mustBeEqualDigit = false;
                    }
                }

                if (ceilingKey != null) {
                    break;
                }
            }

            return ceilingKey;
        }

        public String select(int index) {
            if (index < 0 || index >= size()) {
                throw new IllegalArgumentException("Index cannot be negative and must be lower than TST size");
            }

            String targetKey = null;
            int currentIndex = 0;

            for (char tst1 = 0; tst1 < R; tst1++) {
                for (char tst2 = 0; tst2 < R; tst2++) {

                    // Before beginning to check character2 value, check for key of size 1
                    if (tst2 == 0) {
                        TernarySearchTrie<Value> ternarySearchTrieKeySize1 = ternarySearchTries[tst1][NULL_CHAR_INDEX];

                        int size = ternarySearchTrieKeySize1.size();

                        if (currentIndex + size > index) {
                            int targetIndex = index - currentIndex;
                            targetKey = ternarySearchTrieKeySize1.select(targetIndex);
                            break;
                        }

                        currentIndex += size;
                    }

                    TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst1][tst2];

                    int size = ternarySearchTrie.size();

                    if (currentIndex + size > index) {
                        int targetIndex = index - currentIndex;
                        targetKey = ternarySearchTrie.select(targetIndex);
                        break;
                    }

                    currentIndex += size;
                }

                if (targetKey != null) {
                    break;
                }
            }

            return targetKey;
        }

        public int rank(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (key.length() == 0) {
                return 0;
            }

            char character1 = key.charAt(0);
            char character2;

            TernarySearchTrie<Value> keyTernarySearchTrie;
            boolean canDecreaseCharacterValue = true;

            if (key.length() == 1) {
                character2 = NULL_CHAR_INDEX;

                keyTernarySearchTrie = ternarySearchTries[character1][character2];

                if (character1 > 0) {
                    character1--;
                } else {
                    canDecreaseCharacterValue = false;
                }
            } else {
                character2 = key.charAt(1);

                keyTernarySearchTrie = ternarySearchTries[character1][character2];

                if (character2 > 0) {
                    character2--;
                } else if (character1 > 0) {
                    character1--;
                    character2 = R - 1;
                } else {
                    canDecreaseCharacterValue = false;
                }
            }

            // If there are no keys with a prefix lower than the current key we just have to check its TST
            if (!canDecreaseCharacterValue) {
                return keyTernarySearchTrie.rank(key);
            }

            int totalSize = 0;
            boolean mustBeEqualDigit = true;

            for (char tst1 = character1; true; tst1--) {

                if (!mustBeEqualDigit) {
                    character2 = R - 1;
                }

                for (char tst2 = character2; true; tst2--) {
                    TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst1][tst2];
                    totalSize += ternarySearchTrie.size();

                    // Before decrementing character1 value check for key of size 1
                    if (tst2 == 0) {
                        TernarySearchTrie<Value> ternarySearchTrieKeySize1 = ternarySearchTries[tst1][NULL_CHAR_INDEX];
                        totalSize += ternarySearchTrieKeySize1.size();

                        mustBeEqualDigit = false;
                        break;
                    }
                }

                // tst1 value never becomes less than zero in the for loop, so we need this extra validation
                if (tst1 == 0) {
                    break;
                }
            }

            return totalSize + keyTernarySearchTrie.rank(key);
        }

        public String min() {
            if (isEmpty()) {
                return null;
            }

            for (char tst1 = 0; tst1 < R; tst1++) {
                for (char tst2 = 0; tst2 < R; tst2++) {

                    // Before beginning to check character2 value, check for key of size 1
                    if (tst2 == 0) {
                        TernarySearchTrie<Value> ternarySearchTrieKeySize1 = ternarySearchTries[tst1][NULL_CHAR_INDEX];
                        if (!ternarySearchTrieKeySize1.isEmpty()) {
                            return ternarySearchTrieKeySize1.min();
                        }
                    }

                    TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst1][tst2];
                    if (!ternarySearchTrie.isEmpty()) {
                        return ternarySearchTrie.min();
                    }
                }
            }

            return null;
        }

        public String max() {
            if (isEmpty()) {
                return null;
            }

            for (char tst1 = R - 1; true; tst1--) {
                for (char tst2 = R - 1; true; tst2--) {

                    TernarySearchTrie<Value> ternarySearchTrie = ternarySearchTries[tst1][tst2];
                    if (!ternarySearchTrie.isEmpty()) {
                        return ternarySearchTrie.max();
                    }

                    // Before decrementing character1 value check for key of size 1
                    if (tst2 == 0) {
                        TernarySearchTrie<Value> ternarySearchTrieKeySize1 = ternarySearchTries[tst1][NULL_CHAR_INDEX];
                        if (!ternarySearchTrieKeySize1.isEmpty()) {
                            return ternarySearchTrieKeySize1.max();
                        }

                        break;
                    }
                }

                // tst1 value never becomes less than zero in the for loop, so we need this extra validation
                if (tst1 == 0) {
                    break;
                }
            }

            return null;
        }

        public void deleteMin() {
            if (isEmpty()) {
                return;
            }

            String minKey = min();
            delete(minKey);
        }

        public void deleteMax() {
            if (isEmpty()) {
                return;
            }

            String maxKey = max();
            delete(maxKey);
        }
    }

    public static void main(String[] args) {
        HybridTernarySearchTrie<Integer> hybridTernarySearchTrie =
                new Exercise13_HybridTSTWithR2BranchingAtTheRoot().new HybridTernarySearchTrie<>();

        StdOut.println("Size: " + hybridTernarySearchTrie.size());
        StdOut.println("Expected: 0");

        // Put tests
        hybridTernarySearchTrie.put("Rene", 0);
        hybridTernarySearchTrie.put("Re", 1);
        hybridTernarySearchTrie.put("Re", 10);
        hybridTernarySearchTrie.put("Algorithms", 2);
        hybridTernarySearchTrie.put("Algo", 3);
        hybridTernarySearchTrie.put("Algor", 4);
        hybridTernarySearchTrie.put("Tree", 5);
        hybridTernarySearchTrie.put("Trie", 6);
        hybridTernarySearchTrie.put("TST", 7);
        hybridTernarySearchTrie.put("Trie123", 8);
        hybridTernarySearchTrie.put("Z-Function", 9);
        hybridTernarySearchTrie.put("B", 11);
        hybridTernarySearchTrie.put("Binary", 12);

        StdOut.println("\nSize: " + hybridTernarySearchTrie.size());
        StdOut.println("Expected: 12");

        // Get tests
        StdOut.println("\nGet Re: " + hybridTernarySearchTrie.get("Re"));
        StdOut.println("Expected: 10");
        StdOut.println("Get Algorithms: " + hybridTernarySearchTrie.get("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Get Trie123: " + hybridTernarySearchTrie.get("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Get Algori: " + hybridTernarySearchTrie.get("Algori"));
        StdOut.println("Expected: null");
        StdOut.println("Get Zooom: " + hybridTernarySearchTrie.get("Zooom"));
        StdOut.println("Expected: null");

        // Keys() test
        StdOut.println("\nAll keys");
        for(String key : hybridTernarySearchTrie.keys()) {
            StdOut.println(key);
        }

        // Keys with prefix tests
        StdOut.println("\nKeys with prefix Alg");
        StringJoiner keysWithPrefix1 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysWithPrefix("Alg")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println(keysWithPrefix1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with prefix T");
        StringJoiner keysWithPrefix2 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysWithPrefix("T")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println(keysWithPrefix2.toString());
        StdOut.println("Expected: TST Tree Trie Trie123");

        StdOut.println("\nKeys with prefix R");
        StringJoiner keysWithPrefix3 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysWithPrefix("R")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println(keysWithPrefix3.toString());
        StdOut.println("Expected: Re Rene");

        StdOut.println("\nKeys with prefix ZZZ");
        StringJoiner keysWithPrefix4 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysWithPrefix("ZZZ")) {
            keysWithPrefix4.add(key);
        }
        StdOut.println(keysWithPrefix4.toString());
        StdOut.println("Expected: ");

        StdOut.println("\nKeys with prefix B");
        StringJoiner keysWithPrefix5 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysWithPrefix("B")) {
            keysWithPrefix5.add(key);
        }
        StdOut.println(keysWithPrefix5.toString());
        StdOut.println("Expected: B Binary");

        // Keys that match tests
        StdOut.println("\nKeys that match Alg..");
        StringJoiner keysThatMatch1 = new StringJoiner("Alg..");

        for(String key : hybridTernarySearchTrie.keysThatMatch("Alg..")) {
            keysThatMatch1.add(key);
        }
        StdOut.println(keysThatMatch1.toString());
        StdOut.println("Expected: Algor");

        StdOut.println("\nKeys that match Re");
        StringJoiner keysThatMatch2 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysThatMatch("Re")) {
            keysThatMatch2.add(key);
        }
        StdOut.println(keysThatMatch2.toString());
        StdOut.println("Expected: Re");

        StdOut.println("\nKeys that match Tr.e");
        StringJoiner keysThatMatch3 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysThatMatch("Tr.e")) {
            keysThatMatch3.add(key);
        }
        StdOut.println(keysThatMatch3.toString());
        StdOut.println("Expected: Tree Trie");

        StdOut.println("\nKeys that match .");
        StringJoiner keysThatMatch4 = new StringJoiner(" ");

        for(String key : hybridTernarySearchTrie.keysThatMatch(".")) {
            keysThatMatch4.add(key);
        }
        StdOut.println(keysThatMatch4.toString());
        StdOut.println("Expected: B");

        // Longest-prefix-of tests
        StdOut.println("\nLongest prefix of Re: " + hybridTernarySearchTrie.longestPrefixOf("Re"));
        StdOut.println("Expected: Re");

        StdOut.println("Longest prefix of Alg: " + hybridTernarySearchTrie.longestPrefixOf("Alg"));
        StdOut.println("Expected: ");

        StdOut.println("Longest prefix of Algori: " + hybridTernarySearchTrie.longestPrefixOf("Algori"));
        StdOut.println("Expected: Algor");

        StdOut.println("Longest prefix of Trie12345: " + hybridTernarySearchTrie.longestPrefixOf("Trie12345"));
        StdOut.println("Expected: Trie123");

        StdOut.println("Longest prefix of Zooom: " + hybridTernarySearchTrie.longestPrefixOf("Zooom"));
        StdOut.println("Expected: ");

        // Min tests
        StdOut.println("\nMin key: " + hybridTernarySearchTrie.min());
        StdOut.println("Expected: Algo");

        // Max tests
        StdOut.println("\nMax key: " + hybridTernarySearchTrie.max());
        StdOut.println("Expected: Z-Function");

        // Delete min and delete max tests
        hybridTernarySearchTrie.put("ABCKey", 11);
        hybridTernarySearchTrie.put("ZKey", 12);

        StdOut.println("\nKeys after ABCKey and ZKey insert: ");
        for(String key : hybridTernarySearchTrie.keys()) {
            StdOut.println(key);
        }

        hybridTernarySearchTrie.deleteMin();

        StdOut.println("\nKeys after deleteMin: ");
        for(String key : hybridTernarySearchTrie.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ABCKey deleted");

        hybridTernarySearchTrie.deleteMax();

        StdOut.println("\nKeys after deleteMax: ");
        for(String key : hybridTernarySearchTrie.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ZKey deleted");

        // Floor tests
        StdOut.println("\nFloor of Re: " + hybridTernarySearchTrie.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + hybridTernarySearchTrie.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Azure: " + hybridTernarySearchTrie.floor("Azure"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + hybridTernarySearchTrie.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + hybridTernarySearchTrie.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + hybridTernarySearchTrie.floor("Zoom"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Floor of TAB: " + hybridTernarySearchTrie.floor("TAB"));
        StdOut.println("Expected: Rene");
        StdOut.println("Floor of Cat: " + hybridTernarySearchTrie.floor("Cat"));
        StdOut.println("Expected: Binary");
        StdOut.println("Floor of Ball: " + hybridTernarySearchTrie.floor("Ball"));
        StdOut.println("Expected: B");

        // Ceiling tests
        StdOut.println("\nCeiling of Re: " + hybridTernarySearchTrie.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + hybridTernarySearchTrie.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Bull: " + hybridTernarySearchTrie.ceiling("Bull"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + hybridTernarySearchTrie.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + hybridTernarySearchTrie.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + hybridTernarySearchTrie.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Tro: " + hybridTernarySearchTrie.ceiling("Tro"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Ceiling of Ruby: " + hybridTernarySearchTrie.ceiling("Ruby"));
        StdOut.println("Expected: TST");
        StdOut.println("Ceiling of Azure: " + hybridTernarySearchTrie.ceiling("Azure"));
        StdOut.println("Expected: B");
        StdOut.println("Ceiling of Ball: " + hybridTernarySearchTrie.ceiling("Ball"));
        StdOut.println("Expected: Binary");

        // Select tests
        StdOut.println("\nSelect 0: " + hybridTernarySearchTrie.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + hybridTernarySearchTrie.select(3));
        StdOut.println("Expected: B");
        StdOut.println("Select 5: " + hybridTernarySearchTrie.select(5));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + hybridTernarySearchTrie.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 7: " + hybridTernarySearchTrie.select(7));
        StdOut.println("Expected: TST");
        StdOut.println("Select 10: " + hybridTernarySearchTrie.select(10));
        StdOut.println("Expected: Trie123");
        StdOut.println("Select 11: " + hybridTernarySearchTrie.select(11));
        StdOut.println("Expected: Z-Function");

        // Rank tests
        StdOut.println("\nRank of R: " + hybridTernarySearchTrie.rank("R"));
        StdOut.println("Expected: 5");
        StdOut.println("Rank of Re: " + hybridTernarySearchTrie.rank("Re"));
        StdOut.println("Expected: 5");
        StdOut.println("Rank of A: " + hybridTernarySearchTrie.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + hybridTernarySearchTrie.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + hybridTernarySearchTrie.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + hybridTernarySearchTrie.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of B: " + hybridTernarySearchTrie.rank("B"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Ball: " + hybridTernarySearchTrie.rank("Ball"));
        StdOut.println("Expected: 4");
        StdOut.println("Rank of Tarjan: " + hybridTernarySearchTrie.rank("Tarjan"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Trie123: " + hybridTernarySearchTrie.rank("Trie123"));
        StdOut.println("Expected: 10");
        StdOut.println("Rank of Zzoom: " + hybridTernarySearchTrie.rank("Zzoom"));
        StdOut.println("Expected: 12");

        // Delete tests
        hybridTernarySearchTrie.delete("Z-Function");

        StdOut.println("\nKeys() after deleting Z-Function key");
        for(String key : hybridTernarySearchTrie.keys()) {
            StdOut.println(key);
        }

        hybridTernarySearchTrie.delete("Rene");

        StdOut.println("\nKeys() after deleting Rene key");
        for(String key : hybridTernarySearchTrie.keys()) {
            StdOut.println(key);
        }

        hybridTernarySearchTrie.delete("Re");

        StdOut.println("\nKeys() after deleting Re key");
        for(String key : hybridTernarySearchTrie.keys()) {
            StdOut.println(key);
        }
    }

}
