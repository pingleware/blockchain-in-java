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
import java.sql.ResultSet;
import java.sql.SQLException;

public class Block {

	// Every block contains
	// a hash, previous hash and
	// data of the transaction made
    public  String hash;
    public  String previousHash;
    private String data;
    public  long timeStamp;
    private int difficulty;
    public  int nonce;

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
	public Block(String data)
	{
            this.data = data;
            this.timeStamp = new Date().getTime();
            this.previousHash = getPreviousHashString();
            this.nonce = getNONCE();
            this.hash = newHash(this.previousHash,this.timeStamp,this.nonce,this.data);
    
            //String sql = "INSERT INTO  blocks (timestamp,number,hash,parent_hash,extra_data) VALUES ("+this.timeStamp+",(SELECT COUNT(hash)+1 AS count FROM blocks),'"+this.hash+"','(SELECT hash FROM blocks LIMIT 1)','"+this.data+"');";
            String sql = getQuery(this.hash, this.previousHash, this.nonce, this.timeStamp, this.data);
            System.out.println(sql);
            Database.insert(sql);
	}

	// Function to calculate the hash
    public String calculateBlockHash() {
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            String sql = "SELECT hash, COUNT(hash) AS nonce FROM blocks LIMIT 1";
            System.out.println(sql);
            ResultSet rs = Database.query(sql);
            System.out.println(rs);
            String previousHash = Database.rs.getString("hash");
            this.previousHash = previousHash;
            System.out.println(this.previousHash);
            int nonce = Database.rs.getInt("nonce");
            this.nonce = nonce;
            System.out.println(this.nonce);
    
            String dataToHash = this.previousHash 
              + Long.toString(this.timeStamp) 
              + Integer.toString(this.nonce) 
              + this.data;

            System.out.println(dataToHash);
    
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));

            StringBuffer buffer = new StringBuffer();
            for (byte b : bytes) {
                buffer.append(String.format("%02x", b));
            }
            return buffer.toString();
        } catch (SQLException | NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                if (Database.connection != null) {
                    Database.connection.close();
                }
            } catch(SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return null;
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
            System.err.println(ex.getStackTrace());
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
        return new Block("Genesis");
    }

    public static String getPreviousHashString() {
        try {
            ResultSet rs = Database.query("SELECT hash FROM blocks ORDER BY number DESC");
            return rs.getString("hash");
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (Database.connection != null) {
                    Database.connection.close();
                }
            } catch(SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return null;
    }

    public static int getNONCE() {
        try {
            ResultSet rs = Database.query("SELECT COUNT(hash) AS nonce FROM blocks;");
            return rs.getInt("nonce");
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (Database.connection != null) {
                    Database.connection.close();
                }
            } catch(SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return 0;
    }

    public static String getQuery(String hash,String previousHash,int nonce,long timeStamp,String data) {
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