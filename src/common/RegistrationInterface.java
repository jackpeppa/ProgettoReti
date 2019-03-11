/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Giacomo
 */
public interface RegistrationInterface extends Remote{
    
    public Boolean register(String nome, String password) throws RemoteException;
    
}
