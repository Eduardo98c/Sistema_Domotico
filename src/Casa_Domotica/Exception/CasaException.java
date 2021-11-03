package Casa_Domotica.Exception;

public class CasaException extends Exception{
    String Err;
    public CasaException(String Error){
        super("Errore Casa Domotica : ");
        Err=Error;
    }

    @Override
    public String toString() {
        return getMessage()+ Err;
    }
}
