import java.security.Security;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Wallet {

    public  String publicKey;
    private String privateKey;
    private double balance;

    public Wallet(String account, String passphrase) {
        if (account.length() > 0) {
            try {
                ResultSet rs = Database.query("SELECT * FROM accounts WHERE address='" + account + "';");
                this.publicKey = rs.getString("publicKey");
                this.privateKey = rs.getString("privateKey");
                this.balance = rs.getDouble("balance");    
                rs.close();    
            } catch(SQLException ex) {
                System.err.println(ex);
            }
        } else {
            this.create(passphrase);
        }    
    }

    public Wallet create(String passphrase) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            KeyPair pair = keyGen.generateKeyPair();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(pair.getPublic().toString().getBytes(StandardCharsets.UTF_8));
            StringBuffer buffer = new StringBuffer();
            for (byte b : bytes) {
                buffer.append(String.format("%02x", b));
            }    
            this.privateKey = pair.getPrivate().toString();
            this.publicKey = buffer.toString();    
            this.balance = 0;
            String sql = "INSERT INTO accounts (address,transactionCount,code) VALUES ('"+this.publicKey+"', 0, '"+passphrase+"');"; 
            Database.insert(sql);
            return this;
        } catch(NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }
}