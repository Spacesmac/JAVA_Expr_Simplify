import java.util.HashMap;
import java.util.Stack;

public class ExpressionParser {
    private static int getPriority(char operator) {
        if (operator == '^') {
            return 3;
        } else if (operator == '*' || operator == '/') {
            return 2;
        } else if (operator == '+' || operator == '-') {
            return 1;
        }
        return 0;
    }

    public static HashMap<String, Integer> getNumberOfOperators(String expr) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("+", 0);
        map.put("-", 0);
        map.put("*", 0);
        map.put("/", 0);
        map.put("^", 0);
        map.put("n", 0);
        map.put("v", 0);
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '+') {
                map.put("+", map.get("+") + 1);
            }
            if (expr.charAt(i) == '-') {
                map.put("-", map.get("-") + 1);
            }
            if (expr.charAt(i) == '*') {
                map.put("*", map.get("*") + 1);
            }
            if (expr.charAt(i) == '/') {
                map.put("/", map.get("/") + 1);
            }
            if (expr.charAt(i) == '^') {
                map.put("^", map.get("^") + 1);
            }
            if (expr.charAt(i) >= 'a' && expr.charAt(i) <= 'z') {
                map.put("v", map.get("v") + 1);
            }
            if (expr.charAt(i) >= '0' && expr.charAt(i) <= '9') {
                for (; i < expr.length() && expr.charAt(i) >= '0' && expr.charAt(i) <= '0'; i++)
                    ;
                if (expr.charAt(i) >= 'a' && expr.charAt(i) <= 'z') {
                    map.put("v", map.get("v") + 1);
                } else {
                    map.put("n", map.get("n") + 1);
                }
                i++;
            }
        }
        return map;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static String emptyUselessOperators(String expression) {
        expression = expression.replaceAll("\\s", "");

        for (int i = 0; i < expression.length(); i++) {
            expression = expression.replace("--", "+");
            expression = expression.replace("+-", "-");
            expression = expression.replace("-+", "-");
            expression = expression.replace("++", "+");
            if (expression.charAt(i) >= '0' && expression.charAt(i) <= '9' && i + 1 < expression.length()
                    && expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z') {
                expression = expression.replace("" + expression.charAt(i) + expression.charAt(i + 1),
                        "" + expression.charAt(i) + '*' + expression.charAt(i + 1));
            }
            if (expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z' && i + 1 < expression.length()
                    && expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z') {
                expression = expression.replace("" + expression.charAt(i) + expression.charAt(i + 1),
                        "" + expression.charAt(i) + '*' + expression.charAt(i + 1));
            }
        }

        return expression;
    }

    public static ArithmeticExpression parse(String expression) {
        Stack<ArithmeticExpression> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();
        expression = emptyUselessOperators(expression);
        System.out.println(expression);
        for (int i = 0; i < expression.length(); i++) {
            char content = expression.charAt(i);
            if (Character.isDigit(content) || (content == '-' && i == 0 && i + 1 < expression.length())) {
                int j = i;
                boolean isNeg = false;
                if (content == '-')
                    isNeg = !isNeg;
                i++;
                for (; i < expression.length()
                        && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'); i++)
                    ;
                operands.push(new NumericConstant(Double.parseDouble(expression.substring(j, i))));
                i--;
            } else if (Character.isLetter(content)) {
                operands.push(new Variable(String.valueOf(content)));
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
            } else if (isOperator(content)) {
                while (!operators.isEmpty() && getPriority(operators.peek()) >= getPriority(content)) {
                    char operator = operators.pop();
                    ArithmeticExpression right = operands.pop();
                    ArithmeticExpression left = operands.pop();
                    operands.push(createExpression(operator, left, right));
                }
                operators.push(content);
            }
        }
        while (!operators.isEmpty()) {
            char operator = operators.pop();
            ArithmeticExpression right = operands.pop();
            ArithmeticExpression left = operands.pop();
            operands.push(createExpression(operator, left, right));
        }
        return operands.pop();
    }

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

    public static void main(String[] args) {
        try {
            String expression = "x+5*(y-2)";
            if (args.length > 0) {
                expression = args[0];
            }
            String simpleString = null;
            ArithmeticExpression parsedExpression = parse(expression);
            System.out.println("Parsed Expression: " + parsedExpression.toString());
            ArithmeticExpression loopExpression = parsedExpression.evaluate();
            System.out.println("Simple Expression: " + loopExpression.toString());
            Stack<String> allExpressions = new Stack<>();
            while (true) {
                loopExpression = loopExpression.evaluate();
                if (simpleString != null && (simpleString.equals(loopExpression.toString())
                        || allExpressions.search(simpleString) < 0)) {
                    break;
                }
                simpleString = loopExpression.toString();
                allExpressions.push(simpleString);
                System.out.println("Simplest Expression: " + simpleString);
            }
            if (simpleString.charAt(0) == '(' && simpleString.charAt(simpleString.length() - 1) == ')')
                simpleString = simpleString.substring(1, simpleString.length() - 1);
            System.out.println("Final result: " + simpleString);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}