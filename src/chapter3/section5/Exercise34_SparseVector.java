package chapter3.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Rene Argento on 20/08/17.
 */
public class Exercise34_SparseVector {

    private void doExperiment() {

        int size = 10000;

        double[][] arrayMatrixManyNonZeroes = new double[size][size];
        double[][] arrayMatrixManyZeroes = new double[size][size];

        double[] arrayVectorManyNonZeroes = new double[size];
        double[] arrayVectorManyZeroes = new double[size];

        SparseMatrix sparseMatrixManyNonZeroes = new SparseMatrix(size, size);
        SparseMatrix sparseMatrixManyZeroes = new SparseMatrix(size, size);

        SparseVector sparseVectorManyNonZeroes = new SparseVector(size);
        SparseVector sparseVectorManyZeroes = new SparseVector(size);

        //**************** Populate matrices ****************
        //Most values are nonzero
        for(int row = 0; row < size; row++) {
            for(int column = 0; column < size; column++) {
                int isZero = StdRandom.uniform(100);

                //95% chance of being nonzero
                boolean isNonzeroValue = isZero <= 94;

                double value = 0;

                if (isNonzeroValue) {
                    value = StdRandom.uniform();
                }

                arrayMatrixManyNonZeroes[row][column] = value;
                sparseMatrixManyNonZeroes.put(row, column, value);
            }
        }

        //Most values are zero
        for(int row = 0; row < size; row++) {
            for(int column = 0; column < size; column++) {
                int isZero = StdRandom.uniform(100);

                //5% chance of being nonzero
                boolean isNonzeroValue = isZero >= 95;

                double value = 0;

                if (isNonzeroValue) {
                    value = StdRandom.uniform();
                }

                arrayMatrixManyZeroes[row][column] = value;
                sparseMatrixManyZeroes.put(row, column, value);
            }
        }

        //**************** Populate vectors ****************
        //Most values are nonzero
        for(int column = 0; column < size; column++) {
            int isZero = StdRandom.uniform(100);

            //95% chance of being nonzero
            boolean isNonzeroValue = isZero <= 94;

            double value = 0;

            if (isNonzeroValue) {
                value = StdRandom.uniform();
            }

            arrayVectorManyNonZeroes[column] = value;
            sparseVectorManyNonZeroes.put(column, value);
        }

        //Most values are zero
        for(int column = 0; column < size; column++) {
            int isZero = StdRandom.uniform(100);

            //5% chance of being nonzero
            boolean isNonzeroValue = isZero >= 95;

            double value = 0;

            if (isNonzeroValue) {
                value = StdRandom.uniform();
            }

            arrayVectorManyZeroes[column] = value;
            sparseVectorManyZeroes.put(column, value);
        }

        StdOut.printf("%17s %18s %20s %10s\n", "Method | ", "Matrix type | ", "Vector type | ", "Time spent");
        String[] methods = {"Arrays", "Sparse Vectors"};
        String[] types = {"Many non-zeroes", "Many zeroes"};

        //Array matrix many non-zeroes X array vector many non-zeroes
        Stopwatch stopwatch = new Stopwatch();
        dot(arrayMatrixManyNonZeroes, arrayVectorManyNonZeroes);
        double timeSpent = stopwatch.elapsedTime();
        printResults(methods[0], types[0], types[0], timeSpent);

        //Array matrix many non-zeroes X array vector many zeroes
        stopwatch = new Stopwatch();
        dot(arrayMatrixManyNonZeroes, arrayVectorManyZeroes);
        timeSpent = stopwatch.elapsedTime();
        printResults(methods[0], types[0], types[1], timeSpent);

        //Array matrix many zeroes X array vector many non-zeroes
        stopwatch = new Stopwatch();
        dot(arrayMatrixManyZeroes, arrayVectorManyNonZeroes);
        timeSpent = stopwatch.elapsedTime();
        printResults(methods[0], types[1], types[0], timeSpent);

        //Array matrix many zeroes X array vector many zeroes
        stopwatch = new Stopwatch();
        dot(arrayMatrixManyZeroes, arrayVectorManyZeroes);
        timeSpent = stopwatch.elapsedTime();
        printResults(methods[0], types[1], types[1], timeSpent);

        //Sparse matrix many non-zeroes X sparse vector many non-zeroes
        stopwatch = new Stopwatch();
        sparseMatrixManyNonZeroes.dot(sparseVectorManyNonZeroes);
        timeSpent = stopwatch.elapsedTime();
        printResults(methods[1], types[0], types[0], timeSpent);

        //Sparse matrix many non-zeroes X sparse vector many zeroes
        stopwatch = new Stopwatch();
        sparseMatrixManyNonZeroes.dot(sparseVectorManyZeroes);
        timeSpent = stopwatch.elapsedTime();
        printResults(methods[1], types[0], types[1], timeSpent);

        //Sparse matrix many zeroes X sparse vector many non-zeroes
        stopwatch = new Stopwatch();
        sparseMatrixManyZeroes.dot(sparseVectorManyNonZeroes);
        timeSpent = stopwatch.elapsedTime();
        printResults(methods[1], types[1], types[0], timeSpent);

        //Sparse matrix many zeroes X sparse vector many zeroes
        stopwatch = new Stopwatch();
        sparseMatrixManyZeroes.dot(sparseVectorManyZeroes);
        timeSpent = stopwatch.elapsedTime();
        printResults(methods[1], types[1], types[1], timeSpent);
    }

    private double[] dot(double[][] matrix, double[] array) {
        if (matrix[0].length != array.length) {
            throw new IllegalArgumentException("Matrix columns number and vector dimension must match");
        }

        double[] result = new double[matrix.length];

        for(int row = 0; row < matrix.length; row++) {
            double dot = 0;

            for(int column = 0; column < matrix[0].length; column++) {
                dot += matrix[row][column] * array[column];
            }

            result[row] = dot;
        }

        return result;
    }

    private void printResults(String method, String matrixType, String vectorType, double timeSpent) {
        StdOut.printf("%14s %18s %20s %13.2f\n", method, matrixType, vectorType, timeSpent);
    }

    public static void main(String[] args) {
        new Exercise34_SparseVector().doExperiment();
    }

}
