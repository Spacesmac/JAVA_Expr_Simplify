import java.util.Stack;

import utils.ExpressionUtils;
import utils.OperatorPriority;
import expressions.ArithmeticExpression;
import expressions.*;

public class ExpressionParser {

    private static ArithmeticExpression createExpression(char operator, ArithmeticExpression left,
            ArithmeticExpression right) {
        if (operator == '+') {
            return new Addition(left, right);
        } else if (operator == '-') {
            return new Subtraction(left, right);
        } else if (operator == '*') {
            return new Multiplication(left, right);
        } else if (operator == '/') {
            return new Division(left, right);
        } else if (operator == '^') {
            return new Power(left, right);
        }
        return null;
    }

    public static ArithmeticExpression parse(String expression) {
        Stack<ArithmeticExpression> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();
        expression = ExpressionUtils.emptyUselessOperators(expression);
        for (int i = 0; i < expression.length(); i++) {
            char content = expression.charAt(i);
            if (Character.isDigit(content) || (content == '-' && i == 0 && i + 1 < expression.length())) {
                int j = i;
                boolean isNeg = (content == '-');
                while (i < expression.length()
                        && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    i++;
                }
                double value = Double.parseDouble(expression.substring(j, i));
                operands.push(new NumericConstant(isNeg ? -value : value));
                i--;
            } else if (Character.isLetter(content)) {
                if (i + 3 < expression.length() && expression.substring(i, i + 3).equals("cos")) {
                    i += 2;
                    operators.push('c');
                } else {
                    operands.push(new Variable(String.valueOf(content)));
                }
            } else if (content == '(') {
                operators.push(content);
            } else if (content == ')') {
                while (operators.peek() != '(') {
                    char operator = operators.pop();
                    if (operator == 'c') {
                        operands.push(new Cosinus(operands.pop()));
                    } else {
                        ArithmeticExpression right = operands.pop();
                        ArithmeticExpression left = operands.pop();
                        operands.push(createExpression(operator, left, right));
                    }
                }
                operators.pop();
            } else if (OperatorPriority.isOperator(content)) {
                while (!operators.isEmpty()
                        && OperatorPriority.getPriority(operators.peek()) >= OperatorPriority.getPriority(content)) {
                    char operator = operators.pop();
                    if (operator == 'c') {
                        operands.push(new Cosinus(operands.pop()));
                    } else {
                        ArithmeticExpression right = operands.pop();
                        ArithmeticExpression left = operands.pop();
                        operands.push(createExpression(operator, left, right));
                    }
                }
                operators.push(content);
            }
        }
        while (!operators.isEmpty()) {
            char operator = operators.pop();
            if (operator == 'c') {
                operands.push(new Cosinus(operands.pop()));
            } else {
                ArithmeticExpression right = operands.pop();
                ArithmeticExpression left = operands.pop();
                operands.push(createExpression(operator, left, right));
            }
        }
        return operands.pop();
    }

    public static String startLoop(String expr) {
        String simpleString = null;
        ArithmeticExpression parsedExpression = parse(expr);
        ArithmeticExpression loopExpression = parsedExpression.evaluate();
        Stack<String> allExpressions = new Stack<>();
        while (true) {
            loopExpression = loopExpression.evaluate();
            if (simpleString != null && (simpleString.equals(loopExpression.toString())
                    || allExpressions.search(simpleString) < 0)) {
                break;
            }
            simpleString = loopExpression.toString();
            allExpressions.push(simpleString);
        }
        if (simpleString.charAt(0) == '(' && simpleString.charAt(simpleString.length() - 1) == ')')
            simpleString = simpleString.substring(1, simpleString.length() - 1);
        return simpleString;
    }
}