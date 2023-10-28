SRC=	./src/ExpressionParser.java	\
		./src/Main.java	\
		./src/utils/ExpressionUtils.java	\
		./src/utils/OperatorPriority.java	\
		./src/expressions/*	\

NAME=Main

all:
	javac -d out $(SRC)

run:
	java -cp out $(NAME)

fast_run: all run

# Or run for exemple (java -cp out Main "3x*2+1") to change the expression as a parameter of the main
