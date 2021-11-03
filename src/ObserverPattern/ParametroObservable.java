package ObserverPattern;

import Casa_Domotica.Oggetti_Casa_Domotica;
import Casa_Domotica.Parametri_Casa_Domotica;

import java.util.*;

//CLASSE CONCRETA PER I PARAMETRI OSSERVATI DAI SENSORI MONITORAGGIO
public class ParametroObservable extends Subject {

    //CODA(FIFO) DI OSSERVATORI PER LA CORRETTA GESTIONE DEGLI ALLARMI
    private Queue<Observer> Coda_Sensori;

    private Parametri_Casa_Domotica Parametro;
    private Double ValoreParametro;
    private Double ValoreIniziale;

    //COSTRUTTORE PER INIZIALIZZARE I PARAMETRI
    public ParametroObservable(Parametri_Casa_Domotica P){
        super();
        Coda_Sensori=new LinkedList<>();
        Parametro=P;
        ValoreParametro=P.getValore();
        ValoreIniziale=P.getValore();
    }

    public Queue<Observer> getCoda_Sensori() {
        return Coda_Sensori;
    }

    public Parametri_Casa_Domotica getParametro() {
        return Parametro;
    }

    public double getValoreParametro() {
        return ValoreParametro;
    }

    //METODO PER SETTARE IL VALORE AGGIORNATO DI UN PARAMETRO E CONTROLLARE TRAMITE I
    //SENSORI MONITORAGGIO(OSSERVATORI) CHE IL PARAMETRO NON SFORI IL RANGE
    public void setValoreParametro(Double val_ParInput, String Tipo_Comportamento, Oggetti_Casa_Domotica Ogg) {

        if( (Tipo_Comportamento.contains("INCREMENTA") && Ogg.getStato() == 0) ||(Tipo_Comportamento.contains("DECREMENTA") && Ogg.getStato() == 1)){
          ValoreParametro=this.getParametro().getValore()+val_ParInput;
          if(ValoreParametro >= 10000){
              ValoreParametro=10000.0;
          }
        }
        else if( (Tipo_Comportamento.contains("DECREMENTA") && Ogg.getStato() == 0) || (Tipo_Comportamento.contains("INCREMENTA") && Ogg.getStato() == 1)){
            ValoreParametro=this.getParametro().getValore()-val_ParInput;
            if(ValoreParametro <= 0){
                ValoreParametro=0.0;
            }
        }
        this.Parametro.setValore(ValoreParametro);
        notifyObservers();

    }

    //AGGIUNGE UN NUOVO OSSERVATORE
    @Override
    public void addObserver(Observer observer) {
        getObserverList().add(observer);
    }

    //RIMUOVE UN OSSERVATORE
    @Override
    public void removeObserver(Observer observer) {
        getObserverList().remove(observer);
    }

    //NOTIFICA LO STATO DEL PARAMETRO A TUTTI I SENSORI E IN CASO DI SFORAMENTO RANGE LI INSERISCE IN UNA CODA
    @Override
    public void notifyObservers() {
        String Allarme_Sensore;
        for(Observer obj: getObserverList()){
            Allarme_Sensore=obj.update(this.getValoreParametro());

            if(Allarme_Sensore != null){
                Coda_Sensori.add(obj);
            }
        }
    }
}
