package com.pfms;

import java.util.HashMap;

public class BudgetManager {
    private HashMap<String, Double> budgetCategories;

    public BudgetManager() {
        budgetCategories = new HashMap<>();
    }

    public void addCategory(String category, double budgetAmount) {
        budgetCategories.put(category, budgetAmount);
    }

    public void updateCategory(String category, double expenseAmount) {
        if (budgetCategories.containsKey(category)) {
            budgetCategories.put(category, budgetCategories.get(category) - expenseAmount);
        }
    }

    public double getRemainingBudget(String category) {
        return budgetCategories.getOrDefault(category, 0.0);
    }
}
