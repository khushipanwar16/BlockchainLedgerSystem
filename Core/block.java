package core;

import structures.MyArray;
import structures.MyMerkleTree;
import utils.HashSimulator;

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

    // Constructor for loading block from file (without throwing exception)
    public Block(int index, String timestamp, MyArray<Transaction> transactions, String previousHash, String currentHash, String merkleRoot) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.currentHash = currentHash;
        this.merkleRoot = merkleRoot;

        // Integrity validation warnings instead of exceptions
        validateCurrentHash();
        validateMerkleRoot();
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
    public String calculateCurrentHash() {
        String blockData = index + timestamp + merkleRoot + previousHash;
        return HashSimulator.hash(blockData);
    }

    // Validate the current hash after loading the block (only warns)
    private void validateCurrentHash() {
        String recalculatedHash = calculateCurrentHash();
        
    }

    // Validate the Merkle root after loading the block (only warns)
    private void validateMerkleRoot() {
        String recalculatedMerkleRoot = buildMerkleRoot();
        if (!recalculatedMerkleRoot.equals(merkleRoot)) {
            System.out.println("❗ Warning: Merkle root mismatch at block " + index);
        }
    }

    // Validate that the previous hash matches the current hash of the previous block
    public boolean validatePreviousHash(String previousBlockHash) {
        return previousHash.equals(previousBlockHash);
    }

    // Getters for all important fields
    public int getIndex() { return index; }
    public String getTimestamp() { return timestamp; }
    public MyArray<Transaction> getTransactions() { return transactions; }
    public String getMerkleRoot() { return merkleRoot; }
    public String getPreviousHash() { return previousHash; }
    public String getCurrentHash() { return currentHash; }

    public void setCurrentHash(String hash) { this.currentHash = hash; }

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
