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

        if (simpleLeft instanceof NumericConstant && ((NumericConstant) simpleLeft).getValue() == 0) {
            return simpleRight;
        }
        if (simpleRight instanceof NumericConstant && ((NumericConstant) simpleRight).getValue() == 0) {
            return simpleLeft;
        }
        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = (new BigDecimal("" + ((NumericConstant) simpleLeft).getValue())
                    .add(new BigDecimal("" + ((NumericConstant) simpleRight).getValue())).doubleValue());
            return NumericConstant.create(result);
        }
        if (simpleLeft instanceof Variable && simpleRight instanceof Variable
                && ((Variable) simpleLeft).getName().compareTo(((Variable) simpleRight).getName()) == 0) {
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
        if (simpleLeft instanceof Power && simpleRight instanceof Power) {
            Power leftPow = ((Power) simpleLeft);
            Power rightPow = ((Power) simpleRight);
            if (leftPow.getbase() instanceof Variable && rightPow.getbase() instanceof Variable
                    && ((Variable) leftPow.getbase()).getName()
                            .compareTo(((Variable) rightPow.getbase()).getName()) == 0) {
                if (leftPow.getExponent() instanceof NumericConstant
                        && rightPow.getExponent() instanceof NumericConstant
                        && ((NumericConstant) leftPow.getExponent())
                                .getValue() == ((NumericConstant) leftPow.getExponent()).getValue()) {
                    return Power.create(new Addition(leftPow.getbase(), rightPow.getbase()), leftPow.getExponent());
                }
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