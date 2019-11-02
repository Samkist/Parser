package me.Samkist.Parser;


import me.Samkist.Parser.CustomExceptions.*;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    Stack<Double> operands = new Stack<>();
    Stack<Character> operators = new Stack<>();
    String[] stringTokens;
    String rawString;
    ParserGUI gui;

    public Parser(String rawString, ParserGUI gui) {
        this.rawString = rawString;
        this.gui = gui;
        stringTokens = rawString.split("");
        try {
            errorCheck();
        } catch (IllegalStartException e) {
          gui.getOutputField().setText(rawString);
          gui.messageBox(e.getMessage());
          return;
        } catch (IllegalCharacterException e) {
            gui.messageBox(e.getMessage());
            return;
        }
    }

    private double evaluateExpression() throws IntegerParseException {
        for(int i = 0; i < stringTokens.length; i++) {
            if (stringTokens[i].equals(" "))
                continue;
            if (Character.isDigit(stringTokens[i].charAt(0))) {
                StringBuilder stringBuilder;
                if (stringTokens[i - 1].equals("-")) {
                    stringBuilder = new StringBuilder(stringTokens[i - 1]);
                } else {
                    stringBuilder = new StringBuilder(stringTokens[i]);
                }
                while (i < stringTokens.length && Character.isDigit(stringTokens[i].charAt(0)))
                    stringBuilder.append(stringTokens[i++]);
                try {
                    operands.push(Double.parseDouble(stringBuilder.toString()));
                } catch (Exception e) {
                    throw new IntegerParseException("Failed to parse " + stringBuilder.toString() + " to an expression");
                }
            }
            else if (stringTokens[i].equals("("))
                operators.push(stringTokens[i].charAt(0));
            else if (stringTokens[i].equals(")")) {
                while(operators.peek() != '(') {
                    operands.push(applyOperator(operands.pop(), operators.pop(), operands.pop()));
                }
                operators.pop();
            }
            else if(stringTokens.equals("+") ||
                    stringTokens.equals("/") ||
                    stringTokens.equals("*")) {
                while(!operators.isEmpty() && hasPrecedence(stringTokens[i].charAt(0), operators.peek()))
                    operands.push(applyOperator(operands.pop(), operators.pop(), operands.pop()));
                operators.push(stringTokens[i].charAt(0));
            }
            else if(stringTokens.equals("-")) {
            }

        }
        return 0.0;
    }

    private boolean hasPrecedence(char op1, char op2) {
        if(op2 == ')' || op2 == '(')
            return false;
        else if((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    private void errorCheck() throws IllegalStartException, IllegalCharacterException {
        String[] splitCheckString = rawString.split("");
        if(!splitCheckString[0].equals("=")) {
            throw new IllegalStartException("The first character of the string must be a \"=\"");
        }
        for(int i = 0; i < splitCheckString.length; i++) {
            if(!(Character.isDigit(rawString.charAt(i)) || splitCheckString[i].matches("[-+*/=]"))) {
                throw new IllegalCharacterException("Illegal character \'" + splitCheckString[i] + "\' at index: " + i);
            }
        }
    }

    private double applyOperator(double a, char op, double b) throws ArithmeticException {
        switch(op) {
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if(b == 0)
                    throw new ArithmeticException("Attempt to divide by zero, please remove 0 and please try again");
                return a / b;
            default:
                return a + b;
        }
    }
}
