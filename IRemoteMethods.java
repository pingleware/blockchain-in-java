import java.rmi.Remote; 
import java.rmi.RemoteException;  

// Creating Remote interface for our application 
public interface IRemoteMethods extends Remote {  
   void printMsg() throws RemoteException;
   void loadValidATS() throws RemoteException;
   String getPublicIpAddress() throws RemoteException;
   Wallet addNewWallet(String passphrase) throws RemoteException;
   String saveToken(String name,String symbol,int decimals,double totalSupply) throws RemoteException;
} 