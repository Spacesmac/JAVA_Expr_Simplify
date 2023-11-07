import java.util.Stack;

import utils.ExpressionUtils;
import utils.OperatorPriority;
import utils.ParserHandle;
import expressions.ArithmeticExpression;

public class ExpressionParser {
    ParserHandle handler;

    public ExpressionParser() {
        this.handler = new ParserHandle();
    }

    public ArithmeticExpression parse(String expression) {
        Stack<ArithmeticExpression> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();
        expression = ExpressionUtils.emptyUselessOperators(expression);

        for (int i = 0; i < expression.length(); i++) {
            char content = expression.charAt(i);
            
            if (Character.isDigit(content) || content == '-' && (i == 0 || OperatorPriority.isOperator(expression.charAt(i - 1))) && i + 1 < expression.length()) {
                i = handler.handleIsNumber(i, expression, operands, content);
            } else if (Character.isLetter(content)) {
                i = handler.handleIsLetter(i, expression, operands, operators, content);
            } else if (content == '(') {
                operators.push(content);
            }
            else if (content == ')')
                handler.handleClosingParenthesis(operands, operators);
            else if (OperatorPriority.isOperator(content))
                handler.handleOperator(operands, operators, content);
        }
        while (!operators.isEmpty())
            handler.handleRemainingOperators(operands, operators);
        return operands.pop();
    }

    public String startLoop(String expr) {
        String simpleString = null;
        ArithmeticExpression parsedExpression = parse(expr);
        ArithmeticExpression loopExpression = parsedExpression.evaluate();
        Stack<String> allExpressions = new Stack<>();
        if (expr.chars().filter(ch->ch == '(').count() != expr.chars().filter(ch->ch == ')').count())
            return "Error: Missing parenthesis";
        while (true) {
            loopExpression = loopExpression.evaluate();
            if (simpleString != null && (simpleString.equals(loopExpression.toString()) || allExpressions.search(simpleString) < 0))
                break;
            simpleString = loopExpression.toString();
            allExpressions.push(simpleString);
        }
        if (simpleString.charAt(0) == '(' && simpleString.charAt(simpleString.length() - 1) == ')')
            simpleString = simpleString.substring(1, simpleString.length() - 1);
        return simpleString;
    }
}