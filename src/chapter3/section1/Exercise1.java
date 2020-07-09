package chapter3.section1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 22/04/17.
 */
public class Exercise1 {

    public static void main(String[] args) {
        BinarySearchSymbolTable<String, Double> scoresMap = new BinarySearchSymbolTable<>(2);
        scoresMap.put("A+", 4.33);
        scoresMap.put("A", 4.00);
        scoresMap.put("A-", 3.67);
        scoresMap.put("B+", 3.33);
        scoresMap.put("B", 3.00);
        scoresMap.put("B-", 2.67);
        scoresMap.put("C+", 2.33);
        scoresMap.put("C", 2.00);
        scoresMap.put("C-", 1.67);
        scoresMap.put("D", 1.00);
        scoresMap.put("F", 0.00);

        String[] grades = StdIn.readAllLines();
        double gpa = new Exercise1().computeGPA(scoresMap, grades);
        StdOut.printf("GPA: %.2f", gpa);
    }

    private double computeGPA(BinarySearchSymbolTable<String, Double> scoresMap, String[] grades) {

        double totalGrades = 0;

        for (String grade : grades) {
            if (scoresMap.get(grade) == null) {
                StdOut.println("You did not enter a valid grade. Please re-enter the grades. Valid grades are A+ - F");
            }
            totalGrades += scoresMap.get(grade);
        }

        return totalGrades / grades.length;
    }

}
