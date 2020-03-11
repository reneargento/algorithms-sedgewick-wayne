package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise11 {
	
	private static int evaluatePostfix(String postfixExpression) {
		Stack<Integer> operands = new Stack<>();

		String[] values = postfixExpression.split("\\s");
		for (String s : values) {
			if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) {
				int operand2 = operands.pop();
				int operand1 = operands.pop();
				int result = 0;
				if (s.equals("+")) {
					result = operand1 + operand2;
				} else if (s.equals("-")) {
					result = operand1 - operand2;
				} else if (s.equals("*")) {
					result = operand1 * operand2;
				} else if (s.equals("/")) {
					result = operand1 / operand2;
				}
				operands.push(result);
			} else {
				operands.push(Integer.parseInt(s));
			}
		}
		return operands.pop();
	}

	// Parameter example: "4 2 + 1 3 - *"
	public static void main(String[] args) {
		String postfixExpression = args[0];
		StdOut.println("Result: " + evaluatePostfix(postfixExpression));
		StdOut.println("Expected: -12");
	}

}
