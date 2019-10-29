package me.Samkist.Parser;

import BreezySwing.GBFrame;

import javax.swing.*;

public class ParserGUI extends GBFrame {
    private static JFrame frame = new ParserGUI();
    private JTextField inputField = addTextField("", 1,1 ,2 ,1);
    private JButton inputButton = addButton("Input", 2, 1, 1, 1);
    private JButton resetButton = addButton("Reset", 2, 2, 1,1);
    private JButton exitButton = addButton("Exit", 3, 1, 2, 1);
    private JTextField outputField = addTextField("", 6,1,2,3);

    public static void main(String[] args) {
        frame.setSize(400, 400);
        frame.setTitle("Parser Program");
        frame.setVisible(true);
    }

    public JTextField getOutputField() {
        return outputField;
    }

    public void buttonClicked(JButton jButton) {
        if(jButton.equals(inputButton)) {
            new Parser(inputField.getText(), this);
            inputField.grabFocus();
        }
        if(jButton.equals(resetButton)) {
            inputField.setText("");
            inputField.grabFocus();
        }
        if(jButton.equals(exitButton)) {
            System.exit(0);
        }
    }
}
