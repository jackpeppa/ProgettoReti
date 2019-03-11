/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import common.RegistrationInterface;
import java.awt.Color;
import client.view.Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import common.Configuration;
import common.Packet;
import common.typePack;
import client.view.Logged;

/**
 *
 * @author Giacomo
 */
public class ControlListener implements ActionListener {
    
    private Login login;
    private Logged logged;
    private RegistrationInterface serverInterface;
    private Socket server;
    private Thread listingThread;
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private String username="";          //nome utente
    
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
                    login.setlabelLog("CAMPI VALIDI", Color.green);
                    login.dispose();
                    logged = new Logged(this);
                    logged.show();
                    listingThread = new Thread(new ListingThread(logged, Integer.parseInt((String)req.getCampo("porta"))));
                    listingThread.start();
                    
                }
                else
                    login.setlabelLog("CAMPI NON VALIDI", Color.red);
                
                
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
            
            
    }
    
    }catch(Exception ex){ex.printStackTrace();}
    }
}
            
    
    

