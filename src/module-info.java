module Sistema.Domotico {
    
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.java;


    opens Casa_Domotica to javafx.base;
    opens FactoryMethod to javafx.base;
    opens Gestione_Sensori to javafx.base;
    opens sample;
     
}