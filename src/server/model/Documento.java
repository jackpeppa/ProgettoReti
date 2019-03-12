/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package server.model;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Giacomo
 */
public class Documento {
    private final String nome;
    private final String creatore;
    private int numSezioni;
    private ArrayList<String> ammessi;   //ammessi a modificarlo
    private ArrayList<String>  editors;  //nome di chi sta editando la sezione i-esima
    private Boolean[] sezioni;  //true->in editing; false->libera
    
    public Documento(String nome, String creatore, int numSezioni)
    {
        this.nome = nome;
        this.creatore = creatore;
        this.ammessi = new ArrayList<String>();
        this.numSezioni = numSezioni;
        this.sezioni = new Boolean[numSezioni];
        this.editors = new ArrayList<String>(numSezioni);
        for(int i =0;i< numSezioni;i++)
            editors.add("");
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
            editors.set(n, nome);
            return true;
        }
        
    }
    
    public void rilasciaSezione(int n)
    {
        if(n>numSezioni || n<0)
            return;
        sezioni[n]=Boolean.FALSE;
        editors.set(n, "");
    }
    
    public ArrayList<String> getAmmessi()
    {
        return (ArrayList<String>)ammessi.clone();
    }
    
    public ArrayList<String> getEditors()
    {
        return (ArrayList<String>)editors.clone();
    }
    
    public String getEditor(int section)
    {
        String temp = editors.get(section);
        if(sezioni[section]== Boolean.FALSE)
            return "";
        return editors.get(section);
    }
    
    public int getNumOfSections()
    {
        return numSezioni;
    }
    
    
    
    
}
