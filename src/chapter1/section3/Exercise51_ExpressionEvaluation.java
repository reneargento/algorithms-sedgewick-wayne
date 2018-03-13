package chapter1.section3;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 8/28/16.
 */
public class Exercise51_ExpressionEvaluation {

    //Test samples
    // 2 * 2 + 1 -> 5 //No parentheses
    // 1 * ( 2 + 1 ) -> 3 //Mix of parentheses and no parentheses
    // ( 1 * 2 + 1 ) -> 3
    // 1 + 2 * ( 3 * 2 - 2 * ( 3 * 2 ) + 1 ) -> -9
    // ( 2 * ( 3 - ( 5 / 2 ) ) + 3 ) -> 4
    public static void main(String[] args) {

        Stack<String> operators = new Stack<>();
        Stack<Double> values = new Stack<>();

        while (!StdIn.isEmpty()) {
            //Read token, push if operator.
            String token = StdIn.readString();
            if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals("+")) {
                if (operators.size() > 0) {
                    checkIfExistsAndSolveLowPrecedenceOperation(operators, values);
                }

                operators.push(token);
            } else if (token.equals("-")) {
                if (operators.size() > 0) {
                    checkIfExistsAndSolveLowPrecedenceOperation(operators, values);
                }

                operators.push(token);
            } else if (token.equals("*")) {
                operators.push(token);
            } else if (token.equals("/")) {
                operators.push(token);
            } else if (token.equals("sqrt")) {
                operators.push(token);
            } else if (token.equals(")")) {
                String operator = operators.pop();

                if (operator.equals("(")) {
                    //First case - There was just a number inside the parentheses. E.g. ( 6 )
                    //Check to see if there is any high precedence operation waiting for the expression result
                    checkAndSolveExpressionWaitingParenteshisResult(operators, values);
                } else {
                    solveExpressionOnTopOfStack(values, operator);

                    String lastOperator = operators.pop();
                    if (lastOperator.equals("(") && operators.size() > 0) {
                        //Second case - There was one expression inside the parentheses. E.g. ( 1 + 2 )
                        //Check to see if there is any high precedence operation waiting for the expression result

                        checkAndSolveExpressionWaitingParenteshisResult(operators, values);
                    }
                    //Note - Since we are solving expressions as we read the input, there is no case where we have
                    //more than one expression inside the parentheses
                }
            } else {
                String lastOperator = null;

                if (operators.size() > 0) {
                    lastOperator = operators.peek();
                }

                if (lastOperator != null && (lastOperator.equals("*")
                        || lastOperator.equals("/")
                        || lastOperator.equals("sqrt"))) {
                    operators.pop();
                    double result;

                    if (lastOperator.equals("*")) {
                        result = Double.parseDouble(token) * values.pop();
                    } else if (lastOperator.equals("/")) {
                        result = values.pop() / Double.parseDouble(token);
                    } else {
                        result = Math.sqrt(Double.parseDouble(token));
                    }

                    values.push(result);
                } else {
                    //Token not operator or parenthesis; push double value
                    values.push(Double.parseDouble(token));
                }
            }
        }

        //Finish remaining operation (if exists)
        if (values.size() > 1) {
            String operand = operators.pop();
            solveExpressionOnTopOfStack(values, operand);
        }

        StdOut.println(values.pop());
    }

    private static void solveExpressionOnTopOfStack(Stack<Double> values, String lastOperand) {
        //Pop, evaluate and push result
        double value = values.pop();
        if (lastOperand.equals("+")) {
            value = values.pop() + value;
        } else if (lastOperand.equals("-")) {
            value = values.pop() - value;
        } else if (lastOperand.equals("*")) {
            value = values.pop() * value;
        } else if (lastOperand.equals("/")) {
            value = values.pop() / value;
        } else if (lastOperand.equals("sqrt")) {
            value = Math.sqrt(value);
        }
        values.push(value);
    }

    private static void checkAndSolveExpressionWaitingParenteshisResult(Stack<String> operators, Stack<Double> values) {
        String operatorBeforeExpression = operators.peek();

        if (operatorBeforeExpression.equals("*")
                || operatorBeforeExpression.equals("/")
                || operatorBeforeExpression.equals("sqrt")) {
            operators.pop();
            solveExpressionOnTopOfStack(values, operatorBeforeExpression);
        }
    }

    private static void checkIfExistsAndSolveLowPrecedenceOperation(Stack<String> operators, Stack<Double> values) {
        String operatorBeforeExpression = operators.peek();

        if (operatorBeforeExpression.equals("-") || operatorBeforeExpression.equals("+")) {
            operators.pop();
            solveExpressionOnTopOfStack(values, operatorBeforeExpression);
        }

    }
}
