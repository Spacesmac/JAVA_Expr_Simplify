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

    @Override
    public String toString() {
        if (value == (int) value) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }
}