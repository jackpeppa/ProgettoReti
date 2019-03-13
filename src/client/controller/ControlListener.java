/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package client.controller;

import client.view.Editing;
import common.RegistrationInterface;
import java.awt.Color;
import client.view.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import common.Configuration;
import common.Packet;
import common.typePack;
import client.view.Logged;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Giacomo
 */
public class ControlListener implements ActionListener {
    
    private Login login;   //form login
    private Logged logged;   //form principale
    private Editing editing;  //form di editing
    private RegistrationInterface serverInterface;
    private Socket server;
    private Thread listingThread;     //thread inviti
    private Thread chatThread;        //thread chat multicast
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private String username="";          //nome utente
    private SocketChannel serverChannel;   //channel per i files dei documenti
    
    private DatagramSocket chatSocket; //dove spedisco i messaggi della chat
    private InetAddress group;    //gruppo del documento che sto editando
    
    private String inEditingDoc;  //mi salvo il documento che sto editando
    private int inEditingSec;     //relativa sezione del documento
    
    
    public ControlListener(RegistrationInterface serverInterface)
    {
        this.serverInterface = serverInterface;
        login = new Login(this);
        login.show();
    }

    @Override
    public void actionPerformed(ActionEvent e)  {
        try
        {
        switch(e.getActionCommand()){
            
            case "Login":
            {
                String password="";
                Boolean resp = false;

                username=login.getUsernameLog();
                password=login.getPasswordLog();
               
                if(password.equals("") || username.equals("")){
                    login.setlabelLog("CAMPI NON VALIDI", Color.red);
                    break;
                }
                
                server = new Socket(Configuration.SERVER_NAME, Configuration.SERVER_TCP_PORT);
                toServer = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
                fromServer = new DataInputStream(new BufferedInputStream(server.getInputStream()));
         
                Packet req = new Packet(typePack.LOGIN);
                req.addCampo("username", username);
                req.addCampo("password", password);
                
                req.writePacket(toServer);
                
                
                req.readPacket(fromServer);
              
                
                if(req.getType().equals(typePack.OP_OK))
                {
                    serverChannel = SocketChannel.open(new InetSocketAddress(Configuration.SERVER_NAME, Integer.parseInt((String)req.getCampo("filePort"))));
                    login.setlabelLog("CAMPI VALIDI", Color.green);
                    login.dispose();
                    logged = new Logged(this);
                    logged.setTitle(("LOGGED : "+username));
                    logged.show();
                    listingThread = new Thread(new ListingThread(logged, Integer.parseInt((String)req.getCampo("porta"))));
                    listingThread.start();
                    
                }
                else
                {
                    login.setlabelLog("CAMPI NON VALIDI", Color.red);
                    fromServer.close();
                    toServer.close();
                    server.close();
                }
                
                
                break;    
                
                
                
                
            }
            case "Register":
            {
                String username="";
                String password="";
                Boolean resp = false;
                
                username=login.getUsernameReg();
                password=login.getPasswordReg();
                
                if(password.equals("") || username.equals("")){
                    login.setlabelReg("CAMPI NON VALIDI", Color.red);
                    break;
                }
                
                try{
                resp = serverInterface.register(username, password);
                }catch(RemoteException ex){ex.printStackTrace();}
                
                if(resp)
                    login.setlabelReg("REGISTRATO", Color.green);
                else
                    login.setlabelReg("CAMPI NON VALIDI", Color.red);
                
                break;
                
             }
            
            
            case "logout":
            {
                Packet req = new Packet(typePack.LOGOUT);
                req.addCampo("username", username);
                req.writePacket(toServer);
                
                toServer.close();
                fromServer.close();
                server.close();
                listingThread.interrupt();
                
                logged.dispose();
                login = new Login(this);
                login.show();
                
                break;
            }
            
            case "create":
            {
                String nome = logged.getCreateNameField();
                String temp = logged.getCreateNumField();
                if(nome.equals("") || temp.equals("")){
                    logged.setCreateLabel("CAMPI NON VALIDI", Color.red);
                    break;
                }
                int sezioni; 
                try{sezioni=Integer.parseInt(temp);}catch(NumberFormatException ex)
                {
                    logged.setCreateLabel("CAMPO SEZIONI SBAGLIATO", Color.red);
                    break;
                }
                
                if(sezioni < 1)
                {
                    logged.setCreateLabel("CAMPO SEZIONI SBAGLIATO", Color.red);
                    break;
                }
                    
                
                Packet req = new Packet(typePack.CREATE);
                req.addCampo("name", nome);
                req.addCampo("sections",Integer.toString(sezioni));
                
                req.writePacket(toServer);
                
                req.readPacket(fromServer);
                
                if(req.getType().equals(typePack.OP_OK))
                    logged.setCreateLabel("DOCUMENTO CREATO CON SUCCESSO", Color.green);
                else
                    logged.setCreateLabel("SCEGLIERE UN ALTRO NOME", Color.red);
                
                break;
                
            }
            
            case "share":
            {
                String nomeDoc = logged.getShareNameField();
                String nomeUser = logged.getShareUsernameField();
                
                if(nomeDoc.equals("") || nomeUser.equals("")){
                    logged.setShareLabel("CAMPI NON VALIDI", Color.red);
                    break;
                }
                
                Packet req = new Packet(typePack.SHARE);
                req.addCampo("doc", nomeDoc);
                req.addCampo("username", nomeUser);
                
                req.writePacket(toServer);
                
                req.readPacket(fromServer);
                
                typePack tipo = req.getType();
                if(tipo.equals(typePack.OP_OK))
                    logged.setShareLabel("DOCUMENTO CONDIVISO CON SUCCESSO", Color.green);
                else
                    logged.setShareLabel("IMPOSSIBILE CONDIVIDERE IL DOCUMENTO", Color.red);
                    
                break;    
            }
            
            case "show":
            {
                String nomeDoc = logged.getShowNameField();
                String sez = logged.getShowNumField();
                
                if(nomeDoc.equals("")){
                    logged.setShowLabel("INSERIRE NOME DOCUMENTO", Color.red);
                    break;
                }
                
                Packet req;
                if(sez.equals(""))
                {
                    req = new Packet(typePack.SHOW_DOC);
                    req.addCampo("doc", nomeDoc);
                    req.writePacket(toServer);
                    
                    req.readPacket(fromServer);
                    if(req.getType().equals(typePack.OP_ERR))
                    {
                        logged.setShowLabel("DOCUMENTO INESISTENTE", Color.red);
                        break;
                    }
                    
                    String sectionsInEditing = req.getCampo("editors");
                    logged.appendShowTextArea(nomeDoc+" ----> "+sectionsInEditing+"\n\n\n");
                    
                    Long dimension = Long.parseLong(req.getCampo("dim"));
                    
                    receiveDoc(serverChannel, nomeDoc, dimension);
                    logged.setShowLabel("DOCUMENTO SCARICATO CON SUCCESSO", Color.green);
                    break;
                }
                else
                {
                    int sezione;
                    try{sezione = Integer.parseInt(sez);}
                    catch(Exception exc){
                        logged.setShowLabel("SEZIONE NON VALIDA", Color.red);
                        break;
                    }
                    req = new Packet(typePack.SHOW_SEC);
                    req.addCampo("doc", nomeDoc);
                    req.addCampo("section", Integer.toString(sezione));
                    req.writePacket(toServer);
                    
                    req.readPacket(fromServer);
                    if(req.getType().equals(typePack.OP_ERR))
                    {
                        logged.setShowLabel("IL FILE NON ESISTE", Color.red);
                        break;
                    }
                    String editor = req.getCampo("editor");
                    if(editor.equals(""))
                        logged.appendShowTextArea(nomeDoc+" ----> "+"free"+"\n\n\n");
                    else
                      logged.appendShowTextArea(nomeDoc+" ----> "+editor+" sta editando il documento\n\n\n");
                    Long dimension = Long.parseLong(req.getCampo("dim"));
                    receiveDoc(serverChannel, nomeDoc+sezione, dimension);
                    logged.setShowLabel("DOCUMENTO SCARICATO CON SUCCESSO", Color.green);
                    break;
                }
                
                
            }
            case "edit":
            {
                String doc = logged.getEditNameField();
                String s = logged.getEditNumField();
                
                if(doc.equals("") || s.equals("")){
                    logged.setEditLabel("CAMPI NON VALIDI", Color.red);
                    break;
                }
                int sezione;
                try{sezione = Integer.parseInt(s);}
                    catch(Exception exc){
                        logged.setEditLabel("SEZIONE NON VALIDA", Color.red);
                        break;
                    }
                Packet req = new Packet(typePack.EDIT);
                req.addCampo("doc", doc);
                req.addCampo("section", Integer.toString(sezione));
                req.writePacket(toServer);
                
                req.readPacket(fromServer);
                typePack tipo = req.getType();
                if(tipo.equals(typePack.OP_ERR))
                {
                    logged.setEditLabel("FILE INESISTENTE", Color.red);
                    break;
                }
                else if(tipo.equals(typePack.OP_ERR_SECTION))
                {
                    logged.setEditLabel("QUALCUNO STA EDITANDO", Color.red);
                    break;
                }
                else if(tipo.equals(typePack.OP_ERR_NOT_INVITED))
                {
                    logged.setEditLabel("NON SEI TRA GLI INVITATI", Color.red);
                    break;
                }
                
                Long dimension = Long.parseLong(req.getCampo("dim"));
                receiveDoc(serverChannel, doc+sezione, dimension);
                String addr = req.getCampo("address");
                
                //mi servono per la "end-edit"
                inEditingDoc = doc;
                inEditingSec = sezione;
                
                editing = new Editing(this);
                logged.hide();
                editing.show();
                
                chatSocket= new DatagramSocket();
                group = InetAddress.getByName(addr);
                chatThread = new Thread(new ChatThread(addr,editing));   //todo chat
                chatThread.start();
                break;
            }
            
            case "send-message":
            {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String time ="["+sdf.format(cal.getTime())+"]";
                byte[] msg = (time+" "+username+" :   "+editing.getMessageTextArea()).getBytes();
                DatagramPacket packet = new DatagramPacket(msg, msg.length, group, Configuration.CLIENT_CHAT_PORT);
                chatSocket.send(packet);
                break;
            }
            case "end-edit" :
            {
                Packet req = new Packet(typePack.END_EDIT);
                req.addCampo("doc", inEditingDoc);
                req.addCampo("section", Integer.toString(inEditingSec));
                
                Long dimension = Files.size(Paths.get(Configuration.CLIENT_DOCS_DIRECTORY_NAME+"/"+inEditingDoc+inEditingSec+".txt"));
                req.addCampo("dimension",Long.toString(dimension));
                
                sendSection(serverChannel, inEditingSec, inEditingDoc);
                req.writePacket(toServer);
                
                req.readPacket(fromServer);  //todo fallimento
                
                //todo invio richiesta, invio file al server
                chatSocket.close();
                group = null;
                editing.dispose();
                chatThread.interrupt();
                logged.show();
                
                
                
                break;
            }
            
            
    }
    
    }catch(Exception ex){ex.printStackTrace();}
    }
    
    //legge dim byte dalla socket e crea il file nella cartella predefinita
    private void receiveDoc(SocketChannel ch, String nomeDoc, Long dimension) throws FileNotFoundException, IOException
    {
          Path path = Paths.get(Configuration.CLIENT_DOCS_DIRECTORY_NAME+"/"+nomeDoc+".txt");
          try{
              Files.createFile(path);
          }catch(FileAlreadyExistsException ex){
                Files.delete(path);
                Files.createFile(path);
                }
          FileOutputStream fis = new FileOutputStream(Configuration.CLIENT_DOCS_DIRECTORY_NAME+"/"+nomeDoc+".txt");
          FileChannel fileCh = fis.getChannel();
          fileCh.transferFrom(ch, 0, dimension);
          fileCh.close();
          fis.close();
    }
    
    private void sendSection(SocketChannel channel, int section, String docName) throws FileNotFoundException, IOException
    {
        System.out.println("send section: " +channel.toString()+section+docName);
        FileInputStream fis = new FileInputStream(Configuration.CLIENT_DOCS_DIRECTORY_NAME+"/"+docName+Integer.toString(section)+".txt");
        FileChannel ch = fis.getChannel();
        System.out.println(ch.toString());
        ch.transferTo(0, ch.size(), channel);
        ch.close();
        fis.close();
    }
    
    
}
            
    
    

