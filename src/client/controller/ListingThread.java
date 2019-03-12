/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package client.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import common.Configuration;
import common.Packet;
import client.view.Logged;

/**
 *
 * @author Giacomo
 */
public class ListingThread implements Runnable {
    
    private Socket server;
    private Logged logged;
    private DataInputStream fromServer;
    
    public ListingThread(Logged logged,int port) throws IOException
    {
        server = new Socket(Configuration.SERVER_NAME, port);
        this.logged = logged;
        this.fromServer = new DataInputStream(new BufferedInputStream(server.getInputStream()));
        
    }
    

    @Override
    public void run() {
        Packet invites = new Packet();
        try{
        do
        {
            invites.readPacket(fromServer);
            String res = invites.getCampo("list");
            logged.setDocTextArea(res);
            
            
            
        }while(!Thread.currentThread().isInterrupted());
        }catch(Exception ex){}  //solleva eof quando il server chiude la connessione,non è significativa
        finally
        {
            try
            {
                fromServer.close();
                server.close();
            }catch(Exception e){e.printStackTrace();}
        }
        
    }
    
}
