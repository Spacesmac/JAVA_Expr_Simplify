package expressions;

import java.math.BigDecimal;

public class Subtraction implements ArithmeticExpression {
    private ArithmeticExpression left;
    private ArithmeticExpression right;

    private Subtraction(ArithmeticExpression left, ArithmeticExpression right) {
        this.left = left;
        this.right = right;
    }

    public static ArithmeticExpression create(ArithmeticExpression left, ArithmeticExpression right) {
        return new Subtraction(left, right);
    }

    public ArithmeticExpression getLeft() {
        return this.left;
    }

    public ArithmeticExpression getRight() {
        return this.right;
    }

    ArithmeticExpression simpleEvaluate(ArithmeticExpression simpleLeft, ArithmeticExpression simpleRight) {
        if (cond.isTwoNumericConstant(simpleLeft, simpleRight)) {
            double result = (new BigDecimal("" + ((NumericConstant) simpleLeft).getValue())
                    .subtract(new BigDecimal("" + ((NumericConstant) simpleRight).getValue())).doubleValue());
            return NumericConstant.create(result);
        }
        if (cond.isSameName(simpleLeft, simpleRight)) {
            ((Variable) simpleRight).setX_value((new BigDecimal("" + ((Variable) simpleLeft).getX_value())
                    .subtract(new BigDecimal("" + ((Variable) simpleRight).getX_value()))).doubleValue());
            if (((Variable) simpleRight).getX_value() == 0) {
                return NumericConstant.create(0);
            }
            return simpleRight;
        }
        return null;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = left.evaluate();
        ArithmeticExpression simpleRight = right.evaluate();
        ArithmeticExpression res = simpleEvaluate(simpleLeft, simpleRight);
        if (res != null)
            return res;
        if (cond.isOneAdditionandOneNum(simpleLeft, simpleRight)) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (cond.isNumeric(leftOperand.getRight())) {
                return Addition.create(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (cond.isNumeric(leftOperand.getLeft())) {
                return Addition.create(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        if (cond.isOneAdditionandOneVar(simpleLeft, simpleRight)) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (cond.isVariable(leftOperand.getRight())) {
                return Addition.create(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (cond.isVariable(leftOperand.getLeft())) {
                return Addition.create(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        if (cond.isOneSubtractionandOneNum(simpleLeft, simpleRight)) {
            Subtraction leftOperand = ((Subtraction) simpleLeft);
            if (cond.isNumeric(leftOperand.getRight())) {
                return new Subtraction(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (cond.isNumeric(leftOperand.getLeft())) {
                return new Subtraction(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        if (cond.isOneSubtractionandOneVar(simpleLeft, simpleRight)) {
            Subtraction leftOperand = ((Subtraction) simpleLeft);
            if (cond.isVariable(leftOperand.getRight())) {
                return new Subtraction(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (cond.isVariable(leftOperand.getLeft())) {
                return new Subtraction(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        return new Subtraction(simpleLeft, simpleRight);
    }

    public StringBuilder toStringBuilder() {
        StringBuilder str = new StringBuilder();
        str.append('(');
        str.append(left.toStringBuilder());
        str.append(" - ");
        str.append(right.toStringBuilder());
        str.append(')');
        return str;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}