package Gestione_Sensori;

import PatternProxy.Client;
import PatternProxy.DB_Proxy_Singleton_Connection;
import FactoryMethod.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CaricamentoSensoriIntervento {
    private Client C;
    private ObservableList<SensoreIntervento> BufferQuerySensInterv;
    private ObservableList<SensoreIntervento> BufferQuerySensInterv_Coll;

    public ObservableList<SensoreIntervento> getBufferQuerySensInterv() {
        return BufferQuerySensInterv;
    }
    public ObservableList<SensoreIntervento> getBufferQuerySensInterv_Coll() {
        return BufferQuerySensInterv_Coll;
    }

    public CaricamentoSensoriIntervento(Client Client_import) {
        C = Client_import;
        BufferQuerySensInterv = FXCollections.observableArrayList();
        BufferQuerySensInterv_Coll=FXCollections.observableArrayList();
    }
    public void dataAllarme(SensoreIntervento I) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        String QueryDataAllarme="UPDATE Sensore_Intervento SET Data_Allarme=? WHERE Nome_Sensore_I=? AND Data_Creazione=? AND Parametro_Rif=?";
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryDataAllarme)){
            S.setString(1, String.valueOf(date));
            S.setString(2, I.getNome());
            S.setString(3, I.getDataCreazione());
            S.setString(4, I.getParametro_riferimento());
            S.executeUpdate();
            caricaSensori();
        }catch(Exception ex){
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
    public void interruttoreSensore(SensoreIntervento I, int interruttore) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryInterruttoreSensori;
        if(interruttore == 1) {
            QueryInterruttoreSensori= "UPDATE Sensore_Intervento SET Stato= 1,StringaStato='ACCESO' WHERE Nome_Sensore_I=?  AND Data_Creazione=? AND Parametro_rif=? AND Descrizione_Azione_Eseguita=? AND Comportamento_Azione=?";
        }
        else
          QueryInterruttoreSensori="UPDATE Sensore_Intervento SET Stato= 0,StringaStato='SPENTO' WHERE Nome_Sensore_I=? AND Data_Creazione=? AND Parametro_rif=? AND Descrizione_Azione_Eseguita=? AND Comportamento_Azione=?";

        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryInterruttoreSensori)) {
            S.setString(1, I.getNome());
            S.setString(2, I.getDataCreazione());
            S.setString(3, I.getParametro_riferimento());
            S.setString(4, I.getAzione_Eseguita());
            S.setString(5, I.getComportamento_Azione());
            S.executeUpdate();
            caricaSensori();

        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                JOptionPane.showMessageDialog(null, "Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else {
                JOptionPane.showMessageDialog(null, "Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    public void resetSensori() throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryResetSensori="UPDATE Sensore_Intervento SET stato=0,Data_Allarme=null;";
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryResetSensori)) {
            S.executeUpdate();
            caricaSensori();
        }catch (Exception ex) {
            if (ex instanceof SQLException) {
                JOptionPane.showMessageDialog(null, "Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else {
                JOptionPane.showMessageDialog(null, "Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    public void caricaSensori() throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensoriM = "SELECT * FROM Sensore_Intervento";
        BufferQuerySensInterv.clear();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensoriM)) {

            ResultSet Search = S.executeQuery();

            while (Search.next()) {
                Creator CreaInterv = new Creator_Sensore_Intervento(Search.getString(1), Search.getInt(2), Search.getString(4), Search.getString(5), Search.getString(6), Search.getString(7),Search.getString(8));
                SensoreIntervento Sens_Temp = (SensoreIntervento) CreaInterv.getSensore();
                BufferQuerySensInterv.add(Sens_Temp);
            }
        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                JOptionPane.showMessageDialog(null, "Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else {
                JOptionPane.showMessageDialog(null, "Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    public SensoreIntervento caricaSensoreInterv(SensoreIntervento I) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QuerySelectSensore = "SELECT * FROM Sensore_Intervento WHERE Nome_Sensore_I=? AND Data_Creazione=? AND Parametro_rif=? AND Descrizione_Azione_Eseguita=? AND Comportamento_Azione=?";
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QuerySelectSensore)) {
            S.setString(1, I.getNome());
            S.setString(2, I.getDataCreazione());
            S.setString(3, I.getParametro_riferimento());
            S.setString(4, I.getAzione_Eseguita());
            S.setString(5, I.getComportamento_Azione());
            ResultSet Search = S.executeQuery();
            while (Search.next()) {
                Creator CreaInterv = new Creator_Sensore_Intervento(Search.getString(1), Search.getInt(2), Search.getString(4), Search.getString(5), Search.getString(6), Search.getString(7), Search.getString(8));
                SensoreIntervento Sens_Temp = (SensoreIntervento) CreaInterv.getSensore();
                return Sens_Temp;
            }

        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                JOptionPane.showMessageDialog(null, "Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else {
                JOptionPane.showMessageDialog(null, "Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return null;
    }
    public void caricaSensoriCollegati(SensoreMonitoraggio M) throws SQLException{
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensoriI_Coll="SELECT * FROM Sensore_Intervento S1 WHERE EXISTS(SELECT * FROM Sensore_Monitoraggio M JOIN Attiva A on M.Nome_Sensore_M = A.Nome_Sensore_Monit JOIN Sensore_Intervento S2 ON A.Nome_Sensore_interv = S2.Nome_Sensore_I WHERE S1.Nome_Sensore_I=S2.Nome_Sensore_I AND M.Nome_Sensore_M=?)";
        BufferQuerySensInterv_Coll.clear();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensoriI_Coll)) {
            S.setString(1,M.getNome());
            ResultSet Search = S.executeQuery();

            while (Search.next()) {
                Creator CreaInterv = new Creator_Sensore_Intervento(Search.getString(1), Search.getInt(2), Search.getString(4), Search.getString(5), Search.getString(6), Search.getString(7),Search.getString(8));
                SensoreIntervento Sens_Temp = (SensoreIntervento) CreaInterv.getSensore();
                BufferQuerySensInterv_Coll.add(Sens_Temp);
            }
        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                JOptionPane.showMessageDialog(null, "Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else {
                JOptionPane.showMessageDialog(null, "Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    public void scollegaSensore(SensoreMonitoraggio M, SensoreIntervento I) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensoriM_NonColl = "DELETE FROM Attiva A WHERE A.Nome_Sensore_Monit=? AND A.Nome_Sensore_interv=?";
        BufferQuerySensInterv_Coll.clear();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensoriM_NonColl)){
            S.setString(1,M.getNome());
            S.setString(2,I.getNome());
            S.executeUpdate();
            caricaSensoriCollegati(M);
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
    public boolean inserisciSensoreI(SensoreIntervento I) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryInserimentoSensoreIntervento = "INSERT INTO Sensore_Intervento(Nome_Sensore_I, stato, StringaStato, Data_Creazione,Data_Allarme, Parametro_rif, Descrizione_Azione_Eseguita, Comportamento_Azione) VALUES(UPPER(?),?,UPPER(?),UPPER(?),UPPER(?),UPPER(?),UPPER(?),UPPER(?))";

        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryInserimentoSensoreIntervento)) {
            S.setString(1, I.getNome());
            S.setInt(2, I.getStato());
            S.setString(3, I.getStringaStato());
            S.setString(4, I.getDataCreazione());
            S.setString(5, null);
            S.setString(6,I.getParametro_riferimento());
            S.setString(7, I.getAzione_Eseguita());
            S.setString(8, I.getComportamento_Azione());
            S.executeUpdate();
            caricaSensori();
            return true;

        } catch (Exception ex) {
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                JOptionPane.showMessageDialog(null, "Nome Sensore Già Registrato");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else if (ex instanceof SQLException) {
                JOptionPane.showMessageDialog(null, "Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else {
                JOptionPane.showMessageDialog(null, "Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return false;

    }

    public void eliminaSensoreI(SensoreIntervento I) throws SQLException {
        String QueryEliminazioneSensore = "DELETE FROM Sensore_Intervento WHERE Nome_Sensore_I=? AND Data_Creazione=? AND Parametro_rif=? AND Descrizione_Azione_Eseguita=? AND Comportamento_Azione=?";
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryEliminazioneSensore)) {
            S.setString(1, I.getNome());
            S.setString(2, I.getDataCreazione());
            S.setString(3, I.getParametro_riferimento());
            S.setString(4, I.getAzione_Eseguita());
            S.setString(5, I.getComportamento_Azione());
            S.executeUpdate();
            caricaSensori();
        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                JOptionPane.showMessageDialog(null, "Errore Richiesta");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } else {
                JOptionPane.showMessageDialog(null, "Errore Interno, si prega di riprovare");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

}
