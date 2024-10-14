package com.pfms;

import java.util.PriorityQueue;

class InvestmentOption implements Comparable<InvestmentOption> {
    String name;
    double potentialReturn;

    public InvestmentOption(String name, double potentialReturn) {
        this.name = name;
        this.potentialReturn = potentialReturn;
    }

    @Override
    public int compareTo(InvestmentOption other) {
        // Higher potential return gets higher priority
        return Double.compare(other.potentialReturn, this.potentialReturn);  // Max heap
    }
}

public class InvestmentManager {
    private PriorityQueue<InvestmentOption> investmentHeap;

    public InvestmentManager() {
        investmentHeap = new PriorityQueue<>();
    }

    // Add a new investment option to the heap
    public void addInvestmentOption(String name, double potentialReturn) {
        InvestmentOption option = new InvestmentOption(name, potentialReturn);
        investmentHeap.add(option);
    }

    // Get the best investment option (highest potential return)
    public InvestmentOption getBestInvestment() {
        return investmentHeap.peek();
    }

    // Remove the best investment option after it is chosen
    public InvestmentOption removeBestInvestment() {
        return investmentHeap.poll();
    }
}
