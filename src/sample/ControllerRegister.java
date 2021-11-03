package sample;

import Accesso_Account.Account_Attivato;
import Accesso_Account.Account_Collaudo;
import PatternProxy.Client;
import PatternProxy.DB_Proxy_Singleton_Connection;
import Accesso_Account.Exception.Prototype_AccountError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

public class ControllerRegister {

    @FXML
    private Label ErrName1, ErrLastName1, ErrEmail1, ErrUser1, ErrPass1;
    @FXML
    private Label ErrName2, ErrLastName2, ErrEmail2, ErrUser2, ErrPass2;
    @FXML
    private TextField NameField1, LastNameField1, EmailField1, UserField1, PassField1;
    @FXML
    private TextField NameField2, LastNameField2, EmailField2, UserField2, PassField2;
    @FXML
    private Client Utente;
    private String Nome, Cognome, Email, Username, Password;

    private static final Prototype_AccountError Err = new Prototype_AccountError();


    protected void setUser(Client U) {
        Utente = U;
    }

    public ControllerRegister() {
    }

    public void BackToLogin(ActionEvent actionEvent) {

            Parent tableViewParent = null;
            try {
                tableViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("sample/Login.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setTitle("Login");
            assert tableViewParent != null;
            window.setScene(new Scene(tableViewParent));
            window.show();
    }
    private void Reset_Variabili(TextField Field1, TextField Field2, TextField Field3, TextField Field4, TextField Field5){
        Field1.setText("");
        Field2.setText("");
        Field3.setText("");
        Field4.setText("");
        Field5.setText("");
    }
    private void SetColor_style(TextField Field,String color_border){
        Field.setStyle("-fx-border-color: "+color_border+"; -fx-background-color:  rgba(53,89,119,0);-fx-border-width: 0px 0px 2px 0px");

    }
    private void Reset_Colori_Variabili(TextField Field1, TextField Field2, TextField Field3, TextField Field4, TextField Field5){
        SetColor_style(Field1, "#0598ff");
        SetColor_style(Field2, "#0598ff");
        SetColor_style(Field3, "#0598ff");
        SetColor_style(Field4, "#0598ff");
        SetColor_style(Field5, "#0598ff");
    }
    private void Set_Variabili(String Field1, String Field2, String Field3, String Field4, String Field5) {
        Nome = Field1;
        Cognome = Field2;
        Email = Field3;
        Username = Field4;
        Password = Field5;

    }
    private void Set_Errori(Label Field1, Label Field2, Label Field3, Label Field4, Label Field5) {
        Field1.setText(Err.errorName(this.Nome));
        Field2.setText(Err.errorLastname(this.Cognome));
        Field3.setText(Err.errorEmail(this.Email));
        Field4.setText(Err.errorUser(this.Username));
        Field5.setText(Err.errorPass(this.Password));
    }

    private boolean VerifyRegistration(Label Field1, Label Field2, Label Field3, Label Field4, Label Field5) {

        Set_Errori(Field1, Field2, Field3, Field4, Field5);

        if (Field1.getText() == null && Field2.getText() == null && Field3.getText() == null && Field4.getText() == null && Field5.getText() == null) {
            return true;
        } else {
            if (Field1.getText() != null)
                Field1.setTextFill(Paint.valueOf("#800517"));
            if (Field2.getText() != null)
                Field2.setTextFill(Paint.valueOf("#800517"));
            if (Field3.getText() != null)
                Field3.setTextFill(Paint.valueOf("#800517"));
            if (Field4.getText() != null)
                Field4.setTextFill(Paint.valueOf("#800517"));
            if (Field5.getText() != null)
                Field5.setTextFill(Paint.valueOf("#800517"));
        }
        return false;
    }
    //METODO PER L'INSERIMENTO DI UN NUOVO UTENTE NEL DATABASE
    private boolean InsertUser(DB_Proxy_Singleton_Connection Access, String QueryAccount, Label Err_usr) {

        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        try (PreparedStatement S = Access.getInstance().returnConnection().prepareStatement(QueryAccount)) {
            S.setString(1, this.Utente.getAccount().getIdAccount());
            S.setString(2, this.Nome);
            S.setString(3, this.Cognome);
            S.setString(4, this.Email);
            S.setString(5, this.Username);
            S.setString(6, this.Password);
            S.setString(7, date.toString());
            S.executeUpdate();
            S.close();
            return true;

        } catch (Exception ex) {
            if(ex instanceof SQLIntegrityConstraintViolationException){
                Err_usr.setTextFill(Paint.valueOf("#800517"));
                Err_usr.setText("L'username è già stato assegnato");
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
            else if (ex instanceof SQLException) {
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
    //METODO PER LA REGISTRAZIONE DI UN ACCOUNT DI TIPO ATTIVATO
    public void RegisterNewAttivato() throws SQLException {

        Set_Variabili(NameField1.getText(), LastNameField1.getText(), EmailField1.getText(), UserField1.getText(), PassField1.getText());

        if (VerifyRegistration(ErrName1, ErrLastName1, ErrEmail1, ErrUser1, ErrPass1) ) {
            System.out.println("Registrazione Effettuata");
            Utente.setAccount(new Account_Attivato(Nome, Cognome, Email, Username, Password));

            String QueryInsert = "INSERT INTO Account_Attivato(idAccount_Attivato, NOME, COGNOME, EMAIL, USERNAME, PASSWORD,DATA_CREAZIONE) VALUES(?,?,?,?,?,?,?)";
            DB_Proxy_Singleton_Connection Access = Utente.getDbConnection_singleton();
            System.out.println(Utente.getAccount().getIdAccount());

            if(InsertUser(Access,QueryInsert,ErrUser1))
                JOptionPane.showMessageDialog(null, "REGISTRAZIONE COMPLETATA");
            else
                JOptionPane.showMessageDialog(null, "REGISTRAZIONE FALLITA");
        }
        else {
            JOptionPane.showMessageDialog(null,"REGISTRAZIONE FALLITA");
        }
        Reset_Variabili(NameField1, LastNameField1, EmailField1, UserField1, PassField1);
        Reset_Colori_Variabili(NameField1, LastNameField1, EmailField1, UserField1, PassField1);
    }

    //METODO PER LA REGISTRAZIONE DI UN ACCOUNT DI TIPO COLLAUDO
    public void RegisterNewCollaudo() throws SQLException {
        Set_Variabili(NameField2.getText(), LastNameField2.getText(), EmailField2.getText(), UserField2.getText(), PassField2.getText());

        if (VerifyRegistration(ErrName2, ErrLastName2, ErrEmail2, ErrUser2, ErrPass2)) {
            System.out.println("Registrazione Effettuata");
            Utente.setAccount(new Account_Collaudo(Nome, Cognome, Email, Username, Password));
            String QueryInsert = "INSERT INTO Account_Collaudo(idAccount_Collaudo, NOME, COGNOME, EMAIL, USERNAME, PASSWORD,DATA_CREAZIONE)VALUES(?,?,?,?,?,?,?)";
            DB_Proxy_Singleton_Connection Access = Utente.getDbConnection_singleton();
            if(InsertUser(Access,QueryInsert,ErrUser2))  //REGISTRA UN NUOVO ACCOUNT E NEL CASO DI FALLIMENTO, RITORNA L'ERRORE OPPORTUNO
                JOptionPane.showMessageDialog(null, "REGISTRAZIONE COMPLETATA");
            else
                JOptionPane.showMessageDialog(null, "REGISTRAZIONE FALLITA");
        } else {
            JOptionPane.showMessageDialog(null,"REGISTRAZIONE FALLITA");
        }
        Reset_Variabili(NameField2, LastNameField2, EmailField2, UserField2, PassField2);
        Reset_Colori_Variabili(NameField2, LastNameField2, EmailField2, UserField2, PassField2);
    }

    private void VerifyText(TextField Field, Label ErrField, String ErrType) {
        if (ErrType == null) {
            SetColor_style(Field,"#4CC417");
            ErrField.setText("");
        } else {
            ErrField.setText(ErrType);
            ErrField.setTextFill(Paint.valueOf("#eb0707"));
            SetColor_style(Field,"#eb0707");
        }
    }
    //EVENTI PER LA SEGNALAZIONE DI ERRORI DURANTE LA REGISTRAZIONE IN FASE DI COMPILAZIONE
    public void VerifyName1() {
        String ErrType = Err.errorName(this.NameField1.getText());
        VerifyText(this.NameField1, this.ErrName1, ErrType);
    }

    public void VerifyLastName1() {
        String ErrType = Err.errorLastname(this.LastNameField1.getText());
        VerifyText(this.LastNameField1, this.ErrLastName1, ErrType);
    }

    public void VerifyEmail1() {
        String ErrType = Err.errorEmail(this.EmailField1.getText());
        VerifyText(this.EmailField1, this.ErrEmail1, ErrType);
    }

    public void VerifyUsername1() {
        String ErrType = Err.errorUser(this.UserField1.getText());
        VerifyText(this.UserField1, this.ErrUser1, ErrType);
    }

    public void VerifyPass1() {
        String ErrType = Err.errorPass(this.PassField1.getText());
        VerifyText(this.PassField1, this.ErrPass1, ErrType);
    }

    public void VerifyName2() {
        String ErrType = Err.errorName(this.NameField2.getText());
        VerifyText(this.NameField2, this.ErrName2, ErrType);
    }

    public void VerifyLastName2() {
        String ErrType = Err.errorLastname(this.LastNameField2.getText());
        VerifyText(this.LastNameField2, this.ErrLastName2, ErrType);
    }

    public void VerifyEmail2() {
        String ErrType = Err.errorEmail(this.EmailField2.getText());
        VerifyText(this.EmailField2, this.ErrEmail2, ErrType);
    }

    public void VerifyUsername2() {
        String ErrType = Err.errorUser(this.UserField2.getText());
        VerifyText(this.UserField2, this.ErrUser2, ErrType);
    }

    public void VerifyPass2() {
        String ErrType = Err.errorPass(this.PassField2.getText());
        VerifyText(this.PassField2, this.ErrPass2, ErrType);
    }

}
