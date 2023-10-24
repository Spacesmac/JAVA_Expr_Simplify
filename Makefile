SRC=	./src/ArithmeticExpression.java	\
		./src/ExpressionParser.java	\

NAME=ExpressionParser

all:
	javac -d out $(SRC)

run:
	java -cp out $(NAME)
# Or run for exemple (java -cp out ExpressionParser "3x*2+1") to change the expression as a parameter of the main
