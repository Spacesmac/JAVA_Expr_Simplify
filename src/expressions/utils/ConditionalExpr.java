package expressions.utils;

import expressions.Addition;
import expressions.ArithmeticExpression;
import expressions.NumericConstant;
import expressions.Power;
import expressions.Subtraction;
import expressions.Variable;

public class ConditionalExpr {
    public boolean isTwoNumericConstant(ArithmeticExpression left, ArithmeticExpression right) {
        return left instanceof NumericConstant && right instanceof NumericConstant;
    }

    public boolean isOneVarAndOneNum(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof NumericConstant && right instanceof Variable)
            return true;
        return right instanceof NumericConstant && left instanceof Variable;
    }

    public boolean isTwoVariable(ArithmeticExpression left, ArithmeticExpression right) {
        return left instanceof Variable && right instanceof Variable;
    }

    public boolean isOneAdditionandOneNum(ArithmeticExpression left, ArithmeticExpression right) {
        return left instanceof Addition && right instanceof NumericConstant;
    }

    public boolean isOneAdditionandOneVar(ArithmeticExpression left, ArithmeticExpression right) {
        return left instanceof Addition && right instanceof Variable;
    }

    public boolean isOneSubtractionandOneVar(ArithmeticExpression left, ArithmeticExpression right) {
        return left instanceof Subtraction && right instanceof Variable;
    }

    public boolean isOneSubtractionandOneNum(ArithmeticExpression left, ArithmeticExpression right) {
        return left instanceof Subtraction && right instanceof NumericConstant;
    }

    public boolean isTwoPower(ArithmeticExpression left, ArithmeticExpression right) {
        return left instanceof Power && right instanceof Power;
    }

    public boolean isVariable(ArithmeticExpression left) {
        return left instanceof Variable;
    }

    public boolean isNumeric(ArithmeticExpression left) {
        return left instanceof NumericConstant;
    }

    public boolean isSameVariableName(Variable left, Variable right) {
        return left.getName().compareTo(right.getName()) == 0;
    }

    public boolean isSameNumericValue(NumericConstant left, NumericConstant right) {
        return left.getValue() == right.getValue();
    }

    public boolean isSameValue(ArithmeticExpression left, ArithmeticExpression right) {
        return isTwoNumericConstant(left, right) && isSameNumericValue((NumericConstant) left, (NumericConstant) right);
    }

    public boolean isSameName(ArithmeticExpression left, ArithmeticExpression right) {
        return isTwoVariable(left, right) && isSameVariableName((Variable) left, (Variable) right);
    }
}
