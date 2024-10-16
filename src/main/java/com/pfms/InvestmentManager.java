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
package com.pfms;

import java.util.PriorityQueue;

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
        return "SIP Name: " + name + ", Rate: " + sipRate + "%, Monthly Investment: ₹" + monthlyInvestment;}
}
public class InvestmentManager {
    private PriorityQueue<InvestmentOption> investmentHeap;
    private Long savings = User.user.getLong("savings");  // Available savings

    public InvestmentManager() {
        investmentHeap = new PriorityQueue<>();
    }

    // Add a new SIP option to the heap
    public void addInvestmentOption(String name, double sipRate, double monthlyInvestment) {
        if (monthlyInvestment <= savings) {
            InvestmentOption option = new InvestmentOption(name, sipRate, monthlyInvestment);
            investmentHeap.add(option);
        } else {
            System.out.println("Not enough savings to invest in: " + name);
        }
    }

    // Get the best SIP option (highest rate of return)
    public InvestmentOption getBestInvestment() {
        return investmentHeap.peek();
    }

    // Invest in the best SIP (deduct from savings and remove from heap)
    public void investInBestOption() {
        InvestmentOption bestOption = investmentHeap.poll();
        if (bestOption != null) {
            if (bestOption.monthlyInvestment <= savings) {
                savings -= bestOption.monthlyInvestment;
                System.out.println("Invested in " + bestOption.name + ".\nRemaining savings: ₹" + savings);
            } else {
                System.out.println("Not enough savings to invest in " + bestOption.name);
            }
        } else {
            System.out.println("No SIPs available to invest in.");
        }
    }

    // View current savings
    public double getSavings() {
        return savings;
    }}