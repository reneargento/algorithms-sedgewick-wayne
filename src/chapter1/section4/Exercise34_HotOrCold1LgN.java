package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 23/11/16.
 */
//IOI 2010 task
//Based on http://stackoverflow.com/questions/25558951/hot-and-cold-binary-search-game
    // Worst case is O(lg n) + 6 when we start in an "end quarter"
    // or O(lg n) + 4 when we start in a "middle quarter"
public class Exercise34_HotOrCold1LgN {

    private int hotOrCold(int n, int target) {
        return firstGuesses(n, target, 1, n);
    }

    private int firstGuesses(int number, int target, int low, int high) {

        //Check if it is in the first half
        int firstGuessIndex = number / 2;

        if (firstGuessIndex == target) {
            StdOut.println("Found it!");
            return firstGuessIndex;
        }

        //Check if it is in the second half
        int secondGuessIndex = (number / 2) + 1;
        if (secondGuessIndex == target) {
            StdOut.println("Found it!");
            return secondGuessIndex;
        } else {
            boolean isItHotter = isItHotter(firstGuessIndex, secondGuessIndex, target);

            if (isItHotter) {
                //Secret is in the second half, so the next guess will be "high"
                return initialSearch(target, secondGuessIndex, false, secondGuessIndex, high);
            } else {
                //Secret is in the first half, so the next guess will be "low"
                return initialSearch(target, secondGuessIndex, true, low, firstGuessIndex);
            }
        }
    }

    //This is just in case we fall into one of the 2 "end quarters" and need to go to one of the 2 "middle quarters"
    private int initialSearch(int target, int lastGuess, boolean isNextGuessInLeftEnd, int low, int high) {

        if (low > high) {
            return -1;
        }

        int nextGuess;
        //Check new guess
        if (isNextGuessInLeftEnd) {
            nextGuess = low;
        } else {
            nextGuess = high;
        }
        boolean isItHotter = isItHotter(lastGuess, nextGuess, target);

        int middle = low + (high - low) / 2;

        if (nextGuess == target) {
            return nextGuess;
        } else if (isItHotter) {
            //We are in one of the 2 end quarters

            int middleOfMiddle;

            if (isNextGuessInLeftEnd) {
                middleOfMiddle = low + (middle - low) / 2;
            } else {
                middleOfMiddle = middle + (high - middle) / 2;
            }

            //Guess middleOfMiddle
            isItHotter(nextGuess, middleOfMiddle, target);
            if (middleOfMiddle == target) {
                return middleOfMiddle;
            }

            //Guess middleOfMiddle + 1
            isItHotter = isItHotter(middleOfMiddle, middleOfMiddle + 1, target);
            if (middleOfMiddle + 1 == target) {
                return middleOfMiddle + 1;
            }

            if (isItHotter) {
                //Secret is in the second half
                return search(target, middleOfMiddle + 1, middleOfMiddle + 1, high);
            } else {
                //Secret is in the first half
                return search(target, middleOfMiddle + 1, low, middleOfMiddle);
            }
        } else {
            //We are in one of the 2 middle quarters
            if (isNextGuessInLeftEnd) {
                return search(target, nextGuess, middle + 1, high);
            } else {
                return search(target, nextGuess, low, middle);
            }
        }
    }

    //We are in a "middle quarter"
    //Considering [a, b] as the interval we know the secret integer exists within,
    // take c to be the last number we guessed.
    // We want to determine the position of the secret with respect
    // to the mid point (a + b) / 2, so we have a new number d to guess at to know the
    // secret relative position to (a + b) / 2.
    // How do we know such number d?
    // By solving the equation (c + d) / 2 = (a + b) / 2, which yields d = a + b - c.
    // Guessing at that d, we shrink the range [a, b] appropriately based on
    // the answer(colder or hotter) and then we repeat the process.
    private int search(int target, int lastGuess, int low, int high) {

        if (low == high) {
            if (low == target) {
                //Found it!
                return low;
            } else {
                return -1;
            }
        }

        if (low > high) {
            return -1;
        }

        // a = low
        // b = high
        // c = lastGuess
        // d = nextGuess

        int nextGuess = low + high - lastGuess;

        boolean isItHotter = isItHotter(lastGuess, nextGuess, target);

        int middle = low + (high - low) / 2;

        if (nextGuess == target) {
            return nextGuess;
        } else if (isItHotter) {
            if (nextGuess >= high) {
                return search(target, nextGuess, middle + 1, high);
            } else {
                return search(target, nextGuess, low, middle);
            }
        } else {
            if (nextGuess >= high) {
                return search(target, nextGuess, low, middle);
            } else {
                return search(target, nextGuess, middle + 1, high);
            }
        }
    }

    private boolean isItHotter(int lastGuess, int currentGuess, int secret) {

        if (currentGuess == secret) {
            StdOut.println("Found it!");
            return true;
        }

        if (Math.abs(secret - currentGuess) < Math.abs(secret - lastGuess)) {
            StdOut.println("Hotter - Last guess: " + lastGuess + " Current guess: " + currentGuess);
            return true;
        } else {
            StdOut.println("Colder - Last guess: " + lastGuess + " Current guess: " + currentGuess);
            return false;
        }
    }

    public static void main(String[] args) {
        Exercise34_HotOrCold1LgN hotOrCold = new Exercise34_HotOrCold1LgN();
        StdOut.println("Hot or Cold: " + hotOrCold.hotOrCold(10, 3) + " Expected: 3");
        StdOut.println("Hot or Cold: " + hotOrCold.hotOrCold(20, 12) + " Expected: 12");
        StdOut.println("Hot or Cold: " + hotOrCold.hotOrCold(10, 11) + " Expected: -1");
    }

}
