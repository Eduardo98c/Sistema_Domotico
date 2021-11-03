package PatternProxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB_RealAccessConnection implements IClient {
    //DATI FONDAMENTALI PER EFFETTUARE LA CONNESSIONE AL DATABASE
    private final static String URL = "jdbc:mysql://localhost:3306/Db_Sensori?&useTimezone=true&serverTimezone=UTC";
    private final static String USER="Eddy";
    private final static String PASSWORD="password";
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private Connection Conn;

    DB_RealAccessConnection(){}

    //METODO CHE EFFETTUA LA CONNESSIONE AL DATABASE MA CHE PUO' ESSERE RESTITUITO ESTERNAMENTE SOLO DALLA CLASSE DELEGATA
    protected Connection Start_Connection()  {
        try {
             Class.forName(DRIVER);
             this.Conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DB_RealAccessConnection.class.getName()).log(Level.SEVERE,null,ex);
        }
        return this.Conn;
    }
    //METODI EREDITATI DALL'INTERFACCIA PROXY CHE IN QUESTO CASO NON POTRANNO RESTITUIRE NULLA
    //POICHE' LA CLASSE NON DEVE ESSERE ACCESSIBILE DIRETTAMENTE
    @Override
    public Connection returnConnection(){
        System.out.println("Accesso vietato a questa risorsa");
        return null;
    }
    @Override
    public DB_Proxy_Singleton_Connection getInstance() {
        System.out.println("Accesso vietato a questa risorsa");
        return null;
    }
}
