package sample;

import Accesso_Account.Account_Attivato;
import PatternProxy.Client;
import Casa_Domotica.*;
import FactoryMethod.SensoreIntervento;
import FactoryMethod.SensoreMonitoraggio;
import Gestione_Sensori.CaricamentoSensoriIntervento;
import Gestione_Sensori.CaricamentoSensoriMonitoraggio;
import ObserverPattern.Observer;
import ObserverPattern.ParametroObservable;
import ObserverPattern.SensoreMonitoraggioObserver;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

public class ControllerIntervento implements Initializable {

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
    TableColumn<Oggetti_Casa_Domotica, String> StringaStato_col;

    //TABELLA PARAMETRI
    @FXML
    TableView<Parametri_Casa_Domotica> Tabella_Parametri;
    @FXML
    TableColumn<Parametri_Casa_Domotica, String> Parametro_col;
    @FXML
    TableColumn<Parametri_Casa_Domotica, String> Unita_col;
    @FXML
    TableColumn<Parametri_Casa_Domotica, Double> Valore_col;
    //TABELLA GESTIONE ALLARMI
    @FXML
    private ListView<String> Table_AllarmiSensor;

    //TABELLA GESTIONE AGGIORNAMENTO DEI VALORI
    @FXML
    private ListView<String> Table_AggiornVal;

    //ALTRI ELEMENTI DELL'INTERFACCIA GRAFICA
    @FXML
    private TextField Id, Name, Lastname, Email, User, pass;
    @FXML
    private Button id_bottone_interr;

    //LISTe DI STRINGHE PER LA STAMPA DEL LOG DEI PROBLEMI DELLA CASA DOMOTICA
    private ObservableList<String> Allarmi;
    private ObservableList<String> AggiornamentoValori;

    //CODA DI OBSERVER
    private Queue<Observer> Gestione_Coda;
    //LISTE PER LA MEMORIAZZAZIONE DEI VALORI CARICATI DAL DATABASE E PER GESTIRE GLI ALLARMI
    private ObservableList<Oggetti_Casa_Domotica> BufferQueryOggetti;
    private ObservableList<Comportamento_Oggetti> BufferQueryComportamenti;
    private ObservableList<Parametri_Casa_Domotica> BufferQueryParametri;
    private ObservableList<Parametri_Casa_Domotica> BackupParametri;
    private ObservableList<SensoreMonitoraggio> BufferQueryMonit;
    private ObservableList<SensoreIntervento> BufferQueryInterv;

    //OGGETTI PER CARICAMENTO O LA MODIFICA DAL DATABASE
    private CaricamentoOggettiSql O_sql;
    private CaricamentoComportamentiSql C_sql;
    private CaricamentoVariabiliSql V_sql;
    private CaricamentoSensoriMonitoraggio M_sql;
    private CaricamentoSensoriIntervento I_sql;

    //ACCOUNT INTERVENTO IMPORTATO
    private Account_Attivato A;
    private Client C;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //INIZIALIZZAZIONE CELLE TABELLA OGGETTI
        SerialeOggetto_col.setCellValueFactory(new PropertyValueFactory<>("seriale"));
        NomeOggetto_col.setCellValueFactory(new PropertyValueFactory<>("nome"));
        MarcaOggetto_col.setCellValueFactory(new PropertyValueFactory<>("marca"));
        StatoOggetto_col.setCellValueFactory(new PropertyValueFactory<>("Stato"));
        StringaStato_col.setCellValueFactory(new PropertyValueFactory<>("StringaStato"));
        Tabella_Oggetti.setItems(BufferQueryOggetti);

        //INIZIALIZZAZIONE CELLE TABELLA PARAMETRI
        Parametro_col.setCellValueFactory(new PropertyValueFactory<>("parametro"));
        Unita_col.setCellValueFactory(new PropertyValueFactory<>("unita"));
        Valore_col.setCellValueFactory(new PropertyValueFactory<>("Valore"));
        Tabella_Parametri.setItems(BufferQueryParametri);


    }

    public void setAccount(Account_Attivato acc, Client Client_import) {
        C = Client_import;
        A = acc;
        setTextAcc(Id, Name, Lastname, Email, User, pass);
        O_sql = new CaricamentoOggettiSql(C);
        V_sql = new CaricamentoVariabiliSql(C);
        C_sql = new CaricamentoComportamentiSql(C);
        M_sql = new CaricamentoSensoriMonitoraggio(C);
        I_sql = new CaricamentoSensoriIntervento(C);
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
        BackupParametri.setAll(V_sql.getBufferQueryParametri());
        BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());
    }

    //INIZIALIZZAZIONE BUFFER PER LA GESTIONE DEI DATI DEL DATABASE E LA GESTIONE DEGLI ALLARMI
    public ControllerIntervento() {
        Gestione_Coda = new LinkedList<>();
        Allarmi = FXCollections.observableArrayList();
        AggiornamentoValori = FXCollections.observableArrayList();
        BufferQueryOggetti = FXCollections.observableArrayList();
        BufferQueryParametri = FXCollections.observableArrayList();
        BackupParametri=FXCollections.observableArrayList();
        BufferQueryComportamenti = FXCollections.observableArrayList();
        BufferQueryMonit = FXCollections.observableArrayList();
        BufferQueryInterv = FXCollections.observableArrayList();
    }

    //METODO PER MOSTRARE NEL LOG DELLA CASA DOMOTICA LE EMERGENZE
    public void setAllarmi(String allarme) {
        Allarmi.add("");
        Allarmi.add(allarme);
        Table_AllarmiSensor.setItems(Allarmi);
    }

    //METODO PER SETTARE IL BOTTONE DI ACCENSIONE/SPEGNIMENTO
    public void attivaInterruttore(MouseEvent mouseEvent) {
        if (Tabella_Oggetti.getSelectionModel().getSelectedItem() != null) {
            if (Tabella_Oggetti.getSelectionModel().getSelectedItem().getStato() == 1) {
                id_bottone_interr.setDisable(false);
                id_bottone_interr.setText("SPEGNI");
            } else {
                id_bottone_interr.setDisable(false);
                id_bottone_interr.setText("ACCENDI");
            }
        }
    }

    //METODO PER LA GESTIONE DI ACCENSIONE/SPEGNIMENTO DELL'OGGETTO E TUTTE LE CONSEGUENZE DEL CAMBIO DI STATO
    public void interruttoreOggetto(ActionEvent actionEvent) throws SQLException {
        Oggetti_Casa_Domotica Ogg_temp = Tabella_Oggetti.getSelectionModel().getSelectedItem();
        if (Tabella_Oggetti.getSelectionModel().getSelectedItem() != null) {

            //SE L'OGGETTO VIENE ACCESO/SPENTO
            if (id_bottone_interr.getText().equals("ACCENDI")) {
                O_sql.interruttoreOggetti(Tabella_Oggetti.getSelectionModel().getSelectedItem(), 1);
                Tabella_Oggetti.setItems(O_sql.getBufferQueryOggetti());
                id_bottone_interr.setText("INTERRUTTORE");
                id_bottone_interr.setDisable(true);

                //GESTISCO I COMPORTAMENTI DELL'OGGETTO
                gestisci_Comportamenti(Ogg_temp);

            } else if (id_bottone_interr.getText().equals("SPEGNI")) {
                Tabella_Oggetti.getSelectionModel().getSelectedItem().setStato(1);
                O_sql.interruttoreOggetti(Tabella_Oggetti.getSelectionModel().getSelectedItem(), 0);
                Tabella_Oggetti.setItems(O_sql.getBufferQueryOggetti());
                id_bottone_interr.setText("INTERRUTTORE");
                id_bottone_interr.setDisable(true);

                //GESTISCO I COMPORTAMENTI DEGLI OGGETTI AL CONTRARIO DATO CHE L'OGGETTO VIENE CHIUSO
                gestisci_Comportamenti(Ogg_temp);
            }
        }
    }

    //CARICA TUTTI I COMPORTAMENTI DELL'OGGETTO AL FINE DI INCREMENTARE/DECREMENTARE I PARAMETRI
    private void gestisci_Comportamenti(Oggetti_Casa_Domotica O) throws SQLException {
        C_sql.caricaComportamenti(O);
        BufferQueryComportamenti.setAll(C_sql.getBufferQueryComportamenti());

        //CONTROLLA I PARAMETRI INCREMENTATI/DECREMENTATI DALL'OGGETTO E PROVVEDE A GESTIRLI CON IL PATTERN(OBSERVER)
        for (Comportamento_Oggetti CompOgg : BufferQueryComportamenti) {
            Parametri_Casa_Domotica Parametro_temp = V_sql.CaricaParametroComportamento(CompOgg.getParametroComport());
            System.out.println(Parametro_temp.getParametro());
            creaCodaMonitoraggio(Parametro_temp, CompOgg, O);
        }
    }

    //GESTISCE ALLARMI TRAMITE LA CLASSE ParametroObservable (PATTERN OBSERVER) CHE CREERÀ UNA CODA DI SENSORI DA GESTIRE
    private void creaCodaMonitoraggio(Parametri_Casa_Domotica P, Comportamento_Oggetti Comport, Oggetti_Casa_Domotica Ogg) throws SQLException {
        M_sql.CaricaSensoriParametro(P);
        BufferQueryMonit.setAll(M_sql.getBufferQuerySensMonitPar());

        //CREO PARAMETRO DA OSSERVARE(OBSERVABLE)
        ParametroObservable parametroObs = new ParametroObservable(P);

        //VENGONO CREATI TANTI OSSERVATORI QUANTI SONO I SENSORI MONITORAGGIO DELLO STESSO PARAMETRO(OBSERVER)
        for (SensoreMonitoraggio Sensor : BufferQueryMonit) {
            SensoreMonitoraggioObserver SensObs = new SensoreMonitoraggioObserver(Sensor,parametroObs);
            parametroObs.addObserver(SensObs);
        }

        //IL PARAMETRO VERRA' INCREMENTATO O DECREMENTATO A SECONDA DEL TIPO DI OGGETTO  E SE VIENE ACCESO O SPENTO
        parametroObs.setValoreParametro(Comport.getValore_consumo(), Comport.getTipo_Comportamento(), Ogg);

        //UNA VOLTA SCATTATI I SENSORI CON IL METODO NOTIFYALL, CARICHIAMO LA CODA DI SENSORI PER LA RISOLUZIONE DEGLI ALLARMI
        Gestione_Coda.addAll(parametroObs.getCoda_Sensori());

        //AGGIORNA LA MODIFICA DEL VALORE SUL DATABASE
        V_sql.modificaValore(P, parametroObs.getValoreParametro());
        V_sql.caricaParametri();
        Tabella_Parametri.setItems(V_sql.getBufferQueryParametri());

        gestisci_UscitaCoda(parametroObs.getCoda_Sensori(), Ogg);
    }

    //GESTISCE I SENSORI MONITORAGGIO MAN MANO CHE ESCONO DALLA CODA
    private void gestisci_UscitaCoda(Queue<Observer> Coda, Oggetti_Casa_Domotica Ogg) throws SQLException {

        //ESTRAZIONE DALLA CODA DI SENSORI SCATTATI
        while (!Coda.isEmpty()) {
            //IL SENSORE MONITORAGGIO ESCE DALLA CODA
            SensoreMonitoraggioObserver Obs = (SensoreMonitoraggioObserver) Coda.poll();
            SensoreMonitoraggio M = Obs.getSensoreMonitoraggio();
            setAllarmi(M.allarmeSuonato());
            M_sql.DataAllarme(M);

            //GLI OGGETTI DELLA CASA VENGONO SPENTI IN CASO DI EMERGENZA
            O_sql.caricaOggetti();
            BufferQueryOggetti.setAll(O_sql.getBufferQueryOggetti());
            for(Oggetti_Casa_Domotica Oggetto: BufferQueryOggetti){
                O_sql.interruttoreOggetti(Oggetto,0);
                setAllarmi(Oggetto.getNome() + " È STATO SPENTO FORZATAMENTE PER: " + M.allarmeSuonato());
            }
            O_sql.caricaOggetti();
            Tabella_Oggetti.setItems(O_sql.getBufferQueryOggetti());

            //VENGONO CARICATI I SUOI SENSORI INTERVENTO
            I_sql.caricaSensoriCollegati(M);
            BufferQueryInterv.setAll(I_sql.getBufferQuerySensInterv_Coll());

            //VENGONO GESTITI I SENSORI INTERVENTO
            gestione_AllarmiIntervento(M);
        }
    }

    //PER OGNI SENSORE MONITORAGGIO USCITO, VENGONO ATTIVATI ED ESEGUITI TUTTI I SENSORI INTERVENTO
    private void gestione_AllarmiIntervento(SensoreMonitoraggio M) throws SQLException {
        Parametri_Casa_Domotica P;
        int Sec_Max;

        //SCORRE TUTTI I SENSORI INTERVENTO FINO ALLA RISOLUZIONE DELL'EMERGENZA
        for (SensoreIntervento Sens : BufferQueryInterv) {

            //ATTIVA TUTTI I SENSORI INTERVENTO COINVOLTI NELL'EMERGENZA
            I_sql.interruttoreSensore(Sens, 1);
            I_sql.dataAllarme(Sens);
            Sens.setDataAllarme(I_sql.caricaSensoreInterv(Sens).getDataAllarme());
            Allarmi.add(Sens.allarmeSuonato() + " DATA ALLARME: " + Sens.getDataAllarme());
            Table_AllarmiSensor.setItems(Allarmi);

            if (Sens.getComportamento_Azione().contains("INCREMENTA")) {
                Sec_Max=M.getValore_Positivo()-M.getValore_Critico()/BufferQueryInterv.size();
                P = V_sql.CaricaParametroComportamento(M.getParametro_riferimento());
                GesioneIncrementoParametri(P, M, Sec_Max);
                //AGGIORNAMENTO LOG EMERGENZA TERMINATA

            } else if (Sens.getComportamento_Azione().contains("DECREMENTA")) {
                Sec_Max= M.getValore_Critico() - M.getValore_Positivo() / BufferQueryInterv.size();
                P = V_sql.CaricaParametroComportamento(M.getParametro_riferimento());
                GestioneDecrementoParametri(P, M, Sec_Max);
                //AGGIORNAMENTO LOG EMERGENZA TERMINATA
            }

        }

        P = V_sql.CaricaParametroComportamento(M.getParametro_riferimento());
        //I SENSORI UTILIZZATI VENGONO SPENTI
        spegniSensoriIntervento(P, M);
    }

    //METODO PER LO SPEGNIMENTO DEI SENSORI INTERVENTO CHE HANNO RISOLTO L'EMERGENZA
    private void spegniSensoriIntervento(Parametri_Casa_Domotica P, SensoreMonitoraggio M) throws SQLException {
        //SE L'ALLARME È CESSATO, ALLORA SPEGNE I SENSORI INTERVENTO
        if ((P.getValore() <= M.getValore_Critico() && M.getValore_Positivo() < M.getValore_Critico()) || (P.getValore() > M.getValore_Critico() && M.getValore_Positivo() > M.getValore_Critico())) {
            for (SensoreIntervento Sens : BufferQueryInterv) {
                //SPEGNE TUTTI I SENSORI DOPO CHE L'EMERGENZA È TERMINATA
                I_sql.interruttoreSensore(Sens, 0);
                setAllarmi(Sens.getNome() + " SI SPEGNE");
            }
        }
    }
    //GESTIONE PER L'INCREMENTO DEL VALORE DEI PARAMETRI DA PARTE DEI SENSORI INTERVENTO
    private void GesioneIncrementoParametri(Parametri_Casa_Domotica P, SensoreMonitoraggio M, int Sec_Max) throws SQLException {
        double ValIncr;
        int sec = 0;

        //FIN QUANDO NON RIPORTA IL PARAMETRO AD UN VALORE POSITIVO
        while (P.getValore() < M.getValore_Positivo() && sec < Sec_Max) {
            P = V_sql.CaricaParametroComportamento(M.getParametro_riferimento());

            ValIncr = P.getValore() + 10.0;
            System.out.println(ValIncr);
            if (ValIncr >= M.getValore_Positivo()) {
                ValIncr = Double.valueOf(M.getValore_Positivo());
            }

            V_sql.modificaValore(P, ValIncr);

            //RICARICA IL PARAMETRO MODIFICATO
            P = V_sql.CaricaParametroComportamento(M.getParametro_riferimento());
            V_sql.caricaParametri();
            Tabella_Parametri.setItems(V_sql.getBufferQueryParametri());

            logAggiornamento(P.getParametro(), P.getValore(), 2, "STA AUMENTANDO");
            Table_AggiornVal.setItems(AggiornamentoValori);
            sec++;
        }
    }

    //GESTIONE PER IL DECREMENTO DEL VALORE DEI PARAMETRI DA PARTE DEI SENSORI INTERVENTO
    private void GestioneDecrementoParametri(Parametri_Casa_Domotica P, SensoreMonitoraggio M, int Sec_Max) throws SQLException {
        double ValDecr;
        int sec = 0;

        //FIN QUANDO NON RIPORTA IL PARAMETRO AD UN VALORE POSITIVO
        while (P.getValore() > M.getValore_Positivo() && sec < Sec_Max) {
            P = V_sql.CaricaParametroComportamento(M.getParametro_riferimento());
            ValDecr = P.getValore() - 10;
            if (P.getValore() < M.getValore_Positivo()) {
                ValDecr = Double.valueOf(M.getValore_Positivo());
            }
            if (ValDecr <= 0) {
                ValDecr = 0.0;
            }

            V_sql.modificaValore(P, ValDecr);

            //RICARICA IL PARAMETRO MODIFICATO
            P = V_sql.CaricaParametroComportamento(M.getParametro_riferimento());
            V_sql.caricaParametri();
            BufferQueryParametri.setAll(V_sql.getBufferQueryParametri());
            Tabella_Parametri.setItems(BufferQueryParametri);

            //AGGIORNAMENTO DEL LOG PER L'AGGIORNAMENTO DELLE VARIABILI
            logAggiornamento(P.getParametro(), P.getValore(), 2, "SI STA RIDUCENDO");
            Table_AggiornVal.setItems(AggiornamentoValori);
            sec++;
        }
    }


    public void logout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/Login.fxml"));
        Parent tableViewParent = loader.load();
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Login");
        window.setScene(new Scene(tableViewParent));
        window.show();
    }

    @FXML
    private void setTextAcc(TextField I, TextField N, TextField L, TextField E, TextField U, TextField P) {
        I.setText(A.getIdAccount());
        N.setText(A.getNome());
        L.setText((A.getCognome()));
        E.setText(A.getEmail());
        U.setText(A.getUsername());
        P.setText(A.getPassword());
    }

    //AGGIORNAMENTO DEL LOG PER L'AGGIORNAMENTO DELLE VARIABILI
    private void logAggiornamento(String Par, Double Val, int Num_Sec, String Comport) {
        PauseTransition pause = new PauseTransition(Duration.seconds(Num_Sec));

        pause.setOnFinished(event ->
                AggiornamentoValori.add(Par + " " + Comport + " " + Val)
        );
        pause.play();
    }

    public void clearSensoriScatt(ActionEvent actionEvent) {
        Allarmi.clear();
        Table_AllarmiSensor.setItems(Allarmi);
    }

    public void clearAggiornamentoVal(ActionEvent actionEvent) {
        AggiornamentoValori.clear();
        Table_AggiornVal.setItems(AggiornamentoValori);
    }

}
