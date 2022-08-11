import java.util.ArrayList;
import java.io.Serializable;

class Transaction implements Serializable {

    public Transaction(String senderWallet, String recipient, double amount) {
        ArrayList<String> data = new ArrayList<String>();
        data.add(senderWallet);
        data.add(recipient);
        data.add(String.format("%f",amount));
        Block block = new Block(data.toString());
        String subSQL = "SELECT number FROM blocks WHERE hash='"+block.hash+"'";
        String sql = "INSERT INTO transactions (hash,from_address,to_address,value,block_timestamp,block_number,block_hash) VALUES ('"+block.hash+"','"+senderWallet+"','"+recipient+"',"+amount+","+block.timeStamp+","+(subSQL)+",'"+block.hash+"');";
        Database.insert(sql);
        sql = "UPDATE accounts SET transactionCount=(SELECT transactionCount FROM accounts WHERE address='"+senderWallet+"')+1 WHERE address='"+senderWallet+"';";
        Database.insert(sql);
    }
}