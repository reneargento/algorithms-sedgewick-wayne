package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to ru2saig (https://github.com/ru2saig) for reporting that the sqrt unary operator should be included.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/308
public class Exercise9 {

    private static String getInfixExpression(String input) {
        Stack<String> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        String[] inputValues = input.split("\\s");

        for (String value : inputValues) {
            if (value.equals("(")) {
                //do nothing
            } else if (value.equals("+")
                    || value.equals("-")
                    || value.equals("*")
                    || value.equals("/")
                    || value.equals("sqrt")) {
                operators.push(value);
            } else if (value.equals(")")) {
                String subExpression;
                String operator = operators.pop();
                String value2 = operands.pop();

                if (operator.equals("sqrt")) {
                    subExpression = operator + " ( " + value2 + " )";
                } else {
                    String value1 = operands.pop();
                    subExpression = "( " + value1 + " " + operator + " " + value2 + " )";
                }
                operands.push(subExpression);
            } else {
                operands.push(value);
            }
        }
        return operands.pop();
    }

    // Parameter examples:
    // "1 + 2 ) * 3 - 4 ) * 5 - 6 ) ) )"
    // "2 + 3 ) * 5 + sqrt 7.0 ) ) )"
    public static void main(String[] args) {
        String input = args[0];
        StdOut.println(getInfixExpression(input));
    }
}
