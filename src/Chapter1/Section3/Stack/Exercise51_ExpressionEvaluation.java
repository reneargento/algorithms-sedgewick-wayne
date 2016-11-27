package Chapter1.Section3.Stack;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by rene on 8/28/16.
 */
public class Exercise51_ExpressionEvaluation {

    public static void main(String[] args){

        Stack<String> ops = new Stack<>();
        Stack<Double> vals = new Stack<>();

        while (!StdIn.isEmpty()) {
            //Read token, push if operator.
            String s = StdIn.readString();
            if (s.equals("(")) {
                ops.push(s);
            } else if (s.equals("+")){
                if(ops.size() > 0) {
                    checkIfExistsAndSolveLowPrecedenceOperation(ops, vals);
                }

                ops.push(s);
            } else if (s.equals("-")) {
                if(ops.size() > 0) {
                    checkIfExistsAndSolveLowPrecedenceOperation(ops, vals);
                }

                ops.push(s);
            } else if (s.equals("*")) {
                ops.push(s);
            } else if (s.equals("/")) {
                ops.push(s);
            } else if (s.equals("sqrt")) {
                ops.push(s);
            } else if (s.equals(")")) {
                String op = ops.pop();

                if (op.equals("(")){
                    //First case - There was just a number inside the parentheses. E.g. ( 6 )
                    //Check to see if there is any high precedence operation waiting for the expression result
                    checkAndSolveExpressionWaitingParenteshisResult(ops, vals);
                } else {
                    solveExpressionOnTopOfStack(vals, op);

                    String lastOperator = ops.pop();
                    if (lastOperator.equals("(") && ops.size() > 0) {
                        //Second case - There was one expression inside the parentheses. E.g. ( 1 + 2 )
                        //Check to see if there is any high precedence operation waiting for the expression result

                        checkAndSolveExpressionWaitingParenteshisResult(ops, vals);
                    }
                    //Note - Since we are solving expressions as we read the input, there is no case where we have
                    //more than one expression inside the parentheses
                }
            } else {
                String lastOperator = null;

                if(ops.size() > 0) {
                    lastOperator = ops.peek();
                }

                if(lastOperator != null && (lastOperator.equals("*") || lastOperator.equals("/") || lastOperator.equals("sqrt"))) {
                    ops.pop();
                    double result;

                    if (lastOperator.equals("*")) {
                        result = Double.parseDouble(s) * vals.pop();
                    } else if (lastOperator.equals("/")) {
                        result = vals.pop() / Double.parseDouble(s);
                    } else {
                        result = Math.sqrt(Double.parseDouble(s));
                    }

                    vals.push(result);
                } else {
                    //Token not operator or parenthesis; push double value
                    vals.push(Double.parseDouble(s));
                }
            }
        }

        //Finish remaining operation (if exists)
        if (vals.size() > 1) {
            String operand = ops.pop();
            solveExpressionOnTopOfStack(vals, operand);
        }

        StdOut.println(vals.pop());
    }

    private static void solveExpressionOnTopOfStack(Stack<Double> vals, String lastOperand) {
        //Pop, evaluate and push result
        double v = vals.pop();
        if (lastOperand.equals("+")) {
            v = vals.pop() + v;
        } else if (lastOperand.equals("-")) {
            v = vals.pop() - v;
        } else if (lastOperand.equals("*")) {
            v = vals.pop() * v;
        } else if (lastOperand.equals("/")) {
            v = vals.pop() / v;
        } else if (lastOperand.equals("sqrt")) {
            v = Math.sqrt(v);
        }
        vals.push(v);
    }

    private static void checkAndSolveExpressionWaitingParenteshisResult(Stack<String> ops, Stack<Double> vals) {
        String operatorBeforeExpression = ops.peek();

        if (operatorBeforeExpression.equals("*")
                || operatorBeforeExpression.equals("/")
                || operatorBeforeExpression.equals("sqrt")) {
            ops.pop();
            solveExpressionOnTopOfStack(vals, operatorBeforeExpression);
        }
    }

    private static void checkIfExistsAndSolveLowPrecedenceOperation(Stack<String> ops, Stack<Double> vals) {
        String operatorBeforeExpression = ops.peek();

        if (operatorBeforeExpression.equals("-") || operatorBeforeExpression.equals("+")) {
            ops.pop();
            solveExpressionOnTopOfStack(vals, operatorBeforeExpression);
        }

    }

    //Test samples
    // 2 * 2 + 1 -> 5 //No parentheses
    // 1 * ( 2 + 1 ) -> 3 //Mix of parentheses and no parentheses
    // ( 1 * 2 + 1 ) -> 3
    // 1 + 2 * ( 3 * 2 - 2 * ( 3 * 2 ) + 1 ) -> -9
    // ( 2 * ( 3 - ( 5 / 2 ) ) + 3 ) -> 4
}
