/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package server.controller;

import common.typePack;
import common.Packet;
import server.model.DataBase;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import common.Configuration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Giacomo
 */
public class RequestHandlerThread implements Runnable {
    private Boolean flag = true;   //terminazione client
    private Socket requestSock;    //scambio file e richieste
    private Socket notifySock;     //per inviare notifiche inviti
    private DataInputStream fromClient;
    private DataOutputStream toClient;
    private DataOutputStream toClientNotify;
    private String username = "";      //username client loggato
    private SocketChannel fileChannel; //channel per inviare i file
    
    public RequestHandlerThread (Socket client) 
    {
        
        this.requestSock = client;
        
        
    }

    @Override
    public void run() {
        try
        {
            
            fromClient = new DataInputStream(new BufferedInputStream(requestSock.getInputStream()));
            toClient = new DataOutputStream(new BufferedOutputStream(requestSock.getOutputStream()));
            
            
            do
            {
               
               Packet request = new Packet(); //pacchetto da leggere
            
               Packet response = new Packet();  //pacchetto risposta
               
               request.readPacket(fromClient);
                
               typePack type = request.getType();
                
               switch(type)
               {
                   case LOGIN:
                   {
                       username = request.getCampo("username");
                       String password = request.getCampo("password");
                       Boolean ok = DataBase.setOnline(username, password);
                       
                       if(ok)
                       {
                            response.setType(typePack.OP_OK);
                           
                            ServerSocket sock = new ServerSocket(0);  //porta casuale libera
                            ServerSocketChannel sockChannel = ServerSocketChannel.open();
                            sockChannel.socket().bind(new InetSocketAddress(Configuration.SERVER_NAME, 0));
                            int chPort = sockChannel.socket().getLocalPort();
                            int port = sock.getLocalPort();
                            response.addCampo("porta", Integer.toString(port));
                            response.addCampo("filePort", Integer.toString(chPort));

                            response.writePacket(toClient);

                            sock.setSoTimeout(5000);
                            sockChannel.socket().setSoTimeout(5000);
                            notifySock = sock.accept();
                            fileChannel = sockChannel.accept();
                            
                            sock.close();
                            sockChannel.close();
                            
                            toClientNotify = new DataOutputStream(new BufferedOutputStream(notifySock.getOutputStream()));
                            DataBase.setInvitesStream(username, toClientNotify);
                            

                            sendInvites(toClientNotify, username);
                           
                            System.out.println("controller.RequestHandlerThread.run(): "+username+" è online");
                       }
                       else
                       {
                           response.setType(typePack.OP_ERR);
                           response.writePacket(toClient);
                           System.out.println("controller.RequestHandlerThread.run(): "+username+" ha sbagliato campi");
                       }
                       
                       
                       
                       break;
                   }
                   
                   case LOGOUT:
                   {
                       if(username!=null)
                       {
                           DataBase.setOffline(username);
                       }
                       flag =false; //termino
                       username ="";
                       
                       break;
                   }
                   
                   case CREATE:
                   {
                       String nome = request.getCampo("name");
                       int sezioni = Integer.parseInt(request.getCampo("sections"));
                       
                       Boolean resp = DataBase.addDocumento(nome, username, sezioni);
                       
                       if(resp)
                       {
                           try{
                           response.setType(typePack.OP_OK);
                           Files.createDirectory(Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+nome));
                           for(int i =0; i< sezioni; i++)
                               Files.createFile(Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+nome+"/"+Integer.toString(i)+".txt"));
                           }catch(FileAlreadyExistsException ex){}
                       }
                       else
                       {
                           response.setType(typePack.OP_ERR);
                       }
                       
                       response.writePacket(toClient);
                       
                       if(resp)
                           sendInvites(toClientNotify, username);
                           
                       break;
                   }
                   
                   case SHARE:
                   {
                       String nomeDoc = request.getCampo("doc");
                       String nomeUtente = request.getCampo("username");
                       Boolean resp= false;
                       
                       if(!(nomeUtente.equals(username)))
                           resp = DataBase.addAmmesso(nomeDoc, nomeUtente, username);
                      
                       if(resp)
                       {
                           response.setType(typePack.OP_OK);
                           sendInvites(toClientNotify, username);
                           sendInvites(DataBase.getInvitesStream(nomeUtente), nomeUtente);
                       }
                       else
                           response.setType(typePack.OP_ERR);
                       response.writePacket(toClient);
                       break;
                   }
                   
                   case SHOW_DOC:
                   {
                       String doc = request.getCampo("doc");
                       String editors = DataBase.getListOfSectionsInEditing(doc);
                       if(editors==null)  //se il doc non esiste
                       {
                           response.setType(typePack.OP_ERR);
                           response.writePacket(toClient);
                           break;
                       }
                       response.setType(typePack.OP_OK);
                       response.addCampo("editors", editors);
                       
                       int numOfSections= DataBase.getNumOfSections(doc);
                       long dim=0;
                       for(int i = 0; i< numOfSections;i++)  //calcolo dimensione documento
                       {
                           dim += Files.size(Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+doc+"/"+Integer.toString(i)+".txt"));
                       }
                       response.addCampo("dim", Long.toString(dim));
                       
                       response.writePacket(toClient);
                       
                       for(int i =0; i< numOfSections; i++)  //invio il documento
                       {
                           sendSection(fileChannel, i, doc);
                       }
                       
                       break;
                   }
                   
                   case SHOW_SEC:
                   {
                       
                   }
                   
                   case EDIT:
                   {
                       
                   }
                   
                   case END_EDIT:
                   {
                       
                   }
                   
                   
                   
                   
                   
                   
               }
                




            }while(flag);
        }catch(Exception e){e.printStackTrace();}
        finally
        {
            try
            {
                fromClient.close();
                toClient.close();
                toClientNotify.close();
                requestSock.close();
                notifySock.close();
                fileChannel.close();
                
                DataBase.rilasciaSezioni(username);
                
            }catch(Exception e){e.printStackTrace();}
        }
    }
    
    private void sendInvites(DataOutputStream toclient, String username) throws IOException
    {
        if(toclient == null)
            return;
        Packet res = new Packet();
        String list = DataBase.getList(username);
        res.addCampo("list", list);
        synchronized(toclient)          //usato da piu thread
        {
            res.writePacket(toclient);
        }
        
    }
    
    //invia sezione al client con NIO
    private void sendSection(SocketChannel channel, int section, String docName) throws FileNotFoundException, IOException
    {
        System.out.println("send section: " +channel.toString()+section+docName);
        FileInputStream fis = new FileInputStream(Configuration.DOCS_DIRECTORY_NAME+"/"+docName+"/"+Integer.toString(section)+".txt");
        FileChannel ch = fis.getChannel();
        System.out.println(ch.toString());
        ch.transferTo(0, ch.size(), channel);
        ch.close();
        fis.close();
    }
    
    
    
    
    
}
