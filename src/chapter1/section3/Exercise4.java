package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise4 {

	public static void main(String[] args) {
		Exercise4 exercise4 = new Exercise4();

		String textStreamInput = args[0]; //[()]
		StdOut.println("Is balanced " + textStreamInput + ": " + exercise4.isBalanced(textStreamInput));

		StdOut.println("Is balanced [()]{}{[()()]()}: " + exercise4.isBalanced("[()]{}{[()()]()}"));
		StdOut.println("Is balanced [(]): " + exercise4.isBalanced("[(])"));
	}
	
	private boolean isBalanced(String input) {

		char[] parentheses = input.toCharArray();
		Stack<Character> stack = new Stack<>();
		
		for (char parenthesis : parentheses) {
			if (parenthesis == '('
					|| parenthesis == '['
					|| parenthesis == '{') {
				stack.push(parenthesis);
			} else {
				if (stack.isEmpty()) {
					return false;
				}
				
				char firstItem = stack.pop();
				
				if (parenthesis == ')' && firstItem != '('
						|| parenthesis == ']' && firstItem != '['
						|| parenthesis == '}' && firstItem != '{') {
					return false;
				}
			}
		}
		
		return true;
	}

}
