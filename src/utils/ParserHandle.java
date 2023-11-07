package utils;

import java.util.Stack;

import expressions.ArithmeticExpression;
import expressions.factory.ExpressionFactory;
import expressions.factory.MinimalExpressionFactory;

public class ParserHandle {
    ExpressionFactory factory;
    public ParserHandle() {
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
    
    public void handleNumericConstant(String expression, Stack<ArithmeticExpression> operands) {
        double value = Double.parseDouble(expression);
        operands.push(factory.createNumericConstant(value));
    }

    public void handleVariable(Stack<ArithmeticExpression> operands, char content, double value) {
        operands.push(factory.createVariable(String.valueOf(content), value));
    }

    public int handleIsNumber(int i, String  expression, Stack<ArithmeticExpression> operands, Stack<Character> operators, char content) {
        int j = i;
        boolean isNeg = (content == '-');
        if (isNeg)
            i++;
        while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
            i++;
        }
        if (i < expression.length() && Character.isLetter(expression.charAt(i)) && expression.charAt(i) != 'c' && expression.charAt(i) !='s') {
            handleVariable(operands, expression.charAt(i), Double.parseDouble(expression.substring(j, i)));
            return i;
        }
        handleNumericConstant(expression.substring(j, i), operands);
        i--;
        return i;
    }

    public int handleIsLetter(int i, String  expression, Stack<ArithmeticExpression> operands, Stack<Character> operators, char content) {
        if (i + 3 < expression.length() && expression.substring(i, i + 3).equals("cos")) {
            i += 2;
            operators.push('c');
        } else if (i + 3 < expression.length() && expression.substring(i, i + 3).equals("sin")) {
            i += 2;
            operators.push('s');
        } else
            handleVariable(operands, content, 1);
        return i;
    }

    public void handleClosingParenthesis(Stack<ArithmeticExpression> operands, Stack<Character> operators) {
        while (operators.peek() != '(') {
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
        operators.pop();
        if (!operators.isEmpty()) {
            char op = operators.peek();
            if (op == 'c' || op == 's') {
                operators.pop();
                operands.push(factory.createFunction(op, operands.pop()));
            }
        }
    }

    public void handleOperator(Stack<ArithmeticExpression> operands, Stack<Character> operators, char content) {
        while (!operators.isEmpty() && OperatorPriority.getPriority(operators.peek()) >= OperatorPriority.getPriority(content)) {
            char operator = operators.pop();
            if (operator == 'c' || operator == 's') {
                operands.push(factory.createFunction(operator, operands.pop()));
            } else {
                ArithmeticExpression right = operands.pop();
                ArithmeticExpression left = operands.pop();
                operands.push(createExpression(operator, left, right));
            }
        }
        operators.push(content);
    }
    
    public void handleRemainingOperators(Stack<ArithmeticExpression> operands, Stack<Character> operators) {
        char operator = operators.pop();
        if (operator == 'c' || operator == 's') {
            operands.push(factory.createFunction(operator, operands.pop()));
        } else {
            ArithmeticExpression right = operands.pop();
            ArithmeticExpression left = operands.pop();
            operands.push(createExpression(operator, left, right));
        }
    }
    
}
