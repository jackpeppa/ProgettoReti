/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.util.ArrayList;

/**
 *
 * @author Giacomo
 */
public class Documento {
    private final String nome;
    private final String creatore;
    private int numSezioni;
    private ArrayList<String> ammessi;   //ammessi a modificarlo
    private Boolean[] sezioni;  //true->in editing; false->libera
    
    public Documento(String nome, String creatore, int numSezioni)
    {
        this.nome = nome;
        this.creatore = creatore;
        this.ammessi = new ArrayList<String>();
        this.numSezioni = numSezioni;
        this.sezioni = new Boolean[numSezioni];
    }
    
    
    public String getNome()
    {
        return nome;
    }
    
    public String getCreatore()
    {
        return creatore;
    }
    
    public void addAmmesso(String nome)
    {
        if(!ammessi.contains(nome))
            ammessi.add(nome);
    }
    
    public Boolean richiediSezione(int n, String nome)
    {
        if(n>numSezioni || n < 0)
            return false;
        else if(sezioni[n]==Boolean.TRUE)
            return false;
        else if(!(nome.equals(creatore) || ammessi.contains(nome)))
            return false;
        else
        {
            sezioni[n] = Boolean.TRUE;
            return true;
        }
        
    }
    
    public void rilasciaSezione(int n)
    {
        if(n>numSezioni || n<0)
            return;
        sezioni[n]=Boolean.FALSE;
    }
    
    public ArrayList<String> getAmmessi()
    {
        return (ArrayList<String>)ammessi.clone();
    }
    
    
    
    
}
