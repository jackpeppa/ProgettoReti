/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import common.RegistrationInterface;
import server.model.DataBase;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;

/**
 *
 * @author Giacomo
 */
public class RegistrationImpl extends RemoteObject implements RegistrationInterface {
    
    private DataBase db;
    
    public RegistrationImpl(DataBase db)
    {
        this.db=db;
    }

    @Override
    public Boolean register(String nome, String password) throws RemoteException {
        return db.addUtente(nome,password);
    }
    
    
    
}
