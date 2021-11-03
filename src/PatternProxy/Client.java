package PatternProxy;

import Accesso_Account.Account;
import Accesso_Account.Account_Attivato;
import Accesso_Account.Account_Collaudo;

import java.sql.SQLException;

public class Client{

    private IClient C;
    private Account account;


    public Client(){
    }

    public DB_Proxy_Singleton_Connection getDbConnection_singleton() throws SQLException {
        this.C=new DB_Proxy_Singleton_Connection();
        return  C.getInstance();
    }
    public Account getAccount() {
        if(this.account instanceof Account_Attivato){
            return this.account;
        }
        else if(this.account instanceof Account_Collaudo){
            return this.account;
        }
        return  null;
    }

    public void setAccount(Account account) {
        this.account=account;
    }



}
