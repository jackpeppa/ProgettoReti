/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package server.model;

import common.Configuration;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Giacomo
 */
public class DataBase {
    
    private static DataBase instance =null;
    private static ConcurrentHashMap<String, Utente> utenti;
    private static ConcurrentHashMap<String, Documento> documenti;
    private static HashSet<String> multicastAddresses;   //contiene ip multicast usati(1 per documento)
    
    
    private DataBase()
    {}
    
    public static DataBase getInstance()
    {
        if(instance==null)
        {
            utenti =new ConcurrentHashMap<String,Utente>();
            documenti = new ConcurrentHashMap<String, Documento>();
            multicastAddresses = new HashSet<String>();
            instance = new DataBase();
        }
        return instance;
    }
    
    
    public static Boolean addUtente(String nome,String password)
    {
        Utente u = new Utente(nome,password);
        
        return (utenti.putIfAbsent(nome, u)==null);  //per sincronizzazione
        
    }
    
    public static Boolean removeUtente(String nome)
    {
        return (utenti.remove(nome) != null);
    }
    
    public static Boolean setOnline(String nome,String password)
    {
        Utente u = utenti.get(nome);
        if(u!= null)
        {
            synchronized(u)
            {
                String pwd = u.getPassword();
                if(pwd != null && pwd.equals(password))
                {
                   if(u.isOnline())
                       return false;
                   u.setOnline(Boolean.TRUE);
                   return true;
                }
            }
        }
     return false;
        
    }
    
    public static Boolean setOffline(String nome)
    {
        Utente u = utenti.get(nome);
        if(u!= null)
        {
            synchronized(u)
            {
                u.setOnline(Boolean.FALSE);
            }
            
            return true;
        }
        return false;
    }
    //1 se nomeducumento o sezione non esistono, 2 se è già in editing, 0 se l'utente lo può editare, 3 se l'utente non è ammesso
    public static int editDocumento(String nomeUtente,String nomeDocumento,int sezione)
    {
        Documento d = documenti.get(nomeDocumento);
        Utente u = utenti.get(nomeUtente);
        
        if(d==null || u==null)
            return 1;
        int resp;
        synchronized(d)
        {
            resp= d.richiediSezione(sezione, nomeUtente);
        }
        if(resp == 1)
            return 1;
        else if(resp == 2)
            return 2;
        else if(resp == 3)
            return 3;
        synchronized(u)
        {
            u.setInEdit(nomeDocumento,sezione);
        }
        
        return 0;                 
    }
    
    
    public static void endEditDocumento(String nome, int sezione)
    {
        Documento d = documenti.get(nome);
        if(d!=null)
        {
            synchronized(d)
            {
                d.rilasciaSezione(sezione);
            }
        }
    }
    
    public static void rilasciaSezioni(String nomeUtente)    //rilascia la sezione che sta editando quell'utente
    {
        Utente u = utenti.get(nomeUtente);
        if(u==null)
            return;
        String nomeDoc;
        int sezione;
        synchronized(u)
        {
            nomeDoc = u.getInEdit();
            sezione = u.getInEditSec();
            if(sezione== -1)
                return;
            u.setInEdit("", -1);   //valori per il non in editing
        }
        if(sezione!=-1)
            endEditDocumento(nomeDoc, sezione);
        
    }
    
    
    public static Boolean addDocumento(String nome, String creatore, int numSez)
    {
        try{Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+nome);}catch(Exception e){return false;}  //verifico che il nome sia valido
        Documento newDoc = new Documento(nome, creatore, numSez);
        
        Utente u = utenti.get(creatore);
        
        if(documenti.putIfAbsent(nome, newDoc)==null)
        {
            synchronized(u)
            {
                u.addDocCreato(nome);
            }
            //genero indirizzo multicast
            Random r = new Random();
            InetAddress inetAddr = null;
            String addr= null;
            synchronized(multicastAddresses)
            {
                do{
                    addr = "239." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
                    try{
                    inetAddr = InetAddress.getByName(addr);
                    }catch(UnknownHostException ex){ex.printStackTrace();}

                }while(!inetAddr.isMulticastAddress() || multicastAddresses.contains(addr));
                multicastAddresses.add(addr);
            }
            
            synchronized(newDoc)
            {
                newDoc.setMulticastAddr(addr);
            }
            return true;
        }
        return false;
    }
    
    public static String getMulticastAddr(String nomeDoc)
    {
        Documento doc = documenti.get(nomeDoc);
        if(doc == null) return null;
        return doc.getMulticastAddr();
    }
    
    //restituisce lista documenti modificabili dall'utente
    public static String getList(String nome)
    {
        Utente u = utenti.get(nome);
        ArrayList<String> docs = null;
        synchronized(u)
        {
            docs = u.getDocumenti();
        }
        
        Documento d = null;
        StringBuilder builder = new StringBuilder();
        String creatore = null;
        ArrayList<String> ammessi = null;
        
        for(String nomeDoc : docs)
        {
            d = documenti.get(nomeDoc);
            int numSez;
            builder.append("Nome Documento: "+nomeDoc+" \n");
            synchronized(d)
            {
                creatore = d.getCreatore();
                ammessi = d.getAmmessi();
                numSez = d.getNumOfSections();
            }
            builder.append("Creatore: "+creatore+"\n");
            builder.append("Numero sezioni : "+numSez+"\n");
            builder.append("Collaboratori : ");
            for(String ammesso:ammessi)
            {
                builder.append(ammesso+", ");
            }
            builder.append("\n-------------------------------------------------\n\n");       
        }
        return new String(builder);
    }
    
    //stringa con stato attuale delle sezioni(se in editing, e da chi), null se il doc non esiste
    public static String getListOfSectionsInEditing(String docName)
    {
        Documento d = documenti.get(docName);
        if(d==null)
            return null;
        ArrayList<String> list;
        synchronized(d)
        {
            list = d.getEditors();
        }
        StringBuilder b = new StringBuilder();
        b.append("Stato sezioni del Documento "+docName+" : ");
        int i=0;
        for(String s: list)
        {
            String temp;
            if(s.equals(""))
            {
                temp = (" "+i+" : "+"free, ");
            }
            else
            {
                temp = (" "+i+" : "+s+" sta editando, ");
            }
            b.append(temp);
            i++;
        }
        return new String(b);
    }
    //null se il doc non esiste o la sezione non esiste, stringa vuota se nessuno sta editando, nome utente altrimenti
    public static String getEditor(String docName, int section)
    {
        Documento d = documenti.get(docName);
        String temp;
        if(d==null)
            return null;
        synchronized(d)
        {
            int numOfSections = d.getNumOfSections();
            if(section>=numOfSections || section < 0)
                return null;
            temp = d.getEditor(section);
        }
        return temp;
        
    }
    
    //true -> username ora puo editare il documento
    public static Boolean addAmmesso(String nomeDoc, String username, String creatore)
    {
        Documento doc = documenti.get(nomeDoc);
        Utente user = utenti.get(username);
        
        if(user==null || doc==null)
            return false;
        synchronized(doc)
        {
            if(!((doc.getCreatore()).equals(creatore)))
                    return false;
            doc.addAmmesso(username);
        }
        
        synchronized(user)
        {
            user.addDoc(nomeDoc);
        }
        
        return true;
    }
    
    //se non è online ritorna null
    public static DataOutputStream getInvitesStream(String username)
    {
        Utente u = utenti.get(username);
        if(u==null)
            return null;
        if(!(u.isOnline()))
            return null;
        return u.getInvitesStream();
    }
    
    public static void setInvitesStream(String username, DataOutputStream out)
    {
        Utente u = utenti.get(username);
        if(u==null)
            return;
        u.setInvitesStream(out);
    }
    
    public static int getNumOfSections(String docName)
    {
        Documento doc = documenti.get(docName);
        if(doc==null)
            return -1;
        int n;
        synchronized(doc)
        {
            n = doc.getNumOfSections();
        }
        return n;
    }
    
}
