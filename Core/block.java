package core;

import structures.MyArray;
import structures.MyMerkleTree;
import utils.HashSimulator;

public class Block {
    private int index;                       
    private String timestamp;                 
    private MyArray<Transaction> transactions; 
    private String merkleRoot;                
    private String previousHash;            
    private String currentHash;               

    public Block(int index, String timestamp, MyArray<Transaction> transactions, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.merkleRoot = buildMerkleRoot();     
        this.currentHash = calculateCurrentHash(); 
    }

    public Block(int index, String timestamp, MyArray<Transaction> transactions, String previousHash, String currentHash, String merkleRoot) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.currentHash = currentHash;
        this.merkleRoot = merkleRoot;

        validateCurrentHash();
        validateMerkleRoot();
    }

    private String buildMerkleRoot() {
        if (transactions.size() == 0) return "";
        String[] hashes = new String[transactions.size()];
        for (int i = 0; i < transactions.size(); i++) {
            hashes[i] = HashSimulator.hash(transactions.get(i).serialize());
        }
        MyMerkleTree<String> merkleTree = new MyMerkleTree<>(hashes);
        return merkleTree.getMerkleRoot();
    }

  
    public String calculateCurrentHash() {
        String blockData = index + timestamp + merkleRoot + previousHash;
        return HashSimulator.hash(blockData);
    }

    private void validateCurrentHash() {
        String recalculatedHash = calculateCurrentHash();
        
    }
 
    private void validateMerkleRoot() {
        String recalculatedMerkleRoot = buildMerkleRoot();
    }

    // public boolean validatePreviousHash(String previousBlockHash) {
    //     return previousHash.equals(previousBlockHash);
    // }


    public int getIndex() { return index; }
    public String getTimestamp() { return timestamp; }
    public MyArray<Transaction> getTransactions() { return transactions; }
    public String getMerkleRoot() { return merkleRoot; }
    public String getPreviousHash() { return previousHash; }
    public String getCurrentHash() { return currentHash; }

    public void setCurrentHash(String hash) { this.currentHash = hash; }

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

    @Override
    public String toString() {
        return serialize();
    }
}
