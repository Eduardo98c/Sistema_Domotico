package Casa_Domotica;


import PatternProxy.Client;
import PatternProxy.DB_Proxy_Singleton_Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//CLASSE ADIBITA ALLE FUNZIONI DI CARICAMENTO,CONTROLLO ED ELIMINAZIONE DEI PARAMETRI SUL DATABASE

public class CaricamentoVariabiliSql {

    private ObservableList<Parametri_Casa_Domotica> BufferQueryParametri;
    Client C;

    public CaricamentoVariabiliSql(Client Client_import){
        C=Client_import;
        BufferQueryParametri =FXCollections.observableArrayList();
    }

    public ObservableList<Parametri_Casa_Domotica> getBufferQueryParametri() {
        return BufferQueryParametri;
    }

    //CARICA IL PARAMETRO USATO DAL COMPORTAMENTO
    public Parametri_Casa_Domotica CaricaParametroComportamento(String Nome_Par) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCasaDomotica="SELECT * FROM Parametri_Casa WHERE Parametro =?";
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCasaDomotica)){
            S.setString(1,Nome_Par);
            ResultSet Search=S.executeQuery();

            while(Search.next()){
                Parametri_Casa_Domotica P=new Parametri_Casa_Domotica(Search.getString(1),Search.getString(2), Search.getDouble(3));
                return P;
            }

        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore in SQL");
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
            }
        }
        return null;
    }
    //METODO PER IL CARICAMENTO DEI PARAMETRI
    public void caricaParametri() throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCasaDomotica="SELECT * FROM Parametri_Casa";
        BufferQueryParametri.clear();

        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCasaDomotica)){

            ResultSet Search=S.executeQuery();        //PERMETTE ESECUZIONE QUERY

            while(Search.next()){ //CONTINUA FIN QUANDO NON HA CARICATO TUTTE LE QUERY
                Parametri_Casa_Domotica P=new Parametri_Casa_Domotica(Search.getString(1),Search.getString(2), Search.getDouble(3));
                BufferQueryParametri.add(P);
            }

        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore in SQL");
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
            }
        }
    }
    //METODO PER L'INSERIMENTO DI NUOVI PARAMETRI
    public String creaParametro(Parametri_Casa_Domotica P) throws SQLException {

        DB_Proxy_Singleton_Connection Access;
        Access = C.getDbConnection_singleton();

        if(controlloParIns(Access,P.getParametro())){
            String QueryInsert="INSERT INTO Parametri_Casa(Parametro,Unita_Di_Misura,Valore)VALUES(UPPER(?),UPPER(?),UPPER(?))";

            if(insertvar(Access,QueryInsert,P)){
                caricaParametri(); //RICARICA LA TABELLA CON IL VALORE AGGIORNATO
                return "Inserimento Effettuato Con Successo";
            }
            else{
                return "Inserimento Non Riuscito";
            }
        }
        else{
            return "Nome Parametro Già Usato";
        }

    }

    //METODO PER L'AGGIORNAMENTO DEI VALORI DEL PARAMETRO PASSATO
    public void modificaValore(Parametri_Casa_Domotica P,Double Val) throws SQLException {
        DB_Proxy_Singleton_Connection Access;
        Access = C.getDbConnection_singleton();

        String Query_Modifica="UPDATE Parametri_Casa SET Valore= ? WHERE Parametro=? AND Unita_Di_Misura = ?";

        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(Query_Modifica)){
            S.setDouble(1,Val);
            S.setString(2,P.getParametro());
            S.setString(3,P.getUnita());
            S.executeUpdate();

        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore in SQL");
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
            }
        }
        caricaParametri();
    }

    //METODO CHE CONTROLLA CHE NON VENGA INSERITO LO STESSO NOME PER UN PARAMETRO
    private boolean controlloParIns(DB_Proxy_Singleton_Connection Access, String Par) {
        String ControlPar=null;
        String QueryCasaDomotica="SELECT * FROM Parametri_Casa WHERE Parametro= ?";
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCasaDomotica)){
            S.setString(1,Par);
            ResultSet Search=S.executeQuery();        //PERMETTE ESECUZIONE QUERY

            while(Search.next()){ //CONTINUA FIN QUANDO NON HA CARICATO TUTTE LE QUERY
                ControlPar=Search.getString("Parametro");
            }
            if(ControlPar == null){
                S.close();
                return true;
            }
        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore in SQL");
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
            }
        }
        return false;
    }

    //METODO PER L'INSERIMENTO VERO E PROPRIO
    private boolean insertvar(DB_Proxy_Singleton_Connection Access, String QueryInsert, Parametri_Casa_Domotica P){

        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryInsert)){
            S.setString(1,P.getParametro());
            S.setString(2,P.getUnita());
            S.setDouble(3,P.getValore());
            S.executeUpdate();
            S.close();
            return true;

        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore in SQL");
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
            }
        }
        return false;
    }

    //METODO PER LA CANCELLAZIONE DI UN PARAMETRO
    public void cancellaVariabileMisurazione(Parametri_Casa_Domotica P) throws SQLException {
        DB_Proxy_Singleton_Connection Access;
        Access = C.getDbConnection_singleton();

        String Query_delete="DELETE FROM Parametri_Casa WHERE Parametro=? AND Unita_Di_Misura = ? AND Valore= ?";

        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(Query_delete)){
            S.setString(1,P.getParametro());
            S.setString(2,P.getUnita());
            S.setDouble(3,P.getValore());
            S.executeUpdate();

        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore in SQL");
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
            }
        }
        caricaParametri();
    }
}
