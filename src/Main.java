import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        try {
            String expression = "2+cos(4)";
            // expression = "(2+2)/8";
            if (args.length > 0) {
                expression = args[0];
            }

            String simpleString = ExpressionParser.startLoop(expression);

            System.out.println("Final result: " + simpleString);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
