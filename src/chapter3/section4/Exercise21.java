package chapter3.section4;

/**
 * Created by rene on 21/07/17.
 */
public class Exercise21 {

    private class LinearProbingHashTableAvgSearchMissCost<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableAvgSearchMissCost(int size) {
            super(size);
        }

        //Average cost of a search miss = (1 + N / (2M) + SUM(square of cluster lengths)) / (2M)
        public long getAverageCostOfSearchMiss() {
            double totalNumberOfComparesForSearchMiss = 1 + keysSize / (2 * size);
            totalNumberOfComparesForSearchMiss += getSumOfClusterLengthSquares();

            return (long) (totalNumberOfComparesForSearchMiss / (2 * size));
        }

        private long getSumOfClusterLengthSquares() {
            long sumOfClusterLengthSquares = 0;

            int currentClusterLength = 0;

            for(int i = 0; i < keys.length; i++) {
                if(keys[i] == null) {
                    if(currentClusterLength != 0) {
                        sumOfClusterLengthSquares += Math.pow(currentClusterLength, 2);
                        currentClusterLength = 0;
                    }
                } else {
                    currentClusterLength++;
                }
            }

            if(currentClusterLength != 0) {
                sumOfClusterLengthSquares += Math.pow(currentClusterLength, 2);
            }

            return sumOfClusterLengthSquares;
        }
    }

    public static void main(String[] args) {
        Exercise21 exercise21 = new Exercise21();
        LinearProbingHashTableAvgSearchMissCost<Integer, Integer> linearProbingHashTableAvgSearchMissCost =
                exercise21.new LinearProbingHashTableAvgSearchMissCost<>(10);

        //Hash code 1
        linearProbingHashTableAvgSearchMissCost.put(1, 1);
        System.out.println(linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMiss() + " Expected: 0");

        //Hash code 0
        linearProbingHashTableAvgSearchMissCost.put(0, 0);
        //Hash code 2
        linearProbingHashTableAvgSearchMissCost.put(2, 2);
        System.out.println(linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMiss() + " Expected: 0");

        //Hash code 3
        linearProbingHashTableAvgSearchMissCost.put(3, 3);
        //Hash code 4
        linearProbingHashTableAvgSearchMissCost.put(4, 4);
        System.out.println(linearProbingHashTableAvgSearchMissCost.getAverageCostOfSearchMiss() + " Expected: 1");
    }

}
