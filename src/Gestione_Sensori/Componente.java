package Gestione_Sensori;

import javafx.beans.property.SimpleStringProperty;

public class Componente {
   private SimpleStringProperty Nome_Componente;
   private SimpleStringProperty DataCreazione;
   private SimpleStringProperty Descrizione_Azione;

   public Componente(String N,String Data_C,String Descr){
      Nome_Componente=new SimpleStringProperty(N);
      DataCreazione=new SimpleStringProperty(Data_C);
      Descrizione_Azione=new SimpleStringProperty(Descr);
   }

   public String getNome_Componente() {
      return Nome_Componente.get();
   }

   public void setNome_Componente(String nome_Componente) {
      this.Nome_Componente.set(nome_Componente);
   }

   public String getDataCreazione() {
      return DataCreazione.get();
   }

   public void setDataCreazione(String dataCreazione) {
      this.DataCreazione.set(dataCreazione);
   }

   public String getDescrizione_Azione() {
      return Descrizione_Azione.get();
   }

   public void setDescrizione_Azione(String descrizione_Azione) {
      this.Descrizione_Azione.set(descrizione_Azione);
   }

}
