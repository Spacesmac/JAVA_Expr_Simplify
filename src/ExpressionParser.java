import java.util.Stack;

import utils.ExpressionUtils;
import utils.OperatorPriority;
import expressions.ArithmeticExpression;
import expressions.factory.ExpressionFactory;
import expressions.factory.MinimalExpressionFactory;

public class ExpressionParser {
    ExpressionFactory factory;

    public ExpressionParser() {
        this.factory = new MinimalExpressionFactory();
    }

    private ArithmeticExpression createExpression(char operator, ArithmeticExpression left,
            ArithmeticExpression right) {
        if (operator == '+') {
            return factory.createAddition(left, right);
        } else if (operator == '-') {
            return factory.createSubtraction(left, right);
        } else if (operator == '*') {
            return factory.createMultiplication(left, right);
        } else if (operator == '/') {
            return factory.createDivision(left, right);
        } else if (operator == '^') {
            return factory.createPower(left, right);
        }
        return null;
    }

    public ArithmeticExpression parse(String expression) {
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
                operands.push(factory.createNumericConstant(isNeg ? -value : value));
                i--;
            } else if (Character.isLetter(content)) {
                if (i + 3 < expression.length() && expression.substring(i, i + 3).equals("cos")) {
                    i += 2;
                    operators.push('c');
                } else if (i + 3 < expression.length() && expression.substring(i, i + 3).equals("sin")) {
                    i += 2;
                    operators.push('s');
                } else {
                    operands.push(factory.createVariable(String.valueOf(content)));
                }
            } else if (content == '(') {
                operators.push(content);
            } else if (content == ')') {
                while (operators.peek() != '(') {
                    char operator = operators.pop();
                    ArithmeticExpression right = operands.pop();
                    ArithmeticExpression left = operands.pop();
                    operands.push(createExpression(operator, left, right));

                }
                operators.pop();
                char op = operators.peek();
                if (op == 'c') {
                    operators.pop();
                    operands.push(factory.createCosinus(operands.pop()));
                } else if (op == 's') {
                    operators.pop();
                    operands.push(factory.createSinus(operands.pop()));
                }
            } else if (OperatorPriority.isOperator(content)) {
                while (!operators.isEmpty()
                        && OperatorPriority.getPriority(operators.peek()) >= OperatorPriority.getPriority(content)) {
                    char operator = operators.pop();
                    if (operator == 'c') {
                        operands.push(factory.createCosinus(operands.pop()));
                    } else if (operator == 's') {
                        operands.push(factory.createSinus(operands.pop()));
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
                operands.push(factory.createCosinus(operands.pop()));
            } else if (operator == 's') {
                operands.push(factory.createSinus(operands.pop()));
            } else {
                ArithmeticExpression right = operands.pop();
                ArithmeticExpression left = operands.pop();
                operands.push(createExpression(operator, left, right));
            }
        }
        return operands.pop();
    }

    public String startLoop(String expr) {
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