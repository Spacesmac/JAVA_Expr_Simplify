package expressions;

import java.math.BigDecimal;

public class Addition implements ArithmeticExpression {
    private ArithmeticExpression left;
    private ArithmeticExpression right;

    public Addition(ArithmeticExpression left, ArithmeticExpression right) {
        this.left = left;
        this.right = right;
    }

    public ArithmeticExpression getLeft() {
        return this.left;
    }

    public ArithmeticExpression getRight() {
        return this.right;
    }

    ArithmeticExpression simpleEvaluate(ArithmeticExpression simpleLeft, ArithmeticExpression simpleRight) {

        if (simpleLeft instanceof NumericConstant && ((NumericConstant) simpleLeft).getValue() == 0) {
            return simpleRight;
        }
        if (simpleRight instanceof NumericConstant && ((NumericConstant) simpleRight).getValue() == 0) {
            return simpleLeft;
        }
        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = (new BigDecimal("" + ((NumericConstant) simpleLeft).getValue())
                    .add(new BigDecimal("" + ((NumericConstant) simpleRight).getValue())).doubleValue());
            return new NumericConstant(result);
        }
        if (simpleLeft instanceof Variable && simpleRight instanceof Variable) {
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
        if (simpleLeft instanceof Addition && simpleRight instanceof NumericConstant) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (leftOperand.getRight() instanceof NumericConstant) {
                return new Addition(leftOperand.getLeft(), new Addition(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof NumericConstant) {
                return new Addition(leftOperand.getRight(), new Addition(leftOperand.getLeft(), simpleRight));
            }
        }
        if (simpleLeft instanceof Addition && simpleRight instanceof Variable) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (leftOperand.getRight() instanceof Variable) {
                return new Addition(leftOperand.getLeft(), new Addition(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof Variable) {
                return new Addition(leftOperand.getRight(), new Addition(leftOperand.getLeft(), simpleRight));
            }
        }
        return new Addition(simpleLeft, simpleRight);

    }

    @Override
    public String toString() {
        String leftString = left.toString();
        String rightString = right.toString();

        return "(" + leftString + " + " + rightString + ")";
    }
}