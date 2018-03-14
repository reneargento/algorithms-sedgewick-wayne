package chapter3.section5;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 15/08/17.
 */
public class Exercise23_SparseMatrices {

    private interface SparseMatrixAPI {
        SparseMatrix sum(SparseMatrix sparseMatrix);
        SparseMatrix dot(SparseMatrix sparseMatrix);
        void put(int row, int column, double value);
        double get(int row, int column);
        void delete(int row, int column);
    }

    public class SparseVector {

        private int dimension;
        private SeparateChainingHashTable<Integer, Double> hashTable;

        public SparseVector(int dimension) {
            hashTable = new SeparateChainingHashTable<>();
            this.dimension = dimension;
        }

        public int size() {
            return hashTable.size();
        }

        public void put(int key, double value) {
            hashTable.put(key, value);
        }

        public double get(int key) {
            if (!hashTable.contains(key)) {
                return 0;
            } else {
                return hashTable.get(key);
            }
        }

        public void delete(int key) {
            hashTable.delete(key);
        }

        public SparseVector plus(SparseVector sparseVector) {
            if (dimension != sparseVector.dimension) {
                throw new IllegalArgumentException("Sparse vector dimensions must be the same.");
            }

            SparseVector result = new SparseVector(dimension);

            //Copy values
            for(int key : hashTable.keys()) {
                result.put(key, get(key));
            }
            //Sum values
            for(int key : sparseVector.hashTable.keys()) {
                double sum = get(key) + sparseVector.get(key);

                if (sum != 0) {
                    result.put(key, sum);
                } else {
                    result.delete(key);
                }
            }

            return result;
        }

        public double dot(SparseVector sparseVector) {
            if (dimension != sparseVector.dimension) {
                throw new IllegalArgumentException("Sparse vector dimensions must be the same.");
            }

            double sum = 0;

            //Iterate over the vector with the fewest nonzeros
            if (size() <= sparseVector.size()) {
                for(int key : hashTable.keys()) {
                    if (sparseVector.hashTable.contains(key)) {
                        sum += get(key) * sparseVector.get(key);
                    }
                }
            } else {
                for(int key : sparseVector.hashTable.keys()) {
                    if (hashTable.contains(key)) {
                        sum += get(key) * sparseVector.get(key);
                    }
                }
            }

            return sum;
        }

        public double dot(double[] that) {
            double sum = 0.0;

            for(int key : hashTable.keys()) {
                sum += this.get(key) * that[key];
            }

            return sum;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for (int key : hashTable.keys()) {
                stringBuilder.append("(").append(key).append(", ").append(get(key)).append(") ");
            }

            return stringBuilder.toString();
        }
    }

    public class SparseMatrix implements SparseMatrixAPI {

        private int rowSize;
        private int columnSize;
        private SparseVector[] rows;
        private SparseVector[] columns;

        SparseMatrix(SparseVector[] rows, SparseVector[] columns, int rowSize, int columnSize) {
            this.rows = rows;
            this.columns = columns;

            this.rowSize = rowSize;
            this.columnSize = columnSize;
        }

        SparseMatrix(int rowSize, int columnSize) {
            this.rowSize = rowSize;
            this.columnSize = columnSize;

            rows = new SparseVector[rowSize];
            for(int i = 0; i < rows.length; i++) {
                rows[i] = new SparseVector(columnSize);
            }

            columns = new SparseVector[columnSize];
            for(int i = 0; i < columns.length; i++) {
                columns[i] = new SparseVector(rowSize);
            }
        }

        @Override
        public double get(int row, int column) {
            if (row < 0 || row >= rowSize) {
                throw new IllegalArgumentException("Invalid row index");
            }
            if (column < 0 || column >= columnSize) {
                throw new IllegalArgumentException("Invalid column index");
            }

            return rows[row].get(column);
        }

        @Override
        public void put(int row, int column, double value) {
            if (row < 0 || row >= rowSize) {
                throw new IllegalArgumentException("Invalid row index");
            }
            if (column < 0 || column >= columnSize) {
                throw new IllegalArgumentException("Invalid column index");
            }

            if (value == 0) {
                delete(row, column);
                return;
            }

            rows[row].put(column, value);
            columns[column].put(row, value);
        }

        @Override
        public void delete(int row, int column) {
            if (row < 0 || row >= rowSize) {
                throw new IllegalArgumentException("Invalid row index");
            }
            if (column < 0 || column >= columnSize) {
                throw new IllegalArgumentException("Invalid column index");
            }

            rows[row].delete(column);
            columns[column].delete(row);
        }

        @Override
        public SparseMatrix sum(SparseMatrix sparseMatrix) {
            if (rowSize != sparseMatrix.rowSize || columnSize != sparseMatrix.columnSize) {
                throw new IllegalArgumentException("Matrix A rows and columns number and Matrix B rows and columns " +
                        "number must match");
            }

            SparseMatrix result = new SparseMatrix(rowSize, columnSize);

            for(int i = 0; i < result.rowSize; i++) {
                result.rows[i] = rows[i].plus(sparseMatrix.rows[i]);
            }

            return result;
        }

        @Override
        public SparseMatrix dot(SparseMatrix sparseMatrix) {
            if (columnSize != sparseMatrix.rowSize) {
                throw new IllegalArgumentException("Matrix A columns number and Matrix B rows number must match");
            }

            SparseMatrix result = new SparseMatrix(rowSize, sparseMatrix.columnSize);

            for(int i = 0; i < rows.length; i++) {
                for(int j = 0; j < sparseMatrix.columnSize; j++) {
                    double dot = rows[i].dot(sparseMatrix.columns[j]);

                    if (dot != 0) {
                        result.put(i, j, dot);
                    }
                }
            }

            return result;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("rows = " + rowSize + ", columns = " + columnSize + "\n");

            for (int row = 0; row < rowSize; row++) {
                stringBuilder.append(row).append(": ").append(rows[row]).append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise23_SparseMatrices sparseMatrices = new Exercise23_SparseMatrices();

        //Matrix A
        // 1 0
        // 7 2

        //Matrix B
        // -4 -5
        //  2  1

        //Matrix C
        // 3 4 2
        // 1 0 3

        //Matrix D
        // 0 0 0
        // 0 2 0

        //Matrix A + Matrix B
        // -3 -5
        //  9  3

        //Matrix A x Matrix C
        //  3  4  2
        // 23 28 20

        //Matrix B x Matrix C
        // -17 -16 -23
        //   7   8   7

        //Matrix C + Matrix D
        // 3 4 2
        // 1 2 3

        //Matrix A x Matrix D
        // 0 0 0
        // 0 4 0

        //Matrix B x Matrix D
        // 0 -10 0
        // 0   2 0

        SparseMatrix sparseMatrixA = sparseMatrices.new SparseMatrix(2, 2);
        SparseMatrix sparseMatrixB = sparseMatrices.new SparseMatrix(2, 2);
        SparseMatrix sparseMatrixC = sparseMatrices.new SparseMatrix(2, 3);
        SparseMatrix sparseMatrixD = sparseMatrices.new SparseMatrix(2, 3);

        sparseMatrixA.put(0, 0, 1);
        sparseMatrixA.put(1, 0, 7);
        sparseMatrixA.put(1, 1, 2);

        sparseMatrixB.put(0, 0, -4);
        sparseMatrixB.put(0, 1, -5);
        sparseMatrixB.put(1, 0, 2);
        sparseMatrixB.put(1, 1, 1);

        sparseMatrixC.put(0, 0, 3);
        sparseMatrixC.put(0, 1, 4);
        sparseMatrixC.put(0, 2, 2);
        sparseMatrixC.put(1, 0, 1);
        sparseMatrixC.put(1, 2, 3);

        sparseMatrixD.put(1, 1, 2);

        StdOut.println("Matrix A + Matrix B");
        SparseMatrix sparseMatrixAPlusB = sparseMatrixA.sum(sparseMatrixB);
        StdOut.println(sparseMatrixAPlusB);
        StdOut.println("Expected:\n" +
                "rows = 2, columns = 2\n" +
                "0: (0, -3.0) (1, -5.0) \n" +
                "1: (0, 9.0) (1, 3.0) \n");

        StdOut.println("Matrix A x Matrix C");
        SparseMatrix sparseMatrixADotC = sparseMatrixA.dot(sparseMatrixC);
        StdOut.println(sparseMatrixADotC);
        StdOut.println("Expected:\n" +
                "rows = 2, columns = 3\n" +
                "0: (0, 3.0) (1, 4.0) (2, 2.0) \n" +
                "1: (0, 23.0) (1, 28.0) (2, 20.0) \n");

        StdOut.println("Matrix B x Matrix C");
        SparseMatrix sparseMatrixBDotC = sparseMatrixB.dot(sparseMatrixC);
        StdOut.println(sparseMatrixBDotC);
        StdOut.println("Expected:\n" +
                "rows = 2, columns = 3\n" +
                "0: (0, -17.0) (1, -16.0) (2, -23.0) \n" +
                "1: (0, 7.0) (1, 8.0) (2, 7.0) \n");

        StdOut.println("Matrix C + Matrix D");
        SparseMatrix sparseMatrixCPlusD = sparseMatrixC.sum(sparseMatrixD);
        StdOut.println(sparseMatrixCPlusD);
        StdOut.println("Expected:\n" +
                "rows = 2, columns = 3\n" +
                "0: (0, 3.0) (1, 4.0) (2, 2.0) \n" +
                "1: (0, 1.0) (1, 2.0) (2, 3.0) \n");

        StdOut.println("Matrix A x Matrix D");
        SparseMatrix sparseMatrixADotD = sparseMatrixA.dot(sparseMatrixD);
        StdOut.println(sparseMatrixADotD);
        StdOut.println("Expected:\n" +
                "rows = 2, columns = 3\n" +
                "0: \n" +
                "1: (1, 4.0) \n");

        StdOut.println("Matrix B x Matrix D");
        SparseMatrix sparseMatrixBDotD = sparseMatrixB.dot(sparseMatrixD);
        StdOut.println(sparseMatrixBDotD);
        StdOut.println("Expected:\n" +
                "rows = 2, columns = 3\n" +
                "0: (1, -10.0) \n" +
                "1: (1, 2.0)");
    }

}
