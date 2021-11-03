package Casa_Domotica;

import PatternProxy.Client;
import PatternProxy.DB_Proxy_Singleton_Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.*;
import java.sql.*;

public class CaricamentoOggettiSql {

    private Client C;
    private ObservableList<Oggetti_Casa_Domotica> BufferQueryOggetti;

    public CaricamentoOggettiSql(Client Client_import){
        C=Client_import;
        BufferQueryOggetti=FXCollections.observableArrayList();
    }

    public ObservableList<Oggetti_Casa_Domotica> getBufferQueryOggetti() {
        return BufferQueryOggetti;
    }

    public void interruttoreOggetti(Oggetti_Casa_Domotica O, int interruttore) throws SQLException {
        DB_Proxy_Singleton_Connection Access=C.getDbConnection_singleton();
        String QueryInterruttoreOggetto;
        if(interruttore == 1) {
            QueryInterruttoreOggetto = "UPDATE Oggetti_Casa SET Stato=1,StringaStato='ACCESO' WHERE Seriale=? AND Nome_Oggetto=? AND Marca_Oggetto=?";
        }
        else{
            QueryInterruttoreOggetto="UPDATE Oggetti_Casa SET Stato=0,StringaStato='SPENTO' WHERE Seriale=? AND Nome_Oggetto=? AND Marca_Oggetto=?";
        }

        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryInterruttoreOggetto)){
            S.setString(1,O.getSeriale());
            S.setString(2,O.getNome());
            S.setString(3,O.getMarca());
            S.executeUpdate();

        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        caricaOggetti();
    }
    public void caricaOggetti() throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaOggetti = "SELECT * FROM Oggetti_Casa";
        BufferQueryOggetti.clear();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCaricaOggetti)){

            ResultSet Search=S.executeQuery();

            while(Search.next()){
                Oggetti_Casa_Domotica Ogg_Temp=new Oggetti_Casa_Domotica(Search.getString(1),Search.getString(2),Search.getString(3),Integer.parseInt(Search.getString(4)));
                BufferQueryOggetti.add(Ogg_Temp);
            }
        }
        catch (Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    public boolean inserisciOggetto(Oggetti_Casa_Domotica O) throws SQLException {
        String QueryInserimentoOggetto="INSERT INTO Oggetti_Casa(Seriale,Nome_Oggetto,Marca_Oggetto,Stato,StringaStato)VALUES(UPPER(?),UPPER(?),UPPER(?),?,UPPER(?))";
        DB_Proxy_Singleton_Connection Access=C.getDbConnection_singleton();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryInserimentoOggetto)){
            S.setString(1, O.getSeriale());
            S.setString(2, O.getNome());
            S.setString(3, O.getMarca());
            S.setInt(4, O.getStato());
            S.setString(5,O.getStringaStato());
            S.executeUpdate();
            caricaOggetti();
            return true;
        }
        catch(Exception ex){
            if(ex instanceof  SQLIntegrityConstraintViolationException){
                JOptionPane.showMessageDialog(null,"Nome Già Registrato");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            else if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return false;

    }
    public void eliminaOggetto(Oggetti_Casa_Domotica O) throws SQLException {
        String QueryEliminazioneOggetto="DELETE FROM Oggetti_Casa WHERE Seriale=? AND Nome_Oggetto=? AND Marca_Oggetto=?";
        DB_Proxy_Singleton_Connection Access=C.getDbConnection_singleton();

        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryEliminazioneOggetto)){
            S.setString(1,O.getSeriale());
            S.setString(2,O.getNome());
            S.setString(3,O.getMarca());
            S.executeUpdate();

        }
        catch(Exception ex){
            if(ex instanceof SQLException){
                JOptionPane.showMessageDialog(null,"Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            else{
                JOptionPane.showMessageDialog(null,"Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        caricaOggetti();
    }


}
