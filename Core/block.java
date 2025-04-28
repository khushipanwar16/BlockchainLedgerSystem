package core;

import structures.MyArray;
import structures.MyMerkleTree;
import utils.HashSimulator;
import utils.TimestampGenerator;

// Represents a single block in the blockchain
public class Block {

    private int index;                        // Block number
    private String timestamp;                 // When the block was created
    private MyArray<Transaction> transactions; // List of transactions
    private String merkleRoot;                 // Merkle Root of all transactions
    private String previousHash;               // Hash of the previous block
    private String currentHash;                // Hash of this block

    // Constructor for creating a new block
    public Block(int index, String timestamp, MyArray<Transaction> transactions, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.merkleRoot = buildMerkleRoot();      // Generate Merkle Root
        this.currentHash = calculateCurrentHash(); // Generate Current Block Hash
    }

    // Build Merkle Root from transactions
    private String buildMerkleRoot() {
        if (transactions.size() == 0) return "";

        String[] hashes = new String[transactions.size()];
        for (int i = 0; i < transactions.size(); i++) {
            hashes[i] = HashSimulator.hash(transactions.get(i).serialize());
        }

        MyMerkleTree<String> merkleTree = new MyMerkleTree<>(hashes);
        return merkleTree.getMerkleRoot();
    }

    // Calculate current block hash
    private String calculateCurrentHash() {
        String blockData = index + timestamp + merkleRoot + previousHash;
        return HashSimulator.hash(blockData);
    }

    // Getters for all important fields
    public int getIndex() {
        return index;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public MyArray<Transaction> getTransactions() {
        return transactions;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    // Serialize block data to save into a file
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("Index: ").append(index).append("\n");
        builder.append("Timestamp: ").append(timestamp).append("\n");
        builder.append("PreviousHash: ").append(previousHash).append("\n");
        builder.append("CurrentHash: ").append(currentHash).append("\n");
        builder.append("MerkleRoot: ").append(merkleRoot).append("\n");
        builder.append("Transactions:\n");
        for (int i = 0; i < transactions.size(); i++) {
            builder.append("  ").append(transactions.get(i).toString()).append("\n");
        }
        return builder.toString();
    }

    // For easy console printing
    @Override
    public String toString() {
        return serialize();
    }
}
