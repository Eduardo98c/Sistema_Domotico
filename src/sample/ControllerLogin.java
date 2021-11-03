package sample;

import Accesso_Account.*;
import Accesso_Account.Exception.AccountException;
import PatternProxy.Client;
import PatternProxy.DB_Proxy_Singleton_Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class ControllerLogin  {

    @FXML
    private Label ErrorLabel1;
    @FXML
    private  TextField UserField1;
    @FXML
    private PasswordField PassField1;
    @FXML
    private ChoiceBox<String> ChoiceAccount=new ChoiceBox<String>();


    private static final Vector<String> Temp_src=new Vector<>(9);
    private static ObservableList<String> Obs_Choice= FXCollections.observableArrayList("ATTIVATO","COLLAUDO");
    private final Client C;


    @FXML
    private void initialize(){
        ChoiceAccount.setItems(Obs_Choice);
    }

    public ControllerLogin(){C=new Client(); }


    public void To_Register(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/Register.fxml"));
        Parent tableViewParent= loader.load();
        ControllerRegister C1=loader.getController();
        C1.setUser(C);
        Stage window=(Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Register");
        window.setScene(new Scene(tableViewParent));
        window.show();

    }

    private boolean Login_Account(DB_Proxy_Singleton_Connection Access, String QueryAccount){
        String ControlUser=null,ControlPass=null;
        try(PreparedStatement S=Access.getInstance().returnConnection().prepareStatement(QueryAccount)){
            S.setString(1,UserField1.getText()); //USERNAME INSERITO DALL'UTENTE PER QUERY
            S.setString(2,PassField1.getText()); //PASSWORD INSERITA DALL'UTENTE PER QUERY

            ResultSet Search=S.executeQuery();        //PERMETTE ESECUZIONE QUERY
            ResultSetMetaData md = Search.getMetaData();
            int columnCount = md.getColumnCount();   //RESTITUISCE IL NUMERO DI COLONNE DELLA TABELLA

            while(Search.next()){ //CONTINUA FIN QUANDO NON HA TROVATO UNA QUERY CON GLI USER E PASS CORRISPONDENTI
                ControlUser=Search.getString("Username");
                ControlPass=Search.getString("Password");

                for(int columnIndex = 1; columnIndex <= columnCount; columnIndex++) { //SCORRE LE COLONNE DELL'ELEMENTO ATTUALE
                    Temp_src.add(Search.getObject(columnIndex).toString());  //RIEMPIE L'ARRAY DI COLONNE
                }
            }
            if(ControlUser != null && ControlPass != null) {  //CONTROLLA SE USER E PASS SONO DIVERSI DA NULL(NESSUNA CORRISPONDENZA)
                ErrorLabel1.setTextFill(Paint.valueOf("#347C17"));
                this.ErrorLabel1.setText("Login effettuato con successo come \n"+QueryAccount.substring(14,30));
                S.close();
                return true;
            }
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
        return false;
    }

    public void ValidateAccount(ActionEvent actionEvent) throws SQLException {
        DB_Proxy_Singleton_Connection Access;
        Access = C.getDbConnection_singleton();  //RITORNO LA CONNESSIONE DEL CLIENT
        String QueryAccountAttivato = "SELECT * FROM Account_Attivato WHERE BINARY Username =? AND BINARY Password= ?";
        String QueryAccountCollaudo = "SELECT * FROM Account_Collaudo WHERE BINARY Username =? AND BINARY Password = ?";

        try {
            if (ChoiceAccount.getValue()=="ATTIVATO" && Login_Account(Access, QueryAccountAttivato)) {  //CONTROLLA VALIDITA' PER ACCESSO AD ACCOUNT ATTIVATO
                Account Acc = new Account_Attivato(Temp_src.elementAt(1), Temp_src.elementAt(2), Temp_src.elementAt(3), Temp_src.elementAt(4), Temp_src.elementAt(5));
                C.setAccount(Acc);
                Temp_src.removeAllElements();

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/Intervento.fxml"));
                Parent tableViewParent = loader.load();
                ControllerIntervento C1=loader.getController();
                C1.setAccount((Account_Attivato) Acc,C);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setTitle("Account Attivato");
                window.setScene(new Scene(tableViewParent));
                window.show();

            } else if (ChoiceAccount.getValue()=="COLLAUDO" && Login_Account(Access, QueryAccountCollaudo)) { //CONTROLLA VALIDITA' PER ACCESSO AD ACCOUNT COLLAUDO
                Account Acc = new Account_Collaudo(Temp_src.elementAt(1), Temp_src.elementAt(2), Temp_src.elementAt(3), Temp_src.elementAt(4), Temp_src.elementAt(5));
                C.setAccount(Acc);
                Temp_src.removeAllElements();

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/Collaudo.fxml"));
                Parent tableViewParent = loader.load();
                ControllerCollaudo C2=loader.getController();
                C2.setAccount( (Account_Collaudo) Acc,C);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setTitle("Account Collaudo");
                window.setScene(new Scene(tableViewParent));
                window.show();

            } else if (ChoiceAccount.getValue() == null) {
                throw new AccountException("Scegliere un account dal menu' a tendina");

            } else {
                throw new AccountException("Login: User o Password non corretti");
            }
        } catch (Exception ex) {
            if (ex instanceof AccountException) {
                ErrorLabel1.setTextFill(Paint.valueOf("#800517"));
                this.ErrorLabel1.setText(ex.toString());
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            else{
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }

    }


}
