package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise11 {
	
	private static int evaluatePostfix(String postfixExpression) {
		Stack<String> operands = new Stack<>();
		Stack<String> operators = new Stack<>();
		
		String[] values = postfixExpression.split("\\s");
		for (String s : values) {
			if (s.equals("(")) {
				//do nothing
			} else if (s.equals("+")
					|| s.equals("-")
					|| s.equals("*")
					|| s.equals("/")) {
				operators.push(s);
			} else if (s.equals(")")) {
				int operand2 = Integer.parseInt(operands.pop());
				int operand1 = Integer.parseInt(operands.pop());
				String operator = operators.pop();
				
				int result = 0;
				if (operator.equals("+")) {
					result = operand1 + operand2;
				} else if (operator.equals("-")) {
					result = operand1 - operand2;
				} else if (operator.equals("*")) {
					result = operand1 * operand2;
				} else if (operator.equals("/")) {
					result = operand1 / operand2;
				}
				operands.push(String.valueOf(result));
			} else {
				operands.push(s);
			}
		}
		
		return Integer.parseInt(operands.pop());
	}

	// Parameter example: "( ( 4 2 + ) 3 / )"
	public static void main(String[] args) {
		String postfixExpression = args[0];
		
		StdOut.println("Result: " + evaluatePostfix(postfixExpression));
	}

}
