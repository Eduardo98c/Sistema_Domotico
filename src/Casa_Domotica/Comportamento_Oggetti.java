package Casa_Domotica;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

//CLASSE PER I COMPORTAMENTI DEGLI OGGETTI DELLA CASA DOMOTICA
public class Comportamento_Oggetti {
    private final SimpleStringProperty Seriale_OggettoRif;
    private final SimpleStringProperty ParametroComport;
    private final SimpleStringProperty Tipo_Comportamento;
    private SimpleDoubleProperty Valore_consumo;

    public Comportamento_Oggetti(String S,String P, String T, Double V){
        Seriale_OggettoRif=new SimpleStringProperty(S);
        ParametroComport=new SimpleStringProperty(P);
        Tipo_Comportamento=new SimpleStringProperty(T);
        Valore_consumo=new SimpleDoubleProperty(V);
    }

    public String getSeriale_OggettoRif() {
        return Seriale_OggettoRif.get();
    }

    private void setSeriale_OggettoRif(String seriale_OggettoRif) {
        this.Seriale_OggettoRif.set(seriale_OggettoRif);
    }

    public String getParametroComport() {
        return ParametroComport.get();
    }

    private void setParametroComport(String parametroComport) {
        this.ParametroComport.set(parametroComport);
    }

    public String getTipo_Comportamento() {
        return Tipo_Comportamento.get();
    }

    private void setTipo_Comportamento(String tipo_Comportamento) {
        this.Tipo_Comportamento.set(tipo_Comportamento);
    }

    public double getValore_consumo() {
        return Valore_consumo.get();
    }

    private void setValore_consumo(double valore_consumo) {
        this.Valore_consumo.set(valore_consumo);
    }

}
