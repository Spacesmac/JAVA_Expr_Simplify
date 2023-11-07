public class Main {
    public static void main(String[] args) {
        try {
            ExpressionParser parser = new ExpressionParser();
            String expression = "2(2x+4)";

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
