package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 09/04/17.
 */
public class Exercise9 {

    class VolumesPerDay implements Comparable<VolumesPerDay>{

        private String date;
        private long volume;

        VolumesPerDay(String date, long volume) {
            this.date = date;
            this.volume = volume;
        }

        @Override
        public int compareTo(VolumesPerDay that) {
            if (this.volume < that.volume) {
                return -1;
            } else if (this.volume > that.volume) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return date + "   " + volume;
        }
    }

    public static void main(String[] args) {
        Exercise9 exercise9 = new Exercise9();
        VolumesPerDay volumesPerDay1 = exercise9.new VolumesPerDay("1-Oct-28", 2775559936L);
        VolumesPerDay volumesPerDay2 = exercise9.new VolumesPerDay("2-Oct-28", 500);
        VolumesPerDay volumesPerDay3 = exercise9.new VolumesPerDay("3-Oct-28", 1000);

        VolumesPerDay[] volumesPerDays = new VolumesPerDay[3];
        volumesPerDays[0] = volumesPerDay1;
        volumesPerDays[1] = volumesPerDay2;
        volumesPerDays[2] = volumesPerDay3;

        Arrays.sort(volumesPerDays);

        for(VolumesPerDay volumesPerDay : volumesPerDays) {
            StdOut.println(volumesPerDay);
        }

        StdOut.println("\nExpected:\n" +
                "2-Oct-28   500\n" +
                "3-Oct-28   1000\n" +
                "1-Oct-28   2775559936");
    }

}
