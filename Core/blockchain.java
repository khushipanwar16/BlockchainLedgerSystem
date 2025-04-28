package core;

import structures.MyLinkedList;
import structures.MyStack;
import structures.MyQueue;
import structures.MyArray;
import utils.TimestampGenerator;

import java.io.*;
import java.util.Scanner;

// Manages the entire blockchain
public class Blockchain {
    private MyLinkedList<Block> chain;

    // Constructor: Initialize an empty chain
    public Blockchain() {
        chain = new MyLinkedList<>();
        initializeGenesisBlock();
    }

    // Create the first block manually
    private void initializeGenesisBlock() {
        if (chain.size() == 0) {
            MyArray<Transaction> genesisTransactions = new MyArray<>();
            genesisTransactions.add(new Transaction("System", "FirstUser", 0));
            Block genesisBlock = new Block(0, TimestampGenerator.getCurrentTimestamp(), genesisTransactions, "0");
            chain.add(genesisBlock);
            saveBlockToFile(genesisBlock);
        }
    }

    // Add a new block to the chain
    public void addBlock(MyArray<Transaction> transactions) {
        Block lastBlock = chain.get(chain.size() - 1);
        int newIndex = lastBlock.getIndex() + 1;
        String timestamp = TimestampGenerator.getCurrentTimestamp();
        String prevHash = lastBlock.getCurrentHash();
        Block newBlock = new Block(newIndex, timestamp, transactions, prevHash);
        chain.add(newBlock);
        saveBlockToFile(newBlock);
    }

    // Save block into a file inside /blocks/
    private void saveBlockToFile(Block block) {
        try {
            File dir = new File("blocks");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileWriter writer = new FileWriter("blocks/block_" + block.getIndex() + ".txt");
            writer.write(block.serialize());
            writer.close();
        } catch (IOException e) {
            System.out.println("❗ Error saving block to file.");
            e.printStackTrace();
        }
    }

    // Display the blockchain (latest to oldest)
    public void displayBlockchain() {
        System.out.println("\n📜 Blockchain:");
        for (int i = 0; i < chain.size(); i++) {
            Block block = chain.get(i);
            System.out.println(block);
            System.out.println("-------------------------------");
        }
    }

    // Display blockchain in reverse (latest block first)
    public void displayBlockchainReverse() {
        System.out.println("\n🔁 Blockchain in Reverse Order:");
        MyStack<Block> stack = new MyStack<>();
        for (int i = 0; i < chain.size(); i++) {
            stack.push(chain.get(i));
        }
        while (!stack.isEmpty()) {
            Block block = stack.pop();
            System.out.println(block);
            System.out.println("-------------------------------");
        }
    }

    // Search all transactions involving a specific user
    public void viewTransactionsByUser(String username) {
        try {
            FileWriter writer = new FileWriter("output/user_transactions_" + username + ".txt");
            writer.write("Transactions involving '" + username + "':\n");

            for (int i = 0; i < chain.size(); i++) {
                Block block = chain.get(i);
                MyArray<Transaction> txns = block.getTransactions();
                for (int j = 0; j < txns.size(); j++) {
                    Transaction tx = txns.get(j);
                    if (tx.getSender().equalsIgnoreCase(username) || tx.getReceiver().equalsIgnoreCase(username)) {
                        writer.write((i+1) + ". " + tx.toString() + "\n");
                    }
                }
            }
            writer.close();
            System.out.println("✅ User transaction report generated.");
        } catch (IOException e) {
            System.out.println("❗ Error writing user transaction report.");
            e.printStackTrace();
        }
    }

    // Search transactions by amount range
    public void searchTransactionsByAmount(int minAmount, int maxAmount) {
        try {
            FileWriter writer = new FileWriter("output/amount_range_report.txt");
            writer.write("Transactions between " + minAmount + " and " + maxAmount + ":\n");

            for (int i = 0; i < chain.size(); i++) {
                Block block = chain.get(i);
                MyArray<Transaction> txns = block.getTransactions();
                for (int j = 0; j < txns.size(); j++) {
                    Transaction tx = txns.get(j);
                    if (tx.getAmount() >= minAmount && tx.getAmount() <= maxAmount) {
                        writer.write((i+1) + ". " + tx.toString() + "\n");
                    }
                }
            }
            writer.close();
            System.out.println("✅ Amount range transaction report generated.");
        } catch (IOException e) {
            System.out.println("❗ Error writing amount range report.");
            e.printStackTrace();
        }
    }

    // Load blockchain from existing block files
    public void loadBlockchainFromFiles() {
        File dir = new File("blocks");
        if (!dir.exists()) {
            System.out.println("❗ Blocks folder doesn't exist. Creating new blockchain.");
            initializeGenesisBlock();
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("❗ No block files found. Creating Genesis Block.");
            initializeGenesisBlock();
            return;
        }

        // Clear current chain first
        chain = new MyLinkedList<>();

        try {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile() && file.getName().startsWith("block_")) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    int index = 0;
                    String timestamp = "";
                    String prevHash = "";
                    String currHash = "";
                    String merkleRoot = "";
                    MyArray<Transaction> transactions = new MyArray<>();

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("Index:")) {
                            index = Integer.parseInt(line.substring(6).trim());
                        } else if (line.startsWith("Timestamp:")) {
                            timestamp = line.substring(10).trim();
                        } else if (line.startsWith("PreviousHash:")) {
                            prevHash = line.substring(13).trim();
                        } else if (line.startsWith("CurrentHash:")) {
                            currHash = line.substring(12).trim();
                        } else if (line.startsWith("MerkleRoot:")) {
                            merkleRoot = line.substring(11).trim();
                        } else if (line.startsWith("Transactions:")) {
                            // Continue reading transaction lines
                            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                                String txnLine = line.trim();
                                txnLine = txnLine.replace("  ", ""); // remove indent
                                String[] parts = txnLine.split(" -> | : ");
                                if (parts.length == 3) {
                                    transactions.add(new Transaction(parts[0], parts[1], Integer.parseInt(parts[2])));
                                }
                            }
                        }
                    }
                    reader.close();
                    Block block = new Block(index, timestamp, transactions, prevHash);
                    chain.add(block);
                }
            }
            System.out.println("✅ Blockchain loaded successfully from files.");
        } catch (IOException e) {
            System.out.println("❗ Error loading blockchain from files.");
            e.printStackTrace();
        }
    }


// Getter to expose the chain to Validator
public structures.MyLinkedList<Block> getChain() {
    return chain;
}



}
