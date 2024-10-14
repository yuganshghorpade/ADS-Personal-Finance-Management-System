package com.pfms;

import java.util.Scanner;

public class PFMS {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        // Initialize components

        //registering user
        // System.out.println("Enter your details");
        // System.out.print("Enter your name:");
        // String name = sc.nextLine();
        // System.out.print("Enter your password:");
        // String password = sc.nextLine();
        // user.registerUser(name, password);
        
        
        //logging user in
        System.out.println("Enter your details");
        System.out.print("Enter your name:");
        String name = sc.nextLine();
        System.out.print("Enter your password:");
        String password = sc.nextLine();
        
        User user = new User(name,password);
        TransactionManager transactionManager = new TransactionManager();
        BudgetManager budgetManager = new BudgetManager();
        InvestmentManager investmentManager = new InvestmentManager();
        
        // Example usage
        user.authenticate("password123");  // Authenticate user

        // Add transactions
        transactionManager.insertTransaction("T001", 250.0);
        transactionManager.insertTransaction("T002", 150.0);

        // Set and update budgets
        budgetManager.addCategory("Food", 500.0);
        budgetManager.updateCategory("Food", 100.0);

        // Add investment options
        investmentManager.addInvestmentOption("Stock A", 10.5);
        investmentManager.addInvestmentOption("Stock B", 7.3);

        System.out.println("Best Investment: " + investmentManager.getBestInvestment().name);
    }
}
