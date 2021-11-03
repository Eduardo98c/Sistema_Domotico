package Accesso_Account;

import Accesso_Account.Exception.*;

import javax.swing.*;

public abstract class Account {

    private String Nome;      //max 16 caratteri
    private String Cognome;   //max 16 caratteri
    private String Email;     //max 30 caratteri
    private String Username;  //max 16 caratteri
    private String Password;  //max 16 caratteri
    private String IdAccount; //9 caratteri

    private static final Prototype_AccountError Err=new Prototype_AccountError();

    public Account(String Nome,String Cognome,String Email,String Username,String Password){
        setNome(Nome);
        setCognome(Cognome);
        setEmail(Email);
        setUsername(Username);
        setPassword(Password);
    }

    protected abstract void generateId();

    public String getIdAccount() {
        return this.IdAccount;
    }
    protected void setIdAccount(String IdAccount){
        this.IdAccount=IdAccount;
    }
    public String getNome(){
        return Nome;
    }

    protected void setNome(String N){
        String ErrType= Err.errorName(N);
        if(ErrType == null){
            this.Nome=N;
        }
        else{
            JOptionPane.showMessageDialog(null,ErrType);
        }
    }
    protected void setCognome(String C) {
       String ErrType=Err.errorLastname(C);
        if(ErrType == null){
            this.Cognome=C;
        }
        else{
            JOptionPane.showMessageDialog(null,ErrType);
        }
    }
    protected void setEmail(String E) {
        String ErrType=Err.errorEmail(E);
        if(ErrType == null){
            this.Email=E;
        }
        else{
            JOptionPane.showMessageDialog(null,ErrType);
        }
    }
    protected void setUsername(String U) {
        String ErrType=Err.errorUser(U);
        if(ErrType == null){
            this.Username=U;
        }
        else{
            JOptionPane.showMessageDialog(null,ErrType);
        }
    }
    protected void setPassword(String P) {
        String ErrType=Err.errorPass(P);
        if(ErrType == null){
            this.Password=P;
        }
        else{
            JOptionPane.showMessageDialog(null,ErrType);
        }
    }
    public String getCognome() {
        return Cognome;
    }

    public String getEmail() {
        return Email;
    }



    public String getUsername() {
        return this.Username;
    }


    public String getPassword() {
        return this.Password;
    }

}
