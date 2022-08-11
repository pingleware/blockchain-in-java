public class RemoteMethods implements IRemoteMethods {  

    // Implementing the interface method 
    public void printMsg() {  
       System.out.println("This is an example RMI program");  
    }  
    public void loadValidATS() {
        Common.loadValidATS();
    }
    public String getPublicIpAddress() {
        return Common.getPublicIpAddress();
    }
    public Wallet addNewWallet(String passPhrase) {
        return new Wallet("",passPhrase);
    }
    public String saveToken(String name,String symbol,int decimals,double totalSupply) {
        return Common.saveToken(name,symbol,decimals,totalSupply);
    }

    public String getExchangeRates(String currency) {
        return Common.getExchangeRates(currency);
    }
} 