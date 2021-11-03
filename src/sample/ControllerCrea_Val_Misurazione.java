package sample;

import PatternProxy.Client;
import Casa_Domotica.CaricamentoVariabiliSql;
import Casa_Domotica.Exception.CasaException;
import Casa_Domotica.Parametri_Casa_Domotica;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerCrea_Val_Misurazione implements Initializable {

    //TABELLA PARAMETRI
    @FXML
    TableView Tabella_Parametri;
    @FXML
    TableColumn<Parametri_Casa_Domotica, String> Parametro_col;
    @FXML
    TableColumn<Parametri_Casa_Domotica, String> Unita_col;
    @FXML
    TableColumn<Parametri_Casa_Domotica, Double> Valore_col;

    //ALTRI ELEMENTI DELL'INTERFACCIA GRAFICA
    @FXML
    private Spinner<Double> id_spinner;
    @FXML
    private TextField id_Parametro;
    @FXML
    private TextField id_unita;
    @FXML
    private Label Err_text_id;

    private ObservableList<Parametri_Casa_Domotica> BufferQueryParametri;
    private CaricamentoVariabiliSql V_sql;
    private Client C;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Double> RangeValue=new SpinnerValueFactory.DoubleSpinnerValueFactory(0,10000,0);
        this.id_spinner.setValueFactory(RangeValue);

        //INIZIALIZZAZIONE CELLE TABELLA PARAMETRI
        Parametro_col.setCellValueFactory(new PropertyValueFactory<>("parametro"));
        Unita_col.setCellValueFactory(new PropertyValueFactory<>("unita"));
        Valore_col.setCellValueFactory(new PropertyValueFactory<>("Valore"));

        Tabella_Parametri.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Tabella_Parametri.setItems(BufferQueryParametri);
    }

    @FXML
    protected void importClient(Client Import_Client){
        C=Import_Client;
        V_sql=new CaricamentoVariabiliSql(C);
        try {
            V_sql.caricaParametri();
            BufferQueryParametri.addAll(V_sql.getBufferQueryParametri());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public ControllerCrea_Val_Misurazione(){
        BufferQueryParametri =FXCollections.observableArrayList();
    }

    //EVENTO PER LA CREAZIONE DI UN NUOVO PARAMETRO
    public void creaParametro(ActionEvent actionEvent) throws SQLException {

        Double Input_val= id_spinner.getValue();
        try{
            if(id_Parametro.getText().equals("") || id_unita.getText().equals("") || id_spinner.getValue() == null){
                throw new CasaException("Campi Vuoti, Immettere dati");
            }
            else if(Input_val >= 0 && Input_val <= 10000 ) {
                Parametri_Casa_Domotica p = new Parametri_Casa_Domotica(id_Parametro.getText(), id_unita.getText(), id_spinner.getValue()); //CREA UN NUOVO PARAMETRO
                String String_err=V_sql.creaParametro(p); //RICHIAMA IL METODO DALLA CLASSE CaricamentoVariabiliSql
                if(String_err.equals("Inserimento Effettuato Con Successo")){
                    Err_text_id.setTextFill(Paint.valueOf("#4CC417"));
                    Err_text_id.setText(String_err);
                    BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());
                    Tabella_Parametri.setItems(BufferQueryParametri);
                }
                else{
                    throw new CasaException(String_err);
                }
            }
            else if(Input_val < 0 || Input_val > 10000){
                throw new CasaException("Limiti valore Input violati: 0 <= Valore <=10000");
            }
        }
        catch(Exception ex){
            if(ex instanceof CasaException){
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                Err_text_id.setTextFill(Paint.valueOf("#ff0000"));
                Err_text_id.setText(ex.toString());
            }
        }

        id_Parametro.setText("");
        id_unita.setText("");
    }

    //EVENTO PER LA CANCELLAZIONE DI UN ELEMENTO
    public void cancellaElemento(ActionEvent actionEvent) throws SQLException {
        ObservableList<Parametri_Casa_Domotica> Temp_delete=FXCollections.observableArrayList();
        Temp_delete.setAll(Tabella_Parametri.getSelectionModel().getSelectedItems());
        for (Parametri_Casa_Domotica parametri_casa_domotica : Temp_delete) {
            V_sql.cancellaVariabileMisurazione(parametri_casa_domotica);//METODO PER ELIMINARE DAL DATABASE
            Tabella_Parametri.getItems().removeAll(Tabella_Parametri.getSelectionModel().getSelectedItems());
        }
        Tabella_Parametri.setItems(V_sql.getBufferQueryParametri());
    }

    //EVENTO PER LA CHIUSURA DELLA FINESTRA
    public void chiudiFinestra(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}