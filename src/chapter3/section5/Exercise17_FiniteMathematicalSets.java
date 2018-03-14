package chapter3.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 06/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise17_FiniteMathematicalSets {

    public class MathSET<Key> extends HashSet<Key> {

        HashSet<Key> universe; //Used to guarantee that only keys in this universe will be added to the math set
        Key[] universeArray; //Required by the API constructor

        MathSET(Key[] universe) {
            universeArray = universe;
            this.universe = new HashSet<>();

            for(Key key : universe) {
                this.universe.add(key);
            }
        }

        public void add(Key key) {
            if (!universe.contains(key)) {
                throw new IllegalArgumentException("Key " + key + " does not belong to the universe");
            }

            super.add(key);
        }

        public MathSET<Key> complement() {
            MathSET<Key> complement = new MathSET<>(universeArray);

            for(Key key : universeArray) {
                if (!contains(key)) {
                    complement.add(key);
                }
            }

            return complement;
        }

        public void union(MathSET<Key> mathSetToUnite) {
            for(Key key : mathSetToUnite.keys()) {
                if (!contains(key)) {
                    add(key);
                }
            }
        }

        public void intersection(MathSET<Key> mathSetToIntersect) {
            Iterable<Key> keysInMathSet = keys();

            for(Key key : keysInMathSet) {
                if (!mathSetToIntersect.contains(key)) {
                    delete(key);
                }
            }
        }

        public void delete(Key key) {
            super.delete(key);
        }

        public boolean contains(Key key) {
            return super.contains(key);
        }

        public boolean isEmpty() {
            return super.isEmpty();
        }

        public int size() {
            return super.size();
        }

    }

    public static void main(String[] args) {
        Exercise17_FiniteMathematicalSets finiteMathematicalSets = new Exercise17_FiniteMathematicalSets();

        Integer[] universe = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        MathSET<Integer> mathSET = finiteMathematicalSets.new MathSET<>(universe);

        //Test add
        mathSET.add(0);
        mathSET.add(2);
        mathSET.add(4);
        mathSET.add(6);

        StdOut.println("Keys in mathSET:");
        StdOut.println(mathSET);
        StdOut.println("Expected: { 0, 2, 4, 6 }");

        StdOut.println("\nSize: " + mathSET.size() + " Expected: 4");
        StdOut.println("isEmpty: " + mathSET.isEmpty() + " Expected: false");
        StdOut.println("Contains 4: " + mathSET.contains(4) + " Expected: true");
        StdOut.println("Contains 9: " + mathSET.contains(9) + " Expected: false");

        StdOut.println("\nDelete 6");
        mathSET.delete(6);
        StdOut.println(mathSET);
        StdOut.println("Expected: { 0, 2, 4 }");

        MathSET<Integer> mathSETUnion = finiteMathematicalSets.new MathSET<>(universe);
        mathSETUnion.add(6);
        mathSETUnion.add(8);
        mathSETUnion.add(9);

        mathSET.union(mathSETUnion);

        StdOut.println("\nUnion with { 6, 8, 9 }");
        StdOut.println(mathSET);
        StdOut.println("Expected: { 0, 2, 4, 6, 8, 9 }");

        MathSET<Integer> complement = mathSET.complement();
        StdOut.println("\nComplement");
        StdOut.println(complement);
        StdOut.println("Expected: { 1, 3, 5, 7 }");

        MathSET<Integer> mathSETIntersect = finiteMathematicalSets.new MathSET<>(universe);
        mathSETIntersect.add(1);
        mathSETIntersect.add(4);
        mathSETIntersect.add(8);
        mathSETIntersect.add(9);

        mathSET.intersection(mathSETIntersect);
        StdOut.println("\nIntersect with { 1, 4, 8, 9 }");
        StdOut.println(mathSET);
        StdOut.println("Expected: { 4, 8, 9 }");
    }

}
