package com.pfms;

class TransactionNode {
    String transactionId;
    double amount;
    TransactionNode left, right;
    int height;

    public TransactionNode(String transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
        height = 1;
    }
}

public class TransactionManager {
    private TransactionNode root;

    public void insertTransaction(String transactionId, double amount) {
        root = insertRec(root, transactionId, amount);
    }

    private TransactionNode insertRec(TransactionNode node, String transactionId, double amount) {
        if (node == null) return new TransactionNode(transactionId, amount);

        if (amount < node.amount) node.left = insertRec(node.left, transactionId, amount);
        else if (amount > node.amount) node.right = insertRec(node.right, transactionId, amount);
        else return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        return balance(node);
    }

    private int height(TransactionNode node) {
        return (node == null) ? 0 : node.height;
    }

    private TransactionNode balance(TransactionNode node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1 && getBalance(node.left) >= 0) return rightRotate(node);
        if (balanceFactor < -1 && getBalance(node.right) <= 0) return leftRotate(node);

        if (balanceFactor > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balanceFactor < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private int getBalance(TransactionNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private TransactionNode rightRotate(TransactionNode y) {
        TransactionNode x = y.left;
        TransactionNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private TransactionNode leftRotate(TransactionNode x) {
        TransactionNode y = x.right;
        TransactionNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }
}
