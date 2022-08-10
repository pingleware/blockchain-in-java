import java.math.BigDecimal;

public interface EIP721Listener {
    public void Transfer(String _from, String _to, BigDecimal _tokenId);
    public void Approval(String _owner, String _approved, BigDecimal _tokenId);
    public void ApprovalForAll(String _owner, String _operator, Boolean _approved);    
}