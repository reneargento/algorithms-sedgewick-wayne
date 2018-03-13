package chapter1.section2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise16_RationalNumbers {

	private long numerator;
	private long denominator;
	
	public Exercise16_RationalNumbers(int numerator, int denominator) {
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
	
	private static int gcd(int numerator, int denominator) {
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
	
	public Exercise16_RationalNumbers plus(Exercise16_RationalNumbers b) {
		int newNumeratorA = this.numerator() * b.denominator();
		int newNumeratorB = b.numerator() * this.denominator();
		
		int resultNumerator = newNumeratorA + newNumeratorB;
		
		int resultDenominator = this.denominator() * b.denominator();

		Exercise16_RationalNumbers plusResult = new Exercise16_RationalNumbers(resultNumerator, resultDenominator);
		return plusResult;
	}
	
	public Exercise16_RationalNumbers minus(Exercise16_RationalNumbers b) {
		int newNumeratorA = this.numerator() * b.denominator();
		int newNumeratorB = b.numerator() * this.denominator();
		
		int resultNumerator = newNumeratorA - newNumeratorB;
		
		int resultDenominator = this.denominator() * b.denominator();

		Exercise16_RationalNumbers minusResult = new Exercise16_RationalNumbers(resultNumerator, resultDenominator);
		return minusResult;
	}
	
	public Exercise16_RationalNumbers times(Exercise16_RationalNumbers b) {
		int resultNumerator = this.numerator() * b.numerator();
		int resultDenominator = this.denominator() * b.denominator();

		Exercise16_RationalNumbers minusResult = new Exercise16_RationalNumbers(resultNumerator, resultDenominator);
		return minusResult;
	}
	
	public Exercise16_RationalNumbers dividedBy(Exercise16_RationalNumbers b) {
		Exercise16_RationalNumbers bReciprocal = new Exercise16_RationalNumbers(b.denominator(), b.numerator());
		
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
		
		Exercise16_RationalNumbers that = (Exercise16_RationalNumbers) x;
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
	
	public static void main(String[] args) {
		Exercise16_RationalNumbers rational1 = new Exercise16_RationalNumbers(1, 4);
		Exercise16_RationalNumbers rational2 = new Exercise16_RationalNumbers(2, 3);
		
		Exercise16_RationalNumbers rationalPlus = rational1.plus(rational2);
		StdOut.println("Plus test: 1/4 + 2/3 = " + rationalPlus.numerator + "/" + rationalPlus.denominator
				+ " Expected: 11/12");
		
		Exercise16_RationalNumbers rationalMinus = rational1.minus(rational2);
		StdOut.println("Minus test: 1/4 - 2/3 = " + rationalMinus.numerator + "/" + rationalMinus.denominator
				+ " Expected: -5/12");

		Exercise16_RationalNumbers rationalTimes = rational1.times(rational2);
		StdOut.println("Times test: 1/4 * 2/3 = " + rationalTimes.numerator + "/" + rationalTimes.denominator
				+ " Expected: 1/6");
		
		Exercise16_RationalNumbers rationalDividedBy = rational1.dividedBy(rational2);
		StdOut.println("Divided by test: 1/4 -/ 2/3 = " + rationalDividedBy.numerator + "/" + rationalDividedBy.denominator
				+ " Expected: 3/8");
		
		Exercise16_RationalNumbers equalRational1 = rational1;
		Exercise16_RationalNumbers equalRational2 = new Exercise16_RationalNumbers(rational1.numerator(), rational1.denominator());
		Exercise16_RationalNumbers nonEqualRational = new Exercise16_RationalNumbers(7, 8);
		StdOut.println("Equal test 1 = " + rational1.equals(equalRational1) + " Expected: true");
		StdOut.println("Equal test 2 = " + rational1.equals(equalRational2) + " Expected: true");
		StdOut.println("Equal test 3 = " + rational1.equals(nonEqualRational) + " Expected: false");
		
		StdOut.println("toString() test 1 = " + rational1 + " Expected: 1/4");
		StdOut.println("toString() test 2 = " + rational2 + " Expected: 2/3");
	}

}
