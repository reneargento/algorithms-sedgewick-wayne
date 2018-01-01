package chapter3.section3;

import chapter3.section2.Exercise38_TreeDrawing;
import edu.princeton.cs.algs4.StdOut;
import util.ArrayGenerator;


/**
 * Created by Rene Argento on 18/06/17.
 */
@SuppressWarnings("unchecked")
public class Exercise17 {

    private static final int NUMBER_OF_KEYS = 16;

    public static void main(String[] args) {
        new Exercise17().generateRandomKeysAndDrawTree();
    }

    private void generateRandomKeysAndDrawTree() {
//        generateRandomKeys(1);
//        StdOut.println();
//        generateRandomKeys(2);

        //Output from the above methods
        //Random keys 1: P Z R X W H F N B L J I E Q C V
        //Random keys 2: R C J O N E L U W F V S G I K Z
        char[] randomKeys1 = {'P', 'Z', 'R', 'X', 'W', 'H', 'F', 'N', 'B', 'L', 'J', 'I', 'E', 'Q', 'C', 'V'};
        char[] randomKeys2 = {'R', 'C', 'J', 'O', 'N', 'E', 'L', 'U', 'W', 'F', 'V', 'S', 'G', 'I', 'K', 'Z'};

        generateRedBlackBSTAndDraw(randomKeys1);
        //generateRedBlackBSTAndDraw(randomKeys2);

        //generateBSTAndDraw(randomKeys1);
        //generateBSTAndDraw(randomKeys2);
    }

    private void generateRandomKeys(int randomKeysSetIndex) {
        char[] keys = ArrayGenerator.generateRandomUniqueUppercaseChars(NUMBER_OF_KEYS);

        StdOut.print("Random keys " + randomKeysSetIndex + ": ");
        for(char key : keys) {
            StdOut.print(key + " ");
        }
    }

    private void generateRedBlackBSTAndDraw(char[] values) {
        Exercise31_TreeDrawing treeDrawingRedBlackBST = new Exercise31_TreeDrawing();
        Exercise31_TreeDrawing.RedBlackBSTDrawable<Character, Character> redBlackBSTDrawable =
                treeDrawingRedBlackBST.new RedBlackBSTDrawable();

        for(char value : values) {
            redBlackBSTDrawable.put(value, value);
        }

        redBlackBSTDrawable.draw();
    }

    private void generateBSTAndDraw(char[] values) {
        Exercise38_TreeDrawing treeDrawingBST = new Exercise38_TreeDrawing();
        Exercise38_TreeDrawing.BinarySearchTreeDrawable<Character, Character> binarySearchTreeDrawable =
                treeDrawingBST.new BinarySearchTreeDrawable();

        for(char value : values) {
            binarySearchTreeDrawable.put(value, value);
        }

        binarySearchTreeDrawable.draw();
    }

}
