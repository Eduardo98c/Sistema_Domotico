package sample;

import PatternProxy.Client;
import Gestione_Sensori.CaricamentoSensoriIntervento;
import Gestione_Sensori.CaricamentoSensoriMonitoraggio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.sql.SQLException;

public class ControllerResetSensori {
    @FXML
    private Label Err_text_SensMonit;

    private Client C;
    private CaricamentoSensoriMonitoraggio M_sql;
    private CaricamentoSensoriIntervento I_sql;

    public ControllerResetSensori(){}

    public void Import_Client(Client Client_Import) {
        C=Client_Import;
        M_sql=new CaricamentoSensoriMonitoraggio(C);
        I_sql=new CaricamentoSensoriIntervento(C);
    }
    public void ResetSensoriMonit(ActionEvent actionEvent) throws SQLException {
        M_sql.resetSensori();
        Err_text_SensMonit.setTextFill(Paint.valueOf("#4CC417"));
        Err_text_SensMonit.setText("Sensori Monitoraggio Resettati");
    }

    public void ResetSensoriInterv(ActionEvent actionEvent) throws SQLException {
        I_sql.resetSensori();
        Err_text_SensMonit.setTextFill(Paint.valueOf("#4CC417"));
        Err_text_SensMonit.setText("Sensori Intervento Resettati");
    }
}
