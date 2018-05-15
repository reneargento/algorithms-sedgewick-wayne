/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed at
 * https://github.com/apache/commons-math/blob/master/NOTICE.txt for
 * additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Adapted from the Apache Commons library QRDecomposition class
// https://github.com/apache/commons-math/blob/master/src/main/java/org/apache/commons/math4/linear/QRDecomposition.java

package util.qrdecomposition;

import util.MatrixUtil;

import java.util.Arrays;

public class QRDecomposition {

    /**
     * A packed TRANSPOSED representation of the QR decomposition.
     * <p>The elements BELOW the diagonal are the elements of the UPPER triangular
     * matrix R, and the rows ABOVE the diagonal are the Householder reflector vectors
     * from which an explicit form of Q can be recomputed if desired.</p>
     */
    private double[][] qrt;
    /** The diagonal elements of R. */
    private double[] rDiagonalElements;
    /** Singularity threshold. */
    private final double threshold;

    /**
     * Calculates the QR-decomposition of the given matrix.
     * The singularity threshold defaults to zero.
     *
     * @param matrix The matrix to decompose.
     *
     * @see #QRDecomposition(double[][],double)
     */
    public QRDecomposition(double[][] matrix) {
        this(matrix, 0d);
    }

    /**
     * Calculates the QR-decomposition of the given matrix.
     *
     * @param matrix The matrix to decompose.
     * @param threshold Singularity threshold.
     * The matrix will be considered singular if the absolute value of
     * any of the diagonal elements of the "R" matrix is smaller than
     * the threshold.
     */
    public QRDecomposition(double[][] matrix, double threshold) {
        this.threshold = threshold;

        final int rows = matrix.length;
        final int columns = matrix[0].length;
        qrt = MatrixUtil.transpose(matrix);
        rDiagonalElements = new double[Math.min(rows, columns)];

        decompose(qrt);
    }

    /** Decompose matrix.
     * @param matrix transposed matrix
     */
    private void decompose(double[][] matrix) {
        for (int minor = 0; minor < Math.min(matrix.length, matrix[0].length); minor++) {
            performHouseholderReflection(minor, matrix);
        }
    }

    /** Perform Householder reflection for a minor matrix(minor, minor) of matrix.
     * @param minor minor index
     * @param matrix transposed matrix
     */
    private void performHouseholderReflection(int minor, double[][] matrix) {

        final double[] qrtMinor = matrix[minor];

        /*
         * Let x be the first column of the minor, and a^2 = |x|^2.
         * x will be in the positions qr[minor][minor] through qr[m][minor].
         * The first column of the transformed minor will be (a,0,0,..)'
         * The sign of a is chosen to be opposite to the sign of the first
         * component of x. Let's find a:
         */
        double xNormSqr = 0;

        for (int row = minor; row < qrtMinor.length; row++) {
            final double c = qrtMinor[row];
            xNormSqr += c * c;
        }
        final double a = (qrtMinor[minor] > 0) ? -Math.sqrt(xNormSqr) : Math.sqrt(xNormSqr);
        rDiagonalElements[minor] = a;

        if (a != 0.0) {

            /*
             * Calculate the normalized reflection vector v and transform
             * the first column. We know the norm of v beforehand: v = x-ae
             * so |v|^2 = <x-ae,x-ae> = <x,x>-2a<x,e>+a^2<e,e> =
             * a^2+a^2-2a<x,e> = 2a*(a - <x,e>).
             * Here <x, e> is now qr[minor][minor].
             * v = x-ae is stored in the column at qr:
             */
            qrtMinor[minor] -= a; // now |v|^2 = -2a*(qr[minor][minor])

            /*
             * Transform the rest of the columns of the minor:
             * They will be transformed by the matrix H = I-2vv'/|v|^2.
             * If x is a column vector of the minor, then
             * Hx = (I-2vv'/|v|^2)x = x-2vv'x/|v|^2 = x - 2<x,v>/|v|^2 v.
             * Therefore the transformation is easily calculated by
             * subtracting the column vector (2<x,v>/|v|^2)v from x.
             *
             * Let 2<x,v>/|v|^2 = alpha. From above we have
             * |v|^2 = -2a*(qr[minor][minor]), so
             * alpha = -<x,v>/(a*qr[minor][minor])
             */
            for (int column = minor + 1; column < matrix.length; column++) {
                final double[] qrtCol = matrix[column];
                double alpha = 0;
                for (int row = minor; row < qrtCol.length; row++) {
                    alpha -= qrtCol[row] * qrtMinor[row];
                }
                alpha /= a * qrtMinor[minor];

                // Subtract the column vector alpha*v from x.
                for (int row = minor; row < qrtCol.length; row++) {
                    qrtCol[row] -= alpha * qrtMinor[row];
                }
            }
        }
    }

    public DecompositionSolver getSolver() {
        return new Solver(qrt, rDiagonalElements, threshold);
    }

    /**
     * Interface handling decomposition algorithms that can solve A &times; X = B.
     * <p>
     * Decomposition algorithms decompose an A matrix that has a product of several specific
     * matrices from which they can solve A &times; X = B in least squares sense: they find X
     * such that ||A &times; X - B|| is minimal.
     */
    public interface DecompositionSolver {

        /**
         * Solve the linear equation A &times; X = B for matrices A.
         * <p>
         * The A matrix is implicit, it is provided by the underlying
         * decomposition algorithm.
         *
         * @param matrix right-hand side of the equation A &times; X = B
         * @return a matrix X that minimizes the two norm of A &times; X - B
         */
        double[][] solve(final double[][] matrix);

        /**
         * Get the <a href="http://en.wikipedia.org/wiki/Moore%E2%80%93Penrose_pseudoinverse">pseudo-inverse</a>
         * of the decomposed matrix.
         * <p>
         * <em>This is equal to the inverse of the decomposed matrix, if such an inverse exists.</em>
         * <p>
         * If no such inverse exists, then the result has properties that resemble that of an inverse.
         * <p>
         * In particular, in this case, if the decomposed matrix is A, then the system of equations
         * \( A x = b \) may have no solutions, or many. If it has no solutions, then the pseudo-inverse
         * \( A^+ \) gives the "closest" solution \( z = A^+ b \), meaning \( \left \| A z - b \right \|_2 \)
         * is minimized. If there are many solutions, then \( z = A^+ b \) is the smallest solution,
         * meaning \( \left \| z \right \|_2 \) is minimized.
         * <p>
         *
         * @return pseudo-inverse matrix (which is the inverse, if it exists),
         * if the decomposition can pseudo-invert the decomposed matrix
         */
        double[][] getInverse();
    }

    private static class Solver implements DecompositionSolver {
        /**
         * A packed TRANSPOSED representation of the QR decomposition.
         * <p>The elements BELOW the diagonal are the elements of the UPPER triangular
         * matrix R, and the rows ABOVE the diagonal are the Householder reflector vectors
         * from which an explicit form of Q can be recomputed if desired.</p>
         */
        private final double[][] qrt;
        /** The diagonal elements of R. */
        private final double[] rDiagonalElements;
        /** Singularity threshold. */
        private final double threshold;

        /**
         * Build a solver from decomposed matrix.
         *
         * @param qrt Packed TRANSPOSED representation of the QR decomposition.
         * @param rDiagonalElements Diagonal elements of R.
         * @param threshold Singularity threshold.
         */
        private Solver(final double[][] qrt,
                       final double[] rDiagonalElements,
                       final double threshold) {
            this.qrt   = qrt;
            this.rDiagonalElements = rDiagonalElements;
            this.threshold = threshold;
        }

        public boolean isNonSingular() {
            return !checkSingular(rDiagonalElements, threshold, false);
        }

        /**
         * Solve the linear equation A &times; X = B for matrices A.
         * <p>
         * The A matrix is implicit, it is provided by the underlying
         * decomposition algorithm.
         *
         * @param matrix right-hand side of the equation A &times; X = B
         * @return a matrix X that minimizes the two norm of A &times; X - B
         * if the matrices dimensions do not match.
         * @throws IllegalArgumentException if the decomposed matrix is singular.
         */
        public double[][] solve(final double[][] matrix) {
            final int qrtRows = qrt.length;
            final int qrtColumns = qrt[0].length;

            if (matrix.length != qrtColumns) {
                throw new IllegalArgumentException("Dimensions mismatch.");
            }
            checkSingular(rDiagonalElements, threshold, true);

            final int columns          = matrix[0].length;
            final int blockSize        = BlockMatrix.BLOCK_SIZE;
            final int cBlocks          = (columns + blockSize - 1) / blockSize;
            final double[][] xBlocks   = BlockMatrix.createBlocksLayout(qrtRows, columns);
            final double[][] newMatrix = new double[matrix.length][blockSize];
            final double[]   alpha     = new double[blockSize];

            for (int kBlock = 0; kBlock < cBlocks; ++kBlock) {
                final int kStart = kBlock * blockSize;
                final int kEnd   = Math.min(kStart + blockSize, columns);
                final int kWidth = kEnd - kStart;

                // get the right hand side vector
                MatrixUtil.copySubMatrix(matrix, 0, qrtColumns - 1, kStart, kEnd - 1, newMatrix);

                // apply Householder transforms to solve Q.newMatrix = matrix
                for (int minor = 0; minor < Math.min(qrtColumns, qrtRows); minor++) {
                    final double[] qrtMinor = qrt[minor];
                    final double factor     = 1.0 / (rDiagonalElements[minor] * qrtMinor[minor]);

                    Arrays.fill(alpha, 0, kWidth, 0.0);
                    for (int row = minor; row < qrtColumns; ++row) {
                        final double   d    = qrtMinor[row];
                        final double[] yRow = newMatrix[row];
                        for (int k = 0; k < kWidth; ++k) {
                            alpha[k] += d * yRow[k];
                        }
                    }
                    for (int k = 0; k < kWidth; ++k) {
                        alpha[k] *= factor;
                    }

                    for (int row = minor; row < qrtColumns; ++row) {
                        final double   d    = qrtMinor[row];
                        final double[] yRow = newMatrix[row];
                        for (int k = 0; k < kWidth; ++k) {
                            yRow[k] += alpha[k] * d;
                        }
                    }
                }

                // solve triangular system R.x = newMatrix
                for (int j = rDiagonalElements.length - 1; j >= 0; --j) {
                    final int      jBlock = j / blockSize;
                    final int      jStart = jBlock * blockSize;
                    final double   factor = 1.0 / rDiagonalElements[j];
                    final double[] yJ     = newMatrix[j];
                    final double[] xBlock = xBlocks[jBlock * cBlocks + kBlock];
                    int index = (j - jStart) * kWidth;

                    for (int k = 0; k < kWidth; ++k) {
                        yJ[k]          *= factor;
                        xBlock[index++] = yJ[k];
                    }

                    final double[] qrtJ = qrt[j];
                    for (int i = 0; i < j; ++i) {
                        final double rIJ  = qrtJ[i];
                        final double[] yI = newMatrix[i];
                        for (int k = 0; k < kWidth; ++k) {
                            yI[k] -= yJ[k] * rIJ;
                        }
                    }
                }
            }

            double[][] blockMatrix = BlockMatrix.createBlockMatrix(qrtRows, columns, xBlocks, false);
            return BlockMatrix.getData(blockMatrix);
        }

        /**
         * Get the <a href="http://en.wikipedia.org/wiki/Moore%E2%80%93Penrose_pseudoinverse">pseudo-inverse</a>
         * of the decomposed matrix.
         * <p>
         * <em>This is equal to the inverse of the decomposed matrix, if such an inverse exists.</em>
         * <p>
         * If no such inverse exists, then the result has properties that resemble that of an inverse.
         * <p>
         * In particular, in this case, if the decomposed matrix is A, then the system of equations
         * \( A x = b \) may have no solutions, or many. If it has no solutions, then the pseudo-inverse
         * \( A^+ \) gives the "closest" solution \( z = A^+ b \), meaning \( \left \| A z - b \right \|_2 \)
         * is minimized. If there are many solutions, then \( z = A^+ b \) is the smallest solution,
         * meaning \( \left \| z \right \|_2 \) is minimized.
         * <p>
         *
         * @return pseudo-inverse matrix (which is the inverse, if it exists),
         * if the decomposition can pseudo-invert the decomposed matrix
         */
        public double[][] getInverse() {
            return solve(MatrixUtil.createIdentityMatrix(qrt[0].length));
        }

        /**
         * Check singularity.
         *
         * @param diag Diagonal elements of the R matrix.
         * @param min Singularity threshold.
         * @param raise Whether to raise a {@link IllegalStateException}
         * if any element of the diagonal fails the check.
         * @return {@code true} if any element of the diagonal is smaller
         * or equal to {@code min}.
         * @throws IllegalStateException if the matrix is singular and
         * {@code raise} is {@code true}.
         */
        private static boolean checkSingular(double[] diag, double min, boolean raise) {
            final int len = diag.length;
            for (int i = 0; i < len; i++) {
                final double d = diag[i];

                if (Math.abs(d) <= min) {
                    if (raise) {
                        throw new IllegalStateException("Matrix is singular.");
                    } else {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}