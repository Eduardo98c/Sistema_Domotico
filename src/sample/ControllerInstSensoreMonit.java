package sample;

import PatternProxy.Client;
import Casa_Domotica.CaricamentoVariabiliSql;
import Casa_Domotica.Parametri_Casa_Domotica;
import FactoryMethod.*;
import Gestione_Sensori.CaricamentoSensoriIntervento;
import Gestione_Sensori.CaricamentoSensoriMonitoraggio;
import Gestione_Sensori.Exception.SensoriException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerInstSensoreMonit implements Initializable {

    //TABELLA SENSORE MONITORAGGIO
    @FXML
    private TableView<SensoreMonitoraggio> Tabella_Monit;
    @FXML
    private TableColumn<SensoreMonitoraggio,String> colM_Nome;
    @FXML
    private TableColumn<SensoreMonitoraggio,Integer> colM_Stato;
    @FXML
    private TableColumn<SensoreMonitoraggio,String> colM_StringaStat;
    @FXML
    private TableColumn<SensoreMonitoraggio,String> colM_DataCreaz;
    @FXML
    private TableColumn<SensoreMonitoraggio,String> colM_DataAllarm;
    @FXML
    private TableColumn<SensoreMonitoraggio,String> colM_Parametro;
    @FXML
    private TableColumn<SensoreMonitoraggio,Integer> colM_ValPos;
    @FXML
    private TableColumn<SensoreMonitoraggio,Integer> colM_ValMed;
    @FXML
    private TableColumn<SensoreMonitoraggio,Integer> colM_ValCrit;

    //TABELLA SENSORE INTERVENTO
    @FXML
    private TableView<SensoreIntervento> Tabella_Interv;
    @FXML
    private TableColumn<SensoreIntervento,String> colI_Nome;
    @FXML
    private TableColumn<SensoreIntervento,Integer> colI_Stato;
    @FXML
    private TableColumn<SensoreIntervento,String> colI_StringaStat;
    @FXML
    private TableColumn<SensoreIntervento,String> colI_DataCreaz;
    @FXML
    private TableColumn<SensoreIntervento,String> colI_DataAllarm;
    @FXML
    private TableColumn<SensoreIntervento,String> colI_Parametro;
    @FXML
    private TableColumn<SensoreIntervento,String> colI_Descrizione_Azione;
    @FXML
    private TableColumn<SensoreIntervento,String> colI_Comportamento_Azione;

    //ALTRI ELEMENTI DELL'INTERFACCIA GRAFICA
    @FXML
    private TextField id_Nome_Sens;
    @FXML
    private ComboBox id_Parametro;
    @FXML
    private Spinner<Integer> Val_Pos;
    @FXML
    private Spinner<Integer> Val_Med;
    @FXML
    private Spinner<Integer> Val_Crit;
    @FXML
    private Button id_bottone_interr;
    @FXML
    private Label Err_text_SensMonit;

    private Client C;
    private ObservableList<SensoreMonitoraggio> BufferQuerySensoriM;
    private ObservableList<SensoreIntervento> BufferQuerySensoriI;
    private ObservableList<Parametri_Casa_Domotica> BufferQueryParametri;
    private ObservableList<String> Scelte_Parametri;
    private CaricamentoVariabiliSql V_sql;
    private CaricamentoSensoriMonitoraggio M_sql;
    private CaricamentoSensoriIntervento I_sql;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //INIZIALIZZAZIONE COMBOBOX E SPINNER
        id_Parametro.setItems(Scelte_Parametri);
        SpinnerValueFactory<Integer> RangeValueP=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        SpinnerValueFactory<Integer> RangeValueM=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        SpinnerValueFactory<Integer> RangeValueC=new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);

        Val_Pos.setValueFactory(RangeValueP);
        Val_Med.setValueFactory(RangeValueM);
        Val_Crit.setValueFactory(RangeValueC);

        //INIZIALIZZAZIONE CELLE TABELLA SENSORE MONITORAGGIO
        colM_Nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        colM_Stato.setCellValueFactory(new PropertyValueFactory<>("Stato"));
        colM_StringaStat.setCellValueFactory(new PropertyValueFactory<>("StringaStato"));
        colM_DataCreaz.setCellValueFactory(new PropertyValueFactory<>("DataCreazione"));
        colM_DataAllarm.setCellValueFactory(new PropertyValueFactory<>("DataAllarme"));
        colM_Parametro.setCellValueFactory(new PropertyValueFactory<>("Parametro_riferimento"));
        colM_ValPos.setCellValueFactory(new PropertyValueFactory<>("Valore_Positivo"));
        colM_ValMed.setCellValueFactory(new PropertyValueFactory<>("Valore_Medio"));
        colM_ValCrit.setCellValueFactory(new PropertyValueFactory<>("Valore_Critico"));
        Tabella_Monit.setItems(BufferQuerySensoriM);

        //INIZIALIZZAZIONE CELLE TABELLA SENSORE INTERVENTO
        colI_Nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        colI_Stato.setCellValueFactory(new PropertyValueFactory<>("Stato"));
        colI_StringaStat.setCellValueFactory(new PropertyValueFactory<>("StringaStato"));
        colI_DataCreaz.setCellValueFactory(new PropertyValueFactory<>("DataCreazione"));
        colI_DataAllarm.setCellValueFactory(new PropertyValueFactory<>("DataAllarme"));
        colI_Parametro.setCellValueFactory(new PropertyValueFactory<>("Parametro_riferimento"));
        colI_Descrizione_Azione.setCellValueFactory(new PropertyValueFactory<>("Azione_Eseguita"));
        colI_Comportamento_Azione.setCellValueFactory(new PropertyValueFactory<>("Comportamento_Azione"));
        Tabella_Interv.setItems(BufferQuerySensoriI);
    }
    public void importClient(Client Client_import) {
        C=Client_import;
        V_sql=new CaricamentoVariabiliSql(C);
        M_sql=new CaricamentoSensoriMonitoraggio(C);
        I_sql=new CaricamentoSensoriIntervento(C);

        try {
            M_sql.caricaSensori();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQuerySensoriM.setAll(M_sql.getBufferQuerySensMonit());

        try {
            V_sql.caricaParametri();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());
        setScelteParametri();
    }
    private void setScelteParametri(){
        Scelte_Parametri.clear();
        for(Parametri_Casa_Domotica Par: BufferQueryParametri){
            Scelte_Parametri.add(Par.getParametro());
        }
    }
    //CARICA I NOMI DEI PARAMETRI NELLA COMBOBOX
    public void caricaParametri(MouseEvent mouseEvent) throws SQLException {
        V_sql.caricaParametri();
        BufferQueryParametri.clear();
        BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());
        setScelteParametri();
        id_Parametro.setItems(Scelte_Parametri);
    }

    public ControllerInstSensoreMonit(){
        BufferQuerySensoriM=FXCollections.observableArrayList();
        BufferQuerySensoriI=FXCollections.observableArrayList();
        BufferQueryParametri= FXCollections.observableArrayList();
        Scelte_Parametri=FXCollections.observableArrayList();
    }

    //METODO PER LA GESTIONE DEGLI ERRORI DA INSERIMENTO
    public String controlErrM(int Pos, int Med, int Crit){
        try{
            if(Pos == Med || Crit == Med){
                throw new SensoriException("Valori Uguali");
            }
            if((Med < Crit && Med < Pos) || (Med > Crit && Med > Pos))
                throw new SensoriException("Il valore medio non è compreso tra il valore Positivo e quello Critico");
            if(Pos > 10000 || Med > 10000 || Crit > 10000){
                throw new SensoriException("Il valore Inserito supera la Soglia prevista");
            }
            if(id_Parametro.getValue() == null){
                throw new SensoriException("Selezionare un Parametro");
            }
            if( id_Nome_Sens.getText().equals("")){
                throw new SensoriException("Inserire un Nome");
            }
        }catch (Exception ex){
            if(ex instanceof SensoriException) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return ex.toString();
            }
        }
        return null;
    }
    //METODO PER LA CREAZIONE DI UN SENSORE MONITORAGGIO
    public void creaSensore() throws SQLException {
        int Pos= Val_Pos.getValue();
        int Med= Val_Med.getValue();
        int Crit=Val_Crit.getValue();

        String Err= controlErrM(Pos,Med,Crit);
        if(Err == null){
            java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
            Creator Nuovo_Sensore = new Creator_Sensore_Monitoraggio(id_Nome_Sens.getText(),1,date.toString(),null,id_Parametro.getValue().toString(),Pos,Med,Crit);

            if(M_sql.inserisciSensoreM((SensoreMonitoraggio) Nuovo_Sensore.getSensore())){
                Tabella_Monit.setItems(M_sql.getBufferQuerySensMonit());
                Err_text_SensMonit.setTextFill(Paint.valueOf("#4CC417"));
                Err_text_SensMonit.setText("Inserimento Effettuato Con Successo");
            }

        }
        else{
            Err_text_SensMonit.setTextFill(Paint.valueOf("#800517"));
            Err_text_SensMonit.setText(Err);
        }
        id_Nome_Sens.setText("");

    }
    //METODO PER L'ELIMINAZIONE DI UN SENSORE MONITORAGGIO
    public void cancellaSensore(ActionEvent actionEvent) throws SQLException {
        ObservableList<SensoreMonitoraggio> Temp_delete=FXCollections.observableArrayList();
        Temp_delete.setAll(Tabella_Monit.getSelectionModel().getSelectedItems());
        for (SensoreMonitoraggio sensoreMonitoraggio : Temp_delete) {
            M_sql.eliminaSensoreM(sensoreMonitoraggio);
            Tabella_Monit.getItems().removeAll(Tabella_Monit.getSelectionModel().getSelectedItems());
        }
        Tabella_Monit.setItems(M_sql.getBufferQuerySensMonit());
    }
    //METODO PER CARICARE I SENSORI INTERVENTO GIÀ COLLEGATI
    public void caricamentoSensInterv(MouseEvent mouseEvent) throws SQLException {
        if(Tabella_Monit.getSelectionModel().getSelectedItem() != null){
            I_sql.caricaSensoriCollegati(Tabella_Monit.getSelectionModel().getSelectedItem());
            Tabella_Interv.setItems(I_sql.getBufferQuerySensInterv_Coll());
        }
    }
    //METODO PER SCOLLEGARE UN SENSORE INTERVENTO
    public void scollegaSensoreInterv(ActionEvent actionEvent) throws SQLException {
        if(Tabella_Monit.getSelectionModel().getSelectedItem()!= null && Tabella_Interv.getSelectionModel().getSelectedItem()!=null){
            SensoreMonitoraggio TempM = Tabella_Monit.getSelectionModel().getSelectedItem();
            SensoreIntervento TempI = Tabella_Interv.getSelectionModel().getSelectedItem();
            I_sql.scollegaSensore(TempM,TempI);
            BufferQuerySensoriI.setAll(I_sql.getBufferQuerySensInterv_Coll());
        }
    }
    //METODO PER LA GESTIONE DI ACCENSIONE/SPEGNIMENTO DEL  SENSORE MONITORAGGIO
    public void interruttoreSensoreM(ActionEvent actionEvent) throws SQLException {
        if(Tabella_Monit.getSelectionModel().getSelectedItem()!=null) {
            if (Tabella_Monit.getSelectionModel().getSelectedItem().getStato() == 0) {
                M_sql.interruttoreSensore(Tabella_Monit.getSelectionModel().getSelectedItem(), 1);
            } else {
                M_sql.interruttoreSensore(Tabella_Monit.getSelectionModel().getSelectedItem(), 0);
            }
            Tabella_Monit.setItems(M_sql.getBufferQuerySensMonit());
            id_bottone_interr.setText("INTERRUTTORE");
            id_bottone_interr.setDisable(true);
        }
    }
    //METODO PER SETTARE IL BOTTONE DI ACCENSIONE/SPEGNIMENTO
    public void attivaInterruttore(MouseEvent mouseEvent) {
        if(Tabella_Monit.getSelectionModel().getSelectedItem()!=null) {
            if (Tabella_Monit.getSelectionModel().getSelectedItem().getStato() == 1) {
                id_bottone_interr.setDisable(false);
                id_bottone_interr.setText("SPEGNI");
            } else {
                id_bottone_interr.setDisable(false);
                id_bottone_interr.setText("ACCENDI");
            }
        }
    }
    //EVENTO PER CHIUSURA DELLA FINESTRA
    public void chiudiFinestra(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


}
