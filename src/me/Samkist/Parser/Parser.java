package me.Samkist.Parser;


import me.Samkist.Parser.CustomExceptions.IllegalCharacterException;
import me.Samkist.Parser.CustomExceptions.IllegalStartException;
import me.Samkist.Parser.CustomExceptions.IllegalTermAmountException;

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
        } catch (IllegalCharacterException | IllegalTermAmountException e) {
            gui.messageBox(e.getMessage());
            return;
        }
        operator = determineOperator();
        numbers = findNumbers();
        try {
            result = calculateExpression(numbers, operator);
        } catch(ArithmeticException e) {
            gui.messageBox(e.getMessage());
            return;
        }
        gui.getOutputField().setText("" + result);
    }

    private void errorCheck() throws IllegalStartException, IllegalCharacterException, IllegalTermAmountException {
        String[] splitCheckString = rawString.split("");
        String[] splitStringWNegatives = rawString.split("[+*/%=]");
        if(!splitCheckString[0].equals("=")) {
            throw new IllegalStartException("The first character of the string must be a \"=\"");
        }
        for(int i = 0; i < splitCheckString.length; i++) {
            if(!(Character.isDigit(rawString.charAt(i)) || splitCheckString[i].matches("[-+*/%=.]"))) {
                throw new IllegalCharacterException("Illegal character \'" + splitCheckString[i] + "\' at index: " + i);
            }
        }
        if(false) {
            throw new IllegalTermAmountException("Illegal amount of terms, please keep the amount of terms at 2");
        }
    }

    private double[] findNumbers() {
        double[] nums = new double[2];
        StringBuilder string = new StringBuilder((CharSequence) rawString);
        string.setCharAt(operatorIndex, ' ');
        string.deleteCharAt(0);
        String a = string.toString();
        String[] numbers = a.split("\\s+");
        for(int i = 0; i < nums.length; i++) {
            nums[i] = Double.parseDouble(numbers[i]);
        }
        return nums;
    }

    private double findNegative(int startIndex) {
        String negativeString = "-";
        String[] splitCheckString = rawString.split("");
        for(int i = startIndex+1; i < splitCheckString.length - startIndex + 1; i++) {
            if(Character.isDigit(rawString.charAt(i))) {
                negativeString += splitCheckString[i];
            } else {
                break;
            }
        }
        System.out.println(negativeString);
        return Double.parseDouble(negativeString);
    }

    private char determineOperator() {
        String[] splitCheckString = rawString.split("");
        for(int i = 0; i < splitCheckString.length; i++) {
            switch (splitCheckString[i]) {
                case "-":
                    if(Character.isDigit(rawString.charAt(i-1)) && Character.isDigit(rawString.charAt(i+1))) {
                        operatorIndex = i;
                        return '-';
                    } else {
                        continue;
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
