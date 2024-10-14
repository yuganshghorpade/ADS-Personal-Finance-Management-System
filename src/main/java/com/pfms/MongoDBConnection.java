package com.pfms;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    
    private static MongoClient mongoClient;
    
    // Connect to the MongoDB server
    public static MongoDatabase connectToDatabase() {
        // You can replace the connection string with your MongoDB connection URI
        // String uri = "mongodb://localhost:27017";
        String uri = "mongodb+srv://yuganshghorpadeviit:WTsEmDUQI2MV8Mes@cluster0.5ogrr.mongodb.net";
        mongoClient = MongoClients.create(uri);
        System.out.println(mongoClient);
        return mongoClient.getDatabase("pfms");  // Replace with your database name
    }

    // public MongoDatabase getDatabase() {
    //     return database;
    // }
    
    // Close the connection
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
