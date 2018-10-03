package calc;

import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;


/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   This is not the program, it's a class declaration (with methods) in it's
 *   own file (which must be named Calculator.java)
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        // TODO List<String> tokens = tokenize(expr);
        // TODO List<String> postfix = infix2Postfix(tokens);
        // TODO double result = evalPostfix(postfix);
        return 0; // result;
    }

    // ------  Evaluate RPN expression -------------------

    // TODO Eval methods

    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    // TODO Methods



    public List<String> infix2Postfix(List<String> infix) {
        ArrayDeque<String> operatorStack = new ArrayDeque<>();
        List<String> postfix = new ArrayList<>();

        for (String i : infix) {
            if (isNumber(i)) {
                postfix.add(i);
            } else if (i.equals(")")) {
                //Adds all operators to postfix until it reaches "("
                while (!operatorStack.peek().equals("("))
                    postfix.add(operatorStack.pop());

                //Removes the "(" from infix
                operatorStack.pop();
            } else { // if i is an operator

                //remove all more valued operators from the stack
                while (!i.equals("(") && !operatorStack.isEmpty() && getPrecedence(i) < getPrecedence(operatorStack.peek())) {
                    postfix.add(operatorStack.pop());
                }
                operatorStack.push(i);
            }
        }

        //Adds all remaining operators to postfix
        for (String i : operatorStack)
            postfix.add(i);


        return postfix;
    }

    private boolean isNumber(String str) {
        for (char i : str.toCharArray()) {
            if (!Character.isDigit(i))
                return false;
        }
        return true;
    }

    int getPrecedence(String op) {
        if ("(".contains(op)) {
            return 1;
        } else if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    enum Assoc {
        LEFT,
        RIGHT
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }


    // ---------- Tokenize -----------------------

    // TODO Methods to tokenize

}
