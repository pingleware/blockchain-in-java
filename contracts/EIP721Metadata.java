import java.math.BigInteger;

public interface EIP721Metadata {
    public String name();
    public String symbol();
    public String tokenURI(BigInteger _tokenId);
}