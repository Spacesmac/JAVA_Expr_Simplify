package expressions;

public class Multiplication implements ArithmeticExpression {
    private ArithmeticExpression left;
    private ArithmeticExpression right;

    public Multiplication(ArithmeticExpression left, ArithmeticExpression right) {
        this.left = left;
        this.right = right;
    }

    public ArithmeticExpression getLeft() {
        return this.left;
    }

    public ArithmeticExpression getRight() {
        return this.right;
    }

    private ArithmeticExpression technicalEvaluate(ArithmeticExpression simpleLeft, ArithmeticExpression simpleRight) {
        if (simpleRight instanceof NumericConstant) {
            double constant = ((NumericConstant) simpleRight).getValue();
            if (simpleLeft instanceof Addition) {
                ArithmeticExpression leftOperand = ((Addition) simpleLeft).getLeft();
                ArithmeticExpression rightOperand = ((Addition) simpleLeft).getRight();
                if ((leftOperand instanceof NumericConstant || leftOperand instanceof Variable) &&
                        (rightOperand instanceof NumericConstant || rightOperand instanceof Variable)) {
                    ArithmeticExpression tmp = new Addition(
                            new Multiplication(leftOperand, new NumericConstant(constant)),
                            new Multiplication(rightOperand, new NumericConstant(constant)));
                    return tmp;
                }
            } else if (simpleLeft instanceof Subtraction) {
                ArithmeticExpression rightOperand = ((Subtraction) simpleLeft).getRight();
                ArithmeticExpression leftOperand = ((Subtraction) simpleLeft).getLeft();
                if ((leftOperand instanceof NumericConstant || leftOperand instanceof Variable) &&
                        (rightOperand instanceof NumericConstant || rightOperand instanceof Variable)) {
                    ArithmeticExpression tmp = new Subtraction(
                            new Multiplication(leftOperand, new NumericConstant(constant)),
                            new Multiplication(rightOperand, new NumericConstant(constant)));
                    return tmp;
                }
            }
        }

        if (simpleLeft instanceof NumericConstant) {
            double constant = ((NumericConstant) simpleLeft).getValue();
            if (simpleRight instanceof Addition) {
                ArithmeticExpression rightOperand = ((Addition) simpleRight).getRight();
                ArithmeticExpression leftOperand = ((Addition) simpleRight).getLeft();
                if ((leftOperand instanceof NumericConstant || leftOperand instanceof Variable) &&
                        (rightOperand instanceof NumericConstant || rightOperand instanceof Variable)) {
                    ArithmeticExpression tmp = new Addition(
                            new Multiplication(rightOperand, new NumericConstant(constant)),
                            new Multiplication(leftOperand, new NumericConstant(constant)));
                    return tmp;
                }
            } else if (simpleRight instanceof Subtraction) {
                ArithmeticExpression rightOperand = ((Subtraction) simpleRight).getRight();
                ArithmeticExpression leftOperand = ((Subtraction) simpleRight).getLeft();
                if ((leftOperand instanceof NumericConstant || leftOperand instanceof Variable) &&
                        (rightOperand instanceof NumericConstant || rightOperand instanceof Variable)) {
                    ArithmeticExpression tmp = new Subtraction(
                            new Multiplication(leftOperand, new NumericConstant(constant)),
                            new Multiplication(rightOperand, new NumericConstant(constant)));
                    return tmp;
                }
            }
        }
        return null;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = left.evaluate();
        ArithmeticExpression simpleRight = right.evaluate();

        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = ((NumericConstant) simpleLeft).getValue()
                    * ((NumericConstant) simpleRight).getValue();
            return new NumericConstant(result);
        }
        if (simpleLeft instanceof NumericConstant && simpleRight instanceof Variable) {
            ((Variable) simpleRight).setX_value(((NumericConstant) simpleLeft).getValue()
                    * ((Variable) simpleRight).getX_value());

            return simpleRight;
        }
        if (simpleRight instanceof NumericConstant && simpleLeft instanceof Variable) {
            ((Variable) simpleLeft).setX_value(((NumericConstant) simpleRight).getValue()
                    * ((Variable) simpleLeft).getX_value());
            return simpleLeft;
        }

        System.out.println(simpleLeft.getClass() + " " + simpleRight.getClass());

        ArithmeticExpression res = technicalEvaluate(simpleLeft, simpleRight);
        if (res != null) {
            return res;
        }
        return new Multiplication(simpleLeft, simpleRight);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " * " + right.toString() + ")";
    }
}