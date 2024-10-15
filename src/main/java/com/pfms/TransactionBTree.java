// // package com.pfms;

// // class TransactionNode {
// //     String transactionId;
// //     double amount;
// //     TransactionNode left, right;
// //     int height;

// //     public TransactionNode(String transactionId, double amount) {
// //         this.transactionId = transactionId;
// //         this.amount = amount;
// //         height = 1;
// //     }
// // }

// // public class TransactionManager {
// //     private TransactionNode root;

// //     public void insertTransaction(String transactionId, double amount) {
// //         root = insertRec(root, transactionId, amount);
// //     }

// //     private TransactionNode insertRec(TransactionNode node, String transactionId, double amount) {
// //         if (node == null) return new TransactionNode(transactionId, amount);

// //         if (amount < node.amount) node.left = insertRec(node.left, transactionId, amount);
// //         else if (amount > node.amount) node.right = insertRec(node.right, transactionId, amount);
// //         else return node;

// //         node.height = 1 + Math.max(height(node.left), height(node.right));

// //         return balance(node);
// //     }

// //     private int height(TransactionNode node) {
// //         return (node == null) ? 0 : node.height;
// //     }

// //     private TransactionNode balance(TransactionNode node) {
// //         int balanceFactor = getBalance(node);

// //         if (balanceFactor > 1 && getBalance(node.left) >= 0) return rightRotate(node);
// //         if (balanceFactor < -1 && getBalance(node.right) <= 0) return leftRotate(node);

// //         if (balanceFactor > 1 && getBalance(node.left) < 0) {
// //             node.left = leftRotate(node.left);
// //             return rightRotate(node);
// //         }

// //         if (balanceFactor < -1 && getBalance(node.right) > 0) {
// //             node.right = rightRotate(node.right);
// //             return leftRotate(node);
// //         }

// //         return node;
// //     }

// //     private int getBalance(TransactionNode node) {
// //         return (node == null) ? 0 : height(node.left) - height(node.right);
// //     }

// //     private TransactionNode rightRotate(TransactionNode y) {
// //         TransactionNode x = y.left;
// //         TransactionNode T2 = x.right;

// //         x.right = y;
// //         y.left = T2;

// //         y.height = Math.max(height(y.left), height(y.right)) + 1;
// //         x.height = Math.max(height(x.left), height(x.right)) + 1;

// //         return x;
// //     }

// //     private TransactionNode leftRotate(TransactionNode x) {
// //         TransactionNode y = x.right;
// //         TransactionNode T2 = y.left;

// //         y.left = x;
// //         x.right = T2;

// //         x.height = Math.max(height(x.left), height(x.right)) + 1;
// //         y.height = Math.max(height(y.left), height(y.right)) + 1;

// //         return y;
// //     }
// // }
// package com.pfms;

// class Transaction {
//     String transactionId;
//     double amount;

//     public Transaction(String transactionId, double amount) {
//         this.transactionId = transactionId;
//         this.amount = amount;
//     }
// }

// class TransactionManager {
//     int t;  // Minimum degree (defines the range for the number of keys)
//     int n;  // Current number of keys
//     Transaction[] transactions;  // Array of transactions (keys)
//     TransactionManager[] children;  // Array of child pointers
//     boolean isLeaf;  // True if node is leaf

//     public TransactionManager(int t, boolean isLeaf) {
//         this.t = t;
//         this.isLeaf = isLeaf;
//         this.transactions = new Transaction[2 * t - 1];  // Max number of keys
//         this.children = new TransactionManager[2 * t];  // Max number of children
//         this.n = 0;  // Initially number of keys is 0
//     }
// }

// public class TransactionBTree {
//     private TransactionManager root;
//     private int t;  // Minimum degree

//     public TransactionBTree(int t) {
//         this.root = null;
//         this.t = t;
//     }

//     // Search for a transaction by transactionId
//     public Transaction search(String transactionId) {
//         return (root == null) ? null : search(root, transactionId);
//     }

//     private Transaction search(TransactionManager node, String transactionId) {
//         int i = 0;
//         while (i < node.n && transactionId.compareTo(node.transactions[i].transactionId) > 0) {
//             i++;
//         }

//         // If the transactionId is found in this node
//         if (i < node.n && node.transactions[i].transactionId.equals(transactionId)) {
//             return node.transactions[i];
//         }

//         // If this is a leaf node, the transaction is not present
//         if (node.isLeaf) {
//             return null;
//         }

//         // Recur down the correct child
//         return search(node.children[i], transactionId);
//     }

//     // Insert a new transaction
//     public void insert(String transactionId, double amount) {
//         Transaction newTransaction = new Transaction(transactionId, amount);

//         if (root == null) {
//             root = new TransactionManager(t, true);
//             root.transactions[0] = newTransaction;
//             root.n = 1;
//         } else {
//             if (root.n == 2 * t - 1) {
//                 TransactionManager newRoot = new TransactionManager(t, false);
//                 newRoot.children[0] = root;

//                 // Split the old root and move the middle key up
//                 splitChild(newRoot, 0, root);

//                 // Insert the new transaction
//                 insertNonFull(newRoot, newTransaction);

//                 // Change root
//                 root = newRoot;
//             } else {
//                 insertNonFull(root, newTransaction);
//             }
//         }
//     }

//     // Helper function to insert when the node is not full
//     private void insertNonFull(TransactionManager node, Transaction transaction) {
//         int i = node.n - 1;

//         if (node.isLeaf) {
//             // Find the location to insert the new transaction
//             while (i >= 0 && transaction.transactionId.compareTo(node.transactions[i].transactionId) < 0) {
//                 node.transactions[i + 1] = node.transactions[i];
//                 i--;
//             }
//             node.transactions[i + 1] = transaction;
//             node.n = node.n + 1;
//         } else {
//             // Find the child to recurse into
//             while (i >= 0 && transaction.transactionId.compareTo(node.transactions[i].transactionId) < 0) {
//                 i--;
//             }
//             i++;

//             // Check if the found child is full
//             if (node.children[i].n == 2 * t - 1) {
//                 splitChild(node, i, node.children[i]);

//                 // After the split, the middle key is moved up, and node.children[i] is split
//                 if (transaction.transactionId.compareTo(node.transactions[i].transactionId) > 0) {
//                     i++;
//                 }
//             }
//             insertNonFull(node.children[i], transaction);
//         }
//     }

//     // Split the child of a node
//     private void splitChild(TransactionManager parent, int i, TransactionManager child) {
//         TransactionManager newNode = new TransactionManager(t, child.isLeaf);
//         newNode.n = t - 1;

//         // Move the last t-1 keys to newNode
//         for (int j = 0; j < t - 1; j++) {
//             newNode.transactions[j] = child.transactions[j + t];
//         }

//         // Move the last t children to newNode if it's not a leaf
//         if (!child.isLeaf) {
//             for (int j = 0; j < t; j++) {
//                 newNode.children[j] = child.children[j + t];
//             }
//         }

//         child.n = t - 1;

//         // Move parent's children to accommodate newNode
//         for (int j = parent.n; j >= i + 1; j--) {
//             parent.children[j + 1] = parent.children[j];
//         }

//         parent.children[i + 1] = newNode;

//         // Move parent's keys to accommodate the key moved up from child
//         for (int j = parent.n - 1; j >= i; j--) {
//             parent.transactions[j + 1] = parent.transactions[j];
//         }

//         parent.transactions[i] = child.transactions[t - 1];
//         parent.n = parent.n + 1;
//     }

//     // Update a transaction's amount based on transactionId
//     public void update(String transactionId, double newAmount) {
//         Transaction transaction = search(transactionId);
//         if (transaction != null) {
//             transaction.amount = newAmount;
//         } else {
//             System.out.println("Transaction not found!");
//         }
//     }

//     // Delete a transaction from the tree
//     public void delete(String transactionId) {
//         if (root == null) {
//             System.out.println("The tree is empty");
//             return;
//         }

//         delete(root, transactionId);

//         // If the root node has 0 keys, make the first child the new root
//         if (root.n == 0) {
//             if (root.isLeaf) {
//                 root = null;
//             } else {
//                 root = root.children[0];
//             }
//         }
//     }

//     private void delete(TransactionManager node, String transactionId) {
//         int idx = findKey(node, transactionId);

//         // If the key is in this node
//         if (idx < node.n && node.transactions[idx].transactionId.equals(transactionId)) {
//             if (node.isLeaf) {
//                 removeFromLeaf(node, idx);
//             } else {
//                 removeFromNonLeaf(node, idx);
//             }
//         } else {
//             if (node.isLeaf) {
//                 System.out.println("The transaction does not exist in the tree");
//                 return;
//             }

//             boolean flag = (idx == node.n);

//             if (node.children[idx].n < t) {
//                 fill(node, idx);
//             }

//             if (flag && idx > node.n) {
//                 delete(node.children[idx - 1], transactionId);
//             } else {
//                 delete(node.children[idx], transactionId);
//             }
//         }
//     }

//     private int findKey(TransactionManager node, String transactionId) {
//         int idx = 0;
//         while (idx < node.n && node.transactions[idx].transactionId.compareTo(transactionId) < 0) {
//             idx++;
//         }
//         return idx;
//     }

//     private void removeFromLeaf(TransactionManager node, int idx) {
//         for (int i = idx + 1; i < node.n; i++) {
//             node.transactions[i - 1] = node.transactions[i];
//         }
//         node.n--;
//     }

//     private void removeFromNonLeaf(TransactionManager node, int idx) {
//         Transaction transaction = node.transactions[idx];

//         if (node.children[idx].n >= t) {
//             Transaction pred = getPredecessor(node, idx);
//             node.transactions[idx] = pred;
//             delete(node.children[idx], pred.transactionId);
//         } else if (node.children[idx + 1].n >= t) {
//             Transaction succ = getSuccessor(node, idx);
//             node.transactions[idx] = succ;
//             delete(node.children[idx + 1], succ.transactionId);
//         } else {
//             merge(node, idx);
//             delete(node.children[idx], transaction.transactionId);
//         }
//     }

//     private Transaction getPredecessor(TransactionManager node, int idx) {
//         TransactionManager cur = node.children[idx];
//         while (!cur.isLeaf) {
//             cur = cur.children[cur.n];
//         }
//         return cur.transactions[cur.n - 1];
//     }

//     private Transaction getSuccessor(TransactionManager node, int idx) {
//         TransactionManager cur = node.children[idx + 1];
//         while (!cur.isLeaf) {
//             cur = cur.children[0];
//         }
//         return cur.transactions[0];
//     }

//     private void fill(TransactionManager node, int idx) {
//         if (idx != 0 && node.children[idx - 1].n >= t) {
//             borrowFromPrev(node, idx);
//         } else if (idx != node.n && node.children[idx + 1].n >= t) {
//             borrowFromNext(node, idx);
//         } else {
//             if (idx != node.n) {
//                 merge(node, idx);
//             } else {
//                 merge(node, idx - 1);
//             }
//         }
//     }

//     private void borrowFromPrev(TransactionManager node, int idx) {
//         TransactionManager child = node.children[idx];
//         TransactionManager sibling = node.children[idx - 1];

//         for (int i = child.n - 1; i >= 0; i--) {
//             child.transactions[i + 1] = child.transactions[i];
//         }

//         if (!child.isLeaf) {
//             for (int i = child.n; i >= 0; i--) {
//                 child.children[i + 1] = child.children[i];
//             }
//         }

//         child.transactions[0] = node.transactions[idx - 1];

//         if (!node.isLeaf) {
//             child.children[0] = sibling.children[sibling.n];
//         }

//         node.transactions[idx - 1] = sibling.transactions[sibling.n - 1];

//         child.n += 1;
//         sibling.n -= 1;
//     }

//     private void borrowFromNext(TransactionManager node, int idx) {
//         TransactionManager child = node.children[idx];
//         TransactionManager sibling = node.children[idx + 1];

//         child.transactions[child.n] = node.transactions[idx];

//         if (!child.isLeaf) {
//             child.children[child.n + 1] = sibling.children[0];
//         }

//         node.transactions[idx] = sibling.transactions[0];

//         for (int i = 1; i < sibling.n; i++) {
//             sibling.transactions[i - 1] = sibling.transactions[i];
//         }

//         if (!sibling.isLeaf) {
//             for (int i = 1; i <= sibling.n; i++) {
//                 sibling.children[i - 1] = sibling.children[i];
//             }
//         }

//         child.n += 1;
//         sibling.n -= 1;
//     }

//     private void merge(TransactionManager node, int idx) {
//         TransactionManager child = node.children[idx];
//         TransactionManager sibling = node.children[idx + 1];

//         child.transactions[t - 1] = node.transactions[idx];

//         for (int i = 0; i < sibling.n; i++) {
//             child.transactions[i + t] = sibling.transactions[i];
//         }

//         if (!child.isLeaf) {
//             for (int i = 0; i <= sibling.n; i++) {
//                 child.children[i + t] = sibling.children[i];
//             }
//         }

//         for (int i = idx + 1; i < node.n; i++) {
//             node.transactions[i - 1] = node.transactions[i];
//         }

//         for (int i = idx + 2; i <= node.n; i++) {
//             node.children[i - 1] = node.children[i];
//         }

//         child.n += sibling.n + 1;
//         node.n--;}
// }

package com.pfms;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class TransactionBTree {

    private static final int T = 2; // Minimum degree (defines the range of children for a node)

    // B-Tree Node class
    public static class BTreeNode {
        public boolean isLeaf;
        public List<Integer> keys;
        public List<ObjectId> children; // References to child nodes (only relevant if not a leaf)
        public ObjectId nodeId; // ObjectId to identify the node in MongoDB

        public BTreeNode(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        // Convert the BTreeNode to a MongoDB document for storage
        public Document toDocument() {
            Document doc = new Document("isLeaf", isLeaf)
                    .append("keys", keys)
                    .append("children", children);
            return doc;
        }

        // Create a BTreeNode from a MongoDB document
        public static BTreeNode fromDocument(Document doc) {
            BTreeNode node = new BTreeNode(doc.getBoolean("isLeaf"));
            node.keys = (List<Integer>) doc.get("keys");
            node.children = (List<ObjectId>) doc.get("children");
            return node;
        }
    }

    // private MongoCollection<Document> collection; // MongoDB collection to store nodes
    public static ObjectId rootId; // The ObjectId of the root node

    // Constructor to initialize the BTree with a MongoDB collection
    MongoDatabase db = MongoDBConnection.connectToDatabase();
    private MongoCollection<Document> collection = db.getCollection("transactions");
    private MongoCollection<Document> userCollection = db.getCollection("users");

    // Insert a new key into the B-Tree
    public void insert(int key) {
        if (rootId == null) {
            BTreeNode root = new BTreeNode(true); // New root node
            root.keys.add(key);
            rootId = saveBTreeNode(root); // Save root node and get its ObjectId
        } else {
            BTreeNode root = getBTreeNode(rootId);
            if (root.keys.size() == 2 * T - 1) {
                // If root is full, split it
                BTreeNode newRoot = new BTreeNode(false);
                newRoot.children.add(rootId);
                splitChild(newRoot, 0, root);
                rootId = saveBTreeNode(newRoot); // Save new root and update rootId
                insertNonFull(newRoot, key);
            } else {
                insertNonFull(root, key);
            }
        }
        userCollection.updateOne(Filters.eq("_id", User.userId), Updates.set("transactions", TransactionBTree.rootId));
    }

    // Insert into a non-full node
    private void insertNonFull(BTreeNode node, int key) {
        int i = node.keys.size() - 1;
        if (node.isLeaf) {
            node.keys.add(0); // Temporary key space
            while (i >= 0 && key < node.keys.get(i)) {
                node.keys.set(i + 1, node.keys.get(i));
                i--;
            }
            node.keys.set(i + 1, key);
            saveBTreeNode(node); // Save the updated node to MongoDB
        } else {
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            i++;
            BTreeNode child = getBTreeNode(node.children.get(i));
            if (child.keys.size() == 2 * T - 1) {
                splitChild(node, i, child);
                if (key > node.keys.get(i)) {
                    i++;
                }
            }
            insertNonFull(child, key);
        }
    }

    // Split a full child node
    private void splitChild(BTreeNode parent, int i, BTreeNode fullChild) {
        BTreeNode newChild = new BTreeNode(fullChild.isLeaf);
        for (int j = 0; j < T - 1; j++) {
            newChild.keys.add(fullChild.keys.remove(T)); // Move second half keys to new child
        }
        if (!fullChild.isLeaf) {
            for (int j = 0; j < T; j++) {
                newChild.children.add(fullChild.children.remove(T)); // Move second half children
            }
        }
        parent.children.add(i + 1, saveBTreeNode(newChild)); // Save new child and add reference to parent
        parent.keys.add(i, fullChild.keys.remove(T - 1)); // Move middle key to parent
        saveBTreeNode(parent); // Save updated parent node
        saveBTreeNode(fullChild); // Save updated full child node
    }

    // Save a BTreeNode to MongoDB and return its ObjectId
    private ObjectId saveBTreeNode(BTreeNode node) {
        if (node.nodeId == null) {
            node.nodeId = new ObjectId(); // Assign a new ObjectId if it's a new node
        }
        Document doc = node.toDocument().append("_id", node.nodeId);
        collection.replaceOne(new Document("_id", node.nodeId), doc, new com.mongodb.client.model.ReplaceOptions().upsert(true));
        return node.nodeId;
    }

    // Retrieve a BTreeNode from MongoDB using its ObjectId
    private BTreeNode getBTreeNode(ObjectId nodeId) {
        Document doc = collection.find(new Document("_id", nodeId)).first();
        if (doc == null) {
            throw new RuntimeException("Node not found");
        }
        BTreeNode node = BTreeNode.fromDocument(doc);
        node.nodeId = nodeId;
        return node;
    }

    // Method to display the B-Tree (for testing/debugging purposes)
    public void displayTree(ObjectId nodeId, int level) {
        BTreeNode node = getBTreeNode(nodeId);
        System.out.println("Level " + level + " " + node.keys);
        if (!node.isLeaf) {
            for (ObjectId childId : node.children) {
                displayTree(childId, level + 1);
            }
        }
    }
}
