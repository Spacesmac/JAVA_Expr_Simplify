package expressions.factory;

import expressions.ArithmeticExpression;

public interface ExpressionFactory {
    ArithmeticExpression createVariable(String name);

    ArithmeticExpression createNumericConstant(double value);

    ArithmeticExpression createAddition(ArithmeticExpression left, ArithmeticExpression right);

    ArithmeticExpression createMultiplication(ArithmeticExpression left, ArithmeticExpression right);

    ArithmeticExpression createSubtraction(ArithmeticExpression left, ArithmeticExpression right);

    ArithmeticExpression createDivision(ArithmeticExpression left, ArithmeticExpression right);

    ArithmeticExpression createPower(ArithmeticExpression left, ArithmeticExpression right);

    ArithmeticExpression createCosinus(ArithmeticExpression base);

    ArithmeticExpression createSinus(ArithmeticExpression base);
}
