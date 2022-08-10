import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

public class Blockchain {

    public static ArrayList<Block> chain;

    public Blockchain() {
        chain.add(Block.genesis());
    }

    public void addBlock(String data) {
        Block newBlock = new Block(data);
        newBlock.mineBlock(4);
        chain.add(newBlock);
    }

    public static String getPreviousHash() {
        return chain.get(chain.size() - 1).getHash();
    }
    
}