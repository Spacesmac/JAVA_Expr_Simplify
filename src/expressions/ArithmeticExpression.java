package expressions;

import expressions.utils.ConditionalExpr;

public interface ArithmeticExpression {
    ArithmeticExpression evaluate();

    ConditionalExpr cond = new ConditionalExpr();

    StringBuilder toStringBuilder();

    String toString();
}