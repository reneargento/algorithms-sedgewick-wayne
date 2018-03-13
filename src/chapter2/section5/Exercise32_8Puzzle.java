package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.*;

/**
 * Created by Rene Argento on 17/04/17.
 */
public class Exercise32_8Puzzle {

    private enum Heuristic {
        TILES_IN_WRONG_POSITION, MANHATTAN_DISTANCE, SUM_SQUARE_DISTANCES;
    }

    private class Node implements Comparable<Node>{
        private String state;
        private int fScore;
        private int gScore;
        private Node parent;

        Node(String state) {
            this.state = state;
        }

        @Override
        public int compareTo(Node that) {
            return this.fScore - that.fScore;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }

            if (!(that instanceof Node)) {
                return false;
            }

            return this.state.equals(((Node) that).state);
        }

        @Override
        public int hashCode() {
            return 31 * 17 + state.hashCode();
        }
    }

    private static final String END_STATE = "123456780";
    private static Map<Heuristic, Long> statesEvaluated = new HashMap<>();
    private static Map<Heuristic, Integer> numberOfMoves = new HashMap<>();

    public static void main(String[] args) {
        Exercise32_8Puzzle puzzle = new Exercise32_8Puzzle();

        int[][] grid = puzzle.generateRandom8PuzzleGrid();

        //Heuristic 1 - Tiles in wrong position
        List<String> solutionHeuristic1 = puzzle.solve8Puzzle(grid, Heuristic.TILES_IN_WRONG_POSITION);

        if (solutionHeuristic1 != null) {
            StdOut.println("Using tiles in wrong position as heuristic:");
            puzzle.printSolution(solutionHeuristic1);
        } else {
            String initialState = puzzle.getState(grid);
            StdOut.println("There is no possible solution for the initial state " + initialState);
            return;
        }

        //Heuristic 2 - Manhattan distance
        StdOut.println("Using manhattan distance as heuristic:");
        List<String> solutionHeuristic2 = puzzle.solve8Puzzle(grid, Heuristic.MANHATTAN_DISTANCE);
        puzzle.printSolution(solutionHeuristic2);

        //Heuristic 3 - Sum of the squares of the tiles in wrong position distance and manhattan distance
        StdOut.println("Using sum of the squares of the tiles in wrong position distance and manhattan distance as heuristic:");
        List<String> solutionHeuristic3 = puzzle.solve8Puzzle(grid, Heuristic.SUM_SQUARE_DISTANCES);
        puzzle.printSolution(solutionHeuristic3);

        //Compare heuristics
        StdOut.println("Number of states evaluated before solving the puzzle:");
        StdOut.println("Tiles in wrong position as heuristic: " + statesEvaluated.get(Heuristic.TILES_IN_WRONG_POSITION));
        StdOut.println("Manhattan distance as heuristic: " + statesEvaluated.get(Heuristic.MANHATTAN_DISTANCE));
        StdOut.println("Sum of squares of tiles in wrong position and manhattan distance as heuristic: " + statesEvaluated.get(Heuristic.SUM_SQUARE_DISTANCES));

        StdOut.println("\nNumber of moves to solve the puzzle:");
        StdOut.println("Tiles in wrong position as heuristic: " + numberOfMoves.get(Heuristic.TILES_IN_WRONG_POSITION));
        StdOut.println("Manhattan distance as heuristic: " + numberOfMoves.get(Heuristic.MANHATTAN_DISTANCE));
        StdOut.println("Sum of squares of tiles in wrong position and manhattan distance as heuristic: " + numberOfMoves.get(Heuristic.SUM_SQUARE_DISTANCES));
    }

    private int[][] generateRandom8PuzzleGrid() {
        int[][] grid = new int[3][3];

        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        StdRandom.shuffle(values);
        int valuesIndex = 0;

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                //Leave the first cell empty
                if (i == 0 && j == 0) {
                    continue;
                }

                grid[i][j] = values[valuesIndex++];
            }
        }

        return grid;
    }

    private List<String> solve8Puzzle(int[][] grid, Heuristic heuristic) {

        long totalStatesEvaluated = 1;
        String initialState = getState(grid);

        Node sourceNode = new Node(initialState);
        sourceNode.fScore = getHeuristicValue(heuristic, initialState);
        sourceNode.gScore = 0;

        // The set of currently discovered nodes that are not evaluated yet.
        // Initially, only the start node is known.
        PriorityQueue<Node> openSet = new PriorityQueue<>(10);
        openSet.add(sourceNode);

        // The set of nodes already evaluated.
        Set<Node> closedSet = new HashSet<>();

        Node current = openSet.poll();

        while (current != null && !current.state.equals(END_STATE)) {

            totalStatesEvaluated++;

            closedSet.add(current);

            for(Node neighbor : getNeighbors(current)) {

                if (closedSet.contains(neighbor)) {
                    continue;
                }

                neighbor.parent = current;

                // The distance (number of moves) from start to the neighbor
                neighbor.gScore = current.gScore + 1;
                // cost = number of moves + heuristic
                neighbor.fScore = neighbor.gScore + getHeuristicValue(heuristic, neighbor.state);

                openSet.add(neighbor);
            }

            current = openSet.poll();
        }

        if (current == null) {
            return null;
        }

        statesEvaluated.put(heuristic, totalStatesEvaluated);
        numberOfMoves.put(heuristic, current.gScore);
        return getSolution(current);
    }

    private String getState(int[][] grid) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                stringBuilder.append(grid[i][j]);
            }
        }

        return stringBuilder.toString();
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        String currentState = node.state;

        int emptyCellIndex = currentState.indexOf('0');

        String neighbor1State;
        String neighbor2State;
        String neighbor3State;
        String neighbor4State;

        switch (emptyCellIndex) {
            case 0:
                neighbor1State = currentState.replace(currentState.charAt(0), '*')
                        .replace(currentState.charAt(1), currentState.charAt(0)).replace('*', currentState.charAt(1));
                neighbor2State = currentState.replace(currentState.charAt(0), '*')
                        .replace(currentState.charAt(3), currentState.charAt(0)).replace('*', currentState.charAt(3));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                break;
            case 1:
                neighbor1State = currentState.replace(currentState.charAt(1), '*')
                        .replace(currentState.charAt(0), currentState.charAt(1)).replace('*', currentState.charAt(0));
                neighbor2State = currentState.replace(currentState.charAt(1), '*')
                        .replace(currentState.charAt(2), currentState.charAt(1)).replace('*', currentState.charAt(2));
                neighbor3State = currentState.replace(currentState.charAt(1), '*')
                        .replace(currentState.charAt(4), currentState.charAt(1)).replace('*', currentState.charAt(4));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                neighbors.add(new Node(neighbor3State));
                break;
            case 2:
                neighbor1State = currentState.replace(currentState.charAt(2), '*')
                        .replace(currentState.charAt(1), currentState.charAt(2)).replace('*', currentState.charAt(1));
                neighbor2State = currentState.replace(currentState.charAt(2), '*')
                        .replace(currentState.charAt(5), currentState.charAt(2)).replace('*', currentState.charAt(5));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                break;
            case 3:
                neighbor1State = currentState.replace(currentState.charAt(3), '*')
                        .replace(currentState.charAt(0), currentState.charAt(3)).replace('*', currentState.charAt(0));
                neighbor2State = currentState.replace(currentState.charAt(3), '*')
                        .replace(currentState.charAt(4), currentState.charAt(3)).replace('*', currentState.charAt(4));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                break;
            case 4:
                neighbor1State = currentState.replace(currentState.charAt(4), '*')
                        .replace(currentState.charAt(1), currentState.charAt(4)).replace('*', currentState.charAt(1));
                neighbor2State = currentState.replace(currentState.charAt(4), '*')
                        .replace(currentState.charAt(3), currentState.charAt(4)).replace('*', currentState.charAt(3));
                neighbor3State = currentState.replace(currentState.charAt(4), '*')
                        .replace(currentState.charAt(5), currentState.charAt(4)).replace('*', currentState.charAt(5));
                neighbor4State = currentState.replace(currentState.charAt(4), '*')
                        .replace(currentState.charAt(7), currentState.charAt(4)).replace('*', currentState.charAt(7));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                neighbors.add(new Node(neighbor3State));
                neighbors.add(new Node(neighbor4State));
                break;
            case 5:
                neighbor1State = currentState.replace(currentState.charAt(5), '*')
                        .replace(currentState.charAt(2), currentState.charAt(5)).replace('*', currentState.charAt(2));
                neighbor2State = currentState.replace(currentState.charAt(5), '*')
                        .replace(currentState.charAt(4), currentState.charAt(5)).replace('*', currentState.charAt(4));
                neighbor3State = currentState.replace(currentState.charAt(5), '*')
                        .replace(currentState.charAt(8), currentState.charAt(5)).replace('*', currentState.charAt(8));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                neighbors.add(new Node(neighbor3State));
                break;
            case 6:
                neighbor1State = currentState.replace(currentState.charAt(6), '*')
                        .replace(currentState.charAt(3), currentState.charAt(6)).replace('*', currentState.charAt(3));
                neighbor2State = currentState.replace(currentState.charAt(6), '*')
                        .replace(currentState.charAt(7), currentState.charAt(6)).replace('*', currentState.charAt(7));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                break;
            case 7:
                neighbor1State = currentState.replace(currentState.charAt(7), '*')
                        .replace(currentState.charAt(4), currentState.charAt(7)).replace('*', currentState.charAt(4));
                neighbor2State = currentState.replace(currentState.charAt(7), '*')
                        .replace(currentState.charAt(6), currentState.charAt(7)).replace('*', currentState.charAt(6));
                neighbor3State = currentState.replace(currentState.charAt(7), '*')
                        .replace(currentState.charAt(8), currentState.charAt(7)).replace('*', currentState.charAt(8));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                neighbors.add(new Node(neighbor3State));
                break;
            case 8:
                neighbor1State = currentState.replace(currentState.charAt(8), '*')
                        .replace(currentState.charAt(5), currentState.charAt(8)).replace('*', currentState.charAt(5));
                neighbor2State = currentState.replace(currentState.charAt(8), '*')
                        .replace(currentState.charAt(7), currentState.charAt(8)).replace('*', currentState.charAt(7));
                neighbors.add(new Node(neighbor1State));
                neighbors.add(new Node(neighbor2State));
                break;
        }

        return neighbors;
    }

    //Heuristcs
    private int getHeuristicValue(Heuristic heuristic, String state) {

        switch (heuristic) {
            case TILES_IN_WRONG_POSITION:
                return getNumberOfTilesInWrongPosition(state);
            case MANHATTAN_DISTANCE:
                return getManhattanDistance(state);
            case SUM_SQUARE_DISTANCES:
                return getSumsOfSquareDistances(state);
        }

        return 0;
    }

    private int getNumberOfTilesInWrongPosition(String state) {
        int numberOfTilesInWrongPosition = 0;

        for(int i = 0; i < state.length(); i++) {
            if (state.charAt(i) != END_STATE.charAt(i)) {
                numberOfTilesInWrongPosition++;
            }
        }

        return numberOfTilesInWrongPosition;
    }

    private int getManhattanDistance(String state) {
        int manhattanDistance = 0;

        int[][] grid = new int[3][3];
        int stateStringIndex = 0;

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                grid[i][j] = state.charAt(stateStringIndex++) - '0';
            }
        }

        //A number correct position in the grid is given by:
        //If number % 3 != 0
        //  correct position row = number / 3
        //  correct position column = number % 3 - 1
        //else
        //  correct position row = number / 3 - 1
        //  correct position column = 2
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    manhattanDistance += Math.abs(2 - i) + Math.abs(2 - j);;
                    continue;
                }

                int correctRow;
                int correctColumn;

                if (grid[i][j] % 3 != 0) {
                    correctRow = grid[i][j] / 3;
                    correctColumn = grid[i][j] % 3 - 1;
                } else {
                    correctRow = grid[i][j] / 3 - 1;
                    correctColumn = 2;
                }

                int distance = Math.abs(correctRow - i) + Math.abs(correctColumn - j);
                manhattanDistance += distance;
            }
        }

        return manhattanDistance;
    }

    private int getSumsOfSquareDistances(String state) {
        return (int) (Math.pow(getNumberOfTilesInWrongPosition(state), 2) + Math.pow(getManhattanDistance(state), 2));
    }

    private List<String> getSolution(Node node) {
        List<String> solution = new ArrayList<>();

        while (node != null) {
            solution.add(node.state);
            node = node.parent;
        }

        List<String> orderedSolution = new ArrayList<>();

        for(int i = solution.size() - 1; i >= 0; i--) {
            orderedSolution.add(solution.get(i));
        }

        return orderedSolution;
    }

    private void printSolution(List<String> states) {
        for(String state : states) {
            int index = 0;

            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    StdOut.print(state.charAt(index++));
                }
                StdOut.println();
            }

            StdOut.println();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
