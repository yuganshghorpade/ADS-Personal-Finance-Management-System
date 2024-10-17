package com.pfms;

import java.util.Scanner;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class User {

    public static Document user;
    public static boolean loggedIn;
    public static ObjectId userId;

    public User() {
        this.loggedIn = false;
    }

    public void registerUser(String name, String password, Double salary, Double wallet, Double savings, BudgetManager budgetManager) {
        MongoDatabase db = MongoDBConnection.connectToDatabase();
        // BudgetManager budgetManager = new BudgetManager();
        Scanner sc = new Scanner(System.in);
        // TransactionManager transactionManager = new TransactionManager(5, loggedIn);
        MongoCollection<Document> collection = db.getCollection("users");

        // Create a new user document
        Document newUser = new Document("name", name)
                .append("password", password)
                .append("transactions", null)
                .append("salary", salary)
                .append("wallet", wallet)
                .append("savings", savings); // Make sure to hash the password before storing

        // Insert the new user into the collection
        // usersCollection.insertOne(newUser);
        collection.insertOne(newUser);

        System.out.println(PFMS.GREEN + "Let's set your budget so that you wont have to add the recurring expenses again and again.");
        boolean isEnteringBudget = true;
        while (isEnteringBudget) {
            System.out.println(PFMS.GREEN + "Enter 1 to enter the recurring expense that you have every month, press 2 to exit");
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
        ObjectId userId = newUser.getObjectId("_id");
        budgetManager.submitBudget(userId);

        // budgetManager.addCategory("Rent", 5.0);
        // budgetManager.addCategory("Emi", 3.0);
        // budgetManager.addCategory("Loan", 1.0);
        Document user = collection.find(Filters.eq("_id", userId)).first();
        System.out.println(PFMS.GREEN + "user" + user);
        System.out.println(PFMS.GREEN + budgetManager.budgetId);
        if (user != null) {
            // Add a new field "budgetId" to the document
            collection.updateOne(Filters.eq("_id", userId), Updates.set("budgetId", budgetManager.budgetId));

            System.out.println(PFMS.GREEN + "Budget ID added to user: " + user.getString("name"));
        } else {
            // User not found
            System.out.println(PFMS.GREEN + "User not found with ID: " + userId);
        }
        System.out.println(PFMS.GREEN + "New user registered: " + name);
    }

    public void loginUser(String name, String password) {
        MongoDatabase db = MongoDBConnection.connectToDatabase();
        MongoCollection<Document> collection = db.getCollection("users");

        User.user = collection.find(Filters.and(
                Filters.eq("name", name),
                Filters.eq("password", password) // Note: Storing plain text passwords is not secure
        )).first(); // Fetch the first matching document
        // System.out.println("this is found user"+user);
        User.userId = User.user.getObjectId("_id"); // Correct way to get the _id field
        BudgetManager.budgetId = user.getObjectId("budgetId");
        // BudgetManager.budgetId = user.getObjectId("").toString();
        // System.out.println("heyyy theree");
        if (user == null) {
            System.out.println(PFMS.GREEN + "Please register before login or check credentials");
        } else {
            System.out.println(PFMS.GREEN + "Successfully logged In");
            User.loggedIn = true;
        }

    }
}
