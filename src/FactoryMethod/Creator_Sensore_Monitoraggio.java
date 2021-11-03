package FactoryMethod;

//CLASSE "CONCRETE_CREATOR" DEL PATTERN FACTORY METHOD
// CHE I OCCUPA DI CREARE IL "PRODUCT" SENSORE MONITORAGGIO
public class Creator_Sensore_Monitoraggio implements Creator{

    private Sensore Install;

    public Creator_Sensore_Monitoraggio(String N,int Stato,String Data_C,String Data_A,String P_Mon,int V_Pos, int V_Med, int V_Crit){

        Install=new SensoreMonitoraggio(N,Stato,Data_C,Data_A,P_Mon,V_Pos,V_Med,V_Crit);
        getSensore();
    }

    //METODO FACTORY DEL PATTERN
    @Override
    public SensoreGenerico getSensore() {
        return (SensoreMonitoraggio) Install;
    }
}
