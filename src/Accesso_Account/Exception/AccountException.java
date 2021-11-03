package Accesso_Account.Exception;

public class AccountException extends Exception {

    String Err;
    public AccountException(String Error){
        super("Errore Account: ");
        Err=Error;
    }

    @Override
    public String toString() {
        return getMessage()+ Err;
    }
}
