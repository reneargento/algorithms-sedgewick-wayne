package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 10/1/16.
 */
public class Exercise19_LocalMinimumMatrix {

    public static void main(String[] args) {
        int matrix1[][] = {{1}};
        int matrix2[][] = {{4, 1},
                           {3, -2}};
        int matrix3[][] = {{5, 2, 3},
                           {4, 6, 1},
                           {7, 8, 9}};
        int matrix4[][] = {{5, 90, 3, 10},
                           {4, -9, 1, 15},
                           {7, -1, 9, 19},
                           {12, 8, 13, 99}};
        int matrix5[][] = {{5, 90, 3, 10},
                          {4,  1, -7, 15},
                          {7, -1, -8, 19},
                          {12, 8, 13, 99}};

        int localMinimum1 = localMinimumMatrix(matrix1);
        int localMinimum2 = localMinimumMatrix(matrix2);
        int localMinimum3 = localMinimumMatrix(matrix3);
        int localMinimum4 = localMinimumMatrix(matrix4);
        int localMinimum5 = localMinimumMatrix(matrix5);

        StdOut.println("Local Minimum Matrix: " + localMinimum1 + " Expected: 1");
        StdOut.println("Local Minimum Matrix: " + localMinimum2 + " Expected: -2");
        StdOut.println("Local Minimum Matrix: " + localMinimum3 + " Expected: 1"); //2 and 4 would also be valid
        StdOut.println("Local Minimum Matrix: " + localMinimum4 + " Expected: -9");
        StdOut.println("Local Minimum Matrix: " + localMinimum5 + " Expected: -8");
    }

    private static int localMinimumMatrix(int[][] matrix) {
        if (matrix == null) {
            throw new RuntimeException("Matrix cannot be null");
        }
        if (matrix.length != matrix[0].length) {
            throw new RuntimeException("Matrix must be NxN");
        }

        //N = 1
        if (matrix.length == 1) {
            return matrix[0][0];
        }

        //N = 2
        if (matrix.length == 2) {
            if (matrix[0][0] < matrix[0][1]
                    && matrix[0][0] < matrix[1][0]) {
                return matrix[0][0];
            } else if (matrix[0][1] < matrix[0][0]
                    && matrix[0][1] < matrix[1][1]) {
                return matrix[0][1];
            } else if (matrix[1][0] < matrix[0][0]
                    && matrix[1][0] < matrix[1][1]) {
                return matrix[1][0];
            } else {
                return matrix[1][1];
            }
        }

        //N > 2
        return localMinimumMatrix(matrix, 0, matrix.length - 1, 0, matrix[0].length - 1);
    }

    //O(n)
    private static int localMinimumMatrix(int[][] matrix, int firstRow, int endRow, int firstColumn, int endColumn) {

        if (firstRow == endRow && firstColumn == endColumn) {
            return matrix[firstRow][firstColumn];
        }

        int currentMinimumElement = matrix[firstRow][firstColumn];
        int currentMinimumElementRow = firstRow;
        int currentMinimumElementColumn = firstColumn;

        int centerRow = firstRow + (endRow - firstRow) / 2;
        int centerColumn = firstColumn + (endColumn - firstColumn) / 2;

        //Look at boundaries and center for the minimum element

        //O(2n) = O(n) - check rows
        for(int i = firstColumn; i <= endColumn; i++) {
            if (matrix[firstRow][i] < currentMinimumElement) {
                currentMinimumElement = matrix[firstRow][i];
                currentMinimumElementRow = firstRow;
                currentMinimumElementColumn = i;
            }

            if (matrix[endRow][i] < currentMinimumElement) {
                currentMinimumElement = matrix[endRow][i];
                currentMinimumElementRow = endRow;
                currentMinimumElementColumn = i;
            }
        }

        //O(2n) = O(n) - check columns
        for(int i = firstRow; i <= endRow; i++) {
            if (matrix[i][firstColumn] < currentMinimumElement) {
                currentMinimumElement = matrix[i][firstColumn];
                currentMinimumElementRow = i;
                currentMinimumElementColumn = firstColumn;
            }

            if (matrix[i][endColumn] < currentMinimumElement) {
                currentMinimumElement = matrix[i][endColumn];
                currentMinimumElementRow = i;
                currentMinimumElementColumn = endColumn;
            }
        }

        //O(n) - check center row
        for(int i = firstColumn; i <= endColumn; i++) {
            if (matrix[centerRow][i] < currentMinimumElement) {
                currentMinimumElement = matrix[centerRow][i];
                currentMinimumElementRow = centerRow;
                currentMinimumElementColumn = i;
            }
        }

        //O(n) - check center column
        for(int i = firstRow; i <= endRow; i++) {
            if (matrix[i][centerColumn] < currentMinimumElement) {
                currentMinimumElement = matrix[i][centerColumn];
                currentMinimumElementRow = i;
                currentMinimumElementColumn = centerColumn;
            }
        }

        //Check if minimum element is a local minimum
        //If not, recurse

        if (currentMinimumElementRow - 1 >= 0
                && matrix[currentMinimumElementRow][currentMinimumElementColumn] > matrix[currentMinimumElementRow - 1][currentMinimumElementColumn]) {
            //Element above is smaller
            if (currentMinimumElementRow - 1 <= centerRow) {
                endRow = centerRow;
            } else {
                firstRow = centerRow;
            }
            if (currentMinimumElementColumn <= centerColumn) {
                endColumn = centerColumn;
            } else {
                firstColumn = centerColumn;
            }
            return localMinimumMatrix(matrix, firstRow, endRow, firstColumn, endColumn);
        } else if (currentMinimumElementRow + 1 < matrix.length
                && matrix[currentMinimumElementRow][currentMinimumElementColumn] > matrix[currentMinimumElementRow + 1][currentMinimumElementColumn]) {
            //Element below is smaller
            if (currentMinimumElementRow + 1 <= centerRow) {
                endRow = centerRow;
            } else {
                firstRow = centerRow;
            }
            if (currentMinimumElementColumn <= centerColumn) {
                endColumn = centerColumn;
            } else {
                firstColumn = centerColumn;
            }
            return localMinimumMatrix(matrix, firstRow, endRow, firstColumn, endColumn);
        } else if (currentMinimumElementColumn - 1 >= 0
                && matrix[currentMinimumElementRow][currentMinimumElementColumn] > matrix[currentMinimumElementRow][currentMinimumElementColumn - 1]) {
            //Element to the left is smaller
            if (currentMinimumElementRow <= centerRow) {
                endRow = centerRow;
            } else {
                firstRow = centerRow;
            }
            if (currentMinimumElementColumn - 1 <= centerColumn) {
                endColumn = centerColumn;
            } else {
                firstColumn = centerColumn;
            }
            return localMinimumMatrix(matrix, firstRow, endRow, firstColumn, endColumn);
        } else if (currentMinimumElementColumn + 1 < matrix[0].length
                && matrix[currentMinimumElementRow][currentMinimumElementColumn] > matrix[currentMinimumElementRow][currentMinimumElementColumn + 1]) {
            //Element to the right is smaller
            if (currentMinimumElementRow <= centerRow) {
                endRow = centerRow;
            } else {
                firstRow = centerRow;
            }
            if (currentMinimumElementColumn + 1 <= centerColumn) {
                endColumn = centerColumn;
            } else {
                firstColumn = centerColumn;
            }
            return localMinimumMatrix(matrix, firstRow, endRow, firstColumn, endColumn);
        } else {
            //The element is a local minimum
            return matrix[currentMinimumElementRow][currentMinimumElementColumn];
        }
    }
}
