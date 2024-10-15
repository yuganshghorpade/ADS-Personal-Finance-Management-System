package com.pfms;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class BudgetManager {
    private HashMap<String, Double> budgetCategories;
    public static ObjectId budgetId;

    public BudgetManager() {
        budgetCategories = new HashMap<>();
    }

    public void addCategory(String category, double budgetAmount) {
        budgetCategories.put(category, budgetAmount);
    }

    public void submitBudget(ObjectId userId){
        MongoDatabase db = MongoDBConnection.connectToDatabase(); // Connect to the database
        MongoCollection<Document> collection = db.getCollection("budgets"); // Get the collection

        // for (Map.Entry<String, Double> entry : budgetCategories.entrySet()) {
        //     Document doc = new Document("category", entry.getKey())
        //                     .append("budget", entry.getValue());
        //     collection.insertOne(doc); // Insert the document into the collection
        // }

        Document userBudgetDocument = new Document("userId",userId)
                .append("categories", new Document());

        for (Map.Entry<String, Double> entry : budgetCategories.entrySet()) {
            // Add each category to the user's budget document
            userBudgetDocument.get("categories", Document.class)
                    .append(entry.getKey(), entry.getValue());
        }

        // Insert the user's budget document into the collection
        collection.insertOne(userBudgetDocument);
        Document budget = collection.find(Filters.eq("userId", userId)).first();
        budgetId = budget.getObjectId("_id");
        System.out.println("Budget categories saved to MongoDB successfully.");
    }

    //TODO:
    public void updateCategory(String category, double expenseAmount) {
        if (budgetCategories.containsKey(category)) {
            budgetCategories.put(category, budgetCategories.get(category) - expenseAmount);
        }
    }

    // public void getRemainingBudget(String category) {
    //     MongoDatabase db = MongoDBConnection.connectToDatabase(); // Connect to the database
    //     MongoCollection<Document> collection = db.getCollection("budgets"); // Get the collection
    //     Document budget = collection.find(Filters.and(
    //             Filters.eq("_id", budgetId) // Note: Storing plain text passwords is not secure
    //     )).first();
    // }

    public void fetchBudget(){
        MongoDatabase db = MongoDBConnection.connectToDatabase(); // Connect to the database
        MongoCollection<Document> collection = db.getCollection("budgets"); // Get the collection
        Document budget = collection.find(Filters.and(
                Filters.eq("_id", BudgetManager.budgetId) // Note: Storing plain text passwords is not secure
        )).first();
        System.out.println("budget"+budget);
    }
}
