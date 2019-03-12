/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editors.
 */
package common;

/**
 *
 * @author Giacomo
 */
public class Configuration {
    public static final String SERVER_NAME="localhost";
    public static final int SERVER_RMI_PORT= 1099;
    public static final String SERVER_RMI_NAME = "TURING";
    
    public static final int SERVER_TCP_PORT=9998;  //porta principale
    public static final int INVITE_TCP_PORT=9999;   //porta inviti
    
    public static final String DOCS_DIRECTORY_NAME = "./turing_docs";  //dove salvo i documenti sul server
    
    public static final String CLIENT_DOCS_DIRECTORY_NAME = "./client_turing_docs"; //dove salvo i documenti sul client
    
    public static final int N_THREADS=10;
}
