package sample;

import PatternProxy.Client;
import Casa_Domotica.*;
import Casa_Domotica.Exception.CasaException;
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
import java.util.UUID;

public class ControllerCrea_Oggetto implements Initializable {

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

    //TABELLA DEI COMPORTAMENTI DEI SINGOLI OGGETTI
    @FXML
    TableView<Comportamento_Oggetti> Tabella_Comportamenti;
    @FXML
    TableColumn<Comportamento_Oggetti,String> SerialeOggettoRif_col;
    @FXML
    TableColumn<Comportamento_Oggetti,String> ParametroComport_col;
    @FXML
    TableColumn<Comportamento_Oggetti,String> Comport_col;
    @FXML
    TableColumn<Comportamento_Oggetti,Double> Consumo_col;

    //ALTRI ELEMENTI DELL'INTERFACCIA GRAFICA
    @FXML
    private ComboBox<String> id_Combobox_Par;
    @FXML
    private ComboBox<String> id_Combobox_Comp;
    @FXML
    private Spinner<Double> id_Val_input;
    @FXML
    private TextField id_Nome_ogg;
    @FXML
    private TextField id_Marca_ogg;
    @FXML
    private Label Err_text_ogg;
    @FXML
    private Label Err_text_Comp;

    private ObservableList<Parametri_Casa_Domotica> BufferQueryParametri;
    private ObservableList<Oggetti_Casa_Domotica> BufferQueryOggetti;
    private ObservableList<String> Scelte_Parametri;
    private final ObservableList<String> Scelte_Comportamenti= FXCollections.observableArrayList("INCREMENTA IL PARAMETRO","DECREMENTA IL PARAMETRO");

    private CaricamentoVariabiliSql V_sql;
    private Oggetti_Casa_Domotica O;
    private CaricamentoOggettiSql O_sql;
    private CaricamentoComportamentiSql C_sql;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //INIZIALIZZAZIONE COMBOBOX E SPINNER
        id_Combobox_Par.setItems(Scelte_Parametri);  //CARICO I PARAMETRI CREATI E LI INSERISCO NELLA COMBOBOX
        id_Combobox_Comp.setItems(Scelte_Comportamenti); //CARICO LE SCELTE DEI COMPORTAMENTI(INCREMENTO O DECREMENTO)
        SpinnerValueFactory<Double> RangeValue=new SpinnerValueFactory.DoubleSpinnerValueFactory(0,10000,0);
        id_Val_input.setValueFactory(RangeValue);


        //INIZIALIZZAZIONE CELLE TABELLA OGGETTI
        SerialeOggetto_col.setCellValueFactory(new PropertyValueFactory<>("seriale"));
        NomeOggetto_col.setCellValueFactory(new PropertyValueFactory<>("nome"));
        MarcaOggetto_col.setCellValueFactory(new PropertyValueFactory<>("marca"));
        StatoOggetto_col.setCellValueFactory(new PropertyValueFactory<>("Stato"));
        StringaStato_col.setCellValueFactory(new PropertyValueFactory<>("StringaStato"));
        Tabella_Oggetti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Tabella_Oggetti.setItems(BufferQueryOggetti);

        //INIZIALIZZAZIONE CELLE TABELLA COMPORTAMENTI
        SerialeOggettoRif_col.setCellValueFactory(new PropertyValueFactory<>("Seriale_OggettoRif"));
        ParametroComport_col.setCellValueFactory(new PropertyValueFactory<>("ParametroComport"));
        Comport_col.setCellValueFactory(new PropertyValueFactory<>("Tipo_Comportamento"));
        Consumo_col.setCellValueFactory(new PropertyValueFactory<>("Valore_consumo"));
    }

    protected void importClient(Client Client_Import) {
        V_sql=new CaricamentoVariabiliSql(Client_Import);
        O_sql=new CaricamentoOggettiSql(Client_Import);
        C_sql=new CaricamentoComportamentiSql(Client_Import);

        try {
            O_sql.caricaOggetti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryOggetti.setAll(O_sql.getBufferQueryOggetti());
        try {
            V_sql.caricaParametri();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());
        setScelteParametri();

    }
    public ControllerCrea_Oggetto(){
        BufferQueryOggetti=FXCollections.observableArrayList();
        BufferQueryParametri=FXCollections.observableArrayList();
        Scelte_Parametri=FXCollections.observableArrayList();
    }

    private void setScelteParametri(){
        Scelte_Parametri.clear();
        for(Parametri_Casa_Domotica Par: BufferQueryParametri){
            Scelte_Parametri.add(Par.getParametro());
        }
    }

    //METODI PER LA GESTIONE DEGLI OGGETTI
    public void crea_Oggetto(ActionEvent actionEvent) throws SQLException {
        try{
            if(id_Nome_ogg.getText().isEmpty() || id_Marca_ogg.getText().isEmpty()){
                throw new CasaException("Campi Vuoti, Immettere dati");
            }
            else{
                String Seriale;
                UUID uuidrand=UUID.randomUUID();
                Seriale=UUID.fromString(uuidrand.toString()).toString();
                O=new Oggetti_Casa_Domotica(Seriale,id_Nome_ogg.getText(),id_Marca_ogg.getText(),0);
                if(O_sql.inserisciOggetto(O)) {
                    Tabella_Oggetti.setItems(O_sql.getBufferQueryOggetti());
                    Err_text_ogg.setTextFill(Paint.valueOf("#4CC417"));
                    Err_text_ogg.setText("Inserimento Effettuato Con Successo");
                }
            }
        }
        catch(Exception ex){
            if(ex instanceof CasaException){
                Err_text_ogg.setTextFill(Paint.valueOf("#800517"));
                Err_text_ogg.setText(ex.toString());
            }
        }
        id_Nome_ogg.setText("");
        id_Marca_ogg.setText("");
    }

    public void cancellaOggetto(ActionEvent actionEvent) throws SQLException {
        ObservableList<Oggetti_Casa_Domotica> Temp_delete=FXCollections.observableArrayList();
        Temp_delete.setAll(Tabella_Oggetti.getSelectionModel().getSelectedItems());
        for (Oggetti_Casa_Domotica oggetti_casa_domotica : Temp_delete) {
            O_sql.eliminaOggetto(oggetti_casa_domotica);//METODO PER ELIMINARE DAL DATABASE
            Tabella_Oggetti.getItems().removeAll(Tabella_Oggetti.getSelectionModel().getSelectedItems());
        }
        Tabella_Oggetti.setItems(O_sql.getBufferQueryOggetti());
    }

    //METODI PER LA GESTIONE DEI COMPORTAMENTI
    public void creaComportamento(ActionEvent actionEvent) {
       O=Tabella_Oggetti.getSelectionModel().getSelectedItem();
       try{
           if(O == null){
               throw new CasaException("Seleziona Un Oggetto");
           }
           if(id_Combobox_Par.getValue()== null || id_Combobox_Comp.getValue()== null || id_Val_input ==null){
               throw new CasaException("Campi Vuoti, Immettere dati");
           }
           else{
               Comportamento_Oggetti Comp=new Comportamento_Oggetti(O.getSeriale(),id_Combobox_Par.getValue(),id_Combobox_Comp.getValue(),id_Val_input.getValue());
               C_sql.inserisciComportamento(Comp,O);
           }
       }
       catch(Exception ex){
           if(ex instanceof CasaException){
               System.err.println(ex.getMessage());
               ex.printStackTrace();
               Err_text_Comp.setTextFill(Paint.valueOf("#800517"));
               Err_text_Comp.setText(ex.toString());
           }
       }
    }
    public void cancellaComportamento(ActionEvent actionEvent) throws SQLException {
        O=Tabella_Oggetti.getSelectionModel().getSelectedItem();
        ObservableList<Comportamento_Oggetti> Temp_delete=FXCollections.observableArrayList();
        Temp_delete.setAll(Tabella_Comportamenti.getSelectionModel().getSelectedItems());
        for (Comportamento_Oggetti comportamento_oggetto: Temp_delete) {
            C_sql.eliminaComportamento(comportamento_oggetto,O);
            Tabella_Comportamenti.getItems().removeAll(Tabella_Comportamenti.getSelectionModel().getSelectedItems());
        }
        Tabella_Comportamenti.setItems(C_sql.getBufferQueryComportamenti());
    }

    //CARICA I COMPORTAMENTI DELL'OGGETTO SELEZIONATO NELLA TABELLA IN BASSO
    public void caricaComportamenti(MouseEvent mouseEvent) throws SQLException {
        if(Tabella_Oggetti.getSelectionModel().getSelectedItem() != null) {
            C_sql.caricaComportamenti(Tabella_Oggetti.getSelectionModel().getSelectedItem());
            Tabella_Comportamenti.setItems(C_sql.getBufferQueryComportamenti());
        }
    }

    //CARICAMENTO DEI PARAMETRI DI CASA DI RIFERIMENTO PER LA CREAZIONE DEL COMPORTAMENTO
    public void caricaParametri(MouseEvent mouseEvent) {
        try {
            V_sql.caricaParametri();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BufferQueryParametri.clear();
        BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());
        setScelteParametri();
        id_Combobox_Par.setItems(Scelte_Parametri);
    }

    //EVENTO PER CHIUSURA DELLA FINESTRA
    public void chiudiFinestra(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

}
