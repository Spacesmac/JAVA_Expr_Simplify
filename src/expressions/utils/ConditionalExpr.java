package expressions.utils;

import expressions.Addition;
import expressions.ArithmeticExpression;
import expressions.NumericConstant;
import expressions.Power;
import expressions.Subtraction;
import expressions.Variable;

public class ConditionalExpr {
    public boolean isTwoNumericConstant(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof NumericConstant && right instanceof NumericConstant)
            return true;
        return false;
    }

    public boolean isOneVarAndOneNum(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof NumericConstant && right instanceof Variable)
            return true;
        if (right instanceof NumericConstant && left instanceof Variable)
            return true;
        return false;
    }

    public boolean isTwoVariable(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof Variable && right instanceof Variable)
            return true;
        return false;
    }

    public boolean isOneAdditionandOneNum(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof Addition && right instanceof NumericConstant)
            return true;
        return false;
    }

    public boolean isOneAdditionandOneVar(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof Addition && right instanceof Variable)
            return true;
        return false;
    }

    public boolean isOneSubtractionandOneVar(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof Subtraction && right instanceof Variable)
            return true;
        return false;
    }

    public boolean isOneSubtractionandOneNum(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof Subtraction && right instanceof NumericConstant)
            return true;
        return false;
    }

    public boolean isTwoPower(ArithmeticExpression left, ArithmeticExpression right) {
        if (left instanceof Power && right instanceof Power)
            return true;
        return false;
    }

    public boolean isVariable(ArithmeticExpression left) {
        if (left instanceof Variable)
            return true;
        return false;
    }

    public boolean isNumeric(ArithmeticExpression left) {
        if (left instanceof NumericConstant)
            return true;
        return false;
    }

    public boolean isSameVariableName(Variable left, Variable right) {
        if (left.getName().compareTo(right.getName()) == 0)
            return true;
        return false;
    }

    public boolean isSameNumericValue(NumericConstant left, NumericConstant right) {
        if (left.getValue() == right.getValue())
            return true;
        return false;
    }

    public boolean isSameValue(ArithmeticExpression left, ArithmeticExpression right) {
        if (isTwoNumericConstant(left, right) && isSameNumericValue((NumericConstant) left, (NumericConstant) right))
            return true;
        return false;
    }

    public boolean isSameName(ArithmeticExpression left, ArithmeticExpression right) {
        if (isTwoVariable(left, right) && isSameVariableName((Variable) left, (Variable) right))
            return true;
        return false;
    }
}
