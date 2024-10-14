package com.pfms;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class User {

    public static boolean loggedIn;
    public static String userId;
    private String username;
    private String password;  // In practice, this should be hashed.
    // private MongoDBConnection mongoDBConnection;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        // this.mongoDBConnection = new MongoDBConnection();
    }

    public boolean authenticate(String password) {
        if (this.password.equals(password)) {
            System.out.println("User authenticated.");
            return true;
        }
        System.out.println("Authentication failed.");
        return false;
    }

    public void registerUser(String name, String password) {
        MongoDatabase db = MongoDBConnection.connectToDatabase();
        BudgetManager budgetManager = new BudgetManager();
        MongoCollection<Document> collection = db.getCollection("users");

        // Create a new user document
        Document newUser = new Document("name", name)
                .append("password", password); // Make sure to hash the password before storing

        // Insert the new user into the collection
        // usersCollection.insertOne(newUser);
        collection.insertOne(newUser);

        budgetManager.addCategory("Rent", 50000.0);
        budgetManager.addCategory("Emi", 30000.0);
        budgetManager.addCategory("Loan", 10000.0);
        String userId = newUser.getObjectId("_id").toString();
        budgetManager.submitBudget(userId);

        Document user = collection.find(Filters.eq("_id", new ObjectId(userId))).first();
        if (user != null) {
        // Add a new field "budgetId" to the document
        collection.updateOne(Filters.eq("_id", new ObjectId(userId)), Updates.set("budgetId", new ObjectId(budgetManager.budgetId)));

        System.out.println("Budget ID added to user: " + user.getString("name"));
    } else {
        // User not found
        System.out.println("User not found with ID: " + userId);
    }
        System.out.println("New user registered: " + name);
    }

    public void loginUser(String name, String password) {
        MongoDatabase db = MongoDBConnection.connectToDatabase();
        MongoCollection<Document> collection = db.getCollection("users");

        Document user = collection.find(Filters.and(
                Filters.eq("name", name),
                Filters.eq("password", password) // Note: Storing plain text passwords is not secure
        )).first(); // Fetch the first matching document
        System.out.println(user);
        User.userId= user.getObjectId("_id").toString(); // Correct way to get the _id field
        BudgetManager.budgetId = user.getObjectId("budgetId").toString();
        // BudgetManager.budgetId = user.getObjectId("").toString();
        System.out.println("heyyy theree");
        if (user == null) {
            System.out.println("Please register before login or check credentials");
        }else{
        System.out.println("Successfully logged In");
        User.loggedIn = true;
        }

    }
}
