package Casa_Domotica;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;


//CLASSE PER I PARAMETRI NELLA CASA DOMOTICA
public class Parametri_Casa_Domotica {

    private SimpleStringProperty Parametro;
    private SimpleStringProperty Unita;
    private SimpleDoubleProperty Valore;

    public Parametri_Casa_Domotica(String P, String U, Double V) {
        Parametro=new SimpleStringProperty(P);
        Unita=new SimpleStringProperty(U);
        Valore=new SimpleDoubleProperty(V);
    }

    public String getParametro() {
        return Parametro.get();
    }

    public void setParametro(String parametro) {
        Parametro.set(parametro);
    }

    public String getUnita() {
        return Unita.get();
    }

    public void setUnita(String unita) {
        Unita.set(unita);
    }

    public Double getValore() {
        return Valore.get();
    }

    public void setValore(Double valore) {
        Valore.set(valore);
    }
}