package FactoryMethod;


import javafx.beans.property.SimpleStringProperty;

//CLASSE "SensoreIntervento CHE ESTENDE "SensoreGenerico" E RAPPRESENTA I SENSORI ADIBITI AGLI INTERVENTI NELLA CASA
public class SensoreIntervento extends SensoreGenerico {
    SimpleStringProperty Azione_Eseguita;
    SimpleStringProperty Comportamento_Azione;

    public SensoreIntervento(String N,int S,String Data_C,String Data_A,String P_rif,String Azione,String Comport) {
        super(N,S,Data_C,Data_A,P_rif);
        Azione_Eseguita=new SimpleStringProperty(Azione);
        Comportamento_Azione=new SimpleStringProperty(Comport);
    }

    //METODO EREDITATO DA "SensoreGenerico" CHE PERMETTE DI MOSTRARE L'ALLARME
    @Override
    public String allarmeSuonato() {
        return "SENSORE INTERVENTO: "+this.getNome()+" SCATTATO "+getComportamento_Azione()+" "+getParametro_riferimento();
    }

    public String getAzione_Eseguita() {
        return Azione_Eseguita.get();
    }

    public void setAzione_Eseguita(String azione_Eseguita) {
        this.Azione_Eseguita.set(azione_Eseguita);
    }

    public String getComportamento_Azione() {
        return Comportamento_Azione.get();
    }

    public void setComportamento_Azione(String comportamento_Azione) {
        this.Comportamento_Azione.set(comportamento_Azione);
    }
}
