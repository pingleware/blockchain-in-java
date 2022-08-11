/**
 * SERVER.java
 * 
 * The main entry point which cretes a Web and RPC server for starting the blockchain and providing endpoints for blockchain operations
 * 
 * IMPORTANT:
 * To ensure integrity is maintained, the first task is to verify the jar file is signed using the correct keystore by using the public key?
 */
import java.security.*;
import java.util.*;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.ClassLoader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import javax.tools.*;
import java.util.*;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 

public class Server {


    public static void main (String [] args){
        boolean _privateNode = false;

        if (args.length == 1) {
            String s1 = new String(args[0]);
            String s2 = new String("-private");
    
            if ( s1.compareToIgnoreCase(s2) > 0 ) {
                _privateNode = false;
            } else if ( s1.compareToIgnoreCase(s2) < 0 ) {
                _privateNode = false;
            } else {
                System.out.println(args[0]);
                _privateNode = true;
            }    
        }

        if (_privateNode == false) {
            Common.loadValidATS();
            String localIPAddress = Common.getPublicIpAddress();
            boolean found = false;
            int total = Common.ipAddress.size();
            for (int k=0; k < total; k++) {
                String _ip = Common.ipAddress.get(k);
                if (Common.stringCompare(_ip,localIPAddress) != 0) {
                    found = true;
                    break;
                }
            }
        }
        try { 
            System.setProperty("java.rmi.server.hostname","127.0.0.1");

            // Instantiating the implementation class 
            RemoteMethods obj = new RemoteMethods(); 
       
            // Exporting the object of implementation class  
            // (here we are exporting the remote object to the stub) 
            IRemoteMethods stub = (IRemoteMethods) UnicastRemoteObject.exportObject(obj, 0);  
            
            // Binding the remote object (stub) in the registry 
            Registry registry = LocateRegistry.createRegistry(1337);
            registry.bind("RemoteMethods", stub);
            if (_privateNode == true) {
                System.err.println("A PRIVATE Blockchain Server is ready!"); 
            } else {
                System.err.println("Blockchain [PUBLIC] Server is ready!"); 
            }
         } catch (Exception e) { 
            System.err.println("Server exception: " + e.toString()); 
            e.printStackTrace(); 
        }
        /*         
        try {
            // 1. Verify the node is registered validating IP and node name gainst github.com public repository?
            // if not registered, thhrow an exception and end the program

            ipAddress = new ArrayList<String>();

            String atsList = sendGET("https://raw.githubusercontent.com/pingleware/blockchain-in-java/main/blockchain.json");
            String items[] = atsList.split("\"([0-9]|[0-9][0-9]|[0-9][0-9][0-9])\":");
            for (int i=0; i<items.length; i++) {
                String item[] = items[i].split("\"ip\":");
                for (int j=1; j<item.length; j++) {
                    String ip[] = item[j].split(",");
                    String _ipAddress = ip[0].replace("\"","").substring(1);
                    ipAddress.add(_ipAddress); 
                }
            }

            //System.out.println(getPublicIpAddress());
            String localIPAddress = getPublicIpAddress();
            boolean found = false;
            int total = ipAddress.size();
            for (int k=0; k < total; k++) {
                String _ip = ipAddress.get(k);
                if (stringCompare(_ip,localIPAddress) != 0) {
                    found = true;
                    System.out.println("FOUND!");
                    break;
                }
            }
            if (found == false) {
                throw new Exception("Not authorized as a public node?");
            }

            //Block.genesis();

            // Create a new account
            // Use https://random-generator.net/random-passphrase-generator/ tp create a passphrase
            Wallet wallet = new Wallet("","schick unpunished fiercely nectary placoid mahout leakiest becurse stockiest acridly soupiest ejecting glazy crueler goodman");
            System.out.println("account: " + wallet.publicKey);
            System.out.println("private key: " + wallet.getPrivateKey());


            saveClass("Token",wallet.publicKey,20000);
            // Start JSON RPC Server

            // Start REST API Server

           
        } catch (IOException | NullPointerException | ProviderException exception){
           System.err.println("JavaServer: " + exception);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        */
    }

}