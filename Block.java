// Java implementation for creating a block in a Blockchain

import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.List;

public class Block {

	// Every block contains
	// a hash, previous hash and
	// data of the transaction made
    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    private Logger logger;

	// Constructor for the block
	public Block(String data, String previousHash, long timeStamp)
	{
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
	}

	// Function to calculate the hash
    public String calculateBlockHash() {
        String dataToHash = previousHash 
          + Long.toString(timeStamp) 
          + Integer.toString(nonce) 
          + data;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public long getTimestamp() {
        return this.timeStamp;
    }

    public String getData() {
        return this.data;
    }

    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix).equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }

    public static void main(String args[]) {
        List<Block> blockchain = new ArrayList<>();
        int prefix = 4;
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        blockchain.add(new Block("Genesis", prefixString, new Date().getTime()));

        Block newBlock = new Block("Test", blockchain.get(blockchain.size() - 1).getHash(), new Date().getTime());
        newBlock.mineBlock(0);
        blockchain.add(newBlock);
        System.out.println(blockchain.get(0).getHash());
        System.out.println(blockchain.get(0).getTimestamp());
        System.out.println(blockchain.get(0).getData());
        System.out.println(blockchain.get(1).getHash());
        System.out.println(blockchain.get(1).getTimestamp());
        System.out.println(blockchain.get(1).getData());
    }
}