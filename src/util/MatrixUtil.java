package util;

import util.qrdecomposition.QRDecomposition;

/**
 * Created by Rene Argento on 29/04/18.
 */
public class MatrixUtil {

    public static double[][] transpose(double[][] matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }

        double[][] transposedMatrix = new double[matrix[0].length][matrix.length];

        for (int row = 0; row < matrix.length; row++) {
            for(int column = 0; column < matrix[0].length; column++) {
                transposedMatrix[column][row] = matrix[row][column];
            }
        }

        return transposedMatrix;
    }

    public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
        if (matrix1[0].length != matrix2.length) {
            throw new IllegalArgumentException("Matrix 1 number of columns must be the same as matrix 2 number of rows");
        }

        double[][] result = new double[matrix1.length][matrix2[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {

                double sum = 0;

                for (int k = 0; k < matrix1[i].length; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }

                result[i][j] = sum;
            }
        }

        return result;
    }

    public static double[][] multiplyByConstant(double[][] matrix, double constant) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }

        double[][] result = new double[matrix.length][matrix[0].length];

        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                result[row][column] = matrix[row][column] * constant;
            }
        }

        return result;
    }

    public static double[][] inverse(double[][] matrix) {

        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Matrix must be square.");
        }

        QRDecomposition decomposition = new QRDecomposition(matrix, 0);
        return decomposition.getSolver().getInverse();
    }


    public static double[][] createIdentityMatrix(int dimension) {
        final double[][] matrix = new double[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            matrix[i][i] = 1.0;
        }
        return matrix;
    }

    public static void copySubMatrix(double[][] originalMatrix, int startRow, int endRow,
                                     int startColumn, int endColumn, double[][] destinationMatrix) {
        if (originalMatrix == null || destinationMatrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }

        if (startRow >= originalMatrix.length || startColumn >= originalMatrix[0].length) {
            throw new IllegalArgumentException("Invalid indexes.");
        }

        for (int row = startRow; row <= endRow; row++) {
            int destinationMatrixRow = row - startRow;

            if (destinationMatrixRow >= destinationMatrix.length) {
                break;
            }

            for (int column = startColumn; column <= endColumn; column++) {
                int destinationMatrixColumn = column - startColumn;

                if (destinationMatrixColumn >= destinationMatrix[0].length) {
                    break;
                }

                destinationMatrix[destinationMatrixRow][destinationMatrixColumn] = originalMatrix[row][column];
            }
        }
    }

}
