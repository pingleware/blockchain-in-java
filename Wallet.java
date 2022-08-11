import java.security.Security;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.MessageDigest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Wallet implements IWallet, Serializable {

    public  String publicKey;
    private String privateKey;
    private double balance;

    public Wallet(String account, String passphrase) {
        if (account.length() > 0) {
            try {
                ResultSet rs = Database.query("SELECT a.address,a.transactionCount,a.privateKey,b.balance FROM accounts a JOIN balances b ON a.address=b.address WHERE a.address='" + account + "';");
                this.publicKey = rs.getString("address");
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
            String sql = "INSERT INTO accounts (address,privateKey,transactionCount,code) VALUES ('"+this.publicKey+"','"+this.privateKey+"', 0, '"+passphrase+"');"; 
            Database.insert(sql);
            sql = "INSERT INTO balances (address,balance) VALUES ('"+this.publicKey+"',0.00);";
            System.out.println(sql);
            Database.insert(sql);
            this.createTransaction(this.publicKey, 0.00);
            return this;
        } catch(NoSuchAlgorithmException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public String sign(String data, String privateKey) {
        try {
            byte [] pkcs8EncodedBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privKey = kf.generatePrivate(keySpec);

            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initSign(privKey);
            sig.update(data.getBytes());
            byte[] signatureBytes = sig.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);    
        } catch(NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction createTransaction(String recipient, double amount) {
        Transaction transaction = new Transaction(this.publicKey, recipient, amount);
        return transaction;
    }

    public void deposit(double amount) {
        String sql = "UPDATE balances SET balance=(SELECT balance FROM balances WHERE address='"+this.publicKey+"')+"+amount+" WHERE address='"+this.publicKey+"';";
        Database.insert(sql);
        this.createTransaction(this.publicKey, amount);
    }

    public double withdrawal(double amount) {
        try {
            ResultSet rs = Database.query("SELECT balance FROM balances WHERE address='"+this.publicKey+"'");
            double currentBalance = rs.getDouble("balance");
            rs.close();
            if (currentBalance >= amount) {
                String sql = "UPDATE balances SET balance=(SELECT balance FROM balances WHERE address='"+this.publicKey+"')-"+amount+" WHERE address='"+this.publicKey+" AND (SELECT balance FROM balances WHERE address='"+this.publicKey+"') >= "+amount+";";
                Database.insert(sql);
                this.createTransaction(this.publicKey, (-1 * amount));
                return amount;    
            } 
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }
}