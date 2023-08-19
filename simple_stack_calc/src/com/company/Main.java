package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter an expression:");
            String expr = sc.nextLine();
            double val = calculator.calculate(expr);
            System.out.printf("The value of %s is %.2f\n", expr, val);
        }
    }


}