package ObserverPattern;

import java.util.ArrayList;

//CLASSE GENERICA PER TUTTI I SUBJECT
public abstract class Subject {

    //LISTA DI OSSERVATORI(SENSORI MONITORAGGIO) CHE CONTROLLERANNO I VALORI DEI PARAMETRI
    private ArrayList<Observer> observerList;

    //COSTRUTTORE CHE LE CLASSI FIGLIE UTILIZZERANNO PER INIZIALIZZARE L'ARRAY DI OBSERVER
    public Subject(){
        observerList=new ArrayList<>();
    }

    //METODI CHE VERRANNO EREDITATE DALLE CLASSI FIGLIE
    public abstract void addObserver(Observer observer);
    public abstract void removeObserver(Observer observer);
    public abstract void notifyObservers();

    public ArrayList<Observer> getObserverList() {
        return observerList;
    }

    public void setObserverList(ArrayList<Observer> observerList) {
        this.observerList = observerList;
    }
}
