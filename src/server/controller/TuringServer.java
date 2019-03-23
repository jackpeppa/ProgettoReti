/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package server.controller;

import server.model.DataBase;
import common.Configuration;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NoSuchObjectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author Giacomo
 */
public class TuringServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ThreadPoolExecutor threadpool = null;
        ServerSocket listener = null;   //accettare richieste dai client
        
        RegistrationImpl registration= null;  //server rmi
        
        DataBase db = null;  //struttura dati principale
        
        db = DataBase.getInstance(); 
        
        try
        {   
            System.setProperty("java.rmi.server.hostname", Configuration.SERVER_NAME);
            registration = new RegistrationImpl(db);
            UnicastRemoteObject.exportObject(registration, 0);
            
            LocateRegistry.createRegistry(Configuration.SERVER_RMI_PORT);
            Registry registry = LocateRegistry.getRegistry(Configuration.SERVER_RMI_PORT);
            
            registry.rebind(Configuration.SERVER_RMI_NAME, registration);
            
            listener = new ServerSocket(Configuration.SERVER_TCP_PORT);
            
            threadpool =(ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.N_THREADS);
            try{
            Files.createDirectory(Paths.get(Configuration.DOCS_DIRECTORY_NAME));  //dove salvo i files
            }catch(FileAlreadyExistsException ex){}
            
            System.out.println("TURING SERVER READY");
            
            while(!(Thread.currentThread().isInterrupted()))
            {
                Socket client = listener.accept();
                
                
                threadpool.execute(new RequestHandlerThread(client));
                
            }
            
            
            
        }catch(Exception e){e.printStackTrace();}
        finally
        {
            threadpool.shutdownNow();
            try{
                UnicastRemoteObject.unexportObject(registration, true);
            }catch(NoSuchObjectException e){e.printStackTrace();}
            
            try{listener.close();}
            catch(IOException e){ e.printStackTrace();}
            System.out.println("SERVER TERMINATO");
        }
    }
    
}
