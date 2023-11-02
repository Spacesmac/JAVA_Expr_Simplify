package expressions;

public class NumericConstant implements ArithmeticExpression {
    private double value;

    private NumericConstant(double value) {
        this.value = value;
    }

    public static ArithmeticExpression create(double val) {
        return new NumericConstant(val);
    }

    @Override
    public ArithmeticExpression evaluate() {
        return this;
    }

    public StringBuilder toStringBuilder() {
        StringBuilder str = new StringBuilder();
        if (value == (int) value) {
            str.append((int) value);
            return str;
        }
        str.append(value);
        return str;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    public double getValue() {
        return value;
    }
}