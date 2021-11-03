package PatternProxy;

import java.sql.Connection;

//INTERFACCIA DEL PATTERN PROXY PER GESTIRE L'ACCESSO ALL'OGGETTO ORIGINALE
public interface IClient{
    Connection returnConnection();
    DB_Proxy_Singleton_Connection getInstance();
}
