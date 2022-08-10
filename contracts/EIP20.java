import java.math.BigDecimal;
import java.math.BigInteger;

public interface EIP20 {
    public String name();
    public String symbol();
    public int decimals();
    public double totalSupply();
    public BigDecimal balanceOf(String _owner);
    public Boolean transfer(String _from, BigDecimal _value);
    public Boolean transferFrom(String _from, String _to, BigDecimal _value);
    public Boolean approve(String _spender, BigDecimal _value);
    public BigDecimal allowance(String _owner, String _spender);
}