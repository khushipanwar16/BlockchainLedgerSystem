package core;

// Represents a single transaction inside a block
public class Transaction {
    private String sender;
    private String receiver;
    private int amount; // Using int (primitive) for simplicity

    // Constructor to create a new transaction
    public Transaction(String sender, String receiver, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    // Getters
    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getAmount() {
        return amount;
    }

    // Simple string format to save transaction info
    @Override
    public String toString() {
        return sender + " -> " + receiver + " : " + amount;
    }

    // Serialize for file saving (if needed separately)
    public String serialize() {
        return sender + "," + receiver + "," + amount;
    }
}
