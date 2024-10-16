package com.pfms;

import java.util.Scanner;

public class PFMS {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        // Initialize components
        
        //registering user
        System.out.println("Enter your details");
        System.out.print("Enter your name:");
        String name = sc.nextLine();
        System.out.print("Enter your password:");
        String password = sc.nextLine();
        System.out.print("Enter your salary:");
        Long salary = sc.nextLong();
        System.out.print("Enter your wallet:");
        Long wallet = sc.nextLong();
        System.out.print("Enter your savings:");
        Long savings = sc.nextLong();

        User user = new User(name,password);

        user.registerUser(name, password, salary, wallet, savings);
        
        
        //logging user in
        // System.out.println("Enter your details");
        // System.out.print("Enter your name:");
        // String name = sc.nextLine();
        // System.out.print("Enter your password:");
        // String password = sc.nextLine();
        
        BudgetManager bm = new BudgetManager();
        user.loginUser(name, password);
        bm.fetchBudget();
        TransactionBTree transactionManager = new TransactionBTree();
        
        InvestmentManager investmentManager = new InvestmentManager();
        
        // Example usage
        // user.authenticate("password123");  // Authenticate user

        // Add transactions

        transactionManager.insert(100);
        transactionManager.insert(200);
        transactionManager.insert(50);
        transactionManager.insert(60);
        transactionManager.insert(120);
        transactionManager.insert(300);
        transactionManager.insert(70);
        transactionManager.insert(170);

        transactionManager.displayTree(transactionManager.rootId, 0);

        // Display the tree


        // budgetManager.updateCategory("Food", 100.0);

        // Add investment options
        investmentManager.addInvestmentOption("Stock A", 10.5);
        investmentManager.addInvestmentOption("Stock B", 7.3);

        System.out.println("Best Investment: " + investmentManager.getBestInvestment().name);
    }
}
// package com.pfms;

// import java.util.Scanner;

// public class PFMS {
//     public static void main(String[] args) {

//         Scanner sc = new Scanner(System.in);
//         User user = null; // To be initialized after registration or login
//         boolean isRegistered = false;
//         boolean isLoggedIn = false;
//         boolean running = true;

//         // Initialize transactionManager, budgetManager, and investmentManager
//         TransactionManager transactionManager = new TransactionManager();
//         BudgetManager budgetManager = new BudgetManager();
//         InvestmentManager investmentManager = new InvestmentManager();

//         while (running) {
//             System.out.println("\nPersonal Finance Management System");
            
//             // If the user is not registered, show Register option
//             if (!isRegistered) {
//                 System.out.println("1. Register User");
//                 System.out.println("2. Exit");
//                 System.out.print("Choose an option: ");
                
//                 int choice = sc.nextInt();
//                 sc.nextLine(); // Consume newline

//                 switch (choice) {
//                     case 1: // Register user
//                         System.out.print("Enter your name: ");
//                         String newName = sc.nextLine();
//                         System.out.print("Enter your password: ");
//                         String newPassword = sc.nextLine();
//                         user = new User(newName, newPassword);

//                         // Register user in the database
//                         user.registerUser(newName, newPassword);
//                         isRegistered = true;
//                         System.out.println("User registered successfully!");
//                         break;

//                     case 2: // Exit
//                         running = false;
//                         System.out.println("Exiting the system. Goodbye!");
//                         break;

//                     default:
//                         System.out.println("Invalid choice. Please try again.");
//                         break;
//                 }
//             } else { // If registered, show the login and other options
//                 if (!isLoggedIn) {
//                     System.out.println("1. Log In");
//                     System.out.println("2. Exit");
//                     System.out.print("Choose an option: ");
//                     int choice = sc.nextInt();
//                     sc.nextLine(); // Consume newline

//                     switch (choice) {
//                         case 1: // Log in
//                             System.out.print("Enter your name: ");
//                             String name = sc.nextLine();
//                             System.out.print("Enter your password: ");
//                             String password = sc.nextLine();
                            
//                             // Authenticate the user
//                             if (user != null && user.authenticate(password)) {
//                                 isLoggedIn = true;
//                                 System.out.println("Login successful!");
//                             } else {
//                                 System.out.println("Invalid name or password. Try again.");
//                             }
//                             break;

//                         case 2: // Exit
//                             running = false;
//                             System.out.println("Exiting the system. Goodbye!");
//                             break;

//                         default:
//                             System.out.println("Invalid choice. Please try again.");
//                             break;
//                     }
//                 } else {
//                     // Display the main menu for logged-in users
//                     System.out.println("\nMain Menu");
//                     System.out.println("1. Add Transaction");
//                     System.out.println("2. Manage Budget");
//                     System.out.println("3. Manage Investments");
//                     System.out.println("4. Log Out");
//                     System.out.println("5. Exit");
//                     System.out.print("Choose an option: ");
                    
//                     int choice = sc.nextInt();
//                     sc.nextLine(); // Consume newline

//                     switch (choice) {
//                         case 1: // Add transaction
//                             System.out.print("Enter transaction ID: ");
//                             String transactionId = sc.nextLine();
//                             System.out.print("Enter transaction amount: ");
//                             double amount = sc.nextDouble();
//                             transactionManager.insertTransaction(transactionId, amount);
//                             System.out.println("Transaction added successfully!");
//                             break;

//                         case 2: // Manage budget
//                             System.out.println("1. Add Category");
//                             System.out.println("2. Update Category");
//                             System.out.print("Choose an option: ");
//                             int budgetChoice = sc.nextInt();
//                             sc.nextLine(); // Consume newline
                            
//                             if (budgetChoice == 1) {
//                                 System.out.print("Enter category name: ");
//                                 String category = sc.nextLine();
//                                 System.out.print("Enter budget limit: ");
//                                 double limit = sc.nextDouble();
//                                 budgetManager.addCategory(category, limit);
//                                 System.out.println("Category added successfully!");
//                             } else if (budgetChoice == 2) {
//                                 System.out.print("Enter category name: ");
//                                 String category = sc.nextLine();
//                                 System.out.print("Enter new amount: ");
//                                 double updatedAmount = sc.nextDouble();
//                                 budgetManager.updateCategory(category, updatedAmount);
//                                 System.out.println("Category updated successfully!");
//                             } else {
//                                 System.out.println("Invalid choice.");
//                             }
//                             break;

//                         case 3: // Manage investments
//                             System.out.println("1. Add Investment Option");
//                             System.out.println("2. View Best Investment");
//                             System.out.print("Choose an option: ");
//                             int investmentChoice = sc.nextInt();
//                             sc.nextLine(); // Consume newline
                            
//                             if (investmentChoice == 1) {
//                                 System.out.print("Enter investment name: ");
//                                 String investmentName = sc.nextLine();
//                                 System.out.print("Enter potential return: ");
//                                 double potentialReturn = sc.nextDouble();
//                                 investmentManager.addInvestmentOption(investmentName, potentialReturn);
//                                 System.out.println("Investment option added successfully!");
//                             } else if (investmentChoice == 2) {
//                                 InvestmentOption bestInvestment = investmentManager.getBestInvestment();
//                                 if (bestInvestment != null) {
//                                     System.out.println("Best Investment: " + bestInvestment.name + " with potential return " + bestInvestment.potentialReturn);
//                                 } else {
//                                     System.out.println("No investments available.");
//                                 }
//                             } else {
//                                 System.out.println("Invalid choice.");
//                             }
//                             break;

//                         case 4: // Log out
//                             isLoggedIn = false;
//                             System.out.println("Logged out successfully.");
//                             break;

//                         case 5: // Exit
//                             running = false;
//                             System.out.println("Exiting the system. Goodbye!");
//                             break;

//                         default:
//                             System.out.println("Invalid choice. Please try again.");
//                             break;
//                     }
//                 }
//             }
//         }
//         sc.close();}
// }