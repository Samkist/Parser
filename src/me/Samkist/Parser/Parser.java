package me.Samkist.Parser;


import me.Samkist.Parser.CustomExceptions.*;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    Stack<Double> nums = new Stack<>();
    Stack<String> ops = new Stack<>();
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
        findNumbers();
        findOperators();
    }

    private double evaluateExpression() {
        
        return 0.0;
    }

    private void findNumbers() {
        SamArray<String> s = new SamArray<>();
        for(int i = 1; i < stringTokens.length; i++) {
            if(Character.isDigit(stringTokens[i].charAt(0))) {
                StringBuilder stringBuilder;
                if(stringTokens[i-1].equals("-")) {
                    stringBuilder = new StringBuilder("-");
                } else {
                    stringBuilder = new StringBuilder();
                }
                while(i < stringTokens.length && (Character.isDigit(stringTokens[i].charAt(0)) || stringTokens[i].equals("."))) {
                    stringBuilder.append(stringTokens[i]);
                    i++;
                }
                s.add(stringBuilder.toString());
            }
        }
        for(String st : s) {
            nums.push(Double.parseDouble(st));
        }
        System.out.println("Numbers: ");
        nums.forEach(System.out::println);
    }

    private void findOperators() {
        for(int i = 0; i < stringTokens.length; i++) {
            if(stringTokens[i].matches("[+*/()^]")) {
                ops.push(stringTokens[i]);
            } else if(stringTokens[i].equals("-")) {
                if(!(Character.isDigit(stringTokens[i + 1].charAt(0))))
                    ops.push(stringTokens[i]);
            }
        }
        System.out.println("Operators: ");
        ops.forEach(System.out::println);
    }

    private void errorCheck() throws IllegalStartException, IllegalCharacterException {
        String[] splitCheckString = rawString.split("");
        if(!splitCheckString[0].equals("=")) {
            throw new IllegalStartException("The first character of the string must be a \"=\"");
        }
        for(int i = 0; i < splitCheckString.length; i++) {
            if(!(Character.isDigit(rawString.charAt(i)) || splitCheckString[i].matches("[-+*/=()^.]") ||
                    splitCheckString[i].equals(" "))) {
                throw new IllegalCharacterException("Illegal character \'" + splitCheckString[i] + "\' at index: " + i);
            }
        }
    }

    private boolean hasPrecedence(String op1, String op2) {
        if(op2.matches("[()]"))
            return false;
        else if(op1.equals("^") && !(op2.matches("[()]")))
            return true;
        return !op2.matches("[*/]") || !op1.matches("[-+]");
    }

    private double applyOperator(double a, String op, double b) throws ArithmeticException {
        switch(op) {
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if(b == 0)
                    throw new ArithmeticException("Attempt to divide by zero, please remove 0 and please try again");
                return a / b;
            case "^":
                return Math.pow(a, b);
            default:
                return a + b;
        }
    }
}
