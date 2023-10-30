public class Main {
    public static void main(String[] args) {
        try {
            ExpressionParser parser = new ExpressionParser();
            String expression = "3x*x+2x*x";
            // expression = "(2+2)/8";
            if (args.length > 0) {
                expression = args[0];
            }

            String simpleString = parser.startLoop(expression);

            System.out.println("Final result: " + simpleString);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
