package PatternProxy;

import java.sql.Connection;

public class DB_Proxy_Singleton_Connection implements IClient{
    //UNICA ISTANZA DELLA CLASSE
    private static DB_Proxy_Singleton_Connection DB_instance=null;
    private Connection Conn;

    protected DB_Proxy_Singleton_Connection(){}
    //METODO STATICO ACCESSIBILE GLOBALMENTE CHE RITORNA L'UNICA ISTANZA DELLA CLASSE
    private static DB_Proxy_Singleton_Connection createIstance(){
        if(DB_instance == null){
            DB_instance=new DB_Proxy_Singleton_Connection();
            DB_RealAccessConnection db_access = new DB_RealAccessConnection();

            //ISTANZA SINGLETON A CUI VIENE DELEGATO IL COMPITO DI FAR ESEGUIRE
            // LA CONNESSIONE AL DATABASE TRAMITE L'OGGETTO ORIGINALE DB_REAL_ACCESS
            DB_instance.Conn= db_access.Start_Connection();
            System.out.println("Connessione ottenuta");
        }
        return DB_instance;
    }
    @Override
    public DB_Proxy_Singleton_Connection getInstance(){
        return createIstance();
    }

    //METODO EREDITATO DALL'INTERFACCIA PROXY CHE RITORNA LA CONNESSIONE AL DATABASE
    @Override
    public Connection returnConnection(){
        if(DB_instance != null)
            return this.Conn;
        else{
            System.out.println("Non è stata creata nessuna connessione\n");
            return null;
        }
    }
}
