package expressions;

public class Variable implements ArithmeticExpression {
    private String name;
    private double x_value;

    public String getName() {
        return this.name;
    }

    private Variable(String name) {
        this.name = name;
        this.x_value = 1;
    }

    double getX_value() {
        return x_value;
    }

    public static ArithmeticExpression create(String name) {
        return new Variable(name);
    }

    void setX_value(double new_x_value) {
        x_value = new_x_value;
    }

    @Override
    public ArithmeticExpression evaluate() {
        return this;
    }

    public StringBuilder toStringBuilder() {
        StringBuilder str = new StringBuilder();
        if (x_value == 0) {
            return str;
        }
        if (x_value != 1) {
            if (x_value == (int) x_value) {
                str.append((int) x_value);
                str.append(name);
            } else {
                str.append(x_value);
                str.append(name);
            }
            return str;
        }
        str.append(name);
        return str;
    }

    @Override
    public String toString() {
        return toStringBuilder().toString();
    }
}