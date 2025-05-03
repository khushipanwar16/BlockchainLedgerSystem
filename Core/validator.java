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

    public void validateBlockchain() {
        try {
            File dir = new File("output");
            if (!dir.exists()) {
                dir.mkdir(); // Ensure output folder exists
            }
    
            FileWriter writer = new FileWriter("output/validation_report.txt");
            writer.write("🔍 Blockchain Validation Report:\n\n");
    
            for (int i = 0; i < chain.size(); i++) {
                Block block = chain.get(i);
                writer.write("Block " + block.getIndex() + ": ");
    
                boolean merkleValid = validateMerkleRoot(block);
                boolean hashValid = validateCurrentHash(block);
                boolean linkValid = (i == 0) || block.getPreviousHash().equals(chain.get(i - 1).getCurrentHash());
    
                // 🚨 Centralized Hash Link Error Detection
                if (!linkValid) {
                    writer.write("❌ Hash Link Error!\n");
                    writer.write("  Expected: " + chain.get(i - 1).getCurrentHash() + " | ");
                    writer.write("  Found: " + block.getPreviousHash() + "\n");
                }
    
                if (!hashValid) {
                    
                    writer.write("Stored: " + block.getCurrentHash() + " | ");
                    writer.write("Recomputed: " + block.calculateCurrentHash() + "\n");
    
                  
                    writer.close();
                    throw new IllegalStateException("Block " + block.getIndex() + " hash mismatch!");
                }
    
                writer.write((merkleValid ? "✔ Merkle Valid | " : "❌ Merkle Invalid | "));
                writer.write((linkValid ? "✔ Previous Link OK" : "❌ Previous Link Broken") + "\n");
            }
    
            writer.write("\n✅ Blockchain validation report saved.\n");
            writer.close();
    
            System.out.println("✅ Report saved to 'output/validation_report.txt'.");
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
