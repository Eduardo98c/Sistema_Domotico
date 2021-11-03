package FactoryMethod;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//CLASSE ASTRATTA CHE IMPLEMENTA L'INTERFACCIA "SENSORE" UTILIZZATA PER IL PATTERN FACTORY METHOD
//CHE RIASSUME LE CARATTERISTICHE COMUNI DI TUTTE LE SOTTOCLASSI
public abstract class SensoreGenerico implements Sensore {
    private final SimpleStringProperty Nome;
    private SimpleIntegerProperty Stato;
    private SimpleStringProperty StringaStato;
    private final SimpleStringProperty DataCreazione;
    private final SimpleStringProperty DataAllarme;
    private final SimpleStringProperty Parametro_riferimento;

    //COSTRUTTORE USATO DALLE SOTTOCLASSI PER INIZIALIZZARE I PARAMETRI DI BASE DEI SENSORI
    public SensoreGenerico(String N,int S,String Data_C,String Data_A,String Par_rif){
        Nome=new SimpleStringProperty(N);
        Stato=new SimpleIntegerProperty(S);
        DataCreazione=new SimpleStringProperty(Data_C);
        DataAllarme=new SimpleStringProperty(Data_A);
        Parametro_riferimento=new SimpleStringProperty(Par_rif);

        if(Stato.get() == 0){
            StringaStato=new SimpleStringProperty("SPENTO");
        }
        else if(Stato.get() == 1){
            StringaStato=new SimpleStringProperty("ACCESO");
        }
    }

    //METODO DA SOVRASCRIVERE NELLE SOTTOCLASSI
    public abstract String allarmeSuonato();

    public String getNome() {
        return Nome.get();
    }

    public void setNome(String nome) {
        this.Nome.set(nome);
    }

    public int getStato() {
        return Stato.get();
    }

    public void setStato(int stato) {
        this.Stato.set(stato);
    }

    public String getStringaStato() {
        return StringaStato.get();
    }

    public void setStringaStato(String stringaStato) {
        this.StringaStato.set(stringaStato);
    }

    public String getDataCreazione() {
        return DataCreazione.get();
    }

    private void setDataCreazione(String dataCreazione) {
        this.DataCreazione.set(dataCreazione);
    }

    public String getDataAllarme() {
        return DataAllarme.get();
    }

    public void setDataAllarme(String dataAllarme) {
        this.DataAllarme.set(dataAllarme);
    }

    public String getParametro_riferimento() {
        return Parametro_riferimento.get();
    }

    public void setParametro_riferimento(String parametro_riferimento) {
        this.Parametro_riferimento.set(parametro_riferimento);
    }

}
