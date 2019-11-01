package me.Samkist.Parser;


import me.Samkist.Parser.CustomExceptions.IllegalCharacterException;
import me.Samkist.Parser.CustomExceptions.IllegalStartException;
import me.Samkist.Parser.CustomExceptions.IllegalTermAmountException;
import me.Samkist.Parser.CustomExceptions.MisplacedOperatorException;

import java.util.ArrayList;

public class Parser {
    private String rawString;
    private ParserGUI gui;
    private double[] numbers;
    private char operator;
    private double result;
    private int operatorIndex = 0;

    public Parser(String rawString, ParserGUI gui) {
        rawString = rawString.replaceAll("\\s+", "");
        this.rawString = rawString;
        this.gui = gui;
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
        operator = determineOperator();
        try {
            numbers = findNumbers();
        } catch (IllegalTermAmountException | MisplacedOperatorException e) {
            gui.messageBox(e.getMessage());
            return;
        }
        try {
            result = calculateExpression(numbers, operator);
        } catch(ArithmeticException e) {
            gui.messageBox(e.getMessage());
            return;
        }
        gui.getOutputField().setText("" + result);
    }

    private void errorCheck() throws IllegalStartException, IllegalCharacterException {
        String[] splitCheckString = rawString.split("");
        if(!splitCheckString[0].equals("=")) {
            throw new IllegalStartException("The first character of the string must be a \"=\"");
        }
        for(int i = 0; i < splitCheckString.length; i++) {
            if(!(Character.isDigit(rawString.charAt(i)) || splitCheckString[i].matches("[-+*/%=.]"))) {
                throw new IllegalCharacterException("Illegal character \'" + splitCheckString[i] + "\' at index: " + i);
            }
        }
    }

    private double[] findNumbers() throws IllegalTermAmountException, MisplacedOperatorException {
        double[] nums = new double[2];
        String[] splitCheckString = rawString.split("");
        StringBuilder string = new StringBuilder((CharSequence) rawString);
        string.setCharAt(operatorIndex, ' ');
        string.deleteCharAt(0);
        String end = splitCheckString[splitCheckString.length-1];
        char z = end.charAt(0);
        if(!Character.isDigit(z)) throw new MisplacedOperatorException("Misplaced operator, last character " + z + " must be an operand");
        for(int i = 0; i < splitCheckString.length; i++) {
            if(splitCheckString[i].equals("-")) {
                if(rawString.charAt(i+1) == '-') {
                    operator = '+';
                    string.replace(i-1, i+1, " ");
                    break;
                }
            }
        }
        String a = string.toString();
        String[] numbers = a.split("\\s+");
        for(int i = 0; i < nums.length; i++) {
            try {
                System.out.println("Trying: " + numbers[i]);
                nums[i] = Double.parseDouble(numbers[i]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e ) {
                throw new IllegalTermAmountException("Bad amount of terms or too many operators");
            }
        }
        return nums;
    }

    private char determineOperator() {
        String[] splitCheckString = rawString.split("");
        for(int i = 0; i < splitCheckString.length; i++) {
            switch (splitCheckString[i]) {
                case "-":
                    if(!(i+1 == rawString.length())) {
                        if (Character.isDigit(rawString.charAt(i - 1)) && Character.isDigit(rawString.charAt(i + 1))) {
                            operatorIndex = i;
                            return '-';
                        } else {
                            continue;
                        }
                    } else {
                        if (Character.isDigit(rawString.charAt(i - 1))) {
                            operatorIndex = i;
                            return '-';
                        } else {
                            continue;
                        }
                    }
                case "+":
                    operatorIndex = i;
                    return '+';
                case "*":
                    operatorIndex = i;
                    return '*';
                case "/":
                    operatorIndex = i;
                    return '/';
            }
        }
        return '+';
    }

    private double calculateExpression(double[] nums, char op) throws ArithmeticException {
        switch(op) {
            case '-':
                return nums[0] - nums[1];
            case '*':
                return nums[0] * nums[1];
            case '/':
                if(nums[1] == 0)
                    throw new ArithmeticException("Attempt to divide by zero, please remove 0 and please try again");
                return nums[0] / nums[1];
            default:
                return nums[0] + nums[1];
        }
    }
}
