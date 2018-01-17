package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/10/16.
 */
//Reference: http://stackoverflow.com/questions/17404642/throwing-eggs-from-a-building
public class Exercise24_ThrowingEggs {

    public static void main(String[] args) {
        int[] floors = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1};

        Exercise24_ThrowingEggs exercise24_throwingEggs = new Exercise24_ThrowingEggs();

        int floor = exercise24_throwingEggs.findFloorInLgN(floors);
        StdOut.println("Floor: "  + floor + " Expected: 6");

        int[] lotOfFloors = {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int floor2 = exercise24_throwingEggs.findFloorIn2LgF(lotOfFloors);
        StdOut.println("Floor: "  + floor2 + " Expected: 4");
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

            //We don't have to worry about the case key < floors[middle]
            if (key > floors[middle]) {
                return findFloorInLgN(floors, middle + 1, high);
            } else { //key == floors[middle]
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
        int low = 0;
        int high = floors.length - 1;

        return findFloorIn2LgF(floors, low, high, 0);
    }

    private int findFloorIn2LgF(int[] floors, int low, int high, int searchLevel) {
        int key = 1;

        if (low <= high) {
            int searchElement;

            if (searchLevel == 0) {
                searchElement = 1;
            } else {
                //Since N is much larger than F, we use repeated doubling when searching for higher floors
                searchElement = searchLevel * 2;
            }

            StdOut.println("Debug - current index: " + searchElement);

            //We don't have to worry about the case key < floors[middle]
            if (key > floors[searchElement]) {
                return findFloorIn2LgF(floors, searchElement + 1, high, ++searchLevel);
            } else { //key == floors[middle]
                //Now we do a normal binary search - O(lg F)
                int indexWhereWeDidntFindElement = searchLevel / 2;
                int newFloor = findFloorInLgN(floors, indexWhereWeDidntFindElement, searchElement - 1);

                if (newFloor == -1) {
                    return searchElement;
                } else {
                    return newFloor;
                }
            }

         }

        return -1;
    }
}
