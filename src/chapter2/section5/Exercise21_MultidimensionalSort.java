package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 13/04/17.
 */
public class Exercise21_MultidimensionalSort {

    private enum Dimensions {
        OneD, TwoD, ThreeD, FourD, FiveD;
    }

    private class Vector implements Comparable<Vector>{

        private Dimensions dimension;
        private int[] values1d;
        private int[][] values2d;
        private int[][][] values3d;
        private int[][][][] values4d;
        private int[][][][][] values5d;

        //Constructors
        Vector(int[] values1d) {
            if(values1d == null) {
                throw new IllegalArgumentException("Array cannot be null");
            }

            this.values1d = values1d;
            this.dimension = Dimensions.OneD;
        }

        Vector(int[][] values2d) {
            if(values2d == null) {
                throw new IllegalArgumentException("Array cannot be null");
            }

            this.values2d = values2d;
            this.dimension = Dimensions.TwoD;
        }

        Vector(int[][][] values3d) {
            if(values3d == null) {
                throw new IllegalArgumentException("Array cannot be null");
            }

            this.values3d = values3d;
            this.dimension = Dimensions.ThreeD;
        }

        Vector(int[][][][] values4d) {
            if(values4d == null) {
                throw new IllegalArgumentException("Array cannot be null");
            }

            this.values4d = values4d;
            this.dimension = Dimensions.FourD;
        }

        Vector(int[][][][][] values5d) {
            if(values5d == null) {
                throw new IllegalArgumentException("Array cannot be null");
            }

            this.values5d = values5d;
            this.dimension = Dimensions.FiveD;
        }

        @Override
        public int compareTo(Vector that) {
            int compare = 0;

            switch (dimension) {
                case OneD: compare = compare1D(that); break;
                case TwoD: compare = compare2D(that); break;
                case ThreeD: compare = compare3D(that); break;
                case FourD: compare = compare4D(that); break;
                case FiveD: compare = compare5D(that); break;
            }

            return compare;
        }

        private int compare1D(Vector that) {
            if(this.values1d == null || that.values1d == null) {
                throw new IllegalArgumentException("Both vectors must exist");
            }

            int smallestSize = Math.min(this.values1d.length, that.values1d.length);

            for(int i=0; i < smallestSize; i++) {
                if(this.values1d[i] < that.values1d[i]) {
                    return -1;
                } else if(this.values1d[i] > that.values1d[i]) {
                    return 1;
                }
            }

            if(this.values1d.length < that.values1d.length) {
                return -1;
            } else if(this.values1d.length > that.values1d.length) {
                return 1;
            }

            return 0;
        }

        private int compare2D(Vector that) {
            if(this.values2d == null || that.values2d == null) {
                throw new IllegalArgumentException("Both vectors must exist");
            }

            int smallestSize1d = Math.min(this.values2d.length, that.values2d.length);
            int smallestSize2d = Math.min(this.values2d[0].length, that.values2d[0].length);

            for(int i=0; i < smallestSize1d; i++) {
                for(int j=0; j < smallestSize2d; j++) {
                    if(this.values2d[i][j] < that.values2d[i][j]) {
                        return -1;
                    } else if(this.values2d[i][j] > that.values2d[i][j]) {
                        return 1;
                    }
                }
            }

            if(this.values2d.length < that.values2d.length) {
                return -1;
            } else if(this.values2d.length > that.values2d.length) {
                return 1;
            }
            for (int j = 0; j < smallestSize2d; j++) {
                if(this.values2d[j].length < that.values2d[j].length) {
                    return -1;
                } else if(this.values2d[j].length > that.values2d[j].length) {
                    return 1;
                }
            }

            return 0;
        }

        private int compare3D(Vector that) {
            if(this.values3d == null || that.values3d == null) {
                throw new IllegalArgumentException("Both vectors must exist");
            }

            int smallestSize1d = Math.min(this.values3d.length, that.values3d.length);
            int smallestSize2d = Math.min(this.values3d[0].length, that.values3d[0].length);
            int smallestSize3d = Math.min(this.values3d[0][0].length, that.values3d[0][0].length);

            for(int i=0; i < smallestSize1d; i++) {
                for(int j=0; j < smallestSize2d; j++) {
                    for(int k=0; k < smallestSize3d; k++) {
                        if(this.values3d[i][j][k] < that.values3d[i][j][k]) {
                            return -1;
                        } else if(this.values3d[i][j][k] > that.values3d[i][j][k]) {
                            return 1;
                        }
                    }
                }
            }

            if(this.values3d.length < that.values3d.length) {
                return -1;
            } else if(this.values3d.length > that.values3d.length) {
                return 1;
            }
            for (int j = 0; j < smallestSize2d; j++) {
                if(this.values3d[j].length < that.values3d[j].length) {
                    return -1;
                } else if(this.values3d[j].length > that.values3d[j].length) {
                    return 1;
                }
            }
            for (int k = 0; k < smallestSize3d; k++) {
                if(this.values3d[0][k].length < that.values3d[0][k].length) {
                    return -1;
                } else if(this.values3d[0][k].length > that.values3d[0][k].length) {
                    return 1;
                }
            }

            return 0;
        }

        private int compare4D(Vector that) {
            if(this.values4d == null || that.values4d == null) {
                throw new IllegalArgumentException("Both vectors must exist");
            }

            int smallestSize1d = Math.min(this.values4d.length, that.values4d.length);
            int smallestSize2d = Math.min(this.values4d[0].length, that.values4d[0].length);
            int smallestSize3d = Math.min(this.values4d[0][0].length, that.values4d[0][0].length);
            int smallestSize4d = Math.min(this.values4d[0][0][0].length, that.values4d[0][0][0].length);

            for(int i=0; i < smallestSize1d; i++) {
                for(int j=0; j < smallestSize2d; j++) {
                    for(int k=0; k < smallestSize3d; k++) {
                        for(int l=0; l < smallestSize4d; l++) {
                            if(this.values4d[i][j][k][l] < that.values4d[i][j][k][l]) {
                                return -1;
                            } else if(this.values4d[i][j][k][l] > that.values4d[i][j][k][l]) {
                                return 1;
                            }
                        }
                    }
                }
            }

            if(this.values4d.length < that.values4d.length) {
                return -1;
            } else if(this.values4d.length > that.values4d.length) {
                return 1;
            }
            for (int j = 0; j < smallestSize2d; j++) {
                if(this.values4d[j].length < that.values4d[j].length) {
                    return -1;
                } else if(this.values4d[j].length > that.values4d[j].length) {
                    return 1;
                }
            }
            for (int k = 0; k < smallestSize3d; k++) {
                if(this.values4d[0][k].length < that.values4d[0][k].length) {
                    return -1;
                } else if(this.values4d[0][k].length > that.values4d[0][k].length) {
                    return 1;
                }
            }
            for (int l = 0; l < smallestSize4d; l++) {
                if(this.values4d[0][0][l].length < that.values4d[0][0][l].length) {
                    return -1;
                } else if(this.values4d[0][0][l].length > that.values4d[0][0][l].length) {
                    return 1;
                }
            }

            return 0;
        }

        private int compare5D(Vector that) {
            if(this.values5d == null || that.values5d == null) {
                throw new IllegalArgumentException("Both vectors must exist");
            }

            int smallestSize1d = Math.min(this.values5d.length, that.values5d.length);
            int smallestSize2d = Math.min(this.values5d[0].length, that.values5d[0].length);
            int smallestSize3d = Math.min(this.values5d[0][0].length, that.values5d[0][0].length);
            int smallestSize4d = Math.min(this.values5d[0][0][0].length, that.values5d[0][0][0].length);
            int smallestSize5d = Math.min(this.values5d[0][0][0][0].length, that.values5d[0][0][0][0].length);

            for(int i=0; i < smallestSize1d; i++) {
                for(int j=0; j < smallestSize2d; j++) {
                    for(int k=0; k < smallestSize3d; k++) {
                        for(int l=0; l < smallestSize4d; l++) {
                            for(int m=0; m < smallestSize5d; m++) {
                                if(this.values5d[i][j][k][l][m] < that.values5d[i][j][k][l][m]) {
                                    return -1;
                                } else if(this.values5d[i][j][k][l][m] > that.values5d[i][j][k][l][m]) {
                                    return 1;
                                }
                            }
                        }
                    }
                }
            }

            if(this.values5d.length < that.values5d.length) {
                return -1;
            } else if(this.values5d.length > that.values5d.length) {
                return 1;
            }
            for (int j = 0; j < smallestSize2d; j++) {
                if(this.values5d[j].length < that.values5d[j].length) {
                    return -1;
                } else if(this.values5d[j].length > that.values5d[j].length) {
                    return 1;
                }
            }
            for (int k = 0; k < smallestSize3d; k++) {
                if(this.values5d[0][k].length < that.values5d[0][k].length) {
                    return -1;
                } else if(this.values5d[0][k].length > that.values5d[0][k].length) {
                    return 1;
                }
            }
            for (int l = 0; l < smallestSize4d; l++) {
                if(this.values5d[0][0][l].length < that.values5d[0][0][l].length) {
                    return -1;
                } else if(this.values5d[0][0][l].length > that.values5d[0][0][l].length) {
                    return 1;
                }
            }
            for (int m = 0; m < smallestSize5d; m++) {
                if(this.values5d[0][0][0][m].length < that.values5d[0][0][0][m].length) {
                    return -1;
                } else if(this.values5d[0][0][0][m].length > that.values5d[0][0][0][m].length) {
                    return 1;
                }
            }

            return 0;
        }
    }

    public static void main(String[] args) {

        Exercise21_MultidimensionalSort multidimensionalSort = new Exercise21_MultidimensionalSort();

        //1 dimension
        int[] array1 = {5, 9, 1, 3, 4};
        int[] array2 = {5, 9, 1, 3};
        int[] array3 = {-1, 99, 2};
        int[] array4 = {99, 10, 1};
        int[] array5 = {2, 3, 4, 5, 6, 7, 8, 9};

        Vector vector1 = multidimensionalSort.new Vector(array1);
        Vector vector2 = multidimensionalSort.new Vector(array2);
        Vector vector3 = multidimensionalSort.new Vector(array3);
        Vector vector4 = multidimensionalSort.new Vector(array4);
        Vector vector5 = multidimensionalSort.new Vector(array5);

        Vector[] vectors = {vector1, vector2, vector3, vector4, vector5};

        Arrays.sort(vectors);

        for(int i=0; i < vectors.length; i++) {
            StdOut.print(vectors[i].values1d[0] + " ");
        }

        StdOut.println("\nExpected: -1 2 5 5 99");

        //2 dimensions
        int[][] array2d1 = {{9, 3}, {1, 2}};
        int[][] array2d2 = {{1, 2}, {5, 6, 6}};

        Vector vector2d1 = multidimensionalSort.new Vector(array2d1);
        Vector vector2d2 = multidimensionalSort.new Vector(array2d2);

        Vector[] vectors2d = {vector2d1, vector2d2};

        Arrays.sort(vectors2d);

        for(int i=0; i < vectors2d.length; i++) {
            StdOut.print(vectors2d[i].values2d[0][0] + " ");
        }

        StdOut.println("\nExpected: 1 9");

        //3 dimensions
        int[][][] array3d1 = {{{9, 2, 1}}, {{2, 3, 4}, {8, 9 ,2}}};
        int[][][] array3d2 = {{{7, 1, 3}}, {{2, 3, 4}, {8, 9 ,2}}};
        int[][][] array3d3 = {{{1, 3}}, {{2, 3, 4}, {8, 9 ,2}}};

        Vector vector3d1 = multidimensionalSort.new Vector(array3d1);
        Vector vector3d2 = multidimensionalSort.new Vector(array3d2);
        Vector vector3d3 = multidimensionalSort.new Vector(array3d3);

        Vector[] vectors3d = {vector3d1, vector3d2, vector3d3};

        Arrays.sort(vectors3d);

        for(int i=0; i < vectors3d.length; i++) {
            StdOut.print(vectors3d[i].values3d[0][0][0] + " ");
        }

        StdOut.println("\nExpected: 1 7 9");

        //4 dimensions
        int[][][][] array4d1 = {{{{1, 3}, {1, 2}}}, {{{1, 3}, {1, 2}}}};
        int[][][][] array4d2 = {{{{1, 1}, {2, 2}}}};
        int[][][][] array4d3 = {{{{2, 2}, {9, 9}}}};

        Vector vector4d1 = multidimensionalSort.new Vector(array4d1);
        Vector vector4d2 = multidimensionalSort.new Vector(array4d2);
        Vector vector4d3 = multidimensionalSort.new Vector(array4d3);

        Vector[] vectors4d = {vector4d1, vector4d2, vector4d3};

        Arrays.sort(vectors4d);

        for(int i=0; i < vectors4d.length; i++) {
            StdOut.print(vectors4d[i].values4d[0][0][0][0] + " ");
        }

        StdOut.println("\nExpected: 1 1 2");

        //5 dimensions
        int[][][][][] array5d1 = {{{{{99, 99, 99}}, {{7, 7}}}}, {{{{11, 11, 12}}}}};
        int[][][][][] array5d2 = {{{{{1, 2, 3}}, {{4, 5, 6}}}}};

        Vector vector5d1 = multidimensionalSort.new Vector(array5d1);
        Vector vector5d2 = multidimensionalSort.new Vector(array5d2);

        Vector[] vectors5d = {vector5d1, vector5d2};

        Arrays.sort(vectors5d);

        for(int i=0; i < vectors5d.length; i++) {
            StdOut.print(vectors5d[i].values5d[0][0][0][0][0] + " ");
        }

        StdOut.println("\nExpected: 1 99");
    }

}
