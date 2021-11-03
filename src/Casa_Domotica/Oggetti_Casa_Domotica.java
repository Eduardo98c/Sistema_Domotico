package Casa_Domotica;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


//CLASSE PER GLI OGGETTI NELLA CASA DOMOTICA
public class Oggetti_Casa_Domotica {

    private final SimpleStringProperty Seriale;
    private final SimpleStringProperty Nome;
    private final SimpleStringProperty Marca;
    private  SimpleIntegerProperty Stato;
    private  SimpleStringProperty StringaStato;

    public Oggetti_Casa_Domotica(String S,String N,String M,int State){
       Seriale =new SimpleStringProperty(S);
       Nome =new SimpleStringProperty(N);
       Marca =new SimpleStringProperty(M);
       Stato =new SimpleIntegerProperty(State);

       if(Stato.get() == 0){
           StringaStato=new SimpleStringProperty("SPENTO");
       }
       else if(Stato.get() == 1){
           StringaStato=new SimpleStringProperty("ACCESO");
       }
    }

    public String getSeriale() {
        return Seriale.get();
    }

    public void setSeriale(String seriale) {
        this.Seriale.set(seriale);
    }

    public String getNome() {
        return Nome.get();
    }

    public void setNome(String nome) {
        this.Nome.set(nome);
    }

    public String getMarca() {
        return Marca.get();
    }

    public void setMarca(String marca) {
        this.Marca.set(marca);
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

}
