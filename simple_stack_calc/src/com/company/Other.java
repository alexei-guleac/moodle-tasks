package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class Other {

    public static void mains(String[] args) {
        Scanner in;
        if (args.length > 0) {
            try {
                in = new Scanner(new File(args[0]));
            } catch (FileNotFoundException e) {
                System.err.println("Requested file not found. Exiting.");
                return;
            }
        } else {
            in = new Scanner(System.in);
        }

        Stack<Double> numbers = new Stack<>();

        Pattern newline = Pattern.compile("\n");
        String prompt = "?: ";

        boolean cont = true;
        boolean stackPrint = true;

        System.out.print(prompt);
        while (cont && in.hasNext()) { // Stop if quit command issued, or EOF
            String token = in.next();
            boolean isOperator = isOperator(token);

            if (isOperator && numbers.size() < 2) {
                System.err.println("Operation failed: not enough operands");
            } else if (isOperator) {
                double right = numbers.pop();
                double left = numbers.pop();
                switch (token) {
                    case "+":
                        numbers.push(left + right);
                        break;
                    case "-":
                        numbers.push(left - right);
                        break;
                    case "*":
                        numbers.push(left * right);
                        break;
                    default:
                        numbers.push(left / right);
                }
            } else if (token.equals("q") || token.equals("quit")) {
                cont = false;
            } else if (token.equals("p") || token.equals("pop")) {
                System.out.println(numbers.pop());
            } else if (token.equals("c") || token.equals("clear")) {
                numbers = new Stack<Double>();
            } else if (token.equals("l") || token.equals("list")) {
                System.out.println(numbers);
                stackPrint = false;
            } else {
                try {
                    numbers.push(Double.parseDouble(token));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid operator, number, or command");
                    stackPrint = false;
                }
            }

            if (in.findWithinHorizon(newline, 1) != null) {
                // Reached end of line
                if (!stackPrint) {
                    // Output was supressed
                    stackPrint = true;
                } else {
                    System.out.println(numbers);
                }

                System.out.print(prompt);
            }
        }
    }

    private static boolean isOperator(String str) {
        String[] opList = {"+", "-", "*", "/"};
        for (String op : opList) {
            if (str.equals(op)) return true;
        }
        return false;
    }

}