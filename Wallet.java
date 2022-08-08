import java.security.Security;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import java.sql.*;

public class Wallet {

    private String publicKey;
    private String privateKey;
    private double balance;

    private Connection c = null;
    private Statement stmt = null;

    public Wallet(String account, String passphrase) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:blockchain.db");
            System.out.println("Opened database successfully");        
            if (account.length() > 0) {
                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM accounts WHERE address='" + account + "';");
                this.publicKey = rs.getString("publicKey");
                this.privateKey = rs.getString("privateKey");
                this.balance = rs.getDouble("balance");    
                rs.close();
            } else {
                this.create(passphrase);
            }    
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void create(String passphrase) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", passphrase);
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
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO accounts (address,transactionCount,code) " +
                        "VALUES ('"+this.publicKey+"', 0, '');"; 
            stmt.executeUpdate(sql);
        } catch(SQLException | NoSuchProviderException | NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
    }
}