package expressions;

import java.math.BigDecimal;

public class Addition implements ArithmeticExpression {
    private ArithmeticExpression left;
    private ArithmeticExpression right;

    private Addition(ArithmeticExpression left, ArithmeticExpression right) {
        this.left = left;
        this.right = right;
    }

    public static ArithmeticExpression create(ArithmeticExpression left, ArithmeticExpression right) {
        return new Addition(left, right);
    }

    public ArithmeticExpression getLeft() {
        return this.left;
    }

    public ArithmeticExpression getRight() {
        return this.right;
    }

    ArithmeticExpression simpleEvaluate(ArithmeticExpression simpleLeft, ArithmeticExpression simpleRight) {
        if (cond.isNumeric(simpleLeft) && ((NumericConstant) simpleLeft).getValue() == 0) {
            return simpleRight;
        }
        if (cond.isNumeric(simpleRight) && ((NumericConstant) simpleRight).getValue() == 0) {
            return simpleLeft;
        }
        if (cond.isTwoNumericConstant(simpleLeft, simpleRight)) {
            double result = (new BigDecimal("" + ((NumericConstant) simpleLeft).getValue())
                    .add(new BigDecimal("" + ((NumericConstant) simpleRight).getValue())).doubleValue());
            return NumericConstant.create(result);
        }
        if (cond.isSameName(simpleLeft, simpleRight)) {
            ((Variable) simpleRight).setX_value((new BigDecimal("" + ((Variable) simpleLeft).getX_value())
                    .add(new BigDecimal("" + ((Variable) simpleRight).getX_value()))).doubleValue());
            return simpleRight;
        }
        return null;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = left.evaluate();
        ArithmeticExpression simpleRight = right.evaluate();
        ArithmeticExpression res = simpleEvaluate(simpleLeft, simpleRight);
        // System.out.println(simpleLeft.getClass() + " " + simpleRight.getClass());
        if (res != null)
            return res;
        res = simpleEvaluate(simpleRight, simpleLeft);
        if (res != null)
            return res;
        if (cond.isOneAdditionandOneNum(simpleLeft, simpleRight)) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (cond.isNumeric(leftOperand.getRight())) {
                return new Addition(leftOperand.getLeft(), new Addition(leftOperand.getRight(), simpleRight));
            } else if (cond.isNumeric(leftOperand.getLeft())) {
                return new Addition(leftOperand.getRight(), new Addition(leftOperand.getLeft(), simpleRight));
            }
        }
        if (cond.isOneAdditionandOneVar(simpleLeft, simpleRight)) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (cond.isVariable(leftOperand.getRight())) {
                return new Addition(leftOperand.getLeft(), new Addition(leftOperand.getRight(), simpleRight));
            } else if (cond.isVariable(leftOperand.getLeft())) {
                return new Addition(leftOperand.getRight(), new Addition(leftOperand.getLeft(), simpleRight));
            }
        }
        if (cond.isTwoPower(simpleLeft, simpleRight)) {
            Power leftPow = ((Power) simpleLeft);
            Power rightPow = ((Power) simpleRight);
            if (cond.isSameName(leftPow.getbase(), rightPow.getbase())
                    && cond.isSameValue(leftPow.getExponent(), rightPow.getExponent())) {
                return Power.create(new Addition(leftPow.getbase(), rightPow.getbase()), leftPow.getExponent());
            }
        }
        return new Addition(simpleLeft, simpleRight);

    }

    public StringBuilder toStringBuilder() {
        StringBuilder str = new StringBuilder();
        str.append('(');
        str.append(left.toStringBuilder());
        str.append(" + ");
        str.append(right.toStringBuilder());
        str.append(')');
        return str;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}