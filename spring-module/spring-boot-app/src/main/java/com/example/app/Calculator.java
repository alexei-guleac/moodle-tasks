package com.example.app;

import java.util.EmptyStackException;
import java.util.StringTokenizer;

/**
 * Stack-based Calculator class
 *
 * @author Eric Bakan
 */
class Calculator {

    private final Stack<String> output; //output stack
    private final Stack<operator> operators; //operator stack

    /**
     * Default constructor
     * Initializes output and operator stacks
     */
    public Calculator() {
        output = new Stack<String>();
        operators = new Stack<operator>();
    }

    /**
     * Calculates the value of an expression
     *
     * @param expr expression to evaluate
     * @return value of expression
     */
    public double calculate(String expr) {
        parseString(expr);
        return evaluateOutput();
    }

    /**
     * Parses a String containing an
     * infix equation and stores the
     * RPN equivalent in output
     *
     * @param expr String to parse
     */
    private void parseString(String expr) {
        //rm trailing spaces
        expr = expr.replaceAll("\\s+", "");
        //add whitespace in
        for (int i = 0; i < operator.operators.length(); i++) {
            String str = "" + operator.operators.charAt(i);
            //handle caret as explicit, not identifier
            if (str.charAt(0) == '^')
                str = "\\^";
            expr = expr.replaceAll("[" + str + "]", " " + str + " ");
        }
        StringTokenizer tokenizer = new StringTokenizer(expr);
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            if (isNum(str))
                output.push(str);
            else {
                operator op = operator.getOperator(str.charAt(0));
                if (op.equals(operator.lpar)) {
                    operators.push(op);
                } else if (op.equals(operator.rpar)) {
                    try {
                        while (!operators.peek().equals(operator.lpar)) {
                            String e = operators.pop().toString();
                            output.push(e);
                        }
                        operators.pop();
                        /*if (!isNum(operators.peek().toString()) && isBrace(operators.peek().toString())) {
                            String e = operators.pop().toString();
                            output.push(e);
                        }*/
                    } catch (EmptyStackException e) {
                        throw new IllegalArgumentException("Mismatched Parentheses");
                    }
                } else {
                    //handle operator order
                    if (!operators.isEmpty() && op.compareOrder(operators.peek()) <= 0) {
                        String e = operators.pop().toString();
                        output.push(e);
                    }
                    operators.push(op);
                }
            }
        }
        while (!operators.isEmpty())
            output.push("" + operators.pop());
    }

    private boolean isBrace(String toString) {
        return toString.trim().equals("(") || toString.trim().equals(")") || toString.trim().equals("[") || toString.trim().equals("]");
    }

    /**
     * Evaluates the expression in the output stack
     *
     * @return value of the expression in output
     */
    private double evaluateOutput() {
        //temporary stack for rpn calculation
        Stack<Double> values = new Stack<Double>();
        //reverse of output
        Stack<String> equation = new Stack<String>();
        //reverse output
        while (!output.isEmpty())
            equation.push("" + output.pop());
        //calculate rpn
        while (!equation.isEmpty()) {
            String str = equation.pop();
            //handle if character
            if (isNum(str))
                values.push(Double.parseDouble(str));
                //handle if operator
            else {
                operator op = operator.getOperator(str.charAt(0));
                double b = values.pop(), a = values.pop();
                values.push(op.operate(a, b));
            }
        }
        return values.pop();
    }

    /**
     * Determines whether the inputted string
     * is a number or an operator
     * Throws an IllegalArgumentException if
     * s is neither a number nor an operator
     *
     * @param s string to be checked
     * @return true if s is a number,
     * false if n is an operator,
     * else an IllegalArgumentException
     */
    private boolean isNum(String s) {
        try { //check if it's an integer
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            char c = s.charAt(0);
            if (operator.isOperator(c)) //if a valid token
                return false;
            else //invalid character
                throw new IllegalArgumentException("INVALID CHARACTER");
        }
    }

    /**
     * Enum of operators
     *
     * @author Eric bakan
     */
    private enum operator {

        /**
         * The operators:
         * Power
         * Multiply
         * Divide
         * Add
         * Subtract
         * Left Parentheses
         * Right Parentheses
         */
        pow(3, '^'), mult(2, 'x'), div(2, '/'), add(1, '+'), sub(1, '-'), lpar(0, '('), rpar(0, ')');

        private static final String operators = "^x/+-()";
        private final int order; //order of operations value
        private final char chr; //character representation

        /**
         * Constructor
         * initializes order and character values
         *
         * @param order order value
         * @param chr   character representation
         */
        operator(int order, char chr) {
            this.order = order;
            this.chr = chr;
        }

        /**
         * Determines of inputted character is an operator
         *
         * @param c character to check
         * @return whether c is a character
         */
        public static boolean isOperator(char c) {
            for (int i = 0; i < operators.length(); i++)
                if (c == operators.charAt(i))
                    return true;
            return false;
        }

        /**
         * Parses a character and returns its
         * operator equivalent
         *
         * @param c character to parse
         * @return operator equivalent
         */
        public static operator getOperator(char c) {
            switch (c) {
                case '^':
                    return operator.pow;
                case 'x':
                    return operator.mult;
                case '/':
                    return operator.div;
                case '+':
                    return operator.add;
                case '-':
                    return operator.sub;
                case '(':
                    return operator.lpar;
                case ')':
                    return operator.rpar;
                default:
                    throw new IllegalArgumentException("INVALID OPERATOR " + c);
            }
        }

        /**
         * Performs an operation with a left and righ value
         *
         * @param a left value
         * @param b right value
         * @return result of evaluated expression
         */
        public double operate(double a, double b) {
            switch (this) {
                case pow:
                    return Math.pow(a, b);
                case mult:
                    return a * b;
                case div:
                    return a / b;
                case add:
                    return a + b;
                case sub:
                    return a - b;
                default:
                    throw new ArithmeticException("INVALID OPERATOR");
            }
        }

        /**
         * Returns char representation of operator
         *
         * @return char representation of operator
         */
        public char getChar() {
            return this.chr;
        }

        /**
         * Overridden toString method
         *
         * @return chr
         */
        @Override
        public String toString() {
            return "" + this.chr;
        }

        /**
         * Returns result of compareTo() of orders
         * of two operators
         *
         * @param o operator to compare with
         * @return 1 if this operator's order is greater
         * than o's, -1 if this operator's order is less
         * than o's, 0 if this operator's order is equal
         * to o's
         */
        public int compareOrder(operator o) {
            return ((Integer) this.order).compareTo(o.getOrder());
        }

        /**
         * Returns this operator order
         *
         * @return
         */
        private int getOrder() {
            return this.order;
        }

    }
}

