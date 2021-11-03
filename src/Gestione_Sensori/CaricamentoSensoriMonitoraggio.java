package Gestione_Sensori;

import PatternProxy.Client;
import PatternProxy.DB_Proxy_Singleton_Connection;
import Casa_Domotica.Parametri_Casa_Domotica;
import FactoryMethod.Creator;
import FactoryMethod.Creator_Sensore_Monitoraggio;
import FactoryMethod.SensoreIntervento;
import FactoryMethod.SensoreMonitoraggio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.*;

public class CaricamentoSensoriMonitoraggio {
    private Client C;
    //LISTA PER IL CARICAMENTO DEI SENSORI MONITORAGGIO
    private ObservableList<SensoreMonitoraggio> BufferQuerySensMonit;

    //LISTA PER IL CARICAMENTO DEI SENSORI MONITORAGGIO SCOLLEGATI DA UN SENSORE INTERVENTO
    private ObservableList<SensoreMonitoraggio> BufferQuerySensMonit_NonColl;

    //LISTA PER IL CARICAMENTO DEI SENSORI MONITORAGGIO CHE GESTISCONO UN DETERMINATO PARAMETRO
    private ObservableList<SensoreMonitoraggio> BufferQuerySensMonitPar;


    //GETTER CARICAMENTI
    public ObservableList<SensoreMonitoraggio> getBufferQuerySensMonit() {
        return BufferQuerySensMonit;
    }

    public ObservableList<SensoreMonitoraggio> getBufferQuerySensMonit_NonColl() {
        return BufferQuerySensMonit_NonColl;
    }
    public ObservableList<SensoreMonitoraggio> getBufferQuerySensMonitPar() {
        return BufferQuerySensMonitPar;
    }

    public CaricamentoSensoriMonitoraggio(Client Client_import) {
        C = Client_import;
        BufferQuerySensMonit = FXCollections.observableArrayList();
        BufferQuerySensMonit_NonColl=FXCollections.observableArrayList();
        BufferQuerySensMonitPar=FXCollections.observableArrayList();
    }

    //SETTA LA DATA DELL'ALLARME PIÙ RECENTE
    public void DataAllarme(SensoreMonitoraggio M) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        String QueryDataAllarme="UPDATE Sensore_Monitoraggio SET Data_Allarme=? WHERE Nome_Sensore_M=? AND Data_Creazione=? AND Parametro_Rif=? ";
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryDataAllarme)){
            S.setString(1, String.valueOf(date));
            S.setString(2, M.getNome());
            S.setString(3, M.getDataCreazione());
            S.setString(4, M.getParametro_riferimento());
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
    //INTERRUTTORE PER GESTIRE L'ACCENSIONE O SPEGNIMENTO DEL SENSORE
    public void interruttoreSensore(SensoreMonitoraggio M,int interruttore) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryInterruttoreSensori;
        if(interruttore == 1){
            QueryInterruttoreSensori="UPDATE Sensore_Monitoraggio SET Stato =1,StringaStato='ACCESO' WHERE Nome_Sensore_M=? AND Data_Creazione=? AND Parametro_Rif=? AND Valore_Pos=? AND Valore_Med=? AND Valore_Crit=?";
        }
        else{
            QueryInterruttoreSensori="UPDATE Sensore_Monitoraggio SET Stato =0,StringaStato='SPENTO' WHERE Nome_Sensore_M=? AND Data_Creazione=? AND Parametro_Rif=? AND Valore_Pos=? AND Valore_Med=? AND Valore_Crit=?";
        }
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryInterruttoreSensori)){
            S.setString(1, M.getNome());
            S.setString(2, M.getDataCreazione());
            S.setString(3, M.getParametro_riferimento());
            S.setInt(4, M.getValore_Positivo());
            S.setInt(5, M.getValore_Medio());
            S.setInt(6, M.getValore_Critico());
            S.executeUpdate();
            caricaSensori();
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
    //METODO PER IL RESET DEI SENSORI MONITORAGGIO
    public void resetSensori() throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryResetSensori="UPDATE Sensore_Monitoraggio SET stato=1,Data_Allarme=null;";
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
    //CARICA TUTTI I SENSORI MONITORAGGIO DEL PARAMETRO SELEZIONATO
    public void CaricaSensoriParametro(Parametri_Casa_Domotica P) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensoriM = "SELECT * FROM Sensore_Monitoraggio WHERE Parametro_Rif=?";
        BufferQuerySensMonitPar.clear();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensoriM)){
            S.setString(1,P.getParametro());
            ResultSet Search=S.executeQuery();

            while(Search.next()){
                Creator CreaMonit= new Creator_Sensore_Monitoraggio(Search.getString(1),Search.getInt(2),Search.getString(4),Search.getString(5),Search.getString(6),Search.getInt(7),Search.getInt(8),Search.getInt(9));
                SensoreMonitoraggio Sens_Temp= (SensoreMonitoraggio) CreaMonit.getSensore();
                BufferQuerySensMonitPar.add(Sens_Temp);
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
    //METODO PER CARICARE TUTTI I SENSORI
    public void caricaSensori() throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensoriM = "SELECT * FROM Sensore_Monitoraggio";
        BufferQuerySensMonit.clear();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensoriM)){

            ResultSet Search=S.executeQuery();

            while(Search.next()){
                Creator CreaMonit= new Creator_Sensore_Monitoraggio(Search.getString(1),Search.getInt(2),Search.getString(4),Search.getString(5),Search.getString(6),Search.getInt(7),Search.getInt(8),Search.getInt(9));
                SensoreMonitoraggio Sens_Temp= (SensoreMonitoraggio) CreaMonit.getSensore();
                BufferQuerySensMonit.add(Sens_Temp);
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
    //METODO PER CARICARE TUTTI I SENSORI MONITORAGGIO NON COLLEGATI AD UN DETERMINATO SENSORE INTERVENTO
    public void caricaSensoriNonCollegati(SensoreIntervento I) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensoriM_NonColl = "SELECT * FROM Sensore_Monitoraggio M1 WHERE M1.Parametro_Rif=? AND NOT EXISTS (SELECT * FROM Sensore_Monitoraggio M2 JOIN Attiva A on M2.Nome_Sensore_M = A.Nome_Sensore_Monit WHERE M1.Nome_Sensore_M=M2.Nome_Sensore_M AND A.Nome_Sensore_interv=?)";
        BufferQuerySensMonit_NonColl.clear();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensoriM_NonColl)){
            S.setString(1,I.getParametro_riferimento());
            S.setString(2,I.getNome());
            ResultSet Search=S.executeQuery();

            while(Search.next()){
                Creator CreaMonit= new Creator_Sensore_Monitoraggio(Search.getString(1),Search.getInt(2),Search.getString(4),Search.getString(5),Search.getString(6),Search.getInt(7),Search.getInt(8),Search.getInt(9));
                SensoreMonitoraggio Sens_Temp= (SensoreMonitoraggio) CreaMonit.getSensore();
                BufferQuerySensMonit_NonColl.add(Sens_Temp);
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
    //METODO PER COLLEGARE UN SENSORE MONITORAGGIO AD UN SENSORE INTERVENTO
    public void collegaSensore(SensoreMonitoraggio M, SensoreIntervento I) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryCaricaSensoriM_NonColl = "INSERT INTO Attiva(nome_sensore_monit, nome_sensore_interv) VALUES(UPPER(?),UPPER(?))";
        BufferQuerySensMonit_NonColl.clear();
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryCaricaSensoriM_NonColl)){
            S.setString(1,M.getNome());
            S.setString(2,I.getNome());
            S.executeUpdate();
            caricaSensoriNonCollegati(I);
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
    //METODO PER LA CREAZIONE DI UN NUOVO SENSORE MONITORAGGIO
    public boolean inserisciSensoreM(SensoreMonitoraggio M) throws SQLException {
        DB_Proxy_Singleton_Connection Access = C.getDbConnection_singleton();
        String QueryInserimentoSensoreMonitoraggio = "INSERT INTO Sensore_Monitoraggio(Nome_Sensore_M, Stato, StringaStato, Data_Creazione, Data_Allarme, Parametro_Rif, Valore_Pos, Valore_Med, Valore_Crit) VALUES(UPPER(?),?,UPPER(?),UPPER(?),UPPER(?),UPPER(?),?,?,?)";

        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryInserimentoSensoreMonitoraggio)) {
            S.setString(1, M.getNome());
            S.setInt(2, M.getStato());
            S.setString(3, M.getStringaStato());
            S.setString(4, M.getDataCreazione());
            S.setString(5, null);
            S.setString(6, M.getParametro_riferimento());
            S.setInt(7, M.getValore_Positivo());
            S.setInt(8, M.getValore_Medio());
            S.setInt(9, M.getValore_Critico());
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
    //METODO PER L'ELIMINAZIONE DI UN NUOVO SENSORE MONITORAGGIO
    public void eliminaSensoreM(SensoreMonitoraggio M) throws SQLException {
        String QueryEliminazioneSensore="DELETE FROM Sensore_Monitoraggio WHERE Nome_Sensore_M=? AND Data_Creazione=? AND Parametro_Rif=? AND Valore_Pos=? AND Valore_Med=? AND Valore_Crit=?";
        DB_Proxy_Singleton_Connection Access=C.getDbConnection_singleton();

        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryEliminazioneSensore)){
            S.setString(1, M.getNome());
            S.setString(2, M.getDataCreazione());
            S.setString(3, M.getParametro_riferimento());
            S.setInt(4, M.getValore_Positivo());
            S.setInt(5, M.getValore_Medio());
            S.setInt(6, M.getValore_Critico());
            S.executeUpdate();
            caricaSensori();
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

}



