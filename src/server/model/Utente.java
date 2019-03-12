/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package server.model;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Giacomo
 */
public class Utente {
    
    private final String nome;
    private final String password;
    private ArrayList<String> documentiCreati;
    private ArrayList<String> documenti;     //documenti che puo editare
    private Boolean online;
    private String inEdit="";    //documento che sta editando
    private int inEditSec = -1;  //sezione del documento
    private DataOutputStream toClientInvites; //notifiche inviti e lista documenti
    
    public Utente(String nome, String password)
    {
        this.nome = nome;
        this.password = password;
        documentiCreati = new ArrayList<String>();
        documenti = new ArrayList<String>();
        this.online=false;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public Boolean isOnline()
    {
        return online;
    }
    
    public void setOnline(Boolean val)
    {
        this.online = val;
    }
    
    public void addDocCreato(String nome)
    {
        if(!documentiCreati.contains(nome))
           documentiCreati.add(nome);
        addDoc(nome);
    }
    
    public void addDoc(String nome)
    {
        if(!documenti.contains(nome))
            documenti.add(nome);
    }
    
    public ArrayList<String> getDocumenti()
    {
        return (ArrayList<String>)documenti.clone();
    }
    
    public String getInEdit()
    {
        return inEdit;
    }
    
    public int getInEditSec()
    {
        return inEditSec;
    }
    
    public void setInEdit(String s, int sec)
    {
        inEdit=s;
        inEditSec = sec;
    }
    
    public void setInvitesStream(DataOutputStream stream)
    {
        this.toClientInvites = stream;
    }
    
    public DataOutputStream getInvitesStream()
    {
        return toClientInvites;
    }
    
    
    
    
    
    
}
