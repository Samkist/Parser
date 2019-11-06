package me.Samkist.Parser;


import me.Samkist.Parser.CustomExceptions.*;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private Stack<Double> nums = new Stack<>();
    private Stack<String> ops = new Stack<>();
    private String[] stringTokens;
    private String rawString;
    private ParserGUI gui;

    public Parser(String rawString, ParserGUI gui) {
        this.rawString = rawString;
        this.gui = gui;
        stringTokens = rawString.split("");
        SamArray<String> stringList= new SamArray<>(stringTokens);
        for(int i = 0; i < stringList.size(); i++) {
            if(stringList.get(i).equals(" "))
                stringList.remove(stringList.get(i));
        }
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
                System.out.println("Pushing num: " + stringBuilder.toString());
                nums.push(Double.parseDouble(stringBuilder.toString()));
            } else if(stringTokens[i].equals("(")) {
                ops.push(stringTokens[i]);
            } else if(stringTokens[i].equals(")")) {
                while(!ops.peek().equals("(")) {
                    System.out.println("Top of operator stack is not (");
                    double secondNum = nums.pop();
                    double firstNum = nums.pop();
                    nums.push(applyOperator(firstNum, ops.pop(), secondNum));
                }
                ops.pop();
            } else if(stringTokens[i].matches("[-+*/^]")) {
                //Switched ops::peek and stringTokens for args
                while(!ops.empty() && hasPrecedence(stringTokens[i], ops.peek())) {
                    System.out.println("Found operator " + stringTokens[i] + " has precedence over " + ops.peek());
                    double secondNum = nums.pop();
                    double firstNum = nums.pop();
                    nums.push(applyOperator(firstNum, ops.pop(), secondNum));
                }
                ops.push(stringTokens[i]);
            }

            while(!ops.empty() && nums.size() >= 2) {
                double secondNum = nums.pop();
                double firstNum = nums.pop();
                System.out.println("Operating: " + firstNum + " " + ops.peek() + " " + secondNum);
                nums.push(applyOperator(firstNum, ops.pop(), secondNum));
            }
        }
        for(String st : s) {
            nums.push(Double.parseDouble(st));
        }
        System.out.println("Numbers: ");
        nums.forEach(System.out::println);
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
