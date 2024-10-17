// package com.pfms;

// import java.util.PriorityQueue;

// class InvestmentOption implements Comparable<InvestmentOption> {
//     String name;
//     double potentialReturn;

//     public InvestmentOption(String name, double potentialReturn) {
//         this.name = name;
//         this.potentialReturn = potentialReturn;
//     }

//     @Override
//     public int compareTo(InvestmentOption other) {
//         // Higher potential return gets higher priority
//         return Double.compare(other.potentialReturn, this.potentialReturn);  // Max heap
//     }
// }

// public class InvestmentManager {
//     private PriorityQueue<InvestmentOption> investmentHeap;

//     public InvestmentManager() {
//         investmentHeap = new PriorityQueue<>();
//     }

//     // Add a new investment option to the heap
//     public void addInvestmentOption(String name, double potentialReturn) {
//         InvestmentOption option = new InvestmentOption(name, potentialReturn);
//         investmentHeap.add(option);
//     }

//     // Get the best investment option (highest potential return)
//     public InvestmentOption getBestInvestment() {
//         return investmentHeap.peek();
//     }

//     // Remove the best investment option after it is chosen
//     public InvestmentOption removeBestInvestment() {
//         return investmentHeap.poll();
//     }
// }




// package com.pfms;

// import java.util.PriorityQueue;

// class InvestmentOption implements Comparable<InvestmentOption> {
//     String name;
//     double sipRate;  // Rate of return for SIP
//     double monthlyInvestment;  // Amount to invest monthly

//     public InvestmentOption(String name, double sipRate, double monthlyInvestment) {
//         this.name = name;
//         this.sipRate = sipRate;
//         this.monthlyInvestment = monthlyInvestment;
//     }

//     @Override
//     public int compareTo(InvestmentOption other) {
//         // Higher SIP rate gets higher priority
//         return Double.compare(other.sipRate, this.sipRate);  // Max heap
//     }

//     @Override
//     public String toString() {
//         return "SIP Name: " + name + ", Rate: " + sipRate + "%, Monthly Investment: ₹" + monthlyInvestment;}
// }
// public class InvestmentManager {
//     private PriorityQueue<InvestmentOption> investmentHeap;
//     private Double savings = User.user.getDouble("savings");  // Available savings

//     public InvestmentManager() {
//         investmentHeap = new PriorityQueue<>();
//     }

//     // Add a new SIP option to the heap
//     public void addInvestmentOption(String name, double sipRate, double monthlyInvestment) {
//         if (monthlyInvestment <= savings) {
//             InvestmentOption option = new InvestmentOption(name, sipRate, monthlyInvestment);
//             investmentHeap.add(option);
//         } else {
//             System.out.println("Not enough savings to invest in: " + name);
//         }
//     }

//     // Get the best SIP option (highest rate of return)
//     public InvestmentOption getBestInvestment() {
//         return investmentHeap.peek();
//     }

//     // Invest in the best SIP (deduct from savings and remove from heap)
//     public void investInBestOption() {
//         InvestmentOption bestOption = investmentHeap.poll();
//         if (bestOption != null) {
//             if (bestOption.monthlyInvestment <= savings) {
//                 savings -= bestOption.monthlyInvestment;
//                 System.out.println("Invested in " + bestOption.name + ".\nRemaining savings: ₹" + savings);
//             } else {
//                 System.out.println("Not enough savings to invest in " + bestOption.name);
//             }
//         } else {
//             System.out.println("No SIPs available to invest in.");
//         }
//     }

//     // View current savings
//     public double getSavings() {
//         return savings;
//     }}

package com.pfms;

import java.util.PriorityQueue;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

class InvestmentOption implements Comparable<InvestmentOption> {
    String name;
    double sipRate;  // Rate of return for SIP
    double monthlyInvestment;  // Amount to invest monthly

    public InvestmentOption(String name, double sipRate, double monthlyInvestment) {
        this.name = name;
        this.sipRate = sipRate;
        this.monthlyInvestment = monthlyInvestment;
    }

    @Override
    public int compareTo(InvestmentOption other) {
        // Higher SIP rate gets higher priority
        return Double.compare(other.sipRate, this.sipRate);  // Max heap
    }

    @Override
    public String toString() {
        return "SIP Name: " + name + ", Rate: " + sipRate + "%, Monthly Investment: ₹" + monthlyInvestment;
    }

    public Document toDocument() {
        // Convert to MongoDB Document
        return new Document("name", name)
                .append("sipRate", sipRate)
                .append("monthlyInvestment", monthlyInvestment);
    }

    public static InvestmentOption fromDocument(Document doc) {
        // Convert MongoDB Document back to InvestmentOption
        return new InvestmentOption(doc.getString("name"), doc.getDouble("sipRate"), doc.getDouble("monthlyInvestment"));
    }
}

public class InvestmentManager {
    private PriorityQueue<InvestmentOption> investmentHeap;
    private MongoCollection<Document> investmentCollection;  // MongoDB collection for investments
    private MongoCollection<Document> userCollection;  // MongoDB collection for users
    private String userId;
    private Double savings;

    MongoDatabase database = MongoDBConnection.connectToDatabase();

    public InvestmentManager() {
        investmentHeap = new PriorityQueue<>();
        investmentCollection = database.getCollection("investments");
        userCollection = database.getCollection("users");
        this.userId = User.user.getObjectId("_id").toString();
        
        // Load the user's savings from MongoDB
        // Document user = userCollection.find(Filters.eq("_id", userId)).first();
        if (User.user != null) {
            this.savings = User.user.getDouble("savings");
        } else {
            System.out.println(PFMS.GREEN + "User not found.");
        }
        
        // Load existing investments from MongoDB
        for (Document doc : investmentCollection.find(Filters.eq("userId", userId))) {
            investmentHeap.add(InvestmentOption.fromDocument(doc));
        }
    }

    // Add a new SIP option to the heap and MongoDB
    public void addInvestmentOption(String name, double sipRate, double monthlyInvestment) {
        if (monthlyInvestment <= savings) {
            InvestmentOption option = new InvestmentOption(name, sipRate, monthlyInvestment);
            investmentHeap.add(option);
            // Save to MongoDB
            Document investmentDoc = option.toDocument().append("userId", userId);
            investmentCollection.insertOne(investmentDoc);
        } else {
            System.out.println(PFMS.GREEN + "Not enough savings to invest in: " + name);
        }
    }

    // Get the best SIP option (highest rate of return)
    // Get the best SIP option (highest rate of return)
public InvestmentOption getBestInvestment() {
    return investmentHeap.peek();  // Should return the top element
}

// Invest in the best SIP (deduct from savings and remove from heap)
public void investInBestOption() {
    InvestmentOption bestOption = investmentHeap.poll();  // Remove the top element
    if (bestOption != null) {
        if (bestOption.monthlyInvestment <= savings) {
            savings -= bestOption.monthlyInvestment;
            System.out.println(PFMS.GREEN + "savings"+savings);
            userCollection.updateOne(Filters.eq("_id", new ObjectId(userId)), Updates.set("savings", savings));
            System.out.println(PFMS.GREEN + "Invested in " + bestOption.name + ".\nRemaining savings: ₹" + savings);
        } else {
            System.out.println(PFMS.GREEN + "Not enough savings to invest in " + bestOption.name);
        }
    } else {
        System.out.println(PFMS.GREEN + "No SIPs available to invest in.");
    }
}


    // View current savings
    public double getSavings() {
        return savings;
    }
}
