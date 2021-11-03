package FactoryMethod;

import javafx.beans.property.SimpleIntegerProperty;

//CLASSE "SensoreMonitoraggio CHE ESTENDE "SensoreGenerico" E RAPPRESENTA I SENSORI ADIBITI AL MONITORAGGIO DELLA CASA
public class SensoreMonitoraggio extends SensoreGenerico {

    private SimpleIntegerProperty Valore_Positivo;
    private SimpleIntegerProperty Valore_Medio;
    private SimpleIntegerProperty Valore_Critico;

    public SensoreMonitoraggio(String N, int S,String Data_C,String Data_A,String P_Rif, int V_Pos, int V_Med, int V_Crit) {
        super(N,S,Data_C,Data_A,P_Rif);
        Valore_Positivo=new SimpleIntegerProperty(V_Pos);
        Valore_Medio=new SimpleIntegerProperty(V_Med);
        Valore_Critico=new SimpleIntegerProperty(V_Crit);

    }
    //METODO EREDITATO DA "SensoreGenerico" CHE PERMETTE DI MOSTRARE L'ALLARME
    @Override
    public String allarmeSuonato() {
        return "SENSORE MONITORAGGIO "+this.getNome()+" SCATTATO";
    }

    public int getValore_Positivo() {
        return Valore_Positivo.get();
    }

    public void setValore_Positivo(int valore_Positivo) {
        this.Valore_Positivo.set(valore_Positivo);
    }

    public int getValore_Medio() {
        return Valore_Medio.get();
    }

    public void setValore_Medio(int valore_Medio) {
        this.Valore_Medio.set(valore_Medio);
    }

    public int getValore_Critico() {
        return Valore_Critico.get();
    }

    public void setValore_Critico(int valore_Critico) {
        this.Valore_Critico.set(valore_Critico);
    }
}
