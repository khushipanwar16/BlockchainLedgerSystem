package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import structures.MyArray;
import structures.MyLinkedList;
import utils.HashSimulator;

// Class to validate blockchain
public class Validator {

    private MyLinkedList<Block> chain;

    // Constructor: take the blockchain to validate
    public Validator(MyLinkedList<Block> chain) {
        this.chain = chain;
    }

    // Validate the entire blockchain
    public void validateBlockchain() {
        try {
            File dir = new File("output");
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileWriter writer = new FileWriter("output/validation_report.txt");
            writer.write("Blockchain Validation Report:\n\n");

            for (int i = 0; i < chain.size(); i++) {
                Block block = chain.get(i);
                writer.write("Block " + block.getIndex() + ":\n");

                boolean merkleValid = validateMerkleRoot(block);
                boolean hashValid = validateCurrentHash(block);
                boolean linkValid = (i == 0) || validatePreviousHash(chain.get(i - 1), block);

                writer.write(merkleValid ? "✔ Merkle Root Valid\n" : "❌ Merkle Root Invalid\n");
                writer.write(hashValid ? "✔ Hash Match\n" : "❌ Hash Mismatch\n");
                writer.write(linkValid ? "✔ Previous Link OK\n" : "❌ Previous Link Broken\n");

                boolean blockValid = merkleValid && hashValid && linkValid;
                writer.write("Status: " + (blockValid ? "VALID" : "INVALID") + "\n");
                writer.write("-----------------------------------\n");
            }

            writer.close();
            System.out.println("✅ Blockchain validation report generated successfully.");
        } catch (IOException e) {
            System.out.println("❗ Error writing validation report.");
            e.printStackTrace();
        }
    }

    // Validate Merkle Root by recomputing it
    private boolean validateMerkleRoot(Block block) {
        MyArray<Transaction> txns = block.getTransactions();
        if (txns.size() == 0) return block.getMerkleRoot().equals("");

        String[] hashes = new String[txns.size()];
        for (int i = 0; i < txns.size(); i++) {
            hashes[i] = HashSimulator.hash(txns.get(i).serialize());
        }

        // Rebuild Merkle Tree
        structures.MyMerkleTree<String> merkleTree = new structures.MyMerkleTree<>(hashes);
        String recomputedRoot = merkleTree.getMerkleRoot();
        return recomputedRoot.equals(block.getMerkleRoot());
    }

    // Validate the Current Hash by recalculating block data
    private boolean validateCurrentHash(Block block) {
        String data = block.getIndex() + block.getTimestamp() + block.getMerkleRoot() + block.getPreviousHash();
        String recomputedHash = HashSimulator.hash(data);
        return recomputedHash.equals(block.getCurrentHash());
    }

    // Validate the linkage between two blocks
    private boolean validatePreviousHash(Block prevBlock, Block currentBlock) {
        return prevBlock.getCurrentHash().equals(currentBlock.getPreviousHash());
    }
}
