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

    private static final int T = 3; // Minimum degree (defines the range of children for a node)

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
        System.out.println(PFMS.GREEN + "Level " + level + " " + node.keys);
        if (!node.isLeaf) {
            for (ObjectId childId : node.children) {
                displayTree(childId, level + 1);
            }
        }
    }
}
