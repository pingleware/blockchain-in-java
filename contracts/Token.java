import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.awt.event.*;
import java.util.*;
import java.math.BigDecimal;

public class Token implements EIP20, Serializable {
    public static String _contractOwner;
    public static String _contract;
    public static String _name;
    public static String _symbol;
    public static int    _decimals;
    public static double _totalSupply;

    public String _sender;
    public double _value;

    private HashMap<String,BigDecimal> _balances;
    private HashMap<String,BigDecimal> _approvals;
    private HashMap<String,String> _allowances;

    private boolean found = false;
    private String _owner = null;
    private String _spender = null;
    private String _from = null;
    private String _to = null;
    private BigDecimal _amount;

    private List<EIP20Listener> listeners = new ArrayList<EIP20Listener>();

    public Token() {
        this._balances  = new HashMap<String, BigDecimal>();
        this._balances.put(_contract, new BigDecimal(_totalSupply));
    }

    public void addListener(EIP20Listener toAdd) {
        listeners.add(toAdd);
    }

    public void Transfer(String _from, String _to, BigDecimal _value) {
        // Send to PubSub Server
        for (EIP20Listener hl : listeners) {
            hl.Transfer(_from, _to, _value);
        }
    }
    public void Approval(String _owner, String _spender, BigDecimal _value) {
        // Send to PubSub Server
        for (EIP20Listener hl : listeners) {
            hl.Approval(_from, _to, _value);
        }
    }


    public String name() {
        return _name;
    }
    public String symbol() {
        return _symbol;
    }
    public int decimals() {
        return _decimals;
    }
    public double totalSupply() {
        return _totalSupply;
    }
    public BigDecimal balanceOf(String _owner) {
        return this._balances.get(_owner);
    }
    public Boolean transfer(String _from, BigDecimal _value) {
        if (this._balances.get(_from).doubleValue() >= _value.doubleValue()) {
            BigDecimal senderTotal = this._balances.get(_from).subtract(_value);
            BigDecimal contractTotal = this._balances.get(_contract).add(_value);
            this._balances.remove(_from);
            this._balances.remove(_contract);
            this._balances.put(_contract,contractTotal);
            this._balances.put(_from, senderTotal);
            // send Transfer event
            Transfer(_contract, _from, _value);
            return true;
        }
        return false;
    }
    /**
     * msg.sender is the initiator.  _from will always be the owner
     */
    public Boolean transferFrom(String _from, String _to, BigDecimal _value) {
        this.found = false;
        if (this._balances.get(this._sender).doubleValue() >= _value.doubleValue() && this._balances.get(_to) != null) {
            if (this._approvals.get(_from).doubleValue() >= _value.doubleValue()) {
                BigDecimal senderTotal = this._balances.get(_from).subtract(_value);
                BigDecimal receiverTotal = this._balances.get(_to).add(_value);
                this._balances.remove(_from);
                this._balances.remove(_to);
                this._balances.put(_from, senderTotal);
                this._balances.put(_to,receiverTotal);
                this.found = true;    
            } else {
                // look through _allowances to find if the owner granted the spender authority to transfer from their account
                if (this._approvals.get(_from).doubleValue() >= _value.doubleValue()) {
                    if (this._allowances.get(_from) == this._sender) {
                        // sender is authorized
                        BigDecimal senderTotal = this._balances.get(_from).subtract(_value);
                        BigDecimal receiverTotal = this._balances.get(_to).add(_value);
                        this._balances.remove(_from);
                        this._balances.remove(_to);
                        this._balances.put(_from, senderTotal);
                        this._balances.put(_to,receiverTotal);    
                        // send Transfer event
                        Transfer(_from, _to, _value);
                    }
                }
            }
        }
        return this.found;
    }

    public Boolean approve(String _spender, BigDecimal _value) {
        if (this._balances.get(_spender).doubleValue() > 0) {
            this._approvals.put(_spender,_value);
            // send approval event
            Approval(_sender, _spender, _value);
            return true;    
        }
        return false;
    }
    public BigDecimal allowance(String _owner, String _spender) {
        if (this._balances.get(_owner).doubleValue() > 0) {
            this._allowances.put(_owner,_spender);
            return this._approvals.get(_spender);
        }
        return new BigDecimal(0);
    }
}