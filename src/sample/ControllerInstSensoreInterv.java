package sample;

import PatternProxy.Client;
import Casa_Domotica.CaricamentoVariabiliSql;
import Casa_Domotica.Parametri_Casa_Domotica;
import FactoryMethod.Creator;
import FactoryMethod.Creator_Sensore_Intervento;
import FactoryMethod.SensoreIntervento;
import FactoryMethod.SensoreMonitoraggio;
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

public class ControllerInstSensoreInterv implements Initializable {
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
    
    //ALTRI ELEMENTI DELL'INTERFACCIA GRAFICA
    @FXML
    private TextField id_Nome_Sens;
    @FXML
    private ComboBox id_Parametro;
    @FXML
    private TextArea id_Descrizione_Azione;
    @FXML
    private ComboBox id_Comportamento;
    @FXML
    private Label Err_text_SensInterv;
    @FXML
    private Button id_bottone_interr;

    Client C;
    private ObservableList<SensoreMonitoraggio> BufferQuerySensoriM;
    private ObservableList<SensoreIntervento> BufferQuerySensoriI;
    private ObservableList<Parametri_Casa_Domotica> BufferQueryParametri;
    private final ObservableList<String> Scelte_Comportamenti= FXCollections.observableArrayList("INCREMENTA IL PARAMETRO","DECREMENTA IL PARAMETRO");

    private ObservableList<String> Scelte_Parametri;
    private CaricamentoVariabiliSql V_sql;
    private CaricamentoSensoriIntervento I_sql;
    private CaricamentoSensoriMonitoraggio M_sql;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //INIZIALIZZAZIONE COMBOBOX
        id_Parametro.setItems(Scelte_Parametri);
        id_Comportamento.setItems(Scelte_Comportamenti);

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

    }
    public void Import_Client(Client Client_import) {
       C=Client_import;
        V_sql=new CaricamentoVariabiliSql(C);
        I_sql=new CaricamentoSensoriIntervento(C);
        M_sql=new CaricamentoSensoriMonitoraggio(C);

        try {
            I_sql.caricaSensori();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQuerySensoriI.setAll(I_sql.getBufferQuerySensInterv());

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
    public ControllerInstSensoreInterv(){
        BufferQuerySensoriM= FXCollections.observableArrayList();
        BufferQuerySensoriI=FXCollections.observableArrayList();
        BufferQueryParametri= FXCollections.observableArrayList();
        Scelte_Parametri=FXCollections.observableArrayList();
    }

    public void creaSensore(ActionEvent actionEvent) throws SQLException {
        String Err= controlErrI();
        if(Err == null) {
            java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
            Creator Nuovo_Sensore=new Creator_Sensore_Intervento(id_Nome_Sens.getText(),0, date.toString(),null,id_Parametro.getValue().toString(),id_Descrizione_Azione.getText(),id_Comportamento.getValue().toString());
            if(I_sql.inserisciSensoreI((SensoreIntervento) Nuovo_Sensore.getSensore())){
                Tabella_Interv.setItems(I_sql.getBufferQuerySensInterv());
                Err_text_SensInterv.setTextFill(Paint.valueOf("#4CC417"));
                Err_text_SensInterv.setText("Inserimento Effettuato Con Successo");
            }
        }
        else{
            Err_text_SensInterv.setTextFill(Paint.valueOf("#800517"));
            Err_text_SensInterv.setText(Err);
        }
        id_Nome_Sens.setText("");
        id_Descrizione_Azione.setText("");

    }
    //METODO PER L'ELIMINAZIONE DI UN SENSORE INTERVENTO
    public void cancellaSensore(ActionEvent actionEvent) throws SQLException {
        ObservableList<SensoreIntervento> Temp_delete=FXCollections.observableArrayList();
        Temp_delete.setAll(Tabella_Interv.getSelectionModel().getSelectedItems());
        for(SensoreIntervento sensoreIntervento:Temp_delete){
            I_sql.eliminaSensoreI(sensoreIntervento);
            Tabella_Interv.getItems().removeAll(Tabella_Interv.getSelectionModel().getSelectedItems());
        }
    }
    //METODO PER CARICARE I SENSORI MONITORAGGIO DA COLLEGARE
    public void caricamentoSensMonit(MouseEvent mouseEvent) throws SQLException {
        if(Tabella_Interv.getSelectionModel().getSelectedItem()!= null ){
            M_sql.caricaSensoriNonCollegati(Tabella_Interv.getSelectionModel().getSelectedItem());
            Tabella_Monit.setItems(M_sql.getBufferQuerySensMonit_NonColl());
        }
    }

    //METOD0 PER COLLEGARE UN SENSORE INTERVENTO AD UN DETERMINATO SENSORE MONITORAGGIO
    public void collegasensore(ActionEvent actionEvent) throws SQLException {

        if(Tabella_Monit.getSelectionModel().getSelectedItem()!=null && Tabella_Interv.getSelectionModel().getSelectedItem()!=null) {
            SensoreMonitoraggio TempM = Tabella_Monit.getSelectionModel().getSelectedItem();
            SensoreIntervento TempI = Tabella_Interv.getSelectionModel().getSelectedItem();
            M_sql.collegaSensore(TempM, TempI);
            BufferQuerySensoriM.setAll(M_sql.getBufferQuerySensMonit_NonColl());
        }
    }

    //METODO PER LA GESTIONE DEGLI ERRORI DA INSERIMENTO
    private String controlErrI(){
        try{
           if(id_Nome_Sens.getText().equals("")){
               throw new SensoriException("Inserire un Nome");
           }
           if(id_Parametro.getValue() == null){
                throw new SensoriException("Selezionare un Parametro");
           }
           if(id_Comportamento.getValue() == null){
               throw new SensoriException("Selezionare un Comportamento");
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

    //METODO PER LA GESTIONE DI ACCENSIONE/SPEGNIMENTO DEL  SENSORE INTERVENTO
    public void interruttoreSensoreI(ActionEvent actionEvent) throws SQLException {
        if(Tabella_Interv.getSelectionModel().getSelectedItem()!=null) {
            if(Tabella_Interv.getSelectionModel().getSelectedItem().getStato() ==1) {
                I_sql.interruttoreSensore(Tabella_Interv.getSelectionModel().getSelectedItem(),0);
            }
            else{
                I_sql.interruttoreSensore(Tabella_Interv.getSelectionModel().getSelectedItem(),1);
            }
            Tabella_Interv.setItems(I_sql.getBufferQuerySensInterv());
            id_bottone_interr.setText("INTERRUTTORE");
            id_bottone_interr.setDisable(true);
        }
    }
    //METODO PER SETTARE IL BOTTONE DI ACCENSIONE/SPEGNIMENTO
    public void attivaInterruttore(MouseEvent mouseEvent) {
        if(Tabella_Interv.getSelectionModel().getSelectedItem()!=null) {
            if (Tabella_Interv.getSelectionModel().getSelectedItem().getStato() == 1) {
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
