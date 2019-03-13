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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import common.Configuration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

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
                           DataBase.rilasciaSezioni(username);
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
                           
                           response.setType(typePack.OP_OK);
                           try{
                           Files.createDirectory(Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+nome));
                           }catch(FileAlreadyExistsException ex){}
                           for(int i =0; i< sezioni; i++)
                           {
                               Path path = Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+nome+"/"+Integer.toString(i)+".txt");
                                try{
                                    Files.createFile(path);
                                    }catch(FileAlreadyExistsException ex){
                                        Files.delete(path);
                                        Files.createFile(path);
                                    }
                           }
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
                       String doc = request.getCampo("doc");
                       int sezione = Integer.parseInt(request.getCampo("section"));
                       String editor = DataBase.getEditor(doc, sezione);
                       if(editor==null)  //doc o sezione non esiste
                       {
                           response.setType(typePack.OP_ERR);
                           response.writePacket(toClient);
                           break;
                       }
                       response.setType(typePack.OP_OK);
                       response.addCampo("editor", editor);
                       
                       Long dim = Files.size(Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+doc+"/"+sezione+".txt"));
                       response.addCampo("dim", Long.toString(dim));
                       
                       sendSection(fileChannel, sezione, doc);
                       response.writePacket(toClient);
                       
                       break;  
                   }
                   
                   case EDIT:
                   {
                       String doc = request.getCampo("doc");
                       int sezione = Integer.parseInt(request.getCampo("section"));
                       int resp = DataBase.editDocumento(username, doc, sezione);
                       switch (resp)
                       {
                           case 1:   //file non esistente
                           {
                               response.setType(typePack.OP_ERR);
                               break;
                           }
                           case 2:   //già in editing
                           {
                               response.setType(typePack.OP_ERR_SECTION);
                               break;
                           }
                           case 3:   //utente non invitato
                           {
                               response.setType(typePack.OP_ERR_NOT_INVITED);
                               break;
                           }
                       }
                       if(resp != 0)
                       {
                           response.writePacket(toClient);
                           break;
                       }
                       
                       response.setType(typePack.OP_OK);
                       response.addCampo("address",DataBase.getMulticastAddr(doc));     //todo address
                       
                       Long dim = Files.size(Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+doc+"/"+sezione+".txt"));
                       response.addCampo("dim", Long.toString(dim));
                       
                       sendSection(fileChannel, sezione, doc);
                       response.writePacket(toClient);
                       
                       break;
                   }
                   
                   case END_EDIT:
                   {
                       String nomeDoc = request.getCampo("doc");
                       int sezione = Integer.parseInt(request.getCampo("section"));
                       Long dim = Long.parseLong(request.getCampo("dimension"));
                       receiveSection(fileChannel, sezione, nomeDoc, dim);
                       DataBase.endEditDocumento(nomeDoc, sezione);
                       response.setType(typePack.OP_OK);
                       response.writePacket(toClient);
                       break;
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
                DataBase.setOffline(username);
                
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
    
    private void receiveSection(SocketChannel channel, int section, String docName, Long dimension) throws IOException
    {
        Path path = Paths.get(Configuration.DOCS_DIRECTORY_NAME+"/"+docName+"/"+Integer.toString(section)+".txt");
        Files.delete(path);
        Files.createFile(path);
        FileOutputStream fis = new FileOutputStream(Configuration.DOCS_DIRECTORY_NAME+"/"+docName+"/"+Integer.toString(section)+".txt");
        FileChannel ch = fis.getChannel();
        ch.transferFrom(channel, 0, dimension);
        ch.close();
        fis.close();
    }
}
