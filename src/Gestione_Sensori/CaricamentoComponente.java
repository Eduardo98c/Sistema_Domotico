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

public class CaricamentoComponente {
    Client C;
    private ObservableList<Componente> BufferQueryComponente;
    private ObservableList<Componente> BufferQueryComponenteM;
    private ObservableList<Componente> BufferQueryComponenteI;
    private ObservableList<SensoreMonitoraggio> BufferQuerySensoreM;
    private ObservableList<SensoreIntervento> BufferQuerySensoreI;

    public ObservableList<Componente> getBufferQueryComponente() {
        return BufferQueryComponente;
    }

    public ObservableList<Componente> getBufferQueryComponenteM() {
        return BufferQueryComponenteM;
    }

    public ObservableList<Componente> getBufferQueryComponenteI() {
        return BufferQueryComponenteI;
    }

    public ObservableList<SensoreMonitoraggio> getBufferQuerySensoreM() {
        return BufferQuerySensoreM;
    }

    public ObservableList<SensoreIntervento> getBufferQuerySensoreI() {
        return BufferQuerySensoreI;
    }

    public CaricamentoComponente(Client Client_import){
        C=Client_import;
        BufferQueryComponente= FXCollections.observableArrayList();
        BufferQueryComponenteM=FXCollections.observableArrayList();
        BufferQueryComponenteI=FXCollections.observableArrayList();

        BufferQuerySensoreM=FXCollections.observableArrayList();
        BufferQuerySensoreI=FXCollections.observableArrayList();
    }

    //CARICA TUTTE LE COMPONENTI
    public void caricaComponenti() throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaComponenti="SELECT * FROM Componente";
        BufferQueryComponente.clear();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCaricaComponenti)) {

            ResultSet Search = S.executeQuery();

            while (Search.next()) {
                Componente Comp_temp= new Componente(Search.getString(1),Search.getString(2),Search.getString(3));
                BufferQueryComponente.add(Comp_temp);
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

    //CARICA LE COMPONENTI INSTALLATE NEI SENSORI MONITORAGGIO
    public void caricaComponentiM(SensoreMonitoraggio M, Boolean Collegamento) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaComponenti="";
        if(Collegamento){
            QueryCaricaComponenti="SELECT * FROM Componente WHERE EXISTS(SELECT * FROM Sensore_Monitoraggio M JOIN Ha_CompM H ON M.Nome_Sensore_M = H.Nome_Sensore_M JOIN Componente C ON H.Nome_Componente_M = C.Nome_Componente WHERE M.Nome_Sensore_M=?)";
        }else{
            QueryCaricaComponenti="SELECT * FROM Componente WHERE NOT EXISTS(SELECT * FROM Sensore_Monitoraggio M JOIN Ha_CompM H ON M.Nome_Sensore_M = H.Nome_Sensore_M JOIN Componente C ON H.Nome_Componente_M = C.Nome_Componente WHERE M.Nome_Sensore_M=?)";
        }

        BufferQueryComponenteM.clear();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCaricaComponenti)) {
            S.setString(1,M.getNome());
            ResultSet Search = S.executeQuery();

            while (Search.next()) {
                Componente Comp_temp= new Componente(Search.getString(1),Search.getString(2),Search.getString(3));
                BufferQueryComponenteM.add(Comp_temp);
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

    //CARICA LE COMPONENTI INSTALLATE NEI SENSORI INTERVENTO
    public void caricaComponentiI(SensoreIntervento I,Boolean Collegamento) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaComponenti;
        if(Collegamento){
            QueryCaricaComponenti="SELECT * FROM Componente WHERE EXISTS(SELECT * FROM Sensore_Intervento I JOIN Ha_CompI H on I.Nome_Sensore_I = H.Nome_Sensore_I JOIN Componente C on C.Nome_Componente = H.Nome_Componente_I WHERE I.Nome_Sensore_I=?)";
        }else{
            QueryCaricaComponenti="SELECT * FROM Componente WHERE NOT EXISTS(SELECT * FROM Sensore_Intervento I JOIN Ha_CompI H on I.Nome_Sensore_I = H.Nome_Sensore_I JOIN Componente C on C.Nome_Componente = H.Nome_Componente_I WHERE I.Nome_Sensore_I=?)";

        }

        BufferQueryComponenteI.clear();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCaricaComponenti)) {
            S.setString(1,I.getNome());
            ResultSet Search = S.executeQuery();

            while (Search.next()) {
                Componente Comp_temp= new Componente(Search.getString(1),Search.getString(2),Search.getString(3));
                BufferQueryComponenteI.add(Comp_temp);
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
    //CARICA I SENSORI MONITORAGGIO COLLEGATI O NON COLLEGATI AL COMPONENTE
    public void caricaSensoriM(Componente Comp,Boolean Collegamento) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensori;
        if(Collegamento){
            QueryCaricaSensori="SELECT * FROM Sensore_Monitoraggio M1 WHERE EXISTS(SELECT * FROM Sensore_Monitoraggio M2 JOIN Ha_CompM H ON M1.Nome_Sensore_M = H.Nome_Sensore_M JOIN Componente C ON H.Nome_Componente_M = C.Nome_Componente WHERE M1.Nome_Sensore_M=M2.Nome_Sensore_M AND C.Nome_Componente=?)";
        }
        else
            QueryCaricaSensori="SELECT * FROM Sensore_Monitoraggio M1 WHERE NOT EXISTS(SELECT * FROM Sensore_Monitoraggio M2 JOIN Ha_CompM H ON M1.Nome_Sensore_M = H.Nome_Sensore_M JOIN Componente C ON H.Nome_Componente_M = C.Nome_Componente WHERE M1.Nome_Sensore_M=M2.Nome_Sensore_M AND  C.Nome_Componente=?)";

        BufferQuerySensoreM.clear();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensori)) {
            S.setString(1,Comp.getNome_Componente());
            ResultSet Search = S.executeQuery();

            while (Search.next()) {
                Creator CreaMonit= new Creator_Sensore_Monitoraggio(Search.getString(1),Search.getInt(2),Search.getString(4),Search.getString(5),Search.getString(6),Search.getInt(7),Search.getInt(8),Search.getInt(9));
                SensoreMonitoraggio Sens_Temp= (SensoreMonitoraggio) CreaMonit.getSensore();
                BufferQuerySensoreM.add(Sens_Temp);
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
    //CARICA I SENSORI INTERVENTO COLLEGATI O NON COLLEGATI AL COMPONENTE
    public void caricaSensoriI(Componente Comp,Boolean Collegamento) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensori;
        if(Collegamento){
            QueryCaricaSensori="SELECT * FROM Sensore_Intervento I1 WHERE EXISTS(SELECT * FROM Sensore_Intervento I2 JOIN Ha_CompI H on I2.Nome_Sensore_I = H.Nome_Sensore_I JOIN Componente C ON H.Nome_Componente_I = C.Nome_Componente WHERE I1.Nome_Sensore_I=I2.Nome_Sensore_I AND C.Nome_Componente=?)";
        }
        else{
            QueryCaricaSensori="SELECT * FROM Sensore_Intervento I1 WHERE NOT EXISTS(SELECT * FROM Sensore_Intervento I2 JOIN Ha_CompI H on I2.Nome_Sensore_I = H.Nome_Sensore_I JOIN Componente C ON H.Nome_Componente_I = C.Nome_Componente WHERE I1.Nome_Sensore_I=I2.Nome_Sensore_I AND C.Nome_Componente=?)";
        }
        BufferQuerySensoreI.clear();
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensori)) {
            S.setString(1,Comp.getNome_Componente());
            ResultSet Search = S.executeQuery();

            while (Search.next()) {
                Creator CreaInterv = new Creator_Sensore_Intervento(Search.getString(1), Search.getInt(2), Search.getString(4), Search.getString(5), Search.getString(6), Search.getString(7),Search.getString(8));
                SensoreIntervento Sens_Temp = (SensoreIntervento) CreaInterv.getSensore();
                BufferQuerySensoreI.add(Sens_Temp);
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
    //METODO PER IL COLLEGAMENTO DELLE COMPONENTI AI SENSORI
    public void collegaComponenti(String Nome_Sensore, Componente Comp,Boolean Type) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCollegamentoComponenti="";
        String Nome_Sens=Nome_Sensore;
        if(Type){
            QueryCollegamentoComponenti="INSERT INTO Ha_CompM(Nome_Sensore_M, Nome_Componente_M) VALUES(UPPER(?),UPPER(?))";
        }
        else{
            QueryCollegamentoComponenti="INSERT INTO Ha_CompI(Nome_Sensore_I, Nome_Componente_I) VALUES(UPPER(?),UPPER(?))";

        }
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCollegamentoComponenti)) {
            S.setString(1,Nome_Sens);
            S.setString(2,Comp.getNome_Componente());
            S.executeUpdate();
            caricaComponenti();
        }catch (Exception ex) {
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                JOptionPane.showMessageDialog(null, "Collegamento Già Esistente");
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
    }
    //METODO PER LO SCOLLEGAMENTO DEI COMPONENTI DAI SENSORI
    public void scollegaComponenti(String Nome_Sensore,Componente Comp,Boolean Type) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCollegamentoComponenti="";
        String Nome_Sens=Nome_Sensore;
        if(Type){
            QueryCollegamentoComponenti="DELETE FROM Ha_CompM WHERE Nome_Sensore_M=? AND Nome_Componente_M=?";
        }
        else{
            QueryCollegamentoComponenti="DELETE FROM Ha_CompI WHERE Nome_Sensore_I=? AND Nome_Componente_I=?";
        }
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryCollegamentoComponenti)) {
            S.setString(1,Nome_Sens);
            S.setString(2,Comp.getNome_Componente());
            S.executeUpdate();
            caricaComponenti();
        }catch (Exception ex) {
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                JOptionPane.showMessageDialog(null, "Collegamento Già Esistente");
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
    }

    //INSERISCE UN NUOVO COMPONENTE
    public boolean inserisciComponente(Componente Comp) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryInserimentoComponente="INSERT INTO Componente(Nome_Componente, Data_Creazione, Descrizione_Azione) VALUES(UPPER(?),UPPER(?),UPPER(?))";
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryInserimentoComponente)) {
            S.setString(1,Comp.getNome_Componente());
            S.setString(2,Comp.getDataCreazione());
            S.setString(3,Comp.getDescrizione_Azione());
            S.executeUpdate();
            caricaComponenti();
            return true;
        }catch (Exception ex) {
            if (ex instanceof SQLIntegrityConstraintViolationException) {
                JOptionPane.showMessageDialog(null, "Nome Componente Già Registrato");
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
    public void Elimina_Componente(Componente Comp) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryEliminazioneComponente="DELETE FROM Componente WHERE Nome_Componente = ? AND Data_Creazione=? AND Descrizione_Azione=?";
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryEliminazioneComponente)) {
            S.setString(1,Comp.getNome_Componente());
            S.setString(2,Comp.getDataCreazione());
            S.setString(3,Comp.getDescrizione_Azione());
            S.executeUpdate();
            caricaComponenti();

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


}
