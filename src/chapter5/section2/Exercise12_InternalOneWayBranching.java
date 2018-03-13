package chapter5.section2;

import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 03/02/18.
 */
// The methods required to eliminate internal one-way branching are the same as the methods required to eliminate
// external one-way branching, except for put() and delete().
public class Exercise12_InternalOneWayBranching {

    @SuppressWarnings("unchecked")
    public class TrieNoInternalOneWayBranching<Value>
            extends Exercise11_ExternalOneWayBranching.TrieNoExternalOneWayBranching<Value> {

        @Override
        public void put(String key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            boolean isNewKey = false;

            if (!contains(key)) {
                isNewKey = true;
            }

            root = put(root, key, value, 0, isNewKey);
        }

        private Node put(Node node, String key, Value value, int digit, boolean isNewKey) {

            // This exercise eliminates internal one-way branching but allows external one-way branching.
            // If external one-way branching were to be eliminated as well, once we entered this if condition
            // we would create a node representing all the remaining key characters. See Exercise11_ExternalOneWayBranching.
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

            int nodeCharactersLength = 1;

            if (node.characters != null) {
                nodeCharactersLength = node.characters.length();

                // If the key already exists, update its value
                if (!isNewKey && digit - 1 + nodeCharactersLength == key.length()) {
                    node.value = value;
                    return node;
                }

                Node parentNode = new Node();
                // Node's size is already updated so there is no need to increment it
                parentNode.size = node.size;

                StringBuilder parentNodeCharacters = new StringBuilder(String.valueOf(node.characters.charAt(0)));

                int maxLength = Math.max(nodeCharactersLength, key.length() - digit + 1);

                // Key is new
                for (int index = 1; index < maxLength; index++) {

                    if (index < nodeCharactersLength && digit - 1 + index < key.length()) {
                        char existingNodeCharacter = node.characters.charAt(index);
                        char newNodeCurrentCharacter = key.charAt(digit - 1 + index);

                        if (existingNodeCharacter == newNodeCurrentCharacter) {
                            parentNodeCharacters.append(existingNodeCharacter);
                        } else {
                            parentNode.characters = parentNodeCharacters.toString();
                            splitNodes(node, parentNode, key, value, index, digit - 1);
                            return parentNode;
                        }
                    } else {
                        break;
                    }
                }
            }

            if (digit - 1 + nodeCharactersLength < key.length()) {
                char nextChar = key.charAt(digit - 1 + nodeCharactersLength);
                node.next[nextChar] = put(node.next[nextChar], key, value, digit + nodeCharactersLength, isNewKey);
            }

            // Merge nodes on the way back to avoid internal one-way branching
            mergeNodes(node, key, digit);

            return node;
        }

        private void splitNodes(Node originalNode, Node splitParentNode, String key, Value value, int index, int digit) {

            // The first child node will have as characters the substring before the split point
            if (index < originalNode.characters.length()) {
                String remainingCharacters = originalNode.characters.substring(index, originalNode.characters.length());
                Node firstChild = new Node();

                if (remainingCharacters.length() > 1) {
                    firstChild.characters = remainingCharacters;
                }

                firstChild.size = originalNode.size - 1;
                firstChild.value = originalNode.value;

                firstChild.next = originalNode.next;

                char firstChildIndexChar = originalNode.characters.charAt(index);
                splitParentNode.next[firstChildIndexChar] = firstChild;
            } else {
                if (originalNode.value != null) {
                    splitParentNode.value = originalNode.value;
                }
            }

            // The other nodes will have the characters on and after the split point
            if (index + digit < key.length()) {
                String otherChildCharacters = key.substring(index + digit, key.length());

                Node secondChild = new Node();
                char secondChildIndexChar = otherChildCharacters.charAt(0);

                splitNode1NodePerCharacter(secondChild, otherChildCharacters, value);

                splitParentNode.next[secondChildIndexChar] = secondChild;
            } else {
                splitParentNode.value = value;
            }
        }

        private void splitNode1NodePerCharacter(Node node, String characters, Value value) {
            for (int i = 0; i < characters.length(); i++) {
                node.size = 1;

                if (i == characters.length() - 1) {
                    node.value = value;
                } else {
                    char nextChar = characters.charAt(i + 1);

                    Node newNode = new Node();
                    node.next[nextChar] = newNode;

                    node = newNode;
                }
            }
        }

        private void mergeNodes(Node node, String key, int digit) {
            // Do not merge the trie root
            if (digit == 0) {
                return;
            }

            if (node.value == null && node.size > 1) {
                // node.size > 1 is used to allow external one-way branching

                // Merge should be done only if the current node has only 1 direct child node AND the child node
                // has size higher than 1.
                int numberOfDirectChildren = 0;
                boolean childHasSizeHigherThanOne = true;
                Node childNode = null;
                Character childNodeCharacter = null;

                for (char currentChar = 0; currentChar < R; currentChar++) {
                    if (node.next[currentChar] != null) {
                        numberOfDirectChildren++;
                        childNode = node.next[currentChar];
                        childNodeCharacter = currentChar;

                        if (numberOfDirectChildren > 1) {
                            break;
                        }

                        if (node.next[currentChar].size == 1) {
                            childHasSizeHigherThanOne = false;
                            break;
                        }
                    }
                }

                if (numberOfDirectChildren == 1 && childHasSizeHigherThanOne) {
                    String mergedNodeCharacters;

                    if (node.characters != null) {
                        mergedNodeCharacters = node.characters;
                    } else {
                        mergedNodeCharacters = String.valueOf(key.charAt(digit - 1));
                    }

                    if (childNode.characters != null) {
                        mergedNodeCharacters += childNode.characters;
                    } else {
                        mergedNodeCharacters += String.valueOf(childNodeCharacter);
                    }

                    node.characters = mergedNodeCharacters;
                    node.value = childNode.value;
                    node.next = childNode.next;
                }
            }
        }

        public void delete(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            root = delete(root, key, 0);
        }

        private Node delete(Node node, String key, int digit) {
            if (node == null) {
                return null;
            }

            node.size = node.size - 1;

            int nodeCharactersLength = 1;

            if (node.characters != null) {
                nodeCharactersLength = node.characters.length();
            }

            if (digit - 1 + nodeCharactersLength == key.length()) {
                node.value = null;
            } else {
                char nextChar = key.charAt(digit - 1 + nodeCharactersLength);
                node.next[nextChar] = delete(node.next[nextChar], key, digit + nodeCharactersLength);
            }

            if (node.value != null && node.size > 1) {
                return node;
            }

            if (node.size == 0) {
                return null;
            }

            // Merge nodes on the way back to avoid internal one-way branching
            mergeNodes(node, key, digit);

            // Split nodes on the way back to allow external one-way branching
            if (node.size == 1 && node.characters != null) {
                String characters = node.characters;
                Value nodeValue = (Value) node.value;

                if (characters.length() > 1) {
                    node.value = null;
                }
                node.characters = null;

                splitNode1NodePerCharacter(node, characters, nodeValue);
            }

            return node;
        }

    }

    public class TernarySearchTrieNoInternalOneWayBranching<Value>
            extends Exercise11_ExternalOneWayBranching.TernarySearchTrieNoExternalOneWayBranching<Value> {

        @Override
        public void put(String key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            boolean isNewKey = false;

            if (!contains(key)) {
                isNewKey = true;
                size++;
            }

            root = put(root, key, value, 0, isNewKey);
        }

        private Node put(Node node, String key, Value value, int digit, boolean isNewKey) {
            char currentChar = key.charAt(digit);

            // This exercise eliminates internal one-way branching but allows external one-way branching.
            // If external one-way branching were to be eliminated as well, once we entered this if condition
            // we would create a node containing all the remaining key characters. See Exercise11_ExternalOneWayBranching.
            if (node == null) {
                node = new Node();
                node.characters = String.valueOf(currentChar);
            }

            int nodeCharactersLength = node.characters.length();

            if (currentChar < node.characters.charAt(0)) {
                node.left = put(node.left, key, value, digit, isNewKey);
            } else if (currentChar > node.characters.charAt(0)) {
                node.right = put(node.right, key, value, digit, isNewKey);
            } else {

                if (nodeCharactersLength > 1) {

                    // If the key already exists, update its value
                    if (!isNewKey && digit + nodeCharactersLength == key.length()) {
                        node.value = value;
                        return node;
                    }

                    Node parentNode = new Node();
                    parentNode.left = node.left;
                    parentNode.right = node.right;
                    parentNode.middle = node.middle;
                    parentNode.size = node.size + 1;

                    StringBuilder parentNodeCharacters = new StringBuilder(String.valueOf(node.characters.charAt(0)));

                    int maxLength = Math.max(nodeCharactersLength, key.length() - digit);

                    // Key is new
                    for (int index = 1; index < maxLength; index++) {

                        if (index < nodeCharactersLength && digit + index < key.length()) {
                            char existingNodeCharacter = node.characters.charAt(index);
                            char newNodeCurrentCharacter = key.charAt(digit + index);

                            if (existingNodeCharacter == newNodeCurrentCharacter) {
                                parentNodeCharacters.append(existingNodeCharacter);
                            } else {
                                parentNode.characters = parentNodeCharacters.toString();
                                boolean isNewNodeLeftChild = newNodeCurrentCharacter < existingNodeCharacter;
                                splitNodes(node, parentNode, key, value, index, digit, isNewNodeLeftChild);
                                return parentNode;
                            }
                        } else {
                            break;
                        }
                    }
                }

                if (digit + nodeCharactersLength < key.length()) {
                    node.middle = put(node.middle, key, value, digit + nodeCharactersLength, isNewKey);
                } else {
                    node.value = value;
                }

                if (isNewKey) {
                    node.size = node.size + 1;
                }

                // Merge nodes on the way back to avoid internal one-way branching
                mergeNodes(node);
            }

            return node;
        }

        private void splitNodes(Node originalNode, Node splitParentNode, String key, Value value, int index, int digit,
                                boolean isNewNodeLeftChild) {

            // The middleChild node will have as characters the substring before the split point
            if (index < originalNode.characters.length()) {
                String remainingCharacters = originalNode.characters.substring(index, originalNode.characters.length());
                Node middleChild = new Node();
                middleChild.characters = remainingCharacters;
                middleChild.size = splitParentNode.size - 1;
                middleChild.value = originalNode.value;

                middleChild.left = originalNode.left;
                middleChild.right = originalNode.right;
                middleChild.middle = splitParentNode.middle;

                splitParentNode.middle = middleChild;
            } else {
                if (originalNode.value != null) {
                    splitParentNode.value = originalNode.value;
                }
                splitParentNode.middle = originalNode.middle;
            }

            // The other nodes will have the characters on and after the split point
            if (index + digit < key.length()) {
                String otherChildCharacters = key.substring(index + digit, key.length());
                Node newChild = new Node();

                splitNode1NodePerCharacter(newChild, otherChildCharacters, value);

                if (splitParentNode.middle == null) {
                    splitParentNode.middle = newChild;
                } else if (isNewNodeLeftChild) {
                    splitParentNode.middle.left = newChild;
                } else {
                    splitParentNode.middle.right = newChild;
                }
            } else {
                splitParentNode.value = value;
            }
        }

        private void splitNode1NodePerCharacter(Node node, String characters, Value value) {
            for (int i = 0; i < characters.length(); i++) {
                node.characters = String.valueOf(characters.charAt(i));
                node.size = 1;

                if (i == characters.length() - 1) {
                    node.value = value;
                } else {
                    Node newNode = new Node();
                    node.middle = newNode;
                    node = newNode;
                }
            }
        }

        private void mergeNodes(Node node) {
            if (node.middle != null && node.middle.left == null && node.middle.right == null
                    && node.value == null && node.size > 1) {
                // node.size > 1 is used to allow external one-way branching
                node.characters = node.characters + node.middle.characters;
                node.value = node.middle.value;
                node.middle = node.middle.middle;
            }
        }

        @Override
        public void delete(String key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (!contains(key)) {
                return;
            }

            root = delete(root, key, 0);
            size--;
        }

        private Node delete(Node node, String key, int digit) {
            if (node == null) {
                return null;
            }

            int nodeCharactersLength = node.characters.length();

            int substringRightIndex = digit + nodeCharactersLength;
            if (substringRightIndex > key.length()) {
                substringRightIndex = key.length();
            }

            String currentKeyCharacters = key.substring(digit, substringRightIndex);
            int compare = compareStrings(currentKeyCharacters, node.characters);

            if (compare < 0) {
                node.left = delete(node.left, key, digit);
            } else if (compare > 0) {
                node.right = delete(node.right, key, digit);
            } else if (digit + nodeCharactersLength < key.length()) {
                node.size = node.size - 1;
                node.middle = delete(node.middle, key, digit + nodeCharactersLength);
            } else {
                node.size = node.size - 1;
                node.value = null;
            }

            if (node.size == 0) {
                if (node.left == null && node.right == null) {
                    return null;
                } else if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    Node aux = node;
                    node = min(aux.right);
                    node.right = deleteMin(aux.right);
                    node.left = aux.left;
                }
            }

            // Merge nodes on the way back to avoid internal one-way branching
            mergeNodes(node);

            // Split nodes on the way back to allow external one-way branching
            if (node.middle == null && node.characters.length() > 1) {
                String characters = node.characters;
                Value nodeValue = node.value;
                node.value = null;

                splitNode1NodePerCharacter(node, characters, nodeValue);
            }

            return node;
        }

    }

    public static void main(String[] args) {
        Exercise12_InternalOneWayBranching internalOneWayBranching = new Exercise12_InternalOneWayBranching();
        internalOneWayBranching.trieTests();
        internalOneWayBranching.tstTests();
    }

    private void trieTests() {
        StdOut.println("********Trie tests********");
        TrieNoInternalOneWayBranching<Integer> trieNoInternalOneWayBranching = new TrieNoInternalOneWayBranching<>();

        // Put tests
        trieNoInternalOneWayBranching.put("Rene", 0);
        trieNoInternalOneWayBranching.put("Re", 1);
        trieNoInternalOneWayBranching.put("Re", 10);
        trieNoInternalOneWayBranching.put("Algorithms", 2);
        trieNoInternalOneWayBranching.put("Algo", 3);
        trieNoInternalOneWayBranching.put("Algor", 4);
        trieNoInternalOneWayBranching.put("Tree", 5);
        trieNoInternalOneWayBranching.put("Trie", 6);
        trieNoInternalOneWayBranching.put("TST", 7);
        trieNoInternalOneWayBranching.put("Trie123", 8);
        trieNoInternalOneWayBranching.put("Z-Function", 9);

        // Get tests
        StdOut.println("Get Re: " + trieNoInternalOneWayBranching.get("Re"));
        StdOut.println("Expected: 10");
        StdOut.println("Get Algorithms: " + trieNoInternalOneWayBranching.get("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Get Trie123: " + trieNoInternalOneWayBranching.get("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Get Algori: " + trieNoInternalOneWayBranching.get("Algori"));
        StdOut.println("Expected: null");
        StdOut.println("Get Zooom: " + trieNoInternalOneWayBranching.get("Zooom"));
        StdOut.println("Expected: null");

        // Keys() test
        StdOut.println("\nAll keys");
        for(String key : trieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Keys with prefix tests
        StdOut.println("\nKeys with prefix Alg");
        StringJoiner keysWithPrefix1 = new StringJoiner(" ");

        for(String key : trieNoInternalOneWayBranching.keysWithPrefix("Alg")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println(keysWithPrefix1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with prefix T");
        StringJoiner keysWithPrefix2 = new StringJoiner(" ");

        for(String key : trieNoInternalOneWayBranching.keysWithPrefix("T")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println(keysWithPrefix2.toString());
        StdOut.println("Expected: TST Tree Trie Trie123");

        StdOut.println("\nKeys with prefix R");
        StringJoiner keysWithPrefix3 = new StringJoiner(" ");

        for(String key : trieNoInternalOneWayBranching.keysWithPrefix("R")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println(keysWithPrefix3.toString());
        StdOut.println("Expected: Re Rene");

        StdOut.println("\nKeys with prefix ZZZ");
        StringJoiner keysWithPrefix4 = new StringJoiner(" ");

        for(String key : trieNoInternalOneWayBranching.keysWithPrefix("ZZZ")) {
            keysWithPrefix4.add(key);
        }
        StdOut.println(keysWithPrefix4.toString());
        StdOut.println("Expected: ");

        // Keys that match tests
        StdOut.println("\nKeys that match Alg..");
        StringJoiner keysThatMatch1 = new StringJoiner("Alg..");

        for(String key : trieNoInternalOneWayBranching.keysThatMatch("Alg..")) {
            keysThatMatch1.add(key);
        }
        StdOut.println(keysThatMatch1.toString());
        StdOut.println("Expected: Algor");

        StdOut.println("\nKeys that match Re");
        StringJoiner keysThatMatch2 = new StringJoiner(" ");

        for(String key : trieNoInternalOneWayBranching.keysThatMatch("Re")) {
            keysThatMatch2.add(key);
        }
        StdOut.println(keysThatMatch2.toString());
        StdOut.println("Expected: Re");

        StdOut.println("\nKeys that match Tr.e");
        StringJoiner keysThatMatch3 = new StringJoiner(" ");

        for(String key : trieNoInternalOneWayBranching.keysThatMatch("Tr.e")) {
            keysThatMatch3.add(key);
        }
        StdOut.println(keysThatMatch3.toString());
        StdOut.println("Expected: Tree Trie");

        // Longest-prefix-of tests
        StdOut.println("\nLongest prefix of Re: " + trieNoInternalOneWayBranching.longestPrefixOf("Re"));
        StdOut.println("Expected: Re");

        StdOut.println("Longest prefix of Alg: " + trieNoInternalOneWayBranching.longestPrefixOf("Alg"));
        StdOut.println("Expected: ");

        StdOut.println("Longest prefix of Algori: " + trieNoInternalOneWayBranching.longestPrefixOf("Algori"));
        StdOut.println("Expected: Algor");

        StdOut.println("Longest prefix of Trie12345: " + trieNoInternalOneWayBranching.longestPrefixOf("Trie12345"));
        StdOut.println("Expected: Trie123");

        StdOut.println("Longest prefix of Zooom: " + trieNoInternalOneWayBranching.longestPrefixOf("Zooom"));
        StdOut.println("Expected: ");

        // Min tests
        StdOut.println("\nMin key: " + trieNoInternalOneWayBranching.min());
        StdOut.println("Expected: Algo");

        // Max tests
        StdOut.println("\nMax key: " + trieNoInternalOneWayBranching.max());
        StdOut.println("Expected: Z-Function");

        // Delete min and delete max tests
        trieNoInternalOneWayBranching.put("ABCKey", 11);
        trieNoInternalOneWayBranching.put("ZKey", 12);

        StdOut.println("\nKeys after ABCKey and ZKey insert: ");
        for(String key : trieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        trieNoInternalOneWayBranching.deleteMin();

        StdOut.println("\nKeys after deleteMin: ");
        for(String key : trieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ABCKey deleted");

        trieNoInternalOneWayBranching.deleteMax();

        StdOut.println("\nKeys after deleteMax: ");
        for(String key : trieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ZKey deleted");

        // Floor tests
        StdOut.println("\nFloor of Re: " + trieNoInternalOneWayBranching.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + trieNoInternalOneWayBranching.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + trieNoInternalOneWayBranching.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + trieNoInternalOneWayBranching.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + trieNoInternalOneWayBranching.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + trieNoInternalOneWayBranching.floor("Zoom"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Floor of TAB: " + trieNoInternalOneWayBranching.floor("TAB"));
        StdOut.println("Expected: Rene");

        // Ceiling tests
        StdOut.println("\nCeiling of Re: " + trieNoInternalOneWayBranching.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + trieNoInternalOneWayBranching.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + trieNoInternalOneWayBranching.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + trieNoInternalOneWayBranching.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + trieNoInternalOneWayBranching.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + trieNoInternalOneWayBranching.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Tro: " + trieNoInternalOneWayBranching.ceiling("Tro"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Ceiling of Ruby: " + trieNoInternalOneWayBranching.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        // Select tests
        StdOut.println("\nSelect 0: " + trieNoInternalOneWayBranching.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + trieNoInternalOneWayBranching.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + trieNoInternalOneWayBranching.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + trieNoInternalOneWayBranching.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + trieNoInternalOneWayBranching.select(8));
        StdOut.println("Expected: Trie123");
        StdOut.println("Select 9: " + trieNoInternalOneWayBranching.select(9));
        StdOut.println("Expected: Z-Function");

        // Rank tests
        StdOut.println("\nRank of R: " + trieNoInternalOneWayBranching.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + trieNoInternalOneWayBranching.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + trieNoInternalOneWayBranching.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + trieNoInternalOneWayBranching.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + trieNoInternalOneWayBranching.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + trieNoInternalOneWayBranching.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + trieNoInternalOneWayBranching.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + trieNoInternalOneWayBranching.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zzoom: " + trieNoInternalOneWayBranching.rank("Zzoom"));
        StdOut.println("Expected: 10");

        // Delete tests
        trieNoInternalOneWayBranching.delete("Z-Function");

        StdOut.println("\nKeys() after deleting Z-Function key");
        for(String key : trieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        trieNoInternalOneWayBranching.delete("Rene");

        StdOut.println("\nKeys() after deleting Rene key");
        for(String key : trieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Note that "Re" node is now split into 'R' and 'e' nodes

        trieNoInternalOneWayBranching.delete("Re");

        StdOut.println("\nKeys() after deleting Re key");
        for(String key : trieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }
    }

    private void tstTests() {
        StdOut.println("\n********Ternary Search Trie tests********");
        TernarySearchTrieNoInternalOneWayBranching<Integer> ternarySearchTrieNoInternalOneWayBranching =
                new TernarySearchTrieNoInternalOneWayBranching<>();

        // Put tests
        ternarySearchTrieNoInternalOneWayBranching.put("Rene", 0);
        ternarySearchTrieNoInternalOneWayBranching.put("Re", 1);
        ternarySearchTrieNoInternalOneWayBranching.put("Re", 10);
        ternarySearchTrieNoInternalOneWayBranching.put("Algorithms", 2);
        ternarySearchTrieNoInternalOneWayBranching.put("Algo", 3);
        ternarySearchTrieNoInternalOneWayBranching.put("Algor", 4);
        ternarySearchTrieNoInternalOneWayBranching.put("Tree", 5);
        ternarySearchTrieNoInternalOneWayBranching.put("Trie", 6);
        ternarySearchTrieNoInternalOneWayBranching.put("TST", 7);
        ternarySearchTrieNoInternalOneWayBranching.put("Trie123", 8);
        ternarySearchTrieNoInternalOneWayBranching.put("Z-Function", 9);

        // Get tests
        StdOut.println("Get Re: " + ternarySearchTrieNoInternalOneWayBranching.get("Re"));
        StdOut.println("Expected: 10");
        StdOut.println("Get Algorithms: " + ternarySearchTrieNoInternalOneWayBranching.get("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Get Trie123: " + ternarySearchTrieNoInternalOneWayBranching.get("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Get Algori: " + ternarySearchTrieNoInternalOneWayBranching.get("Algori"));
        StdOut.println("Expected: null");
        StdOut.println("Get Zooom: " + ternarySearchTrieNoInternalOneWayBranching.get("Zooom"));
        StdOut.println("Expected: null");

        // Keys() test
        StdOut.println("\nAll keys");
        for(String key : ternarySearchTrieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Keys with prefix tests
        StdOut.println("\nKeys with prefix Alg");
        StringJoiner keysWithPrefix1 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoInternalOneWayBranching.keysWithPrefix("Alg")) {
            keysWithPrefix1.add(key);
        }
        StdOut.println(keysWithPrefix1.toString());
        StdOut.println("Expected: Algo Algor Algorithms");

        StdOut.println("\nKeys with prefix T");
        StringJoiner keysWithPrefix2 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoInternalOneWayBranching.keysWithPrefix("T")) {
            keysWithPrefix2.add(key);
        }
        StdOut.println(keysWithPrefix2.toString());
        StdOut.println("Expected: TST Tree Trie Trie123");

        StdOut.println("\nKeys with prefix R");
        StringJoiner keysWithPrefix3 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoInternalOneWayBranching.keysWithPrefix("R")) {
            keysWithPrefix3.add(key);
        }
        StdOut.println(keysWithPrefix3.toString());
        StdOut.println("Expected: Re Rene");

        StdOut.println("\nKeys with prefix ZZZ");
        StringJoiner keysWithPrefix4 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoInternalOneWayBranching.keysWithPrefix("ZZZ")) {
            keysWithPrefix4.add(key);
        }
        StdOut.println(keysWithPrefix4.toString());
        StdOut.println("Expected: ");

        // Keys that match tests
        StdOut.println("\nKeys that match Alg..");
        StringJoiner keysThatMatch1 = new StringJoiner("Alg..");

        for(String key : ternarySearchTrieNoInternalOneWayBranching.keysThatMatch("Alg..")) {
            keysThatMatch1.add(key);
        }
        StdOut.println(keysThatMatch1.toString());
        StdOut.println("Expected: Algor");

        StdOut.println("\nKeys that match Re");
        StringJoiner keysThatMatch2 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoInternalOneWayBranching.keysThatMatch("Re")) {
            keysThatMatch2.add(key);
        }
        StdOut.println(keysThatMatch2.toString());
        StdOut.println("Expected: Re");

        StdOut.println("\nKeys that match Tr.e");
        StringJoiner keysThatMatch3 = new StringJoiner(" ");

        for(String key : ternarySearchTrieNoInternalOneWayBranching.keysThatMatch("Tr.e")) {
            keysThatMatch3.add(key);
        }
        StdOut.println(keysThatMatch3.toString());
        StdOut.println("Expected: Tree Trie");

        // Longest-prefix-of tests
        StdOut.println("\nLongest prefix of Re: " + ternarySearchTrieNoInternalOneWayBranching.longestPrefixOf("Re"));
        StdOut.println("Expected: Re");

        StdOut.println("Longest prefix of Alg: " + ternarySearchTrieNoInternalOneWayBranching.longestPrefixOf("Alg"));
        StdOut.println("Expected: ");

        StdOut.println("Longest prefix of Algori: " + ternarySearchTrieNoInternalOneWayBranching.longestPrefixOf("Algori"));
        StdOut.println("Expected: Algor");

        StdOut.println("Longest prefix of Trie12345: " + ternarySearchTrieNoInternalOneWayBranching.longestPrefixOf("Trie12345"));
        StdOut.println("Expected: Trie123");

        StdOut.println("Longest prefix of Zooom: " + ternarySearchTrieNoInternalOneWayBranching.longestPrefixOf("Zooom"));
        StdOut.println("Expected: ");

        // Min tests
        StdOut.println("\nMin key: " + ternarySearchTrieNoInternalOneWayBranching.min());
        StdOut.println("Expected: Algo");

        // Max tests
        StdOut.println("\nMax key: " + ternarySearchTrieNoInternalOneWayBranching.max());
        StdOut.println("Expected: Z-Function");

        // Delete min and delete max tests
        ternarySearchTrieNoInternalOneWayBranching.put("ABCKey", 11);
        ternarySearchTrieNoInternalOneWayBranching.put("ZKey", 12);

        StdOut.println("\nKeys after ABCKey and ZKey insert: ");
        for(String key : ternarySearchTrieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        ternarySearchTrieNoInternalOneWayBranching.deleteMin();

        StdOut.println("\nKeys after deleteMin: ");
        for(String key : ternarySearchTrieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ABCKey deleted");

        ternarySearchTrieNoInternalOneWayBranching.deleteMax();

        StdOut.println("\nKeys after deleteMax: ");
        for(String key : ternarySearchTrieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        StdOut.println("Expected: ZKey deleted");

        // Floor tests
        StdOut.println("\nFloor of Re: " + ternarySearchTrieNoInternalOneWayBranching.floor("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Floor of Algori: " + ternarySearchTrieNoInternalOneWayBranching.floor("Algori"));
        StdOut.println("Expected: Algor");
        StdOut.println("Floor of Ball: " + ternarySearchTrieNoInternalOneWayBranching.floor("Ball"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Floor of Tarjan: " + ternarySearchTrieNoInternalOneWayBranching.floor("Tarjan"));
        StdOut.println("Expected: TST");
        StdOut.println("Floor of AA: " + ternarySearchTrieNoInternalOneWayBranching.floor("AA"));
        StdOut.println("Expected: null");
        StdOut.println("Floor of Zoom: " + ternarySearchTrieNoInternalOneWayBranching.floor("Zoom"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Floor of TAB: " + ternarySearchTrieNoInternalOneWayBranching.floor("TAB"));
        StdOut.println("Expected: Rene");

        // Ceiling tests
        StdOut.println("\nCeiling of Re: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("Re"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Algori: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("Algori"));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Ceiling of Ball: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("Ball"));
        StdOut.println("Expected: Re");
        StdOut.println("Ceiling of Tarjan: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("Tarjan"));
        StdOut.println("Expected: Tree");
        StdOut.println("Ceiling of AA: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("AA"));
        StdOut.println("Expected: Algo");
        StdOut.println("Ceiling of Zoom: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("Zoom"));
        StdOut.println("Expected: null");
        StdOut.println("Ceiling of Tro: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("Tro"));
        StdOut.println("Expected: Z-Function");
        StdOut.println("Ceiling of Ruby: " + ternarySearchTrieNoInternalOneWayBranching.ceiling("Ruby"));
        StdOut.println("Expected: TST");

        // Select tests
        StdOut.println("\nSelect 0: " + ternarySearchTrieNoInternalOneWayBranching.select(0));
        StdOut.println("Expected: Algo");
        StdOut.println("Select 3: " + ternarySearchTrieNoInternalOneWayBranching.select(3));
        StdOut.println("Expected: Re");
        StdOut.println("Select 2: " + ternarySearchTrieNoInternalOneWayBranching.select(2));
        StdOut.println("Expected: Algorithms");
        StdOut.println("Select 5: " + ternarySearchTrieNoInternalOneWayBranching.select(5));
        StdOut.println("Expected: TST");
        StdOut.println("Select 8: " + ternarySearchTrieNoInternalOneWayBranching.select(8));
        StdOut.println("Expected: Trie123");
        StdOut.println("Select 9: " + ternarySearchTrieNoInternalOneWayBranching.select(9));
        StdOut.println("Expected: Z-Function");

        // Rank tests
        StdOut.println("\nRank of R: " + ternarySearchTrieNoInternalOneWayBranching.rank("R"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of Re: " + ternarySearchTrieNoInternalOneWayBranching.rank("Re"));
        StdOut.println("Expected: 3");
        StdOut.println("Rank of A: " + ternarySearchTrieNoInternalOneWayBranching.rank("A"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algo: " + ternarySearchTrieNoInternalOneWayBranching.rank("Algo"));
        StdOut.println("Expected: 0");
        StdOut.println("Rank of Algori: " + ternarySearchTrieNoInternalOneWayBranching.rank("Algori"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Algorithms: " + ternarySearchTrieNoInternalOneWayBranching.rank("Algorithms"));
        StdOut.println("Expected: 2");
        StdOut.println("Rank of Tarjan: " + ternarySearchTrieNoInternalOneWayBranching.rank("Tarjan"));
        StdOut.println("Expected: 6");
        StdOut.println("Rank of Trie123: " + ternarySearchTrieNoInternalOneWayBranching.rank("Trie123"));
        StdOut.println("Expected: 8");
        StdOut.println("Rank of Zzoom: " + ternarySearchTrieNoInternalOneWayBranching.rank("Zzoom"));
        StdOut.println("Expected: 10");

        // Delete tests
        ternarySearchTrieNoInternalOneWayBranching.delete("Z-Function");

        StdOut.println("\nKeys() after deleting Z-Function key");
        for(String key : ternarySearchTrieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        ternarySearchTrieNoInternalOneWayBranching.delete("Rene");

        StdOut.println("\nKeys() after deleting Rene key");
        for(String key : ternarySearchTrieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }

        // Note that "Re" node is now split into 'R' and 'e' nodes

        ternarySearchTrieNoInternalOneWayBranching.delete("Re");

        StdOut.println("\nKeys() after deleting Re key");
        for(String key : ternarySearchTrieNoInternalOneWayBranching.keys()) {
            StdOut.println(key);
        }
    }

}
