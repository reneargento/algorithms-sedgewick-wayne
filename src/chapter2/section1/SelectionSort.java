package chapter2.section1;

/**
 * Created by Rene Argento on 02/02/17.
 */
@SuppressWarnings("unchecked")
public class SelectionSort {

    public static void selectionSort(Comparable[] array) {
        for(int i = 0; i < array.length; i++) {
            int minIndex = i;

            for(int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            Comparable temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

}
