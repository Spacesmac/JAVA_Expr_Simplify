package utils;

import java.util.HashMap;

public class ExpressionUtils {
    public static String emptyUselessOperators(String expression) {
        expression = expression.replaceAll("\\s", "");

        for (int i = 0; i < expression.length(); i++) {
            expression = expression.replace("--", "+");
            expression = expression.replace("+-", "-");
            expression = expression.replace("-+", "-");
            expression = expression.replace("++", "+");
            if (expression.charAt(i) >= '0' && expression.charAt(i) <= '9' && i + 1 < expression.length()
                    && expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z') {
                expression = expression.replace("" + expression.charAt(i) +
                        expression.charAt(i + 1),
                        "" + expression.charAt(i) + '*' + expression.charAt(i + 1));
                i += 2;
                continue;
            }
            if (expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z' && i + 3 < expression.length()
                    && expression.substring(i, i + 4).compareTo("cos(") == 0) {
                i += 3;
                for (; i < expression.length() && (expression.charAt(i) == '(' || expression.charAt(i) == ')'
                        || Character.isDigit(expression.charAt(i))); i++) {
                    if (expression.charAt(i) >= '0' && expression.charAt(i) <= '9' && i + 1 < expression.length()
                            && expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z') {
                        expression = expression.replace("" + expression.charAt(i) +
                                expression.charAt(i + 1),
                                "" + expression.charAt(i) + '*' + expression.charAt(i + 1));
                        i += 3;
                    }
                }
                i--;
                continue;
            }
            if (expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z' && i + 1 < expression.length()
                    && expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z') {
                expression = expression.replace("" + expression.charAt(i) + expression.charAt(i + 1),
                        "" + expression.charAt(i) + '*' + expression.charAt(i + 1));
            }
        }
        return expression;
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
}
