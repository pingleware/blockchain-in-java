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
    private int difficulty;
    private int nonce;

    private Logger logger;


	// Constructor for the block
    /**
     *  int prefix = 4;
     *  String previousHash = new String(new char[prefix]).replace('\0', '0');
     *  long timeStamp = new Date().getTime();
     * 
     * @param data
     * @param previousHash
     * @param timeStamp
     */
	public Block(String data, String previousHash, long timeStamp)
	{
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();

        String sql = getQuery(data, previousHash, timeStamp);
        System.out.println(sql);
        Database.insert(sql);
	}

	// Function to calculate the hash
    public String calculateBlockHash() {
        String dataToHash = this.previousHash 
          + Long.toString(this.timeStamp) 
          + Integer.toString(this.nonce) 
          + this.data;
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

    public static String newHash(String previousHash,long timeStamp,int nonce,String data) {
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
            System.out.println(ex.getMessage());
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
        while (!this.hash.substring(0, prefix).equals(prefixString)) {
            this.nonce++;
            this.hash = calculateBlockHash();
        }
        return hash;
    }

    public static Block genesis() {
        int prefix = 4;
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        return new Block("Genesis", prefixString, new Date().getTime());
    }

    public static String getQuery(String data,String previousHash,long timeStamp) {
        String hash = newHash(previousHash,timeStamp,1,data);

        return "INSERT INTO  blocks (timestamp,number,hash,parent_hash,extra_data) VALUES ("+timeStamp+",(SELECT COUNT(hash)+1 AS count FROM blocks),'"+hash+"','"+previousHash+"','"+data+"');";
    }
    /*
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
    */
}