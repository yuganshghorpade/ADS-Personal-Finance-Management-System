package com.pfms;

import java.util.Scanner;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class PFMS {
    public static final String GREEN = "\u001B[32m";
    public static boolean loggedInMenu = true;
    Scanner sc = new Scanner(System.in);
    User user = new User();
    BudgetManager budgetManager = new BudgetManager();

    public void registerMainFunction() {
        System.out.println(PFMS.GREEN + "Enter your username");
        String username = sc.next();
        System.out.println(PFMS.GREEN + "Enter your password");
        String password = sc.next();
        System.out.println(PFMS.GREEN + "Enter your salary (in Rupees)");
        Double salary = sc.nextDouble();
        System.out.println(PFMS.GREEN + "Enter your wallet (in Rupees)");
        Double wallet = sc.nextDouble();
        System.out.println(PFMS.GREEN + "Enter your savings (in Rupees)");
        Double savings = sc.nextDouble();
        user.registerUser(username, password, salary, wallet, savings, budgetManager);

    }

    public void loginMainFunction() {
        System.out.println(PFMS.GREEN + "Enter your credentials for login");
        System.out.print(PFMS.GREEN + "Enter your username:");
        String name = sc.next();
        System.out.print(PFMS.GREEN + "Enter your password:");
        String password = sc.next();
        user.loginUser(name, password);
    }

    public void fetchingBudgetMainFunction() {
        budgetManager.fetchBudget();
    }

    public void resettingBudgetMainFunction() {
        System.out.println(PFMS.GREEN + "Enter your fresh budget:");

        MongoDatabase db = MongoDBConnection.connectToDatabase(); // Connect to the database
        MongoCollection<Document> collection = db.getCollection("budgets"); // Get the collection
        boolean isEnteringBudget = true;
        while (isEnteringBudget) {
            System.out.println(PFMS.GREEN + "Enter 1 to enter the recurring expense , press 2 to exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(PFMS.GREEN + "Enter the name of the recurring transaction:");
                    String budgetName = sc.next();
                    System.out.println(PFMS.GREEN + "Enter the amount of the recurring transaction:");
                    Double amount = sc.nextDouble();
                    budgetManager.addCategory(budgetName, amount);
                    break;
                case 2:
                    isEnteringBudget = false;
                    break;
                default:
                    System.out.println(PFMS.GREEN + "Invalid choice");
                    break;
            }
        }
        ObjectId userId = User.user.getObjectId("_id");
        budgetManager.submitBudget(userId);
        collection.updateOne(Filters.eq("_id", userId), Updates.set("budgetId", BudgetManager.budgetId));
    }

    public void notingTransactionMainFunction(TransactionBTree transactionBTree) {
        System.out.println(PFMS.GREEN + "\nEnter the details of the Transaction you made");
        System.out.print("Enter the amount");
        int amount = sc.nextInt();
        transactionBTree.insert(amount);
        System.out.println(PFMS.GREEN + "Amount inserted" + amount);
    }

    public void displayingTransactionsMainFunction(TransactionBTree transactionBTree) {
        transactionBTree.displayTree(TransactionBTree.rootId, 0);
    }

    public void addInvestmentOptionMainFunction(InvestmentManager investmentManager) {
        System.out.println(PFMS.GREEN + "Enter your Investment name");
        String invName = sc.next();
        System.out.println(PFMS.GREEN + "Enter your SIP Rate");
        Double sipRate = sc.nextDouble();
        System.out.println(PFMS.GREEN + "Enter your monthly Investment (in Rupees)");
        Double monthlyInvestment = sc.nextDouble();
        investmentManager.addInvestmentOption(invName, sipRate, monthlyInvestment);
        System.out.println(PFMS.GREEN + "Option added");
    }

    public void getBestInvestmentMainFunction(InvestmentManager investmentManager) {
        System.out.println(PFMS.GREEN + investmentManager.getBestInvestment());
    }

    public void investInBestOptionMainFunction(InvestmentManager investmentManager) {
        investmentManager.investInBestOption();
    }

    public void fetchUserDetailsMainFunction() {
        System.out.println(PFMS.GREEN + "User" + User.user);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PFMS pfms = new PFMS();
        // Initialize components
        System.out.println(PFMS.GREEN + "Welcome to the Personal Finance Management....");

        boolean displayMenu = true;

        while (displayMenu == true && User.loggedIn==false) {
            System.out.println(PFMS.GREEN + "If you are a new user kindly press 1 for register or press 2 for login. Press 3 for exit.");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    pfms.registerMainFunction();
                    break;
                case 2:
                    pfms.loginMainFunction();
                    break;
                default:
                    displayMenu=false;
                    return;
            }
        }

        while (loggedInMenu == true && User.loggedIn==true) {
            TransactionBTree transactionBTree = new TransactionBTree();
            InvestmentManager investmentManager = new InvestmentManager();
            System.out.println(PFMS.GREEN + "\nWelcome user...");
            System.out.println(PFMS.GREEN + "Press 1 for fetching the budget that you set");
            System.out.println(PFMS.GREEN + "Press 2 for resetting the budget");
            System.out.println(PFMS.GREEN + "Press 3 for noting a transaction");
            System.out.println(PFMS.GREEN + "Press 4 for displaying all the transactions");
            System.out.println(PFMS.GREEN + "Press 5 for setting the data of some Investment Scheme");
            System.out.println(PFMS.GREEN + "Press 6 to get Best Investment");
            System.out.println(PFMS.GREEN + "Press 7 to Invest in Best Options");
            System.out.println(PFMS.GREEN + "Press 8 to Fetch User Details");
            System.out.println(PFMS.GREEN + "Press any other key to exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    pfms.fetchingBudgetMainFunction();
                    break;
                case 2:
                    pfms.resettingBudgetMainFunction();
                    break;
                case 3:
                    pfms.notingTransactionMainFunction(transactionBTree);
                    break;
                case 4:
                    pfms.displayingTransactionsMainFunction(transactionBTree);
                    break;
                case 5:
                    pfms.addInvestmentOptionMainFunction(investmentManager);
                    break;
                case 6:
                    pfms.getBestInvestmentMainFunction(investmentManager);
                    break;
                case 7:
                    pfms.investInBestOptionMainFunction(investmentManager);
                    break;
                case 8:
                    pfms.fetchUserDetailsMainFunction();
                    break;
                default:
                    loggedInMenu = false;
                    return;
            }
        }

        //registering user
        // System.out.println("Enter your details");
        // System.out.print("Enter your name:");
        // String name = sc.nextLine();
        // System.out.print("Enter your password:");
        // String password = sc.nextLine();
        // System.out.print("Enter your salary:");
        // Double salary = sc.nextDouble();
        // System.out.print("Enter your wallet:");
        // Double wallet = sc.nextDouble();
        // System.out.print("Enter your savings:");
        // Double savings = sc.nextDouble();
        // User user = new User(name, password);
        // user.registerUser(name, password, salary, wallet, savings);
        //logging user in
        // System.out.println("Enter your details");
        // System.out.print("Enter your name:");
        // String name = sc.nextLine();
        // System.out.print("Enter your password:");
        // String password = sc.nextLine();
        // BudgetManager bm = new BudgetManager();
        // user.loginUser(name, password);
        // bm.fetchBudget();
        // TransactionBTree transactionManager = new TransactionBTree();
        // InvestmentManager investmentManager = new InvestmentManager();
        // // Adding investments
        // investmentManager.addInvestmentOption("angelOne", 10.2, 100);
        // investmentManager.addInvestmentOption("angeltwo", 13, 1000);
        // investmentManager.addInvestmentOption("angelthree", 8.7, 400);
        // investmentManager.addInvestmentOption("angelfour", 9.1, 700);
        // investmentManager.addInvestmentOption("angelfive", 10, 200);
// Get best investment (should be angeltwo because of highest SIP rate)
        // System.out.println("Best investment before investing: " + investmentManager.getBestInvestment().name);
// Invest in the best option (should invest in angeltwo)
        // investmentManager.investInBestOption();
// Print the remaining savings
        // System.out.println("Remaining savings: " + investmentManager.getSavings());
        // investmentManager.addInvestmentOption("angelOne", 10.2, 100);
        // investmentManager.addInvestmentOption("angeltwo", 13, 1000);
        // investmentManager.addInvestmentOption("angelthree", 8.7, 400);
        // investmentManager.addInvestmentOption("angelfour", 9.1, 700);
        // investmentManager.addInvestmentOption("angelfive", 10, 200);
        // System.out.println(investmentManager.getBestInvestment());
        // investmentManager.investInBestOption();
        // System.out.println(investmentManager.getSavings());
        // Example usage
        // user.authenticate("password123");  // Authenticate user
        // Add transactions
        // transactionManager.insert(100);
        // transactionManager.insert(200);
        // transactionManager.insert(50);
        // transactionManager.insert(60);
        // transactionManager.insert(120);
        // transactionManager.insert(300);
        // transactionManager.insert(70);
        // transactionManager.insert(170);
        // transactionManager.displayTree(transactionManager.rootId, 0);
        // Display the tree
        // budgetManager.updateCategory("Food", 100.0);
        // Add investment options
        // System.out.println("Best Investment: " + investmentManager.getBestInvestment().name);
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
