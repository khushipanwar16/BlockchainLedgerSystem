package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import structures.MyArray;
import structures.MyLinkedList;
import utils.HashSimulator;

public class Validator {

    private MyLinkedList<Block> chain;

    public Validator(MyLinkedList<Block> chain) {
        this.chain = chain;
    }

    public void validateBlockchain() {
        try {
            File dir = new File("output");
            if (!dir.exists()) {
                dir.mkdir(); 
            }
    
            FileWriter writer = new FileWriter("output/validation_report.txt");
            writer.write(" Blockchain Validation Report:\n\n");
    
            for (int i = 0; i < chain.size(); i++) {
                Block block = chain.get(i);
                writer.write("Block " + block.getIndex() + ": ");
    
                boolean merkleValid = validateMerkleRoot(block);
                boolean hashValid = validateCurrentHash(block);
                boolean linkValid = (i == 0) || block.getPreviousHash().equals(chain.get(i - 1).getCurrentHash());
    
                if (!linkValid) {
                    writer.write("❌ Hash Link Error!\n");
                    writer.write("  Expected: " + chain.get(i - 1).getCurrentHash() + " | ");
                    writer.write("  Found: " + block.getPreviousHash() + "\n");
                }
    
                if (!hashValid) {
                    
                    writer.write("Stored: " + block.getCurrentHash() + " | ");
                    writer.write("Recomputed: " + block.calculateCurrentHash() + "\n");
    
                  
                    // writer.write(" Block " + block.getIndex() + " hash mismatch!\n");

                }
    
                writer.write((merkleValid ? "✔ Merkle Valid | " : "❌ Merkle Invalid | "));
                writer.write((linkValid ? "✔ Previous Link OK" : "❌ Previous Link Broken") + "\n");
            }
    
            writer.close();
    
            System.out.println(" Report saved to 'output/validation_report.txt'.");
        } catch (IOException e) {
            System.out.println("! Error writing validation report.");
            e.printStackTrace();
        }
    }
    
    private boolean validateMerkleRoot(Block block) {
        MyArray<Transaction> txns = block.getTransactions();
        if (txns.size() == 0) return block.getMerkleRoot().equals("");

        String[] hashes = new String[txns.size()];
        for (int i = 0; i < txns.size(); i++) {
            hashes[i] = HashSimulator.hash(txns.get(i).serialize());
        }

        structures.MyMerkleTree<String> merkleTree = new structures.MyMerkleTree<>(hashes);
        String recomputedRoot = merkleTree.getMerkleRoot();
        return recomputedRoot.equals(block.getMerkleRoot());
    }

    private boolean validateCurrentHash(Block block) {
        String data = block.getIndex() + block.getTimestamp() + block.getMerkleRoot() + block.getPreviousHash();
        String recomputedHash = HashSimulator.hash(data);
        return recomputedHash.equals(block.getCurrentHash());
    }

private boolean validatePreviousHash(Block prevBlock, Block currentBlock) {
    return prevBlock.getCurrentHash().equals(currentBlock.getPreviousHash());
}

}
