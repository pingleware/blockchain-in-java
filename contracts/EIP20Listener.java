import java.math.BigDecimal;

public interface EIP20Listener {
    public void Transfer(String _from, String _to, BigDecimal _value);
    public void Approval(String _owner, String _spender, BigDecimal _value);
}