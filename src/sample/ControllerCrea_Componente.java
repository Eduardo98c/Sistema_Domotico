package sample;

import PatternProxy.Client;
import FactoryMethod.SensoreIntervento;
import FactoryMethod.SensoreMonitoraggio;
import Gestione_Sensori.CaricamentoComponente;
import Gestione_Sensori.CaricamentoSensoriIntervento;
import Gestione_Sensori.CaricamentoSensoriMonitoraggio;
import Gestione_Sensori.Componente;
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

public class ControllerCrea_Componente implements Initializable {
    //TABELLA COMPONENTI
    @FXML
    private TableView<Componente> Tabella_Componente;
    @FXML
    private TableColumn<Componente, String> colC_Nome;
    @FXML
    private TableColumn<Componente, String> colC_Data_Creazione;
    @FXML
    private TableColumn<Componente, String> colC_Descrizione_Azione;

    //ALTRI ELEMENTI DELL'INTERFACCIA GRAFICA
    @FXML
    private TextField id_Nome_Componente;
    @FXML
    private TextField id_Descrizione_Azione;
    @FXML
    private ComboBox<String> id_SensoreM_Colleg;
    @FXML
    private ComboBox<String> id_SensoreI_Colleg;
    @FXML
    private ComboBox<String> id_SensoreM_Scolleg;
    @FXML
    private ComboBox<String> id_SensoreI_Scolleg;
    @FXML
    private Label Err_text_InsComp;
    @FXML
    private Label Err_text_Colleg_Comp;

    private Client C;

    //LISTE DI CARICAMENTO OGGETTI DAL DATABASE
    private ObservableList<Componente> BufferQueryComponenti;
    private ObservableList<SensoreMonitoraggio> BufferQuerySensoriM_Colleg;
    private ObservableList<SensoreMonitoraggio> BufferQuerySensoriM_Scolleg;
    private ObservableList<SensoreIntervento> BufferQuerySensoriI_Colleg;
    private ObservableList<SensoreIntervento> BufferQuerySensoriI_Scolleg;

    //LISTE DI NOMI PER LE SCELTE DELLE COMBOBOX
    private ObservableList<String> Scelte_SensoriM_Colleg;
    private ObservableList<String> Scelte_SensoriM_Scolleg;
    private ObservableList<String> Scelte_SensoriI_Colleg;
    private ObservableList<String> Scelte_SensoriI_Scolleg;

    //CLASSI PER LA GESTIONE DEI CARICAMENTI DAI DATABASE
    private CaricamentoComponente C_sql;
    private CaricamentoSensoriMonitoraggio M_sql;
    private CaricamentoSensoriIntervento I_sql;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //INIZIALIZZAZIONE TABELLA COMPONENTI
        colC_Nome.setCellValueFactory(new PropertyValueFactory<>("Nome_Componente"));
        colC_Data_Creazione.setCellValueFactory(new PropertyValueFactory<>("DataCreazione"));
        colC_Descrizione_Azione.setCellValueFactory(new PropertyValueFactory<>("Descrizione_Azione"));
        Tabella_Componente.setItems(BufferQueryComponenti);
        Tabella_Componente.setItems(BufferQueryComponenti);
    }

    protected void Import_Client(Client Client_Import) {
        C = Client_Import;
        C_sql = new CaricamentoComponente(C);
        M_sql=new CaricamentoSensoriMonitoraggio(C);
        I_sql=new CaricamentoSensoriIntervento(C);

        try {
            C_sql.caricaComponenti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryComponenti.setAll(C_sql.getBufferQueryComponente());
    }

    public ControllerCrea_Componente() {
        BufferQueryComponenti = FXCollections.observableArrayList();
        BufferQuerySensoriM_Colleg=FXCollections.observableArrayList();
        BufferQuerySensoriM_Scolleg =FXCollections.observableArrayList();
        BufferQuerySensoriI_Colleg=FXCollections.observableArrayList();
        BufferQuerySensoriI_Scolleg =FXCollections.observableArrayList();

        Scelte_SensoriM_Colleg=FXCollections.observableArrayList();
        Scelte_SensoriM_Scolleg=FXCollections.observableArrayList();
        Scelte_SensoriI_Colleg=FXCollections.observableArrayList();
        Scelte_SensoriI_Scolleg=FXCollections.observableArrayList();
    }
    private String controllErrC(){
        try{
            if(id_Nome_Componente.getText().isEmpty()){
                throw new SensoriException("Inserire un Nome");
            }
            if(id_Descrizione_Azione.getText().isEmpty()){
                throw new SensoriException("Inserire una Descrizione");
            }
        }catch (Exception ex){
            if(ex instanceof SensoriException){
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return ex.toString();
            }
        }
        return null;
    }
    public void Crea_Componente(ActionEvent actionEvent) throws SQLException {
        String Err= controllErrC();
        if (Err == null) {
            java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
            Componente Nuovo_Componente = new Componente(id_Nome_Componente.getText(), date.toString(), id_Descrizione_Azione.getText());
            if(C_sql.inserisciComponente(Nuovo_Componente)) {
                Tabella_Componente.setItems(C_sql.getBufferQueryComponente());
                Err_text_InsComp.setTextFill(Paint.valueOf("#4CC417"));
                Err_text_InsComp.setText("Inserimento Effettuato Con Successo");
            }
        }
        else{
            Err_text_InsComp.setTextFill(Paint.valueOf("#800517"));
            Err_text_InsComp.setText(Err);
        }
    }
    //METODO PER ELIMINARE UN COMPONENTE
    public void eliminaComponente(ActionEvent actionEvent) throws SQLException {
        ObservableList<Componente> Temp_Delete=FXCollections.observableArrayList();
        Temp_Delete.setAll(Tabella_Componente.getSelectionModel().getSelectedItems());
        for(Componente Comp : Temp_Delete){
            C_sql.Elimina_Componente(Comp);
            Tabella_Componente.getItems().removeAll(Tabella_Componente.getSelectionModel().getSelectedItems());
        }
        Tabella_Componente.setItems(C_sql.getBufferQueryComponente());
    }

    //COLLEGA UN COMPONENTE AD UN SENSORE MONITORAGGIO
    public void collegaComponenteM(ActionEvent actionEvent) throws SQLException {
        if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null && id_SensoreM_Scolleg.getValue()!=null) {
           C_sql.collegaComponenti(id_SensoreM_Scolleg.getValue(),Tabella_Componente.getSelectionModel().getSelectedItem(),true);
           Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#4CC417"));
           Err_text_Colleg_Comp.setText("Componente Collegata con Successo");
        }
        else{
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
            Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella e un Sensore scollegato");
        }
    }

    //COLLEGA UN COMPONENTE AD UN SENSORE INTERVENTO
    public void collegaComponenteI(ActionEvent actionEvent) throws SQLException {
        if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null && id_SensoreI_Scolleg.getValue()!=null) {
            C_sql.collegaComponenti(id_SensoreI_Scolleg.getValue(),Tabella_Componente.getSelectionModel().getSelectedItem(),false);
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#4CC417"));
            Err_text_Colleg_Comp.setText("Componente Collegata con Successo");
        }
        else{
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
            Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella e un Sensore scollegato");
        }
    }

    //SCOLLEGA UN COMPONENTE AD UN SENSORE MONITORAGGIO
    public void scollegaComponenteM(ActionEvent actionEvent) throws SQLException {
        if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null && id_SensoreM_Colleg.getValue()!=null) {
           C_sql.scollegaComponenti(id_SensoreM_Colleg.getValue(),Tabella_Componente.getSelectionModel().getSelectedItem(),true);
           Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#4CC417"));
           Err_text_Colleg_Comp.setText("Componente Scollegata con Successo");
        }
        else{
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
            Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella e un Sensore già Collegato");
        }
    }

    //SCOLLEGA UN COMPONENTE AD UN SENSORE INTERVENTO
    public void scollegaComponenteI(ActionEvent actionEvent) throws SQLException {
        if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null && id_SensoreI_Colleg.getValue()!=null) {
            C_sql.scollegaComponenti(id_SensoreI_Colleg.getValue(),Tabella_Componente.getSelectionModel().getSelectedItem(),false);
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#4CC417"));
            Err_text_Colleg_Comp.setText("Componente Scollegata con Successo");
        }
        else{
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
            Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella e un Sensore già Collegato");
        }
    }

    //CARICAMENTI SENSORI COLLEGATI E SCOLLEGATI NELLE COMBOBOX
    public void caricaSensoriMColleg(MouseEvent mouseEvent) throws SQLException {
       if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null) {
           Err_text_Colleg_Comp.setText("");
           C_sql.caricaSensoriM(Tabella_Componente.getSelectionModel().getSelectedItem(), true);
           BufferQuerySensoriM_Colleg.clear();
           BufferQuerySensoriM_Colleg.setAll(C_sql.getBufferQuerySensoreM());
           Scelte_SensoriM_Colleg.clear();
           for (SensoreMonitoraggio S : BufferQuerySensoriM_Colleg) {
               Scelte_SensoriM_Colleg.add(S.getNome());
           }
           id_SensoreM_Colleg.setItems(Scelte_SensoriM_Colleg);
       }
       else{
           Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
           Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella");
       }
    }

    public void caricaSensoriMScolleg(MouseEvent mouseEvent) throws SQLException {
        if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null) {
            Err_text_Colleg_Comp.setText("");
            C_sql.caricaSensoriM(Tabella_Componente.getSelectionModel().getSelectedItem(), false);
            BufferQuerySensoriM_Scolleg.clear();
            BufferQuerySensoriM_Scolleg.setAll(C_sql.getBufferQuerySensoreM());
            Scelte_SensoriM_Scolleg.clear();
            for (SensoreMonitoraggio S : BufferQuerySensoriM_Scolleg) {
                Scelte_SensoriM_Scolleg.add(S.getNome());
            }
            id_SensoreM_Scolleg.setItems(Scelte_SensoriM_Scolleg);
        }
        else{
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
            Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella");
        }
    }
    public void caricaSensoriIColleg(MouseEvent mouseEvent) throws SQLException {
        if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null) {
            Err_text_Colleg_Comp.setText("");
            C_sql.caricaSensoriI(Tabella_Componente.getSelectionModel().getSelectedItem(), true);
            BufferQuerySensoriI_Colleg.clear();
            BufferQuerySensoriI_Colleg.setAll(C_sql.getBufferQuerySensoreI());
            Scelte_SensoriI_Colleg.clear();
            for(SensoreIntervento S: BufferQuerySensoriI_Colleg){
                Scelte_SensoriI_Colleg.add(S.getNome());
            }
            id_SensoreI_Colleg.setItems(Scelte_SensoriI_Colleg);
        }
        else{
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
            Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella");
        }
    }
    
    public void caricaSensoriIScolleg(MouseEvent mouseEvent) throws SQLException {
        if(Tabella_Componente.getSelectionModel().getSelectedItem()!=null) {
            Err_text_Colleg_Comp.setText("");
            C_sql.caricaSensoriI(Tabella_Componente.getSelectionModel().getSelectedItem(), false);
            BufferQuerySensoriI_Scolleg.clear();
            BufferQuerySensoriI_Scolleg.setAll(C_sql.getBufferQuerySensoreI());
            Scelte_SensoriI_Scolleg.clear();
            for(SensoreIntervento S: BufferQuerySensoriI_Scolleg){
                Scelte_SensoriI_Scolleg.add(S.getNome());
            }
            id_SensoreI_Scolleg.setItems(Scelte_SensoriI_Scolleg);
        }
        else{
            Err_text_Colleg_Comp.setTextFill(Paint.valueOf("#800517"));
            Err_text_Colleg_Comp.setText("Selezionare un Componente dalla Tabella");
        }
    }

    public void chiudiFinestra(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

}
