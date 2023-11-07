package expressions;

public class Division implements ArithmeticExpression {
    private final ArithmeticExpression left;
    private final ArithmeticExpression right;

    private Division(ArithmeticExpression left, ArithmeticExpression right) {
        this.left = left;
        this.right = right;
    }

    public static ArithmeticExpression create(ArithmeticExpression left, ArithmeticExpression right) {
        return new Division(left, right);
    }

    public ArithmeticExpression getLeft() {
        return this.left;
    }

    public ArithmeticExpression getRight() {
        return this.right;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = left.evaluate();
        ArithmeticExpression simpleRight = right.evaluate();

        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = ((NumericConstant) simpleLeft).getValue()
                    / ((NumericConstant) simpleRight).getValue();
            return NumericConstant.create(result);
        }

        return new Division(simpleLeft, simpleRight);
    }

    public StringBuilder toStringBuilder() {
        StringBuilder str = new StringBuilder();
        str.append('(');
        str.append(left.toStringBuilder());
        str.append(" / ");
        str.append(right.toStringBuilder());
        str.append(')');
        return str;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}