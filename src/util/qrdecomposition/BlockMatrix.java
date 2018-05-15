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

// Adapted from the Apache Commons library BlockRealMatrix class
// https://github.com/apache/commons-math/blob/master/src/main/java/org/apache/commons/math4/linear/BlockRealMatrix.java

package util.qrdecomposition;

public class BlockMatrix {

    /** Block size. */
    public static final int BLOCK_SIZE = 52;
    /** Number of rows of the matrix. */
    private static int rows;
    /** Number of columns of the matrix. */
    private static int columns;
    /** Number of block rows of the matrix. */
    private static int blockRows;
    /** Number of block columns of the matrix. */
    private static int blockColumns;

    public static double[][] createBlocksLayout(final int rows, final int columns) {
        final int blockRows = (rows + BLOCK_SIZE - 1) / BLOCK_SIZE;
        final int blockColumns = (columns + BLOCK_SIZE - 1) / BLOCK_SIZE;

        final double[][] blocks = new double[blockRows * blockColumns][];
        int blockIndex = 0;

        for (int iBlock = 0; iBlock < blockRows; ++iBlock) {
            final int pStart = iBlock * BLOCK_SIZE;
            final int pEnd = Math.min(pStart + BLOCK_SIZE, rows);
            final int iHeight = pEnd - pStart;
            for (int jBlock = 0; jBlock < blockColumns; ++jBlock) {
                final int qStart = jBlock * BLOCK_SIZE;
                final int qEnd = Math.min(qStart + BLOCK_SIZE, columns);
                final int jWidth = qEnd - qStart;
                blocks[blockIndex] = new double[iHeight * jWidth];
                ++blockIndex;
            }
        }

        return blocks;
    }

    /**
     * Create a new dense matrix copying entries from block layout data.
     * <p>The input array <em>must</em> already be in blocks layout.</p>
     *
     * @param rows Number of rows in the new matrix.
     * @param columns Number of columns in the new matrix.
     * @param blockData data for new matrix
     * @param copyArray Whether the input array will be copied or referenced.
     */
    public static double[][] createBlockMatrix(final int rows, final int columns,
                                               final double[][] blockData, final boolean copyArray) {
        double blocks[][];

        BlockMatrix.rows = rows;
        BlockMatrix.columns = columns;

        blockRows = (rows + BLOCK_SIZE - 1) / BLOCK_SIZE;
        blockColumns = (columns + BLOCK_SIZE - 1) / BLOCK_SIZE;

        if (copyArray) {
            // allocate storage blocks, taking care of smaller ones at right and bottom
            blocks = new double[blockRows * blockColumns][];
        } else {
            // reference existing array
            blocks = blockData;
        }

        int index = 0;
        for (int iBlock = 0; iBlock < blockRows; ++iBlock) {
            final int iHeight = blockHeight(iBlock);

            for (int jBlock = 0; jBlock < blockColumns; ++jBlock, ++index) {
                if (blockData[index].length != iHeight * blockWidth(jBlock)) {
                    throw new IllegalStateException("The shape of blockData is inconsistent with block layout.");
                }
                if (copyArray) {
                    blocks[index] = blockData[index].clone();
                }
            }
        }

        return blocks;
    }

    public static double[][] getData(double[][] blocks) {
        final double[][] data = new double[rows][columns];
        final int lastColumns = columns - (blockColumns - 1) * BLOCK_SIZE;

        for (int iBlock = 0; iBlock < blockRows; ++iBlock) {
            final int pStart = iBlock * BLOCK_SIZE;
            final int pEnd = Math.min(pStart + BLOCK_SIZE, rows);
            int regularPos = 0;
            int lastPos = 0;
            for (int p = pStart; p < pEnd; ++p) {
                final double[] dataP = data[p];
                int blockIndex = iBlock * blockColumns;
                int dataPos = 0;
                for (int jBlock = 0; jBlock < blockColumns - 1; ++jBlock) {
                    System.arraycopy(blocks[blockIndex++], regularPos, dataP, dataPos, BLOCK_SIZE);
                    dataPos += BLOCK_SIZE;
                }
                System.arraycopy(blocks[blockIndex], lastPos, dataP, dataPos, lastColumns);
                regularPos += BLOCK_SIZE;
                lastPos    += lastColumns;
            }
        }

        return data;
    }

    /**
     * Get the height of a block.
     * @param blockRow row index (in block sense) of the block
     * @return height (number of rows) of the block
     */
    private static int blockHeight(final int blockRow) {
        return (blockRow == blockRows - 1) ? rows - blockRow * BLOCK_SIZE : BLOCK_SIZE;
    }

    /**
     * Get the width of a block.
     * @param blockColumn column index (in block sense) of the block
     * @return width (number of columns) of the block
     */
    private static int blockWidth(final int blockColumn) {
        return (blockColumn == blockColumns - 1) ? columns - blockColumn * BLOCK_SIZE : BLOCK_SIZE;
    }

}