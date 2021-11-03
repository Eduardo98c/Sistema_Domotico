package Gestione_Sensori.Exception;

public class SensoriException extends Exception{

    String Err;
    public SensoriException(String Error){
        super("Errore Gestione Sensori: ");
        Err=Error;
    }

    @Override
    public String toString() {
        return getMessage()+ Err;
    }
}
