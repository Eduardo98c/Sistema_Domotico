package Casa_Domotica;

import PatternProxy.Client;
import PatternProxy.DB_Proxy_Singleton_Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CaricamentoComportamentiSql {
    private Client C;
    private ObservableList<Comportamento_Oggetti> BufferQueryComportamenti;

    public CaricamentoComportamentiSql(Client Client_import){
        C=Client_import;
        BufferQueryComportamenti= FXCollections.observableArrayList();
    }

    public ObservableList<Comportamento_Oggetti> getBufferQueryComportamenti() {
        return BufferQueryComportamenti;
    }

    public boolean inserisciComportamento(Comportamento_Oggetti Comp, Oggetti_Casa_Domotica O) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryInserimentoComportamento="INSERT INTO Comportamento_Oggetto(Seriale, Parametro_Oggetto, Comportamento, Valore) VALUES (UPPER(?),UPPER(?),UPPER(?),?) ";
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryInserimentoComportamento)){
            S.setString(1, Comp.getSeriale_OggettoRif());
            S.setString(2, Comp.getParametroComport());
            S.setString(3, Comp.getTipo_Comportamento());
            S.setDouble(4,Comp.getValore_consumo());
            S.executeUpdate();
            caricaComportamenti(O);
            return true;
        }
        catch(Exception ex){
            if(ex instanceof SQLIntegrityConstraintViolationException){
                JOptionPane.showMessageDialog(null,"Nome Parametro Già Registrato Per Questo Oggetto");
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
    public void eliminaComportamento(Comportamento_Oggetti Comp, Oggetti_Casa_Domotica O) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryEliminazioneComportamento="DELETE FROM Comportamento_Oggetto WHERE Seriale=? AND Parametro_Oggetto=? AND Comportamento=? AND Valore=?";
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryEliminazioneComportamento)){
            S.setString(1,Comp.getSeriale_OggettoRif());
            S.setString(2,Comp.getParametroComport());
            S.setString(3,Comp.getTipo_Comportamento());
            S.setDouble(4,Comp.getValore_consumo());
            S.executeUpdate();
            caricaComportamenti(O);

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
    }

    public void caricaComportamenti(Oggetti_Casa_Domotica O) throws SQLException {
        DB_Proxy_Singleton_Connection Access=C.getDbConnection_singleton();
        String QueryCaricaComportamenti = "SELECT * FROM Comportamento_Oggetto C1 WHERE EXISTS(SELECT * FROM Oggetti_Casa O JOIN Comportamento_Oggetto C2 on O.Seriale = C2.Seriale WHERE C1.Seriale=C2.Seriale AND O.Seriale=?)";
        BufferQueryComportamenti.clear();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCaricaComportamenti)){
            S.setString(1,O.getSeriale());
            ResultSet Search=S.executeQuery();
            while(Search.next()){
                Comportamento_Oggetti C_temp=new Comportamento_Oggetti(Search.getString(1),Search.getString(2),Search.getString(3),Search.getDouble(4));
                BufferQueryComportamenti.add(C_temp);
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
}
