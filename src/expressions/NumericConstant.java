package expressions;

public class NumericConstant implements ArithmeticExpression {
    private double value;

    public NumericConstant(double value) {
        this.value = value;
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