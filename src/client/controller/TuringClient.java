/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import common.RegistrationInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import common.Configuration;
import client.view.Login;

/**
 *
 * @author Giacomo
 */
public class TuringClient {

    
    public static void main(String[] args) {
        
       Registry reg = null;
       RegistrationInterface serverInterface = null;
       try
       {
           reg = LocateRegistry.getRegistry(Configuration.SERVER_NAME,Configuration.SERVER_RMI_PORT);
           serverInterface = (RegistrationInterface)reg.lookup(Configuration.SERVER_RMI_NAME);
       }catch(Exception e){e.printStackTrace();}
       
        
       ControlListener controlListener = new ControlListener(serverInterface);
    }
    
}