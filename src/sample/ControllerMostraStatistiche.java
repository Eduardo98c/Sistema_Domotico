package sample;

import PatternProxy.Client;
import Casa_Domotica.CaricamentoOggettiSql;
import Casa_Domotica.CaricamentoVariabiliSql;
import Casa_Domotica.Oggetti_Casa_Domotica;
import Casa_Domotica.Parametri_Casa_Domotica;
import FactoryMethod.SensoreIntervento;
import FactoryMethod.SensoreMonitoraggio;
import Gestione_Sensori.CaricamentoComponente;
import Gestione_Sensori.CaricamentoSensoriIntervento;
import Gestione_Sensori.CaricamentoSensoriMonitoraggio;
import Gestione_Sensori.Componente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerMostraStatistiche implements Initializable {
    //TABELLA PARAMETRI
    @FXML
    TableView Tabella_Parametri;
    @FXML
    TableColumn<Parametri_Casa_Domotica, String> Parametro_col;
    @FXML
    TableColumn<Parametri_Casa_Domotica, String> Unita_col;
    @FXML
    TableColumn<Parametri_Casa_Domotica, Double> Valore_col;
    //TABELLA OGGETTI
    @FXML
    TableView<Oggetti_Casa_Domotica> Tabella_Oggetti;
    @FXML
    TableColumn<Oggetti_Casa_Domotica, String> SerialeOggetto_col;
    @FXML
    TableColumn<Oggetti_Casa_Domotica, String> NomeOggetto_col;
    @FXML
    TableColumn<Oggetti_Casa_Domotica, String> MarcaOggetto_col;
    @FXML
    TableColumn<Oggetti_Casa_Domotica, Integer> StatoOggetto_col;
    @FXML
    TableColumn<Oggetti_Casa_Domotica,String> StringaStato_col;
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

    //TABELLA COMPONENTI
    @FXML
    private TableView<Componente> Tabella_Componente;
    @FXML
    private TableColumn<Componente, String> colC_Nome;
    @FXML
    private TableColumn<Componente, String> colC_Data_Creazione;
    @FXML
    private TableColumn<Componente, String> colC_Descrizione_Azione;

    private Client C;

    private ObservableList<Parametri_Casa_Domotica> BufferQueryParametri;
    private ObservableList<Oggetti_Casa_Domotica> BufferQueryOggetti;
    private ObservableList<SensoreMonitoraggio> BufferQuerySensoriM;
    private ObservableList<SensoreIntervento> BufferQuerySensoriI;
    private ObservableList<Componente> BufferQueryComponenti;

    private CaricamentoVariabiliSql V_sql;
    private CaricamentoOggettiSql O_sql;
    private CaricamentoSensoriMonitoraggio M_sql;
    private CaricamentoSensoriIntervento I_sql;
    private CaricamentoComponente C_sql;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //INIZIALIZZAZIONE CELLE TABELLA PARAMETRI
        Parametro_col.setCellValueFactory(new PropertyValueFactory<>("parametro"));
        Unita_col.setCellValueFactory(new PropertyValueFactory<>("unita"));
        Valore_col.setCellValueFactory(new PropertyValueFactory<>("Valore"));
        Tabella_Parametri.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Tabella_Parametri.setItems(BufferQueryParametri);

        //INIZIALIZZAZIONE CELLE TABELLA OGGETTI
        SerialeOggetto_col.setCellValueFactory(new PropertyValueFactory<>("seriale"));
        NomeOggetto_col.setCellValueFactory(new PropertyValueFactory<>("nome"));
        MarcaOggetto_col.setCellValueFactory(new PropertyValueFactory<>("marca"));
        StatoOggetto_col.setCellValueFactory(new PropertyValueFactory<>("Stato"));
        StringaStato_col.setCellValueFactory(new PropertyValueFactory<>("StringaStato"));
        Tabella_Oggetti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Tabella_Oggetti.setItems(BufferQueryOggetti);
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

        //INIZIALIZZAZIONE CELLE TABELLA COMPONENTI
        colC_Nome.setCellValueFactory(new PropertyValueFactory<>("Nome_Componente"));
        colC_Data_Creazione.setCellValueFactory(new PropertyValueFactory<>("DataCreazione"));
        colC_Descrizione_Azione.setCellValueFactory(new PropertyValueFactory<>("Descrizione_Azione"));
        Tabella_Componente.setItems(BufferQueryComponenti);
        Tabella_Componente.setItems(BufferQueryComponenti);
    }
    public void importClient(Client Client_import) {
        C=Client_import;
        V_sql=new CaricamentoVariabiliSql(C);
        O_sql=new CaricamentoOggettiSql(C);
        M_sql=new CaricamentoSensoriMonitoraggio(C);
        I_sql=new CaricamentoSensoriIntervento(C);
        C_sql=new CaricamentoComponente(C);

        try {
            V_sql.caricaParametri();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());

        try {
            O_sql.caricaOggetti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryOggetti.setAll(O_sql.getBufferQueryOggetti());

        try {
            M_sql.caricaSensori();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQuerySensoriM.setAll(M_sql.getBufferQuerySensMonit());

        try {
            I_sql.caricaSensori();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQuerySensoriI.setAll(I_sql.getBufferQuerySensInterv());

        try {
            C_sql.caricaComponenti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryComponenti.setAll(C_sql.getBufferQueryComponente());
    }

    public  ControllerMostraStatistiche(){
        BufferQueryParametri=FXCollections.observableArrayList();
        BufferQueryOggetti=FXCollections.observableArrayList();
        BufferQuerySensoriM= FXCollections.observableArrayList();
        BufferQuerySensoriI=FXCollections.observableArrayList();
        BufferQueryComponenti=FXCollections.observableArrayList();
    }

    public void chiudiFinestra(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
