package chapter2.section1;

/**
 * Created by Rene Argento on 02/02/17.
 */
@SuppressWarnings("unchecked")
public class ShellSort {

    public static void shellSort(Comparable[] array) {
        int incrementSequence = 1;

        while(incrementSequence * 3 + 1 < array.length) {
            incrementSequence *= 3;
            incrementSequence++;
        }

        while (incrementSequence > 0) {

            for(int i = incrementSequence; i < array.length; i++) {
                for(int j = i; j >= incrementSequence && array[j].compareTo(array[j - incrementSequence]) < 0; j -= incrementSequence) {
                    Comparable temp = array[j];
                    array[j] = array[j - incrementSequence];
                    array[j - incrementSequence] = temp;
                }
            }

            incrementSequence /= 3;
        }
    }

}
