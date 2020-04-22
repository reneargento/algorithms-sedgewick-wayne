package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/10/16.
 */
// Thanks to emergencyd (https://github.com/emergencyd) for suggesting an improvement on the findFloorIn2LgF() solution.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/130
// Reference: http://stackoverflow.com/questions/17404642/throwing-eggs-from-a-building
public class Exercise24_ThrowingEggs {

    public static void main(String[] args) {
        int[] floors = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1};

        Exercise24_ThrowingEggs exercise24_throwingEggs = new Exercise24_ThrowingEggs();

        int floor = exercise24_throwingEggs.findFloorInLgN(floors);
        StdOut.println("Floor: "  + floor + " Expected: 6");

        int[] lotOfFloors = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
        int floor2 = exercise24_throwingEggs.findFloorIn2LgF(lotOfFloors);
        StdOut.println("Floor: "  + floor2 + " Expected: 18");
    }

    private int findFloorInLgN(int[] floors) {
        int low = 0;
        int high = floors.length - 1;

        return findFloorInLgN(floors, low, high);
    }

    private int findFloorInLgN(int[] floors, int low, int high) {
        int key = 1;

        if (low <= high) {
            int middle = low + (high - low) / 2;

            StdOut.println("Debug - current index: " + middle);

            // We don't have to worry about the case key < floors[middle]
            if (key > floors[middle]) {
                return findFloorInLgN(floors, middle + 1, high);
            } else { // key == floors[middle]
                int lowerFloor = findFloorInLgN(floors, low, middle - 1);

                if (lowerFloor == -1) {
                    return middle;
                } else {
                    return lowerFloor;
                }
            }
        }

        return -1;
    }

    private int findFloorIn2LgF(int[] floors) {
        int key = 1;
        int searchFloor = 0;

        // Since N can be much larger than F, we use repeated doubling when searching for higher floors
        for (int powerOf2 = 1; searchFloor < floors.length; powerOf2++) {
            StdOut.println("Debug - current index: " + searchFloor);
            if (key == floors[searchFloor]) {
                break;
            }
            searchFloor = 1 << powerOf2;
        }

        // Now we do a normal binary search - O(lg F)
        int previousFloorWithoutEgg = searchFloor / 2;
        searchFloor = Math.min(floors.length - 1, searchFloor);
        int newFloor = findFloorInLgN(floors, previousFloorWithoutEgg + 1, searchFloor - 1);

        if (newFloor == -1) {
            return searchFloor;
        } else {
            return newFloor;
        }
    }
}
