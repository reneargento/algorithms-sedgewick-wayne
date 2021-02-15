package chapter1.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise17_RationalNumbersRobust {

	private class RationalNumbersRobust {
		private long numerator;
		private long denominator;

		private final static String ASSERT_AVOIDING_OVERFLOW_MESSAGE = "Operation would cause overflow";

		public RationalNumbersRobust(int numerator, int denominator) {
			if (denominator == 0) {
				throw new RuntimeException("Denominator cannot be 0");
			}

			this.numerator = numerator;
			this.denominator = denominator;

			int gcd = gcd(numerator, denominator);
			this.numerator /= gcd;
			this.denominator /= gcd;

			if (this.denominator < 0) {
				this.denominator = -1 * this.denominator;
				this.numerator = -1 * this.numerator;
			}
		}

		private int gcd(int numerator, int denominator) {
			if (denominator == 0) {
				return numerator;
			} else {
				return gcd(denominator, numerator % denominator);
			}
		}

		public int numerator() {
			return (int) numerator;
		}

		public int denominator() {
			return (int) denominator;
		}

		public RationalNumbersRobust plus(RationalNumbersRobust b) {
			assert this.numerator * b.denominator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert b.numerator * this.denominator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert this.numerator * b.denominator >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert b.numerator * this.denominator >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;

			long newNumeratorA = this.numerator() * b.denominator();
			long newNumeratorB = b.numerator() * this.denominator();

			assert newNumeratorA + newNumeratorB <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert newNumeratorA + newNumeratorB >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			int resultNumerator = (int) (newNumeratorA + newNumeratorB);

			assert this.denominator * b.denominator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert this.denominator * b.denominator >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;

			int resultDenominator = this.denominator() * b.denominator();
			return new RationalNumbersRobust(resultNumerator, resultDenominator);
		}

		public RationalNumbersRobust minus(RationalNumbersRobust b) {
			assert this.numerator * b.denominator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert b.numerator * this.denominator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert this.numerator * b.denominator >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert b.numerator * this.denominator >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;

			long newNumeratorA = this.numerator() * b.denominator();
			long newNumeratorB = b.numerator() * this.denominator();

			assert newNumeratorA - newNumeratorB >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			int resultNumerator = (int) (newNumeratorA - newNumeratorB);

			assert this.denominator * b.denominator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			int resultDenominator = this.denominator() * b.denominator();

			return new RationalNumbersRobust(resultNumerator, resultDenominator);
		}

		public RationalNumbersRobust times(RationalNumbersRobust b) {
			assert this.numerator * b.numerator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert this.denominator * b.denominator <= Integer.MAX_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert this.numerator * b.numerator >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;
			assert this.denominator * b.denominator >= Integer.MIN_VALUE : ASSERT_AVOIDING_OVERFLOW_MESSAGE;

			int resultNumerator = this.numerator() * b.numerator();
			int resultDenominator = this.denominator() * b.denominator();
			return new RationalNumbersRobust(resultNumerator, resultDenominator);
		}

		public RationalNumbersRobust dividedBy(RationalNumbersRobust b) {
			RationalNumbersRobust bReciprocal = new RationalNumbersRobust(b.denominator(), b.numerator());
			return times(bReciprocal);
		}

		public boolean equals(Object x) {
			if (this == x) {
				return true;
			}
			if (x == null) {
				return false;
			}
			if (this.getClass() != x.getClass()) {
				return false;
			}

			RationalNumbersRobust that = (RationalNumbersRobust) x;
			if (this.numerator() != that.numerator()) {
				return false;
			}
			if (this.denominator() != that.denominator()) {
				return false;
			}
			return true;
		}

		public String toString() {
			return this.numerator() + "/" + this.denominator();
		}
	}

	public static void main(String[] args) {
		new Exercise17_RationalNumbersRobust().testRationalNumbersRobust();
	}

	private void testRationalNumbersRobust() {
		RationalNumbersRobust rational1 = new RationalNumbersRobust(1, 4);
		RationalNumbersRobust rational2 = new RationalNumbersRobust(2, 3);

		RationalNumbersRobust rationalPlus = rational1.plus(rational2);
		StdOut.println("Plus test: 1/4 + 2/3 = " + rationalPlus.numerator + "/" + rationalPlus.denominator
				+ " Expected: 11/12");

		RationalNumbersRobust rationalMinus = rational1.minus(rational2);
		StdOut.println("Minus test: 1/4 - 2/3 = " + rationalMinus.numerator + "/" + rationalMinus.denominator
				+ " Expected: -5/12");

		RationalNumbersRobust rationalTimes = rational1.times(rational2);
		StdOut.println("Times test: 1/4 * 2/3 = " + rationalTimes.numerator + "/" + rationalTimes.denominator
				+ " Expected: 1/6");

		RationalNumbersRobust rationalDividedBy = rational1.dividedBy(rational2);
		StdOut.println("Divided by test: 1/4 / 2/3 = " + rationalDividedBy.numerator + "/" + rationalDividedBy.denominator
				+ " Expected: 3/8");

		RationalNumbersRobust equalRational1 = rational1;
		RationalNumbersRobust equalRational2 = new RationalNumbersRobust(rational1.numerator(), rational1.denominator());
		RationalNumbersRobust nonEqualRational = new RationalNumbersRobust(7, 8);
		StdOut.println("Equal test 1 = " + rational1.equals(equalRational1) + " Expected: true");
		StdOut.println("Equal test 2 = " + rational1.equals(equalRational2) + " Expected: true");
		StdOut.println("Equal test 3 = " + rational1.equals(nonEqualRational) + " Expected: false");

		StdOut.println("toString() test 1 = " + rational1 + " Expected: 1/4");
		StdOut.println("toString() test 2 = " + rational2 + " Expected: 2/3");

		// Would cause an overflow
		RationalNumbersRobust r1 = new RationalNumbersRobust(-2147483648, 1);
		RationalNumbersRobust r2 = new RationalNumbersRobust(-1, 1);
		// r1.plus(r2);
	}
}
