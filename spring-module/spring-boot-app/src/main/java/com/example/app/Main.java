package com.example.app;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Scanner;

@Service
public class Main {

    @PostConstruct
    public  void run() {
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