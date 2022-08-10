public interface IWallet {
    public Wallet create(String passphrase);
    public String getPrivateKey();
}