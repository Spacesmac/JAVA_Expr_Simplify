SRC=	./src/ArithmeticExpression.java	\
		./src/ExpressionParser.java	\
		./src/Main.java	\

NAME=Main

all:
	javac -d out $(SRC)

run:
	java -cp out $(NAME)

fast_run: all run

# Or run for exemple (java -cp out Main "3x*2+1") to change the expression as a parameter of the main
