package ObserverPattern;

import FactoryMethod.SensoreMonitoraggio;

//CLASSE DI SENSORI MONITORAGGIO OSSERVATORI CHE SI OCCUPERANNO DI MONITORARE IL PARAMETRO
public class SensoreMonitoraggioObserver implements Observer{

    private SensoreMonitoraggio sensoreMonitoraggio;
    private ParametroObservable Parametro;

    public  SensoreMonitoraggioObserver(SensoreMonitoraggio M,ParametroObservable P){
        this.sensoreMonitoraggio=M;
        Parametro=P;
        System.out.println("Nuovo Osservatore per il Parametro: "+Parametro.getParametro().getParametro());
    }

    public SensoreMonitoraggio getSensoreMonitoraggio() {
        return sensoreMonitoraggio;
    }

    //CONTROLLA SE IL RANGE DEL SENSORE È RISPETTATO
    public Boolean isRange(Double valore) {
        int Val_Pos = sensoreMonitoraggio.getValore_Positivo();
        int Val_Crit = sensoreMonitoraggio.getValore_Critico();

        return (valore > Val_Crit && Val_Crit < Val_Pos) || (valore < Val_Crit && Val_Crit > Val_Pos);
    }

    //METODO EREDITATO DALL'INTERFACCIA 'OBSERVER' PER FAR CONTROLLARE AI SENSORI MONITORAGGIO CHE IL VALORE DEI PARAMETRI NON SFORI IL RANGE
    @Override
    public String update(Double Valore) {
        if(!isRange(Valore) && this.getSensoreMonitoraggio().getStato()!=0) {
            System.out.println(getSensoreMonitoraggio().allarmeSuonato());
            return this.getSensoreMonitoraggio().allarmeSuonato();
        }
        return null;
    }
}
