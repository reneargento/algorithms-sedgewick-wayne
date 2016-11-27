package Chapter1.Section3.Stack;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Exercise4 {
	
	public boolean isBalanced(String input) {
		boolean isBalanced = true;
		
		char[] parentheses = input.toCharArray();
		Stack<Character> stack = new Stack<>();
		
		for (char parenthesis : parentheses) {
			if (parenthesis == '('
					|| parenthesis == '['
					|| parenthesis == '{'){
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
		
		return isBalanced;
	}
	
	
	public static void main(String[] args) {
		Exercise4 exercise4 = new Exercise4();
		
		StdOut.println("Is balanced [()]{}{[()()]()}: " + exercise4.isBalanced("[()]{}{[()()]()}"));
		StdOut.println("Is balanced [(]): " + exercise4.isBalanced("[(])"));
	}

}
