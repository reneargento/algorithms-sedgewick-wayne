package chapter1.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 * Doomsday algorithm implemented by Junaid Ashraf (aka sickboy)
 */
public class Exercise12 {
    private final int month;
    private final int day;
    private final int year;

    private final String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private final int[] daysInMonths = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public Exercise12(int month, int day, int year) {
        if (year % 4 != 0) { // not a leap year
            daysInMonths[1] = 28;
        }
        if (!isDateValid(month, day, year)) {
            throw new RuntimeException("Invalid date!");
        }
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public int month() {
        return month;
    }

    public int day() {
        return day;
    }

    public int year() {
        return year;
    }

    public String toString() {
        return month() + "/" + day() + "/" + year();
    }

    private boolean isDateValid(int month, int day, int year) {
        return year >= 1
                && month >= 1 && month <= 12
                && day >= 1 && day <= daysInMonths[month - 1];
    }

    /**
     * Returns doomsday for any year in the 21st century
     * More info: <link href="https://www.youtube.com/watch?v=z2x3SSBVGJU">Doomsday algorithm: numberphile</link>
     *
     * @return index of doomsday of current year
     */
    public int getDoomsday() {
        if (year < 2000 || year > 2100) {
            throw new IllegalArgumentException("Year must be from 21st century");
        }
        int numberOfYears = year % 100;
        // On the year 2000 doomsday was a Tuesday. With this information, we can compute the doomsday of any
        // other year.
        // doomsday = (tuesday(3) + number of years from 2000 + number of leap years) % 7
        int doomsday = 2 + numberOfYears + (numberOfYears / 4);

        // If it is a leap year then doomsday will be on January 4th. Otherwise, it will be on January 3rd.
        // From here we can compute the weekday name of January 1st for the current year.
        if (year % 4 == 0) {
            return doomsday + 3;
        } else {
            return doomsday + 4;
        }
    }

    /**
     * @return day of the week
     */
    public String dayOfTheWeek() {
        int numberOfDays = day;
        for (int i = 0; i < month - 1; i++) {
            numberOfDays += daysInMonths[i];
        }
        int dayOfWeek = (numberOfDays + getDoomsday()) % 7;
        return daysOfWeek[dayOfWeek];
    }

    public static void main(String[] args) {
        Exercise12 smartDate1 = new Exercise12(4, 18, 2020);
        StdOut.println(smartDate1.dayOfTheWeek() + " Expected: Saturday");

        Exercise12 smartDate2 = new Exercise12(6, 15, 2005);
        StdOut.println(smartDate2.dayOfTheWeek() + " Expected: Wednesday");

        Exercise12 smartDate3 = new Exercise12(9, 11, 2001);
        StdOut.println(smartDate3.dayOfTheWeek() + " Expected: Tuesday");

        Exercise12 smartDate4 = new Exercise12(12, 31, 2010);
        StdOut.println(smartDate4.dayOfTheWeek() + " Expected: Friday");

        Exercise12 smartDate5 = new Exercise12(7, 4, 2025);
        StdOut.println(smartDate5.dayOfTheWeek() + " Expected: Friday");

        Exercise12 smartDate6 = new Exercise12(11, 22, 2040);
        StdOut.println(smartDate6.dayOfTheWeek() + " Expected: Thursday");

        Exercise12 smartDate7 = new Exercise12(3, 29, 2050);
        StdOut.println(smartDate7.dayOfTheWeek() + " Expected: Tuesday");
    }
}
