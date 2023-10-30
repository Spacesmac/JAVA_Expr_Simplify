SRC=	./src/ExpressionParser.java	\
		./src/Main.java	\
		./src/utils/ExpressionUtils.java	\
		./src/utils/OperatorPriority.java	\
		./src/expressions/*.java	\
		./src/expressions/factory/*.java	\

NAME=Main


all:
	javac -d out $(SRC)

run:
	java -cp out $(NAME) $(ARGS)

fast_run: all run

# You can pass arguments of the main by typing for exemple: make run ARGS="2+2"
# Or run for exemple (java -cp out Main "3x*2+1") to change the expression as a parameter of the main
