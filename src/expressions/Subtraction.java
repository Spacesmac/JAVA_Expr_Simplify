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
        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = (new BigDecimal("" + ((NumericConstant) simpleLeft).getValue())
                    .subtract(new BigDecimal("" + ((NumericConstant) simpleRight).getValue())).doubleValue());
            return NumericConstant.create(result);
        }
        if (simpleLeft instanceof Variable && simpleRight instanceof Variable) {
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
        // System.out.println(simpleLeft.getClass() + " " + simpleRight.getClass());
        if (res != null)
            return res;
        if (simpleLeft instanceof Addition && simpleRight instanceof NumericConstant) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (leftOperand.getRight() instanceof NumericConstant) {
                return Addition.create(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof NumericConstant) {
                return Addition.create(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        if (simpleLeft instanceof Addition && simpleRight instanceof Variable) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (leftOperand.getRight() instanceof Variable) {
                return Addition.create(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof Variable) {
                return Addition.create(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        if (simpleLeft instanceof Subtraction && simpleRight instanceof NumericConstant) {
            Subtraction leftOperand = ((Subtraction) simpleLeft);
            if (leftOperand.getRight() instanceof NumericConstant) {
                return new Subtraction(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof NumericConstant) {
                return new Subtraction(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        if (simpleLeft instanceof Subtraction && simpleRight instanceof Variable) {
            Subtraction leftOperand = ((Subtraction) simpleLeft);
            if (leftOperand.getRight() instanceof Variable) {
                return new Subtraction(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof Variable) {
                return new Subtraction(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        return new Subtraction(simpleLeft, simpleRight);
    }

    @Override
    public String toString() {
        String leftString = left.toString();
        String rightString = right.toString();

        return "(" + leftString + " - " + rightString + ")";
    }
}