import java.math.BigInteger;

public interface EIP721TokenReceiver {
    public String onERC721Received(String _operator, String _from, BigInteger _tokenId, String _data);
}