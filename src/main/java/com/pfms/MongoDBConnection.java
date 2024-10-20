package com.pfms;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    
    private static MongoClient mongoClient;
    
    // Connect to the MongoDB server
    public static MongoDatabase connectToDatabase() {
        // You can replace the connection string with your MongoDB connection URI
        String uri = "mongodb+srv://yuganshghorpadeviit:WTsEmDUQI2MV8Mes@cluster0.5ogrr.mongodb.net";
        mongoClient = MongoClients.create(uri);
        return mongoClient.getDatabase("pfms");  // Replace with your database name
    }

    
    // Close the connection
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
