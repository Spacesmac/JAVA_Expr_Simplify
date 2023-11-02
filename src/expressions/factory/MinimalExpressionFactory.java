package expressions.factory;

import expressions.*;

public class MinimalExpressionFactory implements ExpressionFactory {
    @Override
    public ArithmeticExpression createVariable(String name) {
        return Variable.create(name);
    }

    @Override
    public ArithmeticExpression createNumericConstant(double value) {
        return NumericConstant.create(value);
    }

    @Override
    public ArithmeticExpression createAddition(ArithmeticExpression left, ArithmeticExpression right) {
        return Addition.create(left, right);
    }

    @Override
    public ArithmeticExpression createMultiplication(ArithmeticExpression left, ArithmeticExpression right) {
        return Multiplication.create(left, right);
    }

    @Override
    public ArithmeticExpression createSubtraction(ArithmeticExpression left, ArithmeticExpression right) {
        return Subtraction.create(left, right);
    }

    @Override
    public ArithmeticExpression createDivision(ArithmeticExpression left, ArithmeticExpression right) {
        return Division.create(left, right);
    }

    @Override
    public ArithmeticExpression createPower(ArithmeticExpression left, ArithmeticExpression right) {
        return Power.create(left, right);
    }

    @Override
    public ArithmeticExpression createCosinus(ArithmeticExpression base) {
        return Cosinus.create(base);
    }

    @Override
    public ArithmeticExpression createSinus(ArithmeticExpression base) {
        return Sinus.create(base);
    }

    public ArithmeticExpression createFunction(Character c, ArithmeticExpression value) {
        if (c == 'c')
            return createCosinus(value);
        if (c == 's')
            return createSinus(value);
        return value;
    }
}
