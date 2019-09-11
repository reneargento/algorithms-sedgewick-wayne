package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to Akshay Sharma for mentioning that the postfix expression should not contain any parenthesis
public class Exercise10 {
	
	private static String infixToPostfix(String infixExpression) {
		Stack<String> operands = new Stack<>();
		Stack<String> operators = new Stack<>();
		
		String[] inputValues = infixExpression.split("\\s");
		
		for (String value : inputValues) {
			if (value.equals("(")) {
				//do nothing
			} else if (value.equals("+")
					|| value.equals("-")
					|| value.equals("*")
					|| value.equals("/")) {
				operators.push(value);
			} else if (value.equals(")")) {
				String value2 = operands.pop();
				String value1 = operands.pop();
				String operator = operators.pop();

				String newExpression = value1 + " " + value2 + " " + operator;
				operands.push(newExpression);
			} else {
				operands.push(value);
			}
		}
		
		return operands.pop();
	}
	
	public static void main (String[] args) {
		String infixExpression = "( ( 1 + 2 ) * ( 4 / 2 ) )";
		String postFixExpression = infixToPostfix(infixExpression);
		
		StdOut.println("Postfix expression: " + postFixExpression);
		StdOut.println("Expected: 1 2 + 4 2 / *");
	}

}
