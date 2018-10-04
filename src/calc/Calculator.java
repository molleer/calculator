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
    public double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        double result = evalPostfix(postfix);
        return result; // result;
    }

    // ------  Evaluate RPN expression -------------------

    // TODO Eval methods

    static double applyOperator(String op, double d1, double d2) {
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
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    postfix.add(operatorStack.pop());
                }

                if (operatorStack.isEmpty()) {
                    throw new IllegalArgumentException(MISSING_OPERATOR);
                }

                //Removes the "(" from infix
                operatorStack.pop();
            } else { // if i is an operator

                //remove all more valued operators from the stack

                if (!i.equals("(") && !operatorStack.isEmpty() && !operatorStack.peek().equals("("))
                    popUnitlValue(getPrecedence(i), operatorStack, postfix, getAssociativity(operatorStack.peek()));

                operatorStack.push(i);
            }
        }

        //Adds all remaining operators to postfix
        for (String i : operatorStack)
            postfix.add(i);


        return postfix;
    }

    private void popUnitlValue(int precedence1, ArrayDeque<String> stack, List<String> postfix, Assoc assoc) {
        while (!stack.isEmpty() && precedence1 + (assoc.equals(Assoc.RIGHT) ? 1 : 0) <= getPrecedence(stack.peek())) {
            postfix.add(stack.pop());
        }
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

    public static List<String> tokenize(String str) {
        List<String> tokens = new ArrayList<>();
        operatorCheck(str);
        str = str.replace(" ", "");

        for (char i : str.toCharArray()) {
            if (tokens.isEmpty() || "+-*^/()".contains(tokens.get(tokens.size() - 1)) || "+-*^/()".contains("" + i))
                tokens.add("");

            tokens.set(tokens.size() - 1, tokens.get(tokens.size() - 1) + i);
        }

        return tokens;
    }

    private static void operatorCheck(String str) {

        //Checks if ex:"4 34" exist, no operator between numbers
        String[] splitStr = str.split(" ");

        if (splitStr.length >= 2)
            for (int i = 0; i < (splitStr.length - 1); i++) {
                if (!splitStr[i].equals("") && !splitStr[i + 1].equals(""))
                    if (Character.isDigit(splitStr[i].charAt(splitStr[i].length() - 1)) && Character.isDigit(splitStr[i + 1].charAt(0)))
                        throw new IllegalArgumentException(MISSING_OPERATOR);
            }

        //Checks parenthesis
        int nPar = 0;
        for (char i : str.toCharArray())
            if (i == '(') nPar++;
            else if (i == ')') {
                nPar--;
                if (nPar < 0) break;
            }
        if (nPar != 0)
            throw new IllegalArgumentException(MISSING_OPERATOR);

    }

    public static double evalPostfix(List<String> tokens) {

        ArrayDeque<Double> tempValues = new ArrayDeque<>();
        double tmp;

        for (String token : tokens) {
            if (!"*+-/^".contains(token)) { // It is a number
                tempValues.push(Double.valueOf(token));
            } else { // Operator
                if (tempValues.size() < 2) { // Don´t crash
                    throw new IllegalArgumentException(MISSING_OPERAND);
                }

                tmp = tempValues.pop();
                tempValues.push(applyOperator(token, tmp, tempValues.pop()));

            }
        }


        return tempValues.pop();
    }
}
