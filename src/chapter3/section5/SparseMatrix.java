package chapter3.section5;

/**
 * Created by Rene Argento on 17/08/17.
 */
public class SparseMatrix {

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

    public double get(int row, int column) {
        if (row < 0 || row >= rowSize) {
            throw new IllegalArgumentException("Invalid row index");
        }
        if (column < 0 || column >= columnSize) {
            throw new IllegalArgumentException("Invalid column index");
        }

        return rows[row].get(column);
    }

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


    public SparseMatrix sum(SparseMatrix sparseMatrix) {
        if (rowSize != sparseMatrix.rowSize || columnSize != sparseMatrix.columnSize) {
            throw new IllegalArgumentException("Matrix A rows and columns number and Matrix B rows and columns " +
                    "number must match");
        }

        SparseMatrix result = new SparseMatrix(rowSize, columnSize);

        for (int i = 0; i < result.rowSize; i++) {
            result.rows[i] = rows[i].plus(sparseMatrix.rows[i]);
        }

        return result;
    }

    //Matrix-matrix multiplication
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

    //Matrix-vector multiplication
    public SparseVector dot(SparseVector sparseVector) {
        if (columnSize != sparseVector.dimension) {
            throw new IllegalArgumentException("Matrix columns number and vector dimension must match");
        }

        SparseVector result = new SparseVector(rowSize);

        for(int i = 0; i < rows.length; i++) {
            double dot = rows[i].dot(sparseVector);

            if (dot != 0) {
                result.put(i, dot);
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
