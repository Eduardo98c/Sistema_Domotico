package Accesso_Account;

import java.util.Random;
import java.util.Vector;

public class Account_Attivato extends Account{

    private final Random RNG=new Random(60000);

    public Account_Attivato(String Nome, String Cognome, String Email, String Username, String Password) {
        super(Nome, Cognome, Email, Username, Password);

        if (super.getIdAccount() == null)
            generateId();

    }
    //COSTRUTTORE PER IL CARICAMENTO DEL DATABASE E CARICARE L'ID SENZA RICREARLO
    public Account_Attivato(String IdAccount, String Nome, String Cognome, String Email, String Username, String Password) {
        super(Nome, Cognome, Email, Username, Password);
        setIdAccount(IdAccount);
    }
    public String getIdAccount(){
        return super.getIdAccount();
    }

    @Override
    public void generateId() {
        String Code=this.getNome().substring(2,3)+this.getNome().length()/3+this.getCognome().charAt(0)+this.getCognome().length()/2+this.getEmail().charAt(0)+this.getUsername().charAt(4)+this.getUsername().length()/3+this.getUsername().length()/2+((this.getEmail().length()-this.getUsername().length())/3);
        StringBuilder UniqueCode = new StringBuilder();
        int Max_seed=9;
        int Rand;

        Vector Arr_temp= new Vector(9);

        for(int j=0; j<9; j++) {
            Arr_temp.add(Code.charAt(j));
        }

        for(int i=0;i<9;i++){
            Rand=RNG.nextInt(Max_seed);
            UniqueCode.append(Arr_temp.elementAt(Rand));
            Arr_temp.remove(Rand);
            Max_seed--;
        }
        setIdAccount(UniqueCode.toString());
    }
}
