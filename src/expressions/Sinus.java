package expressions;

public class Sinus implements ArithmeticExpression {
    private ArithmeticExpression base;

    private Sinus(ArithmeticExpression base) {
        this.base = base;
    }

    public static ArithmeticExpression create(ArithmeticExpression base) {
        return new Sinus(base);
    }

    public ArithmeticExpression getbase() {
        return this.base;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = base.evaluate();
        if (simpleLeft instanceof NumericConstant) {
            double result = Math.sin((((NumericConstant) simpleLeft).getValue()));
            return NumericConstant.create(result);
        }
        return new Sinus(simpleLeft);
    }

    @Override
    public String toString() {
        return "sin(" + base.toString() + ")";
    }
}