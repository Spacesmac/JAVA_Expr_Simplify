package expressions;

public class Cosinus implements ArithmeticExpression {
    private ArithmeticExpression base;

    public Cosinus(ArithmeticExpression base) {
        this.base = base;
    }

    public ArithmeticExpression getbase() {
        return this.base;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = base.evaluate();
        if (simpleLeft instanceof NumericConstant) {
            double result = Math.cos((((NumericConstant) simpleLeft).getValue()));
            return new NumericConstant(result);
        }
        return new Cosinus(simpleLeft);
    }

    @Override
    public String toString() {
        return "cos(" + base.toString() + ")";
    }
}