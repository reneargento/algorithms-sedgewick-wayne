package chapter3.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 17/08/17.
 */
public class Exercise25_RegistrarScheduling {

    private boolean isThereAConflict(Map<Integer, HashSet<Integer>> instructorsMap, int instructor, int timeToSchedule) {
        if (timeToSchedule != 9
                && timeToSchedule != 10
                && timeToSchedule != 11
                && timeToSchedule != 13
                && timeToSchedule != 14
                && timeToSchedule != 15) {
            throw new IllegalArgumentException("Invalid class start time (must be 9, 10, 11, 13, 14 or 15)");
        }

        return instructorsMap.get(instructor).contains(timeToSchedule);
    }

    public static void main(String[] args) {
        Exercise25_RegistrarScheduling registrarScheduling = new Exercise25_RegistrarScheduling();

        Map<Integer, HashSet<Integer>> instructorsMap = new HashMap<>();
        HashSet<Integer> instructor1Classes = new HashSet<>();
        instructor1Classes.add(9);
        instructor1Classes.add(10);
        instructor1Classes.add(15);

        HashSet<Integer> instructor2Classes = new HashSet<>();
        instructor2Classes.add(11);
        instructor2Classes.add(14);
        instructor2Classes.add(15);

        HashSet<Integer> instructor3Classes = new HashSet<>();
        instructor3Classes.add(10);

        instructorsMap.put(1, instructor1Classes);
        instructorsMap.put(2, instructor2Classes);
        instructorsMap.put(3, instructor3Classes);

        boolean conflict1 = registrarScheduling.isThereAConflict(instructorsMap, 1, 9);
        StdOut.println("Check conflict 1: " + conflict1 + " Expected: true");

        boolean conflict2 = registrarScheduling.isThereAConflict(instructorsMap, 1, 11);
        StdOut.println("Check conflict 2: " + conflict2 + " Expected: false");

        boolean conflict3 = registrarScheduling.isThereAConflict(instructorsMap, 1, 15);
        StdOut.println("Check conflict 3: " + conflict3 + " Expected: true");

        boolean conflict4 = registrarScheduling.isThereAConflict(instructorsMap, 2, 9);
        StdOut.println("Check conflict 4: " + conflict4 + " Expected: false");

        boolean conflict5 = registrarScheduling.isThereAConflict(instructorsMap, 2, 14);
        StdOut.println("Check conflict 5: " + conflict5 + " Expected: true");

        boolean conflict6 = registrarScheduling.isThereAConflict(instructorsMap, 3, 10);
        StdOut.println("Check conflict 6: " + conflict6 + " Expected: true");

        boolean conflict7 = registrarScheduling.isThereAConflict(instructorsMap, 3, 11);
        StdOut.println("Check conflict 7: " + conflict7 + " Expected: false");
    }

}
