package expressions;

public class Power implements ArithmeticExpression {
    private final ArithmeticExpression base;
    private final ArithmeticExpression exponent;

    private Power(ArithmeticExpression base, ArithmeticExpression exponent) {
        this.base = base;
        this.exponent = exponent;
    }

    public static ArithmeticExpression create(ArithmeticExpression left, ArithmeticExpression right) {
        return new Power(left, right);
    }

    public ArithmeticExpression getbase() {
        return this.base;
    }

    public ArithmeticExpression getExponent() {
        return this.exponent;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = base.evaluate();
        ArithmeticExpression simpleRight = exponent.evaluate();
        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = Math.pow(((NumericConstant) simpleLeft).getValue(),
                    ((NumericConstant) simpleRight).getValue());
            return NumericConstant.create(result);
        }
        return new Power(simpleLeft, simpleRight);
    }

    public StringBuilder toStringBuilder() {
        StringBuilder str = new StringBuilder();
        str.append('(');
        str.append(base.toStringBuilder());
        str.append(" ^ ");
        str.append(exponent.toStringBuilder());
        str.append(')');
        return str;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}