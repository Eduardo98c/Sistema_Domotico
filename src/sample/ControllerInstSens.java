package sample;

import PatternProxy.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerInstSens {
    Client C;

    public void importClient(Client Client_import) {
        C=Client_import;
    }

    public void toIntervento(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/InstSensoreInterv.fxml"));
        Parent tableViewParent= loader.load();
        ControllerInstSensoreInterv C1=loader.getController();
        C1.Import_Client(C);
        Stage window=(Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Installazione Sensore Intervento");
        window.setResizable(false);
        window.setScene(new Scene(tableViewParent));
        window.show();
    }

    public void toMonitoraggio(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("sample/InstSensoreMonit.fxml"));
        Parent tableViewParent= loader.load();
        ControllerInstSensoreMonit C1=loader.getController();
        C1.importClient(C);
        Stage window=(Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Installazione Sensore Monitoraggio");
        window.setResizable(false);
        window.setScene(new Scene(tableViewParent));
        window.show();
    }


    public void chiudiFinestra(ActionEvent actionEvent) {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
