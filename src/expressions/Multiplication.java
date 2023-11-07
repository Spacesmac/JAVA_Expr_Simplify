package expressions;

public class Multiplication implements ArithmeticExpression {
    private ArithmeticExpression left;
    private ArithmeticExpression right;

    private Multiplication(ArithmeticExpression left, ArithmeticExpression right) {
        this.left = left;
        this.right = right;
    }

    public static ArithmeticExpression create(ArithmeticExpression left, ArithmeticExpression right) {
        return new Multiplication(left, right);
    }

    public ArithmeticExpression getLeft() {
        return this.left;
    }

    public ArithmeticExpression getRight() {
        return this.right;
    }

    private ArithmeticExpression technicalEvaluate(ArithmeticExpression simpleLeft, ArithmeticExpression simpleRight) {
        if (cond.isNumeric(simpleRight) || cond.isVariable(simpleRight)) {
            if (simpleLeft instanceof Addition) {
                ArithmeticExpression leftOperand = ((Addition) simpleLeft).getLeft();
                ArithmeticExpression rightOperand = ((Addition) simpleLeft).getRight();
                if ((cond.isNumeric(leftOperand) || cond.isVariable(leftOperand)) &&
                        (cond.isNumeric(rightOperand) || cond.isVariable(rightOperand))) {
                    ArithmeticExpression tmp = Addition.create(
                            new Multiplication(leftOperand, cond.isNumeric(simpleLeft) 
                                ? NumericConstant.create(((NumericConstant) simpleRight).getValue())
                                : Variable.create(((Variable) simpleRight).getName(), ((Variable) simpleRight).getX_value())),
                            new Multiplication(rightOperand, cond.isNumeric(simpleLeft)
                                ? NumericConstant.create(((NumericConstant) simpleRight).getValue())
                                : Variable.create(((Variable) simpleRight).getName(), ((Variable) simpleRight).getX_value())));
                    return tmp;
                }
            } else if (simpleLeft instanceof Subtraction) {
                ArithmeticExpression rightOperand = ((Subtraction) simpleLeft).getRight();
                ArithmeticExpression leftOperand = ((Subtraction) simpleLeft).getLeft();
                if ((cond.isNumeric(leftOperand) || cond.isVariable(leftOperand)) &&
                        (cond.isNumeric(rightOperand) || cond.isVariable(rightOperand))) {
                    ArithmeticExpression tmp = Subtraction.create(
                            new Multiplication(leftOperand, cond.isNumeric(simpleLeft)
                            ? NumericConstant.create(((NumericConstant) simpleRight).getValue())
                            : Variable.create(((Variable) simpleRight).getName(), ((Variable) simpleRight).getX_value())),
                            new Multiplication(rightOperand, cond.isNumeric(simpleLeft)
                            ? NumericConstant.create(((NumericConstant) simpleRight).getValue())
                            : Variable.create(((Variable) simpleRight).getName(), ((Variable) simpleRight).getX_value())));
                    return tmp;
                }
            }
        }

        if (cond.isNumeric(simpleLeft) || cond.isVariable(simpleLeft)) {
            if (simpleRight instanceof Addition) {
                ArithmeticExpression rightOperand = ((Addition) simpleRight).getRight();
                ArithmeticExpression leftOperand = ((Addition) simpleRight).getLeft();
                if ((cond.isNumeric(leftOperand) || cond.isVariable(leftOperand)) &&
                        (cond.isNumeric(rightOperand) || cond.isVariable(rightOperand))) {
                    ArithmeticExpression tmp = Addition.create(
                            new Multiplication(rightOperand, cond.isNumeric(simpleLeft)
                                ? NumericConstant.create(((NumericConstant) simpleLeft).getValue())
                                : Variable.create(((Variable) simpleLeft).getName(), ((Variable) simpleLeft).getX_value())),
                            new Multiplication(leftOperand, cond.isNumeric(simpleLeft)
                                ? NumericConstant.create(((NumericConstant) simpleLeft).getValue())
                                : Variable.create(((Variable) simpleLeft).getName(), ((Variable) simpleLeft).getX_value())));
                    return tmp;
                }
            } else if (simpleRight instanceof Subtraction) {
                ArithmeticExpression rightOperand = ((Subtraction) simpleRight).getRight();
                ArithmeticExpression leftOperand = ((Subtraction) simpleRight).getLeft();
                if ((cond.isNumeric(leftOperand) || cond.isVariable(leftOperand)) &&
                        (cond.isNumeric(rightOperand) || cond.isVariable(rightOperand))) {
                    ArithmeticExpression tmp = Subtraction.create(
                            new Multiplication(leftOperand, cond.isNumeric(simpleLeft) ? NumericConstant.create(((NumericConstant) simpleLeft).getValue()): Variable.create(((Variable) simpleLeft).getName(), ((Variable) simpleLeft).getX_value())),
                            new Multiplication(rightOperand, cond.isNumeric(simpleLeft) ? NumericConstant.create(((NumericConstant) simpleLeft).getValue()): Variable.create(((Variable) simpleLeft).getName(), ((Variable) simpleLeft).getX_value())));
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

        if (cond.isTwoNumericConstant(simpleLeft, simpleRight)) {
            double result = ((NumericConstant) simpleLeft).getValue()
                    * ((NumericConstant) simpleRight).getValue();
            return NumericConstant.create(result);
        }
        if (cond.isVariable(simpleRight) && cond.isOneVarAndOneNum(simpleLeft, simpleRight)) {
            
            ((Variable) simpleRight).setX_value(((NumericConstant) simpleLeft).getValue()
                    * ((Variable) simpleRight).getX_value());
            return simpleRight;
        }
        if (cond.isVariable(simpleLeft) && cond.isOneVarAndOneNum(simpleLeft, simpleRight)) {
            ((Variable) simpleLeft).setX_value(((NumericConstant) simpleRight).getValue()
                    * ((Variable) simpleLeft).getX_value());
            return simpleLeft;
        }
        if (cond.isSameName(simpleLeft, simpleRight)) {
            simpleRight = ((Variable) simpleRight).setX_value(((Variable) simpleLeft).getX_value()
                    + ((Variable) simpleRight).getX_value());
            return Power.create(simpleRight, NumericConstant.create(2));
        }
        if (simpleLeft instanceof Multiplication
                && (cond.isNumeric(simpleRight) || cond.isVariable(simpleRight))) {
            Multiplication leftmuMultiplication = (Multiplication) simpleLeft;
            return new Multiplication(leftmuMultiplication.getLeft(),
                    new Multiplication(leftmuMultiplication.getRight(), simpleRight));
        }
        ArithmeticExpression res = technicalEvaluate(simpleLeft, simpleRight);
        if (res != null) {
            return res;
        }
        return new Multiplication(simpleLeft, simpleRight);
    }

    public StringBuilder toStringBuilder() {
        StringBuilder str = new StringBuilder();

        str.append('(');
        str.append(left.toStringBuilder());
        str.append(" * ");
        str.append(right.toStringBuilder());
        str.append(')');
        return str;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}