import java.math.BigDecimal;

interface ArithmeticExpression {
    ArithmeticExpression evaluate();

    String toString();
}

// NumericConstant
class NumericConstant implements ArithmeticExpression {
    private double value;

    public NumericConstant(double value) {
        this.value = value;
    }

    @Override
    public ArithmeticExpression evaluate() {
        return this;
    }

    @Override
    public String toString() {
        if (value == (int) value) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }
}

// Variable
class Variable implements ArithmeticExpression {
    private String name;
    private double x_value;

    public String getName() {
        return this.name;
    }

    public Variable(String name) {
        this.name = name;
        this.x_value = 1;
    }

    double getX_value() {
        return x_value;
    }

    void setX_value(double new_x_value) {
        x_value = new_x_value;
    }

    @Override
    public ArithmeticExpression evaluate() {
        return this;
    }

    @Override
    public String toString() {
        if (x_value == 0) {
            return "";
        }
        if (x_value != 1) {
            if (x_value == (int) x_value) {
                return ((int) x_value) + name;
            }
            return x_value + name;
        }
        return name;
    }
}

// Addition
class Addition implements ArithmeticExpression {
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

// Subtraction
class Subtraction implements ArithmeticExpression {
    private ArithmeticExpression left;
    private ArithmeticExpression right;

    public Subtraction(ArithmeticExpression left, ArithmeticExpression right) {
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
        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = (new BigDecimal("" + ((NumericConstant) simpleLeft).getValue())
                    .subtract(new BigDecimal("" + ((NumericConstant) simpleRight).getValue())).doubleValue());
            return new NumericConstant(result);
        }
        if (simpleLeft instanceof Variable && simpleRight instanceof Variable) {
            ((Variable) simpleRight).setX_value((new BigDecimal("" + ((Variable) simpleLeft).getX_value())
                    .subtract(new BigDecimal("" + ((Variable) simpleRight).getX_value()))).doubleValue());
            if (((Variable) simpleRight).getX_value() == 0) {
                return new NumericConstant(0);
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
                return new Addition(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof NumericConstant) {
                return new Addition(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
            }
        }
        if (simpleLeft instanceof Addition && simpleRight instanceof Variable) {
            Addition leftOperand = ((Addition) simpleLeft);
            if (leftOperand.getRight() instanceof Variable) {
                return new Addition(leftOperand.getLeft(), new Subtraction(leftOperand.getRight(), simpleRight));
            } else if (leftOperand.getLeft() instanceof Variable) {
                return new Addition(leftOperand.getRight(), new Subtraction(leftOperand.getLeft(), simpleRight));
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

// Multiplication
class Multiplication implements ArithmeticExpression {
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

// Division
class Division implements ArithmeticExpression {
    private ArithmeticExpression left;
    private ArithmeticExpression right;

    public Division(ArithmeticExpression left, ArithmeticExpression right) {
        this.left = left;
        this.right = right;
    }

    public ArithmeticExpression getLeft() {
        return this.left;
    }

    public ArithmeticExpression getRight() {
        return this.right;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = left.evaluate();
        ArithmeticExpression simpleRight = right.evaluate();

        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = ((NumericConstant) simpleLeft).getValue()
                    / ((NumericConstant) simpleRight).getValue();
            return new NumericConstant(result);
        }

        return new Division(simpleLeft, simpleRight);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " / " + right.toString() + ")";
    }
}

// Power
class Power implements ArithmeticExpression {
    private ArithmeticExpression base;
    private ArithmeticExpression exponent;

    public Power(ArithmeticExpression base, ArithmeticExpression exponent) {
        this.base = base;
        this.exponent = exponent;
    }

    public ArithmeticExpression getbase() {
        return this.base;
    }

    public ArithmeticExpression getExponent() {
        return this.exponent;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = base.evaluate();
        ArithmeticExpression simpleRight = exponent.evaluate();
        if (simpleLeft instanceof NumericConstant && simpleRight instanceof NumericConstant) {
            double result = Math.pow(((NumericConstant) simpleLeft).getValue(),
                    ((NumericConstant) simpleRight).getValue());
            return new NumericConstant(result);
        }
        return new Power(simpleLeft, simpleRight);
    }

    @Override
    public String toString() {
        return "(" + base.toString() + " ^ " + exponent.toString() + ")";
    }
}

// Cosinus
class Cosinus implements ArithmeticExpression {
    private ArithmeticExpression base;

    public Cosinus(ArithmeticExpression base) {
        this.base = base;
    }

    public ArithmeticExpression getbase() {
        return this.base;
    }

    @Override
    public ArithmeticExpression evaluate() {
        ArithmeticExpression simpleLeft = base.evaluate();
        if (simpleLeft instanceof NumericConstant) {
            double result = Math.cos((((NumericConstant) simpleLeft).getValue()));
            return new NumericConstant(result);
        }
        return new Cosinus(simpleLeft);
    }

    @Override
    public String toString() {
        return "cos(" + base.toString() + ")";
    }
}