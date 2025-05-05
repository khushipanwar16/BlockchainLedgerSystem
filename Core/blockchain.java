package core;

import java.io.*;
import structures.MyArray;
import structures.MyLinkedList;
import structures.MyStack;
import utils.TimestampGenerator;

public class Blockchain {
    private MyLinkedList<Block> chain;

    public Blockchain() {
        chain = new MyLinkedList<>();
        loadBlockchainFromFiles();
    }

    public void initializeGenesisBlock() {
        if (chain.size() == 0) {
            MyArray<Transaction> genesisTransactions = new MyArray<>();
            genesisTransactions.add(new Transaction("System", "FirstUser", 0));
            Block genesisBlock = new Block(0, TimestampGenerator.getCurrentTimestamp(), genesisTransactions, "0"); 
            chain.add(genesisBlock);
            saveBlockToFile(genesisBlock); 
        }
    }

    public void addBlock(MyArray<Transaction> transactions) {
        Block lastBlock = chain.get(chain.size() - 1);
        int newIndex = lastBlock.getIndex() + 1;
        String timestamp = TimestampGenerator.getCurrentTimestamp();
        String prevHash = lastBlock.getCurrentHash(); 
        Block newBlock = new Block(newIndex, timestamp, transactions, prevHash);
        chain.add(newBlock);
        saveBlockToFile(newBlock); 
    }

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
            System.out.println("! Error saving block to file.");
            e.printStackTrace();
        }
    }

    public void displayBlockchain() {
        System.out.println("\n📜 Blockchain:");
        System.out.println("Chain size = " + chain.size());
        for (int i = 0; i < chain.size(); i++) {
            Block block = chain.get(i);
            System.out.println(block);
            System.out.println("-------------------------------");
        }
    }

    public void displayBlockchainReverse() {
        System.out.println("\n Blockchain in Reverse Order:");
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
                        writer.write((i + 1) + ". " + tx.toString() + "\n");
                    }
                }
            }
            writer.close();
            System.out.println("User transaction report generated.");
        } catch (IOException e) {
            System.out.println("! Error writing user transaction report.");
            e.printStackTrace();
        }
    }

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
                        writer.write((i + 1) + ". " + tx.toString() + "\n");
                    }
                }
            }
            writer.close();
            System.out.println(" Amount range transaction report generated.");
        } catch (IOException e) {
            System.out.println("! Error writing amount range report.");
            e.printStackTrace();
        }
    }

    public void loadBlockchainFromFiles() {
        File dir = new File("blocks");
        if (!dir.exists()) {
            System.out.println("! Blocks folder doesn't exist. Creating new blockchain.");
            initializeGenesisBlock();
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("! No block files found. Creating Genesis Block.");
            initializeGenesisBlock(); 
            return;
        }
        
        for (int i = 0; i < files.length - 1; i++) {
            for (int j = 0; j < files.length - 1 - i; j++) {
                int index1 = Integer.parseInt(files[j].getName().replaceAll("\\D+", ""));
                int index2 = Integer.parseInt(files[j + 1].getName().replaceAll("\\D+", ""));
                if (index1 > index2) {
                   
                    File temp = files[j];
                    files[j] = files[j + 1];
                    files[j + 1] = temp;
                }
            }
        }

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
                        try {
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
                        
                                while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                                    String txnLine = line.trim();
                                    txnLine = txnLine.replace("  ", ""); 
                                    String[] parts = txnLine.split(" -> | : ");
                                    if (parts.length == 3) {
                                        transactions.add(new Transaction(parts[0], parts[1], Integer.parseInt(parts[2])));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("! Failed to parse line: " + line);
                        }
                    }
                    reader.close();

                    Block block = new Block(index, timestamp, transactions, prevHash, currHash, merkleRoot);

                    if (block.getCurrentHash().equals(currHash) && block.getMerkleRoot().equals(merkleRoot)) {
                        chain.add(block);
                    } else {
                        System.out.println("! Invalid block detected at index " + index + ". Skipping block.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("! Error loading blockchain from files.");
            e.printStackTrace();
        }
    }

    public structures.MyLinkedList<Block> getChain() {
        return chain;
    }
}
