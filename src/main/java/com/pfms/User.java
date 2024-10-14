package com.pfms;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class User {

    private String username;
    private String password;  // In practice, this should be hashed.
    // private MongoDBConnection mongoDBConnection;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
        MongoCollection<Document> collection = db.getCollection("users");

        // Create a new user document
        Document newUser = new Document("name", name)
                .append("password", password); // Make sure to hash the password before storing

        // Insert the new user into the collection
        // usersCollection.insertOne(newUser);
        collection.insertOne(newUser);
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
        if (user == null) {
            System.out.println("Please register before login or check credentials");
        }
        System.out.println("Successfully logged In");

    }
}
