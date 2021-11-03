package Accesso_Account.Exception;

//CLASSE CHE VA AD EFFETTUARE I VARI CONTROLLI SULLE VARIABILI DI INPUT E GESTISCE LE ECCEZIONI RITORNANDO IL MESSAGGIO DI ERRORE OPPORTUNO
public class Prototype_AccountError {


    public Prototype_AccountError(){}


    public String errorName(String Nome){
        try{
            if(Nome.isEmpty()){
                throw new AccountException("Stringa Vuota");
            }
            else if(Nome.length() > 16){
                throw new AccountException("Lunghezza massima della stringa sforata");
            }
            else if(Nome.length() < 3){
                throw new AccountException("Nome minimo di 3 caratteri");
            }
        }
        catch(Exception ex) {
            if (ex instanceof AccountException) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return ex.toString();
            }
        }
        return null;
    }

    public String errorLastname(String Cognome){
        try{
            if(Cognome.isEmpty()){
                throw new AccountException("Stringa Vuota");
            }
            else if(Cognome.length() > 16){
                throw new AccountException("Lunghezza massima della stringa sforata");
            }
            else if(Cognome.length() < 3){
                throw new AccountException("Cognome minimo di 3 caratteri");
            }
        }
        catch(Exception ex){
            if(ex instanceof AccountException) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return ex.toString();
            }
        }
        return null;
    }

    public String errorEmail(String Email){
        try{
            if(Email.isEmpty()){
                throw new AccountException("Stringa Vuota");
            }
            else if(Email.length() > 30){
                throw new AccountException("Lunghezza massima della stringa sforata");
            }
            else if(!Email.contains("@")){
                throw new AccountException("Email non valida");
            }
            else if(Email.indexOf("@.com") > 0 || Email.indexOf("@.it") > 0){
                throw new AccountException("Email non valida");
            }
            else if(!Email.endsWith(".it") && !Email.endsWith(".com")){
                throw new AccountException("Email non valida");
            }
        }
        catch(Exception ex){
            if(ex instanceof AccountException) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return ex.toString();
            }
        }
        return null;
    }
    public String errorUser(String User){
        try{
            if(User.isEmpty()){
                throw new AccountException("Stringa Vuota");
            }
            else if(User.length() > 16){
                throw new AccountException("Lunghezza massima della stringa sforata");
            }
            else if(User.length() < 6){
                throw new AccountException("Username Minimo di 6 caratteri");
            }
        }
        catch(Exception ex){
            if(ex instanceof AccountException) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return ex.toString();
            }
        }
        return null;
    }

    public String errorPass(String Pass){
        try{
            if(Pass.isEmpty()){
                throw new AccountException("Stringa Vuota");
            }
            else if(Pass.length() > 16){
                throw new AccountException("Lunghezza massima della stringa sforata");
            }
            else if(Pass.length() < 8){
                throw new AccountException("Password minimo di 8 caratteri");
            }
        }
        catch(Exception ex){
            if(ex instanceof AccountException) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return ex.toString();
            }
        }
        return null;
    }
}
