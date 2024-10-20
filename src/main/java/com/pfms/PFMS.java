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

    }
}