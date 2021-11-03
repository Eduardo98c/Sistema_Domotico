package FactoryMethod;

//CLASSE "CONCRETE_CREATOR" DEL PATTERN FACTORY METHOD
// CHE I OCCUPA DI CREARE IL "PRODUCT" SENSORE INTERVENTO
public class Creator_Sensore_Intervento implements Creator{

    private Sensore Install;

    public Creator_Sensore_Intervento(String N,int S,String Data_C,String Data_A,String P_rif,String DescrAzione,String ComportAzione){
       Install=new SensoreIntervento(N,S,Data_C,Data_A,P_rif,DescrAzione,ComportAzione);
       getSensore();
    }

    //METODO FACTORY DEL PATTERN
    @Override
    public SensoreGenerico getSensore() {
        return (SensoreIntervento) Install;
    }
}
