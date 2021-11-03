package sample;

import Accesso_Account.Account_Collaudo;
import PatternProxy.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerCollaudo {


    @FXML
    private TextField Id,Name,Lastname,Email,User,pass;

    private Account_Collaudo A;
    private Client C;

    public void setAccount(Account_Collaudo acc, Client Client_import) {

        C=Client_import;
        A=acc;
        setTextAcc(Id,Name,Lastname,Email,User,pass);
    }

    public ControllerCollaudo(){

    }

    @FXML
    private void setTextAcc(TextField I, TextField N, TextField L, TextField E, TextField U, TextField P){
        I.setText(A.getIdAccount());
        N.setText(A.getNome());
        L.setText((A.getCognome()));
        E.setText(A.getEmail());
        U.setText(A.getUsername());
        P.setText(A.getPassword());
    }


    public void creaSensore(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/InstSens.fxml"));
        Parent tableViewParent= loader.load();
        ControllerInstSens C1=loader.getController();
        C1.importClient(C);
        Stage window= new Stage();
        window.setTitle("Installazione Sensore");
        window.setScene(new Scene(tableViewParent));
        window.show();
    }

    public void aggiungiComponenti(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/Crea_Componente.fxml"));
        Parent tableViewParent= loader.load();
        ControllerCrea_Componente C1=loader.getController();
        C1.Import_Client(C);
        Stage window= new Stage();
        window.setTitle("Aggiungi Componenti");
        window.setScene(new Scene(tableViewParent));
        window.show();
    }
    public void resettaSensori(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/ResetSensori.fxml"));
        Parent tableViewParent= loader.load();
        ControllerResetSensori C1=loader.getController();
        C1.Import_Client(C);
        Stage window= new Stage();
        window.setTitle("Reset Sensori");
        window.setScene(new Scene(tableViewParent));
        window.show();
    }
    public void mostraStatistiche(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/MostraStatistiche.fxml"));
        Parent tableViewParent= loader.load();
        ControllerMostraStatistiche C1=loader.getController();
        C1.importClient(C);
        Stage window= new Stage();
        window.setTitle("Mostra Statistiche");
        window.setResizable(false);
        window.setScene(new Scene(tableViewParent));
        window.show();
    }

    public void creaValMis(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/Crea_Val_Misurazione.fxml"));
        Parent tableViewParent= loader.load();
        ControllerCrea_Val_Misurazione C1=loader.getController();
        C1.importClient(C);
        Stage window= new Stage();
        window.setTitle("Creazione Parametri");
        window.setResizable(false);
        window.setScene(new Scene(tableViewParent));
        window.show();
    }

    public void Crea_Oggetto(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/Crea_Oggetto.fxml"));
        Parent tableViewParent= loader.load();
        ControllerCrea_Oggetto C1=loader.getController();
        C1.importClient(C);
        Stage window= new Stage();
        window.setTitle("Creazione Oggetto e Comportamenti");
        window.setResizable(false);
        window.setScene(new Scene(tableViewParent));
        window.show();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/Login.fxml"));
        Parent tableViewParent= loader.load();
        Stage window=(Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Login");
        window.setScene(new Scene(tableViewParent));
        window.show();
    }



}
