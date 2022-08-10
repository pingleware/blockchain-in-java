/**
 * Client.java
 * 
 * Client application to interface with the Server.java RMI
 * @see https://www.tutorialspoint.com/java_rmi/java_rmi_application.htm
 * 
 */
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  

public class Client {  
   public static void main(String[] args) {  
      try {  
         // Getting the registry 
         Registry registry = LocateRegistry.getRegistry(1337); 
    
         // Looking up the registry for the remote object 
         IRemoteMethods stub = (IRemoteMethods) registry.lookup("RemoteMethods"); 
    
         // Calling the remote method using the obtained object 
         stub.printMsg(); 

         String ipaddress = stub.getPublicIpAddress();
         System.out.println(ipaddress);

         Wallet wallet = stub.addNewWallet("my not so secret passphrase");
         System.out.println(wallet.publicKey);
         
         // System.out.println("Remote method invoked"); 
      } catch (Exception e) {
         System.err.println("Client exception: " + e.toString()); 
         e.printStackTrace(); 
      }
   } 
}