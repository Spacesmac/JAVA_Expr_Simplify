package expressions;

public class Cosinus implements ArithmeticExpression {
    private ArithmeticExpression base;

    private Cosinus(ArithmeticExpression base) {
        this.base = base;
    }

    public static ArithmeticExpression create(ArithmeticExpression base) {
        return new Cosinus(base);
    }

    public ArithmeticExpression getbase() {
        return this.base;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = base.evaluate();
        if (simpleLeft instanceof NumericConstant) {
            double result = Math.cos((((NumericConstant) simpleLeft).getValue()));
            return NumericConstant.create(result);
        }
        return new Cosinus(simpleLeft);
    }

    @Override
    public String toString() {
        return "cos(" + base.toString() + ")";
    }
}