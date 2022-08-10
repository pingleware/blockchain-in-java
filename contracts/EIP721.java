import java.math.BigDecimal;
import java.math.BigInteger;

public interface EIP721 {
    public BigDecimal balanceOf(String _owner);
    public String ownerOf(BigInteger _tokenId);
    public void safeTransferFrom(String _from, String _to, BigInteger _tokenId, String data);
    public void safeTransferFrom(String _from, String _to, BigInteger _tokenId);
    public void transferFrom(String _from, String _to, BigInteger _tokenId);
    public void approve(String _approved, BigInteger _tokenId);
    public void setApprovalForAll(String _operator, boolean _approved);
    public String getApproved(BigInteger _tokenId);
    public boolean isApprovedForAll(String _owner, String _operator);
}